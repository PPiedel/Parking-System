package pl.yahoo.pawelpiedel.Parking.domain.date;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocalDateTimeValidator.class)
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateTimeConstraint {
    String message() default "Invalid date time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
