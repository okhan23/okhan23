//
// Names: Omaid Khan && Jose Hernandez
// Course: CS 342 Spring 2016 - Troy UIC
// Assignment: RSA Encryption and Decryption
// Date: 15 March 2016
//

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.io.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

public class GUI extends JApplet implements ActionListener
{

	private JFrame frame;
	private JTextField pTextField;
	private JTextField qTextField;

	private JButton BlockFile;
	private JButton EncryptDecrypt;
	private JButton UnBlockFile;
	private JButton CreateKeys;
	private JButton emptyp_q;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		GUI window = new GUI();
		window.frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 310, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		BlockFile = new JButton("Block File");
		BlockFile.setBounds(25, 115, 116, 23);
		BlockFile.addActionListener(this);
		frame.getContentPane().add(BlockFile);

		EncryptDecrypt = new JButton("Encrypt/Decrypt");
		EncryptDecrypt.setBounds(25, 160, 242, 73);
		EncryptDecrypt.addActionListener(this);
		frame.getContentPane().add(EncryptDecrypt);

		UnBlockFile = new JButton("UnBlock File");
		UnBlockFile.setBounds(151, 115, 116, 23);
		UnBlockFile.addActionListener(this);
		frame.getContentPane().add(UnBlockFile);

		CreateKeys = new JButton("Create Keys");
		CreateKeys.setBounds(204, 78, 116, 23);

		frame.getContentPane().add(CreateKeys);
		CreateKeys.addActionListener(this);
		CreateKeys.setBounds(171, 74, 116, 23);
		frame.getContentPane().add(CreateKeys);

		JLabel pValueLabel = new JLabel("p Value: ");
		pValueLabel.setBounds(10, 53, 46, 14);
		frame.getContentPane().add(pValueLabel);

		JLabel instruction2Label = new JLabel("Empty p and q textfields for random.");
		instruction2Label.setBounds(10, 28, 310, 14);
		frame.getContentPane().add(instruction2Label);

		JLabel qValueLabel = new JLabel("q Value: ");
		qValueLabel.setBounds(10, 78, 46, 14);
		frame.getContentPane().add(qValueLabel);

		pTextField = new JTextField();
		pTextField.setBounds(55, 50, 86, 20);
		frame.getContentPane().add(pTextField);
		pTextField.setColumns(10);

		qTextField = new JTextField();
		qTextField.setBounds(55, 75, 86, 20);
		frame.getContentPane().add(qTextField);
		qTextField.setColumns(10);

		JLabel instruction1Label = new JLabel("Enter prime numbers (p and q) to generate a key");
		instruction1Label.setBounds(10, 11, 298, 14);
		frame.getContentPane().add(instruction1Label);

		emptyp_q = new JButton("Empty p and q");
		//emptyp_q.setBounds(204, 53, 116, 23);
		emptyp_q.setBounds(171, 48, 116, 23);
		emptyp_q.addActionListener(this);
		frame.getContentPane().add(emptyp_q);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setBounds(10, 206, 310, 8);
		frame.getContentPane().add(horizontalStrut);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setBounds(10, 108, 310, 8);
		frame.getContentPane().add(horizontalStrut_1);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalStrut_2.setBounds(10, 309, 310, 8);
		frame.getContentPane().add(horizontalStrut_2);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == BlockFile)
		{
			String blockSizeStr = JOptionPane.showInputDialog(null, "Please enter a size for the Block size");
			JOptionPane.showMessageDialog(null, "Please choose a text file to Block. Preferably one that is not empty.");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
			final JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(this);
			File file;
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				file = fc.getSelectedFile();
				JOptionPane.showMessageDialog(null, "Reading message from: " + file.getName() + ".");
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));

					List<String> msgParsed = new Vector<String>();
					String line = null;
					while ((line = br.readLine()) != null) {
							msgParsed.add(line);
						}

					// this will put all the lines into one huge line.
					line = "";
					int length = msgParsed.size();
					for(int i= 0; i<length;i++)
					{
						line+=msgParsed.get(i);
						if((i+1)<length)
							line+="\n";
					}
					System.out.println("Printing line: " + line);

					String outputLine = "";
					length = line.length();
					for(int i = 0; i<length; i++)
					{
						int ascii;
						if(line.charAt(i) == '\n')
							ascii =  10;
						else
							ascii = ((int) line.charAt(i))  - 27;

						String outputValue = "";
						switch(ascii)
						{
							case 0: //null
								outputValue = "00";
								break;
							case 11: // \v
								outputValue = "01";
								break;
							case 9: // \t
								outputValue = "02";
								break;
							case 10: // \n
								outputValue = "03";
								break;
							case 13: // \r
								outputValue = "04";
								break;
							default:
								outputValue = new DecimalFormat("00").format(ascii);
						}
						outputLine = outputValue + outputLine;
					}

					System.out.println("Printing Output line: " + outputLine);

					String blockedFileName = JOptionPane.showInputDialog(null, "Please enter a filename for the blocked file");
					PrintWriter writer;
					try {
						writer = new PrintWriter(blockedFileName + ".txt", "UTF-8");
						int blockSize = 2 * Integer.parseInt(blockSizeStr);
						for(int i = 0; outputLine.length() != 0; i++)
						{
							if(blockSize > outputLine.length())
							{
								String format = "";
								for(int t  = 0; t<blockSize; t++ )
									format += "0";

								format = new DecimalFormat(format).format(Integer.parseInt(outputLine));
								writer.println(format);
								outputLine = "";
							}
							else
							{
								writer.println(outputLine.substring(outputLine.length() - blockSize, outputLine.length() ));
								outputLine = outputLine.substring(0, outputLine.length() - blockSize);
							}
						}

						writer.close();
						JOptionPane.showMessageDialog(null, "Blocked File: " + blockedFileName + ".txt Created.");
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Open command cancelled by user.");
			}
		}
		else if(e.getSource() == EncryptDecrypt)
		{
			JOptionPane.showMessageDialog(null, "Please choose a Proper Key File to Encrypt or Decrypt.");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML File", "xml");
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(this);
			File file;

			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				file = fc.getSelectedFile();
				JOptionPane.showMessageDialog(null, "Reading keys from: " + file.getName() + ".");
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));

					List<String> msgParsed = new Vector<String>();
					String line = null;
					while ((line = br.readLine()) != null) {
						msgParsed.add(line);
					}
					String key1 = msgParsed.get(1).substring(msgParsed.get(1).indexOf('>') + 1);
					key1 = key1.substring(0,key1.indexOf('<'));

					String key2 = msgParsed.get(2).substring(msgParsed.get(2).indexOf('>') + 1);
					key2 = key2.substring(0,key2.indexOf('<'));

					String type = msgParsed.get(1).substring(msgParsed.get(1).indexOf('<')+1,msgParsed.get(1).indexOf('>'));

					if(Objects.equals(type, "dvalue"))
					{
						type = "Decrypt";
						JOptionPane.showMessageDialog(null, "Using a Private Key. Hope you are trying to Decrypt.\n" +
								"Please Choose an Encrypted Blocked text file to Decrypt");

					}
					else if(Objects.equals(type, "evalue"))
					{
						type = "Encrypt";
						JOptionPane.showMessageDialog(null, "Using a Public Key. Hope you are trying to Encrypt.\n" +
								"Please Choose an Decrypted/Un-Crypted Blocked text file to Encrypt");
					}
					else
					{
						// they fucked up... tell em to go fix the shit.
						JOptionPane.showMessageDialog(null, "Invalid Key used, try again with a proper file/format");
						return;
					}

					filter = new FileNameExtensionFilter("TEXT FILE", "txt");
					fc = new JFileChooser();
					fc.setFileFilter(filter);
					returnVal = fc.showOpenDialog(this);

					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						try {
							BigInteger exp = new BigInteger(key1);
							BigInteger n = new BigInteger(key2);

							file = fc.getSelectedFile();
							br = new BufferedReader(new FileReader(file));

							msgParsed = new Vector<>();
							while ((line = br.readLine()) != null) {
								msgParsed.add(line);
							}

							String filename = JOptionPane.showInputDialog(null, "Please enter a filename for the " + type + "ed file");
							PrintWriter writer;
							try
							{
								writer = new PrintWriter(filename + ".txt", "UTF-8");
								int blocksize = msgParsed.get(1).length();

								for (String aMsgParsed : msgParsed)
								{
									BigInteger inputNumber = new BigInteger(aMsgParsed);
									String out = (inputNumber.modPow(exp, n)).toString();
									while(out.length() < blocksize)
									{
										out = "0" + out;
									}
									writer.println(out);
								}

								writer.close();
								JOptionPane.showMessageDialog(null, type + "ed File: " + filename + ".txt Created.");
							} catch (FileNotFoundException | UnsupportedEncodingException e1) {
								e1.printStackTrace();
							}


						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}


				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Open command cancelled by user.");
			}


		}
		else if(e.getSource() == UnBlockFile)
		{
			JOptionPane.showMessageDialog(null, "Please choose a blocked text file to UnBlock. Preferably one that is not empty.");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
			final JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);

			int returnVal = fc.showOpenDialog(this);

			File file;

			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				file = fc.getSelectedFile();
				JOptionPane.showMessageDialog(null, "Reading blocked data from: " + file.getName() + ".");
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));

					List<String> msgParsed = new Vector<String>();
					String line = null;

					while ((line = br.readLine()) != null) {
						msgParsed.add(line);
					}

					line = "";
					for (String aMsgParsed : msgParsed) {
						line = aMsgParsed + line;
					}
					System.out.println("Printing line: " + line);

					String outputLine = "";

					for(int i = 0; line.length() != 0; i+=2)
					{
						int len = line.length();
						String outputValue = "";
						outputValue = line.substring(len-2, len);
						line = line.substring(0, len-2);
						char letter;

						switch(outputValue)
						{
							case "00": //null
								letter = '\0';
								continue;
							case "01": // \v"
								letter = (char) (Integer.parseInt("01") + 27);
								break;
							case "02": // \t
								letter = '\t';
								break;
							case "03": // \n
								letter = '\n';
								break;
							case "04": // \r
								letter = '\r';
								break;
							default:
								letter = (char) (Integer.parseInt(outputValue) + 27);
						}
						outputLine+=letter;
					}

					System.out.println("Final Printing Output line: " + outputLine);

					String blockedFileName = JOptionPane.showInputDialog(null, "Please enter a filename for the Unblocked file");
					PrintWriter writer;
					try
					{
						writer = new PrintWriter(blockedFileName + ".txt", "UTF-8");
						writer.println(outputLine);
						writer.close();
						JOptionPane.showMessageDialog(null, "UnBlocked File: " + blockedFileName + ".txt Created.");
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Open command cancelled by user.");
			}}
		else if (e.getSource() == CreateKeys)
		{
			boolean randBit = false;
			if (Objects.equals(pTextField.getText(), "") || Objects.equals(qTextField.getText(), "")) {
				randBit = true;
			}

			if ((!Objects.equals(pTextField.getText(), "") && Objects.equals(qTextField.getText(), "")) ||
					(Objects.equals(pTextField.getText(), "") && !Objects.equals(qTextField.getText(), ""))) {
				JOptionPane.showMessageDialog(null, "Please enter both p and q, or empty both text fields.");
				return;
			}

			BigInteger p = new BigInteger("0");
			BigInteger q = new BigInteger("0");


			if (randBit) {
				JOptionPane.showMessageDialog(null, "Please choose a file to read random prime numbers from.");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("RESOURCE FILES", "rsc");
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(filter);

				int returnVal = fc.showOpenDialog(this);

				File file;


				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();

					JOptionPane.showMessageDialog(null, "Reading random Primes numbers from: " + file.getName() + ".");
					try {
						BufferedReader br = new BufferedReader(new FileReader(file));

						List<String> randomPrimes = new Vector<String>();
						String line = null;
						while ((line = br.readLine()) != null) {
							randomPrimes.add(line);
						}
						Random rand = new Random();
						p = new BigInteger(randomPrimes.get(rand.nextInt(20)));
						while(!Objects.equals(q = (new BigInteger(randomPrimes.get(rand.nextInt(20)))), p))
						{} // keep looping until some unique value comes up
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else {
					JOptionPane.showMessageDialog(null, "Open command cancelled by user.");
				}

			} else {
				p = new BigInteger(pTextField.getText());
				q = new BigInteger(qTextField.getText());
			}

			KeyGenerator generate = new KeyGenerator(p, q);

			try {
				generate.create();
			} catch (IOException e1) {
				e1.printStackTrace();
			}}
		else if (e.getSource() == emptyp_q)
		{
			pTextField.setText("");
			qTextField.setText("");
		}
	}
}
