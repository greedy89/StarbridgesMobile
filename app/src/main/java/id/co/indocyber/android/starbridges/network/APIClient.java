package id.co.indocyber.android.starbridges.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import id.co.indocyber.android.starbridges.R;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 5/8/2018.
 */

public class APIClient {
    private static Retrofit retrofit = null;
    private static int linkApi = R.string.link_test_public;

    public static Retrofit getClient(Context context) {

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Content-Type", "application/x-www-form-urlencoded").build();
                return chain.proceed(newRequest);
            }
        };


        //RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/plain; charset=utf-8"),"grant_type=password&username=handri.fajar&password=Pass1234.&client_id=ngAuthApp");

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        //Log.d("DataLogin", client.toString());

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(String.class, new StringConverter());
        gb.serializeNulls();
        Gson gson = gb.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(linkApi))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;

    }

    public static Retrofit getClientWithToken(final String token, Context context) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(linkApi))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static Retrofit getClientWithTokenLongerLoading(final String token, Context context) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(newRequest);
            }
        };

        int timeOut=30;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(linkApi))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}





