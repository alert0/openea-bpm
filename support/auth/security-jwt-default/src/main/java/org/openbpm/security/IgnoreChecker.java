package org.openbpm.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.hutool.core.collection.CollectionUtil;

public class IgnoreChecker {
    private List<Pattern> ignores = new ArrayList<Pattern>();

    public void setIgnores(List<String> urls) {
        if (CollectionUtil.isEmpty(urls)) return;
        for (String url : urls) {
            Pattern regex = Pattern.compile(url, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE |
                    Pattern.DOTALL | Pattern.MULTILINE);
            ignores.add(regex);
        }
    }


    /**
     * 判断当前URL是否在忽略的地址中。
     *
     * @param requestUrl
     * @return
     */
    public boolean isIgnores(String requestUrl) {
        // 会再跳转 index.html 所以直接忽略
        if ("/".equals(requestUrl)) return true;

        for (Pattern pattern : ignores) {
            Matcher regexMatcher = pattern.matcher(requestUrl);
            if (regexMatcher.find()) {
                return true;
            }
        }
        return false;
    }

}
