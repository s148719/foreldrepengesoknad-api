package no.nav.foreldrepenger.selvbetjening.felles.attachments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.SlowTests;
import org.junit.Test;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.PDFPageSplitter;
import org.junit.experimental.categories.Category;

@Category(SlowTests.class)
public class PageSplitterTest {

    @Test
    public void testPages() throws Exception {
        List<byte[]> pages = new PDFPageSplitter().split("pdf/spring-framework-reference.pdf");
        assertEquals(798, pages.size());
        for (byte[] page : pages) {
            assertTrue(ImageByteArray2PDFConverterTest.isPdf(page));
        }
    }
}