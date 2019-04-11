package machalica.marcin.spring.ytdownloader.helpers;

import java.util.Hashtable;

import org.springframework.stereotype.Component;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;

@Component
public class YoutubeDLService {
	public String makeYoutubeDLRequest(String videoUrl, String dir, Hashtable<String, String> params) throws YoutubeDLException {
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
}