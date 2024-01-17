package day31;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class FDCalculatorDDT {

	public static void main(String[] args) throws IOException, InterruptedException {
	
		WebDriver driver=new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get("https://www.moneycontrol.com/fixed-income/calculator/state-bank-of-india-sbi/fixed-deposit-calculator-SBI-BSB001.html");
		driver.manage().window().maximize();
		
		String file=System.getProperty("user.dir")+"\\testdata\\caldata.xlsx";
		
		int rows=ExcelUtils.getRowCount(file, "Sheet1");
		
		JavascriptExecutor js=(JavascriptExecutor)driver;
		
		for(int i=1;i<=rows;i++)
		{
			// read data from excel sheet
			String princ=ExcelUtils.getCellData(file,"Sheet1",i,0);
			String rateofinterest=ExcelUtils.getCellData(file,"Sheet1",i,1);
			String per1=ExcelUtils.getCellData(file,"Sheet1",i,2);
			String per2=ExcelUtils.getCellData(file,"Sheet1",i,3);
			String fre=ExcelUtils.getCellData(file,"Sheet1",i,4);
			String exp_mvalue=ExcelUtils.getCellData(file,"Sheet1",i,5);
			
			// pass the to the app
			driver.findElement(By.xpath("//input[@id='principal']")).sendKeys(princ);
			driver.findElement(By.xpath("//input[@id='interest']")).sendKeys(rateofinterest);
			
			driver.findElement(By.xpath("//input[@id='tenure']")).sendKeys(per1);
			
			Select perdrp=new Select(driver.findElement(By.xpath("//select[@id='tenurePeriod']")));
			perdrp.selectByVisibleText(per2);
						
			Select fredrp=new Select(driver.findElement(By.xpath("//select[@id='frequency']")));
			fredrp.selectByVisibleText(fre);
			
			WebElement calculatebtn=driver.findElement(By.xpath("//*[@id='fdMatVal']/div[2]/a[1]")); // calculate button
			js.executeScript("arguments[0].click();",calculatebtn);
			
			//validation & update results in excel
			String act_mvalue=driver.findElement(By.xpath("//span[@id='resp_matval']/strong")).getText();
			
			if(Double.parseDouble(exp_mvalue)==Double.parseDouble(act_mvalue))				
			{	
				System.out.println("Test passed");
				ExcelUtils.setCellData(file,"Sheet1",i,7,"Passed");
				ExcelUtils.fillGreenColor(file, "Sheet1",i,7);
				
			}
			else
			{
				System.out.println("Test failed");
				ExcelUtils.setCellData(file,"Sheet1",i,7,"Failed");
				ExcelUtils.fillRedColor(file, "Sheet1",i,7);
			}
			
			Thread.sleep(3000);
			
			WebElement clearbtn=driver.findElement(By.xpath("//*[@id='fdMatVal']/div[2]/a[2]/img")); //clear button
			js.executeScript("arguments[0].click();",clearbtn);
		}
	}

}
