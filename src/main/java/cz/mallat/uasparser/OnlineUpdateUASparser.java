package cz.mallat.uasparser;

import cz.mallat.uasparser.fileparser.PHPFileParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * The parser will download the definition file from the internet
 * 
 * @author oli
 */
public class OnlineUpdateUASparser extends UASparser {

	protected static final String DATA_RETRIVE_URL = "http://user-agent-string.info/rpc/get_data.php?key=free&format=ini";
	protected static final String VERSION_CHECK_URL = "http://user-agent-string.info/rpc/get_data.php?key=free&format=ini&ver=y";
	protected static final long UPDATE_INTERVAL = 1000 * 60 * 60 * 24; // 1 day

	protected long lastUpdateCheck;
	protected String currentVersion;

	/**
	 * Since we've online access to the data file, we check every day for an update
	 */
	@Override
	protected synchronized void checkDataMaps() throws IOException {
		if (lastUpdateCheck == 0 || lastUpdateCheck < System.currentTimeMillis() - UPDATE_INTERVAL) {
			String versionOnServer = getVersionFromServer();
			if (currentVersion == null || versionOnServer.compareTo(currentVersion) > 0) {
				loadDataFromInternet();
				currentVersion = versionOnServer;
			}
			lastUpdateCheck = System.currentTimeMillis();
		}
	}

	/**
	 * Loads the data file from user-agent-string.info
	 * 
	 * @throws IOException
	 */
	private void loadDataFromInternet() throws IOException {
		URL url = new URL(DATA_RETRIVE_URL);
		InputStream is = url.openStream();
		try {
			PHPFileParser fp = new PHPFileParser(is);
			createInternalDataStructre(fp.getSections());
		} finally {
			is.close();
		}
	}

	/**
	 * Gets the current version from user-agent-string.info
	 * 
	 * @return
	 * @throws IOException
	 */
	protected String getVersionFromServer() throws IOException {
		URL url = new URL(VERSION_CHECK_URL);
		InputStream is = url.openStream();
		try {
			byte[] buff = new byte[4048];
			int len = is.read(buff);
			return new String(buff, 0, len);
		} finally {
			is.close();
		}
	}

}
