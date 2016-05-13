package com.ennjapps.bucketnote.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ennjapps.bucketnote.R;
import com.ennjapps.bucketnote.adapters.OnAddListener;
import com.ennjapps.bucketnote.beans.Drop;
import com.ennjapps.bucketnote.extras.Util;


/**
 * Created by haider on 07-05-2016.
 */
public class DialogAdd extends DialogFragment
        implements TextView.OnEditorActionListener {

    private Activity mContext;




    //Title of the dialog
    private TextView mTitle;
    //The close button for this dialog
    private ImageButton mBtnClose;
    //The area where the user can type his/her goal
    private EditText mInputWhat;
    //The control with which user can select the date for his/her goal by which they feel they wanna accomplish their goal
    //The button clicking which the goal and date will be added to the database
    private Button mBtnAdd;
    //The object which will be notified when the user hits the "Add Drop" button
    private OnAddListener mListener;

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_it:
                    addAction();
                    break;
            }
            dismiss();
        }
    };

    public void setOnAddListener(OnAddListener listener) {
        mListener = listener;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        //The close button for this dialog
        mBtnClose = (ImageButton) view.findViewById(R.id.btn_close);
        //The area where the user can type his/her goal
        mInputWhat = (EditText) view.findViewById(R.id.et_drop);

         TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };



        mInputWhat.addTextChangedListener(textWatcher);


        //The control with which user can select the date for his/her goal by which they feel they wanna accomplish their goal

        //The button clicking which the goal and date will be added to the database
        mBtnAdd = (Button) view.findViewById(R.id.btn_add_it);

        //monitor the user clicking buttons such as DONE on the virtual keyboard
        mInputWhat.setOnEditorActionListener(this);
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnAdd.setOnClickListener(mBtnClickListener);
        //load custom fonts wherever appropriate

        //load custom fonts wherever appropriate
        initCustomFont();
    }


    private void initCustomFont() {
        mTitle.setTypeface(Util.loadRalewayRegular(mContext));
        mInputWhat.setTypeface(Util.loadRalewayThin(mContext));
        mBtnAdd.setTypeface(Util.loadRalewayThin(mContext));
    }
    private void addAction() {
        if (mListener != null) {
            //Load the taskname, convert the user entered date to a specific value of 0 hours 0 minutes and 0 seconds, 12 am precisely on the day they want things to be done
            String taskName = mInputWhat.getText().toString();
            long currentTime = System.currentTimeMillis();
            Drop drop = new Drop(taskName, currentTime,currentTime);

                mListener.onAdd(drop);

            }
        }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            //Hide the keyboard when the user presses done on it
            mBtnAdd.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mInputWhat.getWindowToken(), 0);
            return true;
        }
        return false;
    }
    private  void checkFieldsForEmptyValues(){


        String itemText = mInputWhat.getText().toString();



        if (itemText.length() > 0) {
            mBtnAdd.setEnabled(true);
        } else {
            mBtnAdd.setEnabled(false);
        }

    }

    }


