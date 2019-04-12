package machalica.marcin.spring.ytdownloader.helpers;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class LocalFileHelper {
	public boolean doesFileExist(File file) {
		return file.exists();
	}
}
