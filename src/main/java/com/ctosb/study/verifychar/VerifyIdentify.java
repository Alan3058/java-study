package com.ctosb.study.verifychar;

public class VerifyIdentify {

    private static int[] weightings = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};//权重位
    private static String[] verifyCode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};//校验码

    public static String result(String num) {
        return num + genVerifyCode(num);
    }

    /**
     * 获取最后一位校验码
     *
     * @param num
     * @return
     * @author Alan
     * @time 2015-10-27 下午03:27:22
     */
    public static String genVerifyCode(String num) {
        char nums[] = num.toCharArray();
        int sum = 0;
        for (int i = 0; i < weightings.length; i++) {
            sum += weightings[i] * Integer.parseInt(nums[i] + "");
        }
        int n = sum % 11;
        return verifyCode[n];
    }


    /**
     * @param args
     * @author Alan
     * @time 2015-10-21 上午10:09:31
     */
    public static void main(String[] args) {
        String rs = result("36242219901017305");
        System.out.println(rs);
    }

}
