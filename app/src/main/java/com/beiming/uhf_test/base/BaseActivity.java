package com.beiming.uhf_test.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.beiming.uhf_test.App;
import com.beiming.uhf_test.dialog.LoadingDialog;
import com.beiming.uhf_test.tools.AppManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by htj on 2018/10/31.
 */

public abstract class BaseActivity extends AppCompatActivity {
    //获取TAG的activity名称
    protected final String TAG = this.getClass().getSimpleName();
    private ProgressDialog mProgressDialog;
    private App mApplication;
    private Unbinder bind;
    public BaseActivity activity;
    //是否允许旋转屏幕
    private boolean isAllowScreenRoate = true;
    //封装Toast对象
    private static Toast toast;

    public EditText etSearch;//输入框
    public ImageView ivScan;//扫描按钮
    public ImageView ivSearch;//查询按钮
    public FrameLayout llPopSearch;//弹窗要显示的位置控件
    public boolean isFirstPopShow = true;//判断是否是第一次显示弹窗
    public LoadingDialog dialog;//弹框

    public String titleTextV = "";//标题

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView();
        /**
         *  所有的Activity都依附于一个Application，在Activity中只要通过 getApplication（）方法，就能拿到当前应用中的Application对象
         *
         */
        activity = this;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        bind = ButterKnife.bind(this);
        //设置屏幕是否可旋转
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //初始化控件
        initView();
        //初始化监听
        initListener();
        //设置数据
        initData();
        //隐藏虚拟按键
//        hideBottomUIMenu();
        //设置软键盘弹出遮盖其他布局
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    protected void getData(){

    }

    @Override
    public void finish() {
        super.finish();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 显示dialog
     *
     * @param msg dialog中要显示的信息
     */
    public void showDialog(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    private void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {

            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            _window.setAttributes(params);
        }
    }

    /**
     * 隐藏dialog
     */
    public void hideDialog() {
        mProgressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
//        mApplication.removeActivity(this);
        mProgressDialog.dismiss();
    }

    /**
     * 设置Activity加载的View资源ID
     *
     * @return 资源ID
     */
    protected abstract void setContentView();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 设置数据
     */
    protected abstract void initData();

    /**
     * 是否允许屏幕旋转
     *
     * @param allowScreenRoate true or false
     */
    public void setAllowScreenRoate(boolean allowScreenRoate) {
        isAllowScreenRoate = allowScreenRoate;
    }

    /**
     * 保证同一按钮在1秒内只会响应一次点击事件
     */
    public abstract class OnSingleClickListener implements View.OnClickListener {
        //两次点击按钮之间的间隔，目前为1000ms
        private static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime;

        public abstract void onSingleClick(View view);

        @Override
        public void onClick(View view) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime;
                onSingleClick(view);
            }
        }
    }

    /**
     * 同一按钮在短时间内可重复响应点击事件
     */
    public abstract class OnMultiClickListener implements View.OnClickListener {
        public abstract void onMultiClick(View view);

        @Override
        public void onClick(View v) {
            onMultiClick(v);
        }
    }

    /**
     * 显示提示  toast
     *
     * @param msg 提示信息
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        try {
            if (null == toast) {
                toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
            } else {
                View view = toast.getView();
                toast.cancel();
                toast = new Toast(activity);
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setText(msg);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }


    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && null != imm) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && null != imm) {
            imm.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     *  重写dispatchTouchEvent
     * @param ev 执行的用户行为
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
