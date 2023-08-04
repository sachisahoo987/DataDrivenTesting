package tests;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import base.Base;
import pageobjects.AccountPage;
import pageobjects.HomePage;
import pageobjects.LoginPage;

import util.DataUtil;
import util.ExtentReporter;
import util.MyXLSReader;

public class Login extends Base {
	
	WebDriver driver;
	MyXLSReader excelReader;
	
	ExtentTest test;
	ExtentReports reports;

	@BeforeMethod
	public void init() {
		reports =ExtentReporter.getExtentReport();
		test = reports.createTest("LoginTest");
	}
	
	
	@AfterMethod
	public void tearDown() {
		reports.flush();
		if(driver!=null) {	
			driver.quit();
		}
		
	}
	

	@Test(dataProvider="dataSupplier")
	public void testLogin(HashMap<String,String> hMap) throws IOException {
		
		if(!DataUtil.isRunnable(excelReader, "LoginTest", "Testcases") || hMap.get("Runmode").equals("N")) {
			
			throw new SkipException("Skipping the test as the runmode is set to N");
			
		}
		
		test.log(Status.INFO, "Starting test case Login");
		
		
		driver = openBrowser(hMap.get("Browser"));
		test.log(Status.PASS,"Browser opened succesfully");
		
		
        HomePage homePage = new HomePage(driver);
        homePage.clickOnMyAccountDropMenu();
      
        LoginPage loginPage = homePage.selectLoginOption();
		loginPage.enterEmailAddress(hMap.get("Username"));
		loginPage.enterPassword(hMap.get("Password"));
		AccountPage accountPage = loginPage.clickOnLoginButton();
		
		test.log(Status.FAIL,"Validation Failed");
		
		
		
		
		//////////     ************************ ////
		takeSceenShot(test);
		
		String expectedResult = hMap.get("ExpectedResult");
		
		boolean expectedConvertedResult = false;
		
		if(expectedResult.equalsIgnoreCase("Failure")) {
			
			expectedConvertedResult = false;
			
		}else if(expectedResult.equalsIgnoreCase("Success")){
			
			expectedConvertedResult = true;
		}

		Assert.assertEquals(accountPage.verifyLoginStatusOfUser(),expectedConvertedResult+"------");
		
	
	}
	
	@DataProvider
	public Object[][] dataSupplier() {
		
		Object[][] obj = null;
		
		try {
			
			excelReader = new MyXLSReader("src\\test\\resources\\TutorialsNinja.xlsx");
			obj = DataUtil.getTestData(excelReader, "LoginTest", "Data");
		
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return obj;
		
	}

}
