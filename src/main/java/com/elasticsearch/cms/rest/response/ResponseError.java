package com.elasticsearch.cms.rest.response;

import java.io.Serializable;

public class ResponseError implements Serializable {
    private String error, description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
