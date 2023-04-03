package com.hgq.util;

import com.hgq.entity.CodeMsgEnum;
import com.hgq.entity.ResponseEntity;

public class ResponseUtils {
    /**
     * 请求错误相应
     *
     * @return 错误实体信息
     */
    public static void errorResponse(ResponseEntity responseEntity, CodeMsgEnum codeMsgEnum) {
        responseEntity.setResultCode(CodeMsgEnum.FAIL.getCode());
        responseEntity.setResultDesc(codeMsgEnum.getMsg());
    }

    /**
     * 设置正常的response
     *
     * @param response
     * @param codeMsgEnum
     */
    @SuppressWarnings("rawtypes")
    public static void success(ResponseEntity response, CodeMsgEnum codeMsgEnum) {
        response.setResultCode(CodeMsgEnum.SUCCESS.getCode());
        response.setResultCode(codeMsgEnum.getMsg());
    }
}
