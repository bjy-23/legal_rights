/**
 *
 */
package com.wonders.widget;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.legal_rights.R;

/**
 * @author chenjn
 * @date 下午3:07:30
 */
public class DatePickerDialog extends Dialog {

    /**
     * @param context
     */
    public DatePickerDialog(Context context) {
        super(context);
    }

    public DatePickerDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setLayout(double width, double height) {
        getWindow().setLayout((int) width, (int) height);
    }

    public void setAnimStyle(int styleId) {
        if (getWindow() != null) {
            getWindow().setWindowAnimations(styleId);
        }
    }

    public static class Builder {
        private static final int DEFAULT_STYLE = R.style.UIDialog;
        private Context mContext;
        private View mTitleDivide;
        private LinearLayout mTitlePanel;
        private LinearLayout mButtonPanel;
        private LinearLayout mDayPanel;
        private Button mNegativeButton;
        private Button mPositiveButton;
        private CharSequence mPositiveText;
        private CharSequence mNegativeText;
        private TextView mTitleTextView;
        private CharSequence mTitleText;
        private OnDatePickListener mPositiveButtonListener;
        private OnClickListener mNegativeButtonListener;
        private DatePickerDialog mDialog;
        private View mDividerLineVertical;
        private NumberPicker npYear, npMonth, npDay;
        private int mYear, mMonth, mDay, mCurrentYear, mCurrentMonth, mCurrentDay;
        private Calendar calendar;
        private boolean mDayOff = false;

        public Builder(Context context) {
            this.mContext = context;
            calendar = Calendar.getInstance();
        }

        public Builder setTitle(int titleId) {
            this.mTitleText = getText(mContext, titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitleText = title;
            return this;
        }

        public Builder setDayOff(boolean off) {
            this.mDayOff = off;
            return this;
        }

        public Builder setPositiveButton(int textId, OnDatePickListener listener) {
            this.mPositiveText = getText(mContext, textId);
            this.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnDatePickListener listener) {
            this.mPositiveText = text;
            this.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, OnClickListener listener) {
            this.mNegativeText = getText(mContext, textId);
            this.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            this.mNegativeText = text;
            this.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setDate(int year, int month, int day) {
            this.mYear = year;
            this.mMonth = month;
            this.mDay = day;
            return this;
        }

        public DatePickerDialog create() {

            mDialog = new DatePickerDialog(mContext, DEFAULT_STYLE);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View contentView = inflater.inflate(R.layout.layout_dialog_date, null);

            setupTitlePanel(contentView); // set title panel

            setupDatePanel(contentView); // set content panel
            initDate();

            setupButtonPanel(contentView); // set button panel

            mDialog.setContentView(contentView);

            setupWindowParams();

            return mDialog;
        }

        private void setupWindowParams() {
            mDialog.setAnimStyle(R.style.UIDialog);

            WindowManager wManager = mDialog.getWindow().getWindowManager();
            double width = wManager.getDefaultDisplay().getWidth() * 0.8;
            mDialog.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        private void setupTitlePanel(View contentView) {
            mTitlePanel = (LinearLayout) contentView.findViewById(R.id.title_panel);
            mTitleTextView = (TextView) contentView.findViewById(R.id.title);
            mTitleDivide = (View) contentView.findViewById(R.id.include_title_divide);

            if (!TextUtils.isEmpty(mTitleText)) {
                mTitleTextView.setText(mTitleText);
            } else {
                mTitlePanel.setVisibility(View.GONE);
                mTitleDivide.setVisibility(View.GONE);
            }
        }

        private void setupDayPanel(View contentView) {
            mDividerLineVertical = (View) contentView.findViewById(R.id.divider_line_vertical);
            mDividerLineVertical.setVisibility(View.VISIBLE);
            mDayPanel = (LinearLayout) contentView.findViewById(R.id.linear_day);
            mDayPanel.setVisibility(View.VISIBLE);
            npDay = (NumberPicker) contentView.findViewById(R.id.np_day);
            mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
            npDay.setMaxValue(31);
            npDay.setMinValue(1);
            npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    if (picker.getValue() == mCurrentMonth) {
                        npDay.setMaxValue(mCurrentDay);
                    } else if (picker.getValue() == 2) {
                        if (npYear.getValue() % 4 == 0 && npYear.getValue() % 100 != 0 || npYear.getValue() % 400 == 0) {
                            npDay.setMaxValue(29);
                        } else {
                            npDay.setMaxValue(28);
                        }
                    } else if (picker.getValue() == 1 || picker.getValue() == 3 || picker.getValue() == 5
                            || picker.getValue() == 7 || picker.getValue() == 8 || picker.getValue() == 10
                            || picker.getValue() == 12) {
                        npDay.setMaxValue(31);
                    } else {
                        npDay.setMaxValue(30);
                    }
                    npDay.setMinValue(1);
                }
            });
        }

        private void setupDatePanel(View contentView) {
            npYear = (NumberPicker) contentView.findViewById(R.id.np_year);
            npMonth = (NumberPicker) contentView.findViewById(R.id.np_month);
            if (!mDayOff) {
                setupDayPanel(contentView);
            }
            mCurrentYear = calendar.get(Calendar.YEAR);
            mCurrentMonth = calendar.get(Calendar.MONTH) + 1;

            npYear.setMaxValue(mCurrentYear);
            npYear.setMinValue(1900);
            npYear.setValue(mCurrentYear);

            npMonth.setMaxValue(12);
            npMonth.setMinValue(1);

            npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    // 选择的年份与当前年份相同时最大月份是当前月，选择的月份与当前月分相同时最大日期是当前日期
                    if (picker.getValue() == mCurrentYear) {
                        npMonth.setMaxValue(mCurrentMonth);
                        npMonth.setMinValue(1);
                    } else {
                        npMonth.setMaxValue(12);
                        npMonth.setMinValue(1);
                        if (!mDayOff) {
                            if (npMonth.getValue() == mCurrentMonth) {
                                npDay.setMaxValue(mCurrentDay);
                            } else if (npMonth.getValue() == 2) {
                                if (picker.getValue() % 4 == 0 && picker.getValue() % 100 != 0
                                        || picker.getValue() % 400 == 0) {
                                    npDay.setMaxValue(29);
                                } else {
                                    npDay.setMaxValue(28);
                                }
                            }
                            npDay.setMinValue(1);
                        }
                    }
                }
            });

        }

        private void initDate() {
            if (mYear == 0 && mMonth == 0 && mDay == 0) {
                mYear = mCurrentYear;
                mMonth = mCurrentMonth;
                mDay = mCurrentDay;
            }
            if (mYear == mCurrentYear) {
                npYear.setValue(mYear);
                npMonth.setMaxValue(mCurrentMonth);
                npMonth.setMinValue(1);
            } else if (mYear <= npYear.getMaxValue() && mYear >= npYear.getMinValue()) {
                npYear.setValue(mYear);
            }
            if (mMonth == mCurrentMonth) {
                npMonth.setValue(mMonth);
                if (!mDayOff) {
                    npDay.setMaxValue(mCurrentDay);
                    npDay.setMinValue(1);
                }
            } else if (mMonth <= npMonth.getMaxValue() && mMonth >= npMonth.getMinValue()) {
                npMonth.setValue(mMonth);
                if (!mDayOff) {
                    if (mMonth == 2) {
                        if (mYear % 4 == 0 && mYear % 100 != 0 || mYear % 400 == 0) {
                            npDay.setMaxValue(29);
                        } else {
                            npDay.setMaxValue(28);
                        }
                    } else if (mMonth == 1 || mMonth == 3 || mMonth == 5 || mMonth == 7 || mMonth == 8 || mMonth == 10
                            || mMonth == 12) {
                        npDay.setMaxValue(31);
                    } else {
                        npDay.setMaxValue(30);
                    }
                    npDay.setMinValue(1);
                }
            }
            if (!mDayOff) {
                if (mDay <= npDay.getMaxValue() && mDay >= npDay.getMinValue()) {
                    npDay.setValue(mDay);
                }
            }
        }

        private void setupButtonPanel(View contentView) {
            mPositiveButton = (Button) contentView.findViewById(R.id.btn_positive);
            mNegativeButton = (Button) contentView.findViewById(R.id.btn_negative);
            mButtonPanel = (LinearLayout) contentView.findViewById(R.id.button_panel);

            boolean showPositive = false;
            boolean showNegative = false;

            // set the confirm button visible
            if (!TextUtils.isEmpty(mPositiveText)) {
                showPositive = true;
                mPositiveButton.setText(mPositiveText);
                mPositiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mPositiveButtonListener != null) {
                            if (!mDayOff) {
                                mPositiveButtonListener.onDatePick(mDialog, DialogInterface.BUTTON_POSITIVE,
                                        npYear.getValue(), npMonth.getValue(), npDay.getValue());
                            } else {
                                mPositiveButtonListener.onDatePick(mDialog, DialogInterface.BUTTON_POSITIVE,
                                        npYear.getValue(), npMonth.getValue());
                            }
                        }
                        mDialog.dismiss();
                    }
                });
            } else {
                mPositiveButton.setVisibility(View.GONE);
            }

            // set the cancel button visible
            if (!TextUtils.isEmpty(mNegativeText)) {
                showNegative = true;
                mNegativeButton.setText(mNegativeText);
                mNegativeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mNegativeButtonListener != null) {
                            mNegativeButtonListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                        mDialog.dismiss();
                    }
                });
            } else {
                mNegativeButton.setVisibility(View.GONE);
            }

            // set the button panel layout visible
            if (!showPositive && !showNegative) {
                mButtonPanel.setVisibility(View.GONE);
            }
        }

        public DatePickerDialog show() {
            mDialog = create();
            mDialog.show();
            return mDialog;
        }

        private CharSequence getText(Context context, int resId) {
            if (context == null) {
                return null;
            }
            try {
                return context.getText(resId);
            } catch (Exception e) {
                android.util.Log.e("test", "getText exception: " + e.getMessage());
            }
            return null;
        }

        public interface OnDatePickListener {
            void onDatePick(DialogInterface dialog, int witch, int... args);
        }
    }

}
