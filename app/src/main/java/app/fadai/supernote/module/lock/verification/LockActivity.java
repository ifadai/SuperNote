package app.fadai.supernote.module.lock.verification;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.SizeUtils;

import java.util.List;

import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.module.base.BaseActivity;
import app.fadai.supernote.module.setting.lock.LockSettingActivity;
import app.fadai.supernote.widget.LockView;
import butterknife.BindView;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/22
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class LockActivity extends BaseActivity<ILockView, LockPresenter> implements ILockView, LockView.OnDrawFinishedListener {

    @BindView(R.id.lockview_lock)
    LockView mLickView;

    @BindView(R.id.tv_lock_tip)
    TextView mTvTip;

    @BindView(R.id.ll_lock)
    LinearLayout mLlRoot;

    @BindView(R.id.btn_lock_ok)
    Button mBtnOk;

    @BindView(R.id.btn_lock_redraw)
    Button mBtnReDraw;

    private String mTitle;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_lock;
    }

    @Override
    protected LockPresenter initPresenter() {
        mPresenter = new LockPresenter();
        mPresenter.attch(this);

        return mPresenter;
    }

    @Override
    protected void initBeforeSetContentView() {
        initPushInAnim();
    }

    private void initPushInAnim() {

        Window window = getWindow();
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        TransitionInflater inflater = TransitionInflater.from(mContext);
        Transition pushDownIn = inflater.inflateTransition(R.transition.explode_in);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setEnterTransition(pushDownIn); // 第一次进入时使用
            window.setReenterTransition(pushDownIn); // 再次进入时使用
            window.setExitTransition(pushDownIn);
        }

    }

    @Override
    protected void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
        setTitle(mTitle);
        mTvTip.setText("请输入隐私密码");
        mLlRoot.setVisibility(View.GONE);

        mLickView.setOnDrawFinishedListener(this);
    }

    private void initData() {
        if (getIntent().getStringExtra("title") != null) {
            mTitle = getIntent().getStringExtra("title");
        } else {
            mTitle = "验证密码";
        }
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public void onError() {
        mTvTip.setText("请重试");
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTvTip, "translationX", -SizeUtils.dp2px(8), SizeUtils.dp2px(8), 0);
        animator.setDuration(200);
        animator.start();
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK, new Intent());
        onBackPressed();
    }

    @Override
    public boolean onDrawFinished(List<Integer> passPositions) {
        return mPresenter.verifyPassword(passPositions, Constans.lockPassword);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_edit_lock:
                toLockSetting();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void toLockSetting() {
        Intent intent = new Intent(mContext, LockSettingActivity.class);
        startActivity(intent);
    }

}
