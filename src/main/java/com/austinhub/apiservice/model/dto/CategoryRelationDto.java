package com.austinhub.apiservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRelationDto {
    private Integer id;
    private Integer categoryId;
    private String categoryName;
    private Integer subCategoryId;
    private String subCategoryName;
}
