package machalica.marcin.spring.ytdownloader.downloader;

public class YtUrlHelper {
	public static String getFullYtUrl(String videoUrlPart) {
		return "https://www.youtube.com/watch?v=" + videoUrlPart;
	}
	
	public static String getVideoUrlPart(String url) {
		return url.substring(url.lastIndexOf("watch?v=") + 8);
	}
}
