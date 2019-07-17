package io.agaghd.agaghdnews;

import android.util.Log;
import io.agaghd.agaghdnews.network.NewsApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

public class SSS {
    void test(){
        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .build();
        NewsApi newsApi = retrofit.create(NewsApi.class);
        newsApi.getChannel().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i("wtf", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
