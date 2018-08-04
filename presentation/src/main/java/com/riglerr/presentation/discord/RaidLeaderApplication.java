package com.riglerr.presentation.discord;


import com.riglerr.data.DiscordMessager;
import com.riglerr.data.controllers.DiscordMessageController;
import com.riglerr.data.controllers.RaidAlertCommandModule;
import com.riglerr.data.repositories.InMemoryAlertRepository;
import com.riglerr.domain.usecases.CreateRaidAlertUseCase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class RaidLeaderApplication {
    private JDA bot;
    private JDABuilder botBuilder;

    public static void main(String[] args) {
        try {
            startNewBotInstance(args[0]);
        } catch (LoginException lEx) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startNewBotInstance(String token) throws LoginException {
        var instance = new RaidLeaderApplication().setup(token);
        instance.start();
    }

    private RaidLeaderApplication setup(String token) {
        botBuilder = new JDABuilder(AccountType.BOT).setToken(token);
        setupModules();
    }

    private void setupModules() {
        var commandController = new DiscordMessageController(new RaidAlertCommandModule());
        botBuilder.addEventListener(new MessageListener(commandController));
    }

    private DiscordMessageController addModules() {
        var alertRepo = new InMemoryAlertRepository();
        var discordMessager = new DiscordMessager();
        var createAlertUseCase = new CreateRaidAlertUseCase(alertRepo, discordMessager);
        var raidAlertModule = new RaidAlertCommandModule();
        return new DiscordMessageController();
    }

    private void start() throws LoginException {
        bot = botBuilder.buildAsync();
    }
}
