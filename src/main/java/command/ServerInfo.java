package command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;

public class ServerInfo extends Command {

    public ServerInfo() {
        this.name = "serverinfo";
        this.aliases = new String[] {"server"};
        this.help = "Gives info about server";
    }

    @Override
    protected void execute(CommandEvent e) {
        String[] members = new String[e.getGuild().getMembers().size()];
        for (int i = 0; i < members.length; i++) {
            members[i] = e.getGuild().getMembers().get(i).getEffectiveName();
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLUE);
        eb.setAuthor(e.getGuild().getName());
        eb.addField("Server Owner: ", e.getGuild().getOwner().getEffectiveName(), true);
        eb.addField("Member Count: ", Integer.toString(members.length), true);
        eb.setDescription("**Members:** \n" + Arrays.toString(members) + "\n **Invite Link:** " + "https://discord.gg/eT4uREz3k7");


        e.getChannel().sendMessage(eb.build()).queue();
    }
}
