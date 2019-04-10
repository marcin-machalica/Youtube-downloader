package machalica.marcin.spring.ytdownloader.downloader;

import java.util.LinkedHashMap;

public class YtDownloadFile {
	private static final YtDownloadFile EMPTY_YT_DOWNLOAD_FILE = new YtDownloadFile(new LinkedHashMap<String, String>(), new LinkedHashMap<String, String>(), "", "", "");
	private final LinkedHashMap<String, String> formats;
	private final LinkedHashMap<String, String> qrCodes;
	private final String url;
	private final String title;
	private final String thumbnailUrl;

	public YtDownloadFile(LinkedHashMap<String, String> formats, LinkedHashMap<String, String> qrCodes, String url, String title, String thumbnailUrl) {
		this.formats = formats;
		this.qrCodes = qrCodes;
		this.url = url;
		this.title = title;
		this.thumbnailUrl = thumbnailUrl;
	}

	public LinkedHashMap<String, String> getFormats() {
		return formats;
	}
	
	public LinkedHashMap<String, String> getQrCodes() {
		return qrCodes;
	}

	public String getUrl() {
		return url;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public static YtDownloadFile getEmptyYtDownloadFile() {
		return EMPTY_YT_DOWNLOAD_FILE;
	}

}
