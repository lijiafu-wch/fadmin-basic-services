package f.s.fadmincommon.exception.auth;
import f.s.fadmincommon.constant.CommonConstants;
import f.s.fadmincommon.exception.BaseException;

/**
 * Created by ace on 2017/9/8.
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
