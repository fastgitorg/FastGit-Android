package com.fastaccess.ui.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evernote.android.state.StateSaver;
import com.fastaccess.R;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import net.grandcentrix.thirtyinch.TiDialogFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Kosh on 27 May 2017, 1:51 PM
 */

public abstract class BaseMvpBottomSheetDialogFragment<V extends BaseMvp.FAView, P extends BasePresenter<V>> extends TiDialogFragment<P, V>
        implements BaseMvp.FAView {

    protected BaseMvp.FAView callback;

    @Nullable private Unbinder unbinder;

    @LayoutRes protected abstract int fragmentLayout();

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseMvp.FAView) {
            callback = (BaseMvp.FAView) context;
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        StateSaver.saveInstanceState(this, outState);
        getPresenter().onSaveInstanceState(outState);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, AppHelper.isNightMode(getResources()) ? R.style.DialogThemeDark : R.style.DialogThemeLight);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            StateSaver.restoreInstanceState(this, savedInstanceState);
            getPresenter().onRestoreInstanceState(savedInstanceState);
        }
        getPresenter().setEnterprise(isEnterprise());
    }

    @SuppressLint("RestrictedApi") @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentLayout() != 0) {
            final Context contextThemeWrapper = new ContextThemeWrapper(getContext(), getContext().getTheme());
            LayoutInflater themeAwareInflater = inflater.cloneInContext(contextThemeWrapper);
            View view = themeAwareInflater.inflate(fragmentLayout(), container, false);
            unbinder = ButterKnife.bind(this, view);
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void showProgress(@StringRes int resId) {
        callback.showProgress(resId);
    }

    @Override public void showBlockingProgress(int resId) {
        callback.showBlockingProgress(resId);
    }

    @Override public void hideProgress() {
        callback.hideProgress();
    }

    @Override public void showMessage(@StringRes int titleRes, @StringRes int msgRes) {
        callback.showMessage(titleRes, msgRes);
    }

    @Override public void showMessage(@NonNull String titleRes, @NonNull String msgRes) {
        callback.showMessage(titleRes, msgRes);
    }

    @Override public void showErrorMessage(@NonNull String msgRes) {
        callback.showErrorMessage(msgRes);
    }

    @Override public boolean isLoggedIn() {
        return callback.isLoggedIn();
    }

    @Override public void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle) {}

    @Override public void onDialogDismissed() {

    }

    @Override public void onRequireLogin() {
        callback.onRequireLogin();
    }

    @Override public void onLogoutPressed() {
        callback.onLogoutPressed();
    }

    @Override public void onThemeChanged() {
        callback.onThemeChanged();
    }

    @Override public void onOpenSettings() {
        callback.onOpenSettings();
    }

    @Override public void onOpenFastGitSettings() {
        callback.onOpenFastGitSettings();
    }

    @Override public void onScrollTop(int index) {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) unbinder.unbind();
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext(), getTheme());
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnShowListener(dialogInterface -> {
            if (ViewHelper.isTablet(getActivity())) {
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setLayout(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }
        });
        return dialog;
    }

    @Override public boolean isEnterprise() {
        return callback != null && callback.isEnterprise();
    }

    @Override public void onOpenUrlInBrowser() {
        callback.onOpenUrlInBrowser();
    }

}
