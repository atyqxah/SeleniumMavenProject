package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
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
        driver.get("https://my.haleon-rewards.d-rive.net/login");

        WebElement phoneField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-form_phone")));
        phoneField.sendKeys("137336651");

        WebElement loginButton = driver.findElement(By.xpath("//button[span[text()='Send OTP Code']]"));
        loginButton.click();

        System.out.println("Please enter OTP manually... waiting 30 seconds");
        Thread.sleep(30000);

        System.out.println("OTP detected! Clicking Verify button...");
        WebElement verifyButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[span[text()='Verify']]")
        ));
        verifyButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ant-modal")));
        WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[span[text()='Done']]")
        ));
        doneButton.click();

        boolean loginSuccess = wait.until(ExpectedConditions.urlContains("home"));
        assertTrue(loginSuccess, "Login failed!");

        WebElement profileNavButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".text-center.mt-1.mr-1")
        ));
        profileNavButton.click();

        WebElement personalDetailsButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@class='redirection-box' and .//div[text()='Personal Details']]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", personalDetailsButton);
        Thread.sleep(500);
        personalDetailsButton.click();

        // Update Name, Email, and DOB
        updateInputField("register_name", "Atika");
        updateInputField("register_email", "atika@mail.com");
        updateDobField("register_dob", "2003-05");

        // Save changes: Actions -> JS -> fallback
        WebElement saveButton = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("button.save-btn")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));

        System.out.println("Save button enabled? " + saveButton.isEnabled());
        System.out.println("Save button classes: " + saveButton.getAttribute("class"));

        boolean clicked = false;
        Actions actions = new Actions(driver);
        try {
            actions.moveToElement(saveButton).click().perform();
            clicked = true;
            System.out.println("Clicked Save via Actions.");
        } catch (Exception e1) {
            System.out.println("Actions click failed: " + e1.getMessage());
        }

        if (!clicked) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
                clicked = true;
                System.out.println("Clicked Save via JS.");
            } catch (Exception e2) {
                System.out.println("JS click failed: " + e2.getMessage());
            }
        }

        if (!clicked) {
            WebElement form = driver.findElement(By.id("register"));
            form.submit();
            clicked = true;
            System.out.println("Form submitted via fallback.");
        }

        if (!clicked) {
            throw new RuntimeException("Unable to click Save button by any means");
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("receipt-success-button")));
        WebElement doneUpdateButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[span[text()='Done']]")
        ));
        doneUpdateButton.click();

        System.out.println("✅ Profile update test passed.");
    }

    private void updateInputField(String fieldId, String newValue) throws InterruptedException {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(fieldId)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", input);
        input.click();
        Thread.sleep(200);
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        Thread.sleep(300);

        for (char c : newValue.toCharArray()) {
            input.sendKeys(Character.toString(c));
            Thread.sleep(50);
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].blur();", input);
        System.out.println("Updated " + fieldId + " to: " + newValue);
    }

    private void updateDobField(String fieldId, String newValue) throws InterruptedException {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(fieldId)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        Thread.sleep(300);

        for (char c : newValue.toCharArray()) {
            input.sendKeys(Character.toString(c));
            Thread.sleep(50);
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].blur();", input);
        System.out.println("Updated " + fieldId + " to: " + newValue);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
