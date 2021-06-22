package com.austinhub.apiservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResourceArchivedDTO {
    @JsonProperty("resourceId")
    @NonNull
    public Integer resourceId;
    
    @JsonProperty("isArchived")
    @NonNull
    public Boolean isArchived;
}
