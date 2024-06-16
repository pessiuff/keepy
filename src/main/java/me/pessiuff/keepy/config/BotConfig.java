package me.pessiuff.keepy.config;

import lombok.Getter;
import me.pessiuff.keepy.KeepyBot;

@Getter
public class BotConfig {
    private String token;
    private String developerGuildId;

    public boolean load() {
        token = System.getenv("DISCORD_TOKEN");
        if (token == null) {
            KeepyBot.getLogger().error("DISCORD_TOKEN environment variable was not found.");
            return false;
        }

        developerGuildId = System.getenv("DEVELOPER_GUILD_ID");
        if (developerGuildId == null) {
            KeepyBot.getLogger().error("DEVELOPER_GUILD_ID environment variable was not found.");
            return false;
        }

        return true;
    }
}
