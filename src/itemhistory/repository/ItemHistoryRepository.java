package itemhistory.repository;

import common.CRUDRepository;
import itemhistory.model.ItemHistory;

import java.util.*;
import java.sql.*;

public class ItemHistoryRepository implements CRUDRepository<ItemHistory> {
    private final Connection connection;

    public ItemHistoryRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<ItemHistory> getAll() {
        List<ItemHistory> itemHistories = new ArrayList<>();
        String sql = "SELECT id, item_id, quantity, type, created_at FROM item_history";

        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                ItemHistory itemHistory = ItemHistory.createForm(resultSet);
                itemHistories.add(itemHistory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemHistories;
    }

    @Override
    public int add(ItemHistory t) {
        String sql = "INSERT INTO item_history (item_id, type, quantity) VALUES (?, ?, ?)";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, t.getItemId());
            statement.setString(2, t.convertTypeToString());
            statement.setInt(3, t.getItemQuantity());

            int insertedRow = statement.executeUpdate();
            if (insertedRow > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int update(ItemHistory t) {
        String sql = "UPDATE item_history SET item_id = ?, type = ?, quantity = ?, created_at = ? WHERE id = ?";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setInt(1, t.getItemId());
            statement.setString(2, t.convertTypeToString());
            statement.setInt(3, t.getItemQuantity());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setInt(5, t.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM item_history WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Optional<ItemHistory> findItemby(int id) {
        String sql = "SELECT id, item_id, type, quantity, created_at FROM item_history WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ItemHistory.createForm(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}