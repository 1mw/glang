package com.markaldrich.glang;

public class RegistryTie {
	private String name;
	private int pointer;
	
	public RegistryTie(String name, int pointer) {
		this.name = name;
		this.pointer = pointer;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPointer() {
		return pointer;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPointer(int pointer) {
		this.pointer = pointer;
	}
	
	@Override
	public String toString() {
		return "name: " + name + "; pointer: " + pointer;
	}
}