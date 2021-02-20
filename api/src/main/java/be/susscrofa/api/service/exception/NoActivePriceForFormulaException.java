package be.susscrofa.api.service.exception;

public class NoActivePriceForFormulaException extends EntityConflictException {

    public NoActivePriceForFormulaException(String message) {
        super(message);
    }
}
