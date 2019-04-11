package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;

import machalica.marcin.spring.ytdownloader.helpers.YtFileInfo;
import machalica.marcin.spring.ytdownloader.helpers.YtUrlHelper;
import machalica.marcin.spring.ytdownloader.qrcodegenerator.QrCodeGenerator;

@Service
public class YtDownloaderService implements YtDownloaderDao {
	private static final Logger logger = Logger.getLogger(YtDownloaderService.class);

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

		YoutubeDLRequest request = new YoutubeDLRequest(videoUrl, dir);
		request.setOption("ignore-errors"); // --ignore-errors
		request.setOption("output", "%(title)s_" + format + ".%(ext)s"); // custom filename
		request.setOption("retries", 10); // --retries 10
		request.setOption("format", format); // --format

		YoutubeDLResponse response = YoutubeDL.execute(request);
		String stdOut = response.getOut();

		String[] firstSplit = stdOut.split("Destination: ");
		if (firstSplit.length != 2) {
			return null;
		} else {
			logger.info(fileName + " downloaded successfully from " + YtUrlHelper.getFullYtUrl(videoUrl));
			return file;
		}
	}

	private String getFileName(String videoUrl, String format) throws YoutubeDLException {
		YoutubeDLRequest request = new YoutubeDLRequest(videoUrl);
		request.setOption("ignore-errors"); // --ignore-errors
		request.setOption("retries", 10); // --retries 10
		request.setOption("get-filename"); // --get-filename
		request.setOption("output", "%(title)s_" + format + ".%(ext)s"); // custom filename
		request.setOption("format", format); // --format

		YoutubeDLResponse response = YoutubeDL.execute(request);
		String filename = response.getOut().trim();
		return filename;
	}

	private String getTitle(String videoUrl) throws YoutubeDLException {
		YoutubeDLRequest request = new YoutubeDLRequest(videoUrl);
		request.setOption("ignore-errors"); // --ignore-errors
		request.setOption("retries", 10); // --retries 10
		request.setOption("get-title"); // --get-title
		request.setOption("output", "%(title)s"); // --output "%(title)s"

		YoutubeDLResponse response = YoutubeDL.execute(request);
		return response.getOut().trim();
	}

	private String getThumbnailUrl(String videoUrl) throws YoutubeDLException {
		YoutubeDLRequest request = new YoutubeDLRequest(videoUrl);
		request.setOption("ignore-errors"); // --ignore-errors
		request.setOption("retries", 10); // --retries 10
		request.setOption("get-thumbnail"); // --get-thumbnail

		YoutubeDLResponse response = YoutubeDL.execute(request);
		return response.getOut().trim();
	}

	private LinkedHashMap<String, String> getAvaibleFormats(String videoUrl) throws YoutubeDLException {
		YoutubeDLRequest request = new YoutubeDLRequest(videoUrl);
		request.setOption("ignore-errors"); // --ignore-errors
		request.setOption("retries", 10); // --retries 10
		request.setOption("list-formats"); // --list-formats

		YoutubeDLResponse response = YoutubeDL.execute(request);
		String stdOut = response.getOut();

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
