package com.example.praca_inz_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//----------------------activity displaying the museum website--------------------------------------

public class OpenTheWebsiteActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_the_website);
        WebView webView = new WebView(this);
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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}