package android.ye.zhbjj.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.ye.zhbjj.R;
import android.ye.zhbjj.activity.GuideActivity;
import android.ye.zhbjj.utils.ConstantValue;
import android.ye.zhbjj.utils.SpUtils;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {

    private RelativeLayout rlSplash;
    private boolean isFirstEnter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        initAnim();
    }

    private void initAnim() {
        //旋转动画
        Animation rotated = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotated.setDuration(1000);
        rotated.setFillAfter(true);

        //缩放动画
        Animation scal = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scal.setDuration(1000);
        scal.setFillAfter(true);

        //渐变动画
        Animation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(1000);
        alpha.setFillAfter(true);

        //新建动画集合
        AnimationSet set = new AnimationSet(true);
        //添加动画到集合中
        set.addAnimation(rotated);
        set.addAnimation(scal);
        set.addAnimation(alpha);

        //开启动画
        rlSplash.startAnimation(set);

        //动画监听
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //如果第一次进入则跳转到新手引导界面，否则直接跳转到主页面
                isFirstEnter = SpUtils.getBoolean(getApplicationContext(), ConstantValue.ISFIRST_ENTER, true);
                if (isFirstEnter){
                    //新手引导页面
                    intent = new Intent(getApplicationContext(),GuideActivity.class);
                }else {
                    //主页面
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                }

                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        
    }

    private void initUI() {
        rlSplash = (RelativeLayout) findViewById(R.id.rl_splash);
    }
}
