import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.Random;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {

	// attributes
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; // size of the components, snake, apple
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE * UNIT_SIZE;
	// game speed - the higher the value, the lower the speed
	static final int DELAY = 105;

	// progress bar
	static final int APPLES_PER_LEVEL = 20;

	final int x[] = new int[GAME_UNITS]; // all snake x-coordinates
	final int y[] = new int[GAME_UNITS];

	int bodyParts = 6; // initiial length of the snake
	int applesEaten;
	int appleX; // x-coordinate of the apple (randomly)
	int appleY;

	// generate a bigger apple to give additional points if eaten
	int bigAppleX; // x-coordinate of big apple
	int bigAppleY; // y-coordinate of big apple
	int untilBigApple; // counts until bigApple appears
	boolean showBigApple = false; // don't show the apple until it's time
	int bigAppleTimer = 0; // milliseconds left
	int bigAppleLifeTime = 5000; // 5 seconds
	int pulseSize = 0;
	boolean pulseGrowing = true; // pulsating effect

	char direction = 'R'; // initially going to the right

	boolean running = false;

	Timer timer;
	Random random;

	// constructor
	GamePanel() {
		// instance of random class
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true); // focusibility
		this.addKeyListener(new MyKeyAdapter()); //
		startGame(); // play game
	}

	// method to start game
	public void startGame() {
		// create a new apple on the screen
		newApple();
		running = true; //
		timer = new Timer(DELAY, this); // this - this instance of action listner itaface
		timer.start();
	}

	// restart game
	public void restartGame() {
		bodyParts = 6;
		applesEaten = 0;
		direction = 'R';
		showBigApple = false;
		untilBigApple = 0;
		bigAppleTimer = 0;
		pulseSize = 0;
		pulseGrowing = true;

		for (int i = 0; i < x.length; i++) {
			x[i] = 0;
			y[i] = 0;
		}

		startGame();
	}

	// method to paint the components
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	// draw components
	public void draw(Graphics g) {
		// if the game is running
		if (running) {
			// create a grid along x and y
			for (int i = 0; i <= SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.setColor(Color.DARK_GRAY);
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}

			// drawing the apple
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			// drawing the bigger apple
			if (showBigApple) {
				// pulsating growing/shrinking big apple
				if (pulseGrowing) {
					pulseSize += 1;
					if (pulseSize > 6)
						pulseGrowing = false;
				} else {
					pulseSize -= 1;
					if (pulseSize < 0)
						pulseGrowing = true;
				}

				// big apple is random color,
				g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
				// g.setColor(Color.PINK); // or set it to pink
				g.fillOval(bigAppleX - pulseSize / 2, bigAppleY - pulseSize / 2, UNIT_SIZE + 10 + pulseSize,
						UNIT_SIZE + 10 + pulseSize); // bigger size

				// drawing the timer on the screen
				g.setColor(Color.YELLOW);
				g.setFont(new Font("Arial", Font.PLAIN, 20));
				g.drawString("Bonus Apple: " + (bigAppleTimer / 1000 + 1) + "s", 10, 20);
			}

			// drawing the snake
			for (int i = 0; i < bodyParts; i++) {
				// if it's the head
				if (i == 0) {
					g.setColor(Color.green); // color
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // rectangle for shape
				} else {
					// if the body part is not the head
					g.setColor(Color.yellow);
					// multicolored snake
					// g.setColor(new Color(random.nextInt(255), random.nextInt(255),
					// random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}

			// draw the screen on score/game texts
			g.setColor(Color.yellow);
			g.setFont(new Font("Ink Free", Font.BOLD, 20));
			// font metrics line up font on screen
			FontMetrics metrics = getFontMetrics(g.getFont());
			// centres text on centre of screen, x & y value
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());

			// track progress bar
			drawProgressBar(g);

		} else {
			// execute game over
			gameOver(g);
		}

	}

	// draw progress bar
	private void drawProgressBar(Graphics g) {
		int barWidth = 200;
		int barHeight = 20;
		int barX = (SCREEN_WIDTH - barWidth) / 2;
		int barY = SCREEN_HEIGHT - 40; // 40px from above

		// draw background bar
		g.setColor(Color.darkGray);
		g.fillRect(barX, barY, barWidth, barHeight);

		// draw filled portion
		int filledWidth = (int) (((double) applesEaten / APPLES_PER_LEVEL) * barWidth);
		g.setColor(Color.GREEN);
		g.fillRect(barX, barY, filledWidth, barHeight);

		// draw border
		g.setColor(Color.white);
		g.drawRect(barX, barY, barWidth, barHeight);
	}

	// this should generate a new apple at random position
	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // gets the random coordinate
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; // generate random Y-value coordinate
	}

	// this should generate a big Apple at random position after 5 smaller ones are
	// eaten
	public void newBigApple() {
		bigAppleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		bigAppleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
		showBigApple = true; // should show the big apple
		bigAppleTimer = bigAppleLifeTime; // time left = 5s
	}

	// move the snake
	public void move() {
		// loop through the body parts
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1]; // shifts the x coordinates in the array by 1
			y[i] = y[i - 1];
		}

		// chnage direction
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;

		}

	}

	// check if apple is eaten
	public void checkApples() {
		// check coordinates of both the snake and apple
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++; // add a bodypart
			applesEaten++; // increment the apples eaten
			untilBigApple++;
			newApple(); // generate a new apple

			// create a bigger apple
			if (untilBigApple == 5) {
				newBigApple();
				showBigApple = true;
				untilBigApple = 0;
			}
		}
	}

	// check if big apple is eaten
	public void checkBigApple() {
		// if apple not showing yet don't do anything
		if (!showBigApple)
			return;

		// check if snake eats the big apple
		Rectangle snakeHead = new Rectangle(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
		int bigSize = UNIT_SIZE + 10 + pulseSize;
		Rectangle bigApple = new Rectangle(bigAppleX - pulseSize / 2, bigAppleY - pulseSize / 2, bigSize, bigSize);

		if (snakeHead.intersects(bigApple)) {
			applesEaten += 5;
			// bodyParts += 2; // add 2 body parts
			showBigApple = false; // hide it after eaten
			return; // exit early so timer doesn't have to reach zero after apple is eaten
		}

		// big apple disappears after time expires
		bigAppleTimer -= DELAY;
		if (bigAppleTimer <= 0) {
			showBigApple = false;
		}
	}

	// colisions
	public void checkCollisions() {
		// check head collison with body of snake
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false; // stop the game
			}
		}

		// check if collide with left border
		if (x[0] < 0) {
			running = false;
		}

		// collision with right border
		if (x[0] >= SCREEN_WIDTH) {
			running = false;
		}

		// collision with top border
		if (y[0] < 0) {
			running = false;
		}

		// collision with bottom border
		if (y[0] >= SCREEN_HEIGHT) {
			running = false;
		}

		// stop the timer accordingly
		if (!running) {
			timer.stop();
		}
	}

	// game over method
	public void gameOver(Graphics g) {
		// gme score
		// draw the screen on score/game texts
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		// font metrics line up font on screen
		FontMetrics metrics = getFontMetrics(g.getFont());
		// centres text on centre of screen, x & y value
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());

		// game over big text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		// font metrics line up font on screen
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		// centres text on centre of screen, x & y value
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

		// game restart hint
		g.setColor(Color.yellow);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Press SPACE to restart", (SCREEN_WIDTH - metrics2.stringWidth("Press SPACE to restart")) / 2,
				SCREEN_HEIGHT / 2 + 50);
	}

	// actionperformed method
	@Override
	public void actionPerformed(ActionEvent e) {
		// game running?
		if (running) {
			move(); // call the move method
			checkApples(); // position of apple
			checkBigApple();
			checkCollisions(); // collided with wall or it's self
		}
		// if not running
		repaint();
	}

	// an inner class
	public class MyKeyAdapter extends KeyAdapter {
		// overrides this method
		@Override
		public void keyPressed(KeyEvent e) {
			// examine keys
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_SPACE:
				if (!running) {
					restartGame();
				}
				break;

			}
		}
	}
}
