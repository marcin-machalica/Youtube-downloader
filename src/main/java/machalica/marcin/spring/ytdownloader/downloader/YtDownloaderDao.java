package machalica.marcin.spring.ytdownloader.downloader;

import java.util.HashMap;
import java.util.List;

import com.sapher.youtubedl.YoutubeDLException;

public interface YtDownloaderDao {
	HashMap<String, String> getAvaibleFormats(String videoUrl) throws YoutubeDLException;
	boolean downloadFile(String videoUrl, String format);
}
