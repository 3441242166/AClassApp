package com.example.wanhao.aclassapp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wanhao.aclassapp.util.ActivityCollector;

public abstract class BaseMvpActivity<T extends IBasePresenter> extends AppCompatActivity {

    public T presenter;

    protected abstract T setPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        presenter = setPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if(presenter!=null){
            presenter.destroy();
        }

    }
}
