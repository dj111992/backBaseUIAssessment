import com.assessment.ui.actions.BaseActions;
import com.assessment.ui.actions.RetryStrategy;
import com.assessment.ui.actions.Waits;
import com.assessment.ui.config.EnvFactory;
import com.assessment.ui.factories.DriverFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.typesafe.config.Config;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.logging.Logger;

import static com.assessment.ui.pages.ConduitHomePage.articlesPreview;
import static com.assessment.ui.pages.ConduitHomePage.settingsButton;
import static com.assessment.ui.pages.ConduitHomePage.signInButton;
import static com.assessment.ui.pages.ConduitSettingsPage.settingsPageTitle;
import static com.assessment.ui.pages.ConduitSignInPage.email;
import static com.assessment.ui.pages.ConduitSignInPage.password;
import static com.assessment.ui.pages.ConduitSignInPage.submitButton;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConduitSettingsTest {
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final String HOME_PAGE_URL = "https://" + "candidatex" + ":" + "qa-is-cool" + "@" + config.getString("HOME_PAGE_URL");
    private static WebDriver driver;
    Logger logger = Logger.getLogger("Test Conduit Settings Page");

    @BeforeMethod(alwaysRun = true)
    public void preCondition() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }

        driver.get(HOME_PAGE_URL);

        RetryStrategy retryStrategy = new RetryStrategy();
        Waits waits = new Waits();

        while (retryStrategy.shouldRetry()) {
            waits.waitForSometime(driver, 5000);

            try {
                if (waits.waitForElementIsDisplayed(driver, articlesPreview)) {
                    retryStrategy.noErrorOccurred();
                    logger.info("Particular element is visible on the screen for clicking");
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                try {
                    retryStrategy.errorOccured();
                    driver.navigate().refresh();
                    logger.info("Error occurred and retrying the element locating for button click");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Tag("regression")
    @Test(description = "Checking if Settings page is accessible and loaded fine")
    void assertThatSettingsPageIsAccessible() {
        BaseActions baseActions = new BaseActions();
        Waits waits = new Waits();

        waits.waitforElementClickable(driver, signInButton);
        baseActions.clickButton(driver, signInButton);

        baseActions.sendKeys(driver, email, "dj111992@gmail.com");
        baseActions.sendKeys(driver, password, "Deepak@6");

        waits.waitforElementClickable(driver, submitButton);
        baseActions.clickButton(driver, submitButton);

        waits.waitforElementClickable(driver, settingsButton);
        baseActions.clickButton(driver, settingsButton);

        assertEquals(driver.findElement(settingsPageTitle).getText(), "Your Settings", "Settings title page is not correct");
        logger.info("Settings page is loaded and title is: " + driver.findElement(settingsPageTitle).getText());
    }

    @AfterMethod(alwaysRun = true)
    public void postCondition() {
        driver.quit();
        driver = null;
    }

}
