package Controller;
import Model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


import static javafx.scene.input.KeyCode.*;

public class Game extends Application {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    int pauser = 1, newGame = 0, destroyedEnemy = 0;
    int onlyOnce = 0;
    int adjust = 0;
    float endPosition = 250;
    private GCamera cam;
    static GameManager gm;
    private boolean goUP = false, goDown = false, goRight = false, goLeft = false, shoot = false;
    Scene mainScene, creditsScene, scene, endScene,highScene,pauseScene,levelScene;
    Pane root;
    private int superAttack = 0;
    private long lastShootTime = System.currentTimeMillis();

    @Override
    public void start(Stage stage) throws Exception{
        //gui game window
        root = new Pane();
        gm = new GameManager();
        Canvas canvas = new Canvas( 1920, 1080 );
        root.getChildren().add( canvas );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        stage.setWidth(1024);
        stage.setHeight(576);
        Scene scene = new Scene(root);
        stage.setTitle("Defender");
        Label title = new Label("DEFENDER");
        title.setTextFill(Color.BLACK);
        title.setPadding(new Insets(150));

        final double MAX_FONT_SIZE = 50.0;
        title.setFont(new Font(MAX_FONT_SIZE));

        Button gameButton = new Button("Play Game");
        Button scoresButton = new Button("High Scores");
        Button creditsButton = new Button("Credits");
        Button quitButton = new Button("Quit");
        scoresButton.setOnAction(e->stage.setScene(highScene));
        Button goMenu = new Button("Go to Main Menu");

        gameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pauser = 0;
                gm = new GameManager();
                //gm.spawnBonus();
                gm.spawnPlayer();
                gm.spawnEnemy();
                gc.translate((endPosition-250)-300,0);
                System.out.println(endPosition + "    " + ((endPosition-250)-300) );
                newGame = 1;
                stage.setScene(scene);
                pauser = 0;
                goUP = false;
                goDown = false;
                goRight = false;
                goLeft = false;
                shoot = false;
            }
        });

        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        title.setTextFill(Color.WHITE);
        //1300 720
        mainLayout.setStyle("-fx-background-image: url(file:MediaFiles/background.jpg);");
        mainLayout.getChildren().addAll(
                title,
                gameButton,
                scoresButton,
                creditsButton,
                quitButton
        );

        mainScene = new Scene(mainLayout);
        stage.setScene(mainScene);
        stage.show();

        Label person1 = new Label("Samir Ibrahimzade");
        Label person2 = new Label("Abdullah Ayberk Gorgun");
        Label person3 = new Label("Taner Durmaz");
        Label person4 = new Label("Hassan Raza");
        Label person5 = new Label("Alymbek Sagymbaev");
        person1.setTextFill(Color.WHITE);
        person1.setFont(Font.font(20));
        person2.setTextFill(Color.WHITE);
        person2.setFont(Font.font(20));
        person3.setTextFill(Color.WHITE);
        person3.setFont(Font.font(20));
        person4.setTextFill(Color.WHITE);
        person4.setFont(Font.font(20));
        person5.setTextFill(Color.WHITE);
        person5.setFont(Font.font(20));
        VBox creditsLayout = new VBox(10);
        creditsLayout.setStyle("-fx-background-image: url(file:MediaFiles/background.jpg);");
        creditsScene = new Scene(creditsLayout,1024,576);
        quitButton.setOnAction(e -> Platform.exit());
        creditsButton.setOnAction(e -> stage.setScene(creditsScene));


        gm.loadHighScore();
        String[] high = gm.getHighScores();
        Label scores1 = new Label("HIGH SCORES \n \n\n\n");
        Label scores2 = new Label(high[0]+" "+high[1]+" "+high[2]+"\n"+high[3]+" "+high[4]+" "+high[5]+"\n"+high[6]+" "+high[7]+" "+high[8]+"\n"+high[9]+" "+high[10]+" "+high[11]+"\n"+high[12]+" "+high[13]+" "+high[14]);
        
        scores1.setTextFill(Color.WHITE);
        scores1.setFont(Font.font(20));
        scores2.setTextFill(Color.WHITE);
        scores2.setFont(Font.font(20));
        Button menuButton = new Button("Go To Main Menu");
        menuButton.setOnAction(e -> stage.setScene(mainScene));
        VBox highLayout = new VBox(10);
        //added
        highLayout.setStyle("-fx-background-image: url(file:MediaFiles/background.jpg);");
        highLayout.getChildren().addAll(scores1, scores2, menuButton);
        highScene = new Scene(highLayout, WIDTH, HEIGHT);
        highLayout.setAlignment(Pos.CENTER);
        Button menuButtonForCredits = new Button("Go To Main Menu");

        menuButtonForCredits.setOnAction(e -> stage.setScene(mainScene));
        creditsLayout.setAlignment(Pos.CENTER);
        creditsLayout.getChildren().addAll(
                person1,
                person2,
                person3,
                person4,
                person5,
                menuButtonForCredits
        );


        gc.scale(1.5,1);

        cam = new GCamera(0,0);
        cam.tick(gm.getP());

        //needs to be in render method
        //Graphics g = getDrawGraphics();
        //Graphics2D g2d = g;
        //g2d.translate(cam.getX(),cam.getY());
        //handler.render(g);
        //g2d.translate(-cam.getX(),-cam.getY());

        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
            	if (pauser == 0) {
                    if (destroyedEnemy % 3 == 0 && destroyedEnemy != 0 && onlyOnce == 0) {
                        System.out.println("destroyed enemy " + destroyedEnemy);
                        System.out.println("Level " + destroyedEnemy / 3 + " passed!");
                        gm.increaseEnemySpeeds();
                        gm.increaseLevel();
                        gm.spawnEnemy();
                        gm.setKillScore(((destroyedEnemy % 3) + 1) * 500);
                        onlyOnce = 1;
                        pauser = 1;
                        Label level = new Label("Level " + destroyedEnemy / 3 + " passed! \n\n");
                        Label titan = new Label("Titanium: " + gm.getP().getTitaniumCount()+"\n");
                        level.setStyle("-fx-background-color: grey;");
                        level.setMaxWidth(200);
                        level.setTextFill(Color.WHITE);
                        level.setAlignment(Pos.CENTER);
                        titan.setStyle("-fx-background-color: grey;");
                        titan.setMaxWidth(200);
                        titan.setTextFill(Color.WHITE);
                        titan.setAlignment(Pos.CENTER);
                        Button weaponButton = new Button("Upgrade weapon:  300 Titanium");
                        Button lifeButton = new Button("Upgrade max life:  300 Titanium");
                        Button shieldButton = new Button("Purchase Shield:  300 Titanium");
                        Button resumeButton = new Button("Continue");
                        weaponButton.setMaxWidth(200);
                        lifeButton.setMaxWidth(200);
                        shieldButton.setMaxWidth(200);
                        resumeButton.setMaxWidth(200);
                        VBox levelLayout = new VBox(10);
                        endPosition = gm.getP().getX();
                        levelLayout.setStyle("-fx-background-image: url(file:MediaFiles/background.jpg);");
                        levelLayout.getChildren().addAll(level,titan,shieldButton,lifeButton,weaponButton, resumeButton);
                        levelLayout.setAlignment(Pos.CENTER);
                        levelScene = new Scene(levelLayout, WIDTH, HEIGHT);
                        stage.setScene(levelScene);
                        resumeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                stage.setScene(scene);
                                pauser = 0;
                            }
                        });
                        weaponButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if(gm.getP().getTitaniumCount() >=5) {
                                    gm.upgradeWeapon();
                                    gm.getP().decreaseTitanium();
                                }
                            }
                        });
                        lifeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if(gm.getP().getTitaniumCount() >=5) {
                                    gm.upgradeMaxLife();
                                    gm.getP().decreaseTitanium();
                                }
                            }
                        });

                        shieldButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if(gm.getP().getTitaniumCount() >=5) {
                                    gm.getP().setHasShield(true);
                                    if (gm.getP().getCurDirection() == 0) {
                                        try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipLeftShield.png")) {
                                            gm.getP().setImg(new Image(inputStream));
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (gm.getP().getCurDirection() == 1) {
                                        try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipRightShield.png")) {
                                            gm.getP().setImg(new Image(inputStream));
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    gm.getP().decreaseTitanium();
                                }
                            }
                        });
                        goUP = false;
                        goDown = false;
                        goRight = false;
                        goLeft = false;
                        shoot = false;

                    }
                    checkCol();
                    //player move
                    if(goUP && gm.getP().getY() > 80){
                       // System.out.println("up");
                        gm.getP().move(0,2);

                    }
                    if(goDown && gm.getP().getY() < gm.getMapImage().getHeight() - 110){
                       // System.out.println("down");
                        gm.getP().move(1,2);
                    }
                    if(goRight){
                        // if(gm.getP().getCurDirection() == 0)
                        gc.translate(-2,0);
                        //System.out.println("right");
                        gm.getP().move(3,2);
                        gm.getP().setCurDirection(1);
                    }
                    if(goLeft){
                        // if(gm.getP().getCurDirection() == 1)
                        gc.translate(2,0);
                       // System.out.println("left");
                        gm.getP().move(2,2);
                        gm.getP().setCurDirection(0);
                    }

                    if(shoot && gm.getP().getShootCooldown() < ((System.currentTimeMillis() - lastShootTime)/100) ){
                        if(superAttack == 1) {
                            gm.getBulletList()[gm.getBulletIndex()].setCurDirection(gm.getP().curDirection + 2);
                            gm.getBulletList()[gm.getBulletIndex()].setX(gm.getP().x+47);
                            gm.getBulletList()[gm.getBulletIndex()].setY(gm.getP().y+33);
                            gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                            gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                            gm.increaseBulletIndex();
                            gm.getBulletList()[gm.getBulletIndex()].setCurDirection(gm.getP().curDirection + 4);
                            gm.getBulletList()[gm.getBulletIndex()].setX(gm.getP().x+47);
                            gm.getBulletList()[gm.getBulletIndex()].setY(gm.getP().y+33);
                            gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                            gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                            gm.increaseBulletIndex();
                            gm.getBulletList()[gm.getBulletIndex()].setCurDirection(gm.getP().curDirection + 1);
                            gm.getBulletList()[gm.getBulletIndex()].setX(gm.getP().x+47);
                            gm.getBulletList()[gm.getBulletIndex()].setY(gm.getP().y+33);
                            gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                            gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                            gm.increaseBulletIndex();
                            gm.getBulletList()[gm.getBulletIndex()].setCurDirection(gm.getP().curDirection + 3);
                            gm.getBulletList()[gm.getBulletIndex()].setX(gm.getP().x+47);
                            gm.getBulletList()[gm.getBulletIndex()].setY(gm.getP().y+33);
                            gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                            gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                            gm.increaseBulletIndex();

                            superAttack = 0;
                        }
                        else{

                            gm.getBulletList()[gm.getBulletIndex()].setCurDirection(gm.getP().curDirection);
                            gm.getBulletList()[gm.getBulletIndex()].setX(gm.getP().x+47);
                            gm.getBulletList()[gm.getBulletIndex()].setY(gm.getP().y+33);
                            gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                            gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                            gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                            gm.increaseBulletIndex();
                            lastShootTime = System.currentTimeMillis();
                            if(gm.getP().getWeapon() == 1){
                                gm.getBulletList()[gm.getBulletIndex()].setCurDirection(gm.getP().curDirection + 2);
                                gm.getBulletList()[gm.getBulletIndex()].setX(gm.getP().x+47);
                                gm.getBulletList()[gm.getBulletIndex()].setY(gm.getP().y+33);
                                gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                                gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                                gm.increaseBulletIndex();
                            }

                            if(gm.getP().getWeapon() == 2){
                                gm.getBulletList()[gm.getBulletIndex()].setCurDirection(gm.getP().curDirection + 2);
                                gm.getBulletList()[gm.getBulletIndex()].setX(gm.getP().x+47);
                                gm.getBulletList()[gm.getBulletIndex()].setY(gm.getP().y+33);
                                gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                                gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                                gm.increaseBulletIndex();
                                gm.getBulletList()[gm.getBulletIndex()].setCurDirection(gm.getP().curDirection + 4);
                                gm.getBulletList()[gm.getBulletIndex()].setX(gm.getP().x+47);
                                gm.getBulletList()[gm.getBulletIndex()].setY(gm.getP().y+33);
                                gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(false);
                                gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                                gm.increaseBulletIndex();
                            }
                        }
                    }

                    double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                    //enemy move and shoot
                    for (int i = 0; i < gm.getEnemyList().length; i++) {
                        Enemy e = gm.getEnemyList()[i];
                        if (e.isActive()) {
                            if(e.getX()>
                                    (gm.getMapImage().getWidth())){
                                e.setX(((int)gm.getMapImage().getWidth() -100));
                            }
                            else if (e.getX()<(-gm.getMapImage().getWidth())){
                                e.setX(((int)gm.getMapImage().getWidth()*-1 + 100));
                            }
                            int max = 4;
                            int min = 0;
                            int range = max - min + 1;
                            int rand = (int) (Math.random() * range) + min;
                            if(i < gm.getEnemyList().length/2 -1){
                                for (int j = 0; j < 70000; j++) {
                                    if(rand == 0 ) {
                                        if (e.getY() < gm.getMapImage().getHeight() - 110)
                                            e.move(rand);
                                        else e.move(1);
                                    }
                                    else {
                                    	if(rand==1)
                                    		if(e.getY()>80) e.move(rand);
                                    		else e.move(0);
                                    	else e.move(rand);
                                    }
                                }
                            }
                            else{
                                int dir;
                                if(e.getX()>gm.getP().getX()){
                                    dir = 2;
                                }
                                else{
                                    dir = 3;
                                }
                                for (int j = 0; j < 3500; j++) {
                                    e.move(dir);
                                }
                                if(e.getY()>gm.getP().getY()){
                                    dir = 1;
                                }
                                else{
                                    dir = 0;
                                }
                                for (int j = 0; j < 3500; j++) {
                                    e.move(dir);
                                }
                            }
                            max = 1000;
                            range = max - min + 1;
                            rand = (int) (Math.random() * range) + min;
                            if(rand < 1){
                                gm.getBulletList()[gm.getBulletIndex()].setCurDirection(e.curDirection);
                                gm.getBulletList()[gm.getBulletIndex()].setX(e.x);
                                gm.getBulletList()[gm.getBulletIndex()].setY(e.y);
                                gm.getBulletList()[gm.getBulletIndex()].setEnemyBullet(true);
                                gm.getBulletList()[gm.getBulletIndex()].setActive(true);
                                gm.increaseBulletIndex();
                            }
                        }
                    }
                    //bullet move
                    for (Bullet b : gm.getBulletList()) {
                        if (b.isActive()) {
                            for (int i = 0; i < 5; i++) {
                                b.move(b.getCurDirection());
                            }
                        }
                    }

                    if (gm.getP().getCurDirection() == 1) {
                        gc.translate(-1, 0);

                        gm.getP().move(3, 1);
                    } else {
                        gc.translate(1, 0);
                        gm.getP().move(2, 1);
                    }

                    gc.drawImage(gm.getMapImage(), 0, 0);
                    gc.drawImage(gm.getMapImage(), gm.getMapImage().getWidth(), 0);
                    gc.drawImage(gm.getMapImage(), -1*gm.getMapImage().getWidth(), 0);
                    gc.drawImage(gm.getMapImage(), -2*gm.getMapImage().getWidth(), 0);
                    

                    gc.setFill(Color.GREY);
                    gc.setStroke(Color.GREY);
                    gc.strokeLine(gm.getP().getX()-500+adjust, 80, gm.getP().getX()+500+adjust, 80);
                    gc.strokeLine(gm.getP().getX()+300+adjust, 0, gm.getP().getX()+300+adjust, 80);
                    gc.strokeLine(gm.getP().getX()-80+adjust, 0, gm.getP().getX()-80+adjust, 80);
                    gc.fillRect(gm.getP().getX()-500+adjust, 0, 420, 80);
                    gc.fillRect(gm.getP().getX()+300+adjust, 0, 420, 80);
                    gc.setStroke(Color.BLACK);
                    gc.strokeText("Pause(P)", gm.getP().getX()+310+adjust, 20);
                    gc.strokeText("Score: "+gm.getScore() , gm.getP().getX()+310+adjust, 40);
                    gc.strokeText("Titanium: "+gm.getP().getTitaniumCount() , gm.getP().getX()+310+adjust, 60);
                    gc.strokeText("Lives: " , gm.getP().getX()-200+adjust, 20);
                    gc.drawImage(gm.getP().getImg(), gm.getP().getX()-160+adjust, 10,20,20);
                    gc.strokeText(" x"+gm.getP().getLives() , gm.getP().getX()-140+adjust, 20);

                    ///////minimap part for map
                    Image minimapimg;
                    try (FileInputStream inputStream = new FileInputStream("MediaFiles/mapImgMM2.png")) {
                        minimapimg = new Image(inputStream);
                        gc.drawImage(minimapimg,gm.getP().getX() - 80 + adjust,0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ////////////////////////////

                    ///////image for spaceship
                    Image spaceshipLeftMM,spaceshipRightMM;
                    try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipLeftMM.png")) {
                        spaceshipLeftMM = new Image(inputStream);
                        if(gm.getP().getCurDirection() == 0)
                            gc.drawImage(spaceshipLeftMM,(gm.getP().getX()-80)+(gm.getP().getX()/2.69)+adjust,gm.getP().getY()/6.7);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipRightMM.png")) {
                        spaceshipRightMM = new Image(inputStream);
                        if(gm.getP().getCurDirection() == 1)
                            gc.drawImage(spaceshipRightMM,(gm.getP().getX()-80)+(gm.getP().getX()/2.69)+adjust,gm.getP().getY()/6.7);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ///////////////////

                    
                    if( gm.getMapImage().getWidth()  <  gm.getP().getX()){
                        System.out.println(gm.getMapImage().getWidth()+"borderr right" + gm.getP().getX());
                        gm.getP().setX(0);
                        gc.translate(1026,0);
                    }
                    else if(0 >  gm.getP().getX()){
                        System.out.println("borderr left" + gm.getP().getX());
                        gm.getP().setX(1024);
                        gc.translate(-1026,0);
                    }

                    if (gm.getP().isActive()) {

                        gc.drawImage(gm.getP().getImg(), gm.getP().getX(), gm.getP().getY());
                    }
                    for (Enemy e : gm.getEnemyList()) {
                        if (e.isActive()) {
                            ///for minimap
                            try (FileInputStream inputStream = new FileInputStream("MediaFiles/enemyImg3MM.png")) {
                                Image enemyImg;
                                enemyImg = new Image(inputStream);
                                if(e.getType() == 1)
                                gc.drawImage(enemyImg,(gm.getP().getX()-80)+(e.getX()/2.69)+adjust,e.getY()/6.7);

                            } catch (IOException err) {
                                err.printStackTrace();
                            }

                            try (FileInputStream inputStream = new FileInputStream("MediaFiles/enemyT2MM.png")) {
                                Image enemyImg;
                                enemyImg = new Image(inputStream);
                                if(e.getType() == 2)
                                    gc.drawImage(enemyImg,(gm.getP().getX()-80)+(e.getX()/2.69)+adjust,e.getY()/6.7);

                            } catch (IOException err) {
                                err.printStackTrace();
                            }
                            ///
                            gc.drawImage(e.getImg(), e.getX(), e.getY());
                            //if(gm.getP().getX() > 0)
                            gc.drawImage(e.getImg(), e.getX() - 1026, e.getY());
                           //else
                            gc.drawImage(e.getImg(), e.getX() + 1026, e.getY());
                        }
                    }

                    
                    for (Bullet b : gm.getBulletList()) {
                        if (b.isActive()) {
                            int futureBulletPosOffset = 0;
                            //if bullet moves right
                            if(b.getCurDirection()%2 == 0){
                                futureBulletPosOffset = 2;
                            }
                            else{
                                futureBulletPosOffset = -2;
                            }
                            /*
                            if((Math.abs(gm.getP().getX() - b.getX()) > 100) && (gm.getP().getX() - b.getX() < 550)) {
                                if ((Math.abs(gm.getP().getX() - b.getX()) < Math.abs(gm.getP().getX() - (b.getX() + futureBulletPosOffset))) ) {
                                    b.setActive(false);
                                    System.out.println(Math.abs(gm.getP().getX() - b.getX()));
                                    System.out.println(Math.abs(Math.abs(gm.getP().getX() - (b.getX() + futureBulletPosOffset))));
                                }
                            }*/
                            if(b.getBulletRange() < 0){
                                b.setActive(false);
                            }
                            gc.drawImage(b.getImg(), b.getX(), b.getY());
                            gc.drawImage(b.getImg(), b.getX() - 1026, b.getY());
                            gc.drawImage(b.getImg(), b.getX() + 1026, b.getY());
                            if( gm.getMapImage().getWidth()  <  b.getX()){
                                b.setX(0);
                            }
                            else if(gm.getMapImage().getWidth() <  b.getX()){
                                b.setX(0);
                            }
                        }
                    }

                    for (Bonus b : gm.getBonusList()) {
                        if (b.isActive()) {
                            gc.drawImage(b.getImg(), b.getX(), b.getY());
                        }
                    }
                    for (Titanium titan : gm.getTitaniumList()) {
                        if (titan.isActive()) {
                            gc.drawImage(titan.getImg(), titan.getX(), titan.getY());
                        }
                    }
                    if (gm.checkLives()) {
                        pauser = 1;
                        endPosition = gm.getP().getX();
                        TextField enterName = new TextField();
                        Label over = new Label("GAME OVER! \n \n" + "Your Score: " + gm.getScore() + "\n \nenter name: ");
                        over.setTextFill(Color.WHITE);
                        over.setFont(Font.font(20));
                        enterName.setAlignment(Pos.CENTER);
                        enterName.setMaxWidth(100);
                        Button enterButton = new Button("ENTER");
                        enterButton.setOnAction(e -> {
                            try {
                            	if(enterName.getText().isEmpty() ) {
                            		high("-", gm.getScore(), stage);
                            	}
                            	else {
                                    high(enterName.getText(), gm.getScore(), stage);
                            	}
                            } catch (FileNotFoundException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        });
                        VBox endLayout = new VBox();
                        endLayout.setStyle("-fx-background-image: url(file:MediaFiles/background.jpg);");
                        endLayout.getChildren().addAll(over, enterName, enterButton);
                        endScene = new Scene(endLayout, WIDTH, HEIGHT);
                        endLayout.setAlignment(Pos.CENTER);

                        stage.setScene(endScene);
                        stage.show();
                    }
                }
            }
        }.start();

        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {

                if(event.getCode() == P){//P ?
                	Label pause = new Label("PAUSE \n");
                    Button homeButton = new Button("EXIT");
                    Button resumeButton = new Button("RESUME");
                    VBox pauseLayout = new VBox(10);
                    homeButton.setOnAction(e -> end(stage));
                    endPosition = gm.getP().getX();
                    pauseLayout.setStyle("-fx-background-image: url(file:MediaFiles/background.jpg);");
                    pauseLayout.getChildren().addAll(pause,homeButton,resumeButton);
                    pauseLayout.setAlignment(Pos.CENTER);
                    pauseScene = new Scene(pauseLayout, WIDTH,HEIGHT);
                    stage.setScene(pauseScene);
                    pauser = 1;

                    resumeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            stage.setScene(scene);
                            pauser = 0;
                        }
                    });
                    goUP = false;
                    goDown = false;
                    goRight = false;
                    goLeft = false;
                }
                else{

                    switch (event.getCode()) {
                        case UP:    goUP = true; break;
                        case DOWN:  goDown = true; break;
                        case LEFT:  goLeft  = true; break;
                        case RIGHT: goRight  = true; break;
                        case SPACE: shoot = true; break;
                    }

                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goUP = false; break;
                    case DOWN:  goDown = false; break;
                    case LEFT:  goLeft = false; break;
                    case RIGHT: goRight  = false; break;
                    case SPACE: shoot = false; break;
                }
            }
        });
    }

    public void end(Stage stage) {
    	//pauser = 0;
    	stage.setScene(mainScene);
    }
    public void high(String nam, int sco, Stage stage) throws FileNotFoundException {
    	gm.addNewHighScore(sco, nam);
    	gm.loadHighScore();
        String[] high = gm.getHighScores();
        Label scores1 = new Label("HIGH SCORES \n\n\n \n"  );
        Label scores2 = new Label(high[0]+" "+high[1]+" "+high[2]+"\n"+high[3]+" "+high[4]+" "+high[5]+"\n"+high[6]+" "+high[7]+" "+high[8]+"\n"+high[9]+" "+high[10]+" "+high[11]+"\n"+high[12]+" "+high[13]+" "+high[14]);
        adjust = adjust + 40;
        scores1.setTextFill(Color.WHITE);
        scores1.setFont(Font.font(20));
        scores2.setTextFill(Color.WHITE);
        scores2.setFont(Font.font(20));
        Button menuButton = new Button("Go To Main Menu");
        menuButton.setOnAction(e -> stage.setScene(mainScene));
        VBox highLayout = new VBox(10);
        highLayout.setStyle("-fx-background-image: url(file:MediaFiles/background.jpg);");
        highLayout.getChildren().addAll(scores1, scores2, menuButton);
        highScene = new Scene(highLayout, 1024, 576);

        Button menuButtonForCredits = new Button("Go To Main Menu");

        menuButtonForCredits.setOnAction(e -> stage.setScene(mainScene));

        highLayout.setAlignment(Pos.CENTER);
    	stage.setScene(highScene);
    }

    void checkCol() {
        // 1'st case
        // the player is hit with enemy bullet
        Player player = gm.getP();
        int widthPlayer = (int) player.getImg().getWidth();
        int heightPlayer = (int) player.getImg().getHeight();
        Bullet dummyBuullet = new Bullet();
        int widthBullet = (int) dummyBuullet.getImg().getWidth();
        int heightBullet = (int) dummyBuullet.getImg().getHeight();
        for (Bullet bullet : gm.getBulletList()) {
            // the bullet must be an enemy bullet
            if (bullet.isEnemyBullet() && bullet.isActive()) {
                // the condition when the bullet location is inside the rectangle of player
                if ( Math.abs( bullet.getX() - player.getX() ) < widthPlayer / 2
                        && Math.abs( bullet.getY() - player.getY() ) < heightPlayer / 2 ) {
                    // 1) destroy the bullet
                    // 2) decrease the life of a player
                    gm.getP().setHasShield(false);
                    if(gm.getP().getCurDirection() == 0){
                        try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipLeft4.png")) {
                            gm.getP().setImg(new Image(inputStream)) ;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(gm.getP().getCurDirection() == 1){
                        try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipRight4.png")) {
                            gm.getP().setImg(new Image(inputStream)) ;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    bullet.setActive(false);
                    if(gm.getP().getHasShield() == false)player.decreaseLife();
                    break;
                }
            }
        }
        // 2'nd case
        // the enemy is hit with player bullet
        for (Enemy enemy : gm.getEnemyList()) {
            if (enemy.isActive()) {
                int widthEnemy = (int) enemy.getImg().getWidth();
                int heightEnemy = (int) enemy.getImg().getHeight();
                for (Bullet bullet : gm.getBulletList()) {
                    if (!bullet.isEnemyBullet() && bullet.isActive()) {
                        // the condition when the player bullet location is inside the rectangle of enemy
                        if (Math.abs(enemy.getX() - bullet.getX()) < (widthBullet / 2 + widthEnemy / 2)
                                && Math.abs(enemy.getY() - bullet.getY()) < (heightBullet / 2 + heightEnemy / 2)) {
                            // 1) destroy the player bullet
                            // 2) destroy the enemy
                            if (destroyedEnemy % 3 == 0 && destroyedEnemy != 0) onlyOnce = 0;
                            destroyedEnemy++;
                            gm.increaseScore();
                            enemy.setActive(false);


                            if(enemy.hTitanium()) {
                                gm.getTitaniumList()[gm.getTitaniumIndex()].setX(enemy.getX());
                                gm.getTitaniumList()[gm.getTitaniumIndex()].setY(enemy.getY());
                                gm.getTitaniumList()[gm.getTitaniumIndex()].setActive(true);
                                gm.increaseTitaniumIndex();
                            }

                            if(enemy.hBonus()) {
                            	gm.getBonusList()[gm.getBonusIndex()].setX(enemy.getX());
                            	gm.getBonusList()[gm.getBonusIndex()].setY(enemy.getY());
                            	gm.getBonusList()[gm.getBonusIndex()].setActive(true);
                            	gm.increaseBonusIndex();
                            }
                            bullet.setActive(false);
                            break;
                        }
                    }
                }
            }
        }

        // 3'rd case
        // the player collided with enemy ship
        for (Enemy enemy : gm.getEnemyList()) {
            if (enemy.isActive()) {
                int widthEnemy = (int) enemy.getImg().getWidth();
                int heightEnemy = (int) enemy.getImg().getHeight();
                // the condition when the enemy rectangle is inside the rectangle of player
                if (Math.abs(enemy.getX() - player.getX()) < (widthPlayer / 2 + widthEnemy / 2)
                        && Math.abs(enemy.getY() - player.getY()) < (heightPlayer / 2 + heightEnemy / 2)) {
                    // 1) destroy the enemy
                    // 2) decrease the player's life
                    if(gm.getP().getHasShield() == false)player.decreaseLife();
                    gm.getP().setHasShield(false);
                    if(gm.getP().getCurDirection() == 0){
                        try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipLeft4.png")) {
                            gm.getP().setImg(new Image(inputStream)) ;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(gm.getP().getCurDirection() == 1){
                        try (FileInputStream inputStream = new FileInputStream("MediaFiles/spaceshipRight4.png")) {
                            gm.getP().setImg(new Image(inputStream)) ;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    enemy.setActive(false);

                    break;
                }
            }
        }

        for (Bonus bonus : gm.getBonusList()) {
            if (bonus.isActive()) {
                int widthBonus = (int) bonus.getImg().getWidth();
                int heightBonus = (int) bonus.getImg().getHeight();
                if (Math.abs(bonus.getX() - player.getX()) < (widthPlayer / 2 + widthBonus / 2)
                        && Math.abs(bonus.getY() - player.getY()) < (heightPlayer / 2 + heightBonus / 2)) {
                    bonus.setActive(false);
                    if (bonus.getType() == 1) {
                        if (player.getLives() < player.getMaxLives()) {
                            player.setLives(player.getLives() + 1);
                        }
                    }
                    else{
                        superAttack = 1;
                    }
                }
            }
        }

        for (Titanium titanium : gm.getTitaniumList()) {
            if (titanium.isActive()) {
                int widthBonus = (int) titanium.getImg().getWidth();
                int heightBonus = (int) titanium.getImg().getHeight();
                if (Math.abs(titanium.getX() - player.getX()) < (widthPlayer / 2 + widthBonus / 2)
                        && Math.abs(titanium.getY() - player.getY()) < (heightPlayer / 2 + heightBonus / 2)) {
                    titanium.setActive(false);
                    gm.getP().increaseTitanium();
                }
            }
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}
/*
    public void init(){
        //cam = new GCamera(0,0);
        gm = GameManager.getInstance();
        gm.spawnBonus();

        gm.spawnPlayer();
        gm.spawnEnemy();

    }
*/
////////////////////////////////////////////////////////////////////////////////////////

//        TextField enterName = new TextField();
//        Label over = new Label("GAME OVER! \n \n" + "Your Score: " + gm.getScore() + "\n \nenter name: ");
//        over.setTextFill(Color.WHITE);
//        over.setFont(Font.font(20));
//        enterName.setAlignment(Pos.CENTER);
//        enterName.setMaxWidth(100);
//        Button enterButton = new Button("ENTER");
//        enterButton.setOnAction(e -> stage.setScene(highScene));
//        VBox endLayout = new VBox();
//added
//        endLayout.setStyle("-fx-background-image: url(file:MediaFiles/background.jpg);");
//        endLayout.getChildren().addAll(over, enterName, enterButton);
//        endScene = new Scene(endLayout, WIDTH, HEIGHT);
//      endLayout.setAlignment(Pos.CENTER);
///////////////////////////////////////////////////////