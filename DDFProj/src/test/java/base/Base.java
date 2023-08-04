package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;
import util.ExtentReporter;

public class Base {

	WebDriver driver;
	public ExtentTest extentTest;
	public String screenshotFolderPath;
	public Properties prop;
	

	public WebDriver openBrowser(String browserName) throws IOException {

		prop = new Properties();
		File file = new File("src\\test\\resources\\data.properties");
		FileInputStream fis = new FileInputStream(file);
		prop.load(fis);

		if (browserName.equalsIgnoreCase("chrome")) {

			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(options);

		} else if (browserName.equalsIgnoreCase("firefox")) {

			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();

		} else if (browserName.equalsIgnoreCase("edge")) {

			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();

		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // if element not found it will wait for 10 seconds
		driver.get(prop.getProperty("url"));
		

		return driver;

	}
	
	
	// close database
	// read jason
	// read pdf
	// download pdf
	// log network logs
	// connect database
	// api get
	// api post
	

	public void takeSceenShot(ExtentTest test) { // 
		
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "\\screenshots\\" + dateName + ".png";
		File finalDestination = new File(destination);

		System.out.println("--------------------------------" + destination + "--------------------------------");

		try {
			// get the dynamic folder name
			FileHandler.copy(source, finalDestination);
			// put screenshot file in reports
			test.fail("Dummy failure", MediaEntityBuilder.createScreenCaptureFromPath(destination).build());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
