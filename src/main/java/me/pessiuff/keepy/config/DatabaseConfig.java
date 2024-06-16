package me.pessiuff.keepy.config;

import com.moandjiezana.toml.Toml;
import lombok.Getter;
import me.pessiuff.keepy.KeepyBot;

import java.io.File;

@Getter
public class DatabaseConfig {
    private String host;
    private String databaseName;
    private String userName;
    private String password;

    public boolean load() {
        final File file = new File("db-config.toml");
        if (!file.exists()) {
            KeepyBot.getLogger().error("Database config was not found.");
            return false;
        }

        final Toml toml = new Toml().read(file);

        host = toml.getString("database.host");
        if (host == null) {
            KeepyBot.getLogger().error("Couldn't find database.host in database config.");
            return false;
        }

        databaseName = toml.getString("database.name");
        if (databaseName == null) {
            KeepyBot.getLogger().error("Couldn't find database.name in database config.");
            return false;
        }

        userName = toml.getString("database.username");
        if (userName == null) {
            KeepyBot.getLogger().error("Couldn't find database.username in database config.");
            return false;
        }

        password = toml.getString("database.password");
        if (password == null) {
            KeepyBot.getLogger().error("Couldn't find database.password in database config.");
            return false;
        }

        KeepyBot.getLogger().info("Database config has loaded.");

        return true;
    }
}
