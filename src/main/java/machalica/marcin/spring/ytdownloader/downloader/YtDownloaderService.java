package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sapher.youtubedl.YoutubeDLException;

import machalica.marcin.spring.ytdownloader.helpers.LocalFileHelper;
import machalica.marcin.spring.ytdownloader.helpers.YoutubeDLHelper;
import machalica.marcin.spring.ytdownloader.helpers.YtFileInfo;
import machalica.marcin.spring.ytdownloader.helpers.YtUrlHelper;
import machalica.marcin.spring.ytdownloader.qrcodegenerator.QrCodeGenerator;

@Service
public class YtDownloaderService implements YtDownloaderDao {
	private static final Logger logger = Logger.getLogger(YtDownloaderService.class);
	private final YoutubeDLHelper youtubeDLHelper;
	private final QrCodeGenerator qrCodeGenerator;
	private final LocalFileHelper localFileHelper;

	@Autowired
	public YtDownloaderService(YoutubeDLHelper youtubeDLHelper, QrCodeGenerator qrCodeGenerator,
			LocalFileHelper localFileHelper) {
		this.youtubeDLHelper = youtubeDLHelper;
		this.qrCodeGenerator = qrCodeGenerator;
		this.localFileHelper = localFileHelper;
	}

	@Override
	public YtFileInfo getFileInfo(String videoUrl) throws YoutubeDLException {
		LinkedHashMap<String, String> formats = youtubeDLHelper.getAvaibleFormats(videoUrl);
		if (formats == null || formats.isEmpty()) {
			return YtFileInfo.getEmptyYtFileInfo();
		}

		LinkedHashMap<String, String> qrCodes = getQrCodes(videoUrl, formats);
		if (qrCodes == null || qrCodes.isEmpty()) {
			return YtFileInfo.getEmptyYtFileInfo();
		}

		String title = youtubeDLHelper.getTitle(videoUrl);
		if (title == null || title.isEmpty()) {
			return YtFileInfo.getEmptyYtFileInfo();
		}

		String thumbnailUrl = youtubeDLHelper.getThumbnailUrl(videoUrl);
		if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
			return YtFileInfo.getEmptyYtFileInfo();
		}

		return new YtFileInfo(formats, qrCodes, videoUrl, title, thumbnailUrl);
	}

	@Override
	public File downloadFile(String videoUrl, String format) throws YoutubeDLException {
		String dir = "./downloaded_files/";
		String fileName = youtubeDLHelper.getFileName(videoUrl, format);
		String filePath = dir.substring(2) + fileName;

		File file = new File(filePath);

		if (localFileHelper.doesFileExist(file)) {
			logger.debug(fileName + " was downloaded before");
			return file;
		}

		logger.debug(fileName + " wasn't downloaded before");

		String stdOut = youtubeDLHelper.makeDownloadRequest(videoUrl, format, dir);

		String[] firstSplit = stdOut.split("Destination: ");
		if (firstSplit.length != 2) {
			return null;
		} else {
			logger.info(fileName + " downloaded successfully from " + YtUrlHelper.getFullYtUrl(videoUrl));
			return file;
		}
	}

	private LinkedHashMap<String, String> getQrCodes(String videoUrl, LinkedHashMap<String, String> formats) {
		final String URL_TEMPLATE = YtUrlHelper.getVideoUrlPart(videoUrl) + "/%s";
		LinkedHashMap<String, String> qrCodes = new LinkedHashMap<String, String>();

		for (Map.Entry<String, String> entry : formats.entrySet()) {
			String qrCodeBase64 = qrCodeGenerator.getQRCodeImage(String.format(URL_TEMPLATE, entry.getValue()), 150,
					150);
			qrCodes.put(entry.getKey(), qrCodeBase64);
		}

		return qrCodes;
	}

}