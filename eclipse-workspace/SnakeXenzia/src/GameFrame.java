import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
//	constructor
	GameFrame () {
		
//		an instance of GamePanel		
		this.add(new GamePanel());
		this.setTitle("Snake Xenzia");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack(); // allows JFrame packing around components
		this.setVisible(true);
		this.setLocationRelativeTo(null); //set window in the middle of screen
	}
}
