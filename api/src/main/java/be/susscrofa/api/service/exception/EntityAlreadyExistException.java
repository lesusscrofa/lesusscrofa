package be.susscrofa.api.service.exception;

public class EntityAlreadyExistException extends EntityConflictException {

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
