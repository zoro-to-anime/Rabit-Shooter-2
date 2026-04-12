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
    BitmapFont hp_font; 
    BitmapFont score_font; 
    BitmapFont boss_font;
    BitmapFont hpOrange;
    BitmapFont hpRed;
    BitmapFont hpGreen;
    BitmapFont scoreGold;
    BitmapFont scoreRoyal;
    BitmapFont scoreMagenta;
    BitmapFont scoreBrown;
    
    FreeTypeFontGenerator mine;
    FreeTypeFontGenerator.FreeTypeFontParameter mine_para;
    
    Texture player_img;
    Texture poo_img;
    static Texture bot_img;
    Texture boss_img;
    Texture game_over_img;
    
    Rectangle boss_r;
    boolean boss_spawned = false;
    boolean game_running = false;
    boolean firstLaunch = true;
    //Color hp_color;
    //Color score_color;
    float player_speed = 2;
    static Player player;
    PowerUp powerUp; 
    Boss boss;
    int player_dmg = 5;
    int playerhp = 100;
    float kill_cd = 0;
    public static int score = 0;
    float temp_bot_spawn_timer = 0;
    float bullet_cd =0;
    float spawn_time = 1;
    Array<Bullet> bullet;
    static Array<Bots> bots;
    @Override
    public void create() { //test
        batch = new SpriteBatch();
        hp_font = new BitmapFont();
        score_font = new BitmapFont();
        
        
        
        player_img = new Texture("entity/player.png");
        poo_img = new Texture("poo.png");
        bot_img = new Texture("entity/bot.png");
        boss_img = new Texture("entity/poopboss.png");
        game_over_img = new Texture("ui/gameover.png");
        
        
        font_handle();
        
        // nigga class
        
        player = new Player(player_img,player_speed,100);
        powerUp = new PowerUp(player);
        boss = new Boss(boss_img , 300 , -200 , 300 , player);
        bullet = new Array<>();
        bots = new Array<>();
        
        spawnbots();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        
        batch.begin(); //nigga
        
        first_launch();
        
        if(game_running && !firstLaunch) {
        bulletspawn();
        
        powerUp.render(batch);
        powerUp.tester();
        
        player.update();
        for (Bullet b : bullet) b.update();
        for (Bots b : bots) b.update();
        
        
        kill_entity();
        change_color();
        sample_gui();
        //hit();
        
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
        game_over();
        batch.end(); // end nigga
        
    }
    public void first_launch() {
    	if(firstLaunch) {
    		boss_font.draw(batch, "JEW SHOOTER", 175 , 500);
    		hp_font.draw(batch, "PRESS \"ENTER\" TO PLAY", 185 , 300);
    		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
    			firstLaunch = false;
    			game_running = true;
    		}
    	}
    }
    public void game_over() {
    	if(!game_running && !firstLaunch) {
    		
    		batch.draw(game_over_img , 100, 200);
    		hp_font.draw(batch, "PRESS \"ENTER\" TO PLAY", 185 , 100);
    		
    		powerUp.power_status = false;
    		powerUp.power_x = 700;
    		powerUp.power_y = 700;
    		
    		player.x = 350;
    		player.y = 350;
    		player.hp = 100;
    	
    		bots.clear();
    		bullet.clear();
    	
    		score = 0;
    		
    		boss_spawned = false;
    		boss.x = 300;
    		boss.y = -200;
    		boss.hp = 300;
    		
    		
    		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
    			game_running = true;
    		}
    	}
    }
    public void boss_handle() {
    	if(score > 499) {
    		boss_spawned = true;
    		spawn_time = 2;
    	}
    	if(!boss_spawned) spawn_time =1;
    	if(boss_spawned) {
    		boss.render(batch);
    		boss.update();
    	}
    	if(boss.hp == 20) {
    		for(int i = 1 ; i<=20 ; i++) {
    			spawnbots();
    			boss.hp-=1;
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
    	}
    	if(player.x>800 || player.x <0 || player.y+50 > 800 || player.y <0) {
    		player.hp -= 20;
			player.x = 350;
			player.y = 350;
    	}
    	if(boss.hp<=0) {
    		boss.x = 300;
    		boss.y = -100;
    		boss_spawned = false;
    	}
    }
    public void font_handle() {
    	mine = new FreeTypeFontGenerator(Gdx.files.internal("Minecraft.ttf"));
        mine_para = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mine_para.size = 30;
        
        //nigga 1
        
        mine_para.color = Color.CHARTREUSE;
        hp_font = mine.generateFont(mine_para);
        hpGreen = hp_font;
        mine_para.color = Color.ORANGE;
        hpOrange = mine.generateFont(mine_para);
        mine_para.color = Color.FIREBRICK;
        hpRed = mine.generateFont(mine_para);
        
        //nigga 2
        
        mine_para.color = Color.BROWN;
        score_font = mine.generateFont(mine_para);
        scoreBrown = score_font;
        mine_para.color = Color.ROYAL;
        scoreRoyal = mine.generateFont(mine_para);
        mine_para.color = Color.GOLD;
        scoreGold = mine.generateFont(mine_para);
        mine_para.color = Color.MAGENTA;
        scoreMagenta = mine.generateFont(mine_para);
        
        mine_para.size = 60;
        mine_para.color = Color.SALMON;
        
        boss_font = mine.generateFont(mine_para);
        
        mine.dispose();
    }
    public void sample_gui() {
    	hp_font.draw(batch, "HEALTH : "+player.hp, 75 , 755 );
    	score_font.draw(batch, "SCORE : "+score, 75 , 725 );
    	if(boss_spawned) {
    		boss_font.draw(batch, "BOSS HP : "+boss.hp, 300 , 745 );
    	}
    }
    public void change_color() {
    	if (player.hp <= 20) {
    	    hp_font = hpRed;
    	} else if (player.hp <= 60) {
    	    hp_font = hpOrange;
    	} else {
    	    hp_font = hpGreen;
    	}

    	//diddy
    	if(score >= 300 ) {
    		score_font = scoreMagenta;
    	}
    	else if(score >= 200) {
    		score_font = scoreRoyal;
    	}
    	else if(score >= 100) {
    		score_font = scoreGold;
    	}
    	else {
    		score_font = scoreBrown;
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
        hp_font.dispose();
        score_font.dispose();
        boss_font.dispose();
        poo_img.dispose();
        bot_img.dispose();
        boss_img.dispose();
        game_over_img.dispose();
    }
    
}
