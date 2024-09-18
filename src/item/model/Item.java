package item.model;

import exception.NotEnoughGoodToSellException;
import exception.QuantityValueMustBePositiveException;

import java.util.Objects;

public class Item {
    private int id;
    private String name;
    private int quantity;

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Item(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Item increaseQuantity(int quantity) {
        if (quantity < 0) {
            throw new QuantityValueMustBePositiveException();
        }
        return new Item(this.getName(), this.getQuantity() + quantity);
    }

    public Item decreaseQuantity(int quantity) {
        if (quantity < 0) {
            throw new QuantityValueMustBePositiveException();
        }

        if (this.getQuantity() <= quantity) {
            throw new NotEnoughGoodToSellException();
        }

        return new Item(this.getName(), this.getQuantity() - quantity);
    }

    public Item setQuantity(int quantity) {
        return new Item(this.getId(), this.getName(), quantity);
    }

    @Override
    public String toString() {
        return "Item {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}