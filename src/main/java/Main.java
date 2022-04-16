import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        String token = "OTIzMzkxOTk4Mjg5OTExODY5.YcPV0A.Bv4lMkWd4s4vgWKav1iYX_CwIg4";
        JDA jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new Main());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("JJJJ");
        System.out.println("Author: " + event.getAuthor()); // gives author object
        System.out.println("Received message from " + event.getAuthor().getName());
        System.out.println("Message: " + event.getMessage().getContentDisplay());
        if (event.getMessage().getContentRaw().equals("!play")) {
            event.getChannel().sendMessage("hi").queue();
        }
        String ans = "R";
        if (event.getMessage().getContentRaw().equals("!P")) {
            event.getChannel().sendMessage("You won!").queue();
        }


    }
}
