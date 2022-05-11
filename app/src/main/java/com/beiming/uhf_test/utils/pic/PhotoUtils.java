package com.beiming.uhf_test.utils.pic;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.FileUtils;
import com.beiming.uhf_test.utils.pic.model.Photo;
import com.beiming.uhf_test.utils.pic.model.PhotoFolder;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Description：异步获取本地图片工具类
 * <p>
 * packgeName: 不在列表中展示该包路径下的图片
 * Created by Mjj on 2016/12/2.
 */

public class PhotoUtils {
    private static String mTag;

    public static Map<String, PhotoFolder> getPhotos(Context context) {
        Map<String, PhotoFolder> folderMap = new HashMap<>();

        String allPhotosKey = "所有图片";
        PhotoFolder allFolder = new PhotoFolder();
        allFolder.setName(allPhotosKey);
        allFolder.setDirPath(allPhotosKey);
        allFolder.setPhotoList(new ArrayList<Photo>());
        folderMap.put(allPhotosKey, allFolder);

       /* Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        // 只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(imageUri, null,
                MediaStore.Images.Media.MIME_TYPE + " in(?, ?)",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        int pathIndex = mCursor
                .getColumnIndex(MediaStore.Images.Media.DATA);

        if (mCursor.moveToFirst()) {
            do {
                // 获取图片的路径
                String path = mCursor.getString(pathIndex);
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                String dirPath = parentFile.getAbsolutePath();
//                String photoPath = Environment.getExternalStorageDirectory() +
//                        File.separator + "nari/" + mark + "/image/" + packgeName;
*//*
                //nari目录中的图片不展示
                String nariParentDirPath = new File(dirPath).getParentFile().getParentFile().getParentFile().getAbsolutePath();
                String nariPhotoPath = Environment.getExternalStorageDirectory() +
                        File.separator + "nari";
                //农科院目录中的图片不展示
                String parentDirPath = new File(dirPath).getParentFile().getAbsolutePath();
                String photoPath = Environment.getExternalStorageDirectory() +
                        File.separator + "作物收集系统";
                //品种资源记载本中的图片不展示
                String pzzyParentDirPath = new File(dirPath).getParentFile().getParentFile().getAbsolutePath();
                String pzzyPhotoPath = Environment.getExternalStorageDirectory() +
                        File.separator + "品种资源记载本";
                //留痕管理中的图片不展示
                String lhglParentDirPath = new File(dirPath).getParentFile().getParentFile().getAbsolutePath();
                String lhglPhotoPath = Environment.getExternalStorageDirectory() +
                        File.separator + "留痕管理";
*//*

//                Log.i("CMCC", "dirPath ================ " + dirPath);
//                Log.i("CMCC", "photoPath ============== " + photoPath);
//                Log.i("CMCC", "parentDirPath ============== " + parentDirPath);
*//*                if (nariParentDirPath.equals(nariPhotoPath) || parentDirPath.equals(photoPath)
                        || pzzyParentDirPath.equals(pzzyPhotoPath) || lhglParentDirPath.equals(lhglPhotoPath)) {
                    continue;
                }*//*
                Log.i("CMCC", "photoPath ============== " + path);
                Log.i("CMCC", "dirPath ================ " + dirPath);
                if (folderMap.containsKey(dirPath)) {
                    Photo photo = new Photo(path);
                    PhotoFolder photoFolder = folderMap.get(dirPath);
                    photoFolder.getPhotoList().add(photo);
                    folderMap.get(allPhotosKey).getPhotoList().add(photo);
                    continue;
                } else {
                    // 初始化imageFolder
                    PhotoFolder photoFolder = new PhotoFolder();
                    List<Photo> photoList = new ArrayList<>();
                    Photo photo = new Photo(path);
                    photoList.add(photo);
                    photoFolder.setPhotoList(photoList);
                    photoFolder.setDirPath(dirPath);
                    photoFolder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                    folderMap.put(dirPath, photoFolder);
                    folderMap.get(allPhotosKey).getPhotoList().add(photo);
                }
            } while (mCursor.moveToNext());
        }
        mCursor.close();*/

        //todo 初始化指定的路径下的imageFolder(隐藏图片文件夹中的)
        // 获取图片的路径
        String path;
        //获取本地路径
        File file = new File(ConstantUtil.IMAGE_STR);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] fileList = file.listFiles();
        //儿歌中未添加,实际却已经加密的文件加密
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                path = fileList[i].getAbsolutePath();
                Photo photo = new Photo(path);
                folderMap.get(allPhotosKey).getPhotoList().add(photo);
            }
        }
        return folderMap;
    }

/*    private void savePhotoFile(ArrayList<String> attachmentPicPaths,
                               ArrayList<String> oldAttachmentPicPaths, String tag, Object obj) {
        mTag = tag;
        switch (tag) {
            case "cropDetailsBean":
                mCropDetailsBean = (CropDetailsBean) obj;
                break;
            case "yearsCropDetailsBean":
                mYearsCropDetailsBean = (YearsCropDetailsBean) obj;
                break;
        }
    }*/


    //获取无后缀的文件名
    private static String getOnePhotoStr(String photoName) {
        String s = photoName.substring(0, photoName.indexOf("."));
        return s;
    }

    //复制文件
    public static String copyFile(String oldPath) {
        String photoName = "";
        if (isDesignatedSpot(oldPath)) {//判断是否是指定文件夹中的图片,是则直接拿名字,不是则拷贝后拿文件名
            File file = new File(oldPath);
            photoName = file.getName();
            Log.i("photoName============", photoName);
        } else {
            Log.i("oldPath============", oldPath);
//        String photoName = cropDetailsBean.getEtSampleNumble() + "_" + cropDetailsBean.getEtCropName();
            String strPath = ConstantUtil.IMAGE_STR + "/";
            File packge = new File(strPath);
            if (!packge.exists()) {
                packge.mkdirs();
            }
//        File[] fileList = packge.listFiles();
            String newPath;
            try {
                int bytesum = 0;
                int byteread = 0;
                File oldfile = new File(oldPath);
                photoName = getImageName();
                newPath = strPath + photoName;
//            Log.e("CMCC", "name=============" + oldfile.getName());
                if (oldfile.exists()) { //文件存在时
                    InputStream inStream = new FileInputStream(oldPath); //读入原文件
                    FileOutputStream fs = new FileOutputStream(newPath);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; //字节数 文件大小
                        System.out.println(bytesum);
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                }
                //通知移动端对该文件进行扫描
                FileUtils.notifySystemToScan(newPath);
            } catch (Exception e) {
                System.out.println("复制单个文件操作出错");
                e.printStackTrace();
            }
        }
        return photoName;
    }


    /**
     * 判断是否是指定文件夹中的图片,是则直接拿名字,不是则拷贝后拿文件名
     *
     * @param oldPath 该图片的路径
     * @return
     */
    private static boolean isDesignatedSpot(String oldPath) {
        File file = new File(oldPath);
        String parent = file.getParent();
        Log.i("parent============", parent);
        Log.i("目标文件夹============", ConstantUtil.IMAGE_STR);
        return parent.equals(ConstantUtil.IMAGE_STR);
    }

    //获取图片的文件夹名称
    public static String getImageFilePackgeName() {
//        String type = testTaskBean.getType();
        return "";
    }

    //获取图片的文件名称
    public static String getImageName() {
        return System.currentTimeMillis() + ".jpg";
    }

    /**
     * 删除图片
     *
     * @param imgRes     图片名称
     * @param packgeName 图片所在包名
     * @return
     */
    public static void deleteImg(String imgRes, String packgeName) {
        String strPath = ConstantUtil.IMAGE_STR + packgeName + "/";
        File file = new File(strPath + imgRes);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 将图片地址转化成uri
     *
     * @param imageName
     * @return
     */
    public static Uri getImageStreamFromExternal(String imageName) {
        File picPath = new File(imageName);
        Uri uri = null;
        if (picPath.exists()) {
            uri = Uri.fromFile(picPath);
            LogUtils.i("路劲存在==" + imageName);
        }

        return uri;
    }

}
