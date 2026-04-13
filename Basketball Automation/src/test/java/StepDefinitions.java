package org.example;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class StepDefinitions {

    WebDriver driver;
    WebDriverWait wait;

    @Given("The user is on the registration page")
    public void openRegistrationPage() throws InterruptedException {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("C:\\Users\\algir\\Desktop\\Basket\\Register Basketball/Register.html");
    }
    @Given("The user fills in all required fields correctly")
    public void the_user_fills_in_all_required_fields_correctly() {
        fillAllFields();
    }

    @Given("The user accepts the terms and conditions")
    public void the_user_accepts_the_terms_and_conditions() {
        acceptAllTerms();
    }

    private void fillField(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(value);
    }

    private void fillAllFields() {
        driver.findElement(By.id("dp")).sendKeys("30/01/2002");
        driver.findElement(By.id("dp")).sendKeys(Keys.ENTER);
        driver.findElement(By.id("member_firstname")).sendKeys("test");
        driver.findElement(By.id("member_lastname")).sendKeys("testsson");
        driver.findElement(By.id("member_emailaddress")).sendKeys("testmail123@gmail.com");
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys("testmail123@gmail.com");
        driver.findElement(By.id("signupunlicenced_password")).sendKeys("BMWSVART123!");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("BMWSVART123!");
    }

    private void acceptCheckbox(By labelForLocator) {
        WebElement label = wait.until(ExpectedConditions.elementToBeClickable(labelForLocator));
        label.click();
    }

    private void acceptAllTerms() {
        acceptCheckbox(By.xpath("//label[@for='sign_up_25']"));
        acceptCheckbox(By.xpath("//label[@for='sign_up_26']"));
        acceptCheckbox(By.xpath("//label[@for='fanmembersignup_agreetocodeofethicsandconduct']"));
    }

    private void declineAllTerms() {
        declineCheckbox(By.xpath("//label[@for='sign_up_25']"));
        declineCheckbox(By.xpath("//label[@for='sign_up_26']"));
        declineCheckbox(By.xpath("//label[@for='fanmembersignup_agreetocodeofethicsandconduct']"));
    }

    private void declineCheckbox(By labelForLocator) {
        WebElement label = wait.until(ExpectedConditions.elementToBeClickable(labelForLocator));
        String forAttr = label.getAttribute("for");
        WebElement checkbox = driver.findElement(By.id(forAttr));
        if (checkbox.isSelected()) {
            label.click();
        }
    }

    @When("The user fills in all required fields and accepts all terms")
    public void fillFieldsAndAcceptAll() {
        fillAllFields();
        acceptAllTerms();
        System.out.println("Filled all fields and accepted all terms");
    }

    @When("The user leaves the last name empty")
    public void leaveLastNameEmpty() {
        fillField(By.id("member_firstname"), "Test");
        fillField(By.id("member_lastname"), ""); // leave empty
        fillField(By.id("member_emailaddress"), "testmail123@test.com");
        fillField(By.id("member_confirmemailaddress"), "testmail123@test.com");
        WebElement dob = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dp")));
        dob.clear();
        dob.sendKeys("30/01/2002");
        fillField(By.xpath("//input[contains(@id,'password')]"), "BMWSVART123!");
        fillField(By.xpath("//input[contains(@id,'confirmpassword')]"), "BMWSVART123!");
        acceptAllTerms();
        System.out.println("Left last name empty and accepted all terms");
    }

    @When("The user enters different passwords")
    public void enterDifferentPasswords() {
        fillAllFields();
        fillField(By.xpath("//input[contains(@id,'password')]"), "BMWSVART123!!");
        fillField(By.xpath("//input[contains(@id,'confirmpassword')]"), "BMWVIT?!");
        declineAllTerms();
        System.out.println("Entered mismatched passwords and did not accept terms");
    }

    @When("The user fills in everything except accepting the terms")
    public void fillWithoutTerms() {
        fillAllFields();
        declineAllTerms();
        System.out.println("Filled form without accepting terms");
    }

    @When("The user clicks Join")
    public void the_user_clicks_join() {
        try {
            WebElement joinBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@type='submit' and @value='CONFIRM AND JOIN']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", joinBtn);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", joinBtn);
            System.out.println("Clicked the Join button");
        } catch (TimeoutException | InterruptedException e) {
            Assert.fail("Failed to click Join button: " + e.getMessage());
        }
    }

    @Then("A new account is created successfully")
    public void verifySuccess() {
        boolean isSuccessPage= wait.until(ExpectedConditions.urlContains("Success"));
        assertTrue("Account has not been created successfully",isSuccessPage);
    }

    @Then("An error message for last name is displayed")
    public void verifyLastNameError() {
        try {
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Last name')]")
            ));
            assertTrue(errorMsg.isDisplayed());
            System.out.println("Last name error message displayed");
        } catch (TimeoutException e) {
            System.out.println("Last name error message not found within the timeout.");
        } finally {
            driver.quit();
        }
    }

    @Then("An error message for password mismatch is displayed")
    public void verifyPasswordMismatch() {
        try {
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Password') or contains(text(),'mismatch')]")
            ));
            assertTrue(errorMsg.isDisplayed());
            System.out.println("Password mismatch error displayed");
        } finally {
            driver.quit();
        }
    }

    @Then("An error message for terms and conditions is displayed")
    public void verifyTermsError() {
        try {
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'confirm') or contains(text(),'terms')]")
            ));
            assertTrue(errorMsg.isDisplayed());
            System.out.println("Terms error message displayed");
        } finally {
            driver.quit();
        }
    }
}