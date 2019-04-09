package machalica.marcin.spring.ytdownloader.downloader;

import java.util.HashMap;

public class YtDownloadFile {
	private static final YtDownloadFile EMPTY_YT_DOWNLOAD_FILE = new YtDownloadFile(new HashMap<String, String>(), "");
	private final HashMap<String, String> formats;
	private final String url;

	public YtDownloadFile(HashMap<String, String> formats, String url) {
		this.formats = formats;
		this.url = url;
	}

	public HashMap<String, String> getFormats() {
		return formats;
	}

	public String getUrl() {
		return url;
	}

	public static YtDownloadFile getEmptyYtDownloadFile() {
		return EMPTY_YT_DOWNLOAD_FILE;
	}

}
