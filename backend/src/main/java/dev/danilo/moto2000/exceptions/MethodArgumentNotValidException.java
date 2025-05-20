package dev.danilo.moto2000.exceptions;

import dev.danilo.moto2000.dto.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MethodArgumentNotValidException extends RuntimeException {
    private final Response response;
}
