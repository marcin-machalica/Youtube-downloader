package machalica.marcin.spring.ytdownloader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.LinkedHashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.sapher.youtubedl.YoutubeDLException;

import machalica.marcin.spring.ytdownloader.downloader.YtDownloaderDao;
import machalica.marcin.spring.ytdownloader.helpers.YtFileInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class YtDownloaderControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private YtDownloaderDao ytDownloaderService;

	private static final LinkedHashMap<String, String> formats = new LinkedHashMap<String, String>();

	private static final String qrCode = "iVBORw0KGgoAAAANSUhEUgAAAJYAAACWAQAAAAAUekxPAAABF0lEQVR42u2WsQ4CMQxDu";
	private static final LinkedHashMap<String, String> qrCodes = new LinkedHashMap<String, String>();

	private static final String videoUrl = "https://www.youtube.com/watch?v=qwerty";
	private static final String invalidVideoUrl = "https://www.youtube.com/watch?v=azerty";
	private static final String videoPartUrl = "qwerty";
	private static final String invalidVideoPartUrl = "azerty";

	private static final String format = "251";

	private static final String videoTitle = "qwerty";

	private static final String filename = videoTitle + "_" + format + ".webm";
	private static final String filenameWithoutFormat = videoTitle + ".webm";
	private static final String dirWithFilename = "downloaded_files/" + filename;
	private static final String fileLength = "0";

	@BeforeClass
	public static void setupOnce() {
		formats.put("251 webm audio only DASH audio 161k , opus @160k, 4.14MiB", "251");
		formats.put("250 webm audio only DASH audio 85k , opus @ 70k, 2.12MiB", "250");

		formats.forEach((key, value) -> {
			qrCodes.put(key, qrCode);
		});
	}

	@Test
	public void indexTest() throws Exception {
		YtFileInfo expected = YtFileInfo.getEmptyYtFileInfo();

		mockMvc.perform(get("/yt-downloader")).andDo(print()).andExpect(status().isOk())
				.andExpect(model().attribute("file_info", expected));
	}

	@Test
	public void formatsRedirectTest() throws Exception {
		mockMvc.perform(post("/yt-downloader").param("url", "qwerty")).andDo(print()).andExpect(status().isFound())
				.andExpect(redirectedUrl("/yt-downloader/qwerty"));
	}

	@Test
	public void getFileInfoTest() throws Exception {
		YtFileInfo expected = new YtFileInfo(formats, qrCodes, videoUrl, videoTitle, videoPartUrl);
		when(ytDownloaderService.getFileInfo(videoUrl)).thenReturn(expected);

		mockMvc.perform(get("/yt-downloader/{url}", videoPartUrl)).andDo(print()).andExpect(status().isOk())
				.andExpect(model().attribute("file_info", expected));

		verify(ytDownloaderService).getFileInfo(videoUrl);
	}

	@Test
	public void getFileInfoWithInvalidUrlTest() throws Exception {
		YtFileInfo expected = YtFileInfo.getEmptyYtFileInfo();
		expected.setError("Not valid URL: " + invalidVideoUrl);
		when(ytDownloaderService.getFileInfo(invalidVideoUrl))
				.thenThrow(new YoutubeDLException("Incomplete YouTube ID"));

		mockMvc.perform(get("/yt-downloader/{url}", invalidVideoPartUrl)).andDo(print()).andExpect(status().isOk())
				.andExpect(model().attribute("file_info", expected));

		verify(ytDownloaderService).getFileInfo(invalidVideoUrl);
	}

	@Test
	public void downloadWithErrorTest() throws Exception {
		when(ytDownloaderService.downloadFile(invalidVideoPartUrl, format)).thenThrow(new YoutubeDLException(""));

		mockMvc.perform(get("/yt-downloader/{url}/{format}", invalidVideoPartUrl, format)).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void downloadTest() throws Exception {
		when(ytDownloaderService.downloadFile(videoPartUrl, format)).thenReturn(new File(dirWithFilename));

		MvcResult mvcResult = mockMvc.perform(get("/yt-downloader/{url}/{format}", videoPartUrl, format)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		String contentDispositionHeaderValue = mvcResult.getResponse().getHeader("Content-Disposition");
		String contentLengthHeaderValue = mvcResult.getResponse().getHeader("Content-Length");

		assertEquals("attachment; filename=" + filenameWithoutFormat, contentDispositionHeaderValue);
		assertEquals(fileLength, contentLengthHeaderValue);
	}
}
