package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends Exception {
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
