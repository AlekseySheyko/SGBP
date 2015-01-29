package com.widevision.sgbp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	Context context;
	static String json = "";

	private static long TIME_OUT_IN_SECONDS = 150;

	Bitmap map;

	// constructor
	public JSONParser() {

	}

	public JSONParser(Context context) {
		this.context = context;
	}

	public JSONObject getJSONFromUrl(String urlStr) {
		String encodeStr = stringByAddingPercentEscapesUsingEncoding(urlStr);
		System.out.println("encodeStr-------" + encodeStr);
		JSONObject jObj = null;
		// Making HTTP request
		try {
			// defaultHttpClient
			// String SetServerString = "";
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(encodeStr);
			// ResponseHandler<String> responseHandler = new
			// BasicResponseHandler();

			HttpResponse httpResponse = httpClient.execute(httpGet);

			long requestStratTime = new Date().getTime();
			long requestEndTime = new Date().getTime();
			Log.d("requestStratTime", "requestStratTime" + requestStratTime);
			Log.d("requestEndTime", "requestEndTime" + requestEndTime);
			long timeOfRequest = (requestEndTime - requestStratTime) / 1000;
			Log.d("timeOfRequest", "timeOfRequest" + timeOfRequest);
			if (httpResponse == null && timeOfRequest > TIME_OUT_IN_SECONDS) {

				// throw new TimeoutException();
				// ALERT MESSAGE
				threadMsg("SERVER REQUEST TIME OUT.");

			}

			// SetServerString = httpClient.execute(httpGet, responseHandler);
			// threadMsg(SetServerString);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			System.out.println("here 3");
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (Exception e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	private void threadMsg(String msg) {

		if (!msg.equals(null) && !msg.equals("")) {
			Message msgObj = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("message", msg);
			msgObj.setData(b);
			handler.sendMessage(msgObj);
		}
	}

	// Define the Handler that receives messages from the thread and
	// update the progress
	private final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			// String aResponse = msg.getData().getString("message");

			// if ((null != aResponse)) {
			//
			// // ALERT MESSAGE
			// threadMsg("Server Response: " + aResponse);
			// } else {

			// ALERT MESSAGE
			threadMsg("SERVER REQUEST TIME OUT.");
			// }

		}

	};

	public static String stringByAddingPercentEscapesUsingEncoding(String input, String charset) throws UnsupportedEncodingException {
		byte[] bytes = input.getBytes(charset);
		StringBuilder sb = new StringBuilder(bytes.length);
		for (int i = 0; i < bytes.length; ++i) {
			int cp = bytes[i] < 0 ? bytes[i] + 256 : bytes[i];
			if (cp <= 0x20
					|| cp >= 0x7F
					|| (cp == 0x22 || cp == 0x25 || cp == 0x3C || cp == 0x3E || cp == 0x20 || cp == 0x5B || cp == 0x5C || cp == 0x5D || cp == 0x5E
							|| cp == 0x60 || cp == 0x7b || cp == 0x7c || cp == 0x7d)) {
				sb.append(String.format("%%%02X", cp));
			} else {
				sb.append((char) cp);
			}
		}
		return sb.toString();
	}

	public static String stringByAddingPercentEscapesUsingEncoding(String input) {
		try {
			return stringByAddingPercentEscapesUsingEncoding(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Java platforms are required to support UTF-8");
			// will never happen
		}
	}

	// public JSONObject getJSONFromUrlThread(String urlStr) {
	// final String encodeStr = stringByAddingPercentEscapesUsingEncoding(urlStr);
	// System.out.println("encodeStr-------" + encodeStr);
	// JSONObject jObj = null;
	// // Create Inner Thread Class
	// Thread background = new Thread(new Runnable() {
	//
	// // private final HttpClient Client = new DefaultHttpClient();
	// // private String URL =
	// // "http://androidexample.com/media/webservice/getPage.php";
	//
	// // After call for background.start this run method call
	// public void run() {
	// try {
	// DefaultHttpClient httpClient = new DefaultHttpClient();
	// HttpGet httpGet = new HttpGet(encodeStr);
	// // ResponseHandler<String> responseHandler = new
	// // BasicResponseHandler();
	// HttpResponse httpResponse = httpClient.execute(httpGet);
	// // SetServerString = httpClient.execute(httpGet,
	// // responseHandler);
	// // threadMsg(SetServerString);
	// HttpEntity httpEntity = httpResponse.getEntity();
	// is = httpEntity.getContent();
	//
	// } catch (Throwable t) {
	// // just end the background thread
	// Log.i("Animation", "Thread  exception " + t);
	// }
	// }
	//
	// private void threadMsg(String msg) {
	//
	// if (!msg.equals(null) && !msg.equals("")) {
	// Message msgObj = handler.obtainMessage();
	// Bundle b = new Bundle();
	// b.putString("message", msg);
	// msgObj.setData(b);
	// handler.sendMessage(msgObj);
	// }
	// }
	//
	// // Define the Handler that receives messages from the thread and
	// // update the progress
	// private final Handler handler = new Handler() {
	//
	// public void handleMessage(Message msg) {
	//
	// // String aResponse = msg.getData().getString("message");
	//
	// // if ((null != aResponse)) {
	// //
	// // // ALERT MESSAGE
	// // threadMsg("Server Response: " + aResponse);
	// // } else {
	//
	// // ALERT MESSAGE
	// threadMsg("SERVER REQUEST TIME OUT.");
	// // }
	//
	// }
	//
	// };
	// });
	// background.start();
	// try {
	// BufferedReader reader = new BufferedReader(new InputStreamReader(
	// is, "iso-8859-1"), 8);
	// StringBuilder sb = new StringBuilder();
	// String line = null;
	// while ((line = reader.readLine()) != null) {
	// sb.append(line + "\n");
	// }
	// is.close();
	// json = sb.toString();
	// System.out.println("here 3");
	// } catch (Exception e) {
	// Log.e("Buffer Error", "Error converting result " + e.toString());
	// }
	//
	// // try parse the string to a JSON object
	// try {
	// jObj = new JSONObject(json);
	// } catch (Exception e) {
	// Log.e("JSON Parser", "Error parsing data " + e.toString());
	// }
	// background.start();
	// // return JSON String
	// return jObj;
	//
	// }

	public JSONArray getJSONArrayFromUrl(String url) {
		JSONArray jObj = null;
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);

			long requestStratTime = new Date().getTime();
			long requestEndTime = new Date().getTime();
			Log.d("requestStratTime", "requestStratTime" + requestStratTime);
			Log.d("requestEndTime", "requestEndTime" + requestEndTime);
			long timeOfRequest = (requestEndTime - requestStratTime) / 1000;
			Log.d("timeOfRequest", "timeOfRequest" + timeOfRequest);
			if (httpResponse == null && timeOfRequest > TIME_OUT_IN_SECONDS) {

				// throw new TimeoutException();
				// ALERT MESSAGE
				threadMsg("SERVER REQUEST TIME OUT.");

			}

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			System.out.println("here 3");
			Log.e("JSON", "" + json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONArray(json);
		} catch (Exception e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	public JSONObject getJSONFromUrlPost(String url) {
		JSONObject jObj = null;
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpGet = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);

			long requestStratTime = new Date().getTime();
			long requestEndTime = new Date().getTime();
			Log.d("requestStratTime", "requestStratTime" + requestStratTime);
			Log.d("requestEndTime", "requestEndTime" + requestEndTime);
			long timeOfRequest = (requestEndTime - requestStratTime) / 1000;
			Log.d("timeOfRequest", "timeOfRequest" + timeOfRequest);
			if (httpResponse == null && timeOfRequest > TIME_OUT_IN_SECONDS) {

				// throw new TimeoutException();
				// ALERT MESSAGE
				threadMsg("SERVER REQUEST TIME OUT.");

			}

			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			System.out.println("here 3");
			Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (Exception e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

}
