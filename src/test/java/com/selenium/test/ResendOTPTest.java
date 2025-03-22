package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ResendOTPTest {
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
    public void testLoginWithOTP() throws InterruptedException {
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
        
        // ✅ Wait for "Resend OTP" button to be clickable

        //WebElement resendOtpButton = wait.until(
            //ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Resend Code')]"))
            //);
        //resendOtpButton.click();

        WebElement resendOtpButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//p[contains(text(), 'Resend Code')]")
        ));
        resendOtpButton.click();

        //JavascriptExecutor js = (JavascriptExecutor) driver;
        //WebElement resendOtpButton = driver.findElement(By.xpath("//p[contains(text(), 'Resend Code')]"));
        //js.executeScript("arguments[0].click();", resendOtpButton);

        System.out.println("✅ Resend Code clicked successfully!");
        
        // ✅ Wait for success message
        //WebElement successMessage = wait.until(
            //ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'new otp code sent successfully')]"))
            //);
            
        // Validate success message
        //assertTrue(successMessage.isDisplayed(), "Resend OTP failed!");
    }
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
