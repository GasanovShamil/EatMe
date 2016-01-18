package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
import enums.*;
import game.Player;

@SuppressWarnings("serial")
public class ClientGraphic extends JFrame implements WindowListener {
	private Mode mode;
	private Dimension dimension;
	private String error;
	private String serverAdress;
	private String serverPort;
	private String username;
	private String password;
	private boolean check;

	private Client client;

	public ClientGraphic() {
		super("Eat Me If You Can !");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setBackground(Color.lightGray);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/icone.png"));
		setSize(1000, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

		init();
	}

	private void init() {
		dimension = new Dimension(600, 600);
		error = "";
		serverAdress = "";
		serverPort = "";
		username = "";
		password = "";
		check = true;
		client = new Client();

		switchMode(Mode.DEFAULT);
	}

	private void ok() {
		error = "";
		check = true;
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
			pane.add(getConnectPanel(), BorderLayout.CENTER);
			break;

		case MENU:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getMenuPanel(), BorderLayout.CENTER);
			break;

		case WAITING:
			pane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			break;

		case INGAME:
			pane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			pane.add(getIngamePanel(), BorderLayout.CENTER);
			break;

		default:
			dispose();
			break;
		}

		setContentPane(pane);
		refresh();

		if (mode == Mode.WAITING) {
			client.startRound((Player[]) client.recieve());

			switchMode(Mode.INGAME);
		}
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

		ImageIcon image = new ImageIcon("img/logo.png");
		JLabel jlImage = new JLabel(image);
		jlImage.setPreferredSize(new Dimension(200, 200));
		pane.add(jlImage);

		JLabel jlWelcome = new JLabel("<html>Bienvenue sur <br> Eat Me If You Can !</html>");
		pane.add(jlWelcome);

		return pane;
	}

	private JPanel getDefaultPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);
		Box serverAddresseBox = Box.createHorizontalBox();
		JLabel jlServer = new JLabel("Entrez l'adresse du serveur : ");
		Color colorServer = (serverAdress.isEmpty()) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlServer.setForeground(colorServer);

		JTextField jtfServer = new JTextField(serverAdress);
		jtfServer.setPreferredSize(new Dimension(550, 25));

		serverAddresseBox.add(jlServer);
		serverAddresseBox.add(Box.createHorizontalStrut(6));
		serverAddresseBox.add(jtfServer);

		// pane.add(serverAddresseBox);

		Box serverPortBox = Box.createHorizontalBox();

		JLabel jlPort = new JLabel("Entrez le port du serveur : ");
		Color colorPort = (serverPort.isEmpty() || !check) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlPort.setForeground(colorPort);

		JTextField jtfPort = new JTextField(serverPort);
		jtfPort.setPreferredSize(new Dimension(550, 25));

		serverPortBox.add(jlPort);
		serverPortBox.add(Box.createHorizontalStrut(6));
		serverPortBox.add(jtfPort);

		jlPort.setPreferredSize(jlServer.getPreferredSize());

		Box buttonBox = Box.createHorizontalBox();

		JButton jbSubscribe = new JButton("S'inscrire");
		jbSubscribe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbSubscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverAdress = jtfServer.getText();
				serverPort = jtfPort.getText();

				if (!serverAdress.isEmpty() && !serverPort.isEmpty() && check) {
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
						ok();
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

		JButton jbConnect = new JButton("Se connecter");
		jbConnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
						ok();
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

		buttonBox.add(jbSubscribe);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(jbConnect);

		Box mainBox = Box.createVerticalBox();

		mainBox.add(serverAddresseBox);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(serverPortBox);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(buttonBox);

		pane.add(mainBox);
		pane.add(getError());

		return pane;
	}

	private JPanel getSubscribePanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		Box userNameBox = Box.createHorizontalBox();

		JLabel jlUsername = new JLabel("Saisir identifiant : ");
		Color colorUsername = (username.isEmpty()) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlUsername.setForeground(colorUsername);

		JTextField jtfUsername = new JTextField(username);
		jtfUsername.setPreferredSize(new Dimension(550, 25));

		userNameBox.add(jlUsername);
		userNameBox.add(Box.createHorizontalStrut(6));
		userNameBox.add(jtfUsername);

		Color colorPasswords = (password.isEmpty() || !check) ? new Color(205, 92, 92) : new Color(0, 0, 0);

		Box passwordBox = Box.createHorizontalBox();

		JLabel jlPassword = new JLabel("Saisir mot de passe : ");
		jlPassword.setForeground(colorPasswords);

		JPasswordField jpfPassword = new JPasswordField();
		jpfPassword.setPreferredSize(new Dimension(550, 25));

		passwordBox.add(jlPassword);
		passwordBox.add(Box.createHorizontalStrut(6));
		passwordBox.add(jpfPassword);

		Box confPasswordBox = Box.createHorizontalBox();

		JLabel jlNewPassword = new JLabel("Confirmer mot de passe : ");
		jlNewPassword.setForeground(colorPasswords);

		JPasswordField jpfNewPassword = new JPasswordField();
		jpfNewPassword.setPreferredSize(new Dimension(550, 25));

		confPasswordBox.add(jlNewPassword);
		confPasswordBox.add(Box.createHorizontalStrut(6));
		confPasswordBox.add(jpfNewPassword);

		jlUsername.setPreferredSize(jlNewPassword.getPreferredSize());
		jlUsername.setPreferredSize(jlNewPassword.getPreferredSize());

		Box buttonBox = Box.createHorizontalBox();

		JButton jbSubscribe = new JButton("S'inscrire");
		jbSubscribe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbSubscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = jtfUsername.getText();
				password = new String(jpfPassword.getPassword());
				String newPassword = new String(jpfNewPassword.getPassword());
				check = (password.compareTo(newPassword) == 0);

				if (!username.isEmpty() && !password.isEmpty() && check) {
					Message msg = client.connect(Message.CREATE_ACCOUNT, username, password);
					if (msg == Message.SUCCESS) {
						ok();
						switchMode(Mode.MENU);
					} else {
						error = "Cet identifiant est déjà utilisé.";
						check = false;
						switchMode(mode);
					}
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

		JButton jbCancel = new JButton("Retour");
		jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchMode(Mode.DEFAULT);
			}
		});

		buttonBox.add(jbSubscribe);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(jbCancel);

		Box mainBox = Box.createVerticalBox();

		mainBox.add(userNameBox);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(passwordBox);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(confPasswordBox);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(buttonBox);
		pane.add(mainBox);

		pane.add(getError());

		return pane;
	}

	private JPanel getConnectPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		Box userNameBox = Box.createHorizontalBox();

		JLabel jlUsername = new JLabel("Saisir identifiant : ");
		Color colorUsername = (username.isEmpty()) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlUsername.setForeground(colorUsername);

		JTextField jtfUsername = new JTextField(username);
		jtfUsername.setPreferredSize(new Dimension(550, 25));

		userNameBox.add(jlUsername);
		userNameBox.add(Box.createHorizontalStrut(6));
		userNameBox.add(jtfUsername);

		Box passwordBox = Box.createHorizontalBox();

		JLabel jlPassword = new JLabel("Saisir mot de passe : ");
		Color colorPasswords = (password.isEmpty() || !check) ? new Color(205, 92, 92) : new Color(0, 0, 0);
		jlPassword.setForeground(colorPasswords);

		JPasswordField jpfPassword = new JPasswordField();
		jpfPassword.setPreferredSize(new Dimension(550, 25));
		jlPassword.setForeground(colorPasswords);

		passwordBox.add(jlPassword);
		passwordBox.add(Box.createHorizontalStrut(6));
		passwordBox.add(jpfPassword);

		jlUsername.setPreferredSize(jlPassword.getPreferredSize());

		Box buttonBox = Box.createHorizontalBox();

		JButton jbConnect = new JButton("Se connecter");
		jbConnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = jtfUsername.getText();
				password = new String(jpfPassword.getPassword());

				if (!username.isEmpty() && !password.isEmpty() && check) {
					Message msg = client.connect(Message.AUTHENTICATE, username, password);
					if (msg == Message.SUCCESS) {
						ok();
						switchMode(Mode.MENU);
					} else {
						error = "Identifiants incorrects.";
						check = false;
						switchMode(Mode.DEFAULT);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(Mode.CONNECT);
				}
			}
		});

		JButton jbCancel = new JButton("Retour");
		jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchMode(Mode.DEFAULT);
			}
		});

		buttonBox.add(jbConnect);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(jbCancel);

		Box mainBox = Box.createVerticalBox();

		mainBox.add(userNameBox);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(passwordBox);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(buttonBox);
		pane.add(mainBox);
		pane.add(getError());

		return pane;
	}

	private JPanel getMenuPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		if (check) {
			JButton jbStartGame = new JButton("Lancer une partie");
			jbStartGame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStartGame.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					check = false;
					switchMode(mode);
				}
			});
			pane.add(jbStartGame);

			JButton jbDeconnect = new JButton("Se déconnecter");
			jbDeconnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbDeconnect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.DECONNECT);
					switchMode(Mode.DEFAULT);
				}
			});
			pane.add(jbDeconnect);
		} else {
			JButton jbStart3 = new JButton("3 joueurs");
			jbStart3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.START_3P);
					switchMode(Mode.WAITING);
				}
			});
			pane.add(jbStart3);

			JButton jbStart4 = new JButton("4 joueurs");
			jbStart4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.START_4P);
					switchMode(Mode.WAITING);
				}
			});
			pane.add(jbStart4);

			JButton jbStart5 = new JButton("5 joueurs");
			jbStart5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.START_5P);
					switchMode(Mode.WAITING);
				}
			});
			pane.add(jbStart5);

			JButton jbStart6 = new JButton("6 joueurs");
			jbStart6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart6.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					client.send(Message.START_6P);
					switchMode(Mode.WAITING);
				}
			});
			pane.add(jbStart6);

			JButton jbCancel = new JButton("Retour");
			jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					check = true;
					switchMode(mode);
				}
			});
			pane.add(jbCancel);
		}

		return pane;
	}

	private JPanel getWaitingPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		ImageIcon image = new ImageIcon("img/loading.gif");
		JLabel jlImage = new JLabel(image);
		pane.add(jlImage);

		return pane;
	}

	private JPanel getPlayerPanel(Player player) {
		JPanel pane = new JPanel();
		pane.setSize(new Dimension(100, 100));
		pane.setLayout(new GridLayout(3, 1));
		pane.setBackground(Color.CYAN);

		ImageIcon image = new ImageIcon("img/loading.gif");
		JLabel jlRole = new JLabel(player.getRole().getName());
		pane.add(jlRole);

		JLabel jlUsername = new JLabel(player.getUsername());
		pane.add(jlUsername);

		JLabel jlPoints = new JLabel("" + player.getPoints());
		pane.add(jlPoints);

		return pane;
	}

	private JPanel getIngamePanel() {
		boolean isWolf=client.getPlayer().getRole().isWolf();
		JPanel pane = new JPanel();
		pane.setSize(dimension);
		pane.setBackground(Color.YELLOW);
		pane.setLayout(new GridLayout(5, 5));
		for (int i = 0; i < 25; i++) {
			JPanel p = new JPanel();
			JLabel l = new JLabel("" + i);
			p.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			p.add(l);
			pane.add(p);
			System.out.println(p.getPreferredSize());
		}
		Player[] players = client.getPlayers();

		int nbPlayers = players.length;
		
		int cpt;
		switch (nbPlayers) {
		case 3:
			((JPanel) pane.getComponent(22)).add(getPlayerPanel(client.getPlayer()));
			int[] index3 = { 10, 14 };
			cpt = 0;
			for (int i = 0; i < players.length; i++) {
				if (i != client.getPosition()) {
					JPanel enemy = getPlayerPanel(players[i]);
					
					((JPanel) pane.getComponent(index3[cpt++])).add(enemy);
				}
			}

			pane.repaint();
			pane.revalidate();
			break;
			
		case 4:
			((JPanel) pane.getComponent(22)).add(getPlayerPanel(client.getPlayer()));
			int[] index4 = { 2, 10, 14 };
			cpt = 0;
			for (int i = 0; i < players.length; i++) {
				if (i != client.getPosition()) {
					JPanel enemy = getPlayerPanel(players[i]);
					((JPanel) pane.getComponent(index4[cpt++])).add(enemy);
				}
			}

			pane.repaint();
			pane.revalidate();
			break;
			
		case 5:
			((JPanel) pane.getComponent(22)).add(getPlayerPanel(client.getPlayer()));
			int[] index5 = { 1, 3, 10, 14 };
			cpt = 0;
			for (int i = 0; i < players.length; i++) {
				if (i != client.getPosition()) {
					JPanel enemy = getPlayerPanel(players[i]);
					((JPanel) pane.getComponent(index5[cpt++])).add(enemy);
				}
			}
			break;
			
		case 6:
			((JPanel) pane.getComponent(22)).add(getPlayerPanel(client.getPlayer()));
			int[] index6 = { 2, 5, 9, 15, 19 };
			cpt = 0;
			for (int i = 0; i < players.length; i++) {
				if (i != client.getPosition()) {
					JPanel enemy = getPlayerPanel(players[i]);
					((JPanel) pane.getComponent(index6[cpt++])).add(enemy);
				}
			}
			break;

		}
		if(isWolf){
			JLabel l=new JLabel("Qui mordre?");
			((JPanel) pane.getComponent(12)).add(l);
		}else{
			JLabel l=new JLabel("Piege ou bonnet?");
			((JPanel) pane.getComponent(12)).add(l);
		}
		
		return pane;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (client.isAuthenticated()) {
			client.send(Message.DECONNECT);
		}

		e.getWindow().setVisible(false);
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	public static void main(String[] args) throws IOException {
		new ClientGraphic();
	}
}