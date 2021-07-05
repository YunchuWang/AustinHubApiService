package com.austinhub.apiservice.model.dto;

import java.sql.Timestamp;

//  new table: resource and order many to many
//          new table: membership and order many to many
//          membership id, expiration ts, pricing plan, name, type(basic/advanced mem), category
//          (none)
//          resource id, expiration ts, pricing plan, name, type, category
public interface RenewableItemDTO {

    Integer getId();

    Timestamp getExpirationTime();

    String getName();

    String getType();

    String getCategory();
}