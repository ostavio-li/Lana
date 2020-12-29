package com.edwardlee.library.util;


import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Reg Util
 * @author Edwin Li
 */

@Component
public class RegOperation {

    private static Matcher matcher = null;


    public static final int FIRST_PARAM = 1;
    public static final int SECOND_PARAM = 2;
    public static final int THIRD_PARAM = 3;
    public static final int FORTH_PARAM = 4;

    public static boolean match(String reg, String target) {
        Pattern pattern = Pattern.compile(reg);
        matcher = pattern.matcher(target);
        return matcher.matches();
    }

    /**
     * 获取匹配的 参数
     * @param num 正则表达式 参数序号，从 1 开始
     * @return 对应参数值
     */
    public static String getParam(int num) {
        return matcher.group(num);
    }

}
