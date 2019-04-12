package machalica.marcin.spring.ytdownloader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.File;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sapher.youtubedl.YoutubeDLException;

import machalica.marcin.spring.ytdownloader.downloader.YtDownloaderService;
import machalica.marcin.spring.ytdownloader.helpers.LocalFileHelper;
import machalica.marcin.spring.ytdownloader.helpers.YoutubeDLHelper;
import machalica.marcin.spring.ytdownloader.helpers.YtFileInfo;
import machalica.marcin.spring.ytdownloader.qrcodegenerator.QrCodeGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YtDownloaderServiceTests {
	@InjectMocks
	private YtDownloaderService ytDownloaderService;
	@Mock
	private YoutubeDLHelper youtubeDLHelper;
	@Mock
	private QrCodeGenerator qrCodeGenerator;
	@Mock
	private LocalFileHelper localFileHelper;

	private static final LinkedHashMap<String, String> emptyFormats = new LinkedHashMap<String, String>();
	private static final LinkedHashMap<String, String> formats = new LinkedHashMap<String, String>();

	private static final String videoUrl = "https://www.youtube.com/watch?v=qwerty";
	private static final String videoPartUrl = "qwerty";
	private static final String videoTitle = "qwerty";
	private static final String format = "251";
	private static final String filename = videoUrl + "_" + format + ".webm";
	private static final String downloadRequestResponse = "Destination: " + filename;
	private static final String qrCode = "iVBORw0KGgoAAAANSUhEUgAAAJYAAACWAQAAAAAUekxPAAABF0lEQVR42u2WsQ4CMQxDu";
	private static final LinkedHashMap<String, String> qrCodes = new LinkedHashMap<String, String>();
	private static final String notExistingFormat = "5780";
	private static final String videoThumbnailUrl = "https://i.ytimg.com/vi/qwerty/maxresdefault.jpg";
	private static final String notExistingVideoUrl = "https://www.youtube.com/watch?v=azerty";
	private static final String dir = "./downloaded_files/";
	private static final String dirWithFilename = "downloaded_files/" + filename;
	private static final String notExistingDir = "notExistingDir";

	@BeforeClass
	public static void setupOnce() {
		formats.put("251 webm audio only DASH audio 161k , opus @160k, 4.14MiB", "251");
		formats.put("250 webm audio only DASH audio 85k , opus @ 70k, 2.12MiB", "250");

		formats.forEach((key, value) -> {
			qrCodes.put(key, qrCode);
		});
	}

	@Before
	public void setup() throws YoutubeDLException {
		when(youtubeDLHelper.getAvaibleFormats(videoUrl)).thenReturn(formats);
		when(youtubeDLHelper.getAvaibleFormats(notExistingVideoUrl)).thenThrow(new YoutubeDLException(""));

		when(qrCodeGenerator.getQRCodeImage(anyString(), anyInt(), anyInt()))
				.thenReturn("iVBORw0KGgoAAAANSUhEUgAAAJYAAACWAQAAAAAUekxPAAABF0lEQVR42u2WsQ4CMQxDu");

		when(youtubeDLHelper.getTitle(videoUrl)).thenReturn(videoTitle);
		when(youtubeDLHelper.getTitle(notExistingVideoUrl)).thenThrow(new YoutubeDLException(""));

		when(youtubeDLHelper.getThumbnailUrl(videoUrl)).thenReturn(videoThumbnailUrl);
		when(youtubeDLHelper.getThumbnailUrl(notExistingVideoUrl)).thenThrow(new YoutubeDLException(""));

		when(youtubeDLHelper.getFileName(videoUrl, format)).thenReturn(filename);
		when(youtubeDLHelper.getFileName(notExistingVideoUrl, format)).thenThrow(new YoutubeDLException(""));
		when(youtubeDLHelper.getFileName(videoUrl, notExistingFormat)).thenThrow(new YoutubeDLException(""));
		when(youtubeDLHelper.getFileName(notExistingVideoUrl, notExistingFormat)).thenThrow(new YoutubeDLException(""));

		when(youtubeDLHelper.makeDownloadRequest(videoUrl, format, dir)).thenReturn(downloadRequestResponse);
		when(youtubeDLHelper.makeDownloadRequest(notExistingVideoUrl, format, dir))
				.thenThrow(new YoutubeDLException(""));
		when(youtubeDLHelper.makeDownloadRequest(videoUrl, notExistingFormat, dir))
				.thenThrow(new YoutubeDLException(""));
		when(youtubeDLHelper.makeDownloadRequest(notExistingVideoUrl, notExistingFormat, dir))
				.thenThrow(new YoutubeDLException(""));
	}

	@Test
	public void getFileInfoWithoutErrorTest() throws YoutubeDLException {
		YtFileInfo expected = new YtFileInfo(formats, qrCodes, videoUrl, videoTitle, videoThumbnailUrl);
		YtFileInfo actual = ytDownloaderService.getFileInfo(videoUrl);
		assertEquals(expected, actual);
		verify(youtubeDLHelper).getAvaibleFormats(videoUrl);
		verify(youtubeDLHelper).getTitle(videoUrl);
		verify(youtubeDLHelper).getThumbnailUrl(videoUrl);
		formats.forEach((key, value) -> {
			verify(qrCodeGenerator).getQRCodeImage(videoPartUrl + "/" + value, 150, 150);
		});
	}

	@Test(expected = YoutubeDLException.class)
	public void getFileInfoWithErrorsTest() throws YoutubeDLException {
		ytDownloaderService.getFileInfo(notExistingVideoUrl);
	}

	@Test
	public void downloadFileWhileFileExistsTest() throws YoutubeDLException {
		File expected = new File(dirWithFilename);
		when(localFileHelper.doesFileExist(expected)).thenReturn(true);
		File actual = ytDownloaderService.downloadFile(videoUrl, format);
		assertEquals(expected, actual);
		verify(youtubeDLHelper, times(0)).makeDownloadRequest(videoUrl, format, dir);
	}

	@Test
	public void downloadFileWhileFileNotExistsTest() throws YoutubeDLException {
		File expected = new File(dirWithFilename);
		when(localFileHelper.doesFileExist(expected)).thenReturn(false);
		File actual = ytDownloaderService.downloadFile(videoUrl, format);
		assertEquals(expected, actual);
		verify(youtubeDLHelper).makeDownloadRequest(videoUrl, format, dir);
	}

	@Test(expected = YoutubeDLException.class)
	public void downloadFileWithNotExistingVideoUrlErrorTest() throws YoutubeDLException {
		ytDownloaderService.downloadFile(notExistingVideoUrl, format);
	}

	@Test(expected = YoutubeDLException.class)
	public void downloadFileWithNotExistingFormatErrorTest() throws YoutubeDLException {
		ytDownloaderService.downloadFile(videoUrl, notExistingFormat);
	}

	@Test(expected = YoutubeDLException.class)
	public void downloadFileWithNotExistingVideoUrlAndNotExistingFormatErrorTest() throws YoutubeDLException {
		ytDownloaderService.downloadFile(notExistingVideoUrl, notExistingFormat);
	}
}
