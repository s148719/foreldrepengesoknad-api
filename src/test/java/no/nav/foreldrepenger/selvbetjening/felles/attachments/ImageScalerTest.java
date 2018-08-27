package no.nav.foreldrepenger.selvbetjening.felles.attachments;

import no.nav.foreldrepenger.selvbetjening.FastTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@Category(FastTests.class)
public class ImageScalerTest {

    @Test
    public void imgSmallerThanA4RemainsUnchanged() throws Exception {
        URL url = getClass().getResource("/pdf/jks.jpg");
        byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        byte[] scaled = ImageScaler.downToA4(orig, "jpg");
        assertEquals(scaled.length, orig.length);
    }

    @Test
    public void imgBiggerThanA4IsScaledDown() throws Exception {
        URL url = getClass().getResource("/pdf/rdd.png");
        byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        byte[] scaled = ImageScaler.downToA4(orig, "jpg");
        BufferedImage origImg = fromBytes(orig);
        BufferedImage scaledImg = fromBytes(scaled);
        assertTrue(scaledImg.getWidth() < origImg.getWidth());
        assertTrue(scaledImg.getHeight() < origImg.getHeight());
    }

    @Test
    public void scaledImgHasRetainedFormat() throws Exception {
        URL url = getClass().getResource("/pdf/rdd.png");
        final byte[] orig = Files.readAllBytes(Paths.get(url.toURI()));
        final byte[] scaled = ImageScaler.downToA4(orig, "jpg");
        assertTrue(hasJpgSignature(scaled));
    }

    public boolean hasJpgSignature(byte[] bytes) {
        return (bytes[0] & 0XFF) == 0xFF &&
                (bytes[1] & 0XFF) == 0xD8 &&
                (bytes[0] & 0XFF) == 0xFF;
    }

    private BufferedImage fromBytes(byte[] bytes) {
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(in);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
