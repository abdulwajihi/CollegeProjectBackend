package com.example.SocialMediaApp.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class AvatarGenerator {

    private static final String[] COLORS = {
            "#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e",
            "#f39c12", "#e74c3c", "#d35400", "#c0392b", "#7f8c8d"
    };

    public static byte[] generateAvatar(String initials) throws IOException {
        int size = 256;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        // Choose random background color
        String hexColor = COLORS[new Random().nextInt(COLORS.length)];
        g2.setColor(Color.decode(hexColor));
        g2.fillRect(0, 0, size, size);

        // Draw initials in white, centered
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 100));
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(initials)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(initials, x, y);
        g2.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
