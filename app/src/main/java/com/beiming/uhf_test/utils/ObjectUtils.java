package com.beiming.uhf_test.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/24 0024.
 */
public class ObjectUtils {
    private static final String TAG = ObjectUtils.class.getSimpleName();
    public static final char[] DEFAULT_SPLIT_CHARS = new char[]{' ', ',', ';'};

    public static String getString(String s) {
        if (null == s || s.trim().equals("")) {
            return "***";
        }
        return s;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return boolean
     * @dell
     * @2015年6月11日 @上午9:33:22 TODO
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.length() <= 0)
            return true;
        return false;
    }

    public static boolean isMapEmpty(Map map) {
        if (null == map || map.isEmpty()) {
            return true;
        }
        return false;
    }

//    public static boolean isNull(Date date) {
//        if (date == null) return true;
//        long time = date.getTime();
//        //如果日期接近1970则很可能是存储的问题，所以继续表示该日期为null
//        if (time < 100000) return true;
//        return false;
//    }
//
//    public static boolean isNull(Object o) {
//        if (o == null) return true;
//        if (o instanceof Date)
//            return isNull((Date) o);
//        return false;
//    }


    public static boolean isLocationNull(String o) {
        boolean flag = true;
        if (o == null || o.equals("") || o.equals("0") || o.equals("0.0")) {

        } else {
            flag = false;
        }
        return flag;
    }


    /**
     * 判断两个string 是否相同
     *
     * @param left
     * @param right
     * @return boolean
     * @dell
     * @2015年6月17日 @下午4:50:03 TODO
     */
    public static boolean equals(String left, String right) {
        if (isNullOrEmpty(left) || isNullOrEmpty(right))
            return false;
        return left.equals(right);
    }

    /**
     * 判断两个对象是否相同
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a instanceof Byte && b instanceof Character)
            return equals((Byte) a, (Character) b);
        if (a instanceof Character && b instanceof Byte)
            return equals((Character) a, (Byte) b);
        if (a instanceof String && b instanceof Byte)
            return equals((String) a, (Byte) b);
        if (a instanceof Byte && b instanceof String)
            return equals((Byte) a, (String) b);
        if (a instanceof Character && b instanceof String)
            return equals((Character) a, (String) b);
        if (a instanceof String && b instanceof Character)
            return equals((String) a, (Character) b);
        if (a.equals(b)) return true;
        return false;
    }


    public static boolean isStringEmpty(String s) {
        if (null == s || s.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isStringNotEmpty(String s) {
        if (null == s || s.trim().equals("")) {
            return false;
        }
        return true;
    }

    public static boolean isObjectEmpty(Object object) {
        if (object == null) {
            return true;
        }
        return false;
    }

    public static boolean isObjectNotEmpty(Object object) {
        if (object == null) {
            return false;
        }
        return true;
    }

    public static boolean isListEmpty(List list) {
        if (null == list || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isListNotEmpty(List list) {
        if (null == list || list.size() == 0) {
            return false;
        }
        return true;
    }

    public static String[] getSeparatedItems(String text, char sp) {
        if (isNullOrEmpty(text)) {
            return new String[0];
        }
        return getSeparatedItems(text, new char[]{sp});
    }

    public static String[] changeToArrayByPattern(String string, String pattern) {
        String[] stringArr = string.split(pattern);
        return stringArr;
    }

    /**
     * 获取分割字符集 TODO： Author:dell
     *
     * @param text
     * @param sp
     * @return String[]
     * @2015年7月14日 @下午8:05:45
     */
    public static String[] getSeparatedItems(String text, char[] sp) {
        if (isNullOrEmpty(text)) {
            return new String[0];
        }
        if ((sp == null) || (sp.length <= 0)) {
            sp = DEFAULT_SPLIT_CHARS;
        }
        List<String> list = new ArrayList<String>(10);
        StringBuffer sb = new StringBuffer();
        int size = text.length();
        boolean has = false;
        for (int i = 0; i < size; i++) {
            has = false;
            for (int j = 0; j < sp.length; j++)
                if (text.charAt(i) == sp[j]) {
                    has = true;
                    break;
                }
            if (has) {
                if (sb.length() > 0) {
                    list.add(sb.toString());
                    sb.setLength(0);
                }
            } else
                sb.append(text.charAt(i));
        }
        if (sb.length() > 0)
            list.add(sb.toString());
        String[] ss = new String[list.size()];
        for (int i = 0; i < ss.length; i++)
            ss[i] = list.get(i);
        return ss;
    }

    public static String setNewExecuteMethod(String executeMethod) {
        if (executeMethod.endsWith("D")) {
            executeMethod = executeMethod.replace("D", "天一次");
        } else if (executeMethod.endsWith("W")) {
            executeMethod = executeMethod.replace("W", "周一次");
        } else if (executeMethod.endsWith("M")) {
            executeMethod = executeMethod.replace("M", "月一次");
        }
        StringBuffer buffer = new StringBuffer();
        if (ObjectUtils.isStringNotEmpty(executeMethod)) {
            String[] executeMethods = executeMethod.split(",");
            for (String method : executeMethods) {
                switch (method) {
                    case "1D":
                        buffer.append("每天");
                        break;
                    case "1W":
                        buffer.append("每周");
                        break;
                    case "1M":
                        buffer.append("每月");
                        break;
                    case "W1-W5":
                        buffer.append("每工作日（周一至周五）");
                        break;
                    case "W1":
                        buffer.append("周一,");
                        break;
                    case "W2":
                        buffer.append("周二,");
                        break;
                    case "W3":
                        buffer.append("周三,");
                        break;
                    case "W4":
                        buffer.append("周四,");
                        break;
                    case "W5":
                        buffer.append("周五,");
                        break;
                    case "W6":
                        buffer.append("周六,");
                        break;
                    case "W7":
                        buffer.append("周日,");
                        break;

                    default:
                        break;
                }
            }
        }
        String method = buffer.toString();
        if (ObjectUtils.isStringNotEmpty(method)) {
            String laststr = method.substring(method.length() - 1);
            if (",".equals(laststr)) {
                method = method.substring(0, method.length() - 1);
            }
            return method;
        } else {
            return executeMethod;
        }
    }

    public static String object2String(Object object) {
        return object == null ? "" : object.toString();
    }

    public static boolean isNull(Object object) {
        try {
            if (null == object) {
                return true;
            }

            if (object instanceof String) {
                if (object.equals("")) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
