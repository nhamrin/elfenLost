/**
 * The MyGdxGame class is the main java class including initialising objects,
 * rendering them at a certain FPS's loop and lastly disposing of unused items
 * at the end of the game (when exiting the application).
 *
 * This class calls methods from the GameCharacter class (defining
 * the character stats and properties). MyGdxGame also extends ApplicationAdapter.
 *
 * @author Nils Hamrin, Filip Öhlin
 * @version 2022-05-20
 */

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture playerSprite, banditOneSprite, banditTwoSprite, banditThreeSprite, ogreSprite,
	fishermanSprite;
	Texture backgroundArray[] = new Texture[16];
	public static Sprite backgroundSprite;
	OrthographicCamera camera;
	int playerX;
	int playerY;
	int roomCounter = 4;
	int deathWaiter = 0;
	private GameCharacter player, bandits, ogre;
	private BitmapFont font;
	
	/**
     * A 'constructor type' creating and initialising
     * the correct camera settings for player view, and setting up
     * batches and textures to be used in the game.
     */
	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(7);
		
		playerSprite = new Texture("elf.png");
		banditOneSprite = new Texture("bandit1.png");
		banditTwoSprite = new Texture("bandit2.png");
		banditThreeSprite = new Texture("bandit3.png");
		ogreSprite = new Texture("ogre.png");
		fishermanSprite = new Texture("fisherman2.png");
		
		//Loads the backgrounds into an array
		for (int i = 4; i <= 12; i = i + 2) {
			backgroundArray[i] = new Texture("room"+i+".jpg");
		}
		
		for (int i = 3; i <= 9; i = i + 2) {
			backgroundArray[i] = new Texture("room"+i+".jpg");
		}
		
		backgroundArray[0] = new Texture("room0.jpg");
		backgroundArray[15] = new Texture("room15.jpg");
		
		player = new GameCharacter(50, 2, 0, 2);
		bandits = new GameCharacter(10, 4, 0, 0);
		ogre = new GameCharacter(100, 10, 0, 10000000);
		
		// Start players X and Y position in middle of screen
		playerX = Gdx.graphics.getWidth()/2 - playerSprite.getWidth()/2;
		playerY = Gdx.graphics.getHeight()/2 - playerSprite.getHeight()/2;
		
		// Set game view to orthographic camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	/**
	 * A rendering loop updating at 60FPS per second (decided in DesktopLauncher
	 * class) updating and sending images to the screen as the game is played.
	 */
	@Override
	public void render() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		//Renders the background
		backgroundSprite = new Sprite(backgroundArray[roomCounter]);
		backgroundSprite.draw(batch);
		
		if (roomCounter == 3) {
			batch.draw(fishermanSprite, 500, 500, 110, 110);
		}
		else if (roomCounter == 15 && bandits.getHealth() > 0) {
			batch.draw(banditOneSprite, 400, 500, 96, 120);
			batch.draw(banditTwoSprite, 500, 500, 96, 120);
			batch.draw(banditThreeSprite, 600, 500, 96, 120);
		}
		else if (roomCounter == 5 && ogre.getHealth() > 0) {
			batch.draw(ogreSprite, 500, 500, 256, 256);
		}
		else if (roomCounter == 10) {
			//Travelling merchant here
		}
		else if (roomCounter == 7) {
			if (deathWaiter < 60) {
				font.draw(batch, "YOU'RE WINNER !", 525, 650);
				deathWaiter++;
			}
			else {				
				exiter();
			}			
		}
		
		//Renders the playable character
		if (player.getHealth() > 0 && roomCounter != 7) {
			batch.draw(playerSprite, playerX, playerY, 96, 128);
		}
		else if (player.getHealth() < 0) {
			if (deathWaiter < 60) {
				font.draw(batch, "YOU'RE DEAD !", 525, 650);
				deathWaiter++;
			}
			else {				
				exiter();
			}
		}
		
		batch.end();
		
		// Screen/window mode, toggle fullscreen ON/OFF
		if (Gdx.input.isKeyPressed(Input.Keys.F)) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.G)) {
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}

		// Keyboard input, player movement
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerX -= 500 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerX += 500 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerY -= 450 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerY += 450 * Gdx.graphics.getDeltaTime();
        }

        // Screen borders hindering movement outside screen
        // The rooms are indexed (see the indexed game map) and will increment or decrement by a
        // certain value when the playable character touches a border. This will change which
        // background is rendered in the next render-loop.
        if (playerX < 0) {
        	if (roomCounter == 3 || roomCounter == 6 || roomCounter == 8 || roomCounter == 9 || roomCounter == 12 || roomCounter == 15) {
            	roomCounter -= 3;
        		playerX = Gdx.graphics.getWidth() - playerSprite.getWidth();;
        	}
        	else {
        		playerX = 0;
        	}
        }

        if (playerX > Gdx.graphics.getWidth() - playerSprite.getWidth()) {
        	if (roomCounter == 0 || roomCounter == 3 || roomCounter == 5 || roomCounter == 6 || roomCounter == 8 || roomCounter == 9 || roomCounter == 12) {
        		if (roomCounter == 8) {
        			roomCounter += 4;
        			playerX = (Gdx.graphics.getWidth() - playerSprite.getWidth()) / 2;
        			playerY = Gdx.graphics.getHeight() - playerSprite.getHeight(); 
        		}
        		else {
        			roomCounter += 3;
            		playerX = 0;
        		}
        	}
        	else {
        		playerX = Gdx.graphics.getWidth() - playerSprite.getWidth();
        	}
        }

        if (playerY < 0) {
        	if (roomCounter == 6 || roomCounter == 8 || roomCounter == 10) {
        		roomCounter -= 2;
        		playerY = Gdx.graphics.getHeight() - playerSprite.getHeight();
        	}
        	else {
        		playerY = 0;
        	}
        }

        if (playerY > Gdx.graphics.getHeight() - playerSprite.getHeight()) {
        	if (roomCounter == 4 || roomCounter == 5 || roomCounter == 6 || roomCounter == 8 || roomCounter == 12) {
        		if (roomCounter == 12) {
        			roomCounter -= 4;
        			playerX = Gdx.graphics.getWidth() - playerSprite.getWidth();
        			playerY = (Gdx.graphics.getHeight() - playerSprite.getHeight()) / 2;
        		}
        		else {
            		roomCounter += 2;
            		playerY = 0;
        		}
        	}
        	else {
        		playerY = Gdx.graphics.getHeight() - playerSprite.getHeight();
        	}
        }
        
        if (roomCounter == 3) {
        	if (Gdx.input.isTouched()) {
        		player.sellFish();
    		}
        }
        else if (roomCounter == 0) {
        	player.aquireFish();
        }
		else if (roomCounter == 15) {
			if (bandits.getHealth() > 0) {
				if (Gdx.input.isTouched()) {
					player.takeDamage(bandits.getStrength());
					bandits.takeDamage(player.getStrength());
				}
			}
		}
		else if (roomCounter == 5) {
			if (ogre.getHealth() > 0) {
				if (Gdx.input.isTouched()) {
					player.takeDamage(ogre.getStrength());
					ogre.takeDamage(player.getStrength());
				}
			}
		}
		else if (roomCounter == 10) {
			//Travelling merchant here
		}
	}
	
   	/**
     * Method that will wait for 3,5 seconds and then exit the game
     */
	public void exiter() {
		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
		}
		
		Gdx.app.exit();
	}
	
	/**
	 * When the application is exited, items that where either 
	 * created during the 'create()' method or rendered during 'render()'
	 * are thrown away in the 'garbage collector'. 
	 */
	@Override
	public void dispose() {
		batch.dispose();
		playerSprite.dispose();
		backgroundSprite.getTexture().dispose();
	}
}