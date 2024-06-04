package com.dam_proyecto.inventario.model;

/**
 * Category entity used for persistence as a database table.
 * It is related to Product entity.
 *
 * @author Isabel María González Rodríguez
 */
public class Category {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
