package f.s.fadmincommon.exception.auth;

import f.s.fadmincommon.constant.CommonConstants;
import f.s.fadmincommon.exception.BaseException;
/**
 * Created by ace on 2017/9/10.
 */
public class ClientInvalidException extends BaseException {
    public ClientInvalidException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
