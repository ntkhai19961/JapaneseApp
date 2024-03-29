package com.example.win7.apphoctiengnhat.Translate.ui.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.win7.apphoctiengnhat.Translate.presentation.contract.BaseContract;

/**
 * Created by NGusarov on 17/03/17.
 */

public abstract class BaseFragment extends Fragment implements BaseContract.BaseView {


    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}
