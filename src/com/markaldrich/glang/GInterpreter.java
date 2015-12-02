package com.markaldrich.glang;

import java.util.*;
import java.io.*;

public class GInterpreter {
	private int[] stack = new int[1000];
	private int si = 0;
	
	private int[] registers = new int[1000];
	private int ri = 0;
	
	private int i = 0;
	
	private ArrayList<Integer> funcsIgnored = new ArrayList<>();
	private ArrayList<Integer> funcEndsIgnored = new ArrayList<>();
	
	RegistryTie[] ties = new RegistryTie[1000];
	int ti = 0;
	
	public int interpret(String src) {
		// Get lines
		String[] lines = src.split("\n");
		
		for(int x = 0; x < lines.length; x++) {
			lines[x] = lines[x].trim();
		}
		
		// Loop through lines.
		try {
			linesLoop: for(; i < lines.length; ) {
				// Comment
				if(lines[i].startsWith(";")) {
					i++;
					continue;
				}
				
				// Resolve operator
				if(lines[i].startsWith("@init ")) { // 6
					String suffix = lines[i].substring(6).trim();
					String varName = suffix.substring(1);
					RegistryTie t = new RegistryTie(varName, ri++);
					ties[ti++] = t;
				} else if(lines[i].startsWith("@store ")) { // 7
					String suffix = lines[i].substring(7).trim();
					String[] operands = suffix.split("::");
					
					if(operands.length != 2) {
						displayError("ERROR: @store takes 2 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					for(int x = 0; x < operands.length; x++) {
						operands[x] = operands[x].trim();
					}
					
					RegistryTie t = resolveTie(operands[0]);
					if(t == null) {
						displayError("@storel cannot resolve variable: " + operands[0], i);
						return -1;
					}
					
					int value = 0;
					try {
						value = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					registers[t.getPointer()] = value;
				} else if(lines[i].startsWith("@prints ")) { // 8
					String suffix = lines[i].substring(8);
					System.out.print(suffix);
				} else if(lines[i].startsWith("@printl ")) { // 8
					String suffix = lines[i].substring(8);
					System.out.println(suffix);
				} else if(lines[i].startsWith("@printc ")) { // 8
					String suffix = lines[i].substring(8);
					int value = 0;
					
					try {
						value = resolveValue(suffix);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					char c = (char) value;
					System.out.print("" + c);
				} else if(lines[i].startsWith("@print ")) { // 7
					String suffix = lines[i].substring(7);
					suffix.trim();
					
					int value = 0;
					try {
						value = resolveValue(suffix);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					System.out.print(value);
				} else if(lines[i].startsWith("@+ ")) { // 3
					String suffix = lines[i].substring(3);
					String[] operands = suffix.split("::");
					for(int x = 0; x < operands.length; x++) {
						operands[x] = operands[x].trim();
					}
					
					if(operands.length != 3) {
						displayError("@+ takes 3 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					// Resolve operands
					int value1 = 0;
					int value2 = 0;
					int rPointer = 0;
					
					try {
						value1 = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					try {
						value2 = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					RegistryTie t = resolveTie(operands[2]);
					if(t == null) {
						displayError("@+: cannot resolve result variable: " + operands[2], i);
						return 1;
					}
					rPointer = t.getPointer();
					
					registers[rPointer] = value1 + value2;
				} else if(lines[i].startsWith("@- ")) { // 3
					String suffix = lines[i].substring(3);
					String[] operands = suffix.split("::");
					for(int x = 0; x < operands.length; x++) {
						operands[x] = operands[x].trim();
					}
					
					if(operands.length != 3) {
						displayError("@- takes 3 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					// Resolve operands
					int value1 = 0;
					int value2 = 0;
					int rPointer = 0;
					
					try {
						value1 = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					try {
						value2 = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					RegistryTie t = resolveTie(operands[2]);
					if(t == null) {
						displayError("@-: cannot resolve result variable: " + operands[2], i);
						return 1;
					}
					rPointer = t.getPointer();
					
					registers[rPointer] = value1 - value2;
				} else if(lines[i].startsWith("@* ")) { // 3
					String suffix = lines[i].substring(3);
					String[] operands = suffix.split("::");
					for(int x = 0; x < operands.length; x++) {
						operands[x] = operands[x].trim();
					}
					
					if(operands.length != 3) {
						displayError("@* takes 3 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					// Resolve operands
					int value1 = 0;
					int value2 = 0;
					int rPointer = 0;
					
					try {
						value1 = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					try {
						value2 = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					RegistryTie t = resolveTie(operands[2]);
					if(t == null) {
						displayError("@*: cannot resolve result variable: " + operands[2], i);
						return 1;
					}
					rPointer = t.getPointer();
					
					registers[rPointer] = value1 * value2;
				} else if(lines[i].startsWith("@/ ")) { // 3
					String suffix = lines[i].substring(3);
					String[] operands = suffix.split("::");
					for(int x = 0; x < operands.length; x++) {
						operands[x] = operands[x].trim();
					}
					
					if(operands.length != 3) {
						displayError("@/ takes 3 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					// Resolve operands
					int value1 = 0;
					int value2 = 0;
					int rPointer = 0;
					
					try {
						value1 = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					try {
						value2 = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					RegistryTie t = resolveTie(operands[2]);
					if(t == null) {
						displayError("@/: cannot resolve result variable: " + operands[2], i);
						return 1;
					}
					rPointer = t.getPointer();
					
					registers[rPointer] = value1 / value2;
				} else if(lines[i].startsWith("@jumpz ")) { // 7
					String suffix = lines[i].substring(7);
					String[] operands = suffix.split("::");
					if(operands.length != 2) {
						displayError("@jumpz takes 2 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					int value = 0;
					try {
						value = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					int line = 0;
					try {
						line = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					if(value == 0) {
						i = line;
						continue;
					}
				} else if(lines[i].startsWith("@jumpp ")) { // 7
					String suffix = lines[i].substring(7);
					String[] operands = suffix.split("::");
					if(operands.length != 2) {
						displayError("@jumpp takes 2 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					operands[0] = operands[0].trim();
					operands[1] = operands[1].trim();
					
					int value = 0;
					try {
						value = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					int line = 0;
					try {
						line = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					if(value > 0) {
						i = line;
						continue;
					}
				} else if(lines[i].startsWith("@jumpn ")) { // 7
					String suffix = lines[i].substring(7);
					String[] operands = suffix.split("::");
					if(operands.length != 2) {
						displayError("@jumpn takes 2 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					operands[0] = operands[0].trim();
					operands[1] = operands[1].trim();
					
					int value = 0;
					try {
						value = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					int line = 0;
					try {
						line = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					if(value < 0) {
						i = line;
					}
				} else if(lines[i].startsWith("@jumpnz ")) { // 8
					String suffix = lines[i].substring(8);
					String[] operands = suffix.split("::");
					if(operands.length != 2) {
						displayError("@jumpnz takes 2 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					operands[0] = operands[0].trim();
					operands[1] = operands[1].trim();
					
					int value = 0;
					try {
						value = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					int line = 0;
					try {
						line = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					if(value != 0) {
						i = line;
						continue;
					}
				} else if(lines[i].startsWith("@equal ")) { // 7
					String suffix = lines[i].substring(7);
					String[] operands = suffix.split("::");
					if(operands.length != 3) {
						displayError("@equal takes 3 operands, " + operands.length + " supplied.", i);
					}
					operands[0] = operands[0].trim();
					operands[1] = operands[1].trim();
					operands[2] = operands[2].trim();
					
					int value1 = 0;
					int value2 = 0;
					
					try {
						value1 = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					try {
						value2 = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					RegistryTie t = resolveTie(operands[2]);
					if(t == null) {
						displayError("@equal: cannot resolve variable: " + operands[2], i);
						return 1;
					}
					
					if(value1 == value2) {
						registers[t.getPointer()] = 1;
					} else {
						registers[t.getPointer()] = 0;
					}
				} else if(lines[i].startsWith("@push ")) { // 6
					String suffix = lines[i].substring(6);
					suffix = suffix.trim();
					int value = 0;
					
					try {
						value = resolveValue(suffix);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					try {
						pushToStack(value);
					} catch(StackError e) {
						displayError(e.getMessage(), i);
						return 1;
					}
				} else if(lines[i].startsWith("@pop ")) { // 5
					String suffix = lines[i].substring(5);
					suffix = suffix.trim();
					
					RegistryTie t = resolveTie(suffix);
					if(t == null) {
						displayError("@pop: cannot resolve variable: " + suffix, i);
						return 1;
					}
					
					try {
						registers[t.getPointer()] = popFromStack();
					} catch(StackError e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					stack[si] = 0;
				} else if(lines[i].startsWith("@jump ")) { // 6
					String suffix = lines[i].substring(6);
					
					int line = 0;
					try {
						line = resolveValue(suffix);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					i = line;
					continue;
				} else if(lines[i].startsWith("@newline")) {
					System.out.println();
				} else if(lines[i].startsWith("@call ")) { // 6
					String suffix = lines[i].substring(6);
					
					int line = 0;
					try {
						line = resolveValue(suffix);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					pushToStack(i + 1);
					i = line;
					continue;
				} else if(lines[i].startsWith("@func")) {
					if(!funcsIgnored.contains(i)) {
						funcsIgnored.add(i);
						int funcLevel = 0;
						// for(; !lines[i].startsWith("@endfunc"); i++);
						for(;; i++) {
							//System.out.println("i is " + i + ": " + lines[i] + "\n i + 1 is " + (i + 1) + ": " + lines[i + 1]);
							if(lines[i].startsWith("@func")) {
								funcLevel++;
							}
							if(lines[i].startsWith("@endfunc")) {
								if(funcLevel == 1) { // First func is not considered a level
									funcEndsIgnored.add(i);
									i++;
									continue linesLoop;
								} else {
									funcLevel--;
								}
							}
						}
					}
				} else if(lines[i].startsWith("@endfunc")) {
					if(!funcEndsIgnored.contains(i)) {
						displayError("@endfunc missing matching @func", i);
						return 1;
					}
				} else if(lines[i].startsWith("@% ")) {
					String suffix = lines[i].substring(3);
					String[] operands = suffix.split("::");
					for(int x = 0; x < operands.length; x++) {
						operands[x] = operands[x].trim();
					}
					
					if(operands.length != 3) {
						displayError("@% takes 3 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					// Resolve operands
					int value1 = 0;
					int value2 = 0;
					int rPointer = 0;
					
					try {
						value1 = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					try {
						value2 = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					RegistryTie t = resolveTie(operands[2]);
					if(t == null) {
						displayError("@%: cannot resolve result variable: " + operands[2], i);
						return 1;
					}
					rPointer = t.getPointer();
					
					registers[rPointer] = value1 % value2;
				} else if(lines[i].startsWith("@lt ")) {
					String suffix = lines[i].substring(3);
					String[] operands = suffix.split("::");
					for(int x = 0; x < operands.length; x++) {
						operands[x] = operands[x].trim();
					}
					
					if(operands.length != 3) {
						displayError("@lt takes 3 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					// Resolve operands
					int value1 = 0;
					int value2 = 0;
					int rPointer = 0;
					
					try {
						value1 = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					try {
						value2 = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					RegistryTie t = resolveTie(operands[2]);
					if(t == null) {
						displayError("@lt: cannot resolve result variable: " + operands[2], i);
						return 1;
					}
					rPointer = t.getPointer();
					
					registers[rPointer] = (value1 < value2) ? 1 : 0;
				} else if(lines[i].startsWith("@gt ")) {
					String suffix = lines[i].substring(3);
					String[] operands = suffix.split("::");
					for(int x = 0; x < operands.length; x++) {
						operands[x] = operands[x].trim();
					}
					
					if(operands.length != 3) {
						displayError("@gt takes 3 operands, " + operands.length + " supplied.", i);
						return 1;
					}
					
					// Resolve operands
					int value1 = 0;
					int value2 = 0;
					int rPointer = 0;
					
					try {
						value1 = resolveValue(operands[0]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					try {
						value2 = resolveValue(operands[1]);
					} catch(UnresolvedException e) {
						displayError(e.getMessage(), i);
						return 1;
					}
					
					RegistryTie t = resolveTie(operands[2]);
					if(t == null) {
						displayError("@gt: cannot resolve result variable: " + operands[2], i);
						return 1;
					}
					rPointer = t.getPointer();
					
					registers[rPointer] = (value1 > value2) ? 1 : 0;
				}
				
				i++;
			}
		} catch(RuntimeException e) {
			e.printStackTrace();
		} finally {
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(System.getProperty("user.home") + "/log" + System.currentTimeMillis() + ".txt", "UTF-8");
			} catch(Exception e) {
				e.printStackTrace();
				return 1;
			}
			writer.println("\nProgram terminated.");
			writer.println("Printing registers...");
			for(int s = 0; s < 1000; s++) {
				writer.println("" + s + ": " + registers[s]);
			}
			
			writer.println();
			writer.println("Printing ties...");
			for(int s = 0; s < 1000; s++) {
				writer.println("" + s + ": " + ties[s]);
			}
			
			writer.println();
			writer.println("si=" + si);
			
			writer.println();
			writer.println("Printing stack...");
			for(int s = 0; s < 1000; s++) {
				writer.println("" + s + ": " + stack[s]);
			}
			
			writer.close();
			
			return 0;
		}
	}
	
	private RegistryTie resolveTie(String var) {
		String v = var.substring(1);
		for(int i = 0; i < ties.length; i++) {
			if(ties[i] != null && ties[i].getName().equals(v)) {
				return ties[i];
			}
		}
		
		// Variable does not exist, make it
		RegistryTie t = new RegistryTie(v, ri++);
		ties[ti++] = t;
		return t;
	}
	
	private void displayError(String message, int line) {
		System.err.println("ERROR: " + message + "\n@ line " + line);
	}
	
	private int resolveValue(String operand) throws UnresolvedException {
		operand = operand.trim();
		
		if(operand.equals("~")) {
			return i;
		} else if(operand.equals("^")) {
			return i + 1;
		} else if(operand.startsWith("$")) {
			// Variable=
			RegistryTie t = resolveTie(operand);
			if(t == null) {
				throw new UnresolvedException("resolveValue: cannot resolve variable: " + operand);
			}
			
			return registers[t.getPointer()];
		} else if(operand.startsWith("#")) {
			// Register
			String registerName = operand.substring(1);
			int index = 0;
			try {
				index = Integer.parseInt(registerName);
			} catch(Exception e) {
				throw new UnresolvedException("resolveValue: cannot resolve register: " + operand);
			}
			
			return registers[index];
		} else {
			// Literal
			int literal = 0;
			try {
				literal = Integer.parseInt(operand);
			} catch(Exception e) {
				throw new UnresolvedException("resolveValue: cannot resolve literal: " + operand);
			}
			
			return literal;
		}
	}
	
	private void pushToStack(int x) throws StackError {
		try {
			stack[si++] = x;
		} catch(Exception e) {
			throw new StackError("Stack overflow!");
		}
	}
	
	private int popFromStack() throws StackError {
		try {
			return stack[--si];
		} catch(Exception e) {
			throw new StackError("Stack underflow!");
		}
	}
}
