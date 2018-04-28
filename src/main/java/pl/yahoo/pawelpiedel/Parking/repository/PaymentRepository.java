package pl.yahoo.pawelpiedel.Parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.yahoo.pawelpiedel.Parking.domain.payment.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
