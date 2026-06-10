package tests;

import java.util.Random;

public class RandomNumber {

	public static void main(String[] args) 
	{
		Random r= new Random();
		int randomInt= r.nextInt(100);
		String data ="kavya"+randomInt+"gmail.com";
		System.out.println(data);
		

	}

}
