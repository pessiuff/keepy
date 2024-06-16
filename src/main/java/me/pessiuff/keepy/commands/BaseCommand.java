package me.pessiuff.keepy.commands;

import org.javacord.api.interaction.SlashCommandInteraction;

public interface BaseCommand {
    String getName();
    String getDescription();
    void execute(final SlashCommandInteraction interaction);
}
