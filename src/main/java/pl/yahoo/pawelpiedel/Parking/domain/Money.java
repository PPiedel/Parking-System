package pl.yahoo.pawelpiedel.Parking.domain;

import javax.persistence.Embeddable;
import java.math.BigInteger;
import java.util.Currency;
import java.util.Objects;

@Embeddable
public final class Money {
    public static final Currency DEFAULT_CURRENCY = Currency.getInstance("PLN");
    private final BigInteger amount;
    private final Currency currency;

    public Money(BigInteger amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    private Money(BigInteger amount) {
        this(amount, DEFAULT_CURRENCY);
    }

    public BigInteger getAmount() {
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
