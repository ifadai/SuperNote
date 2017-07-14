package app.fadai.supernote.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.fadai.supernote.R;

import app.fadai.supernote.constants.Constans;

/**
 * Created by miaoyongyong on 2016/12/26.
 */

public class ThemeChoicePreference extends DialogPreference {

    private Context mContext;


    private int mCurrentValue; //    现在的value
    private int mNewValue;//    新的value

    private RadioGroup mGroup1;
    private RadioGroup mGroup2;
    private RadioGroup mGroup3;
    private RadioGroup mGroup4;

    //    style文件中的所有theme
    private int[]  mThemes=new int[]{R.style.NoActionBar_Theme1,R.style.NoActionBar_Theme2,R.style.NoActionBar_Theme3
            ,R.style.NoActionBar_Theme4,R.style.NoActionBar_Theme5,R.style.NoActionBar_Theme6
            ,R.style.NoActionBar_Theme7,R.style.NoActionBar_Theme8,R.style.NoActionBar_Theme9
            ,R.style.NoActionBar_Theme10,R.style.NoActionBar_Theme11,R.style.NoActionBar_Theme12
            ,R.style.NoActionBar_Theme13,R.style.NoActionBar_Theme14,R.style.NoActionBar_Theme15
            ,R.style.NoActionBar_Theme16,R.style.NoActionBar_Theme17,R.style.NoActionBar_Theme18
            ,R.style.NoActionBar_Theme19};

    //    所有的radioButton
    private int[] mRdoBtns=new int[]{R.id.rdobtn_1,R.id.rdobtn_2,R.id.rdobtn_3,R.id.rdobtn_4,R.id.rdobtn_5
            ,R.id.rdobtn_6,R.id.rdobtn_7,R.id.rdobtn_8,R.id.rdobtn_9,R.id.rdobtn_10
            ,R.id.rdobtn_11,R.id.rdobtn_12,R.id.rdobtn_13,R.id.rdobtn_14,R.id.rdobtn_15
            ,R.id.rdobtn_16,R.id.rdobtn_17,R.id.rdobtn_18,R.id.rdobtn_19};


    public ThemeChoicePreference(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        View view= LayoutInflater.from(mContext).inflate(R.layout.dialog_theme_choice,null);

        initRadioGroup(view);

        builder.setView(view)
                .setTitle("主题更换")
                .setNegativeButton("", null)
                .setPositiveButton("", null);

        initSelectedRadioBtn(view);
    }

    private void initSelectedRadioBtn(View view){
        for(int i=0;i<mThemes.length;i++){
            if(Constans.theme==mThemes[i]){
                RadioButton radioButton=(RadioButton)view.findViewById(mRdoBtns[i]);
                radioButton.setChecked(true);
                break;
            }
        }
    }

    private void initRadioGroup(View view){
        mGroup1=(RadioGroup)view.findViewById(R.id.group1);
        mGroup2=(RadioGroup)view.findViewById(R.id.group2);
        mGroup3=(RadioGroup)view.findViewById(R.id.group3);
        mGroup4=(RadioGroup)view.findViewById(R.id.group4);

        mGroup1.setOnCheckedChangeListener(mCheckedChangeListener);
        mGroup2.setOnCheckedChangeListener(mCheckedChangeListener);
        mGroup3.setOnCheckedChangeListener(mCheckedChangeListener);
        mGroup4.setOnCheckedChangeListener(mCheckedChangeListener);
    }

    RadioGroup.OnCheckedChangeListener mCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            checkTheme(group,checkedId);
        }
    };

    private void checkTheme(RadioGroup group, int checkedId){
        for(int i=0;i<mRdoBtns.length;i++){
            if(checkedId==mRdoBtns[i]){
                saveTheme(i);
                group.check(mRdoBtns[i]);
                if(getDialog()!=null){
                    getDialog().cancel();
                }
                break;
            }
        }
    }

    private void saveTheme(int position){
        mNewValue=mThemes[position];
        persistInt(mNewValue);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        View mContentView = LayoutInflater.from(getContext()).inflate(
                R.layout.preference_theme_change, parent, false);
        return mContentView;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            mCurrentValue = this.getPersistedInt(Constans.theme);
            mNewValue= mCurrentValue;
        } else {
            // Set default state from the XML attribute
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
            mNewValue=mCurrentValue;
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index,Constans.theme);
    }

}
