package android.ye.zhbjj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.ye.zhbjj.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by ye on 2016/11/8.
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener{

    private TextView tvNewsDetail;
    private ImageButton btnBack;
    private ImageButton btnTextSize;
    private ImageButton btnShare;
    private WebView wvNewsDetail;
    private ProgressBar pbLoading;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initUI();
        initOnClick();
        initData();

    }

    private void initData() {
        //获取传递的url地址
        mUrl = getIntent().getStringExtra("url");
        //webview加载URL
        wvNewsDetail.loadUrl(mUrl);
        //设置WebView
        WebSettings settings = wvNewsDetail.getSettings();
        //显示缩放按钮
        settings.setBuiltInZoomControls(true);
        //支持双击缩放
        settings.setUseWideViewPort(true);
        //支持JS功能
        settings.setJavaScriptEnabled(true);

        wvNewsDetail.setWebViewClient(new WebViewClient() {
            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoading.setVisibility(View.VISIBLE);
            }

            //网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.INVISIBLE);
            }

            //跳转链接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 在跳转链接时强制在当前webview中加载
                view.loadUrl(url);
                return true;
            }
        });

        wvNewsDetail.setWebChromeClient(new WebChromeClient() {

            //进度变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            //网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    private void initOnClick() {
        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);


    }

    private void initUI() {
        tvNewsDetail = (TextView) findViewById(R.id.tv_news_ac_title);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnTextSize = (ImageButton) findViewById(R.id.btn_textsize);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        wvNewsDetail = (WebView) findViewById(R.id.wv_news_details);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);

    }

    private int mTempWhich; //记录临时选中的字体大小(点击确定之前)
    private int mCurrentWhich =1;//记录当前选中的字体大小
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};

        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempWhich = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //跟具选择的字体大小来修改网页字体的大小
                WebSettings settings = wvNewsDetail.getSettings();
                switch (mTempWhich) {
                    case 0:
                        //超大字体
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        //大号字体
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        //正常字体
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        //小号字体
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        //超小号字体
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentWhich = mTempWhich;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
