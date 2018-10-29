package com.example.android.book_listing;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private BookAdapter mAdapter;

    private static final int BOOK_LOADER_ID = 1;

    private TextView mEmptyStateTextView;

    EditText mSearchEditText;

    Button mSearchButton;

    ListView bookListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.text_no_data_found);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);

        mSearchEditText = (EditText) findViewById(R.id.searchEditText);

        mSearchButton = (Button) findViewById(R.id.btnSearch);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoader();
            }
        });

    }


    public void callLoader() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(BOOK_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }


    private String getUserInput() {
        return mSearchEditText.getText().toString();
    }

    private String getUrlForHttpRequest() {
        // url = "https://www.googleapis.com/books/v1/volumes?q="
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.googleapis.com")
                .appendPath("books")
                .appendPath("v1")
                .appendPath("volumes")
                .appendQueryParameter("q", getUserInput());
        String url = builder.build().toString();
        return url;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, getUrlForHttpRequest());

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
            mEmptyStateTextView.setVisibility(View.GONE);
        } else {
            mEmptyStateTextView.setText(R.string.noDataFound);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();

    }
}
