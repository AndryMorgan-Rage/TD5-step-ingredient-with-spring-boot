package org.spring.ingredientmanagementwithspringboot.entity;

import lombok.*;

import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class StockMovement {
    private Integer id;
    private StockValue value;
    private org.spring.ingredientmanagementwithspringboot.entity.MovementTypeEnum type;
    private Instant creationDatetime;


}
