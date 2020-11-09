package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OpenTheWebsiteActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_the_website);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        final Activity activity=this;
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String fallingUrl) {
                super.onReceivedError(view, errorCode,description,fallingUrl);
                Toast.makeText(activity,description,Toast.LENGTH_LONG).show();
            }
        });
        webView.loadUrl("https://msu.mnp.art.pl/");
        setContentView(webView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout);
    }
}