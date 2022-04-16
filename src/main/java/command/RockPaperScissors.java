package command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RockPaperScissors extends Command {
    private EventWaiter eventWaiter;
    private static int score;
    private static int timesPlayed;
    protected static HashMap<User, Integer> userScoreMap;
    protected static HashMap<User, Integer> userPlayedMap;
    protected static boolean played;

    static {
        userScoreMap = new HashMap<>();
        userPlayedMap = new HashMap<>();
        played = true;
    }

    public RockPaperScissors(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
        this.score = 0;
        this.timesPlayed = 0;
        this.name = "rps";
        this.aliases = new String[] {"rockpaperscissors"};
        this.help = "Play Rock, Paper, Scissors (R, P, S)";
        this.category = new Category("Rock-Paper-Scissors");
    }

    @Override
    protected void execute(CommandEvent event) {
        // checks if that person is playing for their first time, provides instructions if so.
        userIntro(event);

        event.reply("Enter your choice [R for Rock, P for paper, S for scissors]");
        eventWaiter.waitForEvent(GuildMessageReceivedEvent.class,
                e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()),
                e -> {
                    String computerPick = generateRandomComputerPick();
                    event.reply("I chose " + computerPick + ".\nYou chose " + e.getMessage().getContentRaw());
                    int status = didUserWin(e.getMessage().getContentRaw(), computerPick);
                    String thumbnail = "https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/bab1d848afdd8b0.png";
                    if (status != -1) {
                        if (userScoreMap.get(e.getAuthor()) == null) {
                            userScoreMap.put(e.getAuthor(), 0);
                        }
                        if (userPlayedMap.get(e.getAuthor()) == null) {
                            userPlayedMap.put(e.getAuthor(), 0);
                        }
                        if (status == 0) {
                            event.reply("Haha! I won!");
                            thumbnail = "https://pixelartmaker-data-78746291193.nyc3" +
                                    ".digitaloceanspaces.com/image/f227001d02cd1ad.png";
                        }
                        else if (status == 1) {
                            event.reply(":(, you won!");
                            userScoreMap.put(e.getAuthor(),
                                    userScoreMap.getOrDefault(e.getAuthor(), 0) + 1);
                        } else if (status == 2) {
                            event.reply("We chose the same thing, TIE!");
                            thumbnail = "https://pixelartmaker-data-78746291193.nyc3" +
                                    ".digitaloceanspaces.com/image/e425ea2738829ab.png";
                        }
                        userPlayedMap.put(e.getAuthor(),
                                userPlayedMap.getOrDefault(e.getAuthor(), 0) + 1);
                    } else event.reply("Invalid input");

                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.BLUE)
                            .setThumbnail(thumbnail)
                            .setAuthor("Game stats for " + e.getAuthor().getName(), "https://www" +
                                            ".google.com",
                                    e.getAuthor().getAvatarUrl())
                            .addField("Number of wins: ",
                                    Integer.toString(userScoreMap.get(e.getAuthor())), true)
                            .addField("Total number of games: ",
                                    Integer.toString(userPlayedMap.get(e.getAuthor())), true);
                    event.reply(eb.build());
                    // add role to user if they won RPS more than 5 times in current session
                    final int TIMES_TO_WIN_RPS_FOR_ROLE = 5;
                    if (userScoreMap.get(event.getAuthor()) >= TIMES_TO_WIN_RPS_FOR_ROLE) {
                        Role rpsMaster = event.getGuild().getRoleById("927687799140937759");
                        event.getGuild().addRoleToMember(event.getMember().getUser().getId(),
                                rpsMaster).queue();
                        EmbedBuilder embedBuilder = new EmbedBuilder()
                                .setColor(Color.MAGENTA)
                                .setThumbnail("https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/a7868e1d3c0e6a1.png")
                                .setAuthor("Congrats " + event.getAuthor().getName() + "!")
                                .setDescription("You have received a " + rpsMaster.getName() + " " +
                                        "role. Continue to play more games and earn roles!");

                        event.reply(embedBuilder.build());
//                        guide.modifyMemberRoles(event.getMember(),
//                                event.getGuild().getRoleById("927687799140937759")).queue();
                    }
                }, 20, TimeUnit.SECONDS, () -> event.reply("You didn't give me a choice! Try " +
                        "again!"));
    }

    private void userIntro(CommandEvent event) {
        if (this.userPlayedMap.get(event.getAuthor()) == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setThumbnail("https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/0475c2ad8d201a8.png")
                    .setAuthor("Welcome to Rock, Paper, Scissors " + event.getAuthor().getName(),
                            "https://www.google.com", event.getAuthor().getAvatarUrl())
                    .setDescription("Type !xrps to end the current RPS session. Good luck!");
            event.reply(embedBuilder.build());
        }
    }

    private int didUserWin(String userPick, String computerPick) {
        int playerChoice = covertChoiceToNum(userPick);
        int computerChoice = covertChoiceToNum(computerPick);
        int res = -1;
        if(computerChoice == playerChoice) {
            res = 2;
            System.out.println("We picked the same thing! This round is a draw.");
        }
        else if(computerChoice == 1 && playerChoice == 2) {
            res = 1;
            System.out.println("PAPER covers ROCK. You win.");
        }
        else if(computerChoice == 1 && playerChoice == 3) {
            res = 0;
            System.out.println("ROCK breaks SCISSORS. I win.");
        }
        else if(computerChoice == 2 && playerChoice == 1) {
            res = 0;
            System.out.println("PAPER covers ROCK. I win.");
        }
        else if(computerChoice == 2 && playerChoice == 3) {
            res = 1;
            System.out.println("SCISSORS cut PAPER. You win.");
        }
        else if(computerChoice == 3 && playerChoice == 1) {
            res = 1;
            System.out.println("ROCK breaks SCISSORS. You win.");
        }
        else if(computerChoice == 3 && playerChoice == 2) {
            res = 0;
            System.out.println("SCISSORS cut PAPER. I win.");
        }

        return res;
    }

    private int covertChoiceToNum(String choice) {
        int res = -1;
        if (choice.equalsIgnoreCase("R")) res = 1;
        else if (choice.equalsIgnoreCase("P")) res = 2;
        else if (choice.equalsIgnoreCase("S")) res = 3;

        return res;
    }

    private String generateRandomComputerPick() {
        final Random rng = new Random();
        String[] choices = {"R", "P", "S"};
        return choices[rng.nextInt(3)];
    }

    public HashMap<User, Integer> getUserPlayedMap() {
        return userPlayedMap;
    }

    public HashMap<User, Integer> getUserScoreMap() {
        return userScoreMap;
    }
}
