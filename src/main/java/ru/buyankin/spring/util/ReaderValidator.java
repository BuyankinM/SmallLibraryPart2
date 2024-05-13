package ru.buyankin.spring.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.buyankin.spring.models.Reader;
import ru.buyankin.spring.services.ReadersService;

@Component
public class ReaderValidator implements Validator {

    private final ReadersService readersService;

    @Autowired
    public ReaderValidator(ReadersService readersService) {
        this.readersService = readersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Reader.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reader reader = (Reader) target;

        // Проверка уникальности ФИО
        if (readersService.checkUniqueName(reader).isPresent())
            errors.rejectValue("name", "duplicate", "Человек с таким ФИО уже занесен в базу");
    }
}
