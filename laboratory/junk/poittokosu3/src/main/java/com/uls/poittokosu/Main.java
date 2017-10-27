package com.uls.poittokosu;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.uls.poittokosu.app.template.KosuTemplateService;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static int year = 2017;
    private static int month = 10;

    public static void main(String[] args) throws IOException {
        logger.info("------- Program Start -------");

        // 入力Parameterを解析
        if (!parseParameterArgs(args)) {
            logger.info("------- Program Finish Fatal -------");
            return;
        }

        // Spring Containerの初期化
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        KosuTemplateService app = ctx.getBean(KosuTemplateService.class);

        // サービス実行
        try {
            app.doService(year, month);
        } catch (Exception e) {
            logger.error("An error has occurred", e);
            logger.info("------- Program Finish Fatal -------");
            ctx.close();
            return;
        }

        ctx.close();
        logger.info("------- Program Finish Normal -------");
    }

    /** システム引数をチェック */
    private static boolean parseParameterArgs(String[] args) {
        if (args.length < 2) {
            logger.error("[Poiっと工数] パラメーターを入力してください : Parameter is needed");
            return false;
        }
        try {
            year = Integer.valueOf(args[0]);
            month = Integer.valueOf(args[1]);
        } catch (Exception e) {
            logger.error("[Poiっと工数] パラメーターが不正です : Your input parameter is invalid", e);
            return false;
        }
        if (year < 2010 || 2040 < year) {
            logger.error("[Poiっと工数] YEARは2010年～2040年にしてください : Your input parameter is invalid");
            return false;
        }
        if (month < 1 || 12 < month) {
            logger.error("[Poiっと工数] MONTHは1月～12月です : Your input parameter is invalid");
            return false;
        }

        logger.info("指定年度 ： {}", year);
        logger.info("指定月 ： {}", month);
        return true;
    }

}
