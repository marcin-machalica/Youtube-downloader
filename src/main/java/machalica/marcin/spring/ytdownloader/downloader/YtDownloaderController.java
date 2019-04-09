package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sapher.youtubedl.YoutubeDLException;

@Controller
public class YtDownloaderController {
	@Autowired
	private YtDownloaderDao ytDownloaderService;

	@GetMapping("/yt-downloader")
	public String index(ModelMap model) {
		model.addAttribute("file_info", YtDownloadFile.getEmptyYtDownloadFile());
		return "ytdownloader";
	}

	@RequestMapping(value = "yt-downloader", method = RequestMethod.POST)
	public String getFileInfo(@ModelAttribute("url") String url, ModelMap model) {
		YtDownloadFile ytDownloadFile = YtDownloadFile.getEmptyYtDownloadFile();
		try {
			ytDownloadFile = ytDownloaderService.getFileInfo(url);
		} catch (YoutubeDLException ex) {
			ex.printStackTrace();
		}
		model.addAttribute("file_info", ytDownloadFile);
		return "ytdownloader";
	}

	@GetMapping("/yt-downloader/download")
	public String download(@RequestParam String url, @RequestParam(name = "format") String format, ModelMap model) {
		File file = null;
		try {
			file = ytDownloaderService.downloadFile(url, format);
		} catch (YoutubeDLException ex) {
			ex.printStackTrace();
		}

		if (file != null) {
			System.out.println(file.getPath());
		} else {
			System.out.println("File is null");
		}

		model.addAttribute("file_info", YtDownloadFile.getEmptyYtDownloadFile());
		return "ytdownloader";
	}

}
