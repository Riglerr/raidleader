package com.riglerr.presentation.discord;

import com.riglerr.data.command.CommandStore;
import com.riglerr.data.DiscordMessenger;
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
    private DiscordMessenger messenger;

    public static void main(String[] args) {
        try {
            new RaidLeaderApplication(args[0])
                    .initializeRepositories()
                    .start()
                    .initializeMessenger()
                    .addModules();

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

    private RaidLeaderApplication initializeMessenger() {
        messenger = new DiscordMessenger(bot);
        return this;
    }

    private RaidLeaderApplication addModules() {
        CommandStore commandStore = new CommandStore();
        addRaidAlertModules(commandStore);
//        botBuilder.addEventListener(new MessageListener(commandStore));
        botBuilder.addEventListener(new Module());
        return this;
    }

    private void addRaidAlertModules(CommandStore commandStore) {
        var createUseCase = new CreateRaidAlertUseCase(alertRepository, messenger);
        commandStore.addCommand("!", "raidalert", "add", createUseCase);
    }

    private RaidLeaderApplication start() throws LoginException, InterruptedException {
        bot = botBuilder.buildBlocking();
        return this;
    }
}
