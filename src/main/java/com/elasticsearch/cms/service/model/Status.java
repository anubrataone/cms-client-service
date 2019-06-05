package com.elasticsearch.cms.service.model;

/**
 * The status of a request to be returned to the client.
 * Taken from the example documentation for MonitoredStatusCode.
 *
 * @author nick
 */
public class Status {

    private int value;

    private Status(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }
}
