package pl.yahoo.pawelpiedel.Parking.domain.payment;

import pl.yahoo.pawelpiedel.Parking.domain.parking.Parking;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;

    @Embedded
    private Money money;

    public Payment() { // required by Hibernate
    }

    public Payment(Parking parking, Money money) {
        this.parking = parking;
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) &&
                Objects.equals(parking, payment.parking) &&
                Objects.equals(money, payment.money);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, parking, money);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", parking=" + parking +
                ", money=" + money +
                '}';
    }
}
