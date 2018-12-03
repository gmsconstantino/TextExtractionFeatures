

import java.util.Scanner;

import Spell.LCSMap;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		String q = in.nextLine();
		
		LCSMap m = new LCSMap();
		
		while(!q.equals("quit")) {
			m.insert(q);
			System.out.println(m.toString());
			q = in.nextLine();
		}
	}

}
