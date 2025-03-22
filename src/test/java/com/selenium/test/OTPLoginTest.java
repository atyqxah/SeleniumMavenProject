package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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

        // Wait for OTP input field (Assuming it's displayed after clicking "Send OTP")
        //WebElement otpField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otp"))); // Update ID
        
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
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

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
