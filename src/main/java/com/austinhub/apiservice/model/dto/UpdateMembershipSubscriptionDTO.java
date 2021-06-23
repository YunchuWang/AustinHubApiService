package com.austinhub.apiservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMembershipSubscriptionDTO {
    @JsonProperty("autoSubscribed")
    @NonNull
    public Boolean autoSubscribed;
}
