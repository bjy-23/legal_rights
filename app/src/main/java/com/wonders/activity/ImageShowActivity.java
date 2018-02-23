package com.wonders.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.legal_rights.R;
import com.wonders.application.AppData;
import com.wonders.constant.Constants;
import com.wonders.http.Retrofit2Helper;
import com.wonders.http.Retrofit2Service;
import com.wonders.bean.PicBean;
import com.wonders.util.ImageLoader;
import com.wonders.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bjy on 2017/1/16.
 */

public class ImageShowActivity extends AppCompatActivity {
    private ImageView imageView;
    private PicBean picBean;
    private boolean isCacheHave;
    private ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        imageView = (ImageView) findViewById(R.id.img);

        picBean = getIntent().getParcelableExtra("picBean");
        imageLoader = ImageLoader.build(ImageShowActivity.this);

        isCacheHave = imageLoader.bindBitmap(imageView,picBean.getPicName(),
                AppData.getInstance().getWidthPixels()
                ,AppData.getInstance().getHeightPixels());
        if (!isCacheHave){
            if (picBean.getType() == 1) {
                getPicFromFile();
            }else {
                getPicFromHttp();
            }
        }
    }

    public void getPicFromFile(){
        imageLoader.setBitmapToDiskCache(new File(picBean.getPicPath()),picBean.getPicName());
        imageLoader.bindBitmap(imageView,picBean.getPicName(),
                AppData.getInstance().getWidthPixels(),
                AppData.getInstance().getHeightPixels());
    }

    public void getPicFromHttp(){
        HashMap<String,String> params = new HashMap<>();
        params.put("planId",picBean.getPlanId());
        params.put("itemCode",picBean.getItemCode());
        params.put("picNum",picBean.getPicNum()+"");

        String url = "";
        if ("".equals(Constants.TYPE)){
            url = Retrofit2Service.GET_BIG_PICTURE;
        }else {
            url = Retrofit2Service.LT_GET_BIG_PICTURE;
        }
        Call<ResponseBody> call = Retrofit2Helper.getInstance().getBigPicture(url,params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LoadingDialog.dismiss();
                String result = "";
                try {
                    result = response.body().string();
                    Log.e("result",result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String picSource = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    picSource = jsonObject.getString("object");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageLoader.setBitmapToDiskCache(picSource,picBean.getPicName());
                imageLoader.bindBitmap(imageView,picBean.getPicName(),
                        AppData.getInstance().getWidthPixels(),
                        AppData.getInstance().getHeightPixels());
                //bitmap释放
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
