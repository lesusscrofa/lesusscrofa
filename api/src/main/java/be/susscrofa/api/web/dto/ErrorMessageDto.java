package be.susscrofa.api.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ErrorMessageDto {

    private List<String> errors;
}
