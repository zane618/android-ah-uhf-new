package com.beiming.uhf_test.helper.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.beiming.uhf_test.App;
import com.beiming.uhf_test.bean.LocationBean;
import com.beiming.uhf_test.utils.LogPrintUtil;

/**
 * Created by zhangshi on 2021/12/3 15:05.
 */
public class LocationHelper {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LogPrintUtil.zhangshi("onLocationChanged");
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    LocationBean bean = new LocationBean();
                    bean.setLatitude(aMapLocation.getLatitude() + "");
                    bean.setLongitude(aMapLocation.getLongitude() + "");
                    bean.setAccuracy(aMapLocation.getAccuracy() + "");
                    bean.setAddress(aMapLocation.getAddress());
                    bean.setCountry(aMapLocation.getCountry());
                    bean.setProvince(aMapLocation.getProvince());
                    bean.setCity(aMapLocation.getCity());
                    bean.setDistrict(aMapLocation.getDistrict());
                    bean.setStreet(aMapLocation.getStreet());
                    bean.setStreetNum(aMapLocation.getStreetNum());
                    bean.setAoiName(aMapLocation.getAoiName());
                    bean.setBuildingId(aMapLocation.getBuildingId());
                    bean.setFloor(aMapLocation.getFloor());
                    bean.setGpsAccuracyStatus(aMapLocation.getGpsAccuracyStatus() + "");
                    bean.setTime(aMapLocation.getTime() + "");
                    bean.setCityCode(aMapLocation.getCityCode());
                    bean.setAdCode(aMapLocation.getAdCode());

                    if (mListener != null) {
                        mListener.onLocationChanged(bean);
                    }
                    /*//获取当前定位结果来源，如网络定位结果，详见定位类型表
					Log.i("定位类型", amapLocation.getLocationType() + "");
					Log.i("获取纬度", amapLocation.getLatitude() + "");
					Log.i("获取经度", amapLocation.getLongitude() + "");
					Log.i("获取精度信息", amapLocation.getAccuracy() + "");

					//如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
					Log.i("地址", amapLocation.getAddress());
					Log.i("国家信息", amapLocation.getCountry());
					Log.i("省信息", amapLocation.getProvince());
					Log.i("城市信息", amapLocation.getCity());
					Log.i("城区信息", amapLocation.getDistrict());
					Log.i("街道信息", amapLocation.getStreet());
					Log.i("街道门牌号信息", amapLocation.getStreetNum());
					Log.i("城市编码", amapLocation.getCityCode());
					Log.i("地区编码", amapLocation.getAdCode());
					Log.i("获取当前定位点的AOI信息", amapLocation.getAoiName());
					Log.i("获取当前室内定位的建筑物Id", amapLocation.getBuildingId());
					Log.i("获取当前室内定位的楼层", amapLocation.getFloor());
					Log.i("获取GPS的当前状态", amapLocation.getGpsAccuracyStatus() + "");

					//获取定位时间
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(amapLocation.getTime());

					Log.i("获取定位时间", df.format(date));*/
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogPrintUtil.zhangshi("AmapError" +  ": location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    if (mListener != null) {
                        LocationBean bean = new LocationBean();
                        bean.setTime(String.valueOf(System.currentTimeMillis()));
                        bean.setErrorInfo(": location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        mListener.onLocationChanged(bean);
                    }
                }
            }
            //定位成功后，销毁
            deActivate();
        }
    };
    private ILocationListener mListener;

    public LocationHelper() {
        init();
    }

    private void init() {
        try {
            mLocationClient = new AMapLocationClient(App.getInstance().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            mLocationClient.setLocationListener(mLocationListener);
        }
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        /*option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }*/
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setGpsFirst(false);
        mLocationOption.setOnceLocation(true);     //只定位一次
        mLocationOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        mLocationOption.setHttpTimeOut(5 * 1000);

        if (mLocationClient != null) {
            mLocationClient.setLocationOption(mLocationOption);
        }
    }

    public void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }
    /**
     * 销毁
     */
    private void deActivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    public interface ILocationListener {
        void onLocationChanged(LocationBean locationBean);
    }

    public LocationHelper setListener(ILocationListener listener) {
        mListener = listener;
        return this;
    }

}
