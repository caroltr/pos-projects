package br.com.cten.dm114projetofinal.infraestructure;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofitSalesProvider;
    private static Retrofit retrofitMessageProvider;

    private static final String BASE_URL = "https://sales-provider.appspot.com";
    private static final String BASE_URL_MESSAGE_PROVIDER = "https://message-provider.appspot.com";

    public static Retrofit getRetrofitInstance() {

        if (retrofitSalesProvider == null) {
            retrofitSalesProvider = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitSalesProvider;
    }

    public static Retrofit getRetrofitMessageInstance() {

        if (retrofitMessageProvider == null) {
            retrofitMessageProvider = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_MESSAGE_PROVIDER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitMessageProvider;
    }
}
