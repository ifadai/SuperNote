package app.fadai.supernote.module.lock.modification;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.Utils;

import java.util.List;

import app.fadai.supernote.constants.CacheManager;
import app.fadai.supernote.module.base.BaseActivity;
import app.fadai.supernote.utils.MD5Util;
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

public class LockModificationActivity extends BaseActivity<ILockModificationView, LockModificationPresenter>
        implements ILockModificationView, LockView.OnDrawFinishedListener, View.OnClickListener {

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

    private String mTitle, mPassword, mLastPassword;
    private int mDrawTimes = 0; // 绘制次数

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_lock;
    }

    @Override
    protected LockModificationPresenter initPresenter() {
        mPresenter = new LockModificationPresenter();
        mPresenter.attch(this);
        return mPresenter;
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
        setTitle(mTitle);
        mTvTip.setText("请设置隐私密码，至少连接四个点");
        mLlRoot.setVisibility(View.GONE);

        mLickView.setOnDrawFinishedListener(this);
        mBtnOk.setOnClickListener(this);
        mBtnReDraw.setOnClickListener(this);
    }

    private void initData() {
        if (getIntent().getStringExtra("title") != null) {
            mTitle = getIntent().getStringExtra("title");
        } else {
            mTitle = "设置密码";
        }
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public boolean onDrawFinished(List<Integer> passPositions) {
        if (mDrawTimes == 0)
            return firstDrawPass(passPositions);
        else
            return secondDrawPass(passPositions);
    }

    private boolean firstDrawPass(List<Integer> passPositions) {
        if (passPositions.size() < 4) {
            mTvTip.setText("至少连接四个点，请重试");
            return false;
        } else {
            mLastPassword = getStringForList(passPositions);
            mTvTip.setText("已记录图案");
            android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLickView.resetPoints();
                    mDrawTimes++;
                    mTvTip.setText("再次绘制图案进行确认");
                    mLlRoot.setVisibility(View.VISIBLE);
                    mBtnOk.setClickable(false);
                    mBtnOk.setTextColor(Utils.getContext().getResources().getColor(R.color.colorBlackAlpha26));
                }
            }, 1 * 1000);
            return true;
        }
    }

    private boolean secondDrawPass(List<Integer> passPositions) {
        if (mLastPassword.equals(getStringForList(passPositions))) {
            mTvTip.setText("请确认您的密码图案");
            mBtnOk.setClickable(true);
            mBtnOk.setTextColor(Utils.getContext().getResources().getColor(R.color.colorBlackAlpha87));
            mLickView.setClickable(false);
            mPassword = MD5Util.getMd5Value(mLastPassword);
            return true;
        } else {
            mTvTip.setText("两次密码不一样，请重试");
            mBtnOk.setClickable(false);
            return false;
        }
    }

    private String getStringForList(List<Integer> passPositions) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : passPositions) {
            sb.append(i.intValue());
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lock_ok:
                savePasswordAndResult();
                break;
            case R.id.btn_lock_redraw:
                reDrawLock();
                break;
        }
    }

    private void savePasswordAndResult() {
        CacheManager.setAndSaveIsLocked(true);
        CacheManager.setAndSaveLockPassword(mPassword);
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private void reDrawLock() {
        mDrawTimes = 0;
        mLlRoot.setVisibility(View.GONE);
        mTvTip.setText("请设置隐私密码，至少连接四个点");
        mLickView.setClickable(true);
        mLickView.resetPoints();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
