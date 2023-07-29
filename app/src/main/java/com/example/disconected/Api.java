package com.example.disconected;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    static Handler handler = new Handler();

    public interface ApiResultCallback {
        void onApiResult(String password,boolean isActive);
    }

    static HandleProps handleProps = new HandleProps();
    public static void postAPI(Context context, String email, ApiResultCallback callback){
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
                    Log.i("respostaJsonSucesso", resposta.getPassword());
                    Log.i("resposta Status", String.valueOf(resposta.isActive()));


                   // Chamar o método onApiResponse do ApiCallback com o valor da senha retornado da API
                    if (callback != null) {
                        callback.onApiResult(resposta.getPassword(), resposta.isActive());
                    }


                    if(resposta.isActive()==true){
                        Intent intent = new Intent("wifi.action.shutdown_wifi");
                        context.sendBroadcast(intent);

                        handler.postDelayed(() -> {
                            handleProps.write("persist.control.wifi.service", Boolean.toString(true));
                            Log.d("Wifi", "Desabilitando Função Wifi");
                        }, 5000);
                    }else{
                        if(resposta.isActive()==false){
                            handleProps.write("persist.control.wifi.service", Boolean.toString(false));
                        }

                    }

                } else {
                    Log.i("respostaJson","Errona conexão!");

                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.i("respostaJson",t.toString());
            }
        });
    }
}