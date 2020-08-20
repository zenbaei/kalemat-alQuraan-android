
package org.zenbaei.kalematAlQuraan.component.ayah.view;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.zenbaei.kalematAlQuraan.common.Initializer;
import org.zenbaei.kalematAlQuraan.common.activity.BaseActivity;
import org.zenbaei.kalematAlQuraan.common.helper.OnSwipeTouchListener;
import org.zenbaei.kalematAlQuraan.component.R;
import org.zenbaei.kalematAlQuraan.component.ayah.business.AyahService;
import org.zenbaei.kalematAlQuraan.component.ayah.entity.Ayah;
import org.zenbaei.kalematAlQuraan.component.setting.dao.SettingDAO;
import org.zenbaei.kalematAlQuraan.component.setting.entity.Setting;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Islam on 11/18/2015.
 */
public class AyahActivity extends BaseActivity {

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
    private ScrollView scrollView;
    private Handler handler = new Handler();
    private List<TextView> currentDisplayedNumberAndTafsirTextViews;
    private List<TextView> currentDisplayedAyah;
    private SettingDAO settingDAO;
    private View targetTableRow;
    private String targetTafsir;
    private String targetAyah;
    private String targetNumber;
    private TextView pagingTextView;
    private TextView surahNameTV;
    TextView ayah, kalemah, tafsir;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayah);

        relativeLayout = (RelativeLayout) findViewById(R.id.ayahRoot);

        scrollView = (ScrollView) findViewById(R.id.ayahScrollRoot);

        //initialize DAO
        ayahService = new AyahService(this);
        settingDAO = new SettingDAO(this);

        //get parameters from intent
        this.surahId = getIntent().getLongExtra("surahId", 0);
        this.surahName = getIntent().getStringExtra("surahName");

        int lastReadPage = getIntent().getIntExtra(Setting.KEY_NAME.LAST_READ_PAGE.name(), 0);
        // call from goToLastReadPage? if not property wont exist in Intent
        if (lastReadPage > 0) {
            this.surahId = getIntent().getIntExtra(Setting.KEY_NAME.LAST_READ_SURAH_ID.name(), 0);
            this.surahName = getIntent().getStringExtra(Setting.KEY_NAME.LAST_READ_SURAH_NAME.name());
            currentPage = lastReadPage;
        }

        calculateColumnSizes();

        //  setToolbarLayout();
        retrievePageCount();

        //get paging TextView
        pagingTextView = (TextView) findViewById(R.id.pagingTextView);
        surahNameTV = (TextView) findViewById(R.id.surahName);

        scrollView.setBackgroundColor(Initializer.getBackgroundColor());
        relativeLayout.setBackgroundColor(Initializer.getBackgroundColor());

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        addGestureListner();

        handler.post(new DoWork());
    }

    private void addGestureListner() {

        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override
            public void onSwipeLeft() {
                previousPage();
            }

            @Override
            public void onSwipeRight() {
                nextPage();
            }

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return super.onTouch(view, motionEvent);
            }

        });
    }

    private void calculateColumnSizes() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels - 40; //2 for the 2 vertical lines + 20 for table padding...
        ayahColWidth = screenWidth * 15 / 100;
        kalemahColWidth = screenWidth * 30 / 100;
        tafsirColWidth = screenWidth * 55 / 100;
        int total = ayahColWidth + kalemahColWidth + tafsirColWidth;
        Log.d("AayahActivity", "Screen Width: " + screenWidth + "Column sizes: " + total);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
        }
        resetTextSize();
        setFontAndBackground();
    }


    private int from(int currentPage) {
        return currentPage * maxRowPerPage - maxRowPerPage;
    }

    private int to() {
        return maxRowPerPage;
    }

    public void resetTextSize() {
        for (TextView tv : currentDisplayedNumberAndTafsirTextViews) {
            tv.setTextSize(Initializer.getFontSize());
        }
    }

    private void addTableLayout() {

        //set surah header name
        surahNameTV.setText(getString(R.string.surah, new Object[]{surahName}));

        //get TableLayout
        ayahTafsirTable = (TableLayout) findViewById(R.id.ayahTableLayout);

        TableLayout ayahHeaderTableLayout = (TableLayout) findViewById(R.id.ayahHeaderTableLayout);

        //clear the table on every render
        ayahHeaderTableLayout.removeAllViewsInLayout();
        ayahTafsirTable.removeAllViewsInLayout();

        //horizontal line
        ayahHeaderTableLayout.addView(getHorizontalLine());

        //header row
        ayahHeaderTableLayout.addView(getHeaderRow());

        //horizontal line
        ayahHeaderTableLayout.addView(getHorizontalLine());

        // reset current displayed Kalemah and Tafsir
        currentDisplayedNumberAndTafsirTextViews = new ArrayList<>();
        currentDisplayedAyah= new ArrayList<>();

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
            TextView textView4Number = new TextView(this);
            TextView textView4Ayah = new TextView(this);
            TextView textView4Tafsir = new TextView(this);

            //add text to tableRow
            tableRow.addView(textView4Tafsir);
            tableRow.addView(getVerticalLine(false));

            tableRow.addView(textView4Ayah);
            tableRow.addView(getVerticalLine(false));

            tableRow.addView(textView4Number);

            tableRow.setBackgroundColor(getResources().getColor(R.color.red));
            this.setTableRowOnLongPressListener(tableRow);


            //set max width to wrap content
            textView4Number.setWidth(ayahColWidth);
            textView4Ayah.setWidth(kalemahColWidth);
            textView4Tafsir.setWidth(tafsirColWidth);

            //set text
            textView4Number.setText(String.valueOf(ayah.getNumber()));
            textView4Ayah.setText(ayah.getKalemah());
            textView4Tafsir.setText(ayah.getTafsir().getTafsir());

            //set text padding
            textView4Number.setPadding(0, rowTopPadding, 10, 0);
            textView4Ayah.setPadding(3, rowTopPadding, 10, 0);
            textView4Tafsir.setPadding(2, rowTopPadding, 10, 0);

            textView4Number.setGravity(Gravity.RIGHT);

            //text size
            textView4Number.setTextSize(Initializer.getFontSize());
            textView4Ayah.setTextSize(Initializer.getFontSize());
            textView4Tafsir.setTextSize(Initializer.getFontSize());

            // set font and background color
            textView4Number.setTextColor(Initializer.getNonAyahFontColor());
            textView4Tafsir.setTextColor(Initializer.getNonAyahFontColor());
            textView4Ayah.setTextColor(Initializer.getFontColor());
            surahNameTV.setTextColor(Initializer.getFontColor());

            // bold
            textView4Ayah.setTypeface(null, Typeface.BOLD);

            addCurrentNumberAndTafsir(textView4Number, textView4Tafsir);
            currentDisplayedAyah.add(textView4Ayah);
        }
    }

    @Override
    public void setFontAndBackground() {
        for (TextView tv : currentDisplayedNumberAndTafsirTextViews) {
            tv.setTextColor(Initializer.getNonAyahFontColor());
        }
        for (TextView tv : currentDisplayedAyah) {
            tv.setTextColor(Initializer.getFontColor());
        }
        surahNameTV.setTextColor(Initializer.getFontColor());
        scrollView.setBackgroundColor(Initializer.getBackgroundColor());
        relativeLayout.setBackgroundColor(Initializer.getBackgroundColor());
        pagingTextView.setTextColor(Initializer.getNonAyahFontColor());
        //set color
        if (Initializer.getBackgroundColor() == getResources().getColor(R.color.darkGray)) {
            ayah.setBackgroundColor(getResources().getColor(R.color.gray));
            kalemah.setBackgroundColor(getResources().getColor(R.color.gray));
            tafsir.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            ayah.setBackgroundColor(getResources().getColor(R.color.lightPink));
            kalemah.setBackgroundColor(getResources().getColor(R.color.lightPink));
            tafsir.setBackgroundColor(getResources().getColor(R.color.lightPink));
        }
    }

    private void setTableRowOnLongPressListener(TableRow tableRow) {

        tableRow.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override
            public void onSwipeLeft() {
                previousPage();
            }

            @Override
            public void onSwipeRight() {
                nextPage();
            }

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                targetTableRow = view;
                setSelectedRowValue(view);
                mDetector.onTouchEvent(motionEvent);
                return super.onTouch(view, motionEvent);
            }
        });
    }

    private void setSelectedRowValue(View tableRow) {
        View tafsirTextView = ((TableRow) tableRow).getChildAt(0);
        View ayahTextView = ((TableRow) tableRow).getChildAt(2);
        View numberTextView = ((TableRow) tableRow).getChildAt(4);
        this.targetTafsir = ((TextView) tafsirTextView).getText().toString();
        this.targetAyah = ((TextView) ayahTextView).getText().toString();
        this.targetNumber = ((TextView) numberTextView).getText().toString();
    }

    /**
     * Add currently displayed Kalemah and Tafsir to easily get access to them when increasing
     * or decreasing font size.
     *
     * @param textView4Ayah
     * @param textView4Tafsir
     */
    private void addCurrentNumberAndTafsir(TextView textView4Ayah, TextView textView4Tafsir) {
        Log.d("AyahActivity", String.format("Adding Kalemah [%s]", textView4Ayah.getText()));
        currentDisplayedNumberAndTafsirTextViews.add(textView4Ayah);
        currentDisplayedNumberAndTafsirTextViews.add(textView4Tafsir);
    }

    private void addPagingView() {
        //set paging content
        pagingTextView.setText(String.format("%s %s %s", currentPage, getString(R.string.from), pageCount));

        //bold
        pagingTextView.setTypeface(null, Typeface.BOLD);
        pagingTextView.setTextColor(Initializer.getNonAyahFontColor());
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


        //set fav_list_item color
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
        ayah = new TextView(this, null);
        kalemah = new TextView(this, null);
        tafsir = new TextView(this, null);


        //set content
        ayah.setText(getString(R.string.ayah));
        kalemah.setText(getString(R.string.kalemah));
        tafsir.setText(getString(R.string.tafsir));

        //set color
        if (Initializer.getBackgroundColor() == getResources().getColor(R.color.darkGray)) {
            ayah.setBackgroundColor(getResources().getColor(R.color.gray));
            kalemah.setBackgroundColor(getResources().getColor(R.color.gray));
            tafsir.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            ayah.setBackgroundColor(getResources().getColor(R.color.lightPink));
            kalemah.setBackgroundColor(getResources().getColor(R.color.lightPink));
            tafsir.setBackgroundColor(getResources().getColor(R.color.lightPink));
        }

        //set max width
        ayah.setWidth(ayahColWidth);
        kalemah.setWidth(kalemahColWidth);
        tafsir.setWidth(tafsirColWidth);


        //set text padding
        ayah.setPadding(0, 0, 35, 0);
        kalemah.setPadding(0, 0, 2, 0);
        tafsir.setPadding(0, 0, 2, 0);

        //add TextView to TableRow and add vertical line after every TextView
        headerRow.addView(tafsir);
        headerRow.addView(getVerticalLine(true));

        headerRow.addView(kalemah);
        headerRow.addView(getVerticalLine(true));

        headerRow.addView(ayah);

        return headerRow;
    }

    private void doSearch() {
        //retrieve surah tafsir
        ayahList = ayahService.findBySurahIdLanguageId(surahId, languageId, from(currentPage), to());
    }

    private void retrievePageCount() {
        pageCount = getNumberOfPages(ayahService.getCountBySurahId(surahId, false), maxRowPerPage);
    }


    /**
     * pagingPreviousTextView onClick Listener
     *
     * @param
     */
    public void previousPage() {
        if (currentPage == 1) {
            Toast.makeText(this, R.string.ayahOnSwipeLeftHint, Toast.LENGTH_SHORT).show();
            return;
        }

        currentPage--;
        handler.post(new DoWork());
    }

    /**
     * pagingNextTextView onClick Listener
     *
     * @param
     */
    public void nextPage() {
        if (currentPage == pageCount) {
            Toast.makeText(this, R.string.ayahOnSwipeRightHint, Toast.LENGTH_SHORT).show();
            return;
        }
        currentPage++;
        handler.post(new DoWork());
    }

    void showCopyFavouriteMenu() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(AyahActivity.this, this.targetTableRow);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new OnMenuClickListenerImpl());

        popup.show();//showing popup menu
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

    private void saveLastReadPage() {
        settingDAO.update(Setting.KEY_NAME.LAST_READ_PAGE, String.valueOf(currentPage));
        settingDAO.update(Setting.KEY_NAME.LAST_READ_SURAH_ID, String.valueOf(surahId));
        settingDAO.update(Setting.KEY_NAME.LAST_READ_SURAH_NAME, surahName);
    }

    class DoWork implements Runnable {
        @Override
        public void run() {
            doSearch();
            addTableLayout();
            addPagingView();
            saveLastReadPage();
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public void onLongPress(MotionEvent e) {
            showCopyFavouriteMenu();
            super.onLongPress(e);
        }

    }

    class OnMenuClickListenerImpl implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String output = "";
            if (item.getTitle().equals(getString(R.string.copy))) {
                output = getString(R.string.copyDone);
                String text = String.format("\"%s\": %s. %s %s، %s %s", targetAyah, targetTafsir, "سورة",
                        surahName, "الآية", targetNumber);
                copyToClipboard(text);
            } else {
                output = getString(R.string.favouriteDone);
                settingDAO.insertIfNotExists(Setting.KEY_NAME.FAVOURITE, targetAyah + "#" + targetTafsir);
            }

            Toast.makeText(AyahActivity.this, output, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}




