package com.app.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class DirDelUtil {

    private static final String YES = "Y";

    private static int folderCounts = 0;

    public static void dirDel(Scanner sc) {
        File folder;

        System.out.print("\n是否使用默认目录？(Y/N):");
        if (YES.equals(sc.next().toUpperCase())) {
            folder = new File("D:\\Program\\workspace");
            System.out.println("默认目录：" + folder.getAbsolutePath());
            if (!folder.exists()) {
                System.out.println("目录不存在：" + folder.getAbsolutePath());
                System.out.println("是否使用当前目录？(Y/N):");
                if (YES.equals(sc.next().toUpperCase())) {
                    folder = new File((new File("")).getAbsolutePath());
                    System.out.println("当前目录：" + folder.getAbsolutePath());
                } else {
                    return;
                }
            }
        } else {
            System.out.print("是否使用当前目录？(Y/N):");
            if (YES.equals(sc.next().toUpperCase())) {
                folder = new File((new File("")).getAbsolutePath());
                System.out.println("当前目录：" + folder.getAbsolutePath());
            } else {
                return;
            }
        }

        String keywordStr;
        System.out.print("\n是否使用默认关键词？(Y/N):");
        if (YES.equals(sc.next().toUpperCase())) {
            keywordStr = "bin,target,build";
            System.out.println("默认关键词：" + keywordStr);
        } else {
            System.out.print("请输入关键词(以半角逗号隔开)：");
            keywordStr = sc.next().toUpperCase();
        }
        String[] keywordArray = keywordStr.split(",");

        String negaword;
        System.out.print("\n是否使用默认排除项？(Y/N):");
        if (YES.equals(sc.next().toUpperCase())) {
            negaword = ".metadata,neusoft,ibatis";
            System.out.println("默认排除项：" + negaword);
        } else {
            System.out.print("请输入排除项(以半角逗号隔开)：");
            negaword = sc.next().toUpperCase();
        }
        String[] negawordArray = negaword.split(",");

        if (keywordArray != null && keywordArray.length > 0) {
            System.out.println("\n搜索中…………");

            File[][] results = new File[keywordArray.length][];

            for (int i = 0; i < keywordArray.length; i++) {
                folderCounts = 0;
                results[i] = searchFile(folder, keywordArray[i], negawordArray);
                System.out.println("\n在 " + folder + " 以及所有子文件夹时查找对象：" + keywordArray[i] + ";  排除对象：" + negaword);
                System.out.println("查找了" + folderCounts + " 个文件夹，共找到 " + results[i].length + " 个符合条件的文件夹：");

                for (int j = 0; j < results[i].length; j++) {
                    System.out.println(results[i][j].getAbsolutePath());
                }
            }

            boolean confirmDeleteFlag = true;
            if (keywordArray.length == 0 || results.length == 0 || results.length == 0) {
                confirmDeleteFlag = false;
            } else {
                boolean resultsDeleteFlag = false;
                for (int i = 0; i < keywordArray.length; i++) {
                    if (results[i].length > 0) {
                        resultsDeleteFlag = true;
                        break;
                    }
                }
                confirmDeleteFlag = resultsDeleteFlag;
            }

            if (confirmDeleteFlag) {
                System.out.print("\n确定删除？(Y/N):");
                if (YES.equals(sc.next().toUpperCase())) {
                    for (int i = 0; i < keywordArray.length; i++) {
                        for (int j = 0; j < results[i].length; j++) {
                            File file = results[i][j];
                            String df = "失败！";
                            try {
                                FileUtils.deleteDirectory(file);
                                df = "成功！";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println(file.getAbsolutePath() + "\t删除" + df);
                        }
                    }
                }
            }

        } else {
            System.out.println("关键词：" + keywordStr + "错误！");
            return;
        }
    }

    private static File[] searchFile(File folder, final String keyWord, final String[] negawordArray) {

        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    boolean negawordFlag = true;

                    if (negawordArray != null && negawordArray.length > 0) {
                        for (String negaword : negawordArray) {
                            if (StringUtils.isNotBlank(negaword) && pathname.getName().toLowerCase().contains(negaword.toLowerCase())) {
                                negawordFlag = false;
                                break;
                            }
                        }
                    }

                    if (negawordFlag) {
                        folderCounts++;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });

        List<File> result = new ArrayList<File>();

        if (subFolders != null && subFolders.length > 0) {
            for (int i = 0; i < subFolders.length; i++) {
                if (keyWord.toLowerCase().equals(subFolders[i].getName().toLowerCase())) {
                    result.add(subFolders[i]);
                } else {
                    File[] foldResult = searchFile(subFolders[i], keyWord, negawordArray);
                    for (int j = 0; j < foldResult.length; j++) {
                        result.add(foldResult[j]);
                    }
                }
            }
        }

        File[] files = new File[result.size()];
        result.toArray(files);
        return files;
    }

}