package Model;

import Controller.GameManager;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Enemy extends GameObject {

    static GameManager gm = GameManager.getInstance();
    private boolean hasTitanium;
    private boolean hasBonus;
    public int type;

    public int getType() {
        return type;
    }

    public Enemy(){

        speed = 1;
        curDirection = 1;

        int rand = (int) (Math.random() * 3);

        if(rand == 0){
            hasBonus = true;
        }
        if(rand == 1){
            hasTitanium = true;
        }

    }
    public boolean hTitanium() {
    	return hasTitanium;
    }
    public boolean hBonus() {
    	return hasBonus;
    }
    public void move(int direction){
        //if direction = 0 move UP
        //if direction = 1 move DOWN
        //if direction = 2 move LEFT
        //if direction = 3 move RIGHT

        if(direction == 0){
            this.y += speed/10000;
        }
        if(direction == 1){
            this.y -= speed/10000;
        }
        if(direction == 2){
            this.x -= speed/10000;
        }
        if(direction == 3){
            this.x += speed/10000;
        }
    }
    /*
    public void shoot(){
        gm.getBulletList()[gm.getBulletIndex()].setCurDirection(this.curDirection);
        gm.getBulletList()[gm.getBulletIndex()].setX(this.x);
        gm.getBulletList()[gm.getBulletIndex()].setY(this.y);
        gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(true);
        gm.getBulletList()[gm.getBulletIndex()].setActive(true);
        gm.increaseBulletIndex();
    }*/
}