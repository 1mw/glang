package com.markaldrich.glang;

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Input your source [i] or load file? [f]: ");
		String input = scanner.nextLine();
		if(input.equals("i")) {
			GInterpreter g = new GInterpreter();
			ArrayList<String> lines = new ArrayList<>();
			
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
		} else if(input.equals("f")) {
			System.out.print("Enter file path: ");
			String path = scanner.nextLine();
			String content = "";
			try {
				content = new Scanner(new File(path)).useDelimiter("\\Z").next();
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
			
			GInterpreter g = new GInterpreter();
			g.interpret(content);
		} else {
			System.out.println("Error: You didn't enter \"f\" or \"i\".");
		}
	}
}