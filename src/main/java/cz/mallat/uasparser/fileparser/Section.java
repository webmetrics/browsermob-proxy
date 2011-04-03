package cz.mallat.uasparser.fileparser;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaBean that holds a section from a parsed file. A section is a row in square brackets, e.g. [main]
 * 
 * @author oli
 */
public class Section {

	private String name;
	private List<Entry> entries;

	public Section(String sectionName) {
		this.name = sectionName;
		this.entries = new ArrayList<Entry>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

}
