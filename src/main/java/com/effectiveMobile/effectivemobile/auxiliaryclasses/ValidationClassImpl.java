package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationClassImpl implements ValidationClass{

    private String validationString;

    private Integer validationInteger;

    @Override
    public Optional<ValidationClassImpl> validEmailOrId(String line){
        boolean resultMatch;

        Pattern pattern = Pattern.compile(ConstantsClass.REGEX_ONLY_NUMBERS);
        Matcher matcher = pattern.matcher(line);
        resultMatch = matcher.find();

        if (resultMatch){
            return Optional.of(new ValidationClassImpl(Integer.valueOf(line)));
        }

        pattern = Pattern.compile(ConstantsClass.REGEX_EMAIL);
        matcher = pattern.matcher(line);
        resultMatch = matcher.find();

        if (resultMatch){
            return Optional.of(new ValidationClassImpl(line));
        }

        return Optional.empty();
    }

    public ValidationClassImpl(Integer validationId) {
        this.validationInteger = validationId;
    }

    public ValidationClassImpl(String validationEmail) {
        this.validationString = validationEmail;
    }
}
