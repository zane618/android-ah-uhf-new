package com.beiming.uhf_test.activity.login;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.bean.LoginBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.greendao.gen.LoginBeanDao;
import com.beiming.uhf_test.utils.PWDUtils;
import com.beiming.uhf_test.utils.ToastUtils;

import java.util.List;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;

    private ImageView mIv1;
    private ImageView mIv2;
    private ImageView mIv3;

    private LinearLayout mLlStep1;
    private LinearLayout mLlStep2;
    private LinearLayout mLlStep3;

    private TextView mTvQuestion3;
    private TextView mTvQuestion2;
    private TextView mTvQuestion1;

    private boolean step1IsVissble = true;
    private boolean step2IsVissble = false;
    private boolean step3IsVissble = false;

    private String name;
    private String pwd;
    private String crop;

    private LoginBeanDao loginBeanDao;

    @Override
    protected int onCreateView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_register;
    }


    @Override
    protected void initView() {
        loginBeanDao = GreenDaoManager.getInstance().getNewSession().getLoginBeanDao();

        mTv1 = (TextView) findViewById(R.id.tv_1);
        mTv2 = (TextView) findViewById(R.id.tv_2);
        mTv3 = (TextView) findViewById(R.id.tv_3);

        mIv1 = (ImageView) findViewById(R.id.iv_1);
        mIv2 = (ImageView) findViewById(R.id.iv_2);
        mIv3 = (ImageView) findViewById(R.id.iv_3);

        mLlStep1 = (LinearLayout) findViewById(R.id.ll_step1);
        mLlStep2 = (LinearLayout) findViewById(R.id.ll_step2);
        mLlStep3 = (LinearLayout) findViewById(R.id.ll_step3);

        findViewById(R.id.tv_next).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_register_ok).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next://第一步：下一步
                EditText mEtName = (EditText) findViewById(R.id.et_name);
                EditText mEtPwd = (EditText) findViewById(R.id.et_pwd);
                EditText mEtCrop = (EditText) findViewById(R.id.et_crop);

                name = mEtName.getText().toString().trim();
                pwd = mEtPwd.getText().toString().trim();
                crop = mEtCrop.getText().toString().trim();

                if (isEmpty(name, pwd, crop)) {//如果输入的信息不为空，执行以下逻辑
                    step1IsVissble = false;
                    step2IsVissble = true;
                    step3IsVissble = false;
                    setView(step1IsVissble, step2IsVissble, step3IsVissble);
                }
                break;
            case R.id.tv_register://第二步：输入验证信息
                EditText mEtAnswer1 = (EditText) findViewById(R.id.et_answer1);
                EditText mEtAnswer2 = (EditText) findViewById(R.id.et_answer2);
                EditText mEtAnswer3 = (EditText) findViewById(R.id.et_answer3);
                String answer1 = mEtAnswer1.getText().toString().trim();
                String answer2 = mEtAnswer2.getText().toString().trim();
                String answer3 = mEtAnswer3.getText().toString().trim();
                if (!TextUtils.isEmpty(answer1) && !TextUtils.isEmpty(answer2) && !TextUtils.isEmpty(answer3)) {
                    //保存数据
                    int size = loginBeanDao.loadAll().size();
                    if (size == 0) {
                        loginBeanDao.insert(new LoginBean((long) 0, name, pwd, "", "", "1", "","","",crop, answer1, answer2, answer3));
                    } else {
                        loginBeanDao.insert(new LoginBean((long) size, name, pwd, "", "", "1","","","", crop, answer1, answer2, answer3));
                    }
                    //切换到第三个布局
                    step1IsVissble = false;
                    step2IsVissble = false;
                    step3IsVissble = true;
                    setView(step1IsVissble, step2IsVissble, step3IsVissble);
                } else {
                    ToastUtils.showToastForLong(this, "三个问题必须填写！以便于您找回密码！");
                }

                break;
            case R.id.tv_register_ok://第三步：完成
                finish();
                break;
        }
    }

    /**
     * 验证用户名和密码
     *
     * @param name
     * @param pwd
     * @param crop
     */
    private boolean isEmpty(String name, String pwd, String crop) {
        boolean isEmpty = true;
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToastForLong(this, "用户名称不能为空");
            isEmpty = false;
            return isEmpty;
        }
        List<LoginBean> beanLists = loginBeanDao.
                queryRaw("WHERE USER_NAME = ?", name);
        if (beanLists.size() > 0 && beanLists != null) {
            ToastUtils.showToastForLong(this, "用户名已存在，请更换！");
            isEmpty = false;
            return isEmpty;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToastForLong(this, "密码不能为空");
            isEmpty = false;
            return isEmpty;
        }
        if (TextUtils.isEmpty(crop)) {
            ToastUtils.showToastForLong(this, "所在项目组不能为空");
            isEmpty = false;
            return isEmpty;
        }
        if (PWDUtils.PwdIsOk(pwd)) {
            return true;
        } else {
            ToastUtils.showToastForLong(this, "密码必须为6-18位字母或数字");
            return false;
        }

    }

    /**
     * 根据页面
     *
     * @param step1IsVissble
     * @param step2IsVissble
     * @param step3IsVissble
     */
    private void setView(boolean step1IsVissble, boolean step2IsVissble, boolean step3IsVissble) {
        if (step1IsVissble) {
            mTv1.setTextColor(getResources().getColor(R.color.blue_00a0e9));
            mIv1.setBackgroundResource(R.drawable.shape_circle_blue);
            mLlStep1.setVisibility(View.VISIBLE);
            mTv2.setTextColor(getResources().getColor(R.color.gray_666));
            mIv2.setBackgroundResource(R.drawable.shape_circle_gray);
            mLlStep2.setVisibility(View.GONE);
            mTv3.setTextColor(getResources().getColor(R.color.gray_666));
            mIv3.setBackgroundResource(R.drawable.shape_circle_gray);
            mLlStep3.setVisibility(View.GONE);
        }
        if (step2IsVissble) {
            mTv1.setTextColor(getResources().getColor(R.color.gray_666));
            mIv1.setBackgroundResource(R.drawable.shape_circle_gray);
            mLlStep1.setVisibility(View.GONE);
            mTv2.setTextColor(getResources().getColor(R.color.blue_00a0e9));
            mIv2.setBackgroundResource(R.drawable.shape_circle_blue);
            mLlStep2.setVisibility(View.VISIBLE);
            mTv3.setTextColor(getResources().getColor(R.color.gray_666));
            mIv3.setBackgroundResource(R.drawable.shape_circle_gray);
            mLlStep3.setVisibility(View.GONE);
        }
        if (step3IsVissble) {
            mTv1.setTextColor(getResources().getColor(R.color.gray_666));
            mIv1.setBackgroundResource(R.drawable.shape_circle_gray);
            mLlStep1.setVisibility(View.GONE);
            mTv2.setTextColor(getResources().getColor(R.color.gray_666));
            mIv2.setBackgroundResource(R.drawable.shape_circle_gray);
            mLlStep2.setVisibility(View.GONE);
            mTv3.setTextColor(getResources().getColor(R.color.blue_00a0e9));
            mIv3.setBackgroundResource(R.drawable.shape_circle_blue);
            mLlStep3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (step1IsVissble) {
            finish();
        }
        if (step2IsVissble) {
            step1IsVissble = true;
            step2IsVissble = false;
            step3IsVissble = false;
        }
        if (step3IsVissble) {
            step1IsVissble = false;
            step2IsVissble = true;
            step3IsVissble = false;
        }
        setView(step1IsVissble, step2IsVissble, step3IsVissble);
    }
}
