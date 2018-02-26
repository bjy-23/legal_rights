package com.wonders.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.legal_rights.R;

import java.util.List;
import java.util.Map;

/**
 * Created by chan on 10/25/16.
 */
public class ListDialog extends DialogFragment {
    public static String TAG = "ListDialog";
    private OnItemClickListener l;
    private List<Map.Entry<String, String>> items;
    private CharSequence[] arrItems;
    private boolean itemsType = false;
    private ListView mListView;
    private TextView mTitleText;
    private CharSequence mTitle;
    private Button mBtnCancel;
    private CharSequence mNegativeText;
    private View.OnClickListener listener;

    public ListDialog setTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public ListDialog setItems(CharSequence[] arrItems, OnItemClickListener listener) {
        itemsType = false;
        if (!itemsType) {
            this.arrItems = arrItems;
            this.l = listener;
        }
        return this;
    }

    public ListDialog setItems(List<Map.Entry<String, String>> items, OnItemClickListener listener) {
        itemsType = true;
        if (itemsType) {
            this.items = items;
            this.l = listener;
        }
        return this;
    }

    public ListDialog setNegativeButton(CharSequence text, View.OnClickListener listener) {
        this.mNegativeText = text;
        this.listener = listener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        return inflater.inflate(R.layout.dialog_dept, container, false);
    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        create();
    }


    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    private void create() {
        View view = getView();
        setupTitle(view);
        setupListView(view);
        setupBottom(view);
        setupWindowParams();
    }

    private void setupTitle(View view) {
        mTitleText = (TextView) view.findViewById(R.id.title);
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleText.setText(mTitle);
        }
    }

    private void setupListView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setAdapter(new ItemAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != l)
                    l.OnItemClick(view, position);
            }
        });

    }

    private void setupBottom(View view) {
        mBtnCancel = (Button) view.findViewById(R.id.btn_three);
        if (!TextUtils.isEmpty(mNegativeText)) {
            mBtnCancel.setText(mNegativeText);
        }
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != listener)
                    listener.onClick(v);
            }
        });
    }

    private void setupWindowParams() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getDialog().getWindow().setAttributes(params);
        int height;
        if (dm.heightPixels * 0.5 > getDialog().getWindow().getAttributes().height) {
            height = getDialog().getWindow().getAttributes().height;
        } else {
            height = (int) (dm.heightPixels * 0.5);
        }
        getDialog().getWindow().setLayout((int) (dm.widthPixels - getResources().getDimension(R.dimen.screen_margin)),
                getDialog().getWindow().getAttributes().height);
    }

    public class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemsType ? items.size() : arrItems.length;
        }

        @Override
        public Object getItem(int position) {
            return itemsType ? items.get(position) : arrItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_dept, parent, false);
            }
            Button btn = (Button) convertView.findViewById(R.id.btn);
            btn.setText(itemsType ? ((Map.Entry<String, String>) getItem(position)).getValue() : getItem(position).toString());
            return convertView;
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }
}
