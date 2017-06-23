package com.cfwin.lycheeupload;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("java -jar lychee <username> <password>");
			return;
		}
		String username = args[0];
		String password = args[1];
		System.out.println(username + ":" + password);
		run(username, password);
	}

	public static void run(String username, String passowrd) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddmmss");
		String filename = sf.format(new Date()) + ".jpg";
		boolean res = work(filename);
		if (res) {
			String add_res = UploadUtil.photo_add(filename);
			if (!add_res.matches("\\d*")) {
				String loginres = UploadUtil.login(username, passowrd);
				System.out.println("loing res:" + loginres);
				add_res = UploadUtil.photo_add(filename);
				if (!add_res.matches("\\d*")) {
					System.out.println("error:" + add_res);
					return;
				}
			}
			// get url
			String resjson = UploadUtil.photo_get("0", add_res);
			String domin = "https://img.fengcl.com/";
			String url = getUrlFromJson(resjson);
			setTextToClipboard(domin + url);
			System.out.println("OK: " + domin + url);
		}
	}

	public static void setTextToClipboard(String str) {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection strsel = new StringSelection(str);
		c.setContents(strsel, null);
	}

	public static String getUrlFromJson(String json) {
		JsonParser p = new JsonParser();
		JsonElement obj = p.parse(json);
		String url = obj.getAsJsonObject().get("url").getAsString();
		return url;
	}

	public static boolean work(String filename) throws Exception {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		DataFlavor[] list = c.getAvailableDataFlavors();
		if (list == null)
			return false;
		DataFlavor t = list[0];
		System.out.println("list len:" + list.length);
		String type = t.getMimeType();
		System.out.println(type);
		if (type.startsWith("image/x-java-image")) {
			BufferedImage bufimg = (BufferedImage) c.getData(DataFlavor.imageFlavor);
			File imageFile = new File(filename);
			ImageIO.write(bufimg, "jpg", imageFile);
			return true;
			// ByteArrayOutputStream b=new ByteArrayOutputStream();
			// ImageIO.write(getBufferedImage(img), "png", b);
			// byte[] imgbys=b.toByteArray();
		}
		return false;
	}
}
