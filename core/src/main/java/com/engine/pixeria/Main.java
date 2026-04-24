package com.engine.pixeria;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    Texture player_img;
    Texture poo_img;
    Texture whiteFull;
    static Texture bot_img;
    Texture boss_img;
    Texture game_over_img;
    
    Rectangle boss_r;
    static boolean boss_spawned = false;
    static boolean game_running = false;
    static boolean mainMenu = true;
    boolean pause = false;
    static boolean gameOver = false;
    //Color hp_color;
    //Color score_color;
    float player_speed = 2;
    static Player player;
    PowerUp powerUp; 
    Boss boss;
    Gui gui;
    int player_dmg = 100;
    int playerhp = 100;
    float kill_cd = 0;
    public static int score = 0;
    float temp_bot_spawn_timer = 0;
    float bullet_cd =0;
    static float spawn_time = 1;
    Array<Bullet> bullet;
    static Array<Bots> bots;
    @Override
    public void create() { //test
        batch = new SpriteBatch();
        
        whiteFull = new Texture("ui/white.png");
        
        player_img = new Texture("entity/player.png");
        poo_img = new Texture("poo.png");
        bot_img = new Texture("entity/bot.png");
        boss_img = new Texture("entity/poopboss.png");
        
        
       
        player = new Player(player_img,player_speed,100);
        powerUp = new PowerUp(player);
        gui = new Gui();
        boss = new Boss(boss_img , 300 , -200 , 300 , player);
        bullet = new Array<>();
        bots = new Array<>();
        
        spawnbots();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        
        batch.begin(); //nigga
        
        gui.render(batch);
        //first_launch();
        
        if(game_running && !mainMenu) {
        bulletspawn();
        
        powerUp.render(batch);
        powerUp.tester();
        
        if(!pause) {
        	player.update();
        	for (Bullet b : bullet) b.update();
        	for (Bots b : bots) b.update();
        }
        
        kill_entity();
        //change_color();
        //sample_gui();
        //hit();
        boss.spawner();
        
        temp_bot_spawn_timer += Gdx.graphics.getDeltaTime();
        bullet_cd += Gdx.graphics.getDeltaTime();
        kill_cd += Gdx.graphics.getDeltaTime();
        if(temp_bot_spawn_timer >=spawn_time) {
        	spawnbots();
        	temp_bot_spawn_timer =0;
        }
        player.render(batch);
        for (Bullet b : bullet) b.render(batch);
        for (Bots b : bots) b.render(batch);
        boss_handle();
        
        }
        hit();
        //game_over();
        pause();
        batch.end(); // end nigga
        
    }
    
    public void pause() {
    	
    	if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
    		pause = !pause;
    	}
    	if (pause) {
    	    batch.setColor(0, 0, 0, 0.5f);
    	    batch.draw(whiteFull, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	    batch.setColor(Color.WHITE);
    	}
    }
    public void boss_handle() {
    	if(boss_spawned) {
    		boss.render(batch);
    		if(!pause) {
    			boss.update();	
    		}
    	}
    	if(Boss.hp <= 20) {
    		for(int i = 1 ; i<=20 ; i++) {
    			spawnbots();
    			Boss.hp-=1;
    		}
    	}
    	
    }
    public void powerUp_handle(float x, float y) {
    	powerUp.spawn();
    	if(powerUp.status()) {
    		powerUp.setLocation(x, y);
    	}
    }
    public void kill_entity() {
    	if(player.hp<=0) {
    		game_running = false;
    		gameOver = true;
    		mainMenu = false;
    	}
    	if(player.x>800 || player.x <0 || player.y+50 > 800 || player.y <0) {
    		player.hp -= 20;
			player.x = 350;
			player.y = 350;
    	}
    	if(Boss.hp<=0) {
    		boss.x = 300;
    		boss.y = -100;
    		boss_spawned = false;
    		Boss.hp = 500;
    	}
    }
    public void bulletspawn() {
    	if(bullet_cd>=0.5f) {
    		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
    			Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    			Vector2 pos = new Vector2(player.x, player.y);
    			Vector2 direction = mouse.sub(pos).nor(); 

    			bullet.add(new Bullet(poo_img, player.x, player.y, direction.x * 12, direction.y * 12));
    			bullet_cd =0;
    		}
    	}
    }
    public static void spawnbots() {
		Random rand = new Random();
		int s_x=0,s_y=0,d=0;
		d = rand.nextInt(4)+1;
		if(d==1) {
			s_y = 820;
			s_x = rand.nextInt(801);
		}
		else if(d==3) {
			s_y = -60;
			s_x = rand.nextInt(801);
		}
		else if(d==2) {
			s_x = 810;
			s_y = rand.nextInt(801);
		}
		else if(d==4) {
			s_x = -60;
			s_y = rand.nextInt(801);
		}
		bots.add(new Bots(bot_img, s_x , s_y,player));
	}
    public void hit() {
    	
    	for(int i =0; i<bullet.size;i++) {
    		boolean bullet_deleted = false;
    		Bullet b = bullet.get(i);
    		Rectangle b_r = new Rectangle(b.x,b.y,20,20);
    		for(int j =0;j<bots.size;j++) {
    			Bots c = bots.get(j);
    			Rectangle c_r = new Rectangle(c.x,c.y,50,50);
    			if(b_r.overlaps(c_r)) {
    				powerUp_handle(b.x,b.y);
    				bullet.removeIndex(i);
    				bots.removeIndex(j);
    				bullet_deleted = true;
    				score +=player_dmg;
    				break;
    			}
    		}
    		boss_r = new Rectangle(boss.x,boss.y,100,100);
    		if(boss_spawned && !bullet_deleted) {
    			if(boss_r.overlaps(b_r)) {
    				bullet.removeIndex(i);
    				boss.hp -= player_dmg;
    			}
    		}
    	}
    	Rectangle p = new Rectangle(player.x+2.5f,player.y+2.5f,45,45);
		boss_r = new Rectangle(boss.x,boss.y,100,100);
		if(boss_r.overlaps(p)&&kill_cd>1.5f) {
			player.hp -= 20;
			boss.hp += 10;
			score -= 200;
			kill_cd =0;
		}
		for(int j =0;j<bots.size;j++) {
			Bots c = bots.get(j);
			Rectangle c_r = new Rectangle(c.x,c.y,50,50);
			if(c_r.overlaps(p)) {
				bots.removeIndex(j);
				player.hp -=10;
				score -=100;
			}
		}
    	
    }
    @Override
    public void dispose() {
        batch.dispose();
        player_img.dispose();
        
        poo_img.dispose();
        bot_img.dispose();
        boss_img.dispose();
        
    }
    
}
