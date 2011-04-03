package cz.mallat.uasparser.fileparser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Emulates the behavior of the php function "parse_ini_file".
 * 
 * Does NOT support all features of the php function.
 * 
 * @author oli
 */
public class PHPFileParser {

	private List<Section> sections;

	public PHPFileParser(InputStream is) throws IOException {
		loadFile(new InputStreamReader(is));
	}

	public PHPFileParser(Reader reader) throws IOException {
		loadFile(reader);
	}

	public PHPFileParser(File file) throws IOException {
		Reader reader = new FileReader(file);
		try {
			loadFile(reader);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadFile(Reader reader) throws IOException {
		this.sections = new ArrayList<Section>();

		BufferedReader bufferedReader = new BufferedReader(reader);

		int unnamedSectionCounter = 0;

		Section currentSection = null;
		Entry currentEntry = null;

		String line = bufferedReader.readLine();
		while (line != null) {
			if (line.trim().startsWith(";")) {
				// comment, do nothing
			} else if (line.trim().startsWith("[") && line.trim().endsWith("]")) {
				String rawLine = line.trim();
				String sectionName = rawLine.substring(1, rawLine.length() - 1);
				currentSection = new Section(sectionName);
				sections.add(currentSection);
			} else {
				if (currentSection == null) {
					currentSection = new Section("unname section" + (++unnamedSectionCounter));
					sections.add(currentSection);
				}

				int indexOfEquals = line.indexOf('=');
				String key = line.substring(0, indexOfEquals);
				String data = line.substring(indexOfEquals + 1);
				key = key.replace('[', ' ');
				key = key.replace(']', ' ');
				key = key.trim();
				data = data.trim();
				if (data.startsWith("\"") && data.endsWith("\"")) {
					data = data.substring(1, data.length() - 1);
				}

				if (currentEntry == null || !currentEntry.getKey().equals(key)) {
					currentEntry = new Entry(key);
					currentSection.getEntries().add(currentEntry);
				}

				currentEntry.getData().add(data);
			}

			line = bufferedReader.readLine();
		}

	}

	public List<Section> getSections() {
		return sections;
	}

}
