package machalica.marcin.spring.ytdownloader;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import machalica.marcin.spring.ytdownloader.helpers.YtFilenameHelper;
import machalica.marcin.spring.ytdownloader.helpers.YtUrlHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YtDownloaderHelpersTests {

	@Test
	public void convertFileNameToTitleWithExtTest() {
		String filename = "asd_18.mp4";
		String expected = "asd.mp4";
		String actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd.mp4";
		expected = "asd.mp4";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd";
		expected = "asd";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "";
		expected = "";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = null;
		expected = "";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd_qwe_zxc_18.mp4";
		expected = "asd_qwe_zxc.mp4";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd_qwe_zxc.mp4";
		expected = "asd_qwe_zxc.mp4";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd_qwe_zxc_";
		expected = "asd_qwe_zxc_";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd_qwe_zxc_.zx.mp4";
		expected = "asd_qwe_zxc_.zx.mp4";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd_qwe_zxc_18.zx.mp4";
		expected = "asd_qwe_zxc_18.zx.mp4";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd_qwe_zxc_18.zx.mp4.";
		expected = "asd_qwe_zxc_18.zx.mp4.";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd_qwe_zxc.zx_18.mp4";
		expected = "asd_qwe_zxc.zx.mp4";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);

		filename = "asd_qwe_zxc_.zx.mp4.";
		expected = "asd_qwe_zxc_.zx.mp4.";
		actual = YtFilenameHelper.convertFileNameToTitleWithExt(filename);
		assertEquals(expected, actual);
	}
	
	@Test
	public void getFullYtUrlTest() {
		String videoUrlPart = "asd_18.mp4";
		String expected = "https://www.youtube.com/watch?v=" + videoUrlPart;
		String actual = YtUrlHelper.getFullYtUrl(videoUrlPart);
		assertEquals(expected, actual);
		
		videoUrlPart = "https://www.youtube.com/watch?v=";
		expected = "https://www.youtube.com/watch?v=" + videoUrlPart;
		actual = YtUrlHelper.getFullYtUrl(videoUrlPart);
		assertEquals(expected, actual);
		
		videoUrlPart = "https://www.youtube.com/watch?v=qwerty";
		expected = "https://www.youtube.com/watch?v=" + videoUrlPart;
		actual = YtUrlHelper.getFullYtUrl(videoUrlPart);
		assertEquals(expected, actual);
		
		videoUrlPart = null;
		expected = "https://www.youtube.com/watch?v=";
		actual = YtUrlHelper.getFullYtUrl(videoUrlPart);
		assertEquals(expected, actual);
	}
	
	@Test
	public void getVideoUrlPartTest() {
		String url = "";
		String expected = url;
		String actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = null;
		expected = "";
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "https://www.youtube.com/watch?v=qwerty";
		expected = "qwerty";
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "https://www.youtube.com/watc?v=qwerty";
		expected = url;
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "https://www.youtube.com/watch?v=qwerty/azerty";
		expected = url;
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "watch?v=qwerty";
		expected = url;
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "https://www.youtube.com/watch?v=watch?v=qwerty";
		expected = "watch?v=qwerty";
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "https://www.youtube.com/watch?v=";
		expected = "";
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "https://www.youtube.com/watch?v=watch?v=";
		expected = "watch?v=";
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=";
		expected = url;
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
		
		url = "https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=qwerty";
		expected = url;
		actual = YtUrlHelper.getVideoUrlPart(url);
		assertEquals(expected, actual);
	}

}
