package com.app.utils;

public class ForTest {

    public static final int WN = 10000;

    public static void main(String[] args) {
        String comm = "cmd /c start /B 7z x D:/h.7z -oD:/ -aoa -pab";
        String[] temp = Zip7Util.TEMP;

        long time1 = 0;
        long time2 = 0;

        for (int wi = 0; wi < WN; wi++) {
            time1 += test1(comm, temp);
            time2 += test2(comm, temp);
            // System.out.println(wi + 1);
        }

        System.out.println("平均未优化list耗时：\t" + time1 / WN / 1000000 + "ms");
        System.out.println("平均优化list耗时：\t" + time2 / WN / 1000000 + "ms");
    }

    private static long test1(String comm, String[] temp) {
        long stratTime1 = System.nanoTime();
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                for (int k = 0; k < temp.length; k++) {
                    String command = comm + temp[i] + temp[j] + temp[k];
                }
            }
        }
        long endTime1 = System.nanoTime();
        long result = endTime1 - stratTime1;
        // System.out.println("未优化list耗时：\t" + result + "us");
        return result;
    }

    private static long test2(String comm, String[] temp) {
        long stratTime2 = System.nanoTime();
        for (int i = 0, n = temp.length; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    String command = comm + temp[i] + temp[j] + temp[k];
                }
            }
        }
        long endTime2 = System.nanoTime();
        long result = endTime2 - stratTime2;
        // System.out.println("优化list耗时：\t" + result + "us");
        return result;
    }

}