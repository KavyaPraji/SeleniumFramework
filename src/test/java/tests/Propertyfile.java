
package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Propertyfile {
	public static void main(String[] args) throws IOException, InterruptedException {
		FileInputStream fis= new FileInputStream("src/test/resources/config.properties");
		Properties ps= new Properties();
		ps.load(fis);
		String BROWSER =ps.getProperty("browser");
		String URL =ps.getProperty("url");
		String USERNAME =ps.getProperty("username");
		String PASSWORD =ps.getProperty("password");
		
		WebDriver driver= new ChromeDriver();
		driver.get(URL);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//input[@formcontrolname='UserName']")).sendKeys(USERNAME);
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(PASSWORD);
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		driver.manage().window().maximize();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[normalize-space()='Onesoft Pharma LLC Branch']")).click();
		
		
		
		
		
		
		
		
		
		
	}

}
