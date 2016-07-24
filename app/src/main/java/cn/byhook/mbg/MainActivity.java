package cn.byhook.mbg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    /**
     * 切换动画时长
     */
    private static final int SWTICH_DURATION = 1000;

    private int[] mResDrawable = {R.drawable.bg1_login, R.drawable.bg2_login, R.drawable.bg3_login};

    private int[] mAnimations = {R.anim.login_anim_toleft, R.anim.login_anim_toright};

    /**
     * 登陆切换
     */
    private ImageView mIvLoginSwitch;

    /**
     * 计时器
     */
    private Timer mTimer;

    /**
     * 背景
     */
    private Drawable[] mChanges;

    /**
     * 背景下标
     */
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void initView() {
        mIvLoginSwitch = (ImageView) findViewById(R.id.iv_login_switch);
        mIvLoginSwitch.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //宽放大1.5倍 CENTER_CROP模式
        mIvLoginSwitch.setLayoutParams(
                new FrameLayout.LayoutParams((int) (getScreenWidth() * 1.5F),
                        FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private int getScreenWidth() {
        DisplayMetrics mDisplayMetrics = getApplicationContext().getResources()
                .getDisplayMetrics();
        return mDisplayMetrics.widthPixels;
    }

    private void init() {
        mChanges = new Drawable[mResDrawable.length];
        try {
            for (int i = 0; i < mResDrawable.length; i++) {
                Bitmap bp = BitmapFactory.decodeResource(getResources(), mResDrawable[i]);
                mChanges[i] = new BitmapDrawable(getResources(), bp);
            }
            handleSwitch();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理混合动画
     */
    private void handleSwitch() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(index++);
            }
        }, 0, 5000);
    }

    /**
     * 处理切换事件
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            final int count = msg.what;

            final TransitionDrawable transitionDrawable = new TransitionDrawable(
                    new Drawable[]{mChanges[count % 3], mChanges[(count + 1) % 3]});

            mIvLoginSwitch.setImageDrawable(transitionDrawable);
            //切换时长
            transitionDrawable.startTransition(SWTICH_DURATION);

            //补间动画
            final AnimationSet anim = (AnimationSet) AnimationUtils.loadAnimation(MainActivity.this, mAnimations[msg.what % 2]);
            anim.setFillAfter(true);
            mIvLoginSwitch.setAnimation(anim);
        }
    };

}
