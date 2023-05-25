package org.vitaliistf;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.vitaliistf.controllers.DispatcherController;

public class BlockTracker extends TelegramLongPollingBot {

    private static BlockTracker INSTANCE;

    private BlockTracker(){
    }

    public static BlockTracker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockTracker();
        }
        return INSTANCE;
    }

    @Override
    public String getBotUsername() {
        return AppConfiguration.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return AppConfiguration.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            try {
                message.setText(new DispatcherController().handle(update.getMessage(), message));
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            TelegramLongPollingBot bot = new BlockTracker();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
