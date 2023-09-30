package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserMyDetailsService;

@Component
public class UserValidator implements Validator {

    private final UserMyDetailsService userMyDetailsService;

    @Autowired
    public UserValidator(UserMyDetailsService userMyDetailsService) {
        this.userMyDetailsService = userMyDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

//    @Override
//    public void validate(Object target, Errors errors) {
//        User user = (User) target;
//        String username = user.getUsername();
//
//            ValidationUtils.rejectIfEmpty(errors, "username", "Empty name", "Empty name");
//        if( userMyDetailsService.loadUserByUsername(username)!=null){
//            errors.rejectValue("username", "Username is already taken", "Username is already taken");
//        }
//    }


    @Override
    public void validate(Object o, Errors errors) {
        User person = (User) o;

        try {
            userMyDetailsService.loadUserByUsername(person.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return; // все ок, пользователь не найден
        }

        errors.rejectValue("username", "", "Человек с таким именем пользователя уже существует");
    }
}
