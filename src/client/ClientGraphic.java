package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
import enums.*;
import game.*;

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
	private ArrayList<Role> listRoles;
	private Role[] roles;

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
		listRoles = null;
		roles = null;

		switchMode(Mode.DEFAULT, 0);
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

	private void setPlayerPositionPanel(JPanel pane, Player[] players, int[] index) {
		int me = client.getPosition();
		int size = players.length;
		int cpt = 0;

		for (int i = me + 1; i < me + size; i++) {
			JPanel enemy = getPlayerPanel(players[i % size]);
			((JPanel) pane.getComponent(index[cpt++])).add(enemy);
		}
	}

	private void switchMode(Mode mode, int i) {
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
		case CHECK_ROUND:
			pane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			break;

		case INGAME:
			pane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			pane.add(getIngamePanel(), BorderLayout.CENTER);
			break;

		case ROLE_ATTRIB:
			if (i == 0) {
				int size = client.getPlayers().length;
				listRoles = Role.generateRoles(size);
				roles = new Role[size];

				pane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			pane.add(getRoleAttribPanel(i), BorderLayout.CENTER);
			break;

		case WINNER:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getEndGamePanel(true), BorderLayout.CENTER);
			break;

		case LOSER:
			pane.add(getLogoPanel(), BorderLayout.WEST);
			pane.add(getEndGamePanel(false), BorderLayout.CENTER);
			break;

		case CONNECTION_LOST:
			pane.add(getConnectionLostPanel(), BorderLayout.CENTER);
			break;

		default:
			dispose();
			break;
		}

		setContentPane(pane);
		refresh();

		if (mode == Mode.WAITING) {
			Object obj = client.recieve();
			if (obj instanceof Message && (Message) obj == Message.CONNECTION_LOST) {
				switchMode(Mode.CONNECTION_LOST, 0);
			} else {
				client.startRound((Player[]) obj);
				switchMode(Mode.INGAME, 0);
			}
		} else if (mode == Mode.CHECK_ROUND) {
			Message message = (Message) client.recieve();

			switch (message) {
			case GAME_END_LOSER:
				switchMode(Mode.LOSER, 0);
				break;

			case GAME_END_WINNER:
				switchMode(Mode.WINNER, 0);
				break;

			case ROUND_END_LOSER:
				switchMode(Mode.ROLE_ATTRIB, 0);
				break;

			case ROUND_END_NEUTRAL:
			case ROUND_END_WINNER:
				switchMode(Mode.WAITING, 0);
				break;

			case CONNECTION_LOST:
				switchMode(Mode.CONNECTION_LOST, 0);
				break;
				
			case ENNEMY_DISCONNECTED:
				error = "Un des utilisateurs s'est déconnecté avant la fin de la partie.";
				switchMode(Mode.MENU, 0);
				break;
				
			default:
				break;
			}
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

		JLabel jlWelcome = new JLabel(
				"<html>Bienvenue " + client.getUsername() + "sur <br> Eat Me If You Can !</html>");
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
						switchMode(mode, 0);
					}

					if (port < 0 || port > 65536) {
						error = "Le port est non valide.";
						switchMode(mode, 0);
					} else if (client.setConnection(serverAdress, port)) {
						ok();
						switchMode(Mode.SUBSCRIBE, 0);
					} else {
						error = "Connexion impossible !";
						switchMode(mode, 0);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(mode, 0);
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
						switchMode(mode, 0);
					}

					if (port < 0 || port > 65536) {
						error = "Le port est non valide.";
						switchMode(mode, 0);
					} else if (client.setConnection(serverAdress, port)) {
						ok();
						switchMode(Mode.CONNECT, 0);
					} else {
						error = "Connexion impossible !";
						switchMode(mode, 0);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(mode, 0);
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
						switchMode(Mode.MENU, 0);
					} else {
						error = "Cet identifiant est déjà utilisé.";
						switchMode(mode, 0);
					}
				} else if (!check) {
					error = "Les mots de passe ne correspondent pas !";
					switchMode(mode, 0);
				} else {
					error = "Champs obligatoire !";
					switchMode(mode, 0);
				}
			}
		});

		JButton jbCancel = new JButton("Retour");
		jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok();
				switchMode(Mode.DEFAULT, 0);
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

				if (!username.isEmpty() && !password.isEmpty()) {
					Message msg = client.connect(Message.AUTHENTICATE, username, password);
					if (msg == Message.SUCCESS) {
						ok();
						switchMode(Mode.MENU, 0);
					} else if (msg == Message.ALREADY_IN_USE) {
						error = "Un utilisateur est déjà connecté avec cet identifiant.";
						switchMode(mode, 0);
					} else if (msg == Message.FAIL) {
						error = "Identifiants incorrects.";
						switchMode(mode, 0);
					}
				} else {
					error = "Champs obligatoire !";
					switchMode(mode, 0);
				}
			}
		});

		JButton jbCancel = new JButton("Retour");
		jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok();
				switchMode(Mode.DEFAULT, 0);
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
					switchMode(mode, 0);
				}
			});
			pane.add(jbStartGame);

			JButton jbDeconnect = new JButton("Se déconnecter");
			jbDeconnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbDeconnect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (client.send(Message.DISCONNECT)) {
						ok();
						switchMode(Mode.DEFAULT, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				}
			});
			pane.add(jbDeconnect);
		} else {
			JButton jbStart3 = new JButton("3 joueurs");
			jbStart3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (client.send(Message.START_3P)) {
						switchMode(Mode.WAITING, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				}
			});
			pane.add(jbStart3);

			JButton jbStart4 = new JButton("4 joueurs");
			jbStart4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (client.send(Message.START_4P)) {
						switchMode(Mode.WAITING, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				}
			});
			pane.add(jbStart4);

			JButton jbStart5 = new JButton("5 joueurs");
			jbStart5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (client.send(Message.START_5P)) {
						switchMode(Mode.WAITING, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				}
			});
			pane.add(jbStart5);

			JButton jbStart6 = new JButton("6 joueurs");
			jbStart6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbStart6.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (client.send(Message.START_6P)) {
						switchMode(Mode.WAITING, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				}
			});
			pane.add(jbStart6);

			JButton jbCancel = new JButton("Retour");
			jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			jbCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					check = true;
					switchMode(mode, 0);
				}
			});
			pane.add(jbCancel);
			
			pane.add(getError());
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
		pane.setSize(new Dimension(150, 150));
		pane.setLayout(new GridLayout(3, 1));

		JButton jbRole = new JButton(player.getRole().getName());
		if (client.getPlayer().getRole().isWolf() && !client.getPlayer().getUsername().equals(player.getUsername())) {
			jbRole.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((Wolf) client.getPlayer().getRole()).bite(player);
					if (client.send(client.getPlayer().getRole())) {
						switchMode(Mode.CHECK_ROUND, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				}
			});
		}
		pane.add(jbRole);

		JLabel jlUsername = new JLabel(player.getUsername());
		pane.add(jlUsername);

		JLabel jlPoints = new JLabel(player.getPoints() + " point(s)");
		pane.add(jlPoints);

		return pane;
	}

	private JPanel getIngamePanel() {
		JPanel pane = new JPanel();
		pane.setSize(dimension);
		pane.setLayout(new GridLayout(5, 5));

		for (int i = 0; i < 25; i++) {
			pane.add(new JPanel());
		}

		Player[] players = client.getPlayers();
		int nbPlayers = players.length;
		boolean isWolf = client.getPlayer().getRole().isWolf();

		((JPanel) pane.getComponent(22)).add(getPlayerPanel(client.getPlayer()));
		switch (nbPlayers) {
		case 3:
			setPlayerPositionPanel(pane, players, new int[] { 10, 14 });
			break;

		case 4:
			setPlayerPositionPanel(pane, players, new int[] { 10, 2, 14 });
			break;

		case 5:
			setPlayerPositionPanel(pane, players, new int[] { 10, 1, 3, 14 });
			break;

		case 6:
			setPlayerPositionPanel(pane, players, new int[] { 15, 5, 2, 9, 19 });
			break;
		}

		if (isWolf) {
			JLabel jlAction = new JLabel("Qui mordre ?");
			JPanel jpAction = ((JPanel) pane.getComponent(12));
			jpAction.add(jlAction);
		} else {
			JLabel jlAction = new JLabel("Que vais-je faire ?");
			JButton jbSleep = new JButton("Dormir");
			jbSleep.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((Innocent) client.getPlayer().getRole()).sleep();
					if (client.send(client.getPlayer().getRole())) {
						switchMode(Mode.CHECK_ROUND, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				}
			});

			JButton jbTrap = new JButton("Poser un piège");
			jbTrap.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((Innocent) client.getPlayer().getRole()).putTrap();
					if (client.send(client.getPlayer().getRole())) {
						switchMode(Mode.CHECK_ROUND, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				}
			});

			JPanel jpAction = ((JPanel) pane.getComponent(12));
			jpAction.add(jlAction);
			jpAction.add(jbSleep);
			jpAction.add(jbTrap);
		}

		return pane;
	}

	private JPanel getRoleAttribPanel(int position) {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		Player[] players = client.getPlayers();
		String text = "";

		JLabel jlText = new JLabel("Rôle de");
		pane.add(jlText);

		for (int i = 0; i < position; i++) {
			if (roles[i] != null) {
				text = players[i].getUsername() + " : " + roles[i].getName();
				JLabel jlUser = new JLabel(text);
				pane.add(jlUser);
			}
		}

		JLabel jlUsername = new JLabel(players[position].getUsername() + " : ");
		pane.add(jlUsername);

		JComboBox<Role> jcbRoles = new JComboBox<Role>();
		for (Role role : listRoles) {
			jcbRoles.addItem(role);
		}
		pane.add(jcbRoles);

		text = (position == players.length - 1) ? "Terminer" : "Suivant";
		JButton jbRoles = new JButton(text);
		jbRoles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listRoles.size() == 1) {
					roles[players.length - 1] = listRoles.get(0);
					if (client.send(roles)) {
						switchMode(Mode.WAITING, 0);
					} else {
						switchMode(Mode.CONNECTION_LOST, 0);
					}
				} else {
					Role role = (Role) jcbRoles.getSelectedItem();
					roles[position] = role;
					listRoles.remove(role);
					switchMode(Mode.ROLE_ATTRIB, position + 1);
				}
			}
		});
		pane.add(jbRoles);

		return pane;
	}

	private JPanel getEndGamePanel(boolean isWinner) {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		String path = isWinner ? "img/winner.gif" : "img/loser.gif";
		ImageIcon image = new ImageIcon(path);
		JLabel jlImage = new JLabel(image);
		pane.add(jlImage);

		String text = isWinner ? "Félicitations !" : "Bouuuuuuuh !";
		JLabel jlText = new JLabel(text);
		pane.add(jlText);

		JButton jbMenu = new JButton("Rejouer");
		jbMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchMode(Mode.MENU, 0);
			}
		});
		pane.add(jbMenu);

		JButton jbLeave = new JButton("Quitter");
		jbLeave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pane.add(jbLeave);

		return pane;
	}

	private JPanel getConnectionLostPanel() {
		JPanel pane = new JPanel();
		pane.setPreferredSize(dimension);

		ImageIcon image = new ImageIcon("img/conlost.png");
		JLabel jlImage = new JLabel(image);
		pane.add(jlImage);

		JButton jbMenu = new JButton("Se reconnecter");
		jbMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok();
				switchMode(Mode.DEFAULT, 0);
			}
		});
		pane.add(jbMenu);

		JButton jbLeave = new JButton("Quitter");
		jbLeave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pane.add(jbLeave);

		return pane;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (client.isAuthenticated()) {
			client.send(Message.DISCONNECT);
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