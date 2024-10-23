package org.houses.utils;

import org.houses.House;

public class ConcreteHouse extends House {

    public ConcreteHouse(int id, String type, int area, int floors, double price) {
        super(id, type, area, floors, price);
    }

    @Override
    public String toString() {
        return "House ID: " + getId() + ", Type: " + getType() + ", Area: " + getArea() +
               ", Floors: " + getFloors() + ", Price: " + getPrice();
    }
}
