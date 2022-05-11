package com.beiming.uhf_test.utils.GreenDaoUtil;

import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Created by htj on 2017/11/10.
 */

public class PhotoBeanConverter implements PropertyConverter<List<PhotoBean>, String> {
    @Override
    public List<PhotoBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        // 先得获得这个，然后再typeToken.getType()，否则会异常
        TypeToken<List<PhotoBean>> typeToken = new TypeToken<List<PhotoBean>>() {
        };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<PhotoBean> arrays) {
        if (arrays == null) {
            return null;
        } else {
            String sb = new Gson().toJson(arrays);
            return sb;
        }
    }
}
