package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;

@Service
public class YtDownloaderService implements YtDownloaderDao {

	@Override
	public YtDownloadFile getFileInfo(String videoUrl) throws YoutubeDLException {
		LinkedHashMap<String, String> formats = getAvaibleFormats(videoUrl);
		if (formats == null || formats.isEmpty()) {
			return YtDownloadFile.getEmptyYtDownloadFile();
		} else {
			return new YtDownloadFile(formats, videoUrl);
		}
	}

	@Override
	public File downloadFile(String videoUrl, String format) throws YoutubeDLException {
		String dir = "./downloaded_files/";
		String fileName = getFileName(videoUrl, format);
		String filePath = dir.substring(2) + fileName;

		File file = new File(filePath);

		if (file.exists()) {
			System.out.println(fileName + " was downloaded before");
			return file;
		}

		System.out.println(fileName + " wasn't downloaded before");

		YoutubeDLRequest request = new YoutubeDLRequest(videoUrl, dir);
		request.setOption("ignore-errors"); // --ignore-errors
		request.setOption("output", "%(title)s.%(ext)s"); // --output "%(title)s"
		request.setOption("retries", 10); // --retries 10
		request.setOption("format", format); // --format

		YoutubeDLResponse response = YoutubeDL.execute(request);
		String stdOut = response.getOut();

		String[] firstSplit = stdOut.split("Destination: ");
		if (firstSplit.length != 2) {
			return null;
		} else {
			System.out.println(fileName + " downloaded successfully");
			return file;
		}
	}

	private String getFileName(String videoUrl, String format) throws YoutubeDLException {
		YoutubeDLRequest request = new YoutubeDLRequest(videoUrl);
		request.setOption("ignore-errors"); // --ignore-errors
		request.setOption("retries", 10); // --retries 10
		request.setOption("get-filename"); // --get-title
		request.setOption("output", "%(title)s.%(ext)s"); // --output "%(title)s.%(ext)s"
		request.setOption("format", format); // --format

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
