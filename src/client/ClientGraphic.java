package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
import enums.*;

@SuppressWarnings("serial")
public class ClientGraphic extends JFrame implements WindowListener {

	private JPanel paneLogo = new JPanel();
	private JPanel paneChoice = new JPanel();
	private JPanel paneCreate = new JPanel();
	private JPanel paneConnect = new JPanel();
	private JPanel paneDeconnectHome = new JPanel();
	private JPanel paneConnectionFaield = new JPanel();
	private JPanel paneStartGame = new JPanel();
	private JPanel paneWaintingRound = new JPanel();
	private JPanel paneRound = new JPanel();

	private Mode mode;
	private String error;
	private String serverAdress;
	private String serverPort;
	private String username;
	private String password;
	private boolean check;

	private Client client;

	public ClientGraphic() {
		super("Eat Me If You Can !");

		setBackground(Color.lightGray);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/EMIYC_icone.png"));

		setSize(1000, 600);
		setResizable(false);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setVisible(true);

		init();
		switchMode(Mode.DEFAULT);
		this.addWindowListener(this);
	}

	private void init() {
		error = "";
		serverAdress = "";
		serverPort = "";
		username = "";
		password = "";
		check = true;
		client = new Client();
	}

	private JLabel getError() {
		JLabel jlError = new JLabel(error);
		jlError.setForeground(new Color(205, 92, 92));
		return jlError;
	}

	private void switchMode(Mode mode) {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());

		this.mode = mode;

		switch (mode) {
		case DEFAULT:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getDefaultPanel(), BorderLayout.CENTER);
			break;

		case SUBSCRIBE:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getSubscribePanel(), BorderLayout.CENTER);
			break;

		case CONNECT:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getDefaultPanel(), BorderLayout.CENTER);
			break;

		default:
			dispose();
			break;
		}

		setContentPane(pane);
		refresh();
	}

	private void refresh() {
		validate();
		repaint();
	}

	public JPanel getLogoPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(new Dimension(200, 300));

		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		JLabel jlDate = new JLabel(date.format(new Date()).toString());
		jlDate.setOpaque(true);
		pane.add(jlDate);

		ImageIcon image = new ImageIcon("img/homelogo.jpg");
		JLabel jlImage = new JLabel(image);
		jlImage.setPreferredSize(new Dimension(200, 200));
		pane.add(jlImage);

		JLabel jlWelcome = new JLabel("<html>Bienvenue sur <br> Eat Me If You Can !</html>");
		pane.add(jlWelcome);

		return pane;
	}

	private JPanel getDefaultPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(new Dimension(600, 600));

		JLabel jlServer = new JLabel("Entrez l'adresse du serveur : ");
		Color colorServer = (serverAdress.isEmpty()) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlServer.setForeground(colorServer);
		pane.add(jlServer);
		JTextField jtfServer = new JTextField(serverAdress);
		jtfServer.setPreferredSize(new Dimension(550, 25));
		pane.add(jtfServer);

		JLabel jlPort = new JLabel("Entrez le port du serveur : ");
		Color colorPort = (serverPort.isEmpty() || !check) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlPort.setForeground(colorPort);
		pane.add(jlPort);
		JTextField jtfPort = new JTextField(serverPort);
		jtfPort.setPreferredSize(new Dimension(550, 25));
		pane.add(jtfPort);

		JButton jbSubscribe = new JButton("S'inscrire");
		jbSubscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverAdress = jtfServer.getText();
				serverPort = jtfPort.getText();

				if (!serverAdress.isEmpty() && !serverPort.isEmpty()) {
					int port = -1;

					try {
						port = Integer.parseInt(serverPort);
					} catch (NumberFormatException nfe) {
						error = "Le port est non valide.";
						switchMode(mode);
					}

					if (port < 0 || port > 65536) {
						error = "Le port est non valide.";
						switchMode(mode);
					} else if (client.setConnection(serverAdress, port)) {
						error = "";
						switchMode(Mode.SUBSCRIBE);
					} else {
						error = "Connexion impossible !";
						switchMode(mode);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(mode);
				}
			}
		});
		pane.add(jbSubscribe);

		JButton jbConnect = new JButton("Se connecter");
		jbConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverAdress = jtfServer.getText();
				serverPort = jtfPort.getText();

				if (!serverAdress.isEmpty() && !serverPort.isEmpty()) {
					int port = -1;

					try {
						port = Integer.parseInt(serverPort);
					} catch (NumberFormatException nfe) {
						error = "Le port est non valide.";
						switchMode(mode);
					}

					if (port < 0 || port > 65536) {
						error = "Le port est non valide.";
						switchMode(mode);
					} else if (client.setConnection(serverAdress, port)) {
						error = "";
						switchMode(Mode.CONNECT);
					} else {
						error = "Connexion impossible !";
						switchMode(mode);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(mode);
				}
			}
		});
		pane.add(jbConnect);

		pane.add(getError());

		return pane;
	}

	private JPanel getSubscribePanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(new Dimension(600, 600));

		JLabel jlUsername = new JLabel("Saisir identifiant : ");
		Color colorUsername = (username.isEmpty()) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlUsername.setForeground(colorUsername);
		pane.add(jlUsername);
		JTextField jtfUsername = new JTextField(username);
		jtfUsername.setPreferredSize(new Dimension(550, 25));
		pane.add(jtfUsername);

		JLabel jlPassword = new JLabel("Saisir mot de passe : ");
		JPasswordField jpfPassword = new JPasswordField();
		jpfPassword.setPreferredSize(new Dimension(550, 25));

		JLabel jlNewPassword = new JLabel("Confirmer mot de passe : ");
		JPasswordField jpfNewPassword = new JPasswordField();
		jpfNewPassword.setPreferredSize(new Dimension(550, 25));

		Color colorPasswords = (password.isEmpty() || !check) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlPassword.setForeground(colorPasswords);
		jlNewPassword.setForeground(colorPasswords);

		pane.add(jlPassword);
		pane.add(jpfPassword);
		pane.add(jlNewPassword);
		pane.add(jpfNewPassword);

		JButton jbSubscribe = new JButton("S'inscrire");
		jbSubscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = jtfUsername.getText();
				password = new String(jpfPassword.getPassword());
				String newPassword = new String(jpfNewPassword.getPassword());
				check = (password.compareTo(newPassword) == 0);

				System.out.println(password);
				System.out.println(newPassword);
				System.out.println(check);

				if (!username.isEmpty() && !password.isEmpty() && check) {
					error = client.connect(Message.CREATE_ACCOUNT, username, password);
					error = "";
					switchMode(Mode.MENU);
				} else if (!check) {
					password = "";
					error = "Les mots de passe ne correspondent pas !";
					switchMode(mode);
				} else {
					error = "Champs obligatoire !";
					switchMode(mode);
				}
			}
		});
		pane.add(jbSubscribe);

		pane.add(getError());

		return pane;
	}

	public void displayBackHome() {
		// repaintContainer();
		// repaintChoicePanel();
		// repaintPaneLogo();
		removePanels();
		// container.add(displayPaneLogo(), BorderLayout.WEST);
		// container.add(displayPaneChoice(), BorderLayout.CENTER);
		pack();
		setVisible(true);

	}

	public JPanel displayDeconnectHome() {
		paneDeconnectHome.setPreferredSize(new Dimension(100, 100));
		JButton buttDeconnect = new JButton("Quit");
		buttDeconnect.setIcon(new ImageIcon("img/quit.jpg"));
		// buttDeconnect.setPreferredSize(new Dimension(100,50));
		JButton buttHome = new JButton("Home");
		buttHome.setIcon(new ImageIcon("img/home.jpg"));
		// buttHome.setPreferredSize(new Dimension(100,50));
		paneDeconnectHome.add(buttDeconnect);
		paneDeconnectHome.add(buttHome);
		buttDeconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		buttHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayBackHome();
				// container.remove(displayDeconnectHome());
				// removePanels();
			}
		});

		return paneDeconnectHome;

	}

	public void repaintContainer() {
		// container.removeAll();
		// container.validate();
		// container.repaint();

	}

	public void repaintPaneLogo() {
		paneLogo.removeAll();
		paneLogo.validate();
		paneLogo.repaint();

	}

	public void repaintChoicePanel() {
		paneChoice.removeAll();
		paneChoice.validate();
		paneChoice.repaint();

	}

	public void removePanels() {
	}

	public JPanel displayPaneChoice() {

		paneChoice.setPreferredSize(new Dimension(600, 600));

		JLabel labelServerAdress = new JLabel("Entrez l'adresse du serveur : ");
		JLabel labelServerPort = new JLabel("Entrez le port du serveur : ");
		JLabel labelChoixConnectionType = new JLabel("Votre choix : ");

		JTextField textFieldServerAdress = new JTextField();
		JTextField textFieldServerPort = new JTextField();

		JButton buttChoixCreateAccount = new JButton("Vous inscrire ?");
		JButton buttChoixAuthenticate = new JButton("Vous connecter ?");

		textFieldServerAdress.setPreferredSize(new Dimension(550, 25));
		textFieldServerPort.setPreferredSize(new Dimension(550, 25));

		paneChoice.add(labelServerAdress);
		paneChoice.add(textFieldServerAdress);
		paneChoice.add(labelServerPort);
		paneChoice.add(textFieldServerPort);
		paneChoice.add(labelChoixConnectionType);
		paneChoice.add(buttChoixCreateAccount);
		paneChoice.add(buttChoixAuthenticate);

		// serverAdress = textFieldServerAdress.getText();
		// serverPort = Integer.parseInt(textFieldServerPort.getText());
		return paneChoice;
	}

	public JPanel displayPaneConnect() {
		// removePanels();
		// container.add(displayDeconnectHome(), BorderLayout.EAST);
		// container.add(displayPaneLogo(), BorderLayout.WEST);
		paneConnect.setPreferredSize(new Dimension(600, 600));

		JLabel labelUsername = new JLabel("Entrez votre username : ");
		JLabel labelPassword = new JLabel("Entrez votre mot de passe : ");

		JTextField textFieldUsername = new JTextField();
		JPasswordField passwordField = new JPasswordField();

		textFieldUsername.setPreferredSize(new Dimension(550, 25));
		passwordField.setPreferredSize(new Dimension(550, 25));

		JButton buttAuthenticate = new JButton("Se connecter");

		buttAuthenticate.setPreferredSize(new Dimension(200, 40));
		paneConnect.add(labelUsername);
		paneConnect.add(textFieldUsername);
		paneConnect.add(labelPassword);
		paneConnect.add(passwordField);
		paneConnect.add(buttAuthenticate);
		buttAuthenticate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				char[] pass = passwordField.getPassword();

				username = textFieldUsername.getText();
				password = new String(pass);

			}
		});
		return paneConnect;

	}

	public JPanel displayPaneCreate() {
		// removePanels();
		// container.add(displayDeconnectHome(), BorderLayout.EAST);
		// container.add(displayPaneLogo(), BorderLayout.WEST);
		paneCreate.setPreferredSize(new Dimension(600, 600));

		JLabel labelUsername = new JLabel("Entrez votre username : ");
		JLabel labelPassword = new JLabel("Entrez votre mot de passe : ");
		JLabel labelPasswordConfirm = new JLabel("Confirmer votre mot de passe : ");

		JTextField textFieldUsername = new JTextField();
		JPasswordField passwordField = new JPasswordField();
		JPasswordField passworConfirmdField = new JPasswordField();

		textFieldUsername.setPreferredSize(new Dimension(550, 25));
		passwordField.setPreferredSize(new Dimension(550, 25));
		passworConfirmdField.setPreferredSize(new Dimension(550, 25));

		JButton buttCreateAccount = new JButton("S'inscrire");
		buttCreateAccount.setPreferredSize(new Dimension(200, 40));

		paneCreate.add(labelUsername);
		paneCreate.add(textFieldUsername);
		paneCreate.add(labelPassword);
		paneCreate.add(passwordField);
		paneCreate.add(labelPasswordConfirm);
		paneCreate.add(passworConfirmdField);
		paneCreate.add(buttCreateAccount);

		password = new String(passwordField.getPassword());
		char[] passC = passwordField.getPassword();
		username = textFieldUsername.getText();

		return paneCreate;
	}

	public JPanel displayPaneConnectionFaield() {
		// container.add(displayDeconnectHome(), BorderLayout.EAST);
		// container.add(displayPaneLogo(), BorderLayout.WEST);
		paneConnectionFaield.setPreferredSize(new Dimension(600, 600));
		// paneConnectionFaield.setBackground(Color.red);
		JLabel labelConnectionFaield = new JLabel("Identifiants incorrects");
		labelConnectionFaield.setForeground(Color.red);

		paneConnectionFaield.add(labelConnectionFaield);
		return paneConnectionFaield;

	}

	public JPanel displayPaneStartGame() {
		paneStartGame.setPreferredSize(new Dimension(600, 600));

		JButton buttStart3 = new JButton("Lancer Une Partie à 3");
		JButton buttStart4 = new JButton("Lancer Une Partie à 4");
		JButton buttStart5 = new JButton("Lancer Une Partie à 5");
		JButton buttStart6 = new JButton("Lancer Une Partie à 6");

		buttStart3.setPreferredSize(new Dimension(500, 500));
		buttStart4.setPreferredSize(new Dimension(500, 500));
		buttStart5.setPreferredSize(new Dimension(500, 500));
		buttStart6.setPreferredSize(new Dimension(500, 500));

		paneStartGame.add(buttStart3);
		paneStartGame.add(buttStart4);
		paneStartGame.add(buttStart5);
		paneStartGame.add(buttStart6);

		buttStart3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				client.send(Message.START_3P);

				// container.add(displayPaneWaintingRound(),
				// BorderLayout.CENTER);

			}
		});
		buttStart4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				client.send(Message.START_4P);

				// container.add(displayPaneWaintingRound(),
				// BorderLayout.CENTER);

			}
		});

		buttStart5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				client.send(Message.START_5P);

				// container.add(displayPaneWaintingRound(),
				// BorderLayout.CENTER);

			}
		});

		buttStart6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				client.send(Message.START_6P);
				// container.add(displayPaneWaintingRound(),
				// BorderLayout.CENTER);

			}
		});
		// container.add(displayDeconnectHome(), BorderLayout.EAST);
		return paneStartGame;
	}

	public JPanel displayPaneWaintingRound() {
		// container.add(displayPaneLogo(), BorderLayout.WEST);
		paneWaintingRound.setPreferredSize(new Dimension(600, 600));
		paneWaintingRound.setBackground(Color.green);
		JLabel labelpaneWaintingRound = new JLabel("En attente d'une PArtie");

		paneConnectionFaield.add(labelpaneWaintingRound);
		return paneConnectionFaield;
	}

	public JPanel displayPaneRoundInnocents() {

		return paneRound;

	}

	public JPanel displayPaneRoundWolf() {

		return paneRound;

	}

	public static void main(String[] args) throws IOException {
		new ClientGraphic();
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		client.send(Message.DECONNECT);

		e.getWindow().setVisible(false);
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}