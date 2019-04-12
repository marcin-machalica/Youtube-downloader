package machalica.marcin.spring.ytdownloader.helpers;

public class YtFilenameHelper {
	public static String convertFileNameToTitleWithExt(String filename) {
		if (filename == null) {
			return "";
		}

		if (!filename.contains("_") || (filename.length() <= filename.lastIndexOf('_') + 1)) {
			return filename;
		}

		try {
			Integer.parseInt(filename.substring(filename.lastIndexOf('_') + 1, filename.lastIndexOf('.')));
		} catch (NumberFormatException ex) {
			return filename;
		}

		String baseFileName = filename.substring(0, filename.lastIndexOf('_'));
		String ext = filename.substring(filename.lastIndexOf('.'));
		String titleWithExt = baseFileName + ext;
		return titleWithExt;
	}
}