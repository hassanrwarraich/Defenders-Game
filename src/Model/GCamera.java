package Model;

import Controller.Game;
import com.sun.management.GcInfo;

public class GCamera extends GameObject {
    private float x,y;

    public GCamera(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void tick(Player player){
        x = player.getX() - 500;

    }

    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public float getX(float x){
        return x;
    }
    public float getY(float y){
        return y;
    }

}