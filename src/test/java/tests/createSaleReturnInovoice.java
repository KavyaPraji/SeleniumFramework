package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class createSaleReturnInovoice {

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
		driver.manage().window().maximize();

		// login

		driver.findElement(By.xpath("//input[@formcontrolname='UserName']")).sendKeys(USERNAME);
		driver.findElement(By.xpath("//input[@formcontrolname='Password']")).sendKeys(PASSWORD);
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		driver.findElement(By.xpath("//button[normalize-space()='Branch 15']")).click();

		// left navigation
		driver.findElement(By.xpath("//*[contains(text(),'Purchasing & Vendor Management')]")).click();
		driver.findElement(By.xpath("//*[contains(text(),'Goods Received Note')]")).click();
		driver.findElement(By.xpath("//input[@value='New']")).click();

		// Entering Vendor

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement vendor = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@placeholder='Search'])[3]")));

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

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@aria-label='Open calendar'])[3]")));

		driver.findElement(By.xpath("(//button[@aria-label='Open calendar'])[3]")).click();

		wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//div[contains(@class,'mat-calendar-body-cell-content') and text()='" + currentDay + "']")));

		driver.findElement(
				By.xpath("//div[contains(@class,'mat-calendar-body-cell-content') and text()='" + currentDay + "']"))
				.click();

		// item search
		String ItemName = "DUPLICATE";
		WebElement itemName = driver.findElement(By.xpath("(//input[@placeholder='Search'])[4]"));
		itemName.sendKeys(ItemName);
		Thread.sleep(2000);
		itemName.sendKeys(Keys.ARROW_DOWN);
		itemName.sendKeys(Keys.ENTER);

		// Enter Quantity and bonus

		WebElement Qty = driver.findElement(By.xpath("//input[@formcontrolname='Qty' and @type='text']"));
		Qty.sendKeys("10");
		WebElement bonus = driver.findElement(By.xpath("//input[@formcontrolname='FreeQty']"));
		bonus.sendKeys("5");

		WebElement unitField = driver.findElement(By.cssSelector("input[formcontrolname='UomName']"));
		wait.until(ExpectedConditions.attributeContains(unitField, "value", ""));

		driver.findElement(By.xpath("(//input[@formcontrolname='ExpiryDateMonth'])[1]")).sendKeys("1");
		driver.findElement(By.cssSelector("input[formcontrolname='ExpiryDateYear']")).sendKeys("27");

		driver.findElement(By.xpath("//input[@value='Save']")).click();

		// verification

		WebElement grnElement = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//td[contains(@class,'mat-column-NumberDisplay')]")));

		String grnNo = grnElement.getText().trim();

		if (!grnNo.isEmpty()) {
			System.out.println("GRN " + grnNo + " Created Successfully");
		} else {
			System.out.println("GRN Creation Failed");
		}

		// left navigation
		driver.findElement(By.xpath("//div[contains(@class,'mat-list-item-content')][contains(.,'Sales Management')]"))
				.click();
		By retailInvoice = By.xpath("//div[contains(@class,'mat-list-item-content') and contains(.,'Retail Invoice')]");
		driver.findElement(retailInvoice).click();

		// new button click
		driver.findElement(By.xpath("//input[@type='button' and @value='New']")).click();

		// item search
		WebElement itemsearch = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@placeholder='Search'])[6]")));
		itemsearch.sendKeys(ItemName);
		Thread.sleep(5000);
		itemsearch.sendKeys(Keys.ARROW_DOWN);
		itemsearch.sendKeys(Keys.ENTER);

		// Batch Selection

		WebElement batchDropdown = driver.findElement(By.xpath("//select[@formcontrolname='Batch']"));

		// Wait for batch options to load
		int count = 0;

		while (count < 20) {

			Select batchWait = new Select(driver.findElement(By.xpath("//select[@formcontrolname='Batch']")));

			if (batchWait.getOptions().size() > 1) {
				break;
			}

			Thread.sleep(1000);
			count++;
		}

		// Re-locate drop down after wait
		batchDropdown = driver.findElement(By.xpath("//select[@formcontrolname='Batch']"));

		batchDropdown.click();

		Select batch = new Select(batchDropdown);

		List<WebElement> options = batch.getOptions();

		System.out.println("Options Count: " + options.size());

		for (WebElement option : options) {
			System.out.println(option.getText());
		}

		for (int i = 1; i < options.size(); i++) {

			if (!options.get(i).getText().trim().equalsIgnoreCase("Select")) {

				batch.selectByIndex(i);

				((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input'));"
						+ "arguments[0].dispatchEvent(new Event('change'));", batchDropdown);

				break;
			}
		}

		// Enter sales Quantity
		driver.findElement(By.xpath("//input[@formcontrolname='Qty']")).sendKeys("2");

		// add button click

		WebElement addButton = driver
				.findElement(By.xpath("//input[@type='button' and normalize-space(@value)='Add']"));
		addButton.click();

		// fetching grand total and entering in amount received

		WebElement grandTotal = driver.findElement(By.xpath("//input[@formcontrolname='GrandTotal']"));
		String grandTotalValue = grandTotal.getAttribute("value");

		WebElement amtReceived = driver.findElement(By.xpath("//input[@formcontrolname='AmtRcvd']"));

		amtReceived.click();

		// Select existing value (0.00)
		amtReceived.sendKeys(Keys.CONTROL, "a");

		// Delete it
		amtReceived.sendKeys(Keys.DELETE);

		// Enter Grand Total
		amtReceived.sendKeys(grandTotalValue);

		// Move focus out of the field
		amtReceived.sendKeys(Keys.TAB);

		Thread.sleep(1000);

		// save the forms
		driver.findElement(By.xpath("//span[normalize-space()='Save']")).click();

		// back to list
		WebElement backToList = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@mattooltip='Back To List']")));

		backToList.click();

		// verification
		WebElement salesNoElement = driver.findElement(By.xpath("//td[contains(@class,'mat-column-NumberDisplay')]"));
		String salesNo = salesNoElement.getText().trim();

		if (!salesNo.isEmpty()) {
			System.out.println("Sales " + salesNo + " Created Successfully");
		} else {
			System.out.println("Sales Creation Failed");
		}

		// Left navigation

		driver.findElement(By.xpath("(//*[contains(normalize-space(text()),'Retail Sales Return')])[1]")).click();

		// sales return new
		WebElement newButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='New']")));

		newButton.click();

		// sales return invoice number
		WebElement searchBox = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@placeholder='Search'])[2]")));

		searchBox.sendKeys(salesNo);
		Thread.sleep(2000);
		searchBox.sendKeys(Keys.ARROW_DOWN);
		searchBox.sendKeys(Keys.ENTER);

		// Entering return qty
		WebElement rtnQty = driver.findElement(By.xpath("//input[@formcontrolname='RtnQty']"));
		rtnQty.sendKeys("1");

		// Get NetValue
		WebElement netValueField = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='NetValue']")));

		// Read value from read only field
		String netValue = netValueField.getAttribute("value");

		// adding payment
		WebElement paymentsButton = driver.findElement(By.xpath("//input[@type='button' and @value='Payments']"));
		paymentsButton.click();

		// Locate Amount field
		WebElement amountField = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@formcontrolname='Amount']")));

		// Clear existing value and enter NetValue
		amountField.clear();
		amountField.sendKeys(netValue);

		// add payment button
		WebElement add = driver.findElement(By.xpath("//button[text()='Add']"));

		add.click();

		// ok button
		WebElement okButton = driver.findElement(By.xpath("//button[normalize-space()='Ok']"));

		okButton.click();

		// saving the form
		WebElement saveButton = driver.findElement(By.xpath("//input[@type='button' and @value='Save']"));
		saveButton.click();

		// verification
		WebElement salesReturnNoElement = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(@class,'mat-column-InvNo')]")));

		String salesReturnNo = salesReturnNoElement.getText().trim();

		System.out.println("Sales Return " + salesReturnNo +" Created Successfully");

		// clean up
		/*
		 * WebElement selectAll = driver .findElement(By.xpath(
		 * "(//div[contains(@class,'mat-checkbox-inner-container')])[1]"));
		 * 
		 * selectAll.click();
		 * 
		 * By deleteBtn = By.xpath("//input[@type='button' and @value='Delete']"); By
		 * yesBtn = By.xpath("//button[normalize-space()='Yes']"); By row =
		 * By.xpath("(//mat-row)[1]");
		 * 
		 * wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
		 * wait.until(ExpectedConditions.elementToBeClickable(yesBtn)).click();
		 * 
		 * // Critical validation
		 * wait.until(ExpectedConditions.invisibilityOfElementLocated(row));
		 */
		// driver.quit();

	}

}
