package asysconel;

import java.util.Scanner;

public class Run {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.printf("CC 입력:");
		String cc = sc.nextLine();
		
		new ASYSCONEL(cc);
		
	}

}
