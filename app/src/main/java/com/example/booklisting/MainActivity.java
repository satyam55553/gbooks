package com.example.booklisting;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {
    private static String key;
    private static String BOOKS_REQUEST_URL;
    BooksAdapter booksAdapter;
    ProgressBar progressBar;
    TextView messageText;
    private static final int BOOKS_LOADER_ID = 1;//useful when there are multiple loaders
    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        progressBar = (ProgressBar) findViewById(R.id.loadingCircle);
        progressBar.setVisibility(View.GONE);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        messageText = (TextView) findViewById(R.id.messageText);

        final ListView listView = (ListView) findViewById(R.id.listViewBooks);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                EditText searchText = (EditText) findViewById(R.id.searchText);
                key = searchText.getText().toString();
                Log.v(LOG_TAG, "key = " + key);
                BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q={" + key + "}";
                booksAdapter = new BooksAdapter(MainActivity.this, R.layout.books_details_list, new ArrayList<Books>());
                listView.setAdapter(booksAdapter);
                listView.setVisibility(View.VISIBLE);
                Log.v(LOG_TAG, "I will be initialising the loader next ");
                getLoaderManager().initLoader(BOOKS_LOADER_ID, null, MainActivity.this);
                Toast.makeText(MainActivity.this, "UPDATING FEED...", Toast.LENGTH_SHORT).show();
                getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, MainActivity.this);

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books books = booksAdapter.getItem(position);
                assert books != null;
                Uri uriUrl = Uri.parse(books.getWebReaderLink());
                Intent intent=new Intent(Intent.ACTION_VIEW,uriUrl);
                startActivity(intent);
            }
        });
        if (networkInfo == null) {
            progressBar.setVisibility(View.GONE);
            messageText.setText("No Internet");
        } else {
            messageText.setText("");
        }

    }


    @Override
    public Loader<List<Books>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "I'm in onCreateLoader " + key);
        return new BooksLoader(this, BOOKS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> books) {
        Log.v(LOG_TAG, "I'm in onLoadFinished");
        progressBar.setVisibility(View.GONE);
        booksAdapter.clear();
        if (books != null && !books.isEmpty()) {
            messageText.setText("");
            booksAdapter.addAll(books);
        } else {
            messageText.setText("No Results Found");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        Log.v(LOG_TAG, "I'm in onLoaderReset");
        booksAdapter.clear();
    }
}
