package org.zenbaei.kalematAlQuraan.component.ayah.view;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.component.ayah.business.AyahService;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.R;

import java.util.List;

/**
 * Created by Islam on 11/18/2015.
 */
public class AyahActivity extends AppCompatActivity {

    private AyahService ayahService;

    public static int maxRowPerPage = 10;

    private int currentPage = 1;

    private int searchCurrentPage = 1;

    private int pageCount;

    private int searchPageCount;

    private int ayahColWidth = 10;

    private int kalemahColWidth = 50;

    private int tafsirColWidth = 400;

    private int rowTopPadding = 30;

    private long surahId;

    private String surahName;

    private boolean searchOpened;

    private EditText searchEditText;

    private MenuItem menuSearchButton;

    private List<Ayah> filteredAyahList;

    private List<Ayah> ayahList;

    private long languageId = 1;

    private enum SearchContext {AYAH, KALEMAH}

    private SearchContext searchContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayah);

        //initialize DAO
        ayahService = new AyahService(this);

        //get parameters from intent
        this.surahId = getIntent().getLongExtra("surahId", 0);
        this.surahName = getIntent().getStringExtra("surahName");

        //  setToolbarLayout();
        retrievePageCount();
        process();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: 11/18/2015 close database
    }


    private void executeMenuCloseAction() {
        searchEditText.getText().clear();
    }

    private int from(int currentPage) {
        return currentPage * maxRowPerPage - maxRowPerPage;
    }

    private int to() {
        return maxRowPerPage;
    }

    private void closeSearchBar() {

        // Remove custom view.
        getActionBar().setDisplayShowCustomEnabled(false);

        // Change search icon accordingly.
        // menuCloseButton.setIcon(iconOpenSearch);
        searchOpened = false;

    }
/*
    private MoviesListAdapter getListAdapter() {
        return (MoviesListAdapter) mMoviesLv.getAdapter();
    }*/


    private void setTableLayout() {

        //set surah header name
        ((TextView) findViewById(R.id.surahName)).setText(String.format("%s %s", "سورة", surahName));

        //get TableLayout
        TableLayout ayahTafsirTable = (TableLayout) findViewById(R.id.ayahTafsirTable);

        //clear the table on every render
        ayahTafsirTable.removeAllViewsInLayout();

        //horizontal line
        ayahTafsirTable.addView(getHorizontalLine());

        //header row
        ayahTafsirTable.addView(getHeaderRow());

        //horizontal line
        ayahTafsirTable.addView(getHorizontalLine());

        List<Ayah> list = null;

        if (isPrepareForRenderingMainPage())
            list = ayahList;
        else
            list = filteredAyahList;

        for (Ayah ayah : list) {
            //new TableRow
            TableRow tableRow = new TableRow(this);

            //add TableRow to TableLayout
            ayahTafsirTable.addView(tableRow);

            //set TableRow width & height
            tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            //create TextViews as table's cells
            TextView textView4Ayah = new TextView(this);
            TextView textView4Kalemah = new TextView(this);
            TextView textView4Tafsir = new TextView(this);

            //set max width to wrap content
            textView4Tafsir.setMaxWidth(tafsirColWidth);

            //add text to tableRow
            tableRow.addView(textView4Ayah);
            tableRow.addView(getVerticalLine(false));
            tableRow.addView(textView4Kalemah);
            tableRow.addView(getVerticalLine(false));
            tableRow.addView(textView4Tafsir);

            //set text
            textView4Ayah.setText(String.valueOf(ayah.getNumber()));
            textView4Kalemah.setText(ayah.getKalemah());
            textView4Tafsir.setText(ayah.getTafsir().getTafsir());

            //align ayah to center
            textView4Ayah.setGravity(Gravity.CENTER);

            //set text color
            textView4Kalemah.setTextColor(getResources().getColor(R.color.red));

            //set text padding
            textView4Ayah.setPadding(0, rowTopPadding, 0, 0);
            textView4Kalemah.setPadding(0, rowTopPadding, 0, 0);
            textView4Tafsir.setPadding(0, rowTopPadding, 0, 0);
        }

    }

    private void setPagingLayout() {
        //get paging TextView
        TextView pagingTextView = (TextView) findViewById(R.id.pagingTextView);
        //get previous TextView
        TextView pagingPreviousTextView = (TextView) findViewById(R.id.pagingPreviousTextView);
        //get next TextView
        TextView pagingNextTextView = (TextView) findViewById(R.id.pagingNextTextView);

        int curPage = isPrepareForRenderingMainPage() ? currentPage : searchCurrentPage;
        int pgCount = isPrepareForRenderingMainPage() ? pageCount : searchPageCount;

        //set paging contect
        pagingTextView.setText(String.format("%s %s %s", curPage, "من", pgCount)); //to do use bundle instead of من

        //dim Previous if first page
        if (curPage == 1)
            pagingPreviousTextView.setEnabled(false);
        else
            pagingPreviousTextView.setEnabled(true);

        //dim Next if last page
        if (curPage == pgCount)
            pagingNextTextView.setEnabled(false);
        else
            pagingNextTextView.setEnabled(true);
    }


    private void setToolbarLayout() {
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        // Set custom view on action bar.
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_view);*/

        // Change search icon accordingly.
        //menuCloseButton.setIcon(R.drawable.close_icon);

        getActionBar().hide();

        listenOnSearchText();
    }


    private void listenOnSearchText() {
        // Search edit text field setup.
        //  searchEditText = (EditText) getActionBar().getCustomView()
        //         .findViewById(R.id.searchText);
        searchEditText.addTextChangedListener(new SearchWatcher());
        searchEditText.requestFocus();
    }

    public TableRow getHorizontalLine() {
        //new TableRow
        TableRow horizontalLine = new TableRow(this);

        //set TableRow width & height
        horizontalLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //create new View represented as a cell
        View cell1View = new View(this);
        View cell2View = new View(this);
        View cell3View = new View(this);
        View cell4View = new View(this);
        View cell5View = new View(this);

        //add View to TableRow
        horizontalLine.addView(cell1View);
        horizontalLine.addView(cell2View);
        horizontalLine.addView(cell3View);
        horizontalLine.addView(cell4View);
        horizontalLine.addView(cell5View);

        //set View width & height
        cell1View.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        cell2View.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        cell3View.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        cell4View.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        cell5View.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));

        //set View background color
        cell1View.setBackgroundColor(getResources().getColor(R.color.black));
        cell2View.setBackgroundColor(getResources().getColor(R.color.black));
        cell3View.setBackgroundColor(getResources().getColor(R.color.black));
        cell4View.setBackgroundColor(getResources().getColor(R.color.black));
        cell5View.setBackgroundColor(getResources().getColor(R.color.black));

        return horizontalLine;
    }


    public LinearLayout getVerticalLine(boolean visible) {
        //new LinearLayout
        LinearLayout linearLayout = new LinearLayout(this);

        //set LinearLayout width & height
        linearLayout.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));


        //set layout color
        if (visible)
            linearLayout.setBackgroundColor(getResources().getColor(R.color.lightPink));

        //new View
        View view = new View(this);

        //add View to LinearLayout
        linearLayout.addView(view);

        //set View width & height
        view.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));

        if (visible)
            //set View background color
            view.setBackgroundColor(getResources().getColor(R.color.black));


        return linearLayout;
    }


    /**
     * Holds Ayah, Kalemah, Tafsir
     *
     * @return
     */
    public TableRow getHeaderRow() {
        TableRow headerRow = new TableRow(this);

        //set TableRow width & height
        headerRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        //create TextView
        TextView ayah = new TextView(this);
        TextView kalemah = new TextView(this);
        TextView tafsir = new TextView(this);


        //set content
        ayah.setText("الآية");
        kalemah.setText("الكلمة");
        tafsir.setText("التفسير");

        //set color
        ayah.setBackgroundColor(getResources().getColor(R.color.lightPink));
        kalemah.setBackgroundColor(getResources().getColor(R.color.lightPink));
        tafsir.setBackgroundColor(getResources().getColor(R.color.lightPink));

        //set max width
        ayah.setMaxWidth(ayahColWidth);
        kalemah.setMaxWidth(kalemahColWidth);
        tafsir.setMaxWidth(tafsirColWidth);

        //add onClick event to allow search
        ayah.setClickable(true);
        kalemah.setClickable(true);

        //ayah on click behavior
        ayah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ayahOnClickListener();
            }
        });

        //kalemah onClick behavior
        kalemah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kalemahOnClickListener();
            }
        });

        //add TextView to TableRow and add vertical line after every TextView
        headerRow.addView(ayah);
        //    headerRow.addView(getSearchImageView(searchImage));
        headerRow.addView(getVerticalLine(true));

        headerRow.addView(kalemah);
        //    headerRow.addView(getSearchImageView(searchImage));
        headerRow.addView(getVerticalLine(true));

        headerRow.addView(tafsir);

        return headerRow;
    }

    private void doSearch() {
        //retrieve surah tafsir
        if (isPrepareForRenderingMainPage())
            ayahList = ayahService.findBySurahIdLanguageId(surahId, languageId, from(currentPage), to());
        else
            filteredAyahList = searchContext.equals(SearchContext.AYAH) ?
                    ayahService.findByAyahNumberLanguageId(Long.parseLong(getSearchTextValue()), languageId, from(searchCurrentPage), to(), false)
                    :
                    ayahService.findByKalemahLanguageId(getSearchTextValue(), languageId, from(searchCurrentPage), to(), true);


    }

    private void retrievePageCount() {
        if (isPrepareForRenderingMainPage())
            pageCount = getNumberOfPages(ayahService.getCountBySurahId(surahId, false), maxRowPerPage);
        else
            searchPageCount = searchContext.equals(SearchContext.AYAH) ?
                    getNumberOfPages(ayahService.getCountByAyahNumber(Long.parseLong(getSearchTextValue()), false), maxRowPerPage)
                    :
                    getNumberOfPages(ayahService.getCountByKalemah(getSearchTextValue(), true), maxRowPerPage);
    }

    private void process() {
        doSearch();
        setTableLayout();
        setPagingLayout();
    }


    /**
     * pagingPreviousTextView onClick Listener
     *
     * @param view
     */
    public void previousPageListener(View view) {
        if (isPrepareForRenderingMainPage())
            currentPage--;
        else
            searchCurrentPage--;

        process();
    }

    /**
     * pagingNextTextView onClick Listener
     *
     * @param view
     */
    public void nextPageListener(View view) {
        if (isPrepareForRenderingMainPage())
            currentPage++;
        else
            searchCurrentPage++;

        process();
    }


    private void setActionBarLabel() {
        if (!searchOpened || !isSearchTextEmpty()) {
            getActionBar().setDisplayShowTitleEnabled(false);
            return;
        }

        getActionBar().setTitle(searchContext.toString());
        getActionBar().setDisplayShowTitleEnabled(true);
    }

    /**
     * Responsible for handling changes in search edit text.
     */
    class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {

        }


        @Override
        public void afterTextChanged(Editable editable) {
            searchOpened = true;
            setActionBarLabel();
            retrievePageCount();
            process();
        }


    }

    public int getNumberOfPages(int rowCount, int maxRowPerPage) {
        if (rowCount <= maxRowPerPage)
            return 1;

        Double rows = new Double(rowCount);
        Double max = new Double(maxRowPerPage);

        Double divide = rows / max;

        divide = Math.ceil(divide);

        return divide.intValue();
    }

    private boolean isSearchTextEmpty() {
        if (searchEditText.getText() == null)
            return true;

        boolean result = searchEditText.getText().toString().trim().isEmpty();

        return result;
    }

    private boolean isPrepareForRenderingMainPage() {
        if (!searchOpened || searchOpened && isSearchTextEmpty())
            return true;
        else //searchOpened = true
            return false;
    }

    private String getSearchTextValue() {
        if (searchEditText.getText() == null)
            return "";
        return searchEditText.getText().toString().trim();
    }

    private void ayahOnClickListener() {
        searchContext = SearchContext.AYAH;
        showActionBar();
        setActionBarLabel();
    }

    private void kalemahOnClickListener() {
        searchContext = SearchContext.KALEMAH;
        showActionBar();
        setActionBarLabel();
    }

    private void showActionBar() {
        getActionBar().show();
        searchOpened = true;
    }

    private void hideActionBar() {
        getActionBar().hide();
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
}



