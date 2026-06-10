package tests;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ExcelFile {

	public static void main(String[] args) throws EncryptedDocumentException, IOException, InterruptedException {
		
	

		FileInputStream fis= new FileInputStream("src/test/resources/LoginData.xlsx");
		Workbook wb= WorkbookFactory.create(fis);
		Sheet s=wb.getSheet("Login");
		int rowCount=s.getLastRowNum();
		for(int i=0;i<=rowCount; i++)
		{ 
			Row r= s.getRow(i);
			String User1= r.getCell(0).toString();
			String Pass1=r.getCell(1).toString();
			WebDriver driver= new ChromeDriver();
			driver.manage().window().maximize();
			driver.get("https://qa.pharmacyplus.app/");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//input[@formcontrolname='UserName']")).sendKeys(User1);
			driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(Pass1);
			driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
			
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[normalize-space()='Onesoft Pharma LLC Branch']")).click();
			driver.quit();
			
		}
		// close workbook
		wb.close();
		
		
		
		
	

	}

}
