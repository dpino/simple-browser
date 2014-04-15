package com.example.simplebrowser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    private static final String DEFAULT_URL = "http://www.google.com";

	private WebView mWebView;
	private EditText mAddressBar;
	private ProgressBar mProgressBar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_main);
        
        wireComponents();
        initializeWebView();
        initializeAddressBar();
    }

	private void wireComponents() {
        mWebView = (WebView) findViewById(R.id.webview);
        mAddressBar = (EditText) findViewById(R.id.addressbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
	}

	private void initializeWebView() {
		// Force links and redirects to open in the WebView instead of in a
		// browser
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.setWebChromeClient(new MyWebChromeClient());

		// Enable JavaScript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
	}
	
	private void hideProgressBar() {
		mProgressBar.setVisibility(View.GONE);
	}

	private void showProgressBar() {
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			MainActivity.this.hideProgressBar();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			MainActivity.this.showProgressBar();
		}

	};
	
	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			MainActivity.this.setProgressValue(newProgress);
			super.onProgressChanged(view, newProgress);
		}
	}
	
	public void setProgressValue(int progress) {
        mProgressBar.setProgress(progress);        
    }

    private void initializeAddressBar() {
    	mAddressBar.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					hideKeyboard();
					loadUrl(getUrlAddress());
				}
			return false;
			}
    		
			private void hideKeyboard() {
				InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(
						mAddressBar.getApplicationWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
    	});    	
    	setUrlAddress(DEFAULT_URL);
	}

    private void setUrlAddress(String url) {
    	mAddressBar.setText(url);
		loadUrl(url);    	
    }

	private void loadUrl(String url) {
		mProgressBar.setVisibility(View.VISIBLE);
		mWebView.loadUrl(url);
		mWebView.requestFocus();
		mProgressBar.setProgress(0);
	}

    private String getUrlAddress() {
    	return mAddressBar.getText().toString();
    }

}
