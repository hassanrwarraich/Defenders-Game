package Model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EnemyType1 extends Enemy {

    public EnemyType1(){

        try (FileInputStream inputStream = new FileInputStream("MediaFiles/enemyImg3.png")) {
            img = new Image(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        type = 1;
    }



}
