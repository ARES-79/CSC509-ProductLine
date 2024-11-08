package components;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TheSubscriber implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(TheSubscriber.class);
	private String ip;
	private int port;
	private boolean running = false;
	
	public TheSubscriber(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void run() {

		try {
			Socket socket = new Socket(ip, port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			logger.info("TheSubscriber is running");
			running = true;
			while (running) {
				String command = in.readLine();
				if (command != null) {
					parse(command);
				}
			}
			logger.info("TheSubscriber is stopping");
			socket.close();
		} catch (IOException e) {
			logger.error("I/O error in TheSubscriber: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Unexpected error in TheSubscriber: {}", e.getMessage(), e);
		} finally {
			running = false;
		}
	}
	
	public void stop() {
		running = false;
	}
	
	private void parse(String command) {
		String[] tokens = command.split(",");
		try {
			int[] x_y_pos = new int[2];
			for (int i = 0; i < 2; i++) {
				x_y_pos[i] = Integer.parseInt(tokens[i]);
			}
			Blackboard.getInstance().setHead(x_y_pos); //update x y pos in black board
		} catch (NumberFormatException e) {
			logger.error("Error parsing command", e);
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
}