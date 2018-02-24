package com.wonders.http;

import com.wonders.bean.CheckRecordBean;
import com.wonders.bean.DbsxBean;
import com.wonders.bean.LoginBean;
import com.wonders.bean.Result;
import com.wonders.bean.UpdateBean;
import com.wonders.bean.UserInfoBean;
import com.wonders.bean.JgBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by bjy on 2016/11/7.
 */
public interface Retrofit2Service {
    public static final String SAVE_PLAN_CHECK_CONTENT = "save_planCheckContent";
    public static final String LT_SAVE_PLAN_CHECK_CONTENT = "lt_save_planCheckContent";
    public static final String QUERY_TODO = "query_todo.do";
    public static final String LT_QUERY_TODO = "lt_query_todo.do";
    public static final String GET_ETP_CHECK_INFO= "get_etpCheckInfo.do";
    public static final String LT_GET_ETP_CHECK_INFO= "lt_get_etpCheckInfo.do";
    public static final String GET_SUPERVISE_RECORD= "get_superviseRecord.do";
    public static final String LT_GET_SUPERVISE_RECORD= "lt_get_superviseRecord.do";
    public static final String GET_PLAN_CHECK_CONTENT= "get_planCheckContent";
    public static final String LT_GET_PLAN_CHECK_CONTENT= "lt_get_planCheckContent";
    public static final String SAVE_PLAN_CHECK_CONTENT_PICTURE_TEMP= "save_planCheckContentPictureTemp.do";
    public static final String LT_SAVE_PLAN_CHECK_CONTENT_PICTURE_TEMP= "lt_save_planCheckContentPictureTemp.do";
    public static final String DELETE_ADD_SOP_INFO= "delete_addSOPInfo.do";
    public static final String LT_DELETE_ADD_SOP_INFO= "lt_delete_addSOPInfo.do";
    public static final String SAVE_GATHER_PLAN_CHECK_CONTENT= "save_gatherPlanCheckContent.do";
    public static final String LT_SAVE_GATHER_PLAN_CHECK_CONTENT= "lt_save_gatherPlanCheckContent.do";
    public static final String GATHER_PLAN_CHECK_CONTENT= "gather_planCheckContent.do";
    public static final String LT_GATHER_PLAN_CHECK_CONTENT= "lt_gather_planCheckContent.do";
    public static final String GET_BIG_PICTURE= "getBigPicture.do";
    public static final String LT_GET_BIG_PICTURE= "lt_getBigPicture.do";
    public static final String GET_PLAN_CHECK_CONTENT_DETAIL= "get_planCheckContentDetail.do";
    public static final String LT_GET_PLAN_CHECK_CONTENT_DETAIL= "lt_get_planCheckContentDetail.do";
    public static final String QUERY_SUPERVISE_RECORD= "query_superviseRecord.do";
    public static final String LT_QUERY_SUPERVISE_RECORD= "lt_query_superviseRecord.do";
    public static final String BINDING_GROUP= "binding_group.do";
    public static final String LT_BINDING_GROUP= "lt_binding_group.do";
    public static final String GET_CHECK_ITEM= "getCheckItem.do";
    public static final String LT_GET_CHECK_ITEM= "lt_getCheckItem.do";
    public static final String DISTRIBUTION_GROUP= "distribution_group.do";
    public static final String LT_DISTRIBUTION_GROUP= "lt_distribution_group.do";
    public static final String QUERY_PRINT_DOCUMENT= "query_printDocument.do";
    public static final String LT_QUERY_PRINT_DOCUMENT= "lt_query_printDocument.do";
    public static final String QUERY_ETPS_INFO= "query_etpsInfo.do";
    public static final String LT_QUERY_ETPS_INFO= "lt_query_etpsInfo.do";
    public static final String SAVE_SOP= "saveSOP.do";
    public static final String LT_SAVE_SOP= "lt_saveSOP.do";

    @GET("get_appinfo")
    Call<Result<UpdateBean>> getAppInfo();

    @POST("get_errorLog.do")
    @FormUrlEncoded
    Call<ResponseBody> sendError(@Field("errorLog") String errorLog);

    @POST("login.do")
    @FormUrlEncoded
    Observable<Result<LoginBean>> login(@FieldMap Map<String,String> map);

    @GET("getAcount/{date}")
    Call<Result<ArrayList<UserInfoBean>>> getAccount(@Path("date") String date);

    @POST("getCheckNumber")
    Call<Result<String>> getCheckNumber();

    @POST
    Call<Result<DbsxBean>> queryToDo(@Url String url);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> getEtpCheckInfo(@Url String url,@Field("etpId") String etpId);

    @POST
    @FormUrlEncoded
    Call<Result<List<CheckRecordBean>>> getSuperviseRecord(@Url String url, @Field("etpId") String etpsId);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> getPlanCheckContent(@Url String url,@Field("planId") String planId);

    @POST
    Call<Result> savePlanCheckContent(@Url String url, @Body RequestBody requestBody);

    @POST
    Call<ResponseBody> saveSop(@Url String url, @Body RequestBody requestBody);

    @POST
    @Multipart
    Call<Result> savePlanCheckContentPictureTemp(@Url String url, @Part MultipartBody.Part file);

    @POST("get_unCheckItem.do")
    @FormUrlEncoded
    Call<ResponseBody> getUncheckItem(@Field("etpsId") String etspId);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> deleteAddSopInfo(@Url String url,@Field("planId") String planId,@Field("itemCode") String itemCode);

    @POST
    @FormUrlEncoded
    Call<Result> saveGatherPlanCheckContent(@Url String url,@FieldMap Map<String,String> map);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> gatherPlanCheckContent(@Url String url,@FieldMap Map<String,String> map);

    @POST("get_informPage")
    @FormUrlEncoded
    Call<ResponseBody> getInformPage(@FieldMap Map<String,String> map);

    @POST("get_resultRecord")
    @FormUrlEncoded
    Call<ResponseBody> getResultRecord(@FieldMap Map<String,String> map);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> getBigPicture(@Url String url,@FieldMap Map<String,String> map);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> getPlanCheckContentDetail(@Url String url,@Field("planId") String planId);

    @POST
    @FormUrlEncoded
    Call<Result<List<JgBean>>> querySuperviseRecord(@Url String url, @FieldMap Map<String,String> map);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> bindingGroup(@Url String url,@FieldMap Map<String,String> map);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> getCheckItem(@Url String url,@FieldMap Map<String,String> map);

    @POST
    Call<ResponseBody> distributionGroup(@Url String url);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> queryPrintDocument(@Url String url,@FieldMap Map<String,String> map);

    @POST("to_superviseRecord.do")
    Call<ResponseBody> toSuperviseRecord();

    @POST
    @FormUrlEncoded
    Call<ResponseBody> queryEtpsInfo(@Url String url,@FieldMap Map<String,String> map);

    @POST("printThreeBooks.do")
    @FormUrlEncoded
    Call<ResponseBody> printThreeBooks(@Field("planId") String planId,@Field("etpsId") String etpsId);

    @POST("query_dataInfo_offline.do")
    @FormUrlEncoded
    Call<ResponseBody> queryDataInfoOffline(@Field("pageNo") String pageNo);

    @POST("ltGetPDFPrintData.do")
    @FormUrlEncoded
    Call<ResponseBody> ltGetPDFPrintData(@FieldMap HashMap<String,String> params);

    @GET("lt_query_dataInfo_offline_db")
    Call<ResponseBody> ltQueryDataInfoOfflineDb();
}
