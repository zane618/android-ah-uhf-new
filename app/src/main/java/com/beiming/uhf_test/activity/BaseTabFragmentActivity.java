package com.beiming.uhf_test.activity;

import android.app.AlertDialog;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.login.LoginActivity;
import com.beiming.uhf_test.adapter.ViewPagerAdapter;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.fragment.KeyDwonFragment;
import com.beiming.uhf_test.utils.TimeUtils;
import com.beiming.uhf_test.widget.NoScrollViewPager;
import com.hc.pda.HcPowerCtrl;
import com.kongzue.baseframework.util.SettingsUtil;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-03-10.
 */
public abstract class BaseTabFragmentActivity extends BaseActivity {

    private final int offscreenPage = 4; //����ViewPager���ڵļ���ҳ��

    protected ActionBar mActionBar;


    protected NoScrollViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;


    protected List<KeyDwonFragment> lstFrg = new ArrayList<KeyDwonFragment>();
    protected List<String> lstTitles = new ArrayList<String>();

    // public Reader mReader;
    private int index = 0;

    private ActionBar.Tab tab_kill, tab_lock, tab_set;
    private DisplayMetrics metrics;
    private AlertDialog dialog;
    private long[] timeArr;

    //合并新版本后添加的变量
    HcPowerCtrl ctrl;
    RfidPower power;
    public Reader uhfReader;

    public String TAG = "TAG";
    public MutableLiveData<Boolean> initState = new MutableLiveData<>();
    public boolean isR2000 = true;
    public SettingsUtil settingsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    protected void initViewPageData() {

    }

    /**
     * ����ActionBar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
//		return super.onCreateOptionsMenu(menu);
    }

    protected void initViewPager() {

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), lstFrg, lstTitles);

        mViewPager = (NoScrollViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(offscreenPage);
    }

    protected void initTabs() {
        for (int i = 0; i < mViewPagerAdapter.getCount() - 1; ++i) {
            mActionBar.addTab(mActionBar.newTab()
                    .setText(mViewPagerAdapter.getPageTitle(i)).setTabListener(mTabListener));
        }
//		tab_kill = mActionBar.newTab().setText(mViewPagerAdapter.getPageTitle(3)).setTabListener(mTabListener);
//		tab_lock = mActionBar.newTab().setText(mViewPagerAdapter.getPageTitle(4)).setTabListener(mTabListener);
//		tab_set = mActionBar.newTab().setText(mViewPagerAdapter.getPageTitle(5)).setTabListener(mTabListener);
        tab_set = mActionBar.newTab().setText(mViewPagerAdapter.getPageTitle(3)).setTabListener(mTabListener);

        //��Ӳ˵�
//        mActionBar.addTab(mActionBar.newTab().setText(getString(R.string.myMenu)).setTabListener(mTabListener));
    }


    protected ActionBar.TabListener mTabListener = new ActionBar.TabListener() {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            long start = TimeUtils.beginTime();
            if (mActionBar.getTabCount() > 3 && tab.getPosition() != 3) {
                mActionBar.removeTabAt(3);
            }
            if (tab.getPosition() == 3) {
                mViewPager.setCurrentItem(index, false);
            } else {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            long end = TimeUtils.beginTime();
            Log.i("CMCC", "时间差=" + (end - start));
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        /*@Override
        public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
            long start = TimeUtils.beginTime();
            if (mActionBar.getTabCount() > 3 && tab.getPosition() != 3) {
                mActionBar.removeTabAt(3);
            }
            if (tab.getPosition() == 3) {
                mViewPager.setCurrentItem(index, false);
            } else {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            long end = TimeUtils.beginTime();
            Log.i("CMCC", "时间差=" + (end - start));
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

        }*/
    };

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        //
        if (mActionBar.getSelectedTab().getText().equals(item.getTitle())) {
            return true;
        }
        if (mActionBar.getTabCount() > 3 && item.getItemId() != android.R.id.home && item.getItemId() != R.id.UHF_ver) {
            mActionBar.removeTabAt(3);
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                exitBy2Click(); //调用双击退出函数
                break;
/*			case R.id.action_kill:
                index = 3;
				mActionBar.addTab(tab_kill, true);
				break;
			case R.id.action_lock:
				index = 4;
				mActionBar.addTab(tab_lock, true);
				break;*/
            case R.id.action_set:
                index = 3;
                mActionBar.addTab(tab_set, true);
                break;

            case R.id.UHF_ver:
//                getUHFVersion();
                exitLogin();
                break;
            default:
                break;
        }

        return true;
    }

    //双击退出程序
    protected abstract void exitBy2Click();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void gotoActivity(Intent it) {
        startActivity(it);
    }

    public void gotoActivity(Class<? extends BaseTabFragmentActivity> clz) {
        Intent it = new Intent(this, clz);
        gotoActivity(it);
    }

    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toastMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void exitLogin() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("确认退出当前登录用户吗");
            builder.setIcon(R.drawable.webtext);

            builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(BaseTabFragmentActivity.this, LoginActivity.class));
                    finish();
                }
            });
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
