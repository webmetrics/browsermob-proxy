package org.browsermob.proxy.http;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.browsermob.core.har.HarCookie;
import org.browsermob.core.har.HarNameValuePair;

import com.google.common.collect.Lists;

/**
 * A very basic cookie parser
 * @author dgomez
 *
 */
public class CookieHeadersParser {
	
	//rfc2616#section-3.3.1
	public static final DateFormat RFC882_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
	public static final DateFormat RFC850_DATE_FORMAT = new SimpleDateFormat("E, dd-MMM-yy HH:mm:ss z");
	public static final DateFormat ASCII_DATE_FORMAT = new SimpleDateFormat("EEE, MM  d HH:mm:ss yyyy");	

	public List<HarCookie> getCookies(HttpResponse response) {
		List<HarCookie> cookies = Lists.newLinkedList();
        for(Header hdr : response.getHeaders("Set-Cookie")) {            	     	            	
        	Map<String, String> cookieData = parseSetCookieHeader(hdr);
        	
        	HarCookie cookie = new HarCookie();
        	cookie.setName(cookieData.get("name"));
        	cookie.setValue(cookieData.get("value"));        	
        	cookie.setPath(cookieData.get("path"));
        	cookie.setDomain(cookieData.get("domain"));        	
        	
        	String expires = cookieData.get("expires");
        	if (expires != null) {
            	Date date = parseDate(expires);            	            	
            	cookie.setExpires(date);		
        	}
        	cookies.add(cookie);
        }
        return cookies;
	}
	
	public List<HarCookie> getCookies(HttpRequest request) {
		List<HarCookie> cookies = newLinkedList();
		for(Header hdr : request.getHeaders("Cookie")) {
			String[] pairs = hdr.getValue().split("; ");	
			for (String p : pairs) {
				HarNameValuePair pair = nameValuePair(p);
				HarCookie cookie = new HarCookie();
				cookie.setName(pair.getName());
				cookie.setValue(pair.getValue());
				cookies.add(cookie);
			}
		}
		return cookies;
	}
	
	private Map<String, String> parseSetCookieHeader(Header setCookieHdr) {
		String[] pairs= setCookieHdr.getValue().split("; ");
    	HarNameValuePair pair = nameValuePair(pairs[0]);
    	
    	Map<String, String> cookieData = newHashMap();
    	cookieData.put("name", pair.getName());
    	cookieData.put("value", pair.getValue());
    	
    	for (int i = 1; i < pairs.length; i++) {
    		pair = nameValuePair(pairs[i]);
        	cookieData.put(pair.getName(), pair.getValue());
    	}    	
    	return cookieData;
	}
	
	private HarNameValuePair nameValuePair(String data) {
		int eqIdx = data.indexOf("=");
		if (eqIdx > 0) {
			String name = data.substring(0, eqIdx);
			String val = data.substring(eqIdx + 1);
			return new HarNameValuePair(name, val);
		}
		else return new HarNameValuePair(data, "");
	}
	
	public Date parseDate(String dateString) {
		//try different formats to comply with rfc2616#section-3.3.1
		DateFormat[] formatters = {RFC882_DATE_FORMAT, RFC850_DATE_FORMAT, ASCII_DATE_FORMAT};
		for (DateFormat df : formatters) {
			try {
				return df.parse(dateString);
			} catch (ParseException e) {
				//ignore
			}
		}
		return null;
	}
}
