package app.fadai.supernote.module.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.app.fadai.supernote.R;

import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.utils.ThemeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/01
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    public T mPresenter;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    public Context mContext;

    protected int theme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        mContext = this;
        mPresenter = initPresenter();
        initBeforeSetContentView();
        setContentView(attachLayoutRes());
        ButterKnife.bind(this);
        setStatusBarBeforeApi19();
        initToolbar();
        initViews();
        updateViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (theme != Constans.theme)
            recreate();
    }

    private void setTheme() {
        theme = Constans.theme;
        setTheme(theme);

    }


    private void setStatusBarBeforeApi19() {
        if (Build.VERSION.SDK_INT < 21 && Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.app_bar);
            // appBarLayout 用于有可折叠标题栏的界面
            if (appBarLayout!=null) {
                appBarLayout.setPadding(0, ThemeUtils.getStatusBarHeight(), 0, 0);
            } else if (mToolbar != null) {
                mToolbar.setPadding(0, ThemeUtils.getStatusBarHeight(), 0, 0);
            }
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
     * 在setContentView之前调用，可不重写，需要时候再重写
     *
     * @describe
     */
    protected void initBeforeSetContentView() {
    }

    ;

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
    protected abstract void updateViews();

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();

    }

    /**
     * 初始化Toolbar
     *
     * @describe
     */
    protected void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

//    /**
//     * 设置Toolbar title
//     *
//     * @describe
//     */
//    protected void setTitle(String title) {
//        if (mToolbar != null) {
//            mToolbar.setTitle(title);
//        }
//    }
}
