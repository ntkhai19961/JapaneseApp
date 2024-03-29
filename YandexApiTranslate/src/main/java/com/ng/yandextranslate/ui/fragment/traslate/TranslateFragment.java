package com.ng.yandextranslate.ui.fragment.traslate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.ng.yandextranslate.App;
import com.ng.yandextranslate.R;
import com.ng.yandextranslate.model.pojo.LanguagePair;
import com.ng.yandextranslate.presentation.contract.translate.TranslateContract;
import com.ng.yandextranslate.presentation.implementation.translate.DaggerTranslateComponent;
import com.ng.yandextranslate.presentation.implementation.translate.TranslateComponent;
import com.ng.yandextranslate.presentation.implementation.translate.TranslateModule;
import com.ng.yandextranslate.presentation.implementation.translate.TranslatePresenterImpl;
import com.ng.yandextranslate.ui.fragment.BaseFragment;
import com.ng.yandextranslate.ui.view.LanguageSelectView;
import com.ng.yandextranslate.util.DebounceTextWatcher;
import com.ng.yandextranslate.util.DoneOnEditorActionListener;

import static com.ng.yandextranslate.util.AppPrefs.DELAY_BEFORE_POST;

/**
 * Created by NG on 15.03.17.
 */

public class TranslateFragment extends BaseFragment implements TranslateContract.View {

    private static final String TAG = TranslateFragment.class.getSimpleName();

    //@BindView(R.id.translate_language_select)
    LanguageSelectView mLanguageSelectView;

    //@BindView(R.id.translate_edit_text_in)
    EditText mEditTextIn;

    //@BindView(R.id.translate_text_view_out)
    TextView mTextViewOut;

    //@BindView(R.id.translate_progress_for_text)
    ProgressBar mProgressBar;

    //@BindView(R.id.translate_button_clear)
    ImageButton mButtonClear;

    //@BindView(R.id.translate_button_add_to_favor)
    ImageButton mButtonAddToFavor;

    private static TranslateComponent mTranslateComponent;

    @Inject
    TranslatePresenterImpl mPresenter;

    @Inject
    DebounceTextWatcher mDebounceTextWatcher;

    public static Fragment newInstance(Bundle args) {
        Fragment fragment = new TranslateFragment();
        return fragment;
    }

    private void initView()
    {
        mLanguageSelectView = getView().findViewById(R.id.translate_language_select);

        mEditTextIn = getView().findViewById(R.id.translate_edit_text_in);

        mTextViewOut = getView().findViewById(R.id.translate_text_view_out);

        mProgressBar = getView().findViewById(R.id.translate_progress_for_text);

        mButtonClear = getView().findViewById(R.id.translate_button_clear);

        mButtonAddToFavor = getView().findViewById(R.id.translate_button_add_to_favor);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);

        ButterKnife.bind(this, rootView);

        initView();

        mTranslateComponent = buildTranslateComponent();
        mTranslateComponent.inject(this);

        mEditTextIn.setOnEditorActionListener(new DoneOnEditorActionListener());

        mEditTextIn.addTextChangedListener(mDebounceTextWatcher);

        mLanguageSelectView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearText();
            }
        });

        mButtonAddToFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!TextUtils.isEmpty(mEditTextIn.getText().toString())) {
                    mPresenter.makeLastFavorite();
                }
            }
        });

        return rootView;
    }

    public static TranslateComponent getTranslateComponent() {
        return mTranslateComponent;
    }

    private TranslateComponent buildTranslateComponent() {
        return DaggerTranslateComponent.builder()
                .appComponent(App.getAppComponent())
                .translateModule(new TranslateModule(this))
                .build();
    }

    public void showDialog(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getFromSpinnerPosition() {
        return mLanguageSelectView.getFromSpinnerPosition();
    }

    @Override
    public int getToSpinnerPosition() {
        return mLanguageSelectView.getToSpinnerPosition();
    }

    @Override
    public void setFromSpinnerSelection(final int position) {
        mLanguageSelectView.setFromSpinnerPosition(position);
    }

    @Override
    public void setToSpinnerPosition(final int position) {
        mLanguageSelectView.setToSpinnerPosition(position);
    }

    @Override
    public void showProgressBar() {
        Log.d(TAG, "showProgressBar");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mButtonAddToFavor.setEnabled(false);
                mTextViewOut.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void dismissProgressBar() {
        Log.d(TAG, "dismissProgressBar");
        mButtonAddToFavor.setEnabled(true);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mButtonAddToFavor.setEnabled(true);
                mTextViewOut.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void setLanguages() {
        mLanguageSelectView.setLanguages();
    }

    @Override
    public void showTranslateResult(String message) {
        mTextViewOut.setText(message);
    }

    @Override
    public void invalidateSpinnerView() {
        mLanguageSelectView.invalidate();
    }

    @Override
    public void swapText() {
        String tmp = mEditTextIn.getText().toString();
        mEditTextIn.setText(mTextViewOut.getText().toString());
        mTextViewOut.setText(tmp);
    }

    @Override
    public void clearText() {
        mEditTextIn.getText().clear();
        mTextViewOut.setText(null);
    }

    @Override
    public String getOriginal() {
        String result = mEditTextIn.getText().toString();
        Log.d(TAG, "result: " + result);
        return result;
    }

    @Override
    public void showError(final String errorMessage) {
        showDialog(errorMessage);
    }
}
