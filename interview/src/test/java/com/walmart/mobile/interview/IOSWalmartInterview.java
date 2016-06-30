package com.walmart.mobile.interview;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

/**
 * 
 * @author Ganesh
 * IOS Native App Automation using Simulator, Appium and Selenium WebDriver
 *
 */

public class IOSWalmartInterview {
	
	private AppiumDriver<MobileElement> driver;
	
	/**
	 * 
	 * Initial Setup for the IOSDriver which will install the Walmart App and instantiate the App on the IOS Simulator
	 * @throws Exception
	 * 
	 */
	
	@Before
	public void setup() throws Exception {
		//Set Up Appium
		File app = new File("/Users/Ganesh/Library/Developer/Xcode/DerivedData/QEInterview-fptjomwgedzfuxafrjuxzfguokir/Build/Products/Debug-iphonesimulator/QEInterview.app");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.2");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 5s");
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		driver = new IOSDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
		
	/**
	 * 
	 * This test is to verify if the Slider is working as expected using Appium IOS Slider functionality. 
	 * Catch the exception if there is any issue like app crash, Slider struck etc..
	 * @throws Exception
	 * 
	 */
	@Test 
	public void testSpinSlider() throws Exception {
		
		WebElement slider = driver.findElementByClassName("UIASlider");
		
		int actPercent = 0, expPercent = 0;
		
		try {
			for(double i= 0.1f ; i < 1.0f ; i += 0.1f) {
				slider.sendKeys(" " + i + " ");
				actPercent = (int) (i*100);
				String strPercent = slider.getAttribute("value");
				String[] extractPercent = strPercent.split("%");
				expPercent = Integer.parseInt(extractPercent[0]);
				boolean isSliderWorking = ((actPercent < expPercent) && (expPercent <= actPercent+5));
				assertTrue("Slider Value is not in Range", isSliderWorking);
				} 
		}
		catch(WebDriverException e){
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
	/**
	 * 
	 * This test is to verify if the Image is spinning clockwise when we move the slider. 
	 * Catch the exception if there is any issue like app crash, Slider struck etc..
	 * The clockwise spinning was not feasible to automate as the attribute Location & Size is not 
	 * sufficient enough to calculate the clockwise spinning.
	 * The other option is Image comparision by capturing the image when we move the slider, 
	 * the sampling is more important on this options as the image spin is not clear on how many frames 
	 * we need to capture
	 * Using the below logic we were able to verify the image is spinning, the idea is the X & Y values 
	 * varies between each quadrant but it can't reach the maximum value of 4.
	 * @throws Exception
	 * 
	 */
	
	@Test
	public void testImageSpin() throws Exception {
		
		WebElement slider = driver.findElementByClassName("UIASlider");
		WebElement logo = driver.findElementByClassName("UIAImage");
		
		int x = 0, y = 0, preX = -1, preY = -1;
		
		try {
			for(double i= 0f ; i < 1.0f ; i += 0.1f) {
				slider.sendKeys(" " + i + " ");							
				
				if (logo.getLocation().getX() > preX) {
					x++;
				} else x--;
				
				if (logo.getLocation().getY() > preY) {
					y++;
				} else y--;
				
				boolean isImageSpinning = (Math.abs(x) > 4 || Math.abs(y) > 4);
				assertFalse("The Image Spin is not working", isImageSpinning);	
				preX = logo.getLocation().getX();
				preY = logo.getLocation().getY();										
				
			} 
		}
		catch(WebDriverException e){
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 
	 * IOSDriver quit after every test case is completed.
	 * @throws Exception
	 * 
	 */
	
	@After
	public void tearDown() throws Exception {
        try{
        	driver.quit();
        }catch(Exception e){
        	
        }
    }
	
}

