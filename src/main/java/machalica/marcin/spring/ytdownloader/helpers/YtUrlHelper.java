package machalica.marcin.spring.ytdownloader.helpers;

public class YtUrlHelper {
	public static String getFullYtUrl(String videoUrlPart) {
		if (videoUrlPart == null) {
			return "https://www.youtube.com/watch?v=";
		}

		return "https://www.youtube.com/watch?v=" + videoUrlPart;
	}

	public static String getVideoUrlPart(String url) {
		if (url == null) {
			return "";
		}

		if (url.contains("https://www.youtube.com/watch?v=")) {
			String videoUrlPart = url.substring(url.indexOf("https://www.youtube.com/watch?v=") + 32);
			if (!videoUrlPart.contains("/")) {
				return videoUrlPart;
			}
		} 
		
		return url;
	}
}