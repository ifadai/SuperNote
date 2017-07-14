package app.fadai.supernote.module.setting.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.AppUtils;

import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.module.base.BaseActivity;
import app.fadai.supernote.module.base.BasePresenter;
import app.fadai.supernote.utils.AliPayUtils;
import butterknife.BindView;

/**
 * Created by miaoyongyong on 2017/2/19.
 */

public class AppAboutActivity extends BaseActivity {

    @BindView(R.id.tv_about_versions)
    TextView tvVersions;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle("关于");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.frame_about_content, new AboutPreferenceFragment()).commit();
        initVersions();
    }

    private void initVersions() {
        String versions = AppUtils.getAppVersionName();
        tvVersions.setText("V " + versions);
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public static class AboutPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private PreferenceScreen mToAlipay;
        private PreferenceScreen mCheckVersion;
        private PreferenceScreen mToMarket;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);
            initView();
        }

        private void initView() {
            mCheckVersion = (PreferenceScreen) findPreference("check_version");
            mToMarket = (PreferenceScreen) findPreference("to_market");

            mCheckVersion.setOnPreferenceClickListener(this);
            mToMarket.setOnPreferenceClickListener(this);
        }


        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals(getString(R.string.about_key_to_alipay))) {
                toAlipay();
            } else if (preference.getKey().equals(getString(R.string.about_key_to_market))) {
                toMarket();
            } else if (preference.getKey().equals(getString(R.string.about_key_check_version))) {
                checkVersion();
            }
            return true;
        }

        private void toAlipay() {
            AliPayUtils.openAliPay2Pay(getActivity(), Constans.myAlipayQRContent);
        }

        private void toMarket() {
            String uri = "market://details?id=" + getActivity().getPackageName();
            openUri(getActivity(), uri);
        }

        private void checkVersion() {
            toMarket();
        }

        /**
         * 发送一个intent
         *
         * @param context
         * @param s
         */
        private static void openUri(Context context, String s) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
            context.startActivity(intent);
        }
    }

}
