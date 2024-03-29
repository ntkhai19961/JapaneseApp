package com.example.win7.apphoctiengnhat.Translate.controller.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.win7.apphoctiengnhat.Translate.model.db.table.HistoryShema;
import com.example.win7.apphoctiengnhat.Translate.model.db.wrappers.BaseCursorWrapper;
import com.example.win7.apphoctiengnhat.Translate.model.db.wrappers.HistoryCursorWrapper;
import com.example.win7.apphoctiengnhat.Translate.model.pojo.HistoryData;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Created by NG on 11.04.17.
 */

public class RepositorySQLiteIml implements Repository {

    private static final String TAG = RepositorySQLiteIml.class.getSimpleName();

    private SQLiteDatabase mDataBase;
    private DBHelper mHelper;
    private AtomicInteger counter = new AtomicInteger();

    public RepositorySQLiteIml(Context context) {
        mHelper = new DBHelper(context);
        mDataBase = mHelper.getWritableDatabase();
    }

    @Override
    public void open() {
        counter.incrementAndGet();
        mDataBase = mHelper.getWritableDatabase();
    }

    @Override
    public void close() {
        if (counter.decrementAndGet() == 0) {
            mDataBase.close();
        }
    }

    private ContentValues getContentValues(HistoryData data) {
        ContentValues content = new ContentValues();

        content.put(HistoryShema.HistoryTable.Cols.TEXT_FROM, data.getOriginalText());
        content.put(HistoryShema.HistoryTable.Cols.TEXT_TO, data.getTranslateText());
        content.put(HistoryShema.HistoryTable.Cols.PAIR, data.getLanguagePair().getLangPairStringValue());
        content.put(HistoryShema.HistoryTable.Cols.FAVORITE, data.isFavorite() ? 1 : 0);
        content.put(HistoryShema.HistoryTable.Cols.TIME, data.getTime());

        return content;
    }

    @Override
    public void addHistory(HistoryData data) {
        Log.d(TAG, "addHistory. data: " + data);

        ContentValues content = getContentValues(data);

        mDataBase.insert(HistoryShema.HistoryTable.NAME, null, content);
    }

    @Override
    public List<HistoryData> getAllHistories() {
        Log.d(TAG, "getAllHistories");

        List<HistoryData> list = new LinkedList<>();
        BaseCursorWrapper<HistoryData> cursor = queryData(HistoryShema.HistoryTable.NAME, null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                list.add(cursor.getData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.d(TAG, "getAllHistories. return list size: " + list.size());

        return list;
    }

    private BaseCursorWrapper queryData(String tableName, String whereClause, String[] whereArgs) {

        Cursor cursor = mDataBase.query(
                tableName, //table name
                null,                                            //columns
                whereClause,                                     //selection
                whereArgs,                                       //selection args[]
                null,                                            //group by
                null,                                            //having
                null                                            //order by
                );

        switch (tableName) {
            case HistoryShema.HistoryTable.NAME: {
                return new HistoryCursorWrapper(cursor);
            }
            default: {
                return BaseCursorWrapper.NullableCursorWrapper.getInstance();
            }
        }
    }

    @Override
    public List<HistoryData> getFavoriteHistories() {
        Log.d(TAG, "getFavoriteHistories");

        List<HistoryData> list = new LinkedList<>();
        BaseCursorWrapper<HistoryData> cursor = queryData(HistoryShema.HistoryTable.NAME, HistoryShema.HistoryTable.Cols.FAVORITE + " = ?",
                new String[] {String.valueOf(1)});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Log.d(TAG, "return allFavoriteHistories. list size: " + list.size());

        return list;
    }

    @Override
    public void deleteHistoryData(int id) {
        mDataBase.delete(HistoryShema.HistoryTable.NAME, HistoryShema.HistoryTable.Cols.ID + " = ?", new String[] {String.valueOf(id)});
    }

    @Override
    public void deleteAllHistoryData() {
        mDataBase.delete(HistoryShema.HistoryTable.NAME, null, null);
    }

    @Override
    public long getDataCount(String tableName) {
        return DatabaseUtils.queryNumEntries(mDataBase, tableName);
    }

    @Override
    public void makeHistoryFavorite(final int id, final boolean isFavorite) {
        HistoryData data = getHistory(id);
        data.setFavorite(isFavorite);
        ContentValues content = getContentValues(data);

        Log.d(TAG, "make favorite: " + data);

        mDataBase.update(HistoryShema.HistoryTable.NAME, content, HistoryShema.HistoryTable.Cols.ID + " = ?", new String[]{String.valueOf(id)});
    }

    public HistoryData getHistory(int id) {
        BaseCursorWrapper<HistoryData> cursor = queryData(HistoryShema.HistoryTable.NAME, HistoryShema.HistoryTable.Cols.ID + " = ?",
                new String[]{String.valueOf(id)});

        try {
            if (cursor.getCount() == 0) {
                return HistoryData.NullableHistoryData.getInstance();
            }
            cursor.moveToFirst();
            return cursor.getData();
        } finally {
            cursor.close();
        }
    }

    @Override
    public int getLastHistoryId() {
        BaseCursorWrapper<HistoryData> cursor = queryData(HistoryShema.HistoryTable.NAME, null, null);

        try {
            if (cursor.getCount() == 0) {
                return HistoryData.NullableHistoryData.getInstance().getKey();
            }
            cursor.moveToLast();
            return cursor.getData().getKey();
        } finally {
            cursor.close();
        }
    }
}
