package com.mistno.minewrap;

import static com.mistno.minewrap.State.END;

import java.io.*;

public class ServerInputTask implements Runnable {

	private final ProcessWrapper process;
	private final StateHolder stateHolder;
	
	public ServerInputTask(ProcessWrapper process, StateHolder stateHolder) {
		this.process = process;
		this.stateHolder = stateHolder;
	}
	
	@Override
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			try {
				try{
					while(!reader.ready()) {
						Thread.sleep(200);
					}
				}catch(InterruptedException e) {
					return;
				}
				
				String input = reader.readLine();
				if(input.equalsIgnoreCase("stop")) {
					stateHolder.setState(END);
				}
			
				process.writer().write(input + "\n");
				process.writer().flush();
			}catch (IOException e) {
				System.err.println("Error when writing command to minecraft: " + e.getMessage());
			}
		}
	}
	
}
