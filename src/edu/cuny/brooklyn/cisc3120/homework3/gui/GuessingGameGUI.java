package edu.cuny.brooklyn.cisc3120.homework3.gui;
//OO in Peogress
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import edu.cuny.brooklyn.cisc3120.homework3.core.IClient;
import edu.cuny.brooklyn.cisc3120.homework3.core.IGuesser;


public class GuessingGameGUI implements ActionListener, IClient, IGuesser {
    //private IClient client;
    //private IGuesser guesser;

    private int limit;
    private int maxInt;
    private int target;
    
    //keeps track of how many attempts the user took
    static private int attempts=0;   
    //an array of integers so can track all previous guesses
    static private  int previousGuess[];
    
    // These components are members so they can be modified from the
    // `actionPerformed` method.
    private JFrame window;
    private JLabel guessLabel;
    private JLabel feedbackLabel;
    private JPanel bottomPanel;
    private JButton backSpace;
    private JButton submit;
    private JLabel mainLabel; 
    private JPanel playAgainPanel;
    private JLabel playAgainLabel;
    private JButton Yes;
    private JButton No;
    //My Spinner attempt
    private JSpinner spinner;
    private int currentGuess;
    private Semaphore lock=new Semaphore(1);
     
    public int nextGuess() 
    {
    	try {
			lock.acquire();
		} catch (InterruptedException e) {
			// ignore this?
			e.printStackTrace();
		}	
    	
    	return currentGuess;
    }   
    
	public void win() {
		guessLabel.setText("");
        feedbackLabel.setText("You have bested me and guessed the target. Watch out for my revenge.");
    	submit.setEnabled(false);
    	//playAgain();//user gets the option to play again
	}

	public void lose() {
		guessLabel.setText("");
		feedbackLabel.setText("Thought you'd beat me?? You can't! YOU LOSE!!");
    	submit.setEnabled(false);
    	//playAgain();//user gets the option to play again
	}

	public void tooLow(int guess) {
		guessLabel.setText("");
		feedbackLabel.setText("Too Low!");	
	}

	public void tooHigh(int guess) {
		guessLabel.setText("");
		feedbackLabel.setText("Too High!");
	}
    
	public GuessingGameGUI(int limit, int maxInt, int target)
    {
        this.limit = limit;
        this.maxInt = maxInt;
        this.target = target;
    }
	
	//checks if the user has entered the same number more then once
	static boolean checkGuess(int currentGuess)
	{
		//iterates thorough all the previous guesses
		for(int i=0; i< attempts; i++){
			if(previousGuess[i] == currentGuess)
				return true;//returns true if they already entered that number
		}
		//since this will only happen if they didn't enter a duplicate
		return false;//returns false
	}	
	
    private void handleBS()
    {
        String text = guessLabel.getText();
        text = text.substring(0, text.length() - 1);
        if (text.equals("")) {
            text = "0";
            backSpace.setEnabled(false);
        }
        guessLabel.setText(text);
    }

    private void handleSubmit()
    {       
        String text = guessLabel.getText();
               
        try {
            int value = Integer.parseInt(text);
            currentGuess=value;
            
        	//checks for valid input (value < 16) (will hopefully be replaced with the spinner)
            if(value > maxInt){
            	guessLabel.setText("");
        		feedbackLabel.setText("So you seriously want to go with that number?? Read the rules.");
        		return;
            }
            
            //checks if the user already entered this number
            if(checkGuess(value)){
            	guessLabel.setText("");
        		feedbackLabel.setText("Don't be stupid, you already guessed that number.");
        		return;
            } else{
            	//puts the current guess in the array
            	previousGuess[attempts] = value;
            }
    		
/*            
            else if (value > target) {
            	guessLabel.setText("");
                feedbackLabel.setText("Too High!");
                attempts++;
            } else if (value < target) {
            	guessLabel.setText("");
                feedbackLabel.setText("Too Low!");
                attempts++;
            } else {
            	guessLabel.setText("");
            	feedbackLabel.setText("You have bested me and guessed the target. Watch out for my revenge.");
                submit.setEnabled(false);
                playAgain();//user gets the option to play again
            }
          
            if(attempts >= limit){
            	feedbackLabel.setText("Thought you'd beat me?? You can't! YOU LOSE!!");
            	submit.setEnabled(false);
            	playAgain();//user gets the option to play again
            }
*/           
            return;
            
        } catch (NumberFormatException ex) {
            // Ignore integer parse exception...
            // What can cause this exception to be thrown?
            feedbackLabel.setText("You're Dumb!");
        }
        finally
        {
        	lock.release();
        }    
        
        } 

    private void handleNumber(int value)
    {
        
        String text = guessLabel.getText();
        backSpace.setEnabled(true);

        if (text.equals("0")) {
            text = "";
        }
        text += Integer.toString(value);
        guessLabel.setText(text);
    }

    public void actionPerformed(ActionEvent e)
    {
        // You can "debug" your code by printing to stdout.
        // If you are using eclips, the results will be written to the eclipse
        // log console.
        //
        // This command is printing out the action command for the button that
        // was pressed.
        System.out.println(e.getActionCommand());

        // Is the button that triggered this event a number button?
        try {
            int value = Integer.parseInt(e.getActionCommand());
            handleNumber(value);
            return;
        } catch (NumberFormatException ex) {
            // Ignore integer parse exception...
            // This happens when "submit" or "BS" is pressed.
        }

        // Is it the backspace button?
        if (e.getActionCommand().equals("BS")) {
            handleBS();
            return;
        }

        // Is it the submit button?
        if (e.getActionCommand().equals("submit")) {
            handleSubmit();
            return;
        }
        //Is it the Yes button (to play another round)
        if(e.getActionCommand().equals("YES")){
        	handleYes();
        	return;
        }
        //Is it the No button (to exit)
        if(e.getActionCommand().equals("NO")){
        	System.exit(0);
        }      
    }

    private JFrame createFrame()
    {
    	JFrame frame = new JFrame("The Guessing Game!");
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(
        	new BoxLayout(
        		frame.getContentPane(),
        		BoxLayout.Y_AXIS)
        );
        return frame;
    }

    private JLabel createMainLabel() {
    	JLabel mainLabel = new JLabel();
    	mainLabel.setText(
            "Guess a number between 1 and " + Integer.toString(this.maxInt));
        mainLabel.setFont(
        	new Font("Arial", Font.BOLD, 22));
        return mainLabel;
    }

    private JLabel createFeedBackLabel() {
    	JLabel feedbackLabel = new JLabel();
        feedbackLabel.setFont(
        	new Font("Arial", Font.BOLD, 22));
        return feedbackLabel;
    }

    private JLabel createGuessLabel() {
    	JLabel guessLabel = new JLabel("0");
        guessLabel.setFont(
        	new Font("Arial", Font.BOLD, 22));
        guessLabel.setOpaque(true);
        guessLabel.setBackground(Color.white);
        return guessLabel;
    }

    private JPanel createNumberPad()
    {
    	JPanel numberPad = new JPanel();
        // If you wanted to, you could make the panel blue,
        // to see where exactly it starts and ends.
        // numberPad.setBackground(Color.BLUE);
        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(Integer.toString(i));
            numberPad.add(button);
            // Call `this.actionPerformed` when the button is pressed.
            button.addActionListener(this);
        }
        return numberPad;
    }
   
    private JSpinner createJSpinner(){
    	SpinnerModel model =new SpinnerNumberModel(0, 0, 16, 1);//should allow user to enter 0? 
    	JSpinner spinner = new JSpinner(model);
    	
    	return spinner;
    }
    
    private JPanel createBottomPanel()
    {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setMinimumSize(new Dimension(32, 400));
        return bottomPanel;
    }

    private JButton createBackSpace()
    {
        JButton backSpace = new JButton("<");
        backSpace.setActionCommand("BS");
        // The backspace will start "disabled".
        // This means you will not be able to click on it until there is
        // something to erase.
        backSpace.setEnabled(false);
        // Call `this.actionPerformed` when the button is pressed.
        backSpace.addActionListener(this);
        return backSpace;
    }

    private JButton createSubmitButton()
    {
    	JButton submit = new JButton("Submit Guess");
        submit.setActionCommand("submit");
        // Call `this.actionPerformed` when the button is pressed.
        submit.addActionListener(this);
        return submit;
    }

    private JPanel createPlayAgainPanel()
    {
        JPanel playAgainPanel = new JPanel();
        playAgainPanel.setMinimumSize(new Dimension(32, 400));
        return playAgainPanel;
    }
    
    private JLabel createPlayAgainLabel() {
    	JLabel playAgainLabel = new JLabel();
    	playAgainLabel.setText("Would you like to play again?");
    	playAgainLabel.setFont(
        	new Font("Arial", Font.BOLD, 18));
    	playAgainLabel.setVisible(false);
    	
        return playAgainLabel;
    }
    
    private JButton createYesButton()
    {
    	JButton Yes = new JButton("Yes");
        Yes.setActionCommand("YES");
        // Call `this.actionPerformed` when the button is pressed.
        Yes.addActionListener(this);
        Yes.setVisible(false);
        return Yes;
    }
  
    private void handleYes()
    {
    	window.setVisible(false);
    	attempts = 0;//resets attempts
    	previousGuess = new int[limit];//acts in place of resetting the array
    	display();
        
    }

    private JButton createNoButton()
    {
    	JButton No = new JButton("No");
        No.setActionCommand("NO");
        // Call `this.actionPerformed` when the button is pressed.
        No.addActionListener(this);
        No.setVisible(false);
        return No;
    }
    
    public void playAgain()
    {
    	bottomPanel.setVisible(false);//replaces bottom panel with playAgain panel and all of it's 'components'
    	playAgainPanel.setVisible(true);//it then prompts the user to select if they want to play again or not
    	playAgainLabel.setVisible(true);//the selection is handled by the Yes or No button.
    	No.setVisible(true);
    	Yes.setVisible(true);
    }
    
    public void display()
    {
        // Create all of the components.
        window = createFrame();
        submit = createSubmitButton();
        JPanel numberPad = createNumberPad();
        spinner = createJSpinner();
        mainLabel = createMainLabel();
        guessLabel = createGuessLabel();
        feedbackLabel = createFeedBackLabel();
        bottomPanel = createBottomPanel();
        backSpace = createBackSpace();
        previousGuess = new int[limit];
        playAgainPanel = createPlayAgainPanel();
        playAgainLabel = createPlayAgainLabel();
        Yes = createYesButton();
        No = createNoButton();

        // Add the backspace button to the numberpad.
        numberPad.add(backSpace);

        // Add submit button and guesslabel to the bottom panel.
        // Why do the components go from left to right?
        // What is the layout manager used by the panel component?
        bottomPanel.add(submit);
        bottomPanel.add(guessLabel);
        
        //Add the playAgainLabel & the Yes and No button to the playAgainPanel
        playAgainPanel.add(playAgainLabel);
        playAgainPanel.add(Yes);
        playAgainPanel.add(No);
        
        // Add the components to the window.
        window.add(mainLabel);
        window.add(numberPad);
        //window.add(spinner);
        window.add(feedbackLabel);
        window.add(bottomPanel);
        window.setVisible(true);
        
        window.add(playAgainPanel);
    }


}
