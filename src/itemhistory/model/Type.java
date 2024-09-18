package itemhistory.model;

public enum Type {
    IN("IN"),
    OUT("OUT");

    private String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static String convertToString(Type type) {
        return type.getName();
    }
}