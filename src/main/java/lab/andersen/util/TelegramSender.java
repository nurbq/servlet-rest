package lab.andersen.util;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.File;

public class TelegramSender {
    private static final String BOT_TOKEN = PropertiesUtils.get("tg.bot.token");
    private static final String CHAT_ID = PropertiesUtils.get("tg.bot.chat.id");
    private static final TelegramBot BOT;

    static {
        BOT = new TelegramBot(BOT_TOKEN);
    }

    public static void sendMessage(String message) {
        BOT.execute(new SendMessage(CHAT_ID, message));
    }

    public static void sendPDF(File pdf) {
        BOT.execute(new SendDocument(CHAT_ID, pdf));
    }
}
