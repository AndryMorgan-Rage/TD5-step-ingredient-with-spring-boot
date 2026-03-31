package org.spring.ingredientmanagementwithspringboot.entity;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class StockListModel {
    private Integer id_ingredient;
    private Instant period;
    private double stock;
}
