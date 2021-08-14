package com.app.utils;

import com.app.config.Config;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DirDelUtil {

    public DirDelUtil(Config config) {
        FOLDER = StringUtils.isBlank(config.getFolder()) ? DEFAULT_FOLDER : config.getFolder();
        KEYWORD = StringUtils.isBlank(config.getKeyword()) ? DEFAULT_KEYWORD : config.getKeyword();
        NEGAWORD = StringUtils.isBlank(config.getNegaword()) ? DEFAULT_NEGAWORD : config.getNegaword();
    }

    private String FOLDER;
    private String KEYWORD;
    private String NEGAWORD;

    private final String DEFAULT_FOLDER = "D:\\Library\\workspace";
    private final String DEFAULT_KEYWORD = "target";
    private final String DEFAULT_NEGAWORD = ".metadata";

    private final String YES = "Y";

    private int folderCounts = 0;

    public void dirDel(Scanner sc) {
        File folder;

        System.out.print("\n是否使用配置目录？(Y/N):");
        if (YES.equals(sc.next().toUpperCase())) {
            folder = new File(FOLDER);
            System.out.println("配置目录：" + folder.getAbsolutePath());
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
                while (true) {
                    System.out.print("\n请输入目录完整路径：");
                    String folderPath = sc.next().replace("\\", "/");
                    folder = new File(folderPath);
                    if (folder.exists()) {
                        System.out.println("输入目录：" + folder.getAbsolutePath());
                        break;
                    } else {
                        System.out.println("文件路径或文件格式错误！");
                    }
                }
            }
        }

        String keyword;
        System.out.print("\n是否使用配置关键词？(Y/N):");
        if (YES.equals(sc.next().toUpperCase())) {
            keyword = KEYWORD;
            System.out.println("配置关键词：" + keyword);
        } else {
            System.out.print("请输入关键词(以半角逗号隔开)：");
            keyword = sc.next().toUpperCase();
        }
        String[] keywordArray = keyword.split(",");

        String negaword;
        System.out.print("\n是否使用配置排除项？(Y/N):");
        if (YES.equals(sc.next().toUpperCase())) {
            negaword = NEGAWORD;
            System.out.println("配置排除项：" + negaword);
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
                System.out.println("\n在 " + folder + " 以及所有子文件夹中：\n\t\t查找对象：" + keywordArray[i] + "\n\t\t排除对象：" + negaword);
                System.out.println("\t查找了" + folderCounts + " 个文件(夹)，共找到 " + results[i].length + " 个符合条件的文件(夹)：");

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
            System.out.println("关键词：" + keyword + "错误！");
            return;
        }
    }

    private File[] searchFile(File folder, final String keyWord, final String[] negawordArray) {

        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                boolean negawordFlag = true;

                if (negawordArray != null && negawordArray.length > 0) {
                    for (String negaword : negawordArray) {
                        if (StringUtils.isNotBlank(negaword)) {
                            String fileName = negaword.contains(File.separator) ? pathname.getAbsolutePath() : pathname.getName().toLowerCase();
                            if (fileName.toLowerCase().contains(negaword.toLowerCase())) {
                                negawordFlag = false;
                                break;
                            }
                        }
                    }
                }

                if (negawordFlag) {
                    folderCounts++;
                    return true;
                } else {
                    return false;
                }
            }
        });

        List<File> result = new ArrayList<>();

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