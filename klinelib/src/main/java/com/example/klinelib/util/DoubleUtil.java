package com.example.klinelib.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by hou on 2015/8/14.
 */
public class DoubleUtil {

    public static double parseDouble(String parserDouble) {
        try {
            return Double.parseDouble(parserDouble);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String format2Decimal(Double d) {
        NumberFormat instance = DecimalFormat.getInstance();
        instance.setMinimumFractionDigits(2);
        instance.setMaximumFractionDigits(2);
        return instance.format(d);
    }

    public static String formatDecimal(Double d) {
        NumberFormat instance = DecimalFormat.getInstance();
        instance.setMinimumFractionDigits(0);
        instance.setMaximumFractionDigits(8);
        return instance.format(d).replace(",", "");
    }


    public static String getDecimalFormatThree(double string) {
        String score = null;

        try {
            DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
            df.applyPattern("0.000");
            score = df.format(Double.valueOf(string));

        } catch (Exception e) {
            return score="0";
        }
        return score;
    }


    /**
     * converting a double number to string by digits
     */
    public static String getStringByDigits(double num, int digits) {
       /* if (digits == 0) {
            return (int) num + "";
        } else {
            NumberFormat instance = DecimalFormat.getInstance();
            instance.setMinimumFractionDigits(digits);
            instance.setMaximumFractionDigits(digits);
            return instance.format(num).replace(",", "");
        }*/
        return String.format(Locale.getDefault(), "%." + digits + "f", num);
    }
}
