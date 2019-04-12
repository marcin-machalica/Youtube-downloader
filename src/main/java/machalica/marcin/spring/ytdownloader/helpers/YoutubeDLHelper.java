package machalica.marcin.spring.ytdownloader.helpers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;

@Service
public class YoutubeDLHelper {
	public String makeYoutubeDLRequest(String videoUrl, String dir, Hashtable<String, String> params)
			throws YoutubeDLException {
		YoutubeDLRequest request;
		if (dir.isEmpty()) {
			request = new YoutubeDLRequest(videoUrl);
		} else {
			request = new YoutubeDLRequest(videoUrl, dir);
		}

		params.forEach((key, value) -> {
			if (value.equals("")) {
				request.setOption(key);
			} else {
				request.setOption(key, value);
			}
		});

		YoutubeDLResponse response = YoutubeDL.execute(request);
		String stdOut = response.getOut().trim();
		return stdOut;
	}

	public LinkedHashMap<String, String> getAvaibleFormats(String videoUrl) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("retries", "10"); // --retries 10
		params.put("list-formats", ""); // --list-formats

		String stdOut = makeYoutubeDLRequest(videoUrl, "", params);

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

	public String getTitle(String videoUrl) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("retries", "10"); // --retries 10
		params.put("get-title", ""); // --get-title
		params.put("output", "%(title)s"); // --output "%(title)s"

		String title = makeYoutubeDLRequest(videoUrl, "", params);
		return title;
	}

	public String getThumbnailUrl(String videoUrl) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("retries", "10"); // --retries 10
		params.put("get-thumbnail", ""); // --get-thumbnail

		String thumbnailUrl = makeYoutubeDLRequest(videoUrl, "", params);
		return thumbnailUrl;
	}

	public String getFileName(String videoUrl, String format) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("retries", "10"); // --retries 10
		params.put("get-filename", ""); // --get-filename
		params.put("output", "%(title)s_" + format + ".%(ext)s"); // custom filename
		params.put("format", format); // --format

		String filename = makeYoutubeDLRequest(videoUrl, "", params);
		return filename;
	}

	public String makeDownloadRequest(String videoUrl, String format, String dir) throws YoutubeDLException {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("ignore-errors", ""); // --ignore-errors
		params.put("output", "%(title)s_" + format + ".%(ext)s"); // custom filename
		params.put("retries", "10"); // --retries 10
		params.put("format", format); // --format

		String response = makeYoutubeDLRequest(videoUrl, dir, params);
		return response;
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