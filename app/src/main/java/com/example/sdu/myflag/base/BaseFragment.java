package com.example.sdu.myflag.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    public View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        init();
        afterCreate(savedInstanceState);
        return mRootView;
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    protected abstract void init();

    public void startNewActivity(Class<?> cl){
        startActivity(new Intent(this.getActivity(), cl));
    }
}
