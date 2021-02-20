package be.susscrofa.api.web.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import be.susscrofa.api.service.exception.EntityConflictException;
import be.susscrofa.api.web.dto.ErrorMessageDto;
import be.susscrofa.api.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgument(IllegalArgumentException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(EntityNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<ExceptionResponse> resourceAlreadyExist(EntityConflictException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(String.valueOf(HttpStatus.CONFLICT.value()));
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessageDto processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return ErrorMessageDto.builder()
            .errors(this.processFieldErrors(fieldErrors))
                .build();
    }

    private List<String> processFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(this::resolveLocalizedErrorMessage)
                .collect(toList());
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();

        return this.messageSource.getMessage(fieldError, currentLocale);
    }
}
