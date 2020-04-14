package cn.qd.search.service;

import cn.qd.search.dao.BrandDao;
import cn.qd.search.dao.SpecDao;
import cn.qd.service.SearchGoodsService;
import com.alibaba.dubbo.config.annotation.Service;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchGoodsService {

    @Value("${beibei.elasticsearch.index}")
    private String index;

    @Value("${beibei.elasticsearch.type}")
    private String type;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private SpecDao specDao;


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public Map<String, Object> search(Map<String, String> searchMap) {
        //返回结果
        Map<String, Object>  result = new HashMap<>();

        //封装搜索请求
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //构造bool 查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //关键字
        String  keyword = searchMap.get("keyword");
        //System.out.println("======================" + keyword);
        //构造匹配查询
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", keyword);
        boolQueryBuilder.must(matchQueryBuilder);

        //查询分类
        if(searchMap.get("category") != null){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("categoryName", searchMap.get("category"));
            boolQueryBuilder.filter(termQueryBuilder);
        }
        //查询品牌
        if(searchMap.get("brand") != null){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brandName", searchMap.get("brand"));
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //查询规格
        for(String searchKey: searchMap.keySet()){
            if(searchKey.startsWith("spec.")){
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(searchKey+".keyword", searchMap.get(searchKey));
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        //查询价格
        if(searchMap.get("price") != null){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price");
            String[] prices = searchMap.get("price").split("_");
            if(!"0".equals(prices[0])){
                rangeQueryBuilder.gte(prices[0]+"00");
            }
            if(!"*".equals(prices[1])){
                rangeQueryBuilder.lte(prices[1] +"00");
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        //当前页
        int pageIndex = Integer.parseInt(searchMap.get("pageIndex"));
        //每一页条数
        int pageSize  = 3;
        //设置分页
        searchSourceBuilder.from((pageIndex - 1) * pageSize);
        searchSourceBuilder.size(pageSize);

        //排序
        if(!"".equals(searchMap.get("sort"))){
            searchSourceBuilder.sort(searchMap.get("sort"),   SortOrder.valueOf(searchMap.get("sortOrder")));
        }

        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name"); //设置高亮 字段
        highlightBuilder.preTags("<font style='color:red'>").postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.query(boolQueryBuilder);

        //聚合 根据分类
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("sku_category").field("categoryName");
        searchSourceBuilder.aggregation(termsAggregationBuilder);

        searchRequest.source(searchSourceBuilder);

        try{
            //获取查询结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();

            //System.out.println("============================" + total);
            SearchHit[] hits = searchHits.getHits();
            List<Map> rows = new ArrayList<>();
            for(SearchHit searchHit : hits){
                Map hitMap = searchHit.getSourceAsMap();
                rows.add(hitMap);

                //取出高亮
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                HighlightField highlightField = highlightFields.get("name");
                Text[] fragments = highlightField.getFragments();
                if(fragments[0] != null){
                    hitMap.put("name", fragments[0].toString()); //用高亮以后的数据 替换 以前的
                }
            }
            result.put("rows", rows);

            //分类列表
            List<String> categoryList = new ArrayList<>();

            String categoryName = null;
            //如果 搜索条件 没有分类
            if(searchMap.get("category") == null){
                Aggregations aggregations = searchResponse.getAggregations();
                Map<String, Aggregation> asMap = aggregations.getAsMap();
                Terms terms = (Terms) asMap.get("sku_category");
                List<? extends Terms.Bucket> buckets = terms.getBuckets();
                for(Terms.Bucket bucket : buckets) {
                    String key = bucket.getKeyAsString();
                    categoryList.add(key);
                }
                if(categoryList.size() > 0){
                    categoryName = categoryList.get(0);
                }
            }else{
                categoryName = searchMap.get("category");
            }
            result.put("categoryList", categoryList);

            //查找品牌列表
            if(searchMap.get("brand") == null) {
                List<Map> brandList = brandDao.findBrandByCategory(categoryName);
                result.put("brandList", brandList);
            }

            //查找规格列表
            List<Map> specList = specDao.findSpecByCategory(categoryName);
            for(Map spec: specList){
                spec.put("options", ((String)spec.get("options")).split(","));
            }
            result.put("specList", specList);
            //System.out.println(specList);

            //得到总条数
            long total = searchHits.getTotalHits();
            //计算总页数
            long totalPage = total % pageSize == 0 ?  total / pageSize :  total / pageSize + 1;
            //System.out.println("------------------" + totalPage);
            result.put("totalPage", totalPage);


        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
