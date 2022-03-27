package com.prey.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

/**
 * @author prey
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBO {

    private Long id;

    private String name;

    private String category;

    private String tag;

    private Double price;


}
