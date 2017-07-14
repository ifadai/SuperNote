package app.fadai.supernote.module.setting.developer;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.app.fadai.supernote.R;

import app.fadai.supernote.module.base.BaseActivity;
import butterknife.BindView;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/12
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class DeveloperActivity extends BaseActivity<IDeveloperView,DeveloperPresenter> implements IDeveloperView, View.OnLongClickListener {

    @BindView(R.id.ll_developer_github)
    LinearLayout mLlToGitHub;

    @BindView(R.id.ll_developer_blog)
    LinearLayout mLlToBlog;

    @BindView(R.id.ll_developer_jianshu)
    LinearLayout mLlToJianShu;

    @BindView(R.id.ll_developer_email)
    LinearLayout mLlEmail;



    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_developer;
    }

    @Override
    protected DeveloperPresenter initPresenter() {
        DeveloperPresenter presenter=new DeveloperPresenter();
        presenter.attch(this);
        return presenter;
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("关于开发者");

        mLlToGitHub.setOnLongClickListener(this);
        mLlToBlog.setOnLongClickListener(this);
        mLlToJianShu.setOnLongClickListener(this);
        mLlEmail.setOnLongClickListener(this);
    }

    @Override
    protected void updateViews() {

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_developer_github:
                mPresenter.openGithub();
                break;
            case R.id.ll_developer_blog:
                mPresenter.openBlog();
                break;
            case R.id.ll_developer_jianshu:
                mPresenter.openJianShu();
                break;
            case R.id.ll_developer_email:
                mPresenter.copyEmail();
                break;
            case R.id.ll_developer_to_alipay:
                mPresenter.toAlipay();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.ll_developer_github:
                mPresenter.copyGithub();
                break;
            case R.id.ll_developer_blog:
                mPresenter.copyBlog();
                break;
            case R.id.ll_developer_jianshu:
                mPresenter.copyJianShu();
                break;
            case R.id.ll_developer_email:
                mPresenter.copyEmail();
                break;
        }
        return true;
    }

    @Override
    public Activity getActivity() {
        return this;
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
