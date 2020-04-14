package cn.qd.search.controller;

import cn.qd.service.SearchGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.qd.util.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
public class SearchController {

    @Reference
    private SearchGoodsService searchGoodsService;

    @GetMapping("/search.do")
    public String search(@RequestParam Map<String, String> searchMap, Model model) throws Exception {


         //判断 当前页
        if(searchMap.get("pageIndex") == null){
            searchMap.put("pageIndex", "1");
        }
        //判断 排序
        if(searchMap.get("sort") == null){
            searchMap.put("sort", "");
        }
        if(searchMap.get("sortOrder") == null){
            searchMap.put("sortOrder", "DESC");
        }
        

        model.addAttribute("searchMap", searchMap);

        //构造URL
        StringBuilder url = new StringBuilder("search.do?");
        for(String key : searchMap.keySet()){
            url.append("&" + key).append("=").append(searchMap.get(key));
        }
        model.addAttribute("url", url.toString());


        Map<String, Object> result = searchGoodsService.search(searchMap);
        model.addAttribute("result", result);

        //总页数
        long totalPage = (Long)result.get("totalPage");
        //当前页
        long pageIndex = Long.parseLong(searchMap.get("pageIndex"));

        //开始页
        long startPage = 1;
        //结束页
        long endPage = totalPage ;

        if(totalPage > 5){
            startPage = pageIndex - 2;
            if(startPage < 1){
                startPage = 1;
            }
            endPage   = startPage + 4;
            if(endPage > totalPage){
                endPage = totalPage;
            }
        }
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);






        return "search";
    }

}
