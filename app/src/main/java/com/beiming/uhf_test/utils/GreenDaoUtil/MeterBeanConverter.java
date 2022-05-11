package com.beiming.uhf_test.utils.GreenDaoUtil;


import com.beiming.uhf_test.bean.MeterBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Created by htj on 2021/5/20.
 */

public class MeterBeanConverter implements PropertyConverter<List<MeterBean>, String> {
    @Override
    public List<MeterBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        // 先得获得这个，然后再typeToken.getType()，否则会异常
        TypeToken<List<MeterBean>> typeToken = new TypeToken<List<MeterBean>>() {
        };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<MeterBean> arrays) {
        if (arrays == null) {
            return null;
        } else {
            String sb = new Gson().toJson(arrays);
            return sb;
        }
    }
}
