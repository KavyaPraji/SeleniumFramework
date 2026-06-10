package tests;

public class RuntimeCMD 
{

	public static void main(String[] args) 
	{
		String browser = System.getProperty("browser");

		String url =System.getProperty("url");

		System.out.println("Browser: " + browser);

		System.out.println("URL: " + url);
	}


}



