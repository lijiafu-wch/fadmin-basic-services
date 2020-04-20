package f.s.fadmincommon.msg.auth;

import f.s.fadmincommon.constant.RestCodeConstants;
import f.s.fadmincommon.msg.BaseResponse;

/**
 * Created by ace on 2017/8/25.
 */
public class TokenForbiddenResponse  extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
