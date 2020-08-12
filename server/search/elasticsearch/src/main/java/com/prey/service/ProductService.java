package com.prey.service;


import com.prey.pojo.JSONResult;
import com.prey.pojo.Product;

/**
 * @description:
 * @author: prey
 * @create: 2020/3/30  11:46
 **/
public interface ProductService {

    JSONResult create();

    JSONResult add(Product product);

    JSONResult select(String keyword);


}
