package com.austinhub.apiservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageList<T> {
    int page;
    int pageSize;
    int totalCount;
    List<T> entries;
}
