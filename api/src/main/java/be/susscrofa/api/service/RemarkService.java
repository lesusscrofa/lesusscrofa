package be.susscrofa.api.service;

import be.susscrofa.api.model.Remark;
import be.susscrofa.api.repository.RemarkRepository;
import be.susscrofa.api.repository.RemarkWithFoodOrderViewRepository;
import be.susscrofa.api.view.DailyOrderRemarkView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RemarkService {

    private final RemarkRepository remarkRepository;

    private final RemarkWithFoodOrderViewRepository remarkWithFoodOrderViewRepository;

    public List<DailyOrderRemarkView> findRemarks(LocalDate day) {
        return remarkWithFoodOrderViewRepository.findAllByDay(day);
    }

    public Remark getRemark(Long clientId, LocalDate day) {
        return remarkRepository.findByClientIdAndDay(clientId, day)
                .orElse(createEmptyRemark(clientId, day));
    }

    public Remark save(Long clientId, LocalDate day, String message) {
        var remark = remarkRepository.findByClientIdAndDay(clientId, day)
                .orElseGet(Remark::new);

        remark.setClientId(clientId);
        remark.setDay(day);
        remark.setMessage(message);

        return remarkRepository.save(remark);
    }

    @Transactional
    public Remark delete(Long clientId, LocalDate day) {
        remarkRepository.deleteByClientIdAndDay(clientId, day);

        return createEmptyRemark(clientId, day);
    }

    private Remark createEmptyRemark(Long clientId, LocalDate day) {
        return new Remark(null, clientId, day, "");
    }
}
