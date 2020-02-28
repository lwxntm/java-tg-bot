package com.github.lwxntm;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;

/**
 * @author lei
 */
public class Main {
    static String javaToken;

    static {
        javaToken = System.getenv("javaToken");
        if (javaToken == null || javaToken.isEmpty()) {
            System.out.println("未检测到javaToken, 使用作者私人bot......");
            javaToken = "1008706432:AAFt2_Yq2SDYaeasZ_mEu1Xj5rAq7RYK-Ko";
        }
    }

    public static void main(String[] args) throws IOException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MyBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
