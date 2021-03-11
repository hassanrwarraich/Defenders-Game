package Model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Titanium extends GameObject{

    public Titanium() {

        speed = 10;
        curDirection = 1;

        try (FileInputStream inputStream = new FileInputStream("MediaFiles/titanium.png")) {
            img = new Image(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
