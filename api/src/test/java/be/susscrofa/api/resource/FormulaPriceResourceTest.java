package be.susscrofa.api.resource;

import be.susscrofa.api.model.FormulaPrice;
import be.susscrofa.api.model.Formula;
import be.susscrofa.api.repository.FormulaPriceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FormulaPriceResourceTest extends AbstractResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FormulaPriceRepository formulaPriceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Rollback
    @Transactional
    void getAllActives() throws Exception {
        formulaPriceRepository.deleteAll();

        var bp1 = FormulaPrice
                .builder()
                .start(LocalDate.now().minusDays(2))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(3.4))
                .build();
        var bp2 = FormulaPrice
                .builder()
                .start(LocalDate.now())
                .formula(Formula.DISH)
                .price(BigDecimal.valueOf(2.4))
                .build();
        var bpNotActiveAnymore = FormulaPrice
                .builder()
                .start(LocalDate.now().plusDays(2))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(1.3))
                .build();
        var bpNotYetActive = FormulaPrice
                .builder()
                .start(LocalDate.now().minusDays(4))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(1.4))
                .build();

        bp1 = formulaPriceRepository.save(bp1);
        bp2 = formulaPriceRepository.save(bp2);
        formulaPriceRepository.save(bpNotActiveAnymore);
        formulaPriceRepository.save(bpNotYetActive);

        this.mockMvc.perform(get("/api/formulas/prices/actives"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", hasItem(bp1.getId().intValue())))
                .andExpect(jsonPath("$[*].id", hasItem(bp2.getId().intValue())));
    }

    @Test
    @Rollback
    @Transactional
    void getAllByFormula() throws Exception {
        formulaPriceRepository.deleteAll();

        var bp1 = FormulaPrice
                .builder()
                .start(LocalDate.now().minusDays(2))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();
        var bp2 = FormulaPrice
                .builder()
                .start(LocalDate.now())
                .formula(Formula.DISH)
                .price(BigDecimal.valueOf(10.3))
                .build();
        var bp3 = FormulaPrice
                .builder()
                .start(LocalDate.now().plusDays(2))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();
        var bp4 = FormulaPrice
                .builder()
                .start(LocalDate.now().minusDays(4))
                .formula(Formula.MENU)
                .price(BigDecimal.valueOf(10.3))
                .build();

        bp1 = formulaPriceRepository.save(bp1);
        formulaPriceRepository.save(bp2);
        bp3 = formulaPriceRepository.save(bp3);
        bp4 = formulaPriceRepository.save(bp4);

        this.mockMvc.perform(get("/api/formulas/prices?formula={formula}", Formula.MENU.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].id", hasItem(bp1.getId().intValue())))
                .andExpect(jsonPath("$[*].id", hasItem(bp3.getId().intValue())))
                .andExpect(jsonPath("$[*].id", hasItem(bp4.getId().intValue())));
    }

    @Test
    @Rollback
    @Transactional
    void getAllByFormula_invalidPAram() throws Exception {
        formulaPriceRepository.deleteAll();

        this.mockMvc.perform(get("/api/formulas/prices"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @Transactional
    void create() throws Exception {
        formulaPriceRepository.deleteAll();

        var start = LocalDate.now();
        var formula = Formula.DISH;
        var price = BigDecimal.valueOf(2.3);

        var bp = FormulaPrice
                .builder()
                .start(start)
                .formula(formula)
                .price(price)
                .build();

        this.mockMvc.perform(post("/api/formulas/prices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bp)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("start").value(start.toString()))
                .andExpect(jsonPath("formula").value(formula.toString()))
                .andExpect(jsonPath("price").value(price));
    }

    @Test
    @Rollback
    @Transactional
    void update() throws Exception {
        formulaPriceRepository.deleteAll();

        var start = LocalDate.now();
        var formula = Formula.DISH;
        var price = BigDecimal.valueOf(10.3);

        var bp = FormulaPrice
                .builder()
                .start(start)
                .formula(formula)
                .price(price)
                .build();

        bp = formulaPriceRepository.save(bp);

        var updatedBp = FormulaPrice
                .builder()
                .start(start.plusDays(1))
                .formula(formula)
                .price(price.add(BigDecimal.ONE))
                .build();

        this.mockMvc.perform(put("/api/formulas/prices/{bpId}", bp.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBp)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("start").value(updatedBp.getStart().toString()))
                .andExpect(jsonPath("formula").value(formula.toString()))
                .andExpect(jsonPath("price").value(updatedBp.getPrice().doubleValue()));
    }

    @Test
    @Rollback
    @Transactional
    void deleteFp() throws Exception {
        formulaPriceRepository.deleteAll();

        var start = LocalDate.now();
        var formula = Formula.DISH;
        var price = BigDecimal.valueOf(10.3);

        var bp = FormulaPrice
                .builder()
                .start(start)
                .formula(formula)
                .price(price)
                .build();

        bp = formulaPriceRepository.save(bp);

        var stilActiveBp = FormulaPrice
                .builder()
                .start(start.minusDays(1))
                .formula(formula)
                .price(price)
                .build();

        formulaPriceRepository.save(stilActiveBp);

        this.mockMvc.perform(delete("/api/formulas/prices/{bpId}", bp.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Rollback
    @Transactional
    void deleteFp_ConflictNoActivePriceForFormula() throws Exception {
        formulaPriceRepository.deleteAll();

        var start = LocalDate.now();
        var formula = Formula.DISH;
        var price = BigDecimal.valueOf(10.3);

        var bp = FormulaPrice
                .builder()
                .start(start)
                .formula(formula)
                .price(price)
                .build();

        bp = formulaPriceRepository.save(bp);

        this.mockMvc.perform(delete("/api/formulas/prices/{bpId}", bp.getId()))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
