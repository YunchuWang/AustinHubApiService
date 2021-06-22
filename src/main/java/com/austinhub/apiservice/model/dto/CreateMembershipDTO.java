package com.austinhub.apiservice.model.dto;

import com.austinhub.apiservice.model.enums.MembershipType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMembershipDTO extends OrderItemDTO {
    private boolean autoSubscribed;
    private MembershipType membershipType;
    private List<OrderItemDTO> resourceItems;
}
