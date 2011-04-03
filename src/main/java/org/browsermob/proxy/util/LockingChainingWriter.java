package org.browsermob.proxy.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class LockingChainingWriter extends ChainableWriter {
    private ReentrantLock lock = new ReentrantLock();
    private File file;

    public LockingChainingWriter(File file) throws IOException {
        super(new FileWriter(file));
        this.file = file;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public void closeSafely() throws IOException {
        try {
            lock();
            close();
        } finally {
            unlock();
        }
    }

    public void delete() {
        file.delete();
    }

    public File getFile() {
        return file;
    }
}
