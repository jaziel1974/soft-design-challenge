package com.cooperative.pollsystem.model.exceptions;

public class DuplicatedIdException extends BusinessException{
    public DuplicatedIdException(String message) {
        super(message);
    }
}
