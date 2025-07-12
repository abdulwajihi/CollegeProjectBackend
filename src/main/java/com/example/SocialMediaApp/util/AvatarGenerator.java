package com.example.SocialMediaApp.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Random;

public class AvatarGenerator {

    private static final String[] COLORS = {
            "#1abc9c","#2ecc71","#3498db","#9b59b6","#34495e",
            "#f39c12","#e74c3c","#d35400","#c0392b","#7f8c8d"
    };

    private static final int FONT_SIZE = 100;
    private static final Font CUSTOM_FONT;

    static {
        Font loadedFont = null;
        String resourcePath = "fonts/Roboto-Bold.ttf";

        try {
            // 1) Resource URL चेक करें
            URL fontUrl = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(resourcePath);
            if (fontUrl == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            System.out.println("✅ Font resource URL: " + fontUrl);

            // 2) OpenStream से बाइट्स पढ़ें
            try (InputStream is = fontUrl.openStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int r;
                while ((r = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, r);
                }
                byte[] fontBytes = baos.toByteArray();
                System.out.println("✅ Font byte length: " + fontBytes.length);

                // 3) मैजिक बाइट्स चेक करें (पहले 4 बाइट्स)
                System.out.print("Magic bytes: ");
                for (int i = 0; i < Math.min(4, fontBytes.length); i++) {
                    System.out.printf("%02X ", fontBytes[i]);
                }
                System.out.println();

                // 4) Font.createFont
                loadedFont = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(fontBytes))
                        .deriveFont(Font.BOLD, FONT_SIZE);
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .registerFont(loadedFont);
                System.out.println("✅ Loaded custom font: " + loadedFont.getFontName());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed loading custom font (" + resourcePath + "): " + e.getMessage());
            // fallback
            loadedFont = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE);
            System.err.println("⚠️ Fallback font: " + loadedFont.getFontName());
        }

        CUSTOM_FONT = loadedFont;
    }

    /** initials के आधार पर PNG बाइट[] जनरेट करता है */
    public static byte[] generateAvatar(String initials) throws IOException {
        int size = 256;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // बैकग्राउंड
        g.setColor(Color.decode(COLORS[new Random().nextInt(COLORS.length)]));
        g.fillRect(0, 0, size, size);

        // टेक्स्ट
        g.setFont(CUSTOM_FONT);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int x = (size - fm.stringWidth(initials)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(initials, x, y);
        g.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        return os.toByteArray();
    }
}