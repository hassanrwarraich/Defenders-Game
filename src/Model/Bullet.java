package Model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Bullet extends GameObject {
    private boolean isEnemyBullet;
    private final int speed = 2;
    private int range = 550;
    public Bullet(){
        try (FileInputStream inputStream = new FileInputStream("MediaFiles/bulletEnemy.png")) {
            img = new Image(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getBulletRange(){
        return range;
    }
    public void move(int direction){
        //left
        if(direction == 0){
            this.x -= speed;
            range -= speed;
        }
        //right
        if(direction == 1){
            this.x += speed;
            range -= speed;
        }
        //up left
        if(direction == 2){
            this.x -= speed;
            this.y += speed/2;
            range -= speed;
        }

        //up right
        if(direction == 3){
            this.x += speed;
            this.y += speed/2;
            range -= speed;
        }
        //down left
        if(direction == 4){
            this.x -= speed;
            this.y -= speed/2;
            range -= speed;
        }
        //down right
        if(direction == 5){
            this.x += speed;
            this.y -= speed/2;
            range -= speed;
        }
    }

    public void setEnemyBullet(boolean enemyBullet) {
        isEnemyBullet = enemyBullet;
        if(isEnemyBullet) {
            try (FileInputStream inputStream = new FileInputStream("MediaFiles/bulletEnemy.png")) {
                img = new Image(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try (FileInputStream inputStream = new FileInputStream("MediaFiles/bullet.png")) {
                img = new Image(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isEnemyBullet() {
        return isEnemyBullet;
    }
}