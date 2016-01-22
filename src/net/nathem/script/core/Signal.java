package net.nathem.script.core;

public class Signal {

	private NathemObject emitter;
	private NathemObject receiver;
	private boolean on;
	
	
	public Signal(NathemObject emitter, NathemObject receiver, boolean on) {
		
		// Liaisons
		this.emitter = emitter;
		this.emitter.getOutputs().add(this);
		this.receiver = receiver;
		this.receiver.getInputs().add(this);
		this.on = on;
	}
	

	public NathemObject getEmitter() {
		return this.emitter;
	}
	
	public NathemObject getReceiver() {
		return this.receiver;
	}

	public boolean isOn() {
		return this.on;
	}
	
	public boolean isOff() {
		return !this.on;
	}
	
	
}
