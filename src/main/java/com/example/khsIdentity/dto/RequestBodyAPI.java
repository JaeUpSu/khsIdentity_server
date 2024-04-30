package com.example.khsIdentity.dto;

public class RequestBodyAPI {
    private Boolean isPrivate;

    public RequestBodyAPI(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
