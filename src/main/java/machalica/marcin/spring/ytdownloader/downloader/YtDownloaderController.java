package machalica.marcin.spring.ytdownloader.downloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.sapher.youtubedl.YoutubeDLException;

import machalica.marcin.spring.ytdownloader.helpers.YtFileInfo;
import machalica.marcin.spring.ytdownloader.helpers.YtFilenameHelper;
import machalica.marcin.spring.ytdownloader.helpers.YtUrlHelper;

@Controller
public class YtDownloaderController {
	private static final Logger logger = Logger.getLogger(YtDownloaderController.class);
	@Autowired
	private YtDownloaderDao ytDownloaderService;

	@GetMapping("/yt-downloader")
	public String index(ModelMap model) {
		model.addAttribute("file_info", YtFileInfo.getEmptyYtFileInfo());
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

		YtFileInfo ytFileInfo = YtFileInfo.getEmptyYtFileInfo();
		try {
			ytFileInfo = ytDownloaderService.getFileInfo(url);
		} catch (YoutubeDLException ex) {
			if (ex.toString().contains("Incomplete YouTube ID")) {
				logger.debug(ex);
				ytFileInfo.setError("Not valid URL: " + url);
			} else {
				logger.error(ex);
			}
		}

		model.addAttribute("file_info", ytFileInfo);
		return "ytdownloader";
	}

	@GetMapping("/yt-downloader/{url}/{format}")
	public void download(@PathVariable String url, @PathVariable String format, HttpServletResponse response) {
		File file = null;
		try {
			file = ytDownloaderService.downloadFile(url, format);
		} catch (YoutubeDLException ex) {
			logger.error(ex);
		}

		if (file == null) {
			logger.error("File is null");
		} else {
			logger.debug(file.getPath());

			response.addHeader("Content-Disposition",
					"attachment; filename=" + YtFilenameHelper.convertFileNameToTitleWithExt(file.getName()));
			response.addHeader("Content-Length", Long.toString(file.length()));
			try {
				Files.copy(Paths.get(file.getPath()), response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}

}
