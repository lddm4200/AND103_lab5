package com.example.and103_lab5.services;

import com.example.and103_lab5.model.CongTy;
import com.example.and103_lab5.model.Response;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {
    public static String BASE_URL = "http://192.168.37.67:3000/";

    ApiServices apiService  = new Retrofit.Builder()
            .baseUrl(ApiServices.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.class);
    @GET("get-list-distributor")
    Call<Response<ArrayList<CongTy>>> getData();
    @POST("add-distributor")
    Call<Response<CongTy>> addStudent(@Body CongTy distributor);

    @DELETE("delete-distributor-by-id/{id}")
    Call<Response<CongTy>> deleteStudent(@Path("id") String id);



    @PUT("update-distributor-by-id/{id}")
    Call<Response<CongTy>> updateStudent(@Path("id") String id,@Body CongTy distributor);

    @GET("search-distributor")
    Call<Response<ArrayList<CongTy>>> searchCongTy(@Query("key") String key);





}


