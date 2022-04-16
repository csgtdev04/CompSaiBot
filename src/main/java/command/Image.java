package command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;

public class Image extends Command {

    public Image() {
        this.name = "image";
        this.arguments = "[width] [height] [image-url] [degrees to rotate(optional)]";
        this.help = "Manipulates image of your choice. Just put the width, height, url, and / or " +
                "rotation degrees.";
    }

    @Override
    protected void execute(CommandEvent e) {
        // check if they provide any arguments
        if (e.getArgs().equalsIgnoreCase("")) {
            e.reply("No arguments provided, " + e.getAuthor().getAsMention());
            e.reply("Format in this manner: " + "!image [width] [height] [image-url] [degrees to rotate(optional)]");
        } else {
            try {
                String[] args = e.getArgs().split(" ");
                int width = Integer.parseInt(args[0]);
                int height = Integer.parseInt(args[1]);
                URL imageURL = new URL(args[2]);
                int rotateAmount = args.length == 4 ? Integer.parseInt(args[3]) : 0;
                OutputStream os = new ByteArrayOutputStream();
                Thumbnails.of(imageURL).forceSize(width, height).rotate(rotateAmount).outputFormat("png").toOutputStream(os);
                byte[] imageInBytes = ((ByteArrayOutputStream) os).toByteArray();
                // https://images.unsplash.com/photo-1641160401272-94ca7afbd64f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80
                e.getChannel().sendFile(imageInBytes, "transformedimage.png").queue();

            } catch(Exception exception) {
                System.out.println("Exception occurred: " + exception);
            }

        }
    }
}
