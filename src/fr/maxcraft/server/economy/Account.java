package fr.maxcraft.server.economy;

public interface Account {
	public boolean take(double d);
	public double getBalance() ;
	public void give(double d);
	public boolean pay(double d,Account to);
		
}
