package app.fadai.supernote.model;

import java.util.List;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/05
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public abstract class LoadDataCallBack<T> {
    protected abstract void onSuccedd(List<T> list);

}
