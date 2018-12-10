package ru.sbt.rgrtu.gol.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Board {

    @Id
    @GeneratedValue
    private int id;
    private int generation;
    private int x;
    private int y;
    private int alive;
    private String userID;

    public void setId(int id) {
        this.id = id;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAlive(int alive) {
        this.alive = alive;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public int getGeneration() {
        return generation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAlive() {
        return alive;
    }

    public String getUserID() {
        return userID;
    }
}
