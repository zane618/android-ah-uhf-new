package com.beiming.uhf_test.activity.pic;


import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.adapter.pic.FolderAdapter;
import com.beiming.uhf_test.adapter.pic.PhotoAdapter;
import com.beiming.uhf_test.bean.pic.AttachmentUpdate;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.FileUtil;
import com.beiming.uhf_test.utils.TimeUtils;
import com.beiming.uhf_test.utils.ToastUtils;
import com.beiming.uhf_test.utils.pic.OtherUtils;
import com.beiming.uhf_test.utils.pic.PhotoUtils;
import com.beiming.uhf_test.utils.pic.model.Photo;
import com.beiming.uhf_test.utils.pic.model.PhotoFolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Description：仿微信图片选择界面
 * <p>
 * Created by Mjj on 2016/12/2.
 */
public class PhotoPickerActivity extends AppCompatActivity implements PhotoAdapter.PhotoClickCallBack {
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    public final static String IMAGE_PACKGE_NAME = "imagePackgeName";
    public final static String TEST_TASK_BEAN = "test_task_bean";
    public final static String IS_TAKE_PHOTO = "is_take_photo";
    public final static String CREAT_YEAR = "creatYear";
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    public final static String KEY_RESULT = "picker_result";
    public final static int REQUEST_CAMERA = 1;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    /**
     * 是否显示相机
     */
    public final static String EXTRA_SHOW_CAMERA = "is_show_camera";
    /**
     * 照片选择模式
     */
    public final static String EXTRA_SELECT_MODE = "select_mode";
    /**
     * 最大选择数量
     */
    public final static String EXTRA_MAX_MUN = "max_num";
    /**
     * 总共选择的图片数量
     */
    public final static String TOTAL_MAX_MUN = "total_num";
    /**
     * 判断一年生和多年生的标识
     */
    public final static String MARK = "MARK";
    /**
     * 一年生的标识
     */
    public final static String YEARMARK = "一年生作物";
    /**
     * 多年生的标识
     */
    public final static String YEARSMARK = "多年生作物";
    /**
     * 征集表的标识
     */
    public final static String COLLECTMARK = "征集表";
    /**
     * 单选
     */
    public final static int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public final static int MODE_MULTI = 1;
    /**
     * 默认最大选择数量
     */
    public final static int DEFAULT_NUM = 3;

    private final static String ALL_PHOTO = "所有图片";
    /**
     * 是否显示相机，默认不显示
     */
    private boolean mIsShowCamera = false;
    /**
     * 是否显示相机，默认不显示
     */
    private boolean isTakePhoto = false;
    /**
     * 照片选择模式，默认是单选模式
     */
    private int mSelectMode = 0;
    /**
     * 最大选择数量，仅多选模式有用
     */
    private int mMaxNum;

    /**
     * 总共选择的图片数量
     */
    private int mTotalNum;

    /**
     * 当前样品存储图片的路径
     */
    /**
     * 图片保存目的地的文件夹名
     */
    private String imagePackgeName = "";

    private GridView mGridView;
    private Map<String, PhotoFolder> mFolderMap;
    private List<Photo> mPhotoLists = new ArrayList<>();
    private ArrayList<String> mSelectList = new ArrayList<>();
    private PhotoAdapter mPhotoAdapter;
    private ProgressDialog mProgressDialog;
    private ListView mFolderListView;

    private TextView mPhotoNumTV;
    private TextView mPhotoNameTV;
    private Button mCommitBtn;
    /**
     * 文件夹列表是否处于显示状态
     */
    boolean mIsFolderViewShow = false;
    /**
     * 文件夹列表是否被初始化，确保只被初始化一次
     */
    boolean mIsFolderViewInit = false;

    /**
     * 拍照时存储拍照结果的临时文件
     */
    private File mTmpFile;
    /**
     * 要展示的文件夹的路径
     */
    private String strPathOpenImageFIle = Environment.getExternalStorageDirectory() +
            File.separator + "nari/image/";
    private String creatYear;
    private int row;//行号
    private int lieNumber;//列的索引号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo_picker);
        initIntentParams();
        if (!OtherUtils.isExternalStorageAvailable()) {
            Toast.makeText(this, "No SD card!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查权限
//        checkPerm();
//        MPermissionUtils.requestPermissionsResult(PhotoPickerActivity.this, 1,
//                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE},
//                new MPermissionUtils.OnPermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        getPhotosTask.execute();
//                    }
//
//                    @Override
//                    public void onPermissionDenied() {
//                        MPermissionUtils.showTipsDialog(PhotoPickerActivity.this);
//                    }
//                });
        if (isTakePhoto) {
            autoObtainCameraPermission();
        } else {
            initView();
            getPhotosTask.execute();
        }
//        getPhotosTask.execute();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.photo_gridview);
        mPhotoNumTV = (TextView) findViewById(R.id.photo_num);
        mPhotoNameTV = (TextView) findViewById(R.id.floder_name);
        findViewById(R.id.bottom_tab_bar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //消费触摸事件，防止触摸底部tab栏也会选中图片
                return true;
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化选项参数
     */
    private void initIntentParams() {
        mIsShowCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        mSelectMode = getIntent().getIntExtra(EXTRA_SELECT_MODE, MODE_SINGLE);
        mMaxNum = getIntent().getIntExtra(EXTRA_MAX_MUN, DEFAULT_NUM);
        mTotalNum = getIntent().getIntExtra(TOTAL_MAX_MUN, 0);
        isTakePhoto = getIntent().getBooleanExtra(IS_TAKE_PHOTO, false);
        //判断是拍照还是选择图片
        if (!isTakePhoto) {
            if (mSelectMode == MODE_MULTI) {
                //如果是多选模式，需要将确定按钮初始化以及绑定事件
                mCommitBtn = (Button) findViewById(R.id.commit);
                mCommitBtn.setVisibility(View.VISIBLE);
                mCommitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectList.addAll(mPhotoAdapter.getmSelectedPhotos());
                        returnData();
                    }
                });
            }
        }
    }

    /**
     * Android6.0检测权限
     */
    private void checkPerm() {
        /**1.在AndroidManifest文件中添加需要的权限。
         *
         * 2.检查权限
         *这里涉及到一个API，ContextCompat.checkSelfPermission，
         * 主要用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED
         * 或者PackageManager.PERMISSION_GRANTED。当返回DENIED就需要进行申请授权了。
         * */
        if (ContextCompat.checkSelfPermission(PhotoPickerActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //权限没有被授予

            /**3.申请授权
             * @param
             *  @param activity The target activity.（Activity|Fragment、）
             * @param permissions The requested permissions.（权限字符串数组）
             * @param requestCode Application specific request code to match with a result（int型申请码）
             *    reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
             *int, String[], int[])}.
             * */
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    1001);
        } else {
            //权限被授予 直接操作
//            choosePhoto();
            getPhotosTask.execute();
        }
    }

    /***
     * 4.处理权限申请回调
     */

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == 1001) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
//                //权限被授予
//                Toast.makeText(PhotoPickerActivity.this, "Permission success", Toast.LENGTH_SHORT).show();
//                getPhotosTask.execute();
//            } else {
//                // Permission Denied
//                Toast.makeText(PhotoPickerActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }

        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
    private void getPhotosSuccess() {
        mProgressDialog.dismiss();

        mPhotoNumTV.setText(OtherUtils.formatResourceString(getApplicationContext(),
                R.string.photos_num, mPhotoLists.size()));

        mPhotoAdapter = new PhotoAdapter(getApplicationContext(), mPhotoLists);
        mPhotoAdapter.setIsShowCamera(mIsShowCamera);
        mPhotoAdapter.setSelectMode(mSelectMode);
        mPhotoAdapter.setMaxNum(mMaxNum);
        mPhotoAdapter.setCurentNum(mTotalNum); // 设置当前已选择的图片数量
        mPhotoAdapter.setPhotoClickCallBack(this);
        mGridView.setAdapter(mPhotoAdapter);
        Set<String> keys = mFolderMap.keySet();
        final List<PhotoFolder> folders = new ArrayList<>();
        for (String key : keys) {
            if (ALL_PHOTO.equals(key)) {
                PhotoFolder folder = mFolderMap.get(key);
                folder.setIsSelected(true);
                folders.add(0, folder);
            } else {
                folders.add(mFolderMap.get(key));
            }
        }
        mPhotoNameTV.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                toggleFolderList(folders);
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPhotoAdapter.isShowCamera() && position == 0) {
//                    showCamera();
                    autoObtainCameraPermission();
                    return;
                }
                selectPhoto(mPhotoAdapter.getItem(position));
            }
        });
    }

    /**
     * 点击选择某张照片
     *
     * @param photo
     */
    private void selectPhoto(Photo photo) {
        if (photo == null) {
            return;
        }
        String path = photo.getPath();
        if (mSelectMode == MODE_SINGLE) {
            mSelectList.add(path);
            returnData();
        }
    }

    @Override
    public void onPhotoClick() {
        List<String> list = mPhotoAdapter.getmSelectedPhotos();
        if (list != null && list.size() > 0) {
            mCommitBtn.setEnabled(true);
            mCommitBtn.setText(OtherUtils.formatResourceString(getApplicationContext(),
                    R.string.commit_num, list.size(), mMaxNum));
        } else {
            mCommitBtn.setEnabled(false);
            mCommitBtn.setText(R.string.commit);
        }
    }

    /**
     * 返回选择图片的路径
     */
    private void returnData() {
        // 返回已选择的图片数据
//        Intent data = new Intent();
//        data.putStringArrayListExtra(KEY_RESULT, mSelectList);
//        setResult(RESULT_OK, data);
        ArrayList<PhotoBean> photoBeans = new ArrayList<>();
        ArrayList<String> imageDescribeList = new ArrayList<>();
        ArrayList<String> imgNamelist = new ArrayList<>();
        String picName;
        for (String uri : mSelectList) {
            PhotoBean photoBean = new PhotoBean();
            photoBean.setMeasAssetBarCode("");
            photoBean.setCreateTime(TimeUtils.getTime());
            photoBean.setImageSrc(uri);
            photoBean.setPicName(uri.substring(uri.lastIndexOf("/") + 1));
            photoBeans.add(photoBean);
        }
        //发送消息让dialog刷新界面
        EventBus.getDefault().post(new AttachmentUpdate(ConstantUtil.REFRESH_PIC_RES_LIST_FROM_CAMERA, photoBeans));
        finish();
    }

    /**
     * 显示或者隐藏文件夹列表
     *
     * @param folders
     */
    private void toggleFolderList(final List<PhotoFolder> folders) {
        //初始化文件夹列表
        if (!mIsFolderViewInit) {
            ViewStub folderStub = (ViewStub) findViewById(R.id.floder_stub);
            folderStub.inflate();
            View dimLayout = findViewById(R.id.dim_layout);
            mFolderListView = (ListView) findViewById(R.id.listview_floder);
            final FolderAdapter adapter = new FolderAdapter(this, folders);
            mFolderListView.setAdapter(adapter);
            mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (PhotoFolder folder : folders) {
                        folder.setIsSelected(false);
                    }
                    PhotoFolder folder = folders.get(position);
                    folder.setIsSelected(true);
                    adapter.notifyDataSetChanged();

                    mPhotoLists.clear();
                    mPhotoLists.addAll(folder.getPhotoList());
                    if (ALL_PHOTO.equals(folder.getName())) {
                        mPhotoAdapter.setIsShowCamera(mIsShowCamera);
                    } else {
                        mPhotoAdapter.setIsShowCamera(false);
                    }
                    //这里重新设置adapter而不是直接notifyDataSetChanged，是让GridView返回顶部
                    mGridView.setAdapter(mPhotoAdapter);
                    mPhotoNumTV.setText(OtherUtils.formatResourceString(getApplicationContext(),
                            R.string.photos_num, mPhotoLists.size()));
                    mPhotoNameTV.setText(folder.getName());
                    toggle();
                }
            });
            dimLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mIsFolderViewShow) {
                        toggle();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            initAnimation(dimLayout);
            mIsFolderViewInit = true;
        }
        toggle();
    }

    /**
     * 弹出或者收起文件夹列表
     */
    private void toggle() {
        if (mIsFolderViewShow) {
            outAnimatorSet.start();
            mIsFolderViewShow = false;
        } else {
            inAnimatorSet.start();
            mIsFolderViewShow = true;
        }
    }

    /**
     * 初始化文件夹列表的显示隐藏动画
     */
    AnimatorSet inAnimatorSet = new AnimatorSet();
    AnimatorSet outAnimatorSet = new AnimatorSet();

    private void initAnimation(View dimLayout) {
        ObjectAnimator alphaInAnimator, alphaOutAnimator, transInAnimator, transOutAnimator;
        //获取actionBar的高
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        /**
         * 这里的高度是，屏幕高度减去上、下tab栏，并且上面留有一个tab栏的高度
         * 所以这里减去3个actionBarHeight的高度
         */
        int height = OtherUtils.getHeightInPx(this) - 3 * actionBarHeight;
        alphaInAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0f, 0.7f);
        alphaOutAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0.7f, 0f);
        transInAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", height, 0);
        transOutAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", 0, height);

        LinearInterpolator linearInterpolator = new LinearInterpolator();

        inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
        inAnimatorSet.setDuration(300);
        inAnimatorSet.setInterpolator(linearInterpolator);
        outAnimatorSet.play(transOutAnimator).with(alphaOutAnimator);
        outAnimatorSet.setDuration(300);
        outAnimatorSet.setInterpolator(linearInterpolator);
    }

    /**
     * 选择文件夹
     *
     * @param photoFolder
     */
    public void selectFolder(PhotoFolder photoFolder) {
        mPhotoAdapter.setDatas(photoFolder.getPhotoList());
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 获取照片的异步任务
     */
    @SuppressLint("StaticFieldLeak")
    private AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(PhotoPickerActivity.this, null, "loading...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mFolderMap = PhotoUtils.getPhotos(PhotoPickerActivity.this.getApplicationContext());

            mPhotoLists.addAll(mFolderMap.get(ALL_PHOTO).getPhotoList());
//            mPhotoLists.addAll(mFolderMap.get(strPathOpenImageFIle).getPhotoList());

            //-----------遍历删除不存在的文件 start-----------------
            ArrayList<Photo> delPhoto = new ArrayList<Photo>();
            for (Photo photo : mPhotoLists) {
                String picPath = photo.getPath();
                if (!picPath.isEmpty()) {
                    File file = new File(picPath);
                    if (!file.exists()) {
                        delPhoto.add(photo);
                    }
                }
            }
            mPhotoLists.removeAll(delPhoto);
            //----------遍历删除不存在的文件 end------------------

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };


    /**
     * 选择相机
     */
    private void showCamera() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = OtherUtils.createFile(getApplicationContext());
            imageUri = FileProvider.getUriForFile(PhotoPickerActivity.this, ConstantUtil.FILE_PROVIDER, mTmpFile);//通过FileProvider创建一个content类型的Uri
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showToastForLong(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(PhotoPickerActivity.this, ConstantUtil.FILE_PROVIDER, fileUri);//通过FileProvider创建一个content类型的Uri
//                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                showCamera();
            } else {
                ToastUtils.showToastForLong(this, "设备没有SD卡");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(PhotoPickerActivity.this, ConstantUtil.FILE_PROVIDER, fileUri);//通过FileProvider创建一个content类型的Uri
//                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                        showCamera();
                    } else {
                        ToastUtils.showToastForLong(this, "设备没有SD卡！");
                    }
                } else {
                    ToastUtils.showToastForLong(this, "请允许打开相机！！");
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtils.showToastForLong(this, "请允许操作SDCard！！");
                }
                break;
        }
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
//                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mTmpFile.getAbsolutePath())));
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
                    mSelectList.add(mTmpFile.getAbsolutePath());
                    Log.e("CMCC", "mSelectList===================" + mSelectList.size());
                    FileUtil.notifySystemToScan(OtherUtils.imgStr);
                    returnData();
                }
            } else {
                if (mTmpFile != null && mTmpFile.exists()) {
                    mTmpFile.delete();
                    Log.e("CMCC", "mSelectList===================删除了" + mSelectList.size());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
