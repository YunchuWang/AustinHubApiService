package com.austinhub.apiservice.model.dto;

import java.sql.Timestamp;

public interface MyAdsDTO {

    long getId();

    String getResourceId();

    String getName();

    String getAddress();

    String getPhone();

    String getEmail();

    String getDescription();

    String getCategoryName();

    String getWebLink();

    String getImageLink();

    Timestamp getExpirationTime();
}

