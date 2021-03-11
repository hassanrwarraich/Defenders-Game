package Model;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public abstract class GameObject {

    public float x;
    public float y;
    public int curDirection;
    public float speed;
    public Image img;
    public boolean active;

    GameObject(){
        active = false;
    }

    public void setCurDirection(int curDirection) {
        this.curDirection = curDirection;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public int getCurDirection() {
        return curDirection;
    }

    public float getSpeed() {
        return speed;
    }

    public Image getImg() {
        return img;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value){
        active = value;
    }
}