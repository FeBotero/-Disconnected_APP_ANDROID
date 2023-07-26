package com.example.disconected;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static void postAPI(String email){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://motoacademyserver.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<JsonResponse> call = apiService.enviarEmailParaAPI(new EmailRequestBody(email));

        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    JsonResponse resposta = response.body();
                    Log.i("respostaJsonSucesso",resposta.getPassword());
                    Log.i("resposta Status", String.valueOf(resposta.isActive()));


                } else {
                    Log.i("respostaJson","erro");
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.i("respostaJson",t.toString());
            }
        });
    }


}