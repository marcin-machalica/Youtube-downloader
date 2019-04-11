package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

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

	@PostMapping("/yt-downloader")
	public RedirectView formatsRedirect(@ModelAttribute("url") String url) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/yt-downloader/" + YtUrlHelper.getVideoUrlPart(url));
		return redirectView;
	}

	@GetMapping("/yt-downloader/{url}")
	public String getFileInfo(@PathVariable String url, ModelMap model) {
		url = YtUrlHelper.getFullYtUrl(url);

		YtDownloadFile ytDownloadFile = YtDownloadFile.getEmptyYtDownloadFile();
		try {
			ytDownloadFile = ytDownloaderService.getFileInfo(url);
		} catch (YoutubeDLException ex) {
			ex.printStackTrace();
		}
		model.addAttribute("file_info", ytDownloadFile);
		return "ytdownloader";
	}

	@GetMapping("/yt-downloader/{url}/{format}")
	public void download(@PathVariable String url, @PathVariable String format, HttpServletResponse response) {
		File file = null;
		try {
			file = ytDownloaderService.downloadFile(url, format);
		} catch (YoutubeDLException ex) {
			ex.printStackTrace();
		}

		if (file == null) {
			System.out.println("File is null");
		} else {
			System.out.println(file.getPath());

			response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
			try {
				Files.copy(Paths.get(file.getPath()), response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
