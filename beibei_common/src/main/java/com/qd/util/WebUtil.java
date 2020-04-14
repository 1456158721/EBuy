package com.qd.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebUtil {

    //GET字符集设置处理
    public static Map<String, String> convertCharsetToUTF8(Map<String, String> searchMap) throws Exception {
        Iterator<Map.Entry<String, String>> entries = searchMap.entrySet().iterator();
        Map map = new HashMap();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            map.put(new String(entry.getKey().getBytes("ISO8859-1"), "UTF-8"), new String(entry.getValue().getBytes("ISO8859-1"), "UTF-8"));
        }
        return map;
    }


}

