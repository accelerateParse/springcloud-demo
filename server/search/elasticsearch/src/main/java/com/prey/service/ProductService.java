package com.prey.service;


import com.prey.pojo.JSONResult;
import com.prey.pojo.bo.ProductBO;

/**
 * @description:
 * @author: prey
 * @create: 2020/3/30  11:46
 **/
public interface ProductService {

    JSONResult create();

    JSONResult add(ProductBO product);

    JSONResult select(String keyword);

    JSONResult test();


}
