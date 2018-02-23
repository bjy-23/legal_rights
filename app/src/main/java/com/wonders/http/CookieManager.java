package com.wonders.http;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by bjy on 2016/11/8.
 */
public class CookieManager implements CookieJar {
    private Context context;
    private static PersistentCookieStore cookieStore;
    private static Cookie cookie;

    public CookieManager(Context context) {
        this.context = context;
        if (cookieStore==null){
            cookieStore = new PersistentCookieStore(context);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
                cookie = item;
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies =cookieStore.get(url);
        return cookies;
    }

    public static void removeCookies(){
        cookieStore.removeAll();
    }

    public static Cookie getCookie(){
        return cookie;
    }
}
