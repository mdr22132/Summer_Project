package game_management;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame
{
	//Window Constructor
	@SuppressWarnings("unused")
	private Window()
	{
		//Set window title
		this.setTitle("Summer Project");
		
		//If true sets the window to full screen
		if(false)
		{
			this.setUndecorated(true);
			this.setExtendedState(Window.MAXIMIZED_BOTH);
		}
		//Else set screen size to 800x600
		else
		{
			this.setSize(800,600);
			this.setLocationRelativeTo(null);
			this.setResizable(false);
		}
		
		//Set default close operation and make visible
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//Set the content pane to a new framework
		this.setContentPane(new Framework());
	}
	
	//Begin game
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new Window();
			}
		});
	}
	
}
