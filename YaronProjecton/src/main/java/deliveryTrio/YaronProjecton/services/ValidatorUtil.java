package deliveryTrio.YaronProjecton.services;

import deliveryTrio.YaronProjecton.exceptions.InvalidInputException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidatorUtil{
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> boolean isValid(T arg) throws InvalidInputException{
        Set<ConstraintViolation<T>> violations = validator.validate(arg);
        if (violations.isEmpty())
            return true;
        String msg = "";
        String invalids = "";
        for (ConstraintViolation<T> violation : violations) {
            msg += String.format("-- %s\n", violation.getMessage());
            invalids += violation.getInvalidValue() + " ";
        }
        throw new InvalidInputException(invalids, msg);
    }
}
