package com.example.chegus.learningTime.util;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class CommonReadText extends Activity{
	

	/*This readNewTxt Method LoadProperty as Sting value
	 * loadProperty = intent.getStringExtra("loadProperty");
	 * 
	 * And Pass The InputStream as a 
	 * inputStream = getResources().getAssets().open(loadProperty);
	 * Pass This value for Read The Assets Manager Files 
	 * .
	 * */
	
	public static void readNewTxt(InputStream inputStream, WebView browser){
		 					
		 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		 int i;
		 try {
		 i = inputStream.read();
		while (i != -1)
		  {
		   byteArrayOutputStream.write(i);
		   i = inputStream.read();
		   }
		    inputStream.close();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 
		 
		 String urls = byteArrayOutputStream.toString();
			StringBuilder html = new StringBuilder();
			html.append( "<?xml version='1.0' encoding='utf-8'?>");
			html.append("<html>");
			html.append("<head>");
			html.append("<link rel=stylesheet href='css/style1.css'>");
			html.append("<link rel=stylesheet href='css/js-image-slider.css'>");
			html.append("<link rel=stylesheet href='css/tooltip.css'>");
			html.append("</head>");
			html.append("<body>");
			html.append(urls);
			html.append("</body></html>");
			
			browser.getSettings().setLoadsImagesAutomatically(true);
		    browser.getSettings().setJavaScriptEnabled(true);
		    browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			browser.loadDataWithBaseURL("file:///android_asset/",html.toString() , "text/html", "utf-8", "");
			browser.setWebViewClient(new MyBrowser());
			//return browser;
	 }
}


class MyBrowser extends WebViewClient {
@Override
public boolean shouldOverrideUrlLoading(WebView view, String url) {
view.loadUrl(url);
return true;
}
}
