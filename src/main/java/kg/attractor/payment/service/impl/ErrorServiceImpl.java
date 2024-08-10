package kg.attractor.payment.service.impl;

import kg.attractor.payment.errors.ErrorResponseBody;
import kg.attractor.payment.service.ErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ErrorResponseBody makeResponse(Exception exception) {
        String errorMessage = exception.getMessage();
        return ErrorResponseBody.builder()
                .error(errorMessage)
                .reasons(Map.of("errors", List.of(errorMessage)))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(BindingResult bindingResult) {
        log.info("Validation error");
        Map<String, List<String>> reasons = new HashMap<>();
        bindingResult.getFieldErrors().stream()
                .filter(e -> e.getDefaultMessage() != null)
                .forEach(e -> {
                    List<String> errors = new ArrayList<>();
                    errors.add(e.getDefaultMessage());
                    if (!reasons.containsKey(e.getField())) {
                        reasons.put(e.getField(), errors);
                    } else {
                        reasons.get(e.getField()).addAll(errors);
                    }
                });

        return ErrorResponseBody.builder()
                .error("Validation error")
                .reasons(reasons)
                .build();
    }
}
