import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import command.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class BotRunner {
    public static void main(String[] args) throws Exception {
        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder builder = new CommandClientBuilder();

        // The default is "Type !!help"
        builder.setHelpWord("helpme");
        builder.setOwnerId("923391998289911869");

        // sets emojis used throughout the bot on successes, warnings, and failures
        builder.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");

        // sets the bot prefix
        builder.setPrefix("!");

        // adds commands
        builder.addCommands(new ServerInfo(), new Image(), new UserInfo(waiter),
                new RockPaperScissors(waiter), new StopRPS());


        // start getting a bot account set up
        String token = "OTIzMzkxOTk4Mjg5OTExODY5.YcPV0A.Bv4lMkWd4s4vgWKav1iYX_CwIg4";
        JDABuilder.createDefault(token)

                // set the game for when the bot is loading
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing("loading..."))

                // add the listeners
                .addEventListeners(waiter, builder.build())

                // start it up!
                .build();
    }
}