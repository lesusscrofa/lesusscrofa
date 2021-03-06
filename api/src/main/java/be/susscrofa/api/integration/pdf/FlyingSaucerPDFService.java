package be.susscrofa.api.integration.pdf;

import be.susscrofa.api.integration.template.TemplateProcessor;
import be.susscrofa.api.service.PDFService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FlyingSaucerPDFService implements PDFService {

    private final TemplateProcessor templateProcessor;

    public byte[] createPdf(@NonNull String templateName, Object parameters) {
        final var processedHtml = templateProcessor.process(templateName, parameters);
        try (var os = new ByteArrayOutputStream()) {
            final var renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os, true);
            renderer.finishPDF();
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Cannot open FileOutputStream", e);
        }
    }
}
