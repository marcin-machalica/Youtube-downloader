package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;

import com.sapher.youtubedl.YoutubeDLException;

import machalica.marcin.spring.ytdownloader.helpers.YtFileInfo;

public interface YtDownloaderDao {
	YtFileInfo getFileInfo(String videoUrl) throws YoutubeDLException;
	File downloadFile(String videoUrl, String format) throws YoutubeDLException;
}
