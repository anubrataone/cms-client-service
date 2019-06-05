package com.elasticsearch.cms.rest.response;
/*
{
    "header": {
        "source": "<Source Micro-service Name>",
        "code": "0",
        "message": "Success",
        "system_time": 1558041284123
    },
    "data": {
        <RESPONSEOBJECT>
    }
}
* */
public class ResponseData<T> {
    private ResponseHeader header;
    private T data;

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
