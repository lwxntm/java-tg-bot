package com.github.lwxntm;


import com.github.lwxntm.handler.FibHandler;
import com.github.lwxntm.handler.IpHandler;
import com.github.lwxntm.handler.PrimeHandler;
import com.github.lwxntm.handler.TodoListOfBot;
import com.github.lwxntm.util.FileInfo;
import com.github.lwxntm.util.FileStr;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author lei
 */
public class MyBot extends TelegramLongPollingBot {
    private static boolean readForAdd = false;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().toLowerCase();
            String returnMsg = "";
            if (readForAdd) {
                try {
                    TodoListOfBot.add(text);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    readForAdd = false;
                    returnMsg = "已经添加！";
                }
            } else if (text.startsWith("del")) {
                String[] args = text.split(" ");
                if (args.length > 1) {
                    TodoListOfBot.del(args[1]);
                }
                returnMsg = "第" + args[1] + "条已经删除";
            } else if (text.startsWith("fib")) {
                String[] args = text.split(" ");
                if (args.length > 1) {
                    returnMsg = FibHandler.fibI(Integer.parseInt(args[1]));
                }
            } else if (text.startsWith("prime")) {
                String[] args = text.split(" ");
                if (args.length > 1) {
                    try {
                        returnMsg = PrimeHandler.primeI(Integer.parseInt(args[1]));
                    } catch (NumberFormatException e) {
                        returnMsg = e.toString();
                    }
                }
            } else if (text.startsWith("ip")) {
                String[] args = text.split(" ");
                if (args.length > 1) {
                    returnMsg = IpHandler.handleRawStr(args[1]);
                }
            } else {
                switch (text.toLowerCase()) {
                    case "mem":
                        try {
                            Properties properties = new Properties();
                            properties.load(new FileInputStream("/proc/meminfo"));
                            returnMsg = properties.getProperty("Active");
                        } catch (IOException e) {
                            returnMsg = e.toString();
                        }
                        break;
                    case "list":
                        returnMsg = TodoListOfBot.list();
                        break;
                    case "add":
                        readForAdd = true;
                        returnMsg = "请输入您要添加的待办事项";
                        break;
                    case "time":
                        try {
                            returnMsg = FileStr.readFileToString("pom.xml2", StandardCharsets.UTF_8);

                        } catch (IOException e) {
                            System.out.println(e.toString());
                        }
                        break;
                    case "size":
                        String pathname = ".";
                        returnMsg = FileInfo.sizeOfDirFriendly(new File(pathname));
                        break;
                    default:
                        returnMsg = text;
                }
            }

            if (returnMsg.isEmpty()) {
                returnMsg = "没有可以回复的内容";
            }
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText(returnMsg);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "☕️bot";
    }

    @Override
    public String getBotToken() {
        return Main.javaToken;
    }

}
