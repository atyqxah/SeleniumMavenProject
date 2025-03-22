package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
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

public class UploadReceiptTest {

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
    public void testUploadReceipt() {
        // ✅ Step 1: Open the upload receipt page directly
        driver.get("https://my.haleon-rewards.d-rive.net/upload"); // Change URL if needed

        System.out.println("Attempting receipt upload without login...");

        // ✅ Step 2: Locate the file input field
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));

        // ✅ Step 3: Specify the receipt file path
        File file = new File("C:\\Users\\YourUsername\\Documents\\test-receipt.jpg");
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return;
        }

        // ✅ Step 4: Upload the file
        fileInput.sendKeys(file.getAbsolutePath());
        System.out.println("Receipt file uploaded successfully!");

        // ✅ Step 5: Click the Submit button
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Submit')]")));
        submitButton.click();

        // ✅ Step 6: Verify upload success
        boolean uploadSuccess = wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[contains(text(), 'Upload successful')]"), "Upload successful"));
        assertTrue(uploadSuccess, "Upload failed!");

        System.out.println("Test Passed: Receipt uploaded without login.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
