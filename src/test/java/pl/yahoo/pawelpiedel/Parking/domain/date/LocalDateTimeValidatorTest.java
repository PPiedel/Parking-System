package pl.yahoo.pawelpiedel.Parking.domain.date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class LocalDateTimeValidatorTest {
    @Autowired
    private LocalDateTimeValidator localDateTimeValidator;

    @MockBean
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void isValid_ValidDateTimeWithSecondsPassed_TrueReturned() {
        //given
        String date = "2018-05-02T18:20:37";

        //when
        boolean isValid = localDateTimeValidator.isValid(date, constraintValidatorContext);

        //then
        assertTrue(isValid);

    }

    @Test
    public void isValid_ValidDateTimeWithFullPrecisionPassed_TrueReturned() {
        //given
        String date = "2018-05-02T18:20:37.999999999";

        //when
        boolean isValid = localDateTimeValidator.isValid(date, constraintValidatorContext);

        //then
        assertTrue(isValid);

    }

    @Test
    public void isValid_OnlyDatePassed_FalseReturned() {
        //given
        String date = "2018-05-02";

        //when
        boolean isValid = localDateTimeValidator.isValid(date, constraintValidatorContext);

        //then
        assertFalse(isValid);

    }

    @Test
    public void isValid_DateWithOnlyMinutesPassed_FalseReturned() {
        //given
        String date = "2018-05-02T15:27";

        //when
        boolean isValid = localDateTimeValidator.isValid(date, constraintValidatorContext);

        //then
        assertFalse(isValid);

    }

    @Test
    public void isValid_WrongFormatPassed_FalseReturned() {
        //given
        String date = "2018/05/02T18:20:37";

        //when
        boolean isValid = localDateTimeValidator.isValid(date, constraintValidatorContext);

        //then
        assertFalse(isValid);

    }

    @Test
    public void isValid_DateTimeWithoutTLetterInsisePassed_FalseReturned() {
        //given
        String date = "2018/05/02 18:20:00";

        //when
        boolean isValid = localDateTimeValidator.isValid(date, constraintValidatorContext);

        //then
        assertFalse(isValid);

    }


    @TestConfiguration
    static class LocalDateTImeValidatorTest {
        @Bean
        LocalDateTimeValidator localDateTimeValidator() {
            return new LocalDateTimeValidator();
        }
    }
}