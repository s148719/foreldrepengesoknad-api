package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import static java.util.stream.Collectors.toList;
import static org.apache.pdfbox.rendering.ImageType.RGB;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentConversionException;

@Component
public class DefaultPDF2ImageConverter implements PDF2ImageConverter {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPDF2ImageConverter.class);

    @Override
    public List<byte[]> convertToImages(List<byte[]> pdfPages) {
        LOG.info("Konverterer {} PDF-side(r) til bilde(r)", pdfPages.size());
        return pdfPages.stream()
                .map(DefaultPDF2ImageConverter::toBufferedImage)
                .map(DefaultPDF2ImageConverter::toBytes)
                .collect(toList());
    }

    private static BufferedImage toBufferedImage(byte[] page) {
        LOG.info("Konverterer {} bytes til image", page.length);
        try (PDDocument document = PDDocument.load(page)) {
            return new PDFRenderer(document).renderImageWithDPI(0, 300, RGB);
        } catch (IOException e) {
            LOG.warn("Kunne ikke konvertere PDF til image", e);
            throw new AttachmentConversionException("Kunne ikke konvertere PDF til bilde", e);
        }
    }

    private static byte[] toBytes(BufferedImage img) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.setUseCache(false);
            ImageIO.write(img, "jpg", baos);
            LOG.info("Konverterte bilde til {}", img);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            LOG.warn("Kunne ikke konvertere image til bytes", e);
            throw new AttachmentConversionException("Kunne ikke konvertere bilde til bytes", e);
        }
    }
}
