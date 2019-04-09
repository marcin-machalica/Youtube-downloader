package machalica.marcin.spring.ytdownloader.downloader;

import java.util.List;

public interface YtDownloaderDao {
	List<String> getAvaibleFormats(String videoUrl);
	boolean downloadFile(String videoUrl, String format);
}
