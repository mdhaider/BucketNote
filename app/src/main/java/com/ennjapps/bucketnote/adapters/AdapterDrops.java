package com.ennjapps.bucketnote.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ennjapps.bucketnote.R;
import com.ennjapps.bucketnote.beans.Drop;
import com.ennjapps.bucketnote.extras.Util;
import com.ennjapps.bucketnote.home.AppBucketDrops;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by haider on 07-05-2016.
 */
public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnSwipeListener {
    public static final int ITEM = 1;
    public static final int NO_ITEM = 2;
    public static final int FOOTER = 3;
    private Context mContext;
    private LayoutInflater mInflater;
    private AddListener mAddListener;
    private MarkListener mMarkListener;
    private Realm mRealm;

    private RealmResults<Drop> mResults;
    private int mSort;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results) {
        mRealm = realm;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        updateResults(results);
    }

    public void updateResults(RealmResults<Drop> results) {
        mResults = results;
        mSort = AppBucketDrops.loadSortOption();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        if (mResults == null) {
            return RecyclerView.NO_ID;
        } else {
            return position < mResults.size() ? mResults.get(position).getAdded() : RecyclerView.NO_ID;
        }
    }

    public void setMarkListener(MarkListener listener) {
        mMarkListener = listener;
    }

    public void setAddListener(AddListener listener) {
        mAddListener = listener;
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (mResults == null) {
            return 0;

        } else {
            return mResults.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mResults == null) {
            return ITEM;

        } else {
            if (position < mResults.size()) {
                return ITEM;
            } else {
                return FOOTER;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View root = mInflater.inflate(R.layout.footer, parent, false);
            FooterHolder viewHolder = new FooterHolder(root);
            return viewHolder;

        } else if (viewType == NO_ITEM) {
            View root = mInflater.inflate(R.layout.no_item, parent, false);
            NoItemsHolder viewHolder = new NoItemsHolder(root);
            return viewHolder;
        } else {
            View root = mInflater.inflate(R.layout.row_drop, parent, false);
            DropHolder dropHolder = new DropHolder(root);
            return dropHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) viewHolder;
            Drop drop = mResults.get(position);
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setCreationdate(drop.getCreationdate());

        } else if (viewHolder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) viewHolder;
            footerHolder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddListener != null) {
                        mAddListener.add();
                    }
                }
            });
        }
    }

    public void markComplete(int position) {
        if (position < mResults.size()) {
            Drop drop = mResults.get(position);
            mRealm.beginTransaction();
            notifyDataSetChanged();
            mRealm.commitTransaction();
        }
    }

    /**
     * @param position the position of the row_drop that was swiped within the RecyclerView
     */
    @Override
    public void onSwipe(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).removeFromRealm();
            mRealm.commitTransaction();
            notifyDataSetChanged();
        }
    }

    public void add(Drop drop) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(drop);
        mRealm.commitTransaction();
        notifyDataSetChanged();
    }

    /**
     * An interface that notifies your class when any row_drop is clicked from your RecyclerView
     */
    public interface MarkListener {
        /**
         * @param position is the position of the row_drop that was clicked by the user inside the RecylerView
         */
        void onMark(int position);
    }

    public class NoItemsHolder extends RecyclerView.ViewHolder {

        public NoItemsHolder(View itemView) {
            super(itemView);
        }
    }

    public class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextWhat;
        private TextView mTextWhen;
        private View mRoot;

        public DropHolder(View itemView) {
            super(itemView);
            mRoot = itemView;
            mTextWhat = (TextView) mRoot.findViewById(R.id.tv_what);
            mTextWhen = (TextView) mRoot.findViewById(R.id.text_when);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mMarkListener != null) {
                //Notify interested classes about the row_drop that was clicked at the current position
                mMarkListener.onMark(getAdapterPosition());
                Toast.makeText(mContext, mTextWhat.getText(), Toast.LENGTH_SHORT).show();
            }

        }

        public void setWhat(String text) {
            mTextWhat.setText(text);
        }



        public void setCreationdate(long creationdate) {
            mTextWhen.setText(Util.getFormattedDate(creationdate));
        }

        @SuppressLint("NewApi")
        public void setBackground(boolean isCompleted) {
            //For items whose status is marked as complete, we would like to highlight them with a different background
            Drawable background = null;
            if (isCompleted) {
                background = new ColorDrawable(Color.argb(237, 142, 121, 187));
            } else {
                background = mContext.getResources().getDrawable(R.drawable.bg_row_drop);
            }
            if (Util.isJellyBeanOrMore()) {
                mRoot.setBackgroundDrawable(background);
            } else {
                mRoot.setBackground(background);
            }
        }


    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        private Button mBtnAdd;

        public FooterHolder(View itemView) {
            super(itemView);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);

        }
    }
}
