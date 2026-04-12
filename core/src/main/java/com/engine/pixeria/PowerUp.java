package com.engine.pixeria;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PowerUp {
	int power_type = 0;
	float power_x;
	float power_y;
	boolean power_status = false;
	boolean locationChanger = true;
	Player player;
	Random rand = new Random();
	Texture[] power_img_array = new Texture[4];
    

	public PowerUp(Player player) {
		this.player = player;
		power_x = 1000;
		power_y = 1000;
		power_img_array[0] = new Texture("powers/heart.png");
		power_img_array[1] = new Texture("powers/coin.png");
		power_img_array[2] = new Texture("powers/nuke.png");
		power_img_array[3] = new Texture("powers/mark.png");
	}
	public void spawn() {
		if(!power_status) {
			int z= rand.nextInt(2);
			if(z==1) {
				power_status = true;
				locationChanger = true;
				int r = rand.nextInt(4)+1;
				if(r==1) {
					power_type = 1;
				}
				if(r==2) {
					power_type = 2;
				}
				if(r==3) {
					power_type = 3;
				}
				if(r==4) {
					power_type = 4;
				}
			}
		}
	}
	public void tester() {
		if(power_status) {
			Rectangle p = new Rectangle(player.x+2.5f,player.y+2.5f,45,45);
			Rectangle power = new Rectangle(power_x+2.5f,power_y+2.5f,45,45);
			if(p.overlaps(power)) {
				if(power_type == 1) {
					player.hp+=20;
				}
				if(power_type == 2) {
					Main.score+=200;
				}
				if(power_type == 3) {
					Main.score += Main.bots.size*5;
					Main.bots.clear();
				}
				if(power_type == 4) {
			    	for(int i = 1 ; i<=20 ; i++) {
			    		Main.spawnbots();
			    	}
				}
				power_status = false;
			}
		}
	}
	public int type() {
		return (power_type-1);
	}
	public boolean status() {
		return power_status;
	}
	public void setLocation(float x , float y) {
		if(locationChanger) {
			power_x = x;
			power_y = y;
			locationChanger = false;
		}
	}
	public void render(SpriteBatch bat) {
		if(power_status) {
			bat.draw(power_img_array[power_type-1], power_x, power_y);
		}
	}
}
