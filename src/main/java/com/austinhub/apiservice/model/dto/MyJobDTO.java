package com.austinhub.apiservice.model.dto;

import java.sql.Timestamp;

public interface MyJobDTO {

    long getId();

    String getResourceId();

    String getName();

    String getAddress();

    String getPhone();

    String getSalary();

    String getContact();

    String getEmail();

    String getDescription();

    String getCategoryName();

    String getCompanyLink();

    Timestamp getExpirationTime();
}