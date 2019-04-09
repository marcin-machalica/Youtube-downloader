package machalica.marcin.spring.ytdownloader.downloader;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class YtDownloaderService implements YtDownloaderDao {

	@Override
	public List<String> getAvaibleFormats(String videoUrl) {
		return null;
	}

	@Override
	public boolean downloadFile(String videoUrl, String format) {
		return false;
	}

}
