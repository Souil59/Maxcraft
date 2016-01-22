package net.nathem.script.core;

import net.nathem.script.enums.OptionType;

public class Option {
	
	private String key;
	private String value;
	private OptionType optionType;
	
	public Option(String key, String value, OptionType optionType) {

		this.key = key;
		this.value = value;
		this.optionType = optionType;
	}
	
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	public OptionType getOptionType() {
		return optionType;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Option [" + key + "=" + value + "]";
	}
	
	
	

}
