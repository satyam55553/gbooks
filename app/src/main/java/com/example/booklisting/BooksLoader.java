package com.example.booklisting;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.AsyncTaskLoader;
import android.util.Log;

public class BooksLoader extends AsyncTaskLoader<List<Books>> {
    private String mUrl;
    private static final String LOG_TAG = BooksLoader.class.getName();

    BooksLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "I'm in onStartLoading");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Books> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        Log.v(LOG_TAG, "I'm in loadInBackground");
        return QueryUtils.fetchBooksDetails(mUrl);
    }
}
