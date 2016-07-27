package edu.cuny.brooklyn.cisc3120.homework3.cli;

import edu.cuny.brooklyn.cisc3120.homework3.core.*;
import edu.cuny.brooklyn.cisc3120.homework3.gui.GuessingGameGUI;

import java.util.Random;

public class MainCLI {
	static final int limit = 4;
    static final int maxInteger = 16;
    
    // This creates a new random number generator
    static Random rand = new Random();
    
    // This generates a random integer.  Note the "+1"!
    static int target = rand.nextInt(maxInteger) + 1;
    
    public static void main(String[] args) throws Exception {
        // TODO:  You can just hard code your config for this assignment.
        // Parsing commandline arguments is optional.
        Configuration myConfig = new Configuration(16, 4);

        // Resolve and inject dependencies.
        //IClient myClient = new ClientCLI();

    	GuessingGameGUI gui = new GuessingGameGUI(limit, maxInteger, target);
    	gui.display();
    	
        // You can use a random guesser:
        // IGuesser myGuesser = new RandomGuesser(myConfig);

        // Or the command-line guesser.
        //IGuesser myGuesser = new GuesserCLI(myConfig);

        IChooser myChooser = new RandomChooser(myConfig);

        Game myGame = new Game(myChooser, gui, gui, myConfig);
        myGame.play();
        
    }


}

