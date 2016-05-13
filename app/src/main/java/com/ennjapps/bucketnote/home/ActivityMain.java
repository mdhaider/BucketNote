package com.ennjapps.bucketnote.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ennjapps.bucketnote.R;
import com.ennjapps.bucketnote.adapters.AdapterDrops;
import com.ennjapps.bucketnote.adapters.AddListener;
import com.ennjapps.bucketnote.adapters.OnAddListener;
import com.ennjapps.bucketnote.adapters.SimpleTouchCallback;
import com.ennjapps.bucketnote.beans.Drop;
import com.ennjapps.bucketnote.extras.MyPreferences;
import com.ennjapps.bucketnote.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.ennjapps.bucketnote.extras.Constants.ADDED;
import static com.ennjapps.bucketnote.extras.Constants.POSITION;
import static com.ennjapps.bucketnote.extras.Constants.SORT_ASCENDING_DATE;
import static com.ennjapps.bucketnote.extras.Constants.SORT_DESCENDING_DATE;

/**
 * Created by haider on 07-05-2016.
 */
public class ActivityMain extends AppCompatActivity {
    public static final String TAG_DIALOG = "dialog_add";
    private Realm mRealm;

    private static long back_pressed_time;
    private static long PERIOD = 2000;

    private RealmResults<Drop> mResults;
    private BucketRecyclerView mRecycler;
    private Button mBtnAdd;
    //The View to be displayed when the RecyclerView is empty.
    private View mEmptyView;
    private Toolbar mToolbar;
    private AdapterDrops mAdapter;
    private ImageView mBackground;
    private OnAddListener mOnAddListener = new OnAddListener() {
        @Override
        public void onAdd(Drop drop) {
            mAdapter.add(drop);
        }
    };
    //When the add row_drop button is clicked, show a dialog that lets the person add a new row_drop
    private View.OnClickListener mBtnAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };
    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };
    private DialogMark.MarkedListener mMarkedListener = new DialogMark.MarkedListener() {
        @Override
        public void onMarked(int position) {
            //Mark an row_drop as complete in our database when the user clicks "Mark as Complete"
            mAdapter.markComplete(position);
        }
    };
    private AdapterDrops.MarkListener mMarkListener = new AdapterDrops.MarkListener() {
        @Override
        public void onMark(int position) {
            //Launch the DialogMark which are shown when the user clicks on some row_drop from our RecyclerView
            Bundle arguments = new Bundle();
            arguments.putInt(POSITION, position);
            DialogMark dialog = new DialogMark();
            dialog.setArguments(arguments);
            dialog.setDialogActionsListener(mMarkedListener);
            dialog.show(getSupportFragmentManager(), "dialog_mark");
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPreferences pref = new MyPreferences(ActivityMain.this);
        if (pref.isFirstTime()) {
            displayToast( "Hi" +""+ pref.getUsername());

            pref.setOld(true);
        } else {
            displayToast( "Welcome back" + " " + pref.getUsername());

        }

        mRealm = Realm.getDefaultInstance();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initRecycler();

    }

    private void initRecycler() {
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mEmptyView = findViewById(R.id.empty_drops);
        mRecycler.setViewsToHideWhenEmpty(mToolbar);
        mRecycler.setViewsToShowWhenEmpty(mEmptyView);

        //Add a divider to our RecyclerView
        LinearLayoutManager linearLM = new LinearLayoutManager(this);
        linearLM.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLM);
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mBtnAdd = (Button) findViewById(R.id.btn_add);

        mBtnAdd.setOnClickListener(mBtnAddListener);
        mResults = mRealm.where(Drop.class).findAllSortedAsync(ADDED);
        mAdapter = new AdapterDrops(this, mRealm, mResults);
        mAdapter.setHasStableIds(true);
        mResults.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                mAdapter.updateResults(mResults);
                mResults.removeChangeListener(this);
            }
        });
        //Let our Activity handle the event when the footer is clicked from our RecyclerView
        mAdapter.setAddListener(mAddListener);
        //Let our Activity handle the event when the Add Drop button is clicked from the empty view
        mAdapter.setMarkListener(mMarkListener);

        //Handler the swipe from our RecyclerView
        ItemTouchHelper.Callback callback =
                new SimpleTouchCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecycler);
        mRecycler.setAdapter(mAdapter);
    }


    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.setOnAddListener(mOnAddListener);
        dialog.show(getSupportFragmentManager(), TAG_DIALOG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar row_drop clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean handled = true;
        switch (id) {
            case R.id.action_add:
                showDialogAdd();
                break;
            case R.id.action_linear:
                LinearLayoutManager linearLM = new LinearLayoutManager(this);
                linearLM.setOrientation(LinearLayoutManager.VERTICAL);
                mRecycler.setLayoutManager(linearLM);

                break;
            case R.id.action_grid:
                LinearLayoutManager gridLM = new GridLayoutManager(this,2);
                gridLM.setOrientation(LinearLayoutManager.VERTICAL);
                mRecycler.setLayoutManager(gridLM);


                break;
            case R.id.settings:
                Intent i = new Intent(this, SettingsExampleActivity.class);
                startActivity(i);
                break;
            default:
                handled = false;
                break;
        }
        int sortOption = SORT_ASCENDING_DATE;

        if (id == R.id.action_sort_ascending_date) {
            sortOption = SORT_ASCENDING_DATE;
            mResults = mRealm.where(Drop.class).findAllSortedAsync(ADDED, Sort.ASCENDING);
        } else if (id == R.id.action_sort_descending_date) {
            sortOption = SORT_DESCENDING_DATE;
            mResults = mRealm.where(Drop.class).findAllSortedAsync(ADDED, Sort.DESCENDING);
        } else {
            mResults = mRealm.where(Drop.class).findAllAsync();
        }
        AppBucketDrops.storeSortOption(sortOption);
        mResults.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                mAdapter.updateResults(mResults);
                mResults.removeChangeListener(this);
            }
        });
        return handled;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed_time + PERIOD > System.currentTimeMillis()) super.onBackPressed();
        else
            displayToast("Press once again to exit!");
            //Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed_time = System.currentTimeMillis();
    }
        public void displayToast(String message) {
            // Inflate toast XML layout
            View layout = getLayoutInflater().inflate(R.layout.toast_layout,
                    (ViewGroup) findViewById(R.id.toast_layout_root));
            // Fill in the message into the textview
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(message);
            // Construct the toast, set the view and display
            Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
}