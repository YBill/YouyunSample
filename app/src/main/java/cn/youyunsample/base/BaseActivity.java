package cn.youyunsample.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by YWB on 2016/6/5.
 *
 * MVP for Base Activity
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends Activity {

    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        presenter.attach((V)this);
    }

    protected abstract T initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dettach();
    }
}
