package app.fadai.supernote.module.lock.verification;

import java.util.List;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/22
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface ILockPresenter {
    boolean verifyPassword(List<Integer> passPositions,String password);
}
