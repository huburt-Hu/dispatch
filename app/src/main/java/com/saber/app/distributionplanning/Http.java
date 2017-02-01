package com.saber.app.distributionplanning;

import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-24 10:20
 */
public class Http {

    private OkHttpClient client;

    private Http() {
        client = new OkHttpClient();
    }

    public static Http getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final Http sInstance = new Http();
    }

    public TestBean getKuaiDi(String com, String num) {
        Request request = new Request.Builder()
                .url("http://apis.baidu.com/showapi_open_bus/express/express_search?" + "com=" + com + "&nu=" + num)
                .header("apikey", "5bba9f3e9999b5122f5d1b8fac9dc013")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            TestBean bean = JsonUtil.parseObject(data, TestBean.class);
            Log.e("tag", bean.toString());
            return bean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PlaceSuggestionBean getPlaceSuggestion(String q) {
        try {
            String url = "http://api.map.baidu.com/place/v2/suggestion?query=" + URLEncoder.encode(q, "UTF-8") +
                    "&region=" + URLEncoder.encode("上海市", "UTF-8") +
                    "&output=json" +
                    "&ak=Lca8qkkLxB8Tm0HTulZpvETKQysHtmVm";
            Log.i("tag", "url:" + url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            String data = response.body().string();
            PlaceSuggestionBean bean = JsonUtil.parseObject(data, PlaceSuggestionBean.class);
            Log.e("tag", bean.toString());
            return bean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
