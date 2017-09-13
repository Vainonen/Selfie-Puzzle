package com.kirja.xxx.selfiepuzzle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class PuzzleView extends Activity {

    String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filepath = getIntent().getStringExtra("FILEPATH");
        Log.i("Reitti", filepath);
        setContentView(R.layout.activity_webview);
        WebView webview = (WebView) findViewById(R.id.webview);
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
/*
        final Activity activity = this;
        activity_webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(PuzzleView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        activity_webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(PuzzleView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
*/
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        webview.loadUrl("file:///android_asset/main2.html");
    }
    class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public String getImageUri() {
            return filepath;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void log(String log) {
            Log.i("javascript", log);
        }
    }
}
