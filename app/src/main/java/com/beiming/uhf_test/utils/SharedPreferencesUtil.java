package com.beiming.uhf_test.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by dell on 2016/3/30.
 */
public class SharedPreferencesUtil {
    private static final String TAG = SharedPreferencesUtil.class.getSimpleName();
    private static SharedPreferencesUtil instance;
    private Context context;

    public SharedPreferencesUtil(Context context) {
        this.context = context;
    }

    public static void setInstance(SharedPreferencesUtil instances) {
        instance = instances;
    }

    public static SharedPreferencesUtil getInstance() {
        return instance;
    }


    public String getKey(String loginkey) {
        String key;
        try {
            key = EncodeUtils.encryptMode(loginkey);
        } catch (Exception ex) {
            key = "";
        }
        return key;
    }

    public void setStringValue(String in_settingName, String in_val) {
        String encrypkey = "";
        try {
            encrypkey = EncodeUtils.encryptMode(in_val);
        } catch (Exception ex) {
            encrypkey = "";
        }
        SharedPreferences sp = context.getSharedPreferences(in_settingName, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(in_settingName, encrypkey);
        ed.commit();
        ed = null;
        sp = null;
    }

    public String getStringValue(String in_settingName) {
        SharedPreferences sp = context.getSharedPreferences(in_settingName, Context.MODE_PRIVATE);
        String ret = sp.getString(in_settingName, "");
        String decrypkey = "";
        if (ObjectUtils.isStringNotEmpty(ret)) {
            //对密码进行AES解密
            try {
                decrypkey = EncodeUtils.decryptMode(ret);
            } catch (Exception ex) {
                decrypkey = "";
            }
        } else
            decrypkey = "";
        sp = null;
        return decrypkey;
    }

    public void clearStringValue(String in_settingName) {
        SharedPreferences sp = context.getSharedPreferences(in_settingName, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public void setBooleanValue(String in_settingName, boolean in_val) {
        String ret = "";
        //对密码进行AES解密
        try {
            ret = EncodeUtils.encryptMode(String.valueOf(in_val));
        } catch (Exception ex) {
            ret = "";
        }
        SharedPreferences sp = context.getSharedPreferences(in_settingName, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(in_settingName, ret);
        ed.commit();
        ed = null;
        sp = null;
    }

    public boolean getBooleanValue(String in_settingName) {
        SharedPreferences sp = context.getSharedPreferences(in_settingName, Context.MODE_PRIVATE);
        String ret = sp.getString(in_settingName, "");
        //对密码进行AES解密
        String result = "";
        if (ObjectUtils.isStringNotEmpty(ret)) {
            try {
                result = EncodeUtils.decryptMode(String.valueOf(ret));
            } catch (Exception ex) {
                result = ex.getMessage();
            }
        }
        sp = null;
        if (ObjectUtils.equals("true", result))
            return true;
        else
            return false;
    }

    public boolean setObjectToShare(String key, Object object) {
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(context);
        if (object == null) {
            SharedPreferences.Editor editor = share.edit().remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = share.edit();
        // 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }

    public Object getObjectFromShare(String key) {
        SharedPreferences sharePre = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            String wordBase64 = sharePre.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object object = ois.readObject();
            bais.close();
            ois.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
