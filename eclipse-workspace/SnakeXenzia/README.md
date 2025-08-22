# Snake Xenzia in Java

A simplified **Snake Xenzia clone** built in Java using **GameFrame** and **GamePanel**.
Most of the game code and logic is inspired by **[@BroCodez](https://www.youtube.com/@BroCodez)** on YouTube. 
This project was created as a learning exercise to get started with building games in Java using GameFrames and GamePanels. 

---

## Features 
- Automatic Snake spawning from the left side of the screen.
- Constant score display.
- Automatic score booster when a bigger apple is eaten. 
- Snake grows in size with each apple. 
- Progress bar (future: will represent level completion). 
- Easy game restart from the game over screen. 

--- 

## Prerequisites 
- **Java 11+** with appropriate JDK & SDK installed. 
- Any IDE will work (project developed in **Eclipse IDE**). 

--- 

## Running the Game 
1. Clone the repository:
   ```bash
   git clone https://github.com/MartiniisLuther/SnakeXenzia_In_Java.git

2. Open the project in your IDE.
3. Run the main entry point: SnakeGame.java.


## Project Structure SnakeXenzia_In_Java/
 src/
    SnakeGame.java  	 # Main entry point 
    GameFrame.java 	 # Frame (window) setup 
    GamePanel.java 	 # Core game logic, rendering, and updates 
    Apple.java		 # Apple and score booster logic

 .gitignore 
 README.md
 LICENSE


## Licence
This project is licensed under the MIT License.
See the LICENSE file for details.


## Attribution 
Large parts of the codebase are based on tutorials and code from @BroCodez on YouTube.
This project was adapted and extended for personal learning purposes.
