package app.fadai.supernote.module.setting.developer;

import android.content.Intent;
import android.net.Uri;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;

import app.fadai.supernote.module.base.BasePresenter;
import app.fadai.supernote.utils.AliPayUtils;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/12
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class DeveloperPresenter extends BasePresenter<IDeveloperView> implements IDeveloperPresenter{
    @Override
    public void openGithub() {
        String uri=mView.getActivity().getResources().getString(R.string.github);
        openUri(uri);
    }

    private void openUri(String uri){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(uri);
        intent.setData(content_url);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void openBlog() {
        String uri=mView.getActivity().getResources().getString(R.string.csdn);
        openUri(uri);
    }

    @Override
    public void openJianShu() {
        String uri=mView.getActivity().getResources().getString(R.string.jian_shu);
        openUri(uri);
    }

    @Override
    public void toAlipay() {
        String alipayQrCode=mView.getActivity().getResources().getString(R.string.alipay_qr_code);
        AliPayUtils.openAliPay2Pay(mView.getActivity(),alipayQrCode);
    }

    @Override
    public void copyGithub() {
        String uri=mView.getActivity().getResources().getString(R.string.github);
        ClipboardUtils.copyText(uri);
        ToastUtils.showShort("已复制");
    }

    @Override
    public void copyBlog() {
        String uri=mView.getActivity().getResources().getString(R.string.csdn);
        ClipboardUtils.copyText(uri);
        ToastUtils.showShort("已复制");
    }

    @Override
    public void copyJianShu() {
        String uri=mView.getActivity().getResources().getString(R.string.jian_shu);
        ClipboardUtils.copyText(uri);
        ToastUtils.showShort("已复制");
    }

    @Override
    public void copyEmail() {
        String uri=mView.getActivity().getResources().getString(R.string.my_email);
        ClipboardUtils.copyText(uri);
        ToastUtils.showShort("已复制");
    }
}
