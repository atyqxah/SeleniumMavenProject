package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

//import java.io.File;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class OTPLoginTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(100));
    }

    @Test
    public void testLoginWithOTP() {
        // Open login page
        driver.get("https://my.haleon-rewards.d-rive.net/login");

        // Locate and enter phone number
        WebElement phoneField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-form_phone"))); // Update ID
        phoneField.sendKeys("137336651");

        // Click on "Send OTP"
        // If have ID
        //WebElement sendOtpButton = driver.findElement(By.id("send-otp")); // Update ID
        //sendOtpButton.click();    

        // Does not have id
        WebElement LoginButton = driver.findElement(By.xpath("//button[span[text()='Send OTP Code']]"));
        LoginButton.click();
        
        // *** MANUAL OTP ENTRY ***  
        System.out.println("Please enter OTP manually... waiting 60 seconds");
        try {
            Thread.sleep(30000); // Allow user to enter OTP manually
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click Login Button
        //WebElement loginButton = driver.findElement(By.id("login-button")); // Update ID
        //loginButton.click();

        // Does not have id
        //WebElement verifyButton = driver.findElement(By.xpath("//button[span[text()='Verify']]"));
        //verifyButton.click();

        // locate by class
        //WebElement verifyButton = driver.findElement(By.cssSelector(".verify-btn button"));
        //verifyButton.click();
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        //WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".verify-btn button")));
        //verifyButton.click();

        System.out.println("OTP detected! Clicking Verify button...");

        // Wait for the Verify button to become clickable
        WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Verify']]")));

        // Click the Verify button
        verifyButton.click();

        System.out.println("Verify button clicked automatically!");
        
        // Popup done
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        // Wait until the modal is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ant-modal")));
        
        // Wait for the "Done" button inside the modal
        WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Done']]")));
        
        // Click the "Done" button
        doneButton.click();

        // Wait for login confirmation (Check if Dashboard or Home appears)
        boolean loginSuccess = wait.until(ExpectedConditions.urlContains("home")); // Modify as needed
        assertTrue(loginSuccess, "Login failed!");

        System.out.println("Login successful!");
    }
    
    @Test
    public void testUnsuccessfulLoginWithIncorrectOTP() {
        // Open login page
        driver.get("https://my.haleon-rewards.d-rive.net/login");

        // Enter valid phone number
        WebElement phoneField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-form_phone")));
        phoneField.sendKeys("137336651"); // valid phone number

        // Click "Send OTP"
        WebElement loginButton = driver.findElement(By.xpath("//button[span[text()='Send OTP Code']]"));
        loginButton.click();
        // Enter an incorrect OTP
        // Wait until all 4 OTP inputs are present
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("input[aria-label*='Digit']"), 3));

        // Get all 4 OTP input fields
        List<WebElement> otpFields = driver.findElements(By.cssSelector("input[aria-label*='Digit']"));

        // Enter OTP digits one by one (for incorrect OTP, e.g., "1234")
        String otp = "1234";
        for (int i = 0; i < otp.length(); i++) {
            WebElement field = otpFields.get(i);
            wait.until(ExpectedConditions.elementToBeClickable(field));
            field.sendKeys(Character.toString(otp.charAt(i)));
        }

        // Click Verify
        WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[span[text()='Verify']]")
        ));
        verifyButton.click();

        // Wait for error message
        boolean isErrorShown = false;
        try {
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Incorrect') or contains(text(), 'invalid') or contains(text(), 'wrong')]")
            ));
            isErrorShown = errorMsg.isDisplayed();
        } catch (Exception e) {
            isErrorShown = false;
        }

        // Ensure we don't navigate to the home/dashboard page
        boolean urlChanged = driver.getCurrentUrl().contains("home");

        assertTrue(isErrorShown || !urlChanged, "OTP verification incorrectly succeeded!");

        System.out.println("Unsuccessful login with wrong OTP handled correctly.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
