package app.fadai.supernote.module.lock.verification;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/22
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface ILockView {

    /**
     *  错误
     *  @describe
     */
    void onError();

    /**
     *  正确
     *  @describe
     */
    void onSuccess();
}
