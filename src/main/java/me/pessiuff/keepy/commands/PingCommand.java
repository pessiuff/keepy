package me.pessiuff.keepy.commands;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;

public class PingCommand implements BaseCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Pong!";
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder()
                .setContent("```Pong!```")
                .setFlags(MessageFlag.EPHEMERAL)
                .respond();
    }
}
