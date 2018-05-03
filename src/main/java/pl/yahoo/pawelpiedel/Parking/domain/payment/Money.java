package pl.yahoo.pawelpiedel.Parking.domain.payment;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Embeddable
public final class Money {
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("PLN");
    private BigDecimal amount;
    private Currency currency;

    public Money() { //required by hibernate
    }

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money(BigDecimal amount) {
        this(amount, DEFAULT_CURRENCY);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
