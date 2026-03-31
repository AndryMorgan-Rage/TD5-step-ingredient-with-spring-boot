package org.spring.ingredientmanagementwithspringboot.entity;

import lombok.*;
import org.spring.ingredientmanagementwithspringboot.entity.Enum.UnitType;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class StockValue {
    private double quantity;
    private UnitType unit;

}
