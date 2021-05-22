package com.austinhub.apiservice.model.dto;

import com.austinhub.apiservice.model.po.Ads;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdsRequest {
    @NotNull
    private List<Ads> ads;
}