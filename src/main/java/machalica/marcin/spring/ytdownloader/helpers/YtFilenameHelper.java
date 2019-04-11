package machalica.marcin.spring.ytdownloader.helpers;

public class YtFilenameHelper {
	public static String convertFileNameToTitleWithExt(String filename) {
		String baseFileName = filename.substring(0, filename.lastIndexOf('_'));
		String ext = filename.substring(filename.lastIndexOf('.'));
		String titleWithExt = baseFileName + ext;
		return titleWithExt;
	}
}
