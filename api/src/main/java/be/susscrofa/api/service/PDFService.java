package be.susscrofa.api.service;

public interface PDFService {
    byte[] createPdf(String templateName, Object parameters);
}
