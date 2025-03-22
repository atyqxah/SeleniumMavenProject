package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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
        
        // wait for button to clickable
        WebElement resendOtpButton = driver.findElement(By.xpath("//button[contains(., 'Resend Code')]"));
        while (!resendOtpButton.isEnabled()) {
        Thread.sleep(1000); } // Wait until the button is enabled
        resendOtpButton.click();

        // Capture success message after clicking Resend OTP
        WebElement successMessage = driver.findElement(By.xpath("//p[contains(text(), 'new otp code sent successfully')]"));
        assertTrue(successMessage.isDisplayed(), "Resend OTP failed!");
    }
}
