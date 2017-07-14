package app.fadai.supernote.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/07
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class ProgressDialogUtils {

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public ProgressDialogUtils(Context context){
        this.mContext=context;
    }

    public void show(String message){
        if(mProgressDialog==null)
            mProgressDialog=new ProgressDialog(mContext);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hide(){
        if(mProgressDialog!=null)
            mProgressDialog.cancel();
    }
}
