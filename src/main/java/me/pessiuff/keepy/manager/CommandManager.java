package me.pessiuff.keepy.manager;

import lombok.Getter;
import me.pessiuff.keepy.KeepyBot;
import me.pessiuff.keepy.commands.BaseCommand;
import org.javacord.api.interaction.SlashCommandBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class CommandManager {
    private final List<BaseCommand> registeredCommands = new ArrayList<>();

    public void addCommand(final BaseCommand command) {
        if (registeredCommands.contains(command)) {
            KeepyBot.getLogger().warn("Tried to register an existing command. ({})", command.getName());
            return;
        }

        for (final BaseCommand targetCommand : registeredCommands) {
            if (targetCommand.getName().equals(command.getName())) {
                KeepyBot.getLogger().warn("Tried to register a command with existing name. ({})", command.getName());
                return;
            }
        }

        registeredCommands.add(command);
    }

    public void registerAll() {
        final Set<SlashCommandBuilder> builders = new HashSet<>();
        registeredCommands.forEach(command -> builders.add(
                new SlashCommandBuilder()
                        .setName(command.getName())
                        .setDescription(command.getDescription())
                        .setOptions(command.getOptions())
        ));

        KeepyBot.getApi().bulkOverwriteServerApplicationCommands(KeepyBot.getDevelopmentServer(), builders);

        KeepyBot.getLogger().info("{} command has been registered.", registeredCommands.size());
    }
}
