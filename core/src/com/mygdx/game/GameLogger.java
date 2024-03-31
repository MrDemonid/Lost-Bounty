package com.mygdx.game;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class GameLogger {

    static Logger logger;

    public static void init(String cl)
    {
        logger = Logger.getLogger(cl);
        try {
            FileHandler f = new FileHandler("log.txt");
            logger.addHandler(f);
            SimpleFormatter frm = new SimpleFormatter();
            f.setFormatter(frm);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void info(String str)
    {
        logger.info(str);
    }

}
