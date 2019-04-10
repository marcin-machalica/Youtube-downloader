package machalica.marcin.spring.ytdownloader.downloader;

import java.util.LinkedHashMap;

public class YtDownloadFile {
	private static final YtDownloadFile EMPTY_YT_DOWNLOAD_FILE = new YtDownloadFile(new LinkedHashMap<String, String>(), "");
	private final LinkedHashMap<String, String> formats;
	private final String url;

	public YtDownloadFile(LinkedHashMap<String, String> formats2, String url) {
		this.formats = formats2;
		this.url = url;
	}

	public LinkedHashMap<String, String> getFormats() {
		return formats;
	}

	public String getUrl() {
		return url;
	}

	public static YtDownloadFile getEmptyYtDownloadFile() {
		return EMPTY_YT_DOWNLOAD_FILE;
	}

}
