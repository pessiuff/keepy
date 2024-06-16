package me.pessiuff.keepy.manager;

import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder;
import com.github.jasync.sql.db.pool.ConnectionPool;
import lombok.Getter;
import me.pessiuff.keepy.KeepyBot;
import me.pessiuff.keepy.config.DatabaseConfig;
import org.javacord.api.entity.user.User;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// TODO: fix shit code

@Getter
public class DatabaseManager {
    private final DatabaseConfig databaseConfig;

    private Connection databaseConnection;

    public DatabaseManager(final DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public boolean initializeDatabase() {
        if (databaseConnection != null) {
            KeepyBot.getLogger().warn("Tried initializing database when already initialized.");
            return false;
        }

        final ConnectionPool<MySQLConnection> pool = MySQLConnectionBuilder.createConnectionPool(
                String.format(
                        "jdbc:mysql://%s/%s?user=%s&password=%s",
                        databaseConfig.getHost(),
                        databaseConfig.getDatabaseName(),
                        databaseConfig.getUserName(),
                        databaseConfig.getPassword()
                )
        );

        try {
            databaseConnection = pool.connect().get();
        } catch (InterruptedException | ExecutionException  e) {
            KeepyBot.getLogger().error("There was a problem while initializing the database: {}", e.getMessage());
            return false;
        }

        KeepyBot.getLogger().info("Database has been initialized. Connection status: {}", databaseConnection.isConnected());

        return true;
    }

    private boolean registerToDatabase(final User user) {
        final CompletableFuture<QueryResult> resultFuture = databaseConnection.sendPreparedStatement(String.format("INSERT INTO economy VALUES (%s, 0)", user.getId()));
        final QueryResult result;
        try {
            result = resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            KeepyBot.getLogger().error("There was a problem while registering a user to database.");
            return false;
        }

        return true;
    }

    public double getBalance(final User user) {
        final CompletableFuture<QueryResult> resultFuture = databaseConnection.sendPreparedStatement(String.format("SELECT * FROM economy WHERE id='%s'", user.getId()));
        final QueryResult result;
        try {
            result = resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            KeepyBot.getLogger().error("There was a problem while requesting balance data from database.");
            return 0;
        }

        final RowData userData;
        try {
            userData = result.getRows().getFirst();
        } catch (NoSuchElementException e) {
            registerToDatabase(user);
            return 0;
        }

        final Double balance = userData.getDouble("balance");

        if (balance == null) {
            KeepyBot.getLogger().error("{} has no balance data in database(?)", user.getDiscriminatedName());
            return 0;
        }

        return balance;
    }
}
