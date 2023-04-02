import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class Memory_Test extends JFrame {

	private JPanel contentPane;
	public int seconds = 5, milliseconds = 1000;
	public Timer timer;

	private int windowWidth = 657;
	private int windowHeight = 405;
	private int lblWidth = 96;
	private int lblHeight = 57;

	private String choice = "";

	private int charCount = 1;
	private int charLimit;

	private String[] actualAnswer = new String[10];
	// since we want a limit on how many characters can be memorized
	// (to not make the game infinitely long or slow to process),
	// it is fine to use an array
	// instead of a different data structure with flexible size

	private int currentLevel = 1;
	private int levels = 3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Memory_Test frame = new Memory_Test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Memory_Test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 647, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Random r = new Random();
		char c = (char) (r.nextInt(26) + 'a');
		actualAnswer[0] = c + "";

		JLabel randomLetter = new JLabel(c + "");
		randomLetter.setForeground(Color.RED);
		randomLetter.setFont(new Font("Tahoma", Font.BOLD, 20));
		randomLetter.setBounds(245, 145, lblWidth, lblHeight);
		contentPane.add(randomLetter);
		randomLetter.setVisible(false);

		// Timer
		ActionListener timerListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (charCount == charLimit) {
					timer.stop();
					for (String elem : actualAnswer) {
						if (elem != null) {
							System.out.println(elem);
						}
					}

					int rndLet = (int) Math.floor(Math.random() * ((charLimit - 1) - 0 + 1) + 0);
					
					// choice of game based on radio buttons
					boolean match = false;
					if (choice.equals("all characters")) {
						match = memorizeAll();
					}
					if (choice.equals("specific characters")) {
						System.out.println(charLimit);
						match = memorizeSpecific(rndLet);
					}

					if (match == true) {
						JOptionPane.showMessageDialog(null, "Correct");
						if (currentLevel < levels) {
							char c = nextLevel(r);
							randomLetter.setText(c + "");
							randomLetter.setVisible(true);
							timer.start();
						} else {
							JOptionPane.showMessageDialog(null, "Thank you for playing.");
							dispose();
						}
					} else {
						String correctAnswer = "";
						if (choice.equals("all characters")) {
							for (String elem : actualAnswer) {
								if (elem != null) {
									correctAnswer += elem;
								}
							}
						}
						if (choice.equals("specific characters")) {
							correctAnswer += actualAnswer[rndLet];
						}

						JOptionPane.showMessageDialog(null, "Incorrect. The answer is: " + correctAnswer);
						if (currentLevel < levels) {
							char c = nextLevel(r);
							randomLetter.setText(c + "");
							randomLetter.setVisible(true);
							timer.start();
						} else {
							JOptionPane.showMessageDialog(null, "Thank you for playing.");
							dispose();
						}
					}
				}

				++milliseconds;
				if (milliseconds >= 15) {
					--seconds;
					milliseconds = 0;
				}

				if (seconds <= 0) {
					int randWidth = (int) Math.floor(Math.random() * ((windowWidth - 60) - 60 + 1) + 60);
					int randHeight = (int) Math.floor(Math.random() * ((windowHeight - 60) - 60 + 1) + 60);

					randomLetter.setVisible(false);
					randomLetter.setBounds(randWidth, randHeight, lblWidth, lblHeight);

					char c = (char) (r.nextInt(26) + 'a');
					randomLetter.setText(c + "");

					actualAnswer[charCount] = c + "";
					charCount++;

					randomLetter.setVisible(true);
					milliseconds = 1000;
					seconds = 5;
				}
			}
		};

		timer = new Timer(1, timerListener);

		JLabel lblHowManyLetters = new JLabel("How many letters do you want to memorize?");
		lblHowManyLetters.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblHowManyLetters.setBounds(2, 94, 355, 22);
		contentPane.add(lblHowManyLetters);

		JLabel lblWouldYouLike = new JLabel(
				"Would you like to memorize all the random letters or be asked for specific characters (ex. 2nd letter)?");
		lblWouldYouLike.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWouldYouLike.setBounds(2, 176, 634, 22);
		contentPane.add(lblWouldYouLike);

		JLabel lblGameTitle = new JLabel("Welcome to the Memory Test!");
		lblGameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameTitle.setFont(new Font("Tahoma", Font.ITALIC, 30));
		lblGameTitle.setBounds(0, 0, 633, 35);
		contentPane.add(lblGameTitle);

		JLabel lblGameDescription = new JLabel(
				"Please configure the following settings \r\nbefore starting the game \r\nto ensure your experience suits your needs.");
		lblGameDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblGameDescription.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameDescription.setBounds(0, 46, 633, 29);
		contentPane.add(lblGameDescription);

		JSpinner spinChooseNumLevels = new JSpinner();
		spinChooseNumLevels.setFont(new Font("Tahoma", Font.BOLD, 20));
		spinChooseNumLevels.setModel(new SpinnerNumberModel(1, 1, 10, 1)); // set maximum value to 10 to match size of
																			// "actualAnswer" array
		spinChooseNumLevels.setBounds(2, 294, 76, 40);
		contentPane.add(spinChooseNumLevels);

		JLabel lblNumLevels = new JLabel("How many levels?");
		lblNumLevels.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNumLevels.setBounds(2, 261, 145, 22);
		contentPane.add(lblNumLevels);

		JSpinner spinChooseCharCount = new JSpinner();
		spinChooseCharCount.setModel(new SpinnerNumberModel(1, 1, 10, 1));
		spinChooseCharCount.setFont(new Font("Tahoma", Font.BOLD, 20));
		spinChooseCharCount.setBounds(2, 127, 76, 40);
		contentPane.add(spinChooseCharCount);

		JRadioButton rdButMemorizeAll = new JRadioButton("Memorize ALL characters");
		rdButMemorizeAll.setFont(new Font("Tahoma", Font.BOLD, 15));
		rdButMemorizeAll.setBounds(2, 208, 228, 23);
		contentPane.add(rdButMemorizeAll);
		rdButMemorizeAll.setActionCommand("all characters");

		JRadioButton rdButMemorizeSpecific = new JRadioButton("Memorize SPECIFIC characters");
		rdButMemorizeSpecific.setFont(new Font("Tahoma", Font.BOLD, 15));
		rdButMemorizeSpecific.setBounds(2, 234, 270, 23);
		contentPane.add(rdButMemorizeSpecific);
		rdButMemorizeSpecific.setActionCommand("specific characters");

		ButtonGroup btnGrp = new ButtonGroup();
		btnGrp.add(rdButMemorizeAll);
		btnGrp.add(rdButMemorizeSpecific);

		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					choice = (btnGrp.getSelection().getActionCommand());
					System.out.println(choice);

					int start = JOptionPane.showConfirmDialog(null, "Are you sure you would like to start?");

					if (start == JOptionPane.YES_OPTION) {
						levels = (int) spinChooseNumLevels.getValue();
						charLimit = (int) spinChooseCharCount.getValue();

						spinChooseNumLevels.setVisible(false);
						lblNumLevels.setVisible(false);
						startButton.setVisible(false);
						spinChooseCharCount.setVisible(false);
						lblHowManyLetters.setVisible(false);

						lblGameTitle.setVisible(false);
						lblGameDescription.setVisible(false);

						lblWouldYouLike.setVisible(false);
						rdButMemorizeAll.setVisible(false);
						rdButMemorizeSpecific.setVisible(false);

						randomLetter.setVisible(true);

						timer.start();
					}
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null,
							"Please choose whether you would like to memorize ALL or a SPECIFIC set of the letters that will be shown");
				}

			}
		});
		startButton.setFont(new Font("Tahoma", Font.BOLD, 25));
		startButton.setBounds(252, 329, 109, 35);
		contentPane.add(startButton);

	}

	private char nextLevel(Random randObj) {
		currentLevel++;
		milliseconds = 1000;
		seconds = 5;
		charCount = 1;
		char c = (char) (randObj.nextInt(26) + 'a');
		actualAnswer[charCount - 1] = c + "";

		return c;
	}

	private boolean memorizeAll() {
		boolean matching = false;
		String guess = "";

		while (guess.equals("")) {

			try {
				guess = JOptionPane.showInputDialog("Type all of the letters you saw in order (no spaces):");

				if (guess.equals("")) { // if user clicks "OK" without typing anything, we want to print a message and
										// repeat
					JOptionPane.showMessageDialog(null, "Please type a guess");
					continue;
				}

				char[] guessArr = guess.toCharArray();
				int i = 0;
				for (i = 0; i < guessArr.length; i++) {
					System.out.println(guessArr[i] + " " + actualAnswer[i]);
					if ((guessArr[i] + "").equals(actualAnswer[i])) {
						continue;
					} else {
						matching = false;
						break;
					}
				}

				if (i == guessArr.length) { // if we got to the END of the guess char array, that means we have a match
					matching = true;
				}
			} catch (NullPointerException ex) {
				JOptionPane.showMessageDialog(null, "Please type a guess");
				guess = "";
			}

		}
		return matching;
	}

	private boolean memorizeSpecific(int randIndex) {
		boolean matching = false;
		String guess = "";

		while (guess.equals("")) {

			try {
				guess = JOptionPane.showInputDialog("Type letter number " + Integer.toString(randIndex + 1) + " ");

				if (guess.equals("")) { // if user clicks "OK" without typing anything, we want to print a message and
										// repeat
					JOptionPane.showMessageDialog(null, "Please type a guess");
					continue;
				}

				if (guess.equals(actualAnswer[randIndex])) { // if we got to the END of the guess char array, that means
																// we have a match
					matching = true;
				}
			} catch (NullPointerException ex) {
				JOptionPane.showMessageDialog(null, "Please type a guess");
				guess = "";
			}

		}
		return matching;
	}
}