package com.example.SocialMediaApp.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class AvatarGenerator {

    private static final String[] COLORS = {
            "#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e",
            "#f39c12", "#e74c3c", "#d35400", "#c0392b", "#7f8c8d"
    };

    private static final Font CUSTOM_FONT;

    static {
        try (InputStream is = AvatarGenerator.class.getResourceAsStream("/fonts/Roboto-Bold.ttf")) {
            if (is == null) throw new RuntimeException("Font resource not found in /fonts");
            Font base = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
            CUSTOM_FONT = base.deriveFont(Font.BOLD, 100f);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed loading custom font: " + e.getMessage());
        }
    }

    public static byte[] generateAvatar(String initials) throws IOException {
        int size = 256;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        // headless-safe rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // random bg
        g.setColor(Color.decode(COLORS[new Random().nextInt(COLORS.length)]));
        g.fillRect(0, 0, size, size);

        // initials
        g.setFont(CUSTOM_FONT);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int x = (size - fm.stringWidth(initials)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(initials, x, y);

        g.dispose();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, "png", os);
        return os.toByteArray();
    }
}