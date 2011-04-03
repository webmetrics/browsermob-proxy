package cz.mallat.uasparser.fileparser;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaBean that holds an entry from a parsed file
 * 
 * @author oli
 */
public class Entry {

	private String key;
	private List<String> data = new ArrayList<String>();

	public Entry(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

}
