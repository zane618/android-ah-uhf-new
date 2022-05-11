package com.beiming.uhf_test.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by htj on 2017/12/4.
 */

public class PWDUtils {

    /**
     * 规则：必须包含大小写字母及数字及符号（每一种都必须含有，不区分大小写）
     * 是否包含
     *
     * @param passwordStr
     * @return
     */
    public static boolean checkManagePWD(String passwordStr) {
        // Z = 字母       S = 数字           T = 特殊字符
        String regexZ = "[A-Za-z]+";
        String regexS = "^\\d+$";
        String regexT = "[~!@#$%^&*.]+";
        String regexZT = "[a-zA-Z~!@#$%^&*.]+";
        String regexZS = "[0-9A-Za-z]+";
        String regexST = "[\\d~!@#$%^&*.]*";
        String regexZST = "[\\da-zA-Z~!@#$%^&*.]+";
        String regexALL = "[\\da-zA-Z~\\W_!@#$%^&*`~()-+=]+";//字母、数字和字符 缺一不可

/*        if (passwordStr.matches(regexALL)) {
            return true;
        } else {
            return false;
        }*/
        if (passwordStr.matches(regexZ)) {
//            return "纯字母，弱";
            return false;
        }
        if (passwordStr.matches(regexS)) {
//            return "纯数字，弱";
        }
        if (passwordStr.matches(regexT)) {
//            return "纯字符，弱";
            return false;
        }
        if (passwordStr.matches(regexZT)) {
//            return "字母字符，中";
            return false;
        }
        if (passwordStr.matches(regexZS)) {
//            return "字母数字，中";
            return false;
        }
        if (passwordStr.matches(regexST)) {
//            return "数字字符，中";
            return false;
        }
        if (passwordStr.matches(regexALL)) {
//            return "密码正确";
            return true;
        }
        if (passwordStr.matches(regexZST)) {
//            return "强";
            return false;
        }
//        return "不知道是啥";
        return false;
    }

    /**
     * 规则1：至少包含大小写字母及数字中的一种
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterOrDigit(String str) {
        boolean isLetterOrDigit = false;//定义一个boolean值，用来表示是否包含字母或数字
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetterOrDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isLetterOrDigit = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isLetterOrDigit && str.matches(regex);
        return isRight;
    }

    /**
     * 规则2：至少包含大小写字母及数字中的两种
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    /**
     * 规则3：必须同时包含大小写字母及数字
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
        return isRight;
    }

    /**
     * 判断EditText输入的数字、中文还是字母方法
     */
    public static void whatIsInput(Context context, EditText edInput) {
        String txt = edInput.getText().toString();

        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是数字", Toast.LENGTH_SHORT).show();
        }
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是字母", Toast.LENGTH_SHORT).show();
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是汉字", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查问题答案是否正确
     *
     * @param pwd1 新密码
     * @param pwd2 却认新密码
     */
    public static boolean checkPassword(Context context, String pwd1, String pwd2) {
        if (TextUtils.isEmpty(pwd1)) {
            ToastUtils.showToastForLong(context, "密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(pwd2)) {
            ToastUtils.showToastForLong(context, "确认密码不能为空");
            return false;
        }
        //限制密码的正则
        if (PwdIsOk(pwd1) && PwdIsOk(pwd2)) {
            if (pwd1.equals(pwd2)) {
                return true;
            } else {
                ToastUtils.showToastForLong(context, "密码不一致，请重新输入");
                return false;
            }
        } else {
            ToastUtils.showToastForLong(context, "密码必须为6-18位字母或数字");
            return false;
        }
    }

    //限制密码的正则
    public static boolean PwdIsOk(String str) {
        String regEx = "^[a-zA-Z0-9]{6,18}$";
//        String regEx = "^[a-zA-Z]\\w{5,17}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //普通用户账号的正则
    public static boolean userNameIsOk(String str) {
        String regEx = "^[0-9]{10}$";
//        String regEx = "^[a-zA-Z]\\w{5,17}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //管理员账号是否正确的正则
    public static boolean manageNumberIsOk(String str) {
//        String regEx = "/\\<[a-zA-Z]\\d\\d\\d\\d\\d\\>/";
        String regEx = "^[a-zA-Z]\\d{5}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
