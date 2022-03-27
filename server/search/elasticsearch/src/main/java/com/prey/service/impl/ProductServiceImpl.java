package com.prey.service.impl;

import com.prey.pojo.JSONResult;
import com.prey.pojo.Product;
import com.prey.pojo.bo.ProductBO;
import com.prey.service.ProductService;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Override
    public JSONResult create() {
        Document b = esTemplate.indexOps(Product.class).createMapping(Product.class);
        boolean putMapping = esTemplate.indexOps(Product.class).putMapping(b);
        return JSONResult.ok(putMapping);
    }

    @Override
    public JSONResult add(ProductBO product) {
        Product p = new Product(product.getId(), product.getName(), product.getCategory(),product.getTag(), product.getPrice());
        Product save = esTemplate.save(p);
        return JSONResult.ok(save);
    }

    @Override
    public JSONResult select(String keyword) {
        Pageable pageable = PageRequest.of(0, 10);

        SortBuilder sortBuilder = new FieldSortBuilder("price")
                .order(SortOrder.ASC);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("name.ik_pinyin",keyword).boost(2))
                .should(QueryBuilders.matchQuery("name",keyword).boost(2))
                //.should(QueryBuilders.matchQuery("category.ik",keyword).boost(1))
                //.should(QueryBuilders.matchQuery("tag.ik",keyword).boost(1))
             ;
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .withSort(sortBuilder)
                .build();
        SearchHits<Product> searchHits = esTemplate.search(query, Product.class);
        List<SearchHit<Product>> data = searchHits.getSearchHits();
        return JSONResult.ok(data);
    }

    @Override
    public JSONResult test() {
        Pageable pageable = PageRequest.of(0, 30);

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .build();
        SearchHits<Product> searchHits = esTemplate.search(query, Product.class);
        List<SearchHit<Product>> data = searchHits.getSearchHits();
        return JSONResult.ok(data);
    }
}
