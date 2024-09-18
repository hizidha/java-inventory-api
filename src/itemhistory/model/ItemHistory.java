package itemhistory.model;

import item.model.Item;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class ItemHistory {
    private int id;
    private int itemId;
    private Item item;
    private int quantity;
    private Type type;
    private Timestamp createAt;

    public ItemHistory(Item item, int quantity, Type type) {
        this.item = item;
        this.quantity = quantity;
        this.type = type;
    }

    public ItemHistory(int id, int itemId, Type type, int quantity) {
        this.id = id;
        this.itemId = itemId;
        this.type = type;
        this.quantity = quantity;
    }

    public ItemHistory(int id, int itemId, Type type, int quantity, Timestamp createAt) {
        this.id = id;
        this.itemId = itemId;
        this.type = type;
        this.quantity = quantity;
        this.createAt = createAt;
    }

    public static ItemHistory createForm(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int itemId = resultSet.getInt("item_id");
        Type type = Type.valueOf(resultSet.getString("type"));
        int quantity = resultSet.getInt("quantity");
        Timestamp createdAt = resultSet.getTimestamp("created_at");

        return new ItemHistory(id, itemId, type, quantity, createdAt);
    }

    public static ItemHistory createInbound(Item item, int quantity) {
        return new ItemHistory(item, quantity, Type.IN);
    }

    public static ItemHistory createOutbound(Item item, int quantity) {
        return new ItemHistory(item, quantity, Type.OUT);
    }

    public int getId() {
        return this.id;
    }

    public int getItemId() {
        return this.itemId;
    }

    public int getItemQuantity() {
        return this.quantity;
    }

    public String convertTypeToString() {
        return Type.convertToString(this.type);
    }

    @Override
    public String toString() {
        return "ItemHistory{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", timestamp=" + createAt +
                '}';
    }
}