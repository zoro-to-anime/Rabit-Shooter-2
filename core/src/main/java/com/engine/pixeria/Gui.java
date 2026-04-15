package com.engine.pixeria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class Gui {
	Texture[] playButton;
	Texture[] difficultyButton;
	Texture[] easyButton,hardButton,midButton;
	Stage stage;
	public Gui() {
	playButton = new Texture[3];
	
	difficultyButton = new Texture[3];
	
	easyButton = new Texture[3];
	midButton = new Texture[3];
	hardButton = new Texture[3];
	
	stage = new Stage();
	Gdx.input.setInputProcessor(stage);
	loadImages();
	
	}
	public void mainMenu() {
		
	}
	public void gameOver() {
		
	}
	public void render() {
		
	}
	public void mouseHandle() {
		
	}
	public void loadImages() {
		playButton[0] = new Texture(Gdx.files.internal("ui/PlayButton/PlayStatic.png"));
		playButton[1] = new Texture(Gdx.files.internal("ui/PlayButton/PlayHover.png"));
		playButton[2] = new Texture(Gdx.files.internal("ui/PlayButton/PlayClicked.png"));
		
		difficultyButton[0] = new Texture(Gdx.files.internal("ui/DifficultyButton/DifficultyStatic.png"));
		difficultyButton[1] = new Texture(Gdx.files.internal("ui/DifficultyButton/DifficultyHover.png"));
		difficultyButton[2] = new Texture(Gdx.files.internal("ui/DifficultyButton/DifficultyClicked.png"));
		
		easyButton[0] = new Texture(Gdx.files.internal("ui/DifficultyButton/Easy/EasyStatic.png"));
		easyButton[1] = new Texture(Gdx.files.internal("ui/DifficultyButton/Easy/EasyHover.png"));
		easyButton[2] = new Texture(Gdx.files.internal("ui/DifficultyButton/Easy/EasyClicked.png"));
		
		midButton[0] = new Texture(Gdx.files.internal("ui/DifficultyButton/Mid/MidStatic.png"));
		midButton[1] = new Texture(Gdx.files.internal("ui/DifficultyButton/Mid/MidHover.png"));
		midButton[2] = new Texture(Gdx.files.internal("ui/DifficultyButton/Mid/MidClicked.png"));
		
		hardButton[0] = new Texture(Gdx.files.internal("ui/DifficultyButton/Hard/HardStatic.png"));
		hardButton[1] = new Texture(Gdx.files.internal("ui/DifficultyButton/Hard/HardHover.png"));
		hardButton[2] = new Texture(Gdx.files.internal("ui/DifficultyButton/Hard/HardClicked.png"));
		mouseHandle();
	}
	public void dispose() {
		for (Texture t : playButton) t.dispose();
        for (Texture t : difficultyButton) t.dispose();
        for (Texture t : easyButton) t.dispose();
        for (Texture t : midButton) t.dispose();
        for (Texture t : hardButton) t.dispose();
        stage.dispose();
	}
}
