

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Spell.LCSMap;
import Spell.LCSObject;

public class FileMain {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
//		Scanner in = new Scanner(new FileInputStream("Movs2.txt"));
		Scanner in = new Scanner(new FileInputStream("OS_LOG_ERROR.txt"));
		String q = in.nextLine();
		
		LCSMap m = new LCSMap();
		
		//m.LCSObjects.add(new LCSObject("COMPRA 9929 *"));
		//m.LCSObjects.add(new LCSObject("TRF * P/ *"));
		//m.LCSObjects.add(new LCSObject("COMPRA 9929 *"));
//		m.LCSObjects.add(new LCSObject("FNAC *"));
		
		while(in.hasNextLine()) {
			m.insert(q);
			q = in.nextLine();
		}
		
		System.out.println(m.toString());
	}

}
