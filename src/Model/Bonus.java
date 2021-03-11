package Model;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Bonus extends GameObject {

    private int type;
    // 0 for super attack
    // 1 for life bonus

    public int getType() {
        return type;
    }

    public Bonus(){

        speed = 10;
        curDirection = 1;

        type = (int) (Math.random() * 2);

        if(type == 1){
            try (FileInputStream inputStream = new FileInputStream("MediaFiles/bonus3.png")) {
                img = new Image(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try (FileInputStream inputStream = new FileInputStream("MediaFiles/superAttack.png")) {
                img = new Image(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}