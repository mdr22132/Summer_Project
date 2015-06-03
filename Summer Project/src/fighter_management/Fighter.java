package fighter_management;

import game_management.Canvas;
import game_management.Framework;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Fighter 
{
	//Instantiate Fighter Variables
	public int x;
	public int y;
	public String name;
	private BufferedImage fighterImg;
	private String fighterImgPath;
	public int fighterImgWidth;
	public int fighterImgHeight;

	//Initialize and Load fighter content
	public Fighter()
	{
		Initialize();
		LoadContent();
	}
	
	//Initialize fighter
	public void Initialize()
	{
		 
	}
	
	//Load fighter
	public void LoadContent()
	{
		//Load fighter image and initialize width and height variables. If the image does not exist log a severe error.
		try
		{
			URL fighterImgURL = this.getClass().getResource(fighterImgPath);
			fighterImg = ImageIO.read(fighterImgURL);
			fighterImgWidth = fighterImg.getWidth();
			fighterImgHeight = fighterImg.getHeight();
		}catch (IOException ex)
		{
			Logger.getLogger(Fighter.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//Update the fighter image
	public void Update()
	{
		//Move fighter left
		if(Canvas.keyboardKeyState(KeyEvent.VK_A) && (x > 0 + fighterImgWidth/2))
			x -= 10;
		
		//Move fighter right
	    if(Canvas.keyboardKeyState(KeyEvent.VK_D) && (x < Framework.frameWidth - fighterImgWidth/2))
	        x  += 10;
	}
	
	//Draw the fighter image to the canvas
	public void Draw(Graphics2D g2d)
	{
		g2d.drawImage(fighterImg, x, y, null);
	}
	
	//Setters and Getters for fighter variables
	public int getX() 
	{
		return x;
	}
	public void setX(int x) 
	{
		this.x = x;
	}
	public int getY() 
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public BufferedImage getfighterImg()
	{
		return fighterImg;
	}
	public void setfighterImg(BufferedImage fighterImg) 
	{
		this.fighterImg = fighterImg;
	}
	public String getfighterImgPath()
	{
		return fighterImgPath;
	}
	public void setfighterImgPath(String fighterImgPath)
	{
		this.fighterImgPath = fighterImgPath;
	}
	public int getfighterImgWidth() 
	{
		return fighterImgWidth;
	}
	public void setfighterImgWidth(int fighterImgWidth) 
	{
		this.fighterImgWidth = fighterImgWidth;
	}
	public int getfighterImgHeight()
	{
		return fighterImgHeight;
	}
	public void setfighterImgHeight(int fighterImgHeight) 
	{
		this.fighterImgHeight = fighterImgHeight;
	}
	
	
}
