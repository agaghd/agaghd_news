package io.agaghd.agaghdnews.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
    public static final String REG_EX_SCRIPT = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    public static final Pattern PATTERN_SCRIPT = Pattern.compile(REG_EX_SCRIPT, Pattern.CASE_INSENSITIVE);

    //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
    public static final String REG_EX_STYLE = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
    public static final Pattern PATTERN_STYLE = Pattern.compile(REG_EX_STYLE, Pattern.CASE_INSENSITIVE);

    // 定义HTML标签的正则表达式
    public static final String REG_EX_HTML = "<[^>]+>";
    public static final Pattern PATTERN_HTML = Pattern.compile(REG_EX_HTML, Pattern.CASE_INSENSITIVE);

    // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    public static final String REG_EX_SPECIAL = "\\&[a-zA-Z]{1,10};";
    public static final Pattern PATTERN_SPECIAL = Pattern.compile(StringUtil.REG_EX_SPECIAL, Pattern.CASE_INSENSITIVE);

    public static final Pattern CONTAIN_NUMBER_PATTERN = Pattern.compile("^.*\\d+.*$");//匹配任意包含字符串的正则表达式
    public static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");//匹配任意包含字符串的正则表达式

    private static SpannableString multiplySpanGray;
    private static SpannableString multiplySpanBlack;

    public static String uuid12() {
        String result = null;
        result = java.util.UUID.randomUUID().toString();
        result = result.substring(result.length() - 11);
        char d = (char) (new java.util.Random().nextInt(26) + 97);
        result = d + result;
        return result;
    }

    public static String getFixLenInt(int len, int val) {
        String r = "";
        String tmp = String.valueOf(val);
        for (int i = 0; i < len - tmp.length(); i++) r += "0";
        r = r + tmp;
        return r;
    }


    public static boolean isValid(String in) {
        return in != null && in.trim().length() > 0 && (!in.equals("null"));
    }

    /**
     * 使用指定的分隔符连接数组元素，如: join(new String[]{"s1","s2","s3"}, ",", "") => "s1,s2,s3"
     *
     * @param elements
     * @param delimiter
     * @param defVal    若某元素为空时的替代值
     * @return
     */
    public static String join(Object[] elements, String delimiter, String defVal) {
        return join(java.util.Arrays.asList(elements).iterator(), delimiter, defVal);
    }

    public static String join(Iterator<?> iter, String delimiter, String defVal) {
        if (iter == null)
            return null;
        StringBuffer sb = new StringBuffer();
        while (iter.hasNext()) {
            Object obj = iter.next();
            sb.append(delimiter).append(obj != null ? obj : defVal);
        }
        return sb.substring(1);
    }

    public static String join(Iterator<?> iter, String delimiter) {
        return join(iter, delimiter, "");
    }

    /**
     * 使用指定的分隔符连接数组元素, 若Java版本为1.8以上，应使用Java自带的String.join方法。
     *
     * @param elements
     * @param delimiter
     * @return
     */
    public static String join(Object[] elements, String delimiter) {
        return join(elements, delimiter, "");
    }

    public static JSONObject query2json(String in) throws Exception {
        JSONObject result = new JSONObject();
        if (in == null) return result;
        String[] params = in.split("&");
        for (String p : params) {
            String[] vs = p.split("=");
            result.put(vs[0], java.net.URLDecoder.decode(vs[1], "UTF-8"));
        }
        return result;
    }

    public static JSONObject tmf814name2json(String in) throws JSONException {
        if (in.charAt(0) == '/')
            in = in.replaceFirst("/", "");
        in = in.replaceAll("/", ",");
        in = in.replaceAll("=", ":");
        JSONObject result = new JSONObject("{" + in + "}");
        return result;
    }

    public static String getMarcroniTag(String nsap) {
        String addr = nsap;
        if (addr == null) return addr;
        addr = addr.replaceAll("\\.", "_");
        if (addr.length() == 40) addr = addr.substring(26, 38);
        return addr;
    }

    public static Object toString(byte[] bytes) {
        if (bytes == null) {
            return JSONObject.NULL;
        }

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }

            sb.append((int) bytes[i]);
        }

        sb.append("]");

        return sb.toString();
    }

    //  2017/11/8  关于小数点的保留位数：商品的数量在系统中显示时若数值为整数，则不显示小数位数（2 件）；若为小数，则最多显示4位小数；
    /*至多保留四位小数*/
    //注释：1.少于等于4位小数的，直接返回
    //     2.多于4位小数的，直接返回4位小数的
    //     3.整数，直接返回
    public static String DecimalFormat4keep0(String str) {
        if (StringUtil.isEmptyString(str)) {
            return str;
        }
        try {
            if (str.equals(".")) {
                return ".";
            }
            double r = Double.parseDouble(str);
            return DecimalFormat4keep0(r);
///// return new DecimalFormat("0.####").format(r);可以代替以下方法
//            String tempString = new DecimalFormat("0.0000").format(r);
//            String[] temp = tempString.split("\\.");
////            Log.d("cj", "strings " + str + " strings " + temp[0] + " strings " + temp[1]);
////            Log.d("cj", "substring " + temp.substring(4, 5));//5
//            if (temp[1].length() == 4 && !temp[1].substring(3, 4).equals("0")) {
//                return tempString;
//            } else if (temp[1].length() == 3 && !temp[1].substring(2, 3).equals("0")) {
//                return new DecimalFormat("0.000").format(r);
//            } else if (temp[1].length() == 2 && !temp[1].substring(1, 2).equals("0")) {
//                return new DecimalFormat("0.00").format(r);
//            } else if (temp[1].length() == 1 && !temp[1].substring(0, 1).equals("0")) {
//                return new DecimalFormat("0.0").format(r);
//            } else {
//                return new DecimalFormat("0").format(r);
//            }
        } catch (Exception e) {
            Log.d("cj", "e " + e.getMessage());
            return str;
        }
    }

    public static String DecimalFormat4keep0(double value) {

        try {
            if (value == 0) {
                return "0";
            }

            DecimalFormat df = new DecimalFormat("0.####");
            df.setRoundingMode(RoundingMode.HALF_UP);

            return df.format(value);
///// return new DecimalFormat("0.####").format(r);可以代替以下方法
//            String tempString = new DecimalFormat("0.0000").format(r);
//            String[] temp = tempString.split("\\.");
////            Log.d("cj", "strings " + str + " strings " + temp[0] + " strings " + temp[1]);
////            Log.d("cj", "substring " + temp.substring(4, 5));//5
//            if (temp[1].length() == 4 && !temp[1].substring(3, 4).equals("0")) {
//                return tempString;
//            } else if (temp[1].length() == 3 && !temp[1].substring(2, 3).equals("0")) {
//                return new DecimalFormat("0.000").format(r);
//            } else if (temp[1].length() == 2 && !temp[1].substring(1, 2).equals("0")) {
//                return new DecimalFormat("0.00").format(r);
//            } else if (temp[1].length() == 1 && !temp[1].substring(0, 1).equals("0")) {
//                return new DecimalFormat("0.0").format(r);
//            } else {
//                return new DecimalFormat("0").format(r);
//            }
        } catch (Exception e) {
            Log.d("cj", "e " + e.getMessage());
            return value + "";
        }
    }

    //  2017/11/8  关于小数点的保留位数：商品的单价在系统中显示时若数值为整数则显示两位小数（2.00 元）；若为小数，则最多显示4位小数（2.1253 元）；
    /*至多保留四位小数*/
    //注释：1.少于等于4位小数的，直接返回
    //     2.多于4位小数的，直接返回4位小数的
    //     3.整数，直接返回2位小数的
    public static String DecimalFormat4keep2(String str) {
        if (StringUtil.isEmptyString(str)) {
            return str;
        }
        try {
            if (str.equals(".")) {
                return ".";
            }
            double r = Double.parseDouble(str);
            if (r == 0) {
                return "0";
            }

            DecimalFormat df = new DecimalFormat("0.00##");
            df.setRoundingMode(RoundingMode.HALF_UP);

            return df.format(r);
///// return new DecimalFormat("0.00##").format(r);可以代替以下方法
//            String tempString = new DecimalFormat("0.0000").format(r);
//            String[] temp = tempString.split("\\.");
////            Log.d("cj", "substring " + temp.substring(4, 5));//5
//            if (!temp[1].substring(3, 4).equals("0")) {
//                return tempString;
//            } else if (!temp[1].substring(2, 3).equals("0")) {
//                return new DecimalFormat("0.000").format(r);
//            } else {
//                return new DecimalFormat("0.00").format(r);
//            }
        } catch (Exception e) {
            Log.d("cj", "e " + e.getMessage());
            return str;
        }
    }

    /**
     * 保留两位小数
     */
    public static String DecimalFormat2(String str) {
        if (StringUtil.isEmptyString(str)) {
            return str;
        }
        try {

            if (str.equals(".")) {
                return ".";
            }

            double f = Double.parseDouble(str);
            return DecimalFormat2(f);

        } catch (Exception e) {
            return str;
        }
    }

    /**
     * 保留两位小数，针对 double 类型
     */
    public static String DecimalFormat2(double value) {
        try {
            if (value == 0) {
                return "0.00";
            }

            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(value);
        } catch (Exception e) {
            return value + "";
        }
    }

    public static String DecimalFormat0(double value) {
        return DecimalFormat0(String.valueOf(value));
    }

    /*保留整数*/
    public static String DecimalFormat0(String str) {
        try {

            if (str.equals(".")) {
                return ".";
            }

            double f = Double.parseDouble(str);

            if (f == 0) {
                return "0";
            }

            DecimalFormat df = new DecimalFormat("###");
            df.setRoundingMode(RoundingMode.HALF_UP);

            return df.format(f);
        } catch (Exception e) {
            return str;
        }
    }

    /*自动判断是否保留两位小数*/
    public static String DecimalFormat2Quantity(String str) {
        try {

            if (str.equals(".")) {
                return ".";
            }

            double f = Double.parseDouble(str);
            if (f == 0) {
                return "0";
            }

            DecimalFormat df = new DecimalFormat("0.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(f);

            /*DecimalFormat df2 = new DecimalFormat("0.00");
            DecimalFormat df1 = new DecimalFormat("0.0");
            DecimalFormat df0 = new DecimalFormat("###");
            if (f == (long) f) {
                return df0.format(f);
            } else {
                String f1 = df1.format(f) + "0";
                String f2 = df2.format(f);
                if (f1.equals(f2)) {
                    return df1.format(f);
                } else {
                    return df2.format(f);
                }
            }*/
            /*if (f < 1 && f > 0) {
                return "0" + df.format(f);
            } else {
                return df.format(f);
            }*/
        } catch (Exception e) {
            return str;
        }
    }


    /**
     * 保留两位小数，针对 double 类型
     */
    public static String DecimalFormat1(double value) {
        try {
            if (value == 0) {
                return "0.0";
            }

            DecimalFormat df = new DecimalFormat("0.0");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(value);
        } catch (Exception e) {
            return value + "";
        }
    }

    /*判断字符串是否为空*/
    public static boolean isEmptyString(String str) {
        if (str != null && !"".equals(str.trim())) {
            return false;
        }
        return true;
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /*将图片置灰*/
    public static final Bitmap greyBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }


    public static String stringtobase64(String s) {
        try {
            String encodeStr = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
            return encodeStr;
        } catch (Exception e) {
            return s;
        }
    }

    public static Bitmap loadLargeImage(String imagePath, int width, int height) {
        if (imagePath != null) {
            //解析图片时需要使用到的参数都封装在这个对象里了
            BitmapFactory.Options opt = new BitmapFactory.Options();
            //不为像素申请内存，只获取图片宽高
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, opt);
            //拿到图片宽高
            int imageWidth = opt.outWidth;
            int imageHeight = opt.outHeight;
            if (imageWidth == 0 || imageHeight == 0) {
                return null;
            }

            //计算缩放比例
            int scale = 1;
            int scaleWidth = imageWidth / width;
            int scaleHeight = imageHeight / height;
            if (scaleWidth >= scaleHeight && scaleWidth >= 1) {
                scale = scaleWidth;
            } else if (scaleWidth < scaleHeight && scaleHeight >= 1) {
                scale = scaleHeight;
            }

            //设置缩放比例
            opt.inSampleSize = scale;
            opt.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(imagePath, opt);
            return bm;
        } else {
            return null;
        }
    }

    public static String base64tostring(String s) {
        try {
            String decodeStr = new String(Base64.decode(s.getBytes(), Base64.DEFAULT));
            return decodeStr;
        } catch (Exception e) {
            return s;
        }
    }

    /*判断字符串是否为手机号码*/
    public static boolean isMobileNum(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        String newString = mobiles.replace("-", "").replace(" ", "");
        Pattern p = Pattern.compile("^([1][3-9])\\d{9}$"); ///[0-1][3-9]
        Matcher m = p.matcher(newString.trim());
        return m.matches();
    }

    /*判断字符串中是否存在中文*/
    public static boolean isHaveHanZi(String hanzi) {
        if (hanzi == null) {
            return false;
        }
        return (hanzi.getBytes().length != hanzi.length());
    }

    /**
     * 设置小数位数控制
     */
    //  2017/11/8 关于小数点的保留位数：商品设置弹窗中手动输入的商品数量和单价最多输入4位小数
    //注：农机具数量最多输入4位小数
    public static InputFilter lengthfilter4 = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String dValue = dest.toString();
            Pattern p = Pattern.compile("[0-9]*");
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            //验证非数字或者小数点的情况
            Matcher m = p.matcher(source);
            if (dValue.contains(".")) {
                //已经存在小数点的情况下，只能输入数字
                if (!m.matches()) {
                    return null;
                }
            } else {
                //未输入小数点的情况下，可以输入小数点和数字
                if (!m.matches() && !source.equals(".")) {
                    return null;
                }
            }
            //验证输入金额的大小
            if (!source.toString().equals("") && !source.toString().equals(".")) {
                double dold = Double.parseDouble(dValue + source.toString());
                if (dold > 99999999) {
                    return dest.subSequence(dstart, dend);
                } else if (dold == 99999999) {
                    if (source.toString().equals(".")) {
                        return dest.subSequence(dstart, dend);
                    }
                }
            }
            //验证小数位精度是否正确
            if (dValue.contains(".")) {
                int index = dValue.indexOf(".");
                int len = dend - index;

                //小数位只能4位
                if (len > 4) {
                    CharSequence newText = dest.subSequence(dstart, dend);
                    return newText;
                }
            }
            return dest.subSequence(dstart, dend) + source.toString();
        }
    };

    /**
     * 设置小数位数控制
     */
    public static InputFilter lengthfilter2 = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String dValue = dest.toString();
            Pattern p = Pattern.compile("[0-9]*");
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            //验证非数字或者小数点的情况
            Matcher m = p.matcher(source);
            if (dValue.contains(".")) {
                //已经存在小数点的情况下，只能输入数字
                if (!m.matches()) {
                    return null;
                }
            } else {
                //未输入小数点的情况下，可以输入小数点和数字
                if (!m.matches() && !source.equals(".")) {
                    return null;
                }
            }
            //验证输入金额的大小
            if (!source.toString().equals("") && !source.toString().equals(".")) {
                double dold = Double.parseDouble(dValue + source.toString());
                if (dold > 99999999) {
                    return dest.subSequence(dstart, dend);
                } else if (dold == 99999999) {
                    if (source.toString().equals(".")) {
                        return dest.subSequence(dstart, dend);
                    }
                }
            }
            //验证小数位精度是否正确
            if (dValue.contains(".")) {
                int index = dValue.indexOf(".");
                int len = dend - index;
                //  2017/11/8 关于小数点的保留位数：
                //手动输入商品的数量和单价时最多输入4位小数；
                //注：农机具数量也做以上的处理

                //小数位只能2位
                if (len > 2) {
                    CharSequence newText = dest.subSequence(dstart, dend);
                    return newText;
                }
            }
            return dest.subSequence(dstart, dend) + source.toString();
        }
    };

    public static int valueGetKey(String[] array, String value) {
        try {
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(value)) {
                    return i;
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /*字符串拼接，四个五个字符，上一行两个；六个字符，上3下3*/
    public static String getTianjiaText(String str) {
        String str1, str2;
        if (str.length() == 4 || str.length() == 5) {
            str1 = str.substring(0, 2);
            str2 = str.substring(2);
            return str1 + "\n" + str2;
        } else if (str.length() == 6) {
            str1 = str.substring(0, 3);
            str2 = str.substring(3);
            return str1 + "\n" + str2;
        }
        return str;
    }

    /*长字符串拼接，参数接收数组或多个字符串,速度快*/
    public static String getSubString(String... paramas) {
        StringBuilder sb = new StringBuilder();
        for (String s : paramas) {
            sb.append(s);
        }
        return sb.toString();
    }


    /**
     * String 转 Double
     */
    public static double stringToDouble(String mString) {
        double mDouble = 0;
        try {
            if (!StringUtil.isEmptyString(mString)) {
                if (mString.equals(".") || mString.equals("-")) {
                    mDouble = 0;
                } else {
                    mDouble = Double.parseDouble(mString);
                }
            }
        } catch (Exception e) {
//            Log.i("cj", "Exception " + mString);
            mDouble = 0;
        }
        return mDouble;
    }

    /**
     * String 转 Long
     */
    public static Long stringToLong(String mString) {
        Long value = 0L;
        try {
            if (!StringUtil.isEmptyString(mString)) {
                value = Long.parseLong(mString);
            }
        } catch (Exception e) {
            value = 0L;
        }
        return value;
    }

    /**
     * String 转 int
     */
    public static int stringToInt(String mString) {
        int value = 0;
        try {
            if (!StringUtil.isEmptyString(mString)) {
                value = Integer.parseInt(mString);
            }
        } catch (Exception e) {
            value = 0;
        }
        return value;
    }

    /**
     * 去除String中的“ 元”
     */
    public static String delTabRMB(String mString) {
        String value = mString;
        if (mString.contains("元")) {
            String[] strs = mString.split(" ");
            value = strs[0];
        }
        return value;
    }

    /**
     * 去除String中的“小计:”
     */
    public static String delTabRMB2(String mString) {
        String value = mString;
        if (mString.contains("小计:")) {
            String[] strs = mString.split(":");
            value = strs[1];
        }
        return value;
    }


    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(CharSequence str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否是有效的整数或浮点数
     *
     * @param str 待判断的字符串
     * @return boolean 是否是有效整数或浮点数
     */
    public static boolean isNumDecimal(CharSequence str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^-?[0-9]+([.][0-9]+)?$");
        Matcher isNumDecimal = pattern.matcher(str);
        if (!isNumDecimal.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断两个字符串的值是否相等
     */
    public static boolean isTheValueSame(String str1, String str2) {
        double value1 = stringToDouble(str1);
        double value2 = stringToDouble(str2);
        return value1 == value2;
    }

    /**
     * 判断两个显示金额字符串的值是否相等
     * 目前存在 2 位以下小数时，还不了款的问题
     */
    public static boolean isTheMoneySame(String d1, String d2) {
        return StringUtil.stringToDouble(d1) == StringUtil.stringToDouble(d2);
    }

    /**
     * 千位分隔符,并且小数点后保留2位
     *
     * @param num
     * @return String
     */
    public static String qianWeiFenGe(double num) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String ss = df.format(num);
        return ss;
    }

    /**
     * 检查字符中是否包含数字
     *
     * @param source 源字符
     * @return boolean 是否包含数字
     */
    public static boolean containNumber(CharSequence source) {
        boolean isContainNumber = false;
        if (!TextUtils.isEmpty(source)) {
            Matcher matcher = CONTAIN_NUMBER_PATTERN.matcher(source);
            isContainNumber = matcher.matches();
        }
        return isContainNumber;
    }

    private StringUtil() {

    }

    public static String getBankNameNo(String bankName, String bankNo) {
        String bankNameNo;
        if (bankName.contains("中国工商银行")) {
            bankNameNo = "中国工商银行";
        } else if (bankName.contains("中国银行")) {
            bankNameNo = "中国银行";
        } else if (bankName.contains("招商银行")) {
            bankNameNo = "招商银行";
        } else if (bankName.contains("中国邮政储蓄银行")) {
            bankNameNo = "中国邮政储蓄银行";
        } else if (bankName.contains("农村信用合作社")) {
            bankNameNo = "农村信用合作社";
        } else if (bankName.contains("中国农业银行")) {
            bankNameNo = "中国农业银行";
        } else if (bankName.contains("中国建设银行")) {
            bankNameNo = "中国建设银行";
        } else {
            //要截取银行，不要显示支行
            if (bankName.contains("银行")) {
                String[] sourceStrArray = bankName.split("银行");
                bankNameNo = sourceStrArray[0] + "银行";
            } else {
                bankNameNo = bankName;
            }

        }
        bankNameNo = bankNameNo + "(" + bankNo.substring(bankNo.length() - 4, bankNo.length()) + ")";
        return bankNameNo;
    }

    /**
     * 截取字符串后四位
     *
     * @param s
     * @return
     */
    public static String BankaccountNo(String s) {
        String BankaccountNo = "**** **** **** ";
        String b = s.substring(s.length() - 4, s.length());
        return BankaccountNo + b;
    }

    /**
     * 格式化银行卡号
     *
     * @param s
     * @return
     */
    public static String FormatBankCard(String s) {
        StringBuilder str = new StringBuilder(s.replace(" ", ""));
        int i = str.length() / 4;
        int j = str.length() % 4;
        for (int x = (j == 0 ? i - 1 : i); x > 0; x--) {
            str = str.insert(x * 4, " ");
        }
        return str.toString();
      /*  if (s.length() <= 4) {
            return s;
        } else if (s.length() <= 8) {
            String b = s.substring(0, 4);
            String c = s.substring(4, s.length());
            return b + " " + c;
        } else if (s.length() <= 12) {
            String b = s.substring(0, 4);
            String c = s.substring(4, 8);
            String d = s.substring(8, s.length());
            return b + " " + c + " " + d;
        } else {
            String b = s.substring(0, 4);
            String c = s.substring(4, 8);
            String d = s.substring(8, 12);
            String e = s.substring(12, s.length());
            return b + " " + c + " " + d + " " + e;
        }*/
    }

    /**
     * 格式化身份证号码
     *
     * @param s
     * @return
     */
    public static String FormatIdCard(String s) {
        if (s.length() <= 6) {
            return s;
        } else if (s.length() <= 14) {
            String b = s.substring(0, 6);
            String c = s.substring(6, s.length());
            return b + " " + c;
        } else {
            String b = s.substring(0, 6);
            String c = s.substring(6, 14);
            String d = s.substring(14, s.length());
            return b + " " + c + " " + d;
        }
//        String BankaccountNo = "**** **** **** ";
//        String b = s.substring(s.length() - 4, s.length());
//        return BankaccountNo + b;
    }


    /**
     * 获取商品单位规格的方法，仅适用于 X 副单位/主单位 格式的字符串
     *
     * @param info 源字符串
     * @return 保存信息的map
     */
    @Nullable
    public static Map<String, String> getGoodsQuantityInfo(String info) {
        Map<String, String> map = new ArrayMap<>();
        map.put("packingQuantity", "");
        map.put("accessoryUnit", "");
        map.put("primaryUnit", "");
        if (info != null) {
            Pattern pattern = Pattern.compile("^([0-9]+[.]*[0-9]*)\\D+/{1}\\D+$");
            Matcher matcher = pattern.matcher(info);
            if (matcher.matches()) {
                Pattern packingQuantityPattern = Pattern.compile("([0-9]+[.]*[0-9]*)");
                Matcher packingQuantityMatcher = packingQuantityPattern.matcher(info);
                if (packingQuantityMatcher.find()) {
                    String packingQuantity = packingQuantityMatcher.group();
                    String[] splitResult = info.split("/");
                    String accessoryUnit = splitResult[0].replace(packingQuantity, "");
                    String primaryUnit = splitResult[1];
                    map.put("packingQuantity", packingQuantity);
                    map.put("accessoryUnit", accessoryUnit);
                    map.put("primaryUnit", primaryUnit);
                }

            }
        }
        return map;
    }

    /**
     * 判断字符是否是合法的URL
     *
     * @param url 判断的url
     * @return 是否是合法url
     */
    public static boolean isValidUrl(CharSequence url) {
        return URL_PATTERN.matcher(url).matches();
    }

    /**
     * 过滤HTML文字的方法
     *
     * @param htmlStr HTML文本
     * @return 过滤后的文本
     */
    public static String filteHtmlContent(String htmlStr) {
        Matcher scriptMatcher = PATTERN_SCRIPT.matcher(htmlStr);
        htmlStr = scriptMatcher.replaceAll(""); // 过滤script标签
        Matcher styleMatcher = PATTERN_STYLE.matcher(htmlStr);
        htmlStr = styleMatcher.replaceAll(""); // 过滤style标签
        Matcher htmlMatcher = PATTERN_HTML.matcher(htmlStr);
        htmlStr = htmlMatcher.replaceAll(""); // 过滤html标签
        Matcher specialMatcher = PATTERN_SPECIAL.matcher(htmlStr);
        htmlStr = specialMatcher.replaceAll(""); // 过滤特殊标签
        return htmlStr;
    }

    /**
     * 过滤HTML文字的方法
     *
     * @param htmlStr HTML文本
     * @return 过滤后的文本，仅替换 \ 的文案
     */
    public static String filteHtmlContentSimple(String htmlStr) {
        return htmlStr.replaceAll("\"", "");
    }

    /**
     * 用于生成有前景色、背景色、加粗等效果的文字，
     *
     * @param text                 字符串
     * @param getColorFromResource 是否从资源文件获取颜色
     * @param frontColor           前景色int值/前景色资源Id
     * @param backColor            背景色int值/背景色Id
     * @param type                 字体样式类型 传Typeface下的几个常量参数
     * @return 添加了指定样式的字符
     */
    @SuppressWarnings("deprecation")
    public static SpannableString getSpannableStringStyled(
            @NonNull String text,
            @NonNull Context context,
            boolean getColorFromResource,
            final int frontColor,
            final int backColor,
            final int type) {
        final int start = 0;
        final int finish = text.length();
        int getbackColor = backColor;
        int getfrontColor = frontColor;
        if (getColorFromResource) {
            try {
                getbackColor = context.getResources().getColor(backColor);
                getfrontColor = context.getResources().getColor(frontColor);
            } catch (Resources.NotFoundException ne) {
                getbackColor = backColor;
                getfrontColor = frontColor;
            }
        }
        BackgroundColorSpan bcsT = new BackgroundColorSpan(getbackColor);
        ForegroundColorSpan fcsBlack = new ForegroundColorSpan(getfrontColor);
        SpannableString spannableSign = new SpannableString(text);
        spannableSign.setSpan(bcsT, start, finish, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableSign.setSpan(fcsBlack, start, finish, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan styleSpan = new StyleSpan(type);
        spannableSign.setSpan(styleSpan, start, finish, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableSign;
    }
}