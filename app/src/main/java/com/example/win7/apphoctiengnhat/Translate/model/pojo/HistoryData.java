package com.example.win7.apphoctiengnhat.Translate.model.pojo;

import java.util.Date;

/**
 * Created by NG on 11.04.17.
 */

public class HistoryData {

    private int key;
    private LanguagePair languagePair;
    private String originalText;
    private String translateText;
    private long time;
    private boolean favorite;

    public HistoryData(int key, LanguagePair languagePair, String originalText, String translateText, long time, boolean favorite) {
        this.key = key;
        this.languagePair = languagePair;
        this.originalText = originalText;
        this.translateText = translateText;
        this.time = time;
        this.favorite = favorite;
    }

    public HistoryData(int key, LanguagePair languagePair, String originalText, String translateText) {
        this.key = key;
        this.languagePair = languagePair;
        this.originalText = originalText;
        this.translateText = translateText;
        this.time = new Date().getTime();
        this.favorite = false;
    }

    public HistoryData(LanguagePair languagePair, String originalText, String translateText, long time) {
        this.languagePair = languagePair;
        this.originalText = originalText;
        this.translateText = translateText;
        this.time = time;
        this.favorite = false;
    }

    public HistoryData(LanguagePair languagePair, String originalText, String translateText) {
        this.languagePair = languagePair;
        this.originalText = originalText;
        this.translateText = translateText;
        this.time = new Date().getTime();
        this.favorite = false;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public LanguagePair getLanguagePair() {
        return languagePair;
    }

    public void setLanguagePair(LanguagePair languagePair) {
        this.languagePair = languagePair;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslateText() {
        return translateText;
    }

    public void setTranslateText(String translateText) {
        this.translateText = translateText;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "HistoryData{" +
                "key=" + key +
                ", languagePair=" + languagePair +
                ", originalText='" + originalText + '\'' +
                ", translateText='" + translateText + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HistoryData that = (HistoryData) o;

        if (key != that.key) return false;
        if (time != that.time) return false;
        if (favorite != that.favorite) return false;
        if (languagePair != null ? !languagePair.equals(that.languagePair) : that.languagePair != null)
            return false;
        if (originalText != null ? !originalText.equals(that.originalText) : that.originalText != null)
            return false;
        return translateText != null ? translateText.equals(that.translateText) : that.translateText == null;

    }

    @Override
    public int hashCode() {
        int result = key;
        result = 31 * result + (languagePair != null ? languagePair.hashCode() : 0);
        result = 31 * result + (originalText != null ? originalText.hashCode() : 0);
        result = 31 * result + (translateText != null ? translateText.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (favorite ? 1 : 0);
        return result;
    }

    public static class NullableHistoryData extends HistoryData {

        private static NullableHistoryData instance;

        public static NullableHistoryData getInstance() {
            if (instance == null) {
                instance = new NullableHistoryData();
            }
            return instance;
        }

        private NullableHistoryData() {
            super(-1, null, "nullable history", "nullable history");
        }
    }
}
