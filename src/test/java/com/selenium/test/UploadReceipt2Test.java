package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
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

public class UploadReceipt2Test {
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
    public void testUploadReceipt2() {
        // Open login page
        driver.get("https://my.haleon-rewards.d-rive.net/login");

        // Locate and enter phone number
        WebElement phoneField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-form_phone"))); // Update ID
        phoneField.sendKeys("137336651");

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


        //WebElement browseButton = wait.until(ExpectedConditions.elementToBeClickable(
            //By.cssSelector("div.box-container-left > p.header-description")
        //));
        //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", browseButton);
        //((JavascriptExecutor) driver).executeScript("arguments[0].click();", browseButton);
        
        // Click "Upload Receipt" button in the bottom navigation bar
        //WebElement uploadReceiptNavButton = wait.until(ExpectedConditions.presenceOfElementLocated(
            //By.xpath("//nav//*[contains(text(), 'Upload Receipt')]")
        //));
        //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", uploadReceiptNavButton);
        //((JavascriptExecutor) driver).executeScript("arguments[0].click();", uploadReceiptNavButton);

        WebElement uploadReceiptNavButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("img.img-upload-receipt")
        ));
        uploadReceiptNavButton.click();
        
        // Click on the upload section
        WebElement uploadSection = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//p[contains(text(), 'Upload your receipt')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", uploadSection);
        
        // Prepare and verify the file to upload
        String filePath = System.getProperty("user.home") + "/Pictures/Screenshots/test.png";
        File file = new File(filePath);
        assertTrue(file.exists(), "Test file not found!");
        
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("input[type='file']")
        ));
        fileInput.sendKeys(file.getAbsolutePath());
        
        // Click the submit button
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@class, 'submit-receipt-btn')]")
        ));
        submitButton.click();
        
        WebElement doneUploadButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.receipt-success-button")
        ));
        doneUploadButton.click();
        
        System.out.println("Test Passed: Receipt uploaded successfully.");

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

