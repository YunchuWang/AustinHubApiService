package com.austinhub.apiservice.model.dto;

import java.sql.Timestamp;

public interface MyBoothDTO {

    long getId();

    String getResourceId();

    String getName();

    String getAddress();

    String getPhone();

    String getEmail();

    String getDescription();

    String getCategoryName();

    String getWebLink();

    Timestamp getExpirationTime();
}

