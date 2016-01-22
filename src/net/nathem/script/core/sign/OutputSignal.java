package net.nathem.script.core.sign;

public class OutputSignal {

	private int signal;
	private boolean on;
	
	
	public OutputSignal(int signal, boolean on) {
		this.signal = signal;
		this.on = on;
	}
	
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public OutputSignal(int signal) {
		this(signal, true);
	}
	
	public boolean isOn()
	{
		return this.on;
	}
	
	public boolean isOff()
	{
		return !this.on;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (on ? 1231 : 1237);
		result = prime * result + signal;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OutputSignal other = (OutputSignal) obj;
		if (on != other.on)
			return false;
		if (signal != other.signal)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OutputSignal [signal=" + signal + ", on=" + on + "]";
	}
	
}
