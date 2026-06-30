package com.hotel.booking.model;
import com.hotel.booking.model.Enums.PaymentMethod;
import com.hotel.booking.model.Enums.Status;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column
    private double amountToBePaid;


    @Column
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column
    private Status paymentStatus;


    @Enumerated(EnumType.STRING)
    @Column
    private PaymentMethod paymentMethod;

    //Stripe web hook updates the record using the payment intent ID
    @Column(nullable = false, unique = true)
    private String paymentIntentID;


    public Payment(){}

    public Payment(PaymentMethod paymentMethod, Status paymentStatus,LocalDate paymentDate, double amountToBePaid, Booking booking) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.amountToBePaid = amountToBePaid;
        this.booking = booking;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Status getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Status paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmountToBePaid() {
        return amountToBePaid;
    }

    public void setAmountToBePaid(double amountToBePaid) {
        this.amountToBePaid = amountToBePaid;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentIntentID() {
        return paymentIntentID;
    }

    public void setPaymentIntentID(String paymentIntentID) {
        this.paymentIntentID = paymentIntentID;
    }
}
