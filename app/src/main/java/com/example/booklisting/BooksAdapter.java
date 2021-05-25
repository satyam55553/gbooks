package com.example.booklisting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BooksAdapter extends ArrayAdapter<Books> {
    private static final String LOG_TAG = BooksAdapter.class.getName();

    BooksAdapter(Context context, int id, List<Books> objects) {
        super(context, id, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        //if no item is present in AdapterView then use LayoutInflater
        if (listItemView == null) {
            Log.v(LOG_TAG, "ListItemView is null");
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.books_details_list, parent, false);
        }

        Books books = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        assert books != null;
        Log.v(LOG_TAG, "Setting the title");
        title.setText(books.getBookTitle());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(books.getBookAuthor());

        ImageView bookCover = (ImageView) listItemView.findViewById(R.id.booksThumbnail);
        bookCover.setImageDrawable(books.getImage());

        return listItemView;
    }


}
