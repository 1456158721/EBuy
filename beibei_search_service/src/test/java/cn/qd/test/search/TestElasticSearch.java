package cn.qd.test.search;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class TestElasticSearch {

    private RestHighLevelClient restHighLevelClient;

    @Before
    public void before(){
        HttpHost httpHost = new HttpHost("192.168.0.221", 9200, "http");
        restHighLevelClient = new  RestHighLevelClient(RestClient.builder(httpHost));
    }

    @Test
    public void test2() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.types("sku");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //分页
        Integer pageIndex = 1;
        Integer pageSize  = 5;

        searchSourceBuilder.from((pageIndex - 1) * pageSize);
        searchSourceBuilder.size(pageSize);

        //设置排序
        searchSourceBuilder.sort("price", SortOrder.valueOf("DESC"));

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name").preTags("<font style='color:red'>").postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);


        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit searchHit : hits){
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

            //获取高亮结果
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("name");
            Text[] fragments = highlightField.getFragments();
            if(fragments[0] != null){
                sourceAsMap.put("name", fragments[0].toString());
            }
        }

        long total = searchHits.getTotalHits();
        //计算总页数
        long totalPage = total / pageSize == 0 ? total / pageSize : total / pageSize + 1;

        restHighLevelClient.close();
    }

    @Test
    public void test1() throws IOException {

        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.types("sku");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();



        //聚合 根据分类
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("sku_category").field("categoryName");
        searchSourceBuilder.aggregation(termsAggregationBuilder);

        searchRequest.source(searchSourceBuilder);


        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();

        Map<String, Aggregation> asMap = aggregations.getAsMap();
        Terms terms = (Terms) asMap.get("sku_category");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for(Terms.Bucket bucket : buckets){
            String key = bucket.getKeyAsString();
            long count = bucket.getDocCount();
            System.out.println(key + "\t" + count);
        }

        restHighLevelClient.close();
    }







}
