package be.susscrofa.api.service;

import be.susscrofa.api.model.Bill;
import be.susscrofa.api.model.Client;
import be.susscrofa.api.model.OrderSummary;
import be.susscrofa.api.repository.OrderWithPriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;

@Service
@AllArgsConstructor
public class BillService {

    private final ClientService clientService;

    private final OrderWithPriceRepository orderWithPriceRepository;

    private final PDFService pdfService;

    public byte[] getBill(long clientId, LocalDate from, LocalDate to) {
        Client client = clientService.getClient(clientId);

        return pdfService.createPdf("invoice.xhtml", Bill.createBill(client, from, to, this.orderFactory(client)));
    }

    private BiFunction<LocalDate, LocalDate, List<OrderSummary>> orderFactory(Client client) {
        return (from, to) -> orderWithPriceRepository.findOrdersSummaries(client.getId(), from, to);
    }
}
