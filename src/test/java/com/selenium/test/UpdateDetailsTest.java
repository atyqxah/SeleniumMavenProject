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

public class UpdateDetailsTest {

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
    public void testUpdateProfile() throws InterruptedException {
        // Open login page
        driver.get("https://my.haleon-rewards.d-rive.net/login");
    
        // Locate and enter phone number
        WebElement phoneField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-form_phone")));
        phoneField.sendKeys("137336651");
    
        // Click "Send OTP Code" button
        WebElement loginButton = driver.findElement(By.xpath("//button[span[text()='Send OTP Code']]"));
        loginButton.click();
    
        // Wait for manual OTP entry
        System.out.println("Please enter OTP manually... waiting 30 seconds");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        System.out.println("OTP detected! Clicking Verify button...");
    
        // Click Verify button
        WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Verify']]")));
        verifyButton.click();
    
        System.out.println("Verify button clicked automatically!");
    
        // Wait for and click "Done" button on modal
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ant-modal")));
        WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Done']]")));
        doneButton.click();
    
        // Check successful login
        boolean loginSuccess = wait.until(ExpectedConditions.urlContains("home"));
        assertTrue(loginSuccess, "Login failed!");
    
        System.out.println("Login successful!");
    
        // ✅ Fixed selector: no spaces, all dots
        WebElement profileNavButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".text-center.mt-1.mr-1")
        ));
        profileNavButton.click();
    
        // Navigate to Personal Details
        WebElement personalDetailsButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@class='redirection-box' and .//div[text()='Personal Details']]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", personalDetailsButton);
        Thread.sleep(500);
        personalDetailsButton.click();
    
        // --- Check existing values and update if necessary ---
        // Check and update Name
        WebElement nameInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("register_name")));
        String currentName = nameInput.getAttribute("value");
        System.out.println("Current Name: " + currentName);
        
        if (!"Junior".equals(currentName)) {
            // Focus the field
            nameInput.click();
        
            // Use JS to fully clear the value
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '';" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                nameInput
            );
        
            // Send new value
            nameInput.sendKeys("Junior");
        
            // Dispatch input/change events again to ensure frontend reacts
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                nameInput
            );
        
            // Blur the field to finalize
            nameInput.sendKeys("\u0009");
        
            System.out.println("Name updated to: Junior");
        }
    
        // Check and update Email

        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("register_email")));
        String currentEmail = emailInput.getAttribute("value");
        System.out.println("Current Name: " + currentEmail);
        
        if (!"junior@mail.com".equals(currentEmail)) {
            // Focus the field
            emailInput.click();
        
            // Use JS to fully clear the value
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '';" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                emailInput
            );
        
            // Send new value
            emailInput.sendKeys("junior@mail.com");
        
            // Dispatch input/change events again to ensure frontend reacts
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                emailInput
            );
        
            // Blur the field to finalize
            emailInput.sendKeys("\u0009");
        
            System.out.println("Email updated to: junior@mail.com");
        }
    
        // Check and update DOB
        //WebElement dobInput = driver.findElement(By.id("register_dob"));
        //String currentDob = dobInput.getAttribute("value");
        //if (!"2000-05".equals(currentDob)) {
            //dobInput.clear();
            //dobInput.sendKeys("2000-05");
        //}
    
        // Click Save button to submit the form
        WebElement saveButton = driver.findElement(By.cssSelector("button.save-btn"));
        saveButton.click();
    
        // Wait for confirmation modal and click "Done"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("receipt-success-button")));
        WebElement doneUpdateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Done']]")));
        doneUpdateButton.click();
    
        System.out.println("✅ Profile update test passed.");
    }
    
    
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
