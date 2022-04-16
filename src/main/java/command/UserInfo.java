package command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserInfo extends Command {
    private EventWaiter eventWaiter;

    public UserInfo(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
        super.name = "user-info";
        super.help = "Get user info";
        super.aliases = new String[] {"userinfo", "ui"};
        super.category = new Category("Members");
        super.cooldown = 10;
        super.arguments = "[name]";
    }

//    @Override
//    protected void execute(CommandEvent e) {
//        if (e.getArgs().isEmpty()) {
//            e.reply("Format in this manner: !user-info [name]");
//        } else {
//            Member name;
//            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd-yyyy");
//            try {
//                name = e.getMessage().getMentionedMembers().get(0);
//                EmbedBuilder eb = new EmbedBuilder()
//                        .setColor(Color.RED)
//                        .setThumbnail("https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/53affeb5b7325fc.png")
//                        .setAuthor("Information on " + name.getUser().getName(), "https://www.google" +
//                                ".com", name.getUser().getAvatarUrl())
//                        .setDescription(name.getUser().getName() + " joined on " + name.getTimeJoined().format(fmt) + " :clock:")
//                        .addField("Status: ", name.getOnlineStatus().toString(), true)
//                        .addField("Nickname: ", name.getNickname() == null ? "No nickname" :
//                                name.getNickname(), true)
//                        .addField("Roles: ", getRolesAsString(name.getRoles()), true);
//
//                e.reply(eb.build());
//                e.reply(e.getAuthor().getAsMention() + " there you go!");
//            } catch (IndexOutOfBoundsException ex) {
//                System.out.println("Exception occurred");
//                e.reply("You need to provide the name as a mention!");
//            }
//
//
//        }
//    }

    @Override
    protected void execute(CommandEvent e) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        e.reply("What's the name of the user (as a mention)?");

        eventWaiter.waitForEvent(GuildMessageReceivedEvent.class,
                e1 -> e1.getAuthor().equals(e.getAuthor()) && e1.getChannel().equals(e.getChannel()), e1 -> {
                    try {
                        Member name = e1.getMessage().getMentionedMembers().get(0);
                        EmbedBuilder eb = new EmbedBuilder()
                                .setColor(Color.RED)
                                .setThumbnail("https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/53affeb5b7325fc.png")
                                .setAuthor("Information on " + name.getUser().getName(), "https://www.google" +
                                        ".com", name.getUser().getAvatarUrl())
                                .setDescription(name.getUser().getName() + " joined on " + name.getTimeJoined().format(fmt) + " :clock:")
                                .addField("Status: ", name.getOnlineStatus().toString(), true)
                                .addField("Nickname: ", name.getNickname() == null ? "No nickname" :
                                        name.getNickname(), true)
                                .addField("Roles: ", getRolesAsString(name.getRoles()), true);

                        e.reply(eb.build());
                        e.reply(e.getAuthor().getAsMention() + " there you go!");
                    } catch (IndexOutOfBoundsException ex) {
                        System.out.println("Exception occurred");
                        e.reply("You need to provide the name as a mention!");
                    }
                }, 10, TimeUnit.SECONDS, () -> e.reply("You didn't send a name! Try again!"));


    }

    private String getRolesAsString(List rolesList) {
        String roles = "";
        if (!rolesList.isEmpty()) {
            Role tempRole = (Role) rolesList.get(0);
            roles += tempRole.getName();
            for (int i = 1; i < rolesList.size(); i++) {
                tempRole = (Role) rolesList.get(i);
                roles += ", " + tempRole.getName();
            }
        } else {
            return "No Roles";
        }
        return roles;
    }
}
