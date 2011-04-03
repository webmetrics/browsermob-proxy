package cz.mallat.uasparser;

import java.util.Iterator;
import java.util.List;

/**
 * JavaBean that holds the data from the [browser] section in the data file
 * 
 * @author oli
 * 
 */
class BrowserEntry {

	private Long type;
	private String family;
	private String name;
	private String url;
	private String company;
	private String companyUrl;
	private String ico;
	private String infoUrl;

	public BrowserEntry(List<String> data) {
		Iterator<String> it = data.iterator();
		this.type = Long.parseLong(it.next());
		this.family = it.next();
		this.url = it.next();
		this.company = it.next();
		this.companyUrl = it.next();
		this.ico = it.next();
		this.infoUrl = it.next();
		// this.name stays empty, will be filled with family + version
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyUrl() {
		return companyUrl;
	}

	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getInfoUrl() {
		return infoUrl;
	}

	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}

	public void copyTo(UserAgentInfo uai, String browserVersionInfo) {
		if (Utils.validString(getFamily())) {
			uai.setUaFamily(getFamily());
			uai.setUaName(uai.getUaFamily()
							+ (browserVersionInfo != null && !browserVersionInfo.isEmpty() ? " " + browserVersionInfo : ""));
		}
		if (Utils.validString(getUrl()))
			uai.setUaUrl(getUrl());
		if (Utils.validString(getCompany()))
			uai.setUaCompany(getCompany());
		if (Utils.validString(getCompanyUrl()))
			uai.setUaCompanyUrl(getCompanyUrl());
		if (Utils.validString(getIco()))
			uai.setUaIcon(getIco());
		if (Utils.validString(getInfoUrl()))
			uai.setUaInfoUrl(UASparser.INFO_URL + getInfoUrl());
	}

}