package cn.youyunsample.base;

/**
 * Created by YWB on 2016/6/5.
 *
 * MVP for Base View
 */
public interface BaseView {

    /**
     * 显示进度条
     */
    void showLoadView();

    /**
     * 取消进度条
     */
    void hideLoadView();
}
