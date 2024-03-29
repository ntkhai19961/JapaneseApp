package com.example.win7.apphoctiengnhat.Translate.controller.network;

import com.example.win7.apphoctiengnhat.Translate.controller.network.data.response.LanguageListResponse;
import com.example.win7.apphoctiengnhat.Translate.controller.network.data.response.TranslateResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by NGusarov on 17/03/17.
 */

public interface YandexTranslateApi {

    public static final String ENDPOINT = "https://translate.yandex.net/";
    //todo refactor this. need more security level
    public static final String API_KEY =
            "trnsl.1.1.20170315T100226Z.fe4426a08d83fdd0.2bbc02ed514d8f9358a92c8673b0ae3a4ffd1ef3";
    public static final String API_STRING = "key=" + API_KEY;

    @POST("api/v1.5/tr.json/getLangs?" + API_STRING)
    Call<LanguageListResponse> loadSupportedLangList(@Query("ui") String languageKey);

    @POST("api/v1.5/tr.json/translate?" + API_STRING)
    Call<TranslateResponse> loadTranslateLang(@Query("text") String text, @Query("lang") String lang);

    @POST("api/v1.5/tr.json/translate?" + API_STRING)
    Call<TranslateResponse> loadTranslate(@Query("text") String text);

}
