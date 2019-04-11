package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sapher.youtubedl.YoutubeDLException;

import machalica.marcin.spring.ytdownloader.helpers.YoutubeDLService;
import machalica.marcin.spring.ytdownloader.helpers.YtFileInfo;
import machalica.marcin.spring.ytdownloader.helpers.YtUrlHelper;
import machalica.marcin.spring.ytdownloader.qrcodegenerator.QrCodeGenerator;

@Service
public class YtDownloaderService implements YtDownloaderDao {
	private static final Logger logger = Logger.getLogger(YtDownloaderService.class);
	private final YoutubeDLService youtubeDLService;
	
	@Autowired
	public YtDownloaderService(YoutubeDLService youtubeDLService) {
		this.youtubeDLService = youtubeDLService;
	}
	
	@Override
	public YtFileInfo getFileInfo(String videoUrl) throws YoutubeDLException {
		LinkedHashMap<String, String> formats = getAvaibleFormats(videoUrl);
		if (formats == null || formats.isEmpty()) {
			return YtFileInfo.getEmptyYtFileInfo();
		}

		LinkedHashMap<String, String> qrCodes = getQrCodes(videoUrl, formats);
		if (qrCodes == null || qrCodes.isEmpty()) {
			return YtFileInfo.getEmptyYtFileInfo();
		}

		String title = getTitle(videoUrl);
		if (title == null || title.isEmpty()) {
			return YtFileInfo.getEmptyYtFileInfo();
		}

		String thumbnailUrl = getThumbnailUrl(videoUrl);
		if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
			return YtFileInfo.getEmptyYtFileInfo();
		}

		return new YtFileInfo(formats, qrCodes, videoUrl, title, thumbnailUrl);
	}

	private LinkedHashMap<String, String> getQrCodes(String videoUrl, LinkedHashMap<String, String> formats) {
		final String URL_TEMPLATE = YtUrlHelper.getVideoUrlPart(videoUrl) + "/%s";
		LinkedHashMap<String, String> qrCodes = new LinkedHashMap<String, String>();

		for (Map.Entry<String, String> entry : formats.entrySet()) {
			String qrCodeBase64 = QrCodeGenerator.getQRCodeImage(String.format(URL_TEMPLATE, entry.getValue()), 150,
					150);
			qrCodes.put(entry.getKey(), qrCodeBase64);
		}

		return qrCodes;
	}

	@Override
	public File downloadFile(String videoUrl, String format) throws YoutubeDLException {
		String dir = "./downloaded_files/";
		String fileName = getFileName(videoUrl, format);
		String filePath = dir.substring(2) + fileName;

		File file = new File(filePath);

		if (file.exists()) {
			logger.debug(fileName + " was downloaded before");
			return file;
		}
		
		logger.debug(fileName + " wasn't downloaded before");
		
		String stdOut = makeDownloadRequest(videoUrl, format, dir);

		String[] firstSplit = stdOut.split("Destination: ");
		if (firstSplit.length != 2) {
			return null;
		} else {
			logger.info(fileName + " downloaded successfully from " + YtUrlHelper.getFullYtUrl(videoUrl));
			return file;
		}
	}

	private String getFileName(String videoUrl, String format) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", "");	// --ignore-errors
		params.put("retries", "10");	// --retries 10
		params.put("get-filename", "");	// --get-filename
		params.put("output", "%(title)s_" + format + ".%(ext)s"); // custom filename
		params.put("format", format);	// --format
		
		String filename = youtubeDLService.makeYoutubeDLRequest(videoUrl, "", params);
		return filename;
	}
	
	private String makeDownloadRequest(String videoUrl, String format, String dir) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("output", "%(title)s_" + format + ".%(ext)s"); // custom filename
		params.put("retries", "10"); // --retries 10
		params.put("format", format); // --format

		String response = youtubeDLService.makeYoutubeDLRequest(videoUrl, dir, params);
		return response;
	}

	private String getTitle(String videoUrl) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("retries", "10"); // --retries 10
		params.put("get-title", ""); // --get-title
		params.put("output", "%(title)s"); // --output "%(title)s"

		String title = youtubeDLService.makeYoutubeDLRequest(videoUrl, "", params);
		return title;
	}

	private String getThumbnailUrl(String videoUrl) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("retries", "10"); // --retries 10
		params.put("get-thumbnail", ""); // --get-thumbnail

		String thumbnailUrl = youtubeDLService.makeYoutubeDLRequest(videoUrl, "", params);
		return thumbnailUrl;
	}

	private LinkedHashMap<String, String> getAvaibleFormats(String videoUrl) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("retries", "10"); // --retries 10
		params.put("list-formats", ""); // --list-formats

		String stdOut = youtubeDLService.makeYoutubeDLRequest(videoUrl, "", params);

		String[] formatsSplitArr = stdOut.split("format code  extension  resolution note");
		if (formatsSplitArr.length != 2) {
			return null;
		} else {
			String[] formatsArr = formatsSplitArr[1].trim().split("\n");
			sortFormats(formatsArr);
			LinkedHashMap<String, String> formats = new LinkedHashMap<String, String>();

			for (String format : formatsArr) {
				if (!format.contains("video only")) {
					formats.put(format, format.split(" ")[0]);
				}
			}

			return formats;
		}
	}

	private void sortFormats(String[] formats) {
		for (String format : formats) {
			if (!format.split(" ")[0].matches("\\d{1,}")) {
				Arrays.sort(formats);
				return;
			}
		}

		Arrays.sort(formats, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Integer.valueOf(o2.split(" ")[0]).compareTo(Integer.valueOf(o1.split(" ")[0]));
			}
		});
	}

}