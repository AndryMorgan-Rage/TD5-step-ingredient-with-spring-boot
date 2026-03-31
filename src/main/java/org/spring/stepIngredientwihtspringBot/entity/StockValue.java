package org.spring.stepIngredientwihtspringBot.entity;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class StockValue {
    private double quantity;
    private UnitType unit;

}
