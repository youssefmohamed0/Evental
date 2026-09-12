package joe.app.EventReservationApp.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import joe.app.EventReservationApp.DTO.PaymentRequestDTO;
import joe.app.EventReservationApp.Enum.PaymentMethod;
import joe.app.EventReservationApp.Enum.PaymentStatus;
import joe.app.EventReservationApp.Enum.ReservationStatus;
import joe.app.EventReservationApp.Enum.SeatStatus;
import joe.app.EventReservationApp.Model.Payment;
import joe.app.EventReservationApp.Model.Reservation;
import joe.app.EventReservationApp.Model.ReservationItem;
import joe.app.EventReservationApp.Repository.ReservationRepository;

@Service
public class PaymentService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    public String processPayment(UUID reservationId, PaymentRequestDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation not found with ID: " + reservationId));

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(reservation.getTotalPrice());
        payment.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        payment.setStatus(PaymentStatus.PENDING);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Reservation is not in PENDING status");
        }

        switch (request.getPaymentMethod()) {
            case "CASH":
                if (request.getCashAmount() == null) {
                    throw new IllegalArgumentException("Cash amount is required for cash payment");
                }
                if (request.getCashAmount().compareTo(reservation.getTotalPrice()) < 0) {
                    throw new IllegalArgumentException("Cash amount is less than total price");
                }
                break;
            case "CREDIT_CARD":
            case "DEBIT_CARD":
                validateCardDetails(request.getCardNumber(), request.getCardExpiry(), request.getCardCvv());
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment method");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.COMPLETED);

        reservation.setPayment(payment);

        bookReservationSeats(reservation);

        reservationRepository.save(reservation);
        return "Payment processed successfully";
    }

    private void bookReservationSeats(Reservation reservation) {
        for (ReservationItem item : reservation.getItems()) {
            item.getSeat().setStatus(SeatStatus.BOOKED);
        }
    }

    private void validateCardDetails(String cardNumber, String cardExpiry, String cardCvv) {

        if (cardNumber == null || cardNumber.isBlank() || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid Card Number");
        }

        if (cardExpiry == null || cardExpiry.isBlank()) {
            throw new IllegalArgumentException("Invalid Card Expiry");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryDate = YearMonth.parse(cardExpiry.trim(), formatter);
            YearMonth currentMonth = YearMonth.now();

            if (expiryDate.isBefore(currentMonth)) {
                throw new IllegalArgumentException("Card has expired");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Card Expiry format (Expected MM/YY)");
        }

        if (cardCvv == null || cardCvv.isBlank() || cardCvv.length() != 3) {
            throw new IllegalArgumentException("Invalid Card Cvv");
        }
    }

}
