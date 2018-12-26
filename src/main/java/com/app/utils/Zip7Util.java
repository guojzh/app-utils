package com.app.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class Zip7Util {

    public static final String COMM1 = "cmd /c start /B 7z x ";
    public static final String COMM2 = " -o";
    public static final String COMM3 = " -aoa -p";

    public static final String[] TEMP = {
        "", "a", "A", "b", "B", "h", "H", "c", "d", "e", "f", "g", "C", "D", "E", "F", "G", "I", "J", "K", "L", "M", "N", "i", "j", "k",
        "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "!", "@", "#", "$", "%", "&", "-", "=", ";", "+", "/", "\\", "?", "*",
        "^", ":", "\"", "[", "]", "{", "}", "<", ">", "|", ",", ".", "~", "`"
    };
    public static String[] NEWTEMP = {
            "", "a", "A", "b", "B", "h", "H", "c", "d", "e", "f", "g", "C", "D", "E", "F", "G", "I", "J", "K", "L", "M", "N", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "!", "@", "#", "$", "%", "&", "-", "=", ";", "+", "/", "\\", "?", "*",
            "^", ":", "\"", "[", "]", "{", "}", "<", ">", "|", ",", ".", "~", "`"
        };

    public static void zip7(Scanner sc) {
        while (true) {
            System.out.print("\n请输入文件完整路径(.7z .zip)：");
            String filePath = sc.next().replace("\\", "/");
            if (filePath.endsWith(".7z") || filePath.endsWith(".zip")) {
                File folder = new File(filePath);
                if (folder.exists()) {
                    String outPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
                    System.out.println("输出路径：" + outPath);

                    String comm = COMM1 + filePath + COMM2 + outPath + COMM3;
                    handle(sc, comm, outPath);
                    break;
                } else {
                    System.out.println("文件不存在！");
                }
            } else {
                System.out.println("文件路径或文件格式错误！");
            }
        }
    }

    private static void handle(Scanner sc, String comm, String outPath) {
        System.out.print("\n为效率考虑，只允许以下规则，预计时间最长为数小时"
                        + "\nE：确定部分，X：模糊部分 (最大长度为4位)"
                        + "\n\t1.E + X\n\t2.X + E\n\t3.X"
                        + "\n请选择模式：");
        String pswE = "";

        ok: while (true) {
            switch (sc.next()) {
                case "1":
                    System.out.print("请输入E确定部分：");
                    pswE = sc.next();
                    inputPswX(sc);
                    process(comm, outPath, pswE, 1);
                    break ok;
                case "2":
                    inputPswX(sc);
                    System.out.print("请输入E确定部分：");
                    pswE = sc.next();
                    process(comm, outPath, pswE, 2);
                    break ok;
                case "3":
                    inputPswX(sc);
                    process(comm, outPath, pswE, 3);
                    break ok;
                default:
                    System.out.println("请输入正确的序号");
                    break;
            }
        }
    }

    private static void inputPswX(Scanner sc) {
        String[] pswX = new String[4];

        input: for (int i = 0; i < 4; i++) {
            while (true) {
                System.out.print("请输入第" + (i + 1) + "位X模糊部分：");
                String input = sc.next();
                if (input.length() == 1) {
                    pswX[i] = input;

                    System.out.print("是否继续输入？(Y/N):");
                    if ("Y".equals(sc.next().toUpperCase())) {
                        break;
                    } else {
                        break input;
                    }
                } else {
                    System.out.println("请输入1位！");
                }
            }
        }

        NEWTEMP = TEMP;
        String[] newTemp = new String[NEWTEMP.length];
        newTemp[0] = "";
        for (int i = 0; i < 4; i++) {
            if (StringUtils.isNotBlank(pswX[i])) {
                newTemp[i + 1] = pswX[i];
                for (int j = 0; j < NEWTEMP.length; j++) {
                    if (pswX[i].equals(NEWTEMP[j])) {
                        NEWTEMP[j] = "XXXXX";
                    }
                }
            } else {
                i++;
                for (int k = 1; k < NEWTEMP.length; k++) {
                    if (!"XXXXX".equals(NEWTEMP[k])) {
                        newTemp[i] = NEWTEMP[k];
                        i++;
                    }
                }
                NEWTEMP = newTemp;
                break;
            }
        }
    }

    private static void process(String comm, String outPath, String pswE, int mode) {
        long stratTime = System.nanoTime();

        Runtime runtime = Runtime.getRuntime();
        Process process = null;

        try {
            ok: for (int i1 = 0, n = NEWTEMP.length; i1 < n; i1++) {
                for (int i2 = 0; i2 < n; i2++) {
                    for (int i3 = 0; i3 < n; i3++) {
                        for (int i4 = 0; i4 < n; i4++) {
                            String psw = null;
                            String pswX = NEWTEMP[i1] + NEWTEMP[i2] + NEWTEMP[i3] + NEWTEMP[i4];
                            switch (mode) {
                                case 1:
                                    psw = pswE + pswX;
                                    break;
                                case 2:
                                    psw = pswX + pswE;
                                    break;
                                default:
                                    break;
                            }
                            System.out.println(psw);

                            process = runtime.exec(comm + psw);
                            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String inline;
                            while ((inline = br.readLine()) != null) {
                                if ("Everything is Ok".equals(inline)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
                                    String filename = outPath + sdf.format(new Date()) + ".txt";
                                    File file = new File(filename);
                                    if (!file.exists()) {
                                        file.createNewFile();
                                    }
                                    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    bw.write("Password:\n" + psw);
                                    bw.flush();
                                    fw.flush();
                                    bw.close();
                                    fw.close();
                                    break ok;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        long totleTime = (endTime - stratTime) / 1000000;
        String totleTimeStr = "";
        if (totleTime >= 1000 * 60 * 60) {
            long hou = totleTime / (1000 * 60 * 60);
            long min = (totleTime - hou * (1000 * 60 * 60)) / (1000 * 60);
            long sec = (totleTime - hou * (1000 * 60 * 60) - min * (1000 * 60)) / 1000;
            long mis = (totleTime - hou * (1000 * 60 * 60) - min * (1000 * 60) - sec * 1000);
            totleTimeStr += hou + " 小时 ";
            if (min > 0) {
                totleTimeStr = min + " 分钟 ";
            }
            if (sec > 0) {
                totleTimeStr = sec + " 秒 ";
            }
            if (mis > 0) {
                totleTimeStr = mis + " 毫秒";
            }
        } else if (totleTime >= 1000 * 60) {
            long min = totleTime / (1000 * 60);
            long sec = (totleTime - min * (1000 * 60)) / 1000;
            long mis = (totleTime - min * (1000 * 60) - sec * 1000);
            totleTimeStr = min + " 分钟 ";
            if (sec > 0) {
                totleTimeStr = sec + " 秒 ";
            }
            if (mis > 0) {
                totleTimeStr = mis + " 毫秒";
            }
        } else if (totleTime >= 1000) {
            long sec = totleTime / 1000;
            long mis = (totleTime - sec * 1000);
            totleTimeStr = sec + " 秒 ";
            if (mis > 0) {
                totleTimeStr = mis + " 毫秒";
            }
        } else {
            totleTimeStr = totleTime + " 毫秒";
        }
        System.out.println("\n\nDone\t共耗时：" + totleTimeStr);
    }

}