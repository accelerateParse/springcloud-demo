package com.prey.controller;

import com.prey.pojo.JSONResult;
import com.prey.pojo.Product;
import com.prey.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: prey
 * @create: 2020/3/30  11:39
 **/
@RestController
@RequestMapping("productInfo")
public class ProductController {
/*

    step 1 先创建一个index 比如叫product

    step 2 给这个index设置组合分词器
    请求体  注意是put请求不是post get 使用postman进行 格式 ：ip+:9200+index名
    put http://192.168.175.131:9200/product_info
    {
        "index": {
        "number_of_replicas" : "0",
                "number_of_shards" : "1",
                "analysis": {
            "analyzer": {
                "ik_pinyin_analyzer": {
                    "tokenizer": "my_ik_pinyin",
                            "filter": "pinyin_first_letter_and_full_pinyin_filter"
                },
                "pinyin_analyzer": {
                    "tokenizer": "my_pinyin"
                }
            },
            "tokenizer": {
                "my_ik_pinyin": {
                    "type": "ik_max_word"
                },
                "my_pinyin": {
                    "type": "pinyin",
                            "keep_first_letter": true,
                            "keep_separate_first_letter": false,
                            "keep_full_pinyin": false,
                            "keep_joined_full_pinyin": true,
                            "keep_none_chinese": true,
                            "none_chinese_pinyin_tokenize": false,
                            "keep_none_chinese_in_joined_full_pinyin": true,
                            "keep_original": false,
                            "limit_first_letter_length": 16,
                            "lowercase": true,
                            "trim_whitespace": true,
                            "remove_duplicated_term": true
                }
            },
            "filter": {
                "pinyin_first_letter_and_full_pinyin_filter": {
                    "type": "pinyin",
                            "keep_first_letter": true,
                            "keep_separate_first_letter": false,
                            "keep_full_pinyin": false,
                            "keep_joined_full_pinyin": true,
                            "keep_none_chinese": true,
                            "none_chinese_pinyin_tokenize": false,
                            "keep_none_chinese_in_joined_full_pinyin": true,
                            "keep_original": false,
                            "limit_first_letter_length": 16,
                            "lowercase": true,
                            "trim_whitespace": true,
                            "remove_duplicated_term": true
                }
            }
        }
    }
    }
*/

    @Autowired
    private ProductService productInfoService;

    /**
      step 3 对下面url发起请求，下面的create方法利用estemplate设置index里的 文档 （es的文档对应数据库的字段）,
     *该方法对应实体类设置了对应需要分词字段的采用分词器方式
     */
    @GetMapping("/create")
    public JSONResult create(){
       return productInfoService.create();
    }

    /* step 4 可以使用swagger-ui测试 调用添加和查询方法
    其他可以看下 https://blog.csdn.net/u013041642/article/details/94416631
    */
    @PostMapping("/add")
    public JSONResult add(@RequestBody Product product){
        return productInfoService.add(product);
    }


    @GetMapping("/select")
    public JSONResult select(@RequestParam String keyword){
        return productInfoService.select(keyword);
    }
}
