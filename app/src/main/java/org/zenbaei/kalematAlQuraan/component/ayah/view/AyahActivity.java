package org.zenbaei.kalematAlQuraan.component.ayah.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.business.AyahService;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.surah.view.SurahActivity;

import java.util.List;

/**
 * Created by Islam on 11/18/2015.
 */
public class AyahActivity extends AppCompatActivity {// implements GestureDetector.OnGestureListener {

    private AyahService ayahService;

    public static int maxRowPerPage = 10;

    private int currentPage = 1;

    private int pageCount;

    private int ayahColWidth;

    private int kalemahColWidth;

    private int tafsirColWidth;

    private int rowTopPadding = 30;

    private long surahId;

    private long languageId = 1;

    private String surahName;

    private List<Ayah> ayahList;

    private int screenWidth;

    private SearchView searchView;

    private ScaleGestureDetector mScaleDetector;

    private GestureDetectorCompat mDetector;

    private float mScaleFactor = 1.f;

    private RelativeLayout relativeLayout;

    private final String TAG = "AyahActivity";

    private TableLayout ayahTafsirTable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayah);

        relativeLayout = (RelativeLayout) findViewById(R.id.ayahLinearLayout);

        //initialize DAO
        ayahService = new AyahService(this);

        //get parameters from intent
        this.surahId = getIntent().getLongExtra("surahId", 0);
        this.surahName = getIntent().getStringExtra("surahName");

        addGestureListner();

        // Create our ScaleGestureDetector
        // mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());

        //  mDetector = new GestureDetectorCompat(this, this);


        calculateColumnSizes();

        //  setToolbarLayout();
        retrievePageCount();
        process();
    }

    private void addGestureListner() {
        RelativeLayout myView = (RelativeLayout) findViewById(R.id.ayahLinearLayout);

        myView.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override

            public void onSwipeDown() {

                // Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();

            }


            @Override

            public void onSwipeLeft() {
                previousPage();
            }


            @Override

            public void onSwipeUp() {
                //Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
            }


            @Override

            public void onSwipeRight() {
                nextPage();
            }

        });
    }

    private void calculateColumnSizes() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels - 2; //2 for the 2 vertical lines
        ayahColWidth = screenWidth * 15 / 100;
        kalemahColWidth = screenWidth * 30 / 100;
        tafsirColWidth = screenWidth * 55 / 100;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        searchView.setQuery("", false);
        searchView.setIconified(true);
    }


    private int from(int currentPage) {
        return currentPage * maxRowPerPage - maxRowPerPage;
    }

    private int to() {
        return maxRowPerPage;
    }


    private void addTableLayout() {

        //set surah header name
        ((TextView) findViewById(R.id.surahName)).setText(getString(R.string.surah, new Object[]{surahName}));

        //get TableLayout
        ayahTafsirTable = (TableLayout) findViewById(R.id.ayahTableLayout);

        //clear the table on every render
        ayahTafsirTable.removeAllViewsInLayout();

        //horizontal line
        ayahTafsirTable.addView(getHorizontalLine());

        //header row
        ayahTafsirTable.addView(getHeaderRow());

        //horizontal line
        ayahTafsirTable.addView(getHorizontalLine());

        setTableContent();

    }

    private void setTableContent() {
        for (Ayah ayah : ayahList) {
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


            //add text to tableRow
            tableRow.addView(textView4Ayah);
            tableRow.addView(getVerticalLine(false));
            tableRow.addView(textView4Kalemah);
            tableRow.addView(getVerticalLine(false));
            tableRow.addView(textView4Tafsir);

            //set max width to wrap content
            textView4Ayah.setWidth(ayahColWidth);
            textView4Kalemah.setWidth(kalemahColWidth);
            textView4Tafsir.setWidth(tafsirColWidth);

            //set text
            textView4Ayah.setText(String.valueOf(ayah.getNumber()));
            textView4Kalemah.setText(ayah.getKalemah());
            textView4Tafsir.setText(ayah.getTafsir().getTafsir());

            //align ayah to center
            textView4Ayah.setGravity(Gravity.RIGHT);

            //set text color
            textView4Kalemah.setTextColor(getResources().getColor(R.color.red));

            //set text padding
            textView4Ayah.setPadding(0, rowTopPadding, 0, 0);
            textView4Kalemah.setPadding(3, rowTopPadding, 0, 0);
            textView4Tafsir.setPadding(2, rowTopPadding, 0, 0);
        }
    }

    private void addPagingView() {
        //get paging TextView
        TextView pagingTextView = (TextView) findViewById(R.id.pagingTextView);

        //set paging content
        pagingTextView.setText(String.format("%s %s %s", currentPage, getString(R.string.from), pageCount));

        //bold
        pagingTextView.setTypeface(null, Typeface.BOLD);
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
        headerRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        //create TextView
        TextView ayah = new TextView(this, null);
        TextView kalemah = new TextView(this, null);
        TextView tafsir = new TextView(this, null);


        //set content
        ayah.setText(getString(R.string.ayah));
        kalemah.setText(getString(R.string.kalemah));
        tafsir.setText(getString(R.string.tafsir));

        //set color
        ayah.setBackgroundColor(getResources().getColor(R.color.lightPink));
        kalemah.setBackgroundColor(getResources().getColor(R.color.lightPink));
        tafsir.setBackgroundColor(getResources().getColor(R.color.lightPink));

        //set max width
        ayah.setWidth(ayahColWidth);
        kalemah.setWidth(kalemahColWidth);
        tafsir.setWidth(tafsirColWidth);

        ayah.setPadding(0, 0, 2, 0);

        //add TextView to TableRow and add vertical line after every TextView
        headerRow.addView(ayah);
        headerRow.addView(getVerticalLine(true));

        headerRow.addView(kalemah);
        headerRow.addView(getVerticalLine(true));

        headerRow.addView(tafsir);

        return headerRow;
    }

    private void doSearch() {
        //retrieve surah tafsir
        ayahList = ayahService.findBySurahIdLanguageId(surahId, languageId, from(currentPage), to());
    }

    private void retrievePageCount() {
        pageCount = getNumberOfPages(ayahService.getCountBySurahId(surahId, false), maxRowPerPage);
    }

    private void process() {
        doSearch();
        addTableLayout();
        addPagingView();
    }


    /**
     * pagingPreviousTextView onClick Listener
     *
     * @param
     */
    public void previousPage() {
        if (currentPage == 1)
            return;

        currentPage--;
        process();
    }

    /**
     * pagingNextTextView onClick Listener
     *
     * @param
     */
    public void nextPage() {
        if (currentPage == pageCount)
            return;

        currentPage++;
        process();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            showDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.about))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.thanks), null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void goToSurahIndex(View view) {
        Intent intent = new Intent(getApplicationContext(), SurahActivity.class);
        startActivity(intent);
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        boolean consumed = super.onTouchEvent(event);

        if (!consumed)
            mScaleDetector.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            Log.i("onScale", String.valueOf(detector.getPreviousSpan()));
            Log.i("onScale", String.valueOf(detector.getPreviousSpanX()));
            Log.i("onScale", String.valueOf(detector.getPreviousSpanY()));

            //  float

            if (mScaleFactor > 1) {
                Log.i("onScale", "Zooming In");
                relativeLayout.setScaleX(mScaleFactor);
                relativeLayout.setScaleY(mScaleFactor);
            } else {
                Log.i("onScale", "Zooming Out");
                //mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
                relativeLayout.setScaleX(1f);
                relativeLayout.setScaleY(1f);
            }


            // Don't let the object get too small or too large.
            //     mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            //  zoom(2f,2f,new PointF(0,0));
            //findViewById(R.id.ayahTafsirTable).invalidate();
            return true;
        }

    }*/
}




