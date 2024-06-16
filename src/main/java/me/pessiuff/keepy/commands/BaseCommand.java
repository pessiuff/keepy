package me.pessiuff.keepy.commands;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;

public interface BaseCommand {
    String getName();
    String getDescription();
    List<SlashCommandOption> getOptions();
    void execute(final SlashCommandInteraction interaction);
}
