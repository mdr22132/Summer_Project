package game_management;

import fighter_management.Fighter;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Game 
{
	//Instantiate variables
	private Fighter fighterTemp;
	private BufferedImage backgroundImg;
	
	//Create a Game object
	public Game()
	{
		//Set gameState to Game Content Loading
		Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
		
		Thread threadForInitGame = new Thread()
		{
			//Initialize and Load content and set gameState to playing
			public void run()
			{
				Initialize();
				LoadContent();
				Framework.gameState = Framework.GameState.PLAYING;
			}
		};
		
		threadForInitGame.start();
	}
	//Initializes a fighter object
	private void Initialize()
	{
		fighterTemp = new Fighter();
	}
	
	//Loads background image
	private void LoadContent()
	{
		//Load background image. If the image does not exist log a severe error.
		try
		{
			URL backgroundImgUrl = this.getClass().getResource("/resources/images/background.jpg");
			backgroundImg = ImageIO.read(backgroundImgUrl);
			
		}catch(IOException ex){
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//Update the fighter
	public void UpdateGame(long gameTime, Point mousePosition)
	{
		fighterTemp.Update();
	}
	
	//Draw the fighter to the canvas
	public void Draw(Graphics2D g2d, Point mousePosition)
	{
		g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		fighterTemp.Draw(g2d);
	}
}
