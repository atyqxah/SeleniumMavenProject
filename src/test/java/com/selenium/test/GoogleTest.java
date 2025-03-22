package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GoogleTest {
    @Test
    public void testGoogleTitle() {
        // Automatically set up ChromeDriver
        WebDriverManager.chromedriver().setup();
        
        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();
        
        // Open Google
        driver.get("https://www.google.com");
        
        // Get and print page title
        String pageTitle = driver.getTitle();
        System.out.println("Page title is: " + pageTitle);

        // Validate page title
        assertEquals("Google", pageTitle);

        // Close the browser
        driver.quit();
    }
}

//WebDriverManager.chromedriver().setup(); → Automatically configures the ChromeDriver.
//WebDriver driver = new ChromeDriver(); → Launches Chrome.
//driver.get("https://www.google.com"); → Opens Google.
//System.out.println(driver.getTitle()); → Prints the title.
//assertEquals("Google", driver.getTitle()); → Verifies the page title.
//driver.quit(); → Closes the browser.