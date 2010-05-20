package org.browsermob.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.lang.IllegalArgumentException;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/** Keep track of what the proxy server is doing.*/
@ThreadSafe
public class ProxyServerLog {
    @GuardedBy("this")
    private List<Block> recentBlocks = new ArrayList<Block>();
    private int maxRecentBlocks = 100;
    @GuardedBy("this")
    private Map<String,List<Block>> namedBlocks;
    @GuardedBy("this")
    private Set<String> activeNamesBlocks;

    public ProxyServerLog(int maxRecentBlocks) {
        this.maxRecentBlocks = maxRecentBlocks;
        namedBlocks = new HashMap<String,List<Block>>();
        activeNamesBlocks = new HashSet<String>();
    }


    public synchronized void clearRecentBlocks() {
        recentBlocks.clear();
    }


    public synchronized List<Block> getRecentBlocks() {
        return new ArrayList<Block>(recentBlocks);
    }


    public synchronized List<Block> getLastNRecentBlocks(int n) {
        return new ArrayList<Block>(recentBlocks.subList(0, n));
    }


    public synchronized void startNewNamedSession(String key) {
        if (activeNamesBlocks.contains(key)) {
            throw new IllegalArgumentException("session with key: " + key + "already exists");
        }
        activeNamesBlocks.add(key);
        namedBlocks.put(key, new ArrayList<Block>());
    }


    public synchronized void clearNamedSession(String key) {
        if (!namedBlocks.containsKey(key)) {
            throw new IllegalArgumentException("session with key: " + key + "does not exist");
        }
        activeNamesBlocks.remove(key);
        namedBlocks.remove(key);
    }


    public synchronized void stopSession(String key) {
        activeNamesBlocks.remove(key);
    }


    public synchronized List<Block> getNamedSession(String key) {
        if (!namedBlocks.containsKey(key)) {
            throw new IllegalArgumentException("session with key: " + key + "does not exist");
        }
        return namedBlocks.get(key);
    }



    private void recordBlocks(List<Block> blocks, HttpObject httpObject, int maxSize) {
        if (blocks.isEmpty()) {
            // put in the first block
            blocks.add(new Block());
        }
        
        Block block = blocks.get(0);
        if (!block.add(httpObject)) {
            Block newBlock = new Block();
            newBlock.add(httpObject);
            blocks.add(0, newBlock);

            if (maxSize > 0 &&  blocks.size() > maxSize) {
                blocks.remove(maxSize);
            }
        }
    }


    public synchronized void record(HttpObject httpObject) {
        recordBlocks(recentBlocks, httpObject, maxRecentBlocks);
        for (String key : activeNamesBlocks) {
            recordBlocks(namedBlocks.get(key), httpObject, -1);
        }
    }

}