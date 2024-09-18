package item.repository;

import common.CRUDRepository;
import item.model.Item;

import java.util.*;
import java.sql.*;

public class ItemRepository implements CRUDRepository<Item> {
    private final Connection connection;

    public ItemRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();

        try {
            String sql = "SELECT id, name, quantity FROM item ORDER BY id";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");

                Item item = new Item(id, name, quantity);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public int checkItem(String name) {
        String sql = "SELECT COUNT(*) FROM item WHERE UPPER(name) = %UPPER(?)%";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int add(Item t) {
        String sql = "INSERT INTO item (name, quantity) VALUES (?, ?)";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, t.getName());
            statement.setInt(2, t.getQuantity());

            int insertedRow = statement.executeUpdate();
            if (insertedRow > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int update(Item t) {
        String sql = "UPDATE item SET name = ?, quantity = ? WHERE id = ?";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, t.getName());
            statement.setInt(2, t.getQuantity());
            statement.setInt(3, t.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM item WHERE id = ?";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setInt(1, id);

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Optional<Item> findItemby(int id) {
        String sql = "SELECT id, name, quantity FROM item WHERE id = ?";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int quantity = resultSet.getInt("quantity");

                    Item item = new Item(id, name, quantity);
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}