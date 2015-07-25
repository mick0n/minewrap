package com.mistno.minewrap;

import java.io.*;

public class ProcessWrapper {

	private Process process;
	private BufferedWriter writer;
	private BufferedReader reader;

	public ProcessWrapper(Process process) {
		this.process = process;
		writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	}

	public Process get() {
		return process;
	}

	public BufferedReader reader() {
		return reader;
	}

	public BufferedWriter writer() {
		return writer;
	}

	public void close() {
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			System.err.println("Couldn't close process' streams: " + e.getMessage());
		}
	}
	
	public boolean isRunning() {
	    try {
	        process.exitValue();
	        return false;
	    } catch (Exception e) {
	        return true;
	    }
	}

}
