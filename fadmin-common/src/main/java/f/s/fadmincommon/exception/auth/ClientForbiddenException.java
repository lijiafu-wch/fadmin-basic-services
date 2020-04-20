package f.s.fadmincommon.exception.auth;


import f.s.fadmincommon.constant.CommonConstants;
import f.s.fadmincommon.exception.BaseException;

/**
 * Created by ace on 2017/9/12.
 */
public class ClientForbiddenException extends BaseException {
    public ClientForbiddenException(String message) {
        super(message, CommonConstants.EX_CLIENT_FORBIDDEN_CODE);
    }

}
