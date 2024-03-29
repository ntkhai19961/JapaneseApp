package com.example.win7.apphoctiengnhat.Translate.controller.network.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by NG on 31.03.17.
 */
public class TranslateResponse {

    @SerializedName("code")
    int code;

    @SerializedName("lang")
    String lang;

    @SerializedName("text")
    List<String> text;

    public int getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public List<String> getText() {
        return text;
    }

    public String getResponseText() { return getText().get(0);}

    @Override
    public String toString() {
        return "TranslateResponse{" +
                "code=" + code +
                ", lang='" + lang + '\'' +
                ", text='" + text.get(0) + '\'' +
                '}';
    }
}
