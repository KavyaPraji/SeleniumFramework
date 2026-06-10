package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Create_PurchaseOrder_Test {

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
		driver.findElement(By.xpath("//div[@class='mat-list-item-content' and contains(.,'Purchase Order')]")).click();

		driver.findElement(By.xpath("//input[@value='New']")).click();

		// adding vendor

		WebElement vendor = driver.findElement(By.xpath("(//input[@placeholder='Search'])[2]"));
		vendor.sendKeys("ONESOFT VENDOR");
		Thread.sleep(2000);
		vendor.sendKeys(Keys.ARROW_DOWN);
		vendor.sendKeys(Keys.ENTER);

		// payment terms

		WebElement dropdown = driver.findElement(By.cssSelector("select[formcontrolname='AdmnPaymentTermsId']"));
		Select select = new Select(dropdown);
		select.selectByVisibleText("60 Days PDC");

		// Phone number

		WebElement phoneField = driver.findElement(By.cssSelector("input[formcontrolname='Phone']"));
		String phoneNumber = phoneField.getAttribute("value");
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			phoneField.sendKeys("9876543210");
		}
		// GLN number

		WebElement GLNfield = driver.findElement(By.cssSelector("input[placeholder='Global Location Number']"));
		String GLNumber = GLNfield.getAttribute("value");
		if (GLNumber == null || GLNumber.trim().isEmpty()) {
			GLNfield.sendKeys("1234567891234");
		}

		// Expected delivery date

		driver.findElement(By.xpath("(//button[@aria-label='Open calendar'])[2]")).click();
		LocalDate today = LocalDate.now();
		String day = String.valueOf(today.getDayOfMonth());
		driver.findElement(
				By.xpath("//div[contains(@class,'mat-calendar-body-cell-content') and text()='" + day + "']")).click();

		// Item name and details

		WebElement itemNamePO = driver.findElement(By.xpath("(//input[@placeholder='Search'])[3]"));
		itemNamePO.sendKeys("DUPLICATE");
		Thread.sleep(2000);
		itemNamePO.sendKeys(Keys.ARROW_DOWN);
		itemNamePO.sendKeys(Keys.ENTER);

		// Wait for item details to load
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='Quantity']")));

		// Enter Quantity and bonus

		WebElement Qty = driver.findElement(By.xpath("//input[@formcontrolname='Quantity']"));
		Qty.sendKeys("10");
		WebElement bonus = driver.findElement(By.xpath("//input[@formcontrolname='Bonus']"));
		bonus.sendKeys("5");

		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement unitField = driver.findElement(By.cssSelector("input[formcontrolname='LargeUnitName']"));
		wait2.until(ExpectedConditions.attributeContains(unitField, "value", ""));

		// clicking save

		driver.findElement(By.xpath("//input[@value='Save']")).click();

		// Verification PO success
		WebElement latestPO = driver.findElement(By.xpath("(//td[contains(@class,'mat-column-NumberDisplay')])[1]"));
		String newPoNumber = latestPO.getText().trim();
		if (!newPoNumber.isEmpty()) {
			System.out.println("Purchase order " + newPoNumber + " created successfully");
		} else {
			System.out.println("Purchase Order Creation Failed");
		}

		// clean up
		driver.findElement(By.xpath("(//div[contains(@class,'mat-checkbox-inner-container')])[1]")).click();
		driver.findElement(By.xpath("//input[@value='Delete']")).click();
		driver.findElement(By.xpath("//button[normalize-space()='Yes']")).click();

		driver.quit();

	}

}
