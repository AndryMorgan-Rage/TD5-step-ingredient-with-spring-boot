package org.spring.stepIngredientwihtspringBot.repository.implement;

import org.spring.stepIngredientwihtspringBot.datasource.Datasource;
import org.spring.stepIngredientwihtspringBot.entity.DishIngredient;
import org.spring.stepIngredientwihtspringBot.repository.DishIngredientRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DishIngredientRepositoryImplement implements DishIngredientRepository {
    private final Datasource datasource;
    public DishIngredientRepositoryImplement(Datasource datasource) {
        this.datasource = datasource;
    }
    @Override
    public void attachIngredient(int dishId, List<DishIngredient> dishIngredient) {
        String sql = """
                INSERT INTO dishingredient (id_dish, id_ingredient, quantity_required, unit)
                VALUES (?, ?, ? , ?::unit_type)
                """;
        Connection conn = null;
        try{
            conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
            for(DishIngredient di : dishIngredient){
                stmt.setInt(1, dishId);
                stmt.setInt(2, di.getIngredient().getId());
                stmt.setDouble(3,di.getQuantity_required());
                stmt.setString(4, di.getUnit().name());
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if(conn != null){
                datasource.closeConnection(conn);
            }
        };
    }

    @Override
    public void detachIngredient(int dishId) {
        String sql = """
                DELETE from dishingredient
                where id_dish = ?
                """;
        try(Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ){
            stmt.setInt(1, dishId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
