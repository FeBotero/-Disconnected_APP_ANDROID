package com.example.disconected;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {
    @POST("client/check")
    Call<JsonResponse> enviarEmailParaAPI(@Body EmailRequestBody requestBody);
}
