import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import static org.junit.Assert.assertEquals;
public class TestJunit {
	
public WebDriver d;
	
	@BeforeTest
	  public void beforeTest() {	  
		  System.setProperty("webdriver.chrome.driver", "C:\\Users\\neha\\Desktop\\neha\\driver\\chromedriver_win32\\chromedriver.exe");	
		  d = new ChromeDriver();
		  d.manage().window().maximize();
	  }
	
	  @Test 
	public void titlecheck() {
		  d.get("http://localhost:8585/java-tomcat-maven-example/");
		  String expected="Hello World!";
		  Assert.assertEquals("Hello World!",expected);
		  
	  }
  @AfterTest
  public void afterTest()
  {
	  
  }
}