package me.pessiuff.keepy;

import lombok.Getter;
import me.pessiuff.keepy.commands.BalanceCommand;
import me.pessiuff.keepy.config.BotConfig;
import me.pessiuff.keepy.config.DatabaseConfig;
import me.pessiuff.keepy.listeners.SlashCommandListener;
import me.pessiuff.keepy.manager.CommandManager;
import me.pessiuff.keepy.manager.DatabaseManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class KeepyBot {
    @Getter
    private static final Logger logger = LoggerFactory.getLogger(KeepyBot.class);

    @Getter
    private static final BotConfig botConfig = new BotConfig();

    @Getter
    private static final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Getter
    private static DatabaseManager databaseManager;

    @Getter
    private static DiscordApi api;

    @Getter
    private static Server developmentServer;

    @Getter
    private static final CommandManager commandManager = new CommandManager();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (!botConfig.load()) return; // try to load the bot config from environment variables
        if (!databaseConfig.load()) return; // try to load database config from db-config.toml

        databaseManager = new DatabaseManager(databaseConfig); // construct databasemanager using database config
        if (!databaseManager.initializeDatabase()) return; // initialize the database & connect to it

        // FallbackLoggerConfiguration.setDebug(true);

        api = new DiscordApiBuilder()
                .setToken(botConfig.getToken())
                .setAllNonPrivilegedIntents()
                .login()
                .join();

        if (api.getServerById(botConfig.getDeveloperGuildId()).isEmpty()) {
            logger.error("Development guild id is incorrect.");
            return;
        }
        developmentServer = api.getServerById(botConfig.getDeveloperGuildId()).get();

        commandManager.addCommand(new BalanceCommand());
        commandManager.registerAll();

        api.addSlashCommandCreateListener(new SlashCommandListener());


    }
}