package com.cfwin.lycheeupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;

public class UploadUtil {

	static final String cookiesfile = "cookes";
	
	private static String domain = "";//eg: https://xxx.xxx.com

	public static void initDomain(String  domainpar)
	{
		domain = domainpar;
	}

	public static String commonMethodEx(HttpEntity entity) throws Exception, IOException {
		File cookfile = new File(cookiesfile);
		CookieStore cookieStore = null;// new BasicCookieStore();
		if (cookfile.isFile()) {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(cookfile));
			cookieStore = (BasicCookieStore) is.readObject();
			is.close();
		} else {
			cookieStore = new BasicCookieStore();
		}
		String url = domain+"/php/index.php";
		Executor executor = Executor.newInstance();
		String res = executor.use(cookieStore).execute(Request.Post(url).body(entity)).returnContent().asString();
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(cookfile));
		os.writeObject(cookieStore);
		os.close();
		return res;
	}

	public static String commonMethod(List<NameValuePair> params) throws Exception, IOException {
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Charset.forName("UTF-8"));
		return commonMethodEx(entity);
	}

	public static void init() throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("function", "Session::init"));
		String res = commonMethod(params);
		System.out.println(res);
	}

	// Albums::get
	public static void albums_get() throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("function", "Albums::get"));
		String res = commonMethod(params);
		System.out.println(res);
	}

	public static void albums_get(String id) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("function", "Album::get"));
		params.add(new BasicNameValuePair("albumID", id));
		params.add(new BasicNameValuePair("password", ""));
		String res = commonMethod(params);
		System.out.println(res);
	}

	public static String photo_get(String albumID, String phoneID) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("function", "Photo::get"));
		params.add(new BasicNameValuePair("albumID", albumID));
		params.add(new BasicNameValuePair("password", ""));
		params.add(new BasicNameValuePair("photoID", phoneID));
		params.add(new BasicNameValuePair("password", ""));
		String res = commonMethod(params);
		// System.out.println(res);
		return res;
	}

	public static String login(String username, String password) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("function", "Session::login"));
		params.add(new BasicNameValuePair("user", username));
		params.add(new BasicNameValuePair("password", password));
		String res = commonMethod(params);
		// System.out.println(res);
		return res;
		// Session::init
	}

	public static String photo_add(String filepath) throws Exception {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("function", "Photo::add");
		builder.addTextBody("albumID", "0");
		builder.addBinaryBody("0", new File(filepath), ContentType.APPLICATION_OCTET_STREAM, filepath);
		HttpEntity multipart = builder.build();
		String res = commonMethodEx(multipart);
		return res;
		// System.out.println(res);
		// 14981465569940
	}
}
