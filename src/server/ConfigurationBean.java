package server;

public class ConfigurationBean {
	private final int port;

	public ConfigurationBean(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
}
