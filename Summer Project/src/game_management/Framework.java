package game_management;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.media.*;

import javax.imageio.ImageIO;


@SuppressWarnings("serial")
public class Framework extends Canvas
{
	//Instantiate Enumerator
	public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED, FIGHTER_SELECT};
	
	//Instantiate Variables
	public static int frameWidth;
	public static int frameHeight;
	public static GameState gameState;
	private long gameTime;
	private long lastTime;
	private long lastMainMusicStart;
	private long mainMusicStartTime;
	private Game game;
	private BufferedImage menuImg;
	private BufferedImage optionsImg;
	private BufferedImage fighterSelectImg;
	private AudioClip mainMenuTheme;
	private boolean mainMenuThemeState = false;
	boolean mouseState;
	double mouseX;
	double mouseY;
	
	//Initialize Variables
	public static final long secInNanoSec = 1000000000L;
	public static final long milisecInNanoSec = 1000000L;
	private final int GAME_FPS = 60;
	private final long GAME_UPDATE_PERIOD = (secInNanoSec * 3)/ GAME_FPS;
	private int mainMusicLength = 48;

	//Construct Framework
	public Framework()
	{
		super();
		
		//Set game state
		gameState = GameState.VISUALIZING;
		
		//Start new thread for the Game loop
		Thread gameThread = new Thread()
		{
			public void run()
			{
				GameLoop();
			}
		};
		gameThread.start();
	}
	
	//Initialize the game
	private void Initialize()
	{
		
	}
	
	//Load game content
	private void LoadContent()
	{
		//Load fighter image and initialize width and height variables. If the image does not exist log a severe error.
		try 
		{
			URL menuImgURL = this.getClass().getResource("/resources/images/menu.jpg");
			menuImg = ImageIO.read(menuImgURL);
			
			URL optionsImgURL = this.getClass().getResource("/resources/images/OptionsMenu.jpg");
			optionsImg = ImageIO.read(optionsImgURL);
			
			URL fighterSelectImgURL = this.getClass().getResource("/resources/images/FighterSelectMenu.jpg");
			fighterSelectImg = ImageIO.read(fighterSelectImgURL);
			
			URL MainMenuThemeURL = this.getClass().getResource("/resources/sounds/MainMenuTheme.wav");
			mainMenuTheme = new AudioClip(MainMenuThemeURL.toString());

		}catch(IOException ex)
		{
			Logger.getLogger(Framework.class.getName()).log(Level.SEVERE,null,ex);
		}
	}
	
	//Loops through the game
	private void GameLoop()
	{
		//Initialize variables
		long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
		
		//Instantiate variables
		long beginTime, timeTaken,timeLeft;
		
		//Loops through the game switch
		while(true)
		{
			beginTime = System.nanoTime();
			
			//Switch based on what state the game is in
			switch(gameState)
			{
			//If game is playing update the game time and Update the game
			case PLAYING:
				gameTime += System.nanoTime() - lastTime;
				game.UpdateGame(gameTime, mousePosition());
				lastTime = System.nanoTime();
				break;
			case GAMEOVER:
				break;	
			//If the game is in the main menu
			case MAIN_MENU:
				//Get Mouse position
				mouseX = mousePosition().getX();
				mouseY = mousePosition().getY();
				//System.out.println(System.nanoTime() - lastMainMusicStart > mainMusicLength);
				
				//If the Main Menu Theme is finished playing then start the Main Menu Theme again
				if(System.nanoTime() - lastMainMusicStart > mainMusicLength * secInNanoSec)
				{
					System.out.println(System.nanoTime() / secInNanoSec);
					mainMenuThemeState = false;
					startMainMenuTheme();
				}
				
				//If the mouse is on the Single Player button and the player clicks then change gameState to Fighter_Select
				if(mouseX > frameWidth/2 - 55 && mouseX < frameWidth/2 + 65)
				{
					if(mouseY > frameHeight/2 - 120 && mouseY < frameHeight/2 - 95)
					{				
						if(mouseState == true)
						{
							gameState = GameState.FIGHTER_SELECT;
						}
					}	
				}
				
				//If the mouse is on the Options button and the player clicks then change gameState to Fighter_Select
				if(mouseX > frameWidth/2 - 55 && mouseX < frameWidth/2 + 65)
				{
					if(mouseY > frameHeight/2 - 80 && mouseY < frameHeight/2 - 55)
					{
						if(mouseState == true)
						{
							gameState = GameState.OPTIONS;
						}
					}	
				}
				break;
			case OPTIONS:
				break;
			case FIGHTER_SELECT:
				break;
			case GAME_CONTENT_LOADING:
				break;
			//Once game is started Initialize and LoadContent for the Main Menu then change gameState to Main Menu
			case STARTING:
				Initialize();
				LoadContent();
				gameState = GameState.MAIN_MENU;
				break;
			//Initialize frameWidth and FrameHeight based on the framework generated
			case VISUALIZING:
				if(this.getWidth() > 1 && visualizingTime > secInNanoSec)
				{
					frameWidth = this.getWidth();
					frameHeight = this.getHeight();
					gameState = GameState.STARTING;
				}
				else
				{
					visualizingTime += System.nanoTime() - lastVisualizingTime;
					lastVisualizingTime = System.nanoTime();
				}
				break;
			default:
				break;
			}
			
			//Repaints the canvas
			repaint();
			
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanoSec;
			if(timeLeft < 10)
				timeLeft = 10;
			try{
				Thread.sleep(timeLeft);
			}catch(InterruptedException ex){}
		}
	}
	
	//Draw the canvas based on gameState
	public void Draw(Graphics2D g2d)
	{
		switch(gameState)
		{
			case PLAYING:
				game.Draw(g2d, mousePosition());
				break;
			case GAMEOVER:
				//game.DrawGameOver(g2d, mousePosition(), gameTime);
				break;
			case MAIN_MENU:
				g2d.drawImage(menuImg, 0, 0, frameWidth, frameHeight, null);
				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
				g2d.drawString("Single Player", frameWidth/2 - 45, frameHeight/2 - 100);
				g2d.drawString("Options", frameWidth/2 - 30, frameHeight/2 - 60);
				g2d.drawRect(frameWidth/2 - 55, frameHeight/2 - 120, 120, 25);
				g2d.drawRect(frameWidth/2 - 55, frameHeight/2 - 80, 120, 25);
				break;
			case OPTIONS:
				g2d.drawImage(optionsImg, 0, 0, frameWidth, frameHeight, null);
				break;
			case FIGHTER_SELECT:
				g2d.drawImage(fighterSelectImg, 0, 0, frameWidth, frameHeight, null);
				break;
			case GAME_CONTENT_LOADING:
				g2d.setColor(Color.white);
				g2d.drawString("LOADING...", frameWidth/2 -5, frameHeight/2);
		default:
			break;
		}
	}
	
	//Starts a new Game
	@SuppressWarnings("unused")
	private void newGame()
	{
		gameTime = 0;
		lastTime = System.nanoTime();
		game = new Game();
	}
	
	//Restarts the game
	@SuppressWarnings("unused")
	private void restartGame()
	{
		gameTime = 0;
		lastTime = System.nanoTime();
		
		//game.restartGame();
		
		gameState = GameState.PLAYING;
	}
	
	//Gets mouse position
	private Point mousePosition()
	{
		try
		{
			Point mp = this.getMousePosition();
			
			if(mp != null)
				return this.getMousePosition();
			else
				return new Point(0,0);
		}catch(Exception e)
		{
			return new Point(0,0);
		}
	}


	@Override
	public void keyReleasedFramework(KeyEvent e){}

	//If the Left Mouse Button in pressed then change mouseState to true
	public void mousePressed(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
			mouseState = true;
	}
	
	//If the Left Mouse Button is released then change mouseState to false
	public void mouseReleased(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
			mouseState = false;
	}
	
	//Starts the main music theme
	public void startMainMenuTheme()
	{	
		
		if(mainMenuThemeState == false)
		{
			//System.out.println("Audio started.");
			lastMainMusicStart = System.nanoTime();
			mainMenuTheme.setVolume(0.1);
			mainMenuTheme.play();
			mainMenuThemeState = true;
			
		}
		

		
	}
}
