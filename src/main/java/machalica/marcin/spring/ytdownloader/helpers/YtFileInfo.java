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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		result = prime * result + ((formats == null) ? 0 : formats.hashCode());
		result = prime * result + ((qrCodes == null) ? 0 : qrCodes.hashCode());
		result = prime * result + ((thumbnailUrl == null) ? 0 : thumbnailUrl.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((videoUrlPart == null) ? 0 : videoUrlPart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YtFileInfo other = (YtFileInfo) obj;
		if (error == null) {
			if (other.error != null)
				return false;
		} else if (!error.equals(other.error))
			return false;
		if (formats == null) {
			if (other.formats != null)
				return false;
		} else if (!formats.equals(other.formats))
			return false;
		if (qrCodes == null) {
			if (other.qrCodes != null)
				return false;
		} else if (!qrCodes.equals(other.qrCodes))
			return false;
		if (thumbnailUrl == null) {
			if (other.thumbnailUrl != null)
				return false;
		} else if (!thumbnailUrl.equals(other.thumbnailUrl))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (videoUrlPart == null) {
			if (other.videoUrlPart != null)
				return false;
		} else if (!videoUrlPart.equals(other.videoUrlPart))
			return false;
		return true;
	}

}