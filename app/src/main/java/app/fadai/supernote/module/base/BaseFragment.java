package app.fadai.supernote.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

/**
 * Created by long on 2016/5/31.
 * 碎片基类
 */
public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {


    protected T mPresenter;

    protected Context mContext;
    //缓存Fragment view
    private View mRootView;
    private boolean mIsMulti = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mPresenter = initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(), container,false);
            ButterKnife.bind(this, mRootView);
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
            updateViews(false);
        }
    }

    /**
     * 绑定布局文件
     *
     * @return 布局id
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * 初始化
     *
     * @describe
     */
    protected abstract T initPresenter();

    /**
     * 初始化视图控件
     *
     * @describe
     */
    protected abstract void initViews();

    /**
     * 更新视图控件
     *
     * @describe
     */
    protected abstract void updateViews(boolean isRefresh);









}
