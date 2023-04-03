package com.hgq.util;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.hgq.entity.CodeMsgEnum;
import com.hgq.entity.RequestData;
import com.hgq.entity.ResponseEntity;

public class SentinelExceptionUtil {

    /**
     * api-返回对象
     *
     * @Param e:
     * @Return {@link ResponseEntity}
     **/
    public static ResponseEntity response(RequestData requestData, BlockException e) {
        ResponseEntity responseData = new ResponseEntity();
        if (e instanceof FlowException) {
            ResponseUtils.errorResponse(responseData, CodeMsgEnum.FLOW_RULE_EXCEPTION);
        } else if (e instanceof DegradeException) {
            ResponseUtils.errorResponse(responseData, CodeMsgEnum.DEGRDE_RULE_EXCEPTION);
        } else if (e instanceof AuthorityException) {
            ResponseUtils.errorResponse(responseData, CodeMsgEnum.AUTHORITY_RULE_EXCEPTION);
        } else if (e instanceof SystemBlockException) {
            ResponseUtils.errorResponse(responseData, CodeMsgEnum.SYSTEM_RULE_EXCEPTION);
        } else {
            ResponseUtils.errorResponse(responseData, CodeMsgEnum.SENTINEL_COMMON_EXCEPTION);
        }
        return responseData;
    }
}
