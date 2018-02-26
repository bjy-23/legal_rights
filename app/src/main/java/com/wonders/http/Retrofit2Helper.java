package com.wonders.http;

import com.wonders.application.AppData;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bjy on 2016/11/7.
 */
public class Retrofit2Helper {
    private static Retrofit2Service service;
    private static int CONNECT_TIME_OUT = 45;
    private static int READ_TIME_OUT = 45;
    private static int WRITE_TIME_OUT = 45;

    public final static String NOR_URL_1 = "http://180.166.102.48:80/android/";//正式
    public final static String NOR_URL_TEST = "http://180.166.102.48:8081/android/";//正式分支
    public final static String TEST_URL_1 = "http://10.1.8.122:8082/android/";//公司测试
    public final static String TEST_URL_2 = "http://10.2.14.140:8080/fpsi4702/android/";//本地测试
    public final static String BSAE_URL = NOR_URL_1;

    public static Retrofit2Service getInstance(){
        if (service==null){
            synchronized (Retrofit2Service.class){
                if (service == null){
                    //设置拦截器
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .cookieJar(new CookieManager(AppData.getInstance()))
                            .connectTimeout(CONNECT_TIME_OUT,TimeUnit.SECONDS)
                            .readTimeout(READ_TIME_OUT,TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(interceptor)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BSAE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();

                    service = retrofit.create(Retrofit2Service.class);
                }
            }
        }
        return service;
    }
}
