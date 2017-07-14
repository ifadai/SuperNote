package app.fadai.supernote.module.base;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/01
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public abstract class BasePresenter<T> {

    public T mView;

    /**
     * 绑定View 初始化时调用
     *
     * @param mView
     * @describe
     */
    public void attch(T mView) {
        this.mView = mView;
    }

    /**
     * 分离view，View销毁时调用
     *
     * @describe
     */
    public void detach() {
        mView = null;
    }


}
