package me.pessiuff.keepy;

import lombok.Getter;
import me.pessiuff.keepy.commands.PingCommand;
import me.pessiuff.keepy.config.BotConfig;
import me.pessiuff.keepy.listeners.SlashCommandListener;
import me.pessiuff.keepy.manager.CommandManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: implement options to command manager

public class KeepyBot {
    @Getter
    private static final Logger logger = LoggerFactory.getLogger(KeepyBot.class);

    @Getter
    private static final BotConfig botConfig = new BotConfig();

    @Getter
    private static DiscordApi api;

    @Getter
    private static Server developmentServer;

    @Getter
    private static final CommandManager commandManager = new CommandManager();

    public static void main(String[] args) {
        if (!botConfig.load()) return;

        FallbackLoggerConfiguration.setDebug(true);

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

        commandManager.addCommand(new PingCommand());
        commandManager.registerAll();

        api.addSlashCommandCreateListener(new SlashCommandListener());
    }
}