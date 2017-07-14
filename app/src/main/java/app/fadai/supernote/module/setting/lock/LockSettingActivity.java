package app.fadai.supernote.module.setting.lock;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import com.app.fadai.supernote.R;

import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.module.base.BaseActivity;
import app.fadai.supernote.module.base.BasePresenter;
import app.fadai.supernote.module.lock.modification.LockModificationActivity;
import app.fadai.supernote.module.lock.verification.LockActivity;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/29
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class LockSettingActivity extends BaseActivity {

    public static final int REQUEST_TO_LOCK = 1;

    public static final int REQUEST_TO_MODIFY_LOCK = 2;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_lock_setting;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("隐私密码");
        getFragmentManager().beginTransaction().replace(R.id.frame_lock_setting_content, new LockSettingFragment()).commit();
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TO_LOCK:
                if(resultCode==RESULT_OK){
                    Intent intent = new Intent();
                    intent.setClass(mContext, LockModificationActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }

    public static class LockSettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        PreferenceScreen mPreLock;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_lock);
            mPreLock=(PreferenceScreen)findPreference("modify_lock");
            mPreLock.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            toLockActivity();
            return true;
        }

        private void toLockActivity() {
            Intent intent = new Intent();
            if (Constans.isLocked) {
                intent.setClass(getActivity(), LockActivity.class);
                startActivityForAnim(intent,REQUEST_TO_LOCK);
            } else {
                intent.setClass(getActivity(), LockModificationActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_TO_MODIFY_LOCK);
            }
        }

        private void startActivityForAnim(Intent intent,int requestCode){
            // 5.0及以上则使用Activity动画
            if(Build.VERSION.SDK_INT>=21){
                Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
                getActivity().startActivityForResult(intent, requestCode, bundle);
            } else {
                getActivity().startActivityForResult(intent, requestCode);
            }
        }
    }
}
