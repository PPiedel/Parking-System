package pl.yahoo.pawelpiedel.Parking.domain.date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocalDateTimeValidator implements ConstraintValidator<LocalDateTimeConstraint, String> {
    private static final String LOCAL_DATE_TIME_REGEX = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2})\\:(\\d{2})\\:(\\d{2})";

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        return date != null && !date.isEmpty() && date.matches(LOCAL_DATE_TIME_REGEX);
    }
}
