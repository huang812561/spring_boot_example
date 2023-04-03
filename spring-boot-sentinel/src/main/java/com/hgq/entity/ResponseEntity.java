package com.hgq.entity;

import lombok.Data;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-08-01 19:33
 * @since 1.0
 **/
@Data
public class ResponseEntity {

    private String resultCode = "0";
    private String resultDesc = "";
    private Object data;

    public ResponseEntity() {
    }

    public ResponseEntity(String resultCode, String resultDesc, Object data) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.data = data;
    }

    public ResponseEntity(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

}
