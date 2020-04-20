package f.s.fadmincommon.msg.auth;

import f.s.fadmincommon.constant.RestCodeConstants;
import f.s.fadmincommon.msg.BaseResponse;

/**
 * Created by ace on 2017/8/23.
 */
public class TokenErrorResponse extends BaseResponse {
    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
