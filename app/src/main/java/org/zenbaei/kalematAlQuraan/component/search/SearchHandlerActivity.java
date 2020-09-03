package org.zenbaei.kalematAlQuraan.component.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.SearchView;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.contentProvider.KalematContentProvider;
import org.zenbaei.kalematAlQuraan.component.ayah.view.SingleAyahActivity;

public class SearchHandlerActivity extends BaseActivity {

    private TextView mTextView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mTextView = findViewById(R.id.searchText);
        mListView = findViewById(R.id.searchList);
        mListView.setBackgroundColor(Initializer.getBackgroundColor());

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void setFontAndBackground() {
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            Intent wordIntent = new Intent(this, SingleAyahActivity.class);
            wordIntent.setData(intent.getData());
            startActivity(wordIntent);
            finish();
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Searches the dictionary and displays results for the given query.
     *
     * @param query The search query
     */
    private void showResults(String query) {

        Cursor cursor = getContentResolver().query(KalematContentProvider.CONTENT_URI, null, null,
                new String[]{query}, null);

        if (cursor == null) {
            // There are no results
            mTextView.setText(getString(R.string.no_results, new Object[]{query}));
        } else {
            // Display the number of results
            int count = cursor.getCount();
            String countString = getStringByCount(count, query);


            mTextView.setText(countString);

            // Create a simple cursor adapter for the definitions and apply them to the ListView
            SearchCursorAdapter cursorAdapter = new SearchCursorAdapter(this, cursor);

            mListView.setAdapter(cursorAdapter);

                        // Define the on-click listener for the list items
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open WordActivity with a specific word Uri
                    Intent wordIntent = new Intent(getApplicationContext(), SingleAyahActivity.class);
                    Uri data = Uri.withAppendedPath(KalematContentProvider.CONTENT_URI,
                            String.valueOf(id));
                    wordIntent.setData(data);
                    startActivity(wordIntent);
                }
            });

        }
    }

    private String getStringByCount(int count, String query) {
        if (count == 1)
            return String.format(getResources().getString(R.string.one), query);
        else if (count == 2)
            return String.format(getResources().getString(R.string.two), query);
        else if (count <= 10)
            return String.format(getResources().getString(R.string.three_to_ten), query, count);
        else
            return String.format(getResources().getString(R.string.other), query, count);
    }

    public void back(View view) {
        super.onBackPressed();
    }

}