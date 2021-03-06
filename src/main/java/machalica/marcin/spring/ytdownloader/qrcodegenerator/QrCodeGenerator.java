package machalica.marcin.spring.ytdownloader.qrcodegenerator;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QrCodeGenerator {
	private byte[] getQRCodePrimByteArr(String url, int width, int height) {
		try {
			url = InetAddress.getLocalHost().getHostAddress() + ":8080/yt-downloader/" + url;
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			byte[] pngData = pngOutputStream.toByteArray();
			return pngData;
		} catch (Exception ex) {
			return new byte[] {};
		}
	}

	public String getQRCodeImage(String url, int width, int height) {
		return Base64.getEncoder().encodeToString(getQRCodePrimByteArr(url, width, height));
	}
}
