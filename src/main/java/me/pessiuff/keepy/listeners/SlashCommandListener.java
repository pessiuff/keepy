package me.pessiuff.keepy.listeners;

import me.pessiuff.keepy.KeepyBot;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

public class SlashCommandListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(final SlashCommandCreateEvent event) {
        final SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        final String commandName = interaction.getCommandName();
        KeepyBot.getCommandManager().getRegisteredCommands().forEach(targetCommand -> { // no check for duplicate commands cuz its done in command manager
            if (targetCommand.getName().equals(commandName)) targetCommand.execute(interaction);
        });
    }
}
