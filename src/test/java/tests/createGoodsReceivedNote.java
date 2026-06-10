package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class createGoodsReceivedNote {

	public static void main(String[] args) throws IOException, InterruptedException {
		FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
		Properties ps = new Properties();
		ps.load(fis);
		String BROWSER = ps.getProperty("browser");
		String URL = ps.getProperty("url");
		String USERNAME = ps.getProperty("username");
		String PASSWORD = ps.getProperty("password");

		WebDriver driver = null;
		if (BROWSER.equals("chrome")) {
			driver = new ChromeDriver();

		} else if (BROWSER.equals("edge")) {
			driver = new EdgeDriver();

		} else if (BROWSER.equals("firefox")) {
			driver = new FirefoxDriver();
		} else {
			driver = new ChromeDriver();

		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.get(URL);

		driver.findElement(By.xpath("//input[@formcontrolname='UserName']")).sendKeys(USERNAME);
		driver.findElement(By.xpath("//input[@formcontrolname='Password']")).sendKeys(PASSWORD);
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		driver.findElement(By.xpath("//button[normalize-space()='Branch 15']")).click();

		driver.manage().window().maximize();

		driver.findElement(By.xpath("//*[contains(text(),'Purchasing & Vendor Management')]")).click();
		driver.findElement(By.xpath("//*[contains(text(),'Goods Received Note')]")).click();
		driver.findElement(By.xpath("//input[@value='New']")).click();

		// adding vendor
		WebElement vendor = driver.findElement(By.xpath("(//input[@placeholder='Search'])[3]"));
		vendor.sendKeys("ONESOFT VENDOR");
		Thread.sleep(2000);
		vendor.sendKeys(Keys.ARROW_DOWN);
		vendor.sendKeys(Keys.ENTER);

		// entering invoice number
		Random rand = new Random();
		String invoiceNumber = "INV_" + rand.nextInt(1000000);
		driver.findElement(By.cssSelector("input[formcontrolname='InvoiceNumber']")).sendKeys(invoiceNumber);

		String currentDay = String.valueOf(LocalDate.now().getDayOfMonth());

		// Invoice Date
		driver.findElement(By.xpath("(//button[@aria-label='Open calendar'])[1]")).click();

		driver.findElement(
				By.xpath("//div[contains(@class,'mat-calendar-body-cell-content') and text()='" + currentDay + "']"))
				.click();

		// Date Received
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@aria-label='Open calendar'])[3]")));

		driver.findElement(By.xpath("(//button[@aria-label='Open calendar'])[3]")).click();

		wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[contains(@class,'mat-calendar-body-cell-content') and text()='" + currentDay + "']")));

		driver.findElement(
				By.xpath("//div[contains(@class,'mat-calendar-body-cell-content') and text()='" + currentDay + "']"))
				.click();

		// item search
		WebElement itemName = driver.findElement(By.xpath("(//input[@placeholder='Search'])[4]"));
		itemName.sendKeys("DUPLICATE");
		Thread.sleep(2000);
		itemName.sendKeys(Keys.ARROW_DOWN);
		itemName.sendKeys(Keys.ENTER);

		// Enter Quantity and bonus

		WebElement Qty = driver.findElement(By.xpath("//input[@formcontrolname='Qty' and @type='text']"));
		Qty.sendKeys("10");
		WebElement bonus = driver.findElement(By.xpath("//input[@formcontrolname='FreeQty']"));
		bonus.sendKeys("5");

		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement unitField = driver.findElement(By.cssSelector("input[formcontrolname='UomName']"));
		wait2.until(ExpectedConditions.attributeContains(unitField, "value", ""));

		driver.findElement(By.xpath("(//input[@formcontrolname='ExpiryDateMonth'])[1]")).sendKeys("1");
		driver.findElement(By.cssSelector("input[formcontrolname='ExpiryDateYear']")).sendKeys("27");

		driver.findElement(By.xpath("//input[@value='Save']")).click();

		String grnNo = driver.findElement(By.xpath("//td[contains(@class,'mat-column-NumberDisplay')]")).getText()
				.trim();

		if (!grnNo.isEmpty()) {
			System.out.println("GRN " + grnNo + " Created Successfully");
		} else {
			System.out.println("GRN Creation Failed");
		}

		// clean up

		WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(15));
		By checkbox = By.xpath("//div[contains(@class,'mat-checkbox-inner-container')]");
		By deleteBtn = By.xpath("//input[@type='button' and @value='Delete']");
		By yesBtn = By.xpath("//button[normalize-space()='Yes']");
		By row = By.xpath("(//mat-row)[1]");

		wait3.until(ExpectedConditions.elementToBeClickable(checkbox)).click();
		wait3.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
		wait3.until(ExpectedConditions.elementToBeClickable(yesBtn)).click();

		// 🔥 critical validation
		wait3.until(ExpectedConditions.invisibilityOfElementLocated(row));

		driver.quit();

	}

}
