package app.fadai.supernote.module.setting.feedback;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.ToastUtils;

import app.fadai.supernote.module.base.BaseActivity;
import butterknife.BindView;


/**
 * Created by miaoyongyong on 2017/2/20.
 */

public class FeedbackActivity extends BaseActivity<IFeedbackView,FeedbackPresenter>
        implements IFeedbackView, View.OnClickListener {

    @BindView(R.id.edit_feedback_content)
    EditText mEtContent;

    @BindView(R.id.edit_feedback_contact)
    EditText mEtContact;

    @BindView(R.id.tv_feedback_tip)
    TextView mTvTip;

    private ProgressDialog mProgressDialog;

    @Override
    protected void initBeforeSetContentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_feedback;
    }

    @Override
    protected FeedbackPresenter initPresenter() {
        FeedbackPresenter presenter=new FeedbackPresenter();
        presenter.attch(this);
        return presenter;
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("反馈");

        mTvTip.setText(new SpanUtils().append("你也可直接发邮件至")
                .append(mContext.getResources().getString(R.string.my_email))
                .setBold()
                .append("（点击复制）")
                .create());
        mTvTip.setOnClickListener(this);
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_feedback_commit:
                submitFeedback();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitFeedback(){
        if(TextUtils.isEmpty(mEtContent.getText())){
            ToastUtils.showShort("请至少填写反馈内容");
        }else {
            sendFeedback();
        }
    }

    private void sendFeedback(){
        String content=mEtContent.getText().toString();
        String contact=mEtContact.getText().toString();
        mPresenter.sendFeedback(content,contact);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_feedback_tip:
                copyEmail();
                break;
        }
    }

    private void copyEmail(){
        ClipboardUtils.copyText(mContext.getResources().getString(R.string.my_email));
        ToastUtils.showShort("已复制");
    }

    @Override
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage("提交中...");
        mProgressDialog.show();
    }

    @Override
    public void cancleLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }
}
