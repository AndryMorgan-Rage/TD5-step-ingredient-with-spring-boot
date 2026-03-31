package org.spring.stepIngredientwihtspringBot.entity;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class StockMovement {
    private Integer id;
    private StockValue value;
    private org.spring.stepIngredientwihtspringBot.entity.MovementTypeEnum type;
    private Instant creationDatetime;


}
