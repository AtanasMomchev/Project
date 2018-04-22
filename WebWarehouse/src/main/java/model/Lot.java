package model;

public class Lot {
    private int id;
    private int size;
    private double weight;

    public Lot(int id, int size, double weight) {
        this.id = id;
        this.size = size;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}