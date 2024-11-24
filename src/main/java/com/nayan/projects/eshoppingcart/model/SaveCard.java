package com.nayan.projects.eshoppingcart.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class SaveCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "saved_card_generator")
    @SequenceGenerator(name = "saved_card_generator", sequenceName = "saved_card_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDtls user;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "last_four_digits")
    private String lastFourDigits;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "expiry_date")
    private String expiryDate;
    
    @Column(name = "cvv")
    private String cvv;

    @Column(name = "card_token")
    private String cardToken;

    @Column(name = "is_default", columnDefinition = "TINYINT(1)")
    private Boolean isDefault;
    
    @Column(name = "is_save", columnDefinition = "TINYINT(1)")
    private Boolean isSave;
}
