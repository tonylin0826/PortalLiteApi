package com.coderobot.portalliteapi;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class PortalLiteApi extends Service {
    private static final String TAG = "PortalLiteApiService";

    private MyBinder mBinder = new MyBinder();
    private WebView mWebView;
    private WindowManager mWindowManager;

    public PortalLiteApi() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log("onCreate");
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 150;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 800;

        mWebView = new WebView(this);

        WebSettings ws = mWebView.getSettings();
        ws.setSaveFormData(false);
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(false);

        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "HTMLOUT");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+ document.getElementsByTagName('html')[0].innerHTML + '</head>'); ");
            }
        });

        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mWebView.loadUrl("https://portalx.yzu.edu.tw/PortalSocialVB/Login.aspx");


        mWindowManager.addView(mWebView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");

        mWindowManager.removeViewImmediate(mWebView);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {

        public void test() {
            String jsSetLogin = "javascript: var x = document.getElementById('Txt_UserID').value = 's1003344';" +
                    "x = document.getElementById('Txt_Password').value = '19921011';" +
                    "x = document.getElementById('ibnSubmit').click();";
            mWebView.loadUrl(jsSetLogin);
        }
    }

    public class DemoJavaScriptInterface {

        @JavascriptInterface
        public void showHTML(String html) {
            log("showHTML");
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView vw, String url, String msg, JsResult ret) {
            log("js alert = " + msg);
            ret.confirm();
            return true;
        }
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
