package cz.mallat.uasparser;

/**
 * JavaBean that returns the data to the calling user from UAParser.parse()
 * 
 * @author oli
 * 
 */
public class UserAgentInfo {

	private String typ;
	private String uaFamily;
	private String uaName;
	private String uaUrl;
	private String uaCompany;
	private String uaCompanyUrl;
	private String uaIcon;
	private String uaInfoUrl;
	private String osFamily;
	private String osName;
	private String osUrl;
	private String osCompany;
	private String osCompanyUrl;
	private String osIcon;

	public UserAgentInfo() {
		this.typ = "unknown";
		this.uaFamily = "unknown";
		this.uaName = "unknown";
		this.uaUrl = "unknown";
		this.uaCompany = "unknown";
		this.uaCompanyUrl = "unknown";
		this.uaIcon = "unknown";
		this.uaInfoUrl = "unknown";
		this.osFamily = "unknown";
		this.osName = "unknown";
		this.osUrl = "unknown";
		this.osCompany = "unknown";
		this.osCompanyUrl = "unknown";
		this.osIcon = "unknown";
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getUaFamily() {
		return uaFamily;
	}

	public void setUaFamily(String uaFamily) {
		this.uaFamily = uaFamily;
	}

	public String getUaName() {
		return uaName;
	}

	public void setUaName(String uaName) {
		this.uaName = uaName;
	}

	public String getUaUrl() {
		return uaUrl;
	}

	public void setUaUrl(String uaUrl) {
		this.uaUrl = uaUrl;
	}

	public String getUaCompany() {
		return uaCompany;
	}

	public void setUaCompany(String uaCompany) {
		this.uaCompany = uaCompany;
	}

	public String getUaCompanyUrl() {
		return uaCompanyUrl;
	}

	public void setUaCompanyUrl(String uaCompanyUrl) {
		this.uaCompanyUrl = uaCompanyUrl;
	}

	public String getUaIcon() {
		return uaIcon;
	}

	public void setUaIcon(String uaIcon) {
		this.uaIcon = uaIcon;
	}

	public String getOsFamily() {
		return osFamily;
	}

	public void setOsFamily(String osFamily) {
		this.osFamily = osFamily;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsUrl() {
		return osUrl;
	}

	public void setOsUrl(String osUrl) {
		this.osUrl = osUrl;
	}

	public String getOsCompany() {
		return osCompany;
	}

	public void setOsCompany(String osCompany) {
		this.osCompany = osCompany;
	}

	public String getOsCompanyUrl() {
		return osCompanyUrl;
	}

	public void setOsCompanyUrl(String osCompanyUrl) {
		this.osCompanyUrl = osCompanyUrl;
	}

	public String getOsIcon() {
		return osIcon;
	}

	public void setOsIcon(String osIcon) {
		this.osIcon = osIcon;
	}

	public String getUaInfoUrl() {
		return uaInfoUrl;
	}

	public void setUaInfoUrl(String uaInfoUrl) {
		this.uaInfoUrl = uaInfoUrl;
	}

}