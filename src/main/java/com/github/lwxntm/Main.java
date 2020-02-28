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
