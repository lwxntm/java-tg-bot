package com.github.lwxntm;

import org.telegram.abilitybots.api.bot.AbilityBot;

/**
 * @author lei
 */
public class CBot extends AbilityBot {
    protected CBot(String botToken, String botUsername) {
        super(botToken, botUsername);
    }

    @Override
    public int creatorId() {
        return 0;
    }
}
