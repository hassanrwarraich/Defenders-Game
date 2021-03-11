package Model;

import Controller.GameManager;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Player extends GameObject {
    private int maxLives;
    private int lives;
    private int weapon;
    private int shootCooldown = 5;
    static GameManager gm = GameManager.getInstance();
    private boolean hasShield;
    private int titaniumCount;

    public void increaseTitanium() {
        this.titaniumCount = this.titaniumCount + 300;
    }

    public void decreaseTitanium() {
        this.titaniumCount = this.titaniumCount - 300;
    }

    public int getTitaniumCount() {
        return titaniumCount;
    }
    public int getShootCooldown() {
        return shootCooldown;
    }
    public Player(){
        maxLives = 4;
        hasShield = false;
        lives = 3;
        speed = 1;
        curDirection = 1;
        weapon = 0;
        try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipRight4.png")) {
            img = new Image(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shoot(){

    }

    public void decreaseLife(){
        lives = lives - 1;
    }

    public int getLives(){
        return lives;
    }

    public int getMaxLives(){
        return maxLives;
    }

    public void setMaxLives(int maxLives){ this.maxLives = maxLives;}

    public int getWeapon(){return  weapon;}

    public void setWeapon(int weapon){this.weapon = weapon;}

    public boolean getHasShield() {
        return hasShield;
    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public void move(int direction, int speed){

        //if direction = 0 move UP
        //if direction = 1 move DOWN
        //if direction = 2 move LEFT
        //if direction = 3 move RIGHT

        //curDirection 0 for Left
        //curDirection 1 for Right

        if(direction == 0){
            this.y -= speed;
        }
        if(direction == 1){
            this.y += speed;
        }
        if(direction == 2){
            if(this.curDirection == 1){
                if(!hasShield) {
                    try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipLeft4.png")) {
                        img = new Image(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(hasShield) {
                    try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipLeftShield.png")) {
                        img = new Image(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                this.x -= speed;

        }
        if(direction == 3){
            if(!hasShield) {
                if (this.curDirection == 0) {
                    try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipRight4.png")) {
                        img = new Image(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(hasShield){
                if (this.curDirection == 0) {
                    try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipRightShield.png")) {
                        img = new Image(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.x += speed;
        }

    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}