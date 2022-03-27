package com.prey.service.impl;


import com.prey.pojo.JSONResult;
import com.prey.pojo.PagedResult;
import com.prey.pojo.Product;
import com.prey.service.ProductService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 我把以前使用6.8.4es代码copy过来有少许改动，没测试很可能有问题
 * @author: prey
 * @create: 2020/3/30  11:47
 **/

public class ProductServiceImplWithES6 {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    
    public JSONResult create() {
        esTemplate.putMapping(Product.class);
        return JSONResult.ok();
    }

    
    public JSONResult add(Product req) {

            IndexQuery indexQuery = new IndexQueryBuilder().withObject(req).build();
            // IndexCoordinates.of("product") 指定对应的index,index相当于数据库的表, 6.8.4 elasticsearchTemplate不需要填该参数
            esTemplate.index(indexQuery,IndexCoordinates.of("product"));
            return JSONResult.ok();
    }

    
    public JSONResult select(String keyword) {
        Pageable pageable = PageRequest.of(0, 20);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("productName.ik_pinyin",keyword).boost(1))
                .should(QueryBuilders.matchQuery("category.ik_pinyin",keyword).boost(2));

        Query  query= new NativeSearchQueryBuilder()
                        .withQuery(queryBuilder)
                        .withPageable(pageable)
                        .withSort(SortBuilders.scoreSort())
                        .build();

        AggregatedPage<Product> pagedProduct = esTemplate.queryForPage(query, Product.class,IndexCoordinates.of("product"));

        PagedResult result = new PagedResult();
        result.setList(pagedProduct.getContent());
        result.setPage(pagedProduct.getNumber());
        result.setRecords(pagedProduct.getTotalElements());
        result.setTotalPages(pagedProduct.getTotalPages());
        return JSONResult.ok(result);
    }

    public JSONResult update(){
        Map<String , Object> sourceMap = new HashMap<>();


        UpdateQuery updateQuery =  UpdateQuery.builder("100")
                .withParams(sourceMap)
                .build();

        /* 6.8.4 elasticsearchTemplate 可以使用下面代码
        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withClass(ProductItem.class)
                .withId(req.getId().toString())
                .withIndexRequest(indexRequest)
                .build();*/


        esTemplate.update(updateQuery,IndexCoordinates.of("product"));
        return JSONResult.ok();
    }
}
