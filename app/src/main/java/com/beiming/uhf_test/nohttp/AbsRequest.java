package com.beiming.uhf_test.nohttp;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.TypeReference;
import com.beiming.uhf_test.App;
import com.beiming.uhf_test.utils.FastJson;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.tools.MultiValueMap;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * api 请求基类, 封装公共逻辑
 */

public abstract class AbsRequest<T> implements OnResponseListener<String> {
    protected static final String BSLASH = "/"; //反斜杠
    protected static final String ANDROID_CLIENT_TYPE = "1";    //0：微信端	1：安卓   2：IOS	-1：其他

    protected Request mRequest;
    protected ApiListener mListener;
    protected Context mContext;
    private String postBodyJson;        //请求参数body

    public AbsRequest(ApiListener listener) {
        mListener = listener;
        mContext = App.getInstance();
    }

    public Request getRequest() {
        mRequest = NoHttp.createStringRequest(Api.BASE_URL + getApi(), getMethod());
        addHttps(mRequest);
        addGlobalHeader(mRequest);
        addGlobalParams(mRequest);
        addBody();
        specifyRequest(mRequest);
        return mRequest;
    }

    private void addHttps(Request mRequest) {
        SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
        if (sslContext != null) {
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            mRequest.setSSLSocketFactory(socketFactory);
            mRequest.setHostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER);
        }
    }

    /**
     * 获取url的后缀参数
     *
     * @return
     */
    private String getUrlParams() {
        StringBuffer sb = new StringBuffer();
//        MultiValueMap<String, Object> map = mRequest.getParamKeyValues();
//
//        Set<String> keys = map.keySet();
//        if (keys != null) {
//            for (String key : keys) {
//                sb.append("&" + key + "=" + map.getValue(key));
//            }
//        }

        return sb.toString();
    }

    /**
     * 添加 body
     */
    protected void addBody() {
        postBodyJson = postParamWrap(mRequest);
        mRequest.setDefineRequestBodyForJson(postBodyJson);
        LogPrintUtil.httpLog("参数：" + postBodyJson);
    }

    @Override
    public void onStart(int what) {
        LogPrintUtil.httpLog("发起请求：" + mRequest.url() + getUrlParams());
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    public void onFinish(int what) {
        if (mListener != null) {
            mListener.onFinish();
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        String exceptionMsg = "";
        if (response.getException() != null) {
            exceptionMsg = response.getException().getMessage();
        }
        LogPrintUtil.httpLog("接口请求失败：接口：" + response.request().url() + "\n; 异常：" + exceptionMsg);

        //接口请求失败：未请求通
        if (mListener != null) {
            mListener.onError(response.getException());
        }
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        LogPrintUtil.httpLog("接口返回：接口：" + response.request().url() + "\n；返回包: " + response.get());
        //公共解析及处理
        HttpResult<T> httpResult = null;
        String resJson = response.get();
//        String resJson = "{\n" +
//                "    \"data\": {\n" +
//                "        \"mobileNo\": \"13721034537\",\n" +
//                "        \"smsState\": \"0\",\n" +
//                "        \"token\": \"6egJfPNEE9TaQ5TUia0tUHx3x/CE9ayUbss7c2jicnpfOev7vruyRhTE5WV7wPfhZ/yuiIBmSHtD4zuPC4Ry1g==\"\n" +
//                "    },\n" +
//                "    \"reCode\": \"0\",\n" +
//                "    \"reInfo\": \"操作成功\"\n" +
//                "}";
        try {
            httpResult = FastJson.parseTypeObject(resJson, new TypeReference<HttpResult<T>>(getDataTClass()) {
            });
            LogPrintUtil.httpLog("AbsRequet 解析完成");
        } catch (Throwable e) {
        }
        if (httpResult != null) { //json解析正常
            responseParse(httpResult, response);
        } else { //json解析异常
            if (mListener != null) {
                mListener.onError(new Exception(""));
            }
        }
    }

    /**
     * 返回包全局公共处理入口
     */
    private void responseParse(HttpResult<T> httpResult, Response<String> response) {
        if (NetConsts.SUCCESS.equals(httpResult.getReCode())) { //业务逻辑成功
            onResultSuccess(httpResult, response);
        } else { //业务逻辑非SUCCESS
            if (NetConsts.FAIL_UPGRADE_APP.equals(httpResult.getReCode())) { //升级
            } else if (NetConsts.FAIL_REFRESH_TOKEN.equals(httpResult.getReCode())) {//刷新Token
                return;
            } else if (NetConsts.FAIL_NEED_EXIT.equals(httpResult.getReCode())) {//踢人
            }
            //强制升级时，避免Toast提示
            if (!NetConsts.FAIL_NEED_EXIT.equals(httpResult.getReCode()) && !NetConsts.FAIL_UPGRADE_APP.equals(httpResult.getReCode())) {
                onResultFail(httpResult, response);
            }
        }
    }

    /**
     * 默认是post, 子类可以覆盖重写
     *
     * @return
     */
    protected RequestMethod getMethod() {
        return RequestMethod.POST;
    }


    /**
     * 对request根据各自业务进行处理，
     * 如：动态拼接url、增加post参数等、特殊配置等，设置tag等
     *
     * @return
     */
    private String postParamWrap(Request request) {
        JSONObject object = new JSONObject();
        /*try {
            JSONObject ext = new JSONObject();
            //app版本
            ext.put("version", App.getInstance().getAppVersion());
            //终端类型：0：PC，1：Android，2：iPhone
            ext.put("terminalType", "1");
            //app设备型号
            ext.put("userAgent", NetWorkUtil.getDeviceInfo());
            ext.put("deviceVersion", Build.VERSION.RELEASE);
            ext.put("deviceBrand", Build.BRAND);
            ext.put("deviceModel", Build.MODEL);
            ext.put("deviceUdid", Util.getIMSI(App.getInstance()));
            ext.put("n_state", NetWorkUtil.getNetInfo());
            ext.put("netState", NetWorkUtil.getNetInfo());

            object.put("ext", ext);
            object.put("ts", String.valueOf(System.currentTimeMillis()));
            setSendParams(object);
            //统一签名
            String signStr = object.optString("aid") + object.optString("sid") +
                    object.optString("ts") + object.optString("token") + object.optString("data");
            object.put("sign", RSAToolkit.encryptToSHA(signStr));
            if (isEncryPt) {
                object.put("data", SignUtil.encryptToRSA(object.optString("data"), BuildConfig.IS_BUILD_ONLINE,
                        BuildConfig.IS_PREBUILD));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return object.toString();
    }

    /**
     * 添加header
     */
    protected void addGlobalHeader(Request request) {
        request.addHeader("android_version", App.getInstance().getAppVersion());
    }

    /**
     * 请求参数添加，此处处理公共的url后缀参数
     */
    protected void addGlobalParams(Request request) {
        String times = String.valueOf(System.currentTimeMillis());
        request.add("times", times);
        request.add("app_ver", App.getInstance().getAppVersion());
        addUrlExtraSuffix(request);
    }

    /**
     * http url 签名参数设置，如果有额外的可以设置，否则可以不覆写
     *
     * @param map
     */
    protected void addUrlExtraSignMap(TreeMap<String, String> map) {
    }

    /**
     * http url 添加后续，接口根据需要增加
     *
     * @param request
     */
    protected void addUrlExtraSuffix(Request request) {
    }

    /**
     * 对request进行自己特有的处理，子类可覆写
     */
    protected void specifyRequest(Request request) {
    }


    protected void postSuccess(HttpResult<T> httpReuslt) {
        if (mListener != null) {
            mListener.onSuccess(httpReuslt);
        }
    }

    protected void postSuccessResponse(Response<String> response) {
        if (mListener != null) {
            mListener.onSuccessResponse(response);
        }
    }

    protected void postFailed(HttpResult responseBean) {
        if (mListener != null) {
            mListener.onFailed(responseBean);
        }
    }

    /**
     * 获取接口名，如需在url拼接restful参数，则拼接好
     *
     * @return
     */
    protected abstract String getApi();

    /**
     * 业务数据成功处理
     *
     * @param response
     */
    protected abstract void onResultSuccess(HttpResult<T> httpResult, Response<String> response);

    /**
     * 获取返回包中data数据类型的classs
     *
     * @return
     */
    protected abstract Class<T> getDataTClass();

    /**
     * 业务数据失败处理
     *
     * @param response
     */
    protected void onResultFail(HttpResult httpResult, Response<String> response) {
        postFailed(httpResult);
    }


    /**
     * 设置post的body参数
     *
     * @param json
     * @throws JSONException
     */
    protected abstract void setSendParams(JSONObject json) throws JSONException;


    public interface ApiListener<T> {
        void onStart();

        void onFinish();

        /**
         * 业务逻辑正向结果
         *
         * @param httpResult
         */
        void onSuccess(HttpResult<T> httpResult);

        /**
         * 业务逻辑正向结果，可以和上面的都回调，这个主要用于类透传的方式，存数据
         */
        void onSuccessResponse(Response<String> response);

        /**
         * 业务逻辑反向结果
         *
         * @param httpResult
         */
        void onFailed(HttpResult httpResult);

        /**
         * 接口请求失败：未请求通
         *
         * @param exception
         */
        void onError(Exception exception);
    }

}
