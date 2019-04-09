package machalica.marcin.spring.ytdownloader.downloader;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sapher.youtubedl.YoutubeDLException;


@Controller
public class YtDownloaderController {
	@Autowired
	private YtDownloaderDao ytDownloaderService;
	
	@GetMapping("/yt-downloader")
	public String index() {
		return "ytdownloader";
	}
	
	@RequestMapping(value = "yt-downloader", method = RequestMethod.POST)
	public String getFormats(@ModelAttribute("path") String path, ModelMap model) {
		HashMap<String, String> formats = null;
		try {
			formats = ytDownloaderService.getAvaibleFormats(path);
		} catch (YoutubeDLException ex) {
			ex.printStackTrace();
		}
		model.addAttribute("formats", formats);
		return "ytdownloader";
	}
	
}
