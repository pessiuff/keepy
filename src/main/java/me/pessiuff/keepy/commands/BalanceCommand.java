package me.pessiuff.keepy.commands;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.List;

public class BalanceCommand implements BaseCommand {
    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Check balance of other people or yourself.";
    }

    @Override
    public List<SlashCommandOption> getOptions() {
        return List.of(
                SlashCommandOption.create(SlashCommandOptionType.USER, "user", "Specify a user if you want to check their balance.", false)
        );
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        final User user = interaction.getUser();
        User targetUser = interaction.getUser();

        final List<SlashCommandInteractionOption> options = interaction.getOptions();

        if (!options.isEmpty()) {
            final SlashCommandInteractionOption userOption = options.getFirst();
            if (userOption.getUserValue().isPresent()) targetUser = userOption.getUserValue().get();
        }

        interaction.createImmediateResponder()
                .setContent(String.format("```You are checking %s's balance.```", targetUser.getDiscriminatedName()))
                .setFlags(MessageFlag.EPHEMERAL)
                .respond();
    }
}
