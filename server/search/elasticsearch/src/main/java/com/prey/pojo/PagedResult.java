package com.prey.pojo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: prey
 * @create: 2020/4/2  13:11
 **/

@Data
public class PagedResult {

    private Integer page;

    private Integer totalPages;

    private Long records;

    private List<?> list;
}
