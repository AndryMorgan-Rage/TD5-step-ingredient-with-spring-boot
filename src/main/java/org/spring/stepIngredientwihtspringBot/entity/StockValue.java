package org.spring.stepIngredientwihtspringBot.entity;

import lombok.*;
import org.spring.stepIngredientwihtspringBot.entity.Enum.UnitType;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class StockValue {
    private double quantity;
    private UnitType unit;

}
