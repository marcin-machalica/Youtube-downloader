package machalica.marcin.spring.ytdownloader.downloader;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;

@Service
public class YtDownloaderService implements YtDownloaderDao {

	@Override
	public HashMap<String, String> getAvaibleFormats(String videoUrl) throws YoutubeDLException {
		YoutubeDLRequest request = new YoutubeDLRequest(videoUrl);
		request.setOption("ignore-errors");		// --ignore-errors
		request.setOption("output", "%(title)s");	// --output "%(title)s"
		request.setOption("retries", 10);		// --retries 10
		request.setOption("list-formats");	// --list-formats

		YoutubeDLResponse response = YoutubeDL.execute(request);
		String stdOut = response.getOut();
				
		String[] formatsSplitArr = stdOut.split("format code  extension  resolution note");
		if (formatsSplitArr.length != 2) {
			return new HashMap<String, String>();
		} else {
			String[] formatsArr = formatsSplitArr[1].trim().split("\n");
			HashMap<String, String> formats = new HashMap<String, String>();
			
			for(String format : formatsArr) {
				formats.put(format, format.split(" ")[0]);
			}
			
			return formats;
		}		
	}

	@Override
	public boolean downloadFile(String videoUrl, String format) {
		return false;
	}

}
