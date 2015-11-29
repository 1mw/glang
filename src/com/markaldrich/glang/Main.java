package com.markaldrich.glang;

import java.util.*;

public class Main {
	public static void main(String[] args) {
		GInterpreter g = new GInterpreter();
		//g.interpret("@init var\n@storel var :: 2\n@+vl var :: 1 :: var\n@printv var");
		//g.interpret("@init var1\n@storel var1 :: 10\n@init var2\n@storel var2 :: 2\n@-vv var1 :: var2 :: var1\n@printv var1");
		ArrayList<String> lines = new ArrayList<>();
		
		Scanner scanner = new Scanner(System.in);
		
		int i = 0;
		while(true) {
			System.out.print("" + i + ": ");
			String s = scanner.nextLine();
			if(!s.equals("EOF")) {
				lines.add(s);
			} else {
				break;
			}
			i++;
		}
		
		String[] array = lines.toArray(new String[] {});
		String src = "";
		for(String s : array) {
			src += s + "\n";
		}
		g.interpret(src);
	}
}