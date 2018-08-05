package com.riglerr.presentation.discord;


import com.riglerr.data.CommandStore;
import com.riglerr.data.DiscordMessager;
import com.riglerr.data.controllers.RaidAlertCommandModule;
import com.riglerr.data.interfaces.CommandModule;
import com.riglerr.data.repositories.InMemoryAlertRepository;
import com.riglerr.domain.interfaces.AlertRepository;
import com.riglerr.domain.usecases.CreateRaidAlertUseCase;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class RaidLeaderApplication {
    private JDA bot;
    private JDABuilder botBuilder;
    private AlertRepository alertRepository;
    private DiscordMessager messager;

    public static void main(String[] args) {
        try {
            new RaidLeaderApplication(args[0])
                    .initializeRepositories()
                    .start()
                    .initializeMessager()
                    .addModules();

        } catch (LoginException lEx) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RaidLeaderApplication(String token) {
        botBuilder = new JDABuilder(AccountType.BOT).setToken(token);
    }

    private RaidLeaderApplication initializeRepositories() {
        alertRepository = new InMemoryAlertRepository();
        return this;
    }

    private RaidLeaderApplication initializeMessager() {
        messager = new DiscordMessager(bot);
        return this;
    }

    private RaidLeaderApplication addModules() {
        CommandStore commandStore = new CommandStore();
        addRaidAlertModules(commandStore);
        bot.addEventListener(new MessageListener(commandStore, messager));
        return this;
    }

    private void addRaidAlertModules(CommandStore commandStore) {
        var createUseCase = new CreateRaidAlertUseCase(alertRepository, messager);
        commandStore.addCommand("!", "raidalert", "add", createUseCase);
    }

    private RaidLeaderApplication start() throws LoginException, InterruptedException {
        bot = botBuilder.buildBlocking();
        return this;
    }
}
