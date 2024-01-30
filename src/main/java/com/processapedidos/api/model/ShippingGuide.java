package com.processapedidos.api.model;

import com.processapedidos.api.enums.ShippingGuideTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ShippingGuide implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @OneToOne
    private CustomerOrder customerOrder;

    private LocalDate shippingDate;

    @Enumerated(EnumType.STRING)
    private ShippingGuideTypeEnum type;
}
