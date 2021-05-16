package com.musala.gateways.model;

import lombok.ToString;

@ToString
public enum Status {
    ONLINE("online"), OFFLINE("offline");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status getByString(String value) {
        for (Status status : Status.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
