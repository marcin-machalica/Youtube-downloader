package machalica.marcin.spring.ytdownloader.helpers;

import java.util.LinkedHashMap;

public class YtFileInfo {
	private static final YtFileInfo EMPTY_YT_FILE_INFO = new YtFileInfo(new LinkedHashMap<String, String>(),
			new LinkedHashMap<String, String>(), "", "", "");
	private final LinkedHashMap<String, String> formats;
	private final LinkedHashMap<String, String> qrCodes;
	private final String url;
	private final String videoUrlPart;
	private final String title;
	private final String thumbnailUrl;
	private String error = "";

	public YtFileInfo(LinkedHashMap<String, String> formats, LinkedHashMap<String, String> qrCodes, String url,
			String title, String thumbnailUrl) {
		this.formats = formats;
		this.qrCodes = qrCodes;
		this.url = url;
		this.videoUrlPart = YtUrlHelper.getVideoUrlPart(url);
		this.title = title;
		this.thumbnailUrl = thumbnailUrl;
		this.error = "";
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

	public String getVideoUrlPart() {
		return videoUrlPart;
	}

	public String getTitle() {
		return title;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public static YtFileInfo getEmptyYtFileInfo() {
		EMPTY_YT_FILE_INFO.setError("");
		return EMPTY_YT_FILE_INFO;
	}

	public boolean isDataPresent() {
		if (url.isEmpty() || title.isEmpty() || thumbnailUrl.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

}