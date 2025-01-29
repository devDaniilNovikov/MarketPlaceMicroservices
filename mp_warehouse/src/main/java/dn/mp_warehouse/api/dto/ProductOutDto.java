package dn.mp_warehouse.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOutDto {

    private String id;

    private String productName;

    private BigDecimal cost;

    private String description;

    private String category;

    private String brand;

    private String country;

    private Long count;

    private String buyerId;

    private String sellerId;
}
