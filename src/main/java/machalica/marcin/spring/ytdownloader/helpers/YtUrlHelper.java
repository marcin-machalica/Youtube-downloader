package machalica.marcin.spring.ytdownloader.helpers;

public class YtUrlHelper {
	public static String getFullYtUrl(String videoUrlPart) {
		return "https://www.youtube.com/watch?v=" + videoUrlPart;
	}

	public static String getVideoUrlPart(String url) {
		if (url.contains("watch?v=")) {
			return url.substring(url.lastIndexOf("watch?v=") + 8);
		} else {
			return url;
		}
	}
}
