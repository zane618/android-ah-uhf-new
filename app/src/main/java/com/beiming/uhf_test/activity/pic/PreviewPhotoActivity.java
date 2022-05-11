package com.beiming.uhf_test.activity.pic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.pic.AttachmentUpdate;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.pic.Bimp;
import com.beiming.uhf_test.utils.pic.BitmapUtils;
import com.beiming.uhf_test.utils.pic.PhotoView;
import com.beiming.uhf_test.widget.ViewPagerFixed;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;



/**
 * Description：进行图片浏览,可删除
 * <p>
 * Created by Mjj on 2016/12/2.
 */

public class PreviewPhotoActivity extends AppCompatActivity {

    private Intent intent;
    // 返回图标
    private TextView back_bt;
    //返回按钮
    private ImageView ivBack;
    //图片描述的布局
    private LinearLayout ll_des;
    private Button button;
    private EditText describe;
    // 发送按钮
    private Button send_bt;
    //删除图标
    private ImageView del_bt;

    //获取前一个activity传过来的position
    private int position;
    //当前的位置
    private int location = 0;

    private ArrayList<View> listViews = null;
    private ViewPagerFixed pager;
    private MyPageAdapter adapter;

    private Context mContext;
    private boolean isApproval = false;
    private ArrayList<PhotoBean> attachmentPaths;
    private ArrayList<PhotoBean> deletePhotoBean = new ArrayList<>();
    private int flage = 0;
    private boolean isEdit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        attachmentPaths = (ArrayList<PhotoBean>) bundle.getSerializable(ConstantUtil.PHOTO_BEAN_LIST);
        back_bt = (TextView) findViewById(R.id.gallery_back);
        send_bt = (Button) findViewById(R.id.send_button);
        //隐藏完成按钮
//        send_bt.setVisibility(View.GONE);
        del_bt = (ImageView) findViewById(R.id.gallery_del);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        //编辑描述控件
        ll_des = (LinearLayout) findViewById(R.id.ll_des);
        describe = (EditText) findViewById(R.id.et_describe);
        button = (Button) findViewById(R.id.btn_describe);
        describe.setEnabled(false);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEdit) {//开始编辑
                    button.setText("完成");
                    describe.setEnabled(true);
                    describe.requestFocus();
//                    if (TextUtils.isEmpty(imageDescribe)) {
//                        describe.setText("");
//                    } else {
//                        describe.setSelection(describe.getText().toString().length());
//                    }
                    startKeyboard(describe);
                } else {//编辑结束
                    button.setText("编辑");
                    describe.setEnabled(false);
                    attachmentPaths.get(pager.getCurrentItem()).setDescribe(describe.getText().toString());
                    closeKeyboard(v);
//                    if (TextUtils.isEmpty(describe.getText().toString())) {
//                        describe.setText(imageResource);
//                    }
                    //TODO 保存
                }
                isEdit = !isEdit;
            }
        });
        //隐藏删除按钮
//        del_bt.setVisibility(View.GONE);
        // 返回键监听
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AttachmentUpdate attachmentUpdate = new AttachmentUpdate(ConstantUtil.REFRESH_PIC_DES_LIST_FROM_PREVIEW, attachmentPaths);
                attachmentUpdate.setDeletePhotoBeans(deletePhotoBean);
                EventBus.getDefault().post(attachmentUpdate);
                finish();
            }
        });
        send_bt.setOnClickListener(new GallerySendListener());
        del_bt.setOnClickListener(new DelListener());
        intent = getIntent();
        int creatOrDetails = intent.getIntExtra(ConstantUtil.CREAT_OR_DETAILS, 0);
        if (creatOrDetails == 0) {
            //创建
            del_bt.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
        } else {
            //详情
            del_bt.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        }
        isShowOkBt();
        pager = (ViewPagerFixed) findViewById(R.id.gallery01);
        pager.addOnPageChangeListener(pageChangeListener);
        for (PhotoBean bean : attachmentPaths) {
            initListViews(bean.getImageSrc());
        }

        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        pager.setPageMargin(10);
        pager.setOffscreenPageLimit(9);

        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
        describe.setText(attachmentPaths.get(id).getDescribe());

        //todo viewpage设置点击事件
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        flage = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        flage = 1;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (flage == 0) {
                            int state = ll_des.getVisibility();
                            if (state == View.VISIBLE) {
                                ll_des.setVisibility(View.GONE);
                            } else {
                                ll_des.setVisibility(View.VISIBLE);
                            }
                        }
                        break;


                }
                return false;
            }
        });
    }

    /**
     * 弹出软键盘
     *
     * @param editText
     */
    public void startKeyboard(EditText editText) {
        // 弹出软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    /**
     * 关闭软键盘
     */
    public void closeKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
            back_bt.setText((location + 1) + " / " + attachmentPaths.size());
            String des = attachmentPaths.get(location).getDescribe();
            if (TextUtils.isEmpty(des)) {
                describe.setText("");
            } else {
                describe.setText(des);
            }
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        PhotoView img = new PhotoView(this);
        img.setImageBitmap(bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    private void initListViews(String path) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        final PhotoView img = new PhotoView(this);
//        img.setImageBitmap(bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
//        Glide.with(mContext).load(path).placeholder(R.mipmap.iv_head).into(new SimpleTarget<GlideDrawable>() {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                img.setImageDrawable(resource);
//            }
//        });
        BitmapUtils.displayImage(mContext, path, R.mipmap.icon_error, img);
        listViews.add(img);
    }

    // 删除按钮添加的监听器
    private class DelListener implements OnClickListener {

        public void onClick(View v) {
            if (listViews.size() == 1) {
                deletePhotoBean.add(attachmentPaths.get(0));
                attachmentPaths.clear();
                back_bt.setText("0 / 0");

                //================================================================================================
                AttachmentUpdate attachmentUpdate = new AttachmentUpdate(ConstantUtil.REFRESH_PIC_DES_LIST_FROM_PREVIEW, attachmentPaths);
                attachmentUpdate.setDeletePhotoBeans(deletePhotoBean);
                EventBus.getDefault().post(attachmentUpdate);
                finish();
            } else {
                deletePhotoBean.add(attachmentPaths.get(location));
                attachmentPaths.remove(location);
                pager.removeAllViews();
                listViews.remove(location);
                adapter.setListViews(listViews);
                back_bt.setText((location + 1) + " / " + attachmentPaths.size());
                adapter.notifyDataSetChanged();
            }

            /*else {
                if (listViews.size() == 1) {
                    Bimp.tempSelectBitmap.clear();
                    Bimp.max = 0;
                    back_bt.setText("" + Bimp.tempSelectBitmap.size() + " / 3");
                    Intent intent = new Intent("data.broadcast.action");
                    sendBroadcast(intent);
                    finish();
                } else {
                    Bimp.tempSelectBitmap.remove(location);
                    Bimp.max--;
                    pager.removeAllViews();
                    listViews.remove(location);
                    adapter.setListViews(listViews);
                    back_bt.setText("" + Bimp.tempSelectBitmap.size() + " / 3");
                    adapter.notifyDataSetChanged();
                }
            }*/

        }
    }

    // 完成按钮的监听
    private class GallerySendListener implements OnClickListener {
        public void onClick(View v) {
///==================================================================================================
            if (isApproval) {
                EventBus.getDefault().post(new AttachmentUpdate(ConstantUtil.REFRESH_PIC_DES_LIST_FROM_PREVIEW, attachmentPaths));
            } else {
//                intent.setClass(mContext, GatherDataActivity.class);
//                startActivity(intent);
            }
            finish();
        }
    }

    public void isShowOkBt() {
        if (isApproval) {
            if (null != attachmentPaths && attachmentPaths.size() > 0) {
                back_bt.setText((location + 1) + " / " + attachmentPaths.size());
                send_bt.setPressed(true);
                send_bt.setClickable(true);
                send_bt.setTextColor(Color.WHITE);
            } else {
                send_bt.setPressed(false);
                send_bt.setClickable(false);
                send_bt.setTextColor(Color.parseColor("#E1E0DE"));
            }
            return;
        }

        if (Bimp.tempSelectBitmap.size() > 0) {
            back_bt.setText("" + Bimp.tempSelectBitmap.size() + " / 3");
            send_bt.setPressed(true);
            send_bt.setClickable(true);
            send_bt.setTextColor(Color.WHITE);
        } else {
            send_bt.setPressed(false);
            send_bt.setClickable(false);
            send_bt.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(listViews.get(position % size));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                container.addView(listViews.get(position % size), 0);
            } catch (Exception e) {
            }
            return listViews.get(position % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AttachmentUpdate attachmentUpdate = new AttachmentUpdate(ConstantUtil.REFRESH_PIC_DES_LIST_FROM_PREVIEW, attachmentPaths);
            attachmentUpdate.setDeletePhotoBeans(deletePhotoBean);
            EventBus.getDefault().post(attachmentUpdate);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
