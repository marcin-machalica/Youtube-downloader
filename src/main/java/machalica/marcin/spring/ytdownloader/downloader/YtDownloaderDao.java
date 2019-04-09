package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;

import com.sapher.youtubedl.YoutubeDLException;

public interface YtDownloaderDao {
	YtDownloadFile getFileInfo(String videoUrl) throws YoutubeDLException;
	File downloadFile(String videoUrl, String format) throws YoutubeDLException;
}
