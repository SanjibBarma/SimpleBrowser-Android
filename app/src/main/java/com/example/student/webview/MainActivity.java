package com.example.student.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progress;
    private ImageView icons;
    private WebView myWebView;
    private LinearLayout layout;
    private SwipeRefreshLayout swipeLayout;
    private String MyCurrentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catchIDs();
    }

    protected void catchIDs(){
        layout = (LinearLayout) findViewById(R.id.layout);
        icons = (ImageView) findViewById(R.id.icons);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setMax(100);

        myWebView = (WebView) findViewById(R.id.myWebView);
        myWebView.loadUrl("https://www.google.com");
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progress.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                icons.setImageBitmap(icon);
            }
        });

        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                layout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                layout.setVisibility(View.GONE);
                swipeLayout.setRefreshing(false);
                super.onPageFinished(view, url);
                MyCurrentUrl = url;
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWebView.reload();
            }
        });
    }


    /*end of catch ids*/

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()){
            myWebView.goBack();
        }else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.super_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_back:
                onBackPressed();
                break;
            case R.id.menu_forward:
                onForwardProcess();
                break;
            case R.id.refresh:
                myWebView.reload();
                break;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, MyCurrentUrl);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Copied URL");
                startActivity(Intent.createChooser(shareIntent,"Share url with your friend..."));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onForwardProcess(){
        if (myWebView.canGoForward()){
            myWebView.goForward();
        }else {
            Toast.makeText(getApplicationContext(),"Cant go further!",Toast.LENGTH_SHORT).show();
        }
    }

}
