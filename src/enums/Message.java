package enums;


/**
 * <p>
 * Valeurs possible des messages entre le serveur et les clients
 * </p>
 */
public enum Message {
	NULL, AUTHENTICATE, CREATE_ACCOUNT, SUCCESS, FAIL, EXIST, ALREADY_IN_USE, START_3P, START_4P, START_5P, START_6P, ROUND_END_WINNER, ROUND_END_NEUTRAL, ROUND_END_LOSER, GAME_END_WINNER, GAME_END_LOSER, CONNECTION_LOST, DECONNECT
}
