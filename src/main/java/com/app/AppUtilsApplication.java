package com.app;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.app.config.Config;
import com.app.utils.DirDelUtil;
import com.app.utils.Zip7Util;

@SpringBootApplication
@EnableAutoConfiguration
public class AppUtilsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AppUtilsApplication.class, args);

        Config config = context.getBean(Config.class);

        System.out.println("\n******************************工具包******************************");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("\n\n\t1.删除文件夹\n\t2.破解密码\n请选择功能：");
            switch (sc.next()) {
                case "1":
                    DirDelUtil dirDelUtil = new DirDelUtil(config);
                    dirDelUtil.dirDel(sc);
                    break;
                case "2":
                    Zip7Util.zip7(sc);
                    break;
                default:
                    System.out.print("请输入正确的序号");
                    break;
            }

            System.out.print("\n\n是否退出？(Y/N):");
            if ("Y".equals(sc.next().toUpperCase())) {
                return;
            }
        }

    }
}