package command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class StopRPS extends Command {

    public StopRPS() {
        this.name = "xrps";
        this.aliases = new String[] {"endrps", "stop-rps", "rpsdone"};
        this.help = "Use this command to end an rps session with this bot";
        this.category = new Category("Rock-Paper-Scissors");
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Stopping RPS for " + event.getAuthor().getName() + "...");

        if (RockPaperScissors.userPlayedMap.get(event.getAuthor()) != null &&
                RockPaperScissors.userScoreMap.get(event.getAuthor()) != null) {
            int userScore = RockPaperScissors.userScoreMap.get(event.getAuthor());
            int totalGames = RockPaperScissors.userPlayedMap.get(event.getAuthor());
            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(Color.YELLOW)
                    .setThumbnail("https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/0475c2ad8d201a8.png")
                    .setAuthor("Final Game Stats")
                    .setDescription(event.getAuthor().getName() + " won " + userScore + " times out " +
                            "of " + totalGames + " RPS games.");
            event.reply(eb.build());
            RockPaperScissors.userPlayedMap.put(event.getAuthor(), null);
            RockPaperScissors.userScoreMap.put(event.getAuthor(), null);
        }

    }
}
