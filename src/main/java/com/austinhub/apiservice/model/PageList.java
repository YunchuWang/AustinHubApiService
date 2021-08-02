package com.austinhub.apiservice.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageList<T> implements Serializable {
    int page;
    int pageSize;
    int totalCount;
    List<T> entries;
}
