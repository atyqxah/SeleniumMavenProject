package com.selenium.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Scanner;

import org.json.JSONObject;
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

public class UploadReceiptTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://my.haleon-rewards.d-rive.net";
    private static final String API_URL = "https://core-sb.d-rive.net/sleekflow/auth/login";
    //private static final String API_URL = "https://core-sb.d-rive.net/auth/verify-token";

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        setAuthLocalStorage();
    }

    @Test
    public void testUploadReceipt() {
        driver.get(BASE_URL + "/home");
        
        // Wait until page is fully loaded
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
            .executeScript("return document.readyState").equals("complete"));
        
        // Locate and click the "Browse All Rewards" button using a CSS selector
        WebElement browseButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("div.box-container-left > p.header-description")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", browseButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", browseButton);
        
        // Click "Upload Receipt" button in the bottom navigation bar
        WebElement uploadReceiptNavButton = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//nav//*[contains(text(), 'Upload Receipt')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", uploadReceiptNavButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", uploadReceiptNavButton);
        
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
        
        // Verify that the upload was successful
        boolean uploadSuccess = wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.xpath("//*[contains(text(), 'Upload successful')]"), "Upload successful"));
        assertTrue(uploadSuccess, "Upload failed!");
        
        System.out.println("Test Passed: Receipt uploaded successfully.");
    }

    private void setAuthLocalStorage() {
        String token = getAuthToken();
        if (token != null) {
            driver.get(BASE_URL);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.localStorage.setItem('authToken', arguments[0]);", token);
            driver.navigate().refresh();
            System.out.println("Token set successfully in Local Storage.");
        } else {
            System.out.println("Failed to authenticate.");
        }
    }

    private String getAuthToken() {
        try {
            URI uri = new URI(API_URL);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("x-company-id", "395e0359-89a1-46d4-a7e5-3c0ee40b567f");
            conn.setRequestProperty("x-secret", "bD6scemb1YmxHpkbCsVNHlxGcXCemu92");

            JSONObject requestBody = new JSONObject();
            requestBody.put("token", "ZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SjFjMlZ5U1dRaU9pSTVOREZrT0RSaE5pMWxPRE5tTFRRNVpEZ3RZVEpqT0MwMVpHTXlZMk15TVdOalltUWlMQ0pqYjIxVmMyVnlTV1FpT2lKak9UVmlPV05sTlMwNU1ETmlMVFF6TkRVdE9EVmtZeTB3TURRek9UWTBPREF4WVdFaUxDSmpiMjF3WVc1NVNXUWlPaUl6T1RWbE1ETTFPUzA0T1dFeExUUTJaRFF0WVRkbE5TMHpZekJsWlRRd1lqVTJOMllpTENKeWIyeGxJam9pUTA5T1UxVk5SVklpTENKd2NtOW1hV3hsU1c1bWJ5STZiblZzYkN3aWNtVjBZV2xzWlhKSlpITWlPbHRkTENKd2NtOXRiM1JsY2tsa2N5STZXMTBzSW1SemNrbGtJanB1ZFd4c0xDSnpkR0YwZFhNaU9pSkJRMVJKVmtVaUxDSnBZWFFpT2pFM05ESTVPVEV6TlRCOS52dkVXUWVjM1BKZDRCVlk4eDkxY1BIdm5wcDZmckJxMnY5NHhxUVhZS1NN");
            requestBody.put("decoded", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI5NDFkODRhNi1lODNmLTQ5ZDgtYTJjOC01ZGMyY2MyMWNjYmQiLCJjb21Vc2VySWQiOiJjOTViOWNlNS05MDNiLTQzNDUtODVkYy0wMDQzOTY0ODAxYWEiLCJjb21wYW55SWQiOiIzOTVlMDM1OS04OWExLTQ2ZDQtYTdlNS0zYzBlZTQwYjU2N2YiLCJyb2xlIjoiQ09OU1VNRVIiLCJwcm9maWxlSW5mbyI6bnVsbCwicmV0YWlsZXJJZHMiOltdLCJwcm9tb3RlcklkcyI6W10sImRzcklkIjpudWxsLCJzdGF0dXMiOiJBQ1RJVkUiLCJpYXQiOjE3NDI5OTEzNTB9.vvEWQec3PJd4BVY8x91cPHvnpp6frBq2v94xqQXYKSM");
            requestBody.put("phone", "60137336651");
            requestBody.put("name", "Atika");
            //requestBody.put("success", true);
            //requestBody.put("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI5NDFkODRhNi1lODNmLTQ5ZDgtYTJjOC01ZGMyY2MyMWNjYmQiLCJjb21Vc2VySWQiOiJjOTViOWNlNS05MDNiLTQzNDUtODVkYy0wMDQzOTY0ODAxYWEiLCJjb21wYW55SWQiOiIzOTVlMDM1OS04OWExLTQ2ZDQtYTdlNS0zYzBlZTQwYjU2N2YiLCJyb2xlIjoiQ09OU1VNRVIiLCJwcm9maWxlSW5mbyI6bnVsbCwicmV0YWlsZXJJZHMiOltdLCJwcm9tb3RlcklkcyI6W10sImRzcklkIjpudWxsLCJzdGF0dXMiOiJBQ1RJVkUiLCJpYXQiOjE3NDI5OTUxMjV9.8uSg1Ml2tfdFoFG9lDVsoMRKg6BiNaOojOpLf2Yh-Rk");

            String jsonString = requestBody.toString();
            System.out.println("➡ Sending JSON: " + jsonString);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonString.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("🔄 Response Code: " + responseCode);

            InputStream stream = (responseCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
            try (Scanner scanner = new Scanner(stream).useDelimiter("\\A")) {
                String response = scanner.hasNext() ? scanner.next() : "";
                System.out.println("📩 Full API Response: " + response);

                if (responseCode >= 400) {
                    System.out.println("❌ API Error - Authentication failed.");
                    return null;
                }

                JSONObject jsonResponse = new JSONObject(response);
                String token = jsonResponse.optString("token", null);
                if (token != null && !token.isEmpty()) {
                    System.out.println("✅ Authentication Successful - Token Received.");
                    return token;
                } else {
                    System.out.println("❌ Authentication Failed - No valid token received.");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

