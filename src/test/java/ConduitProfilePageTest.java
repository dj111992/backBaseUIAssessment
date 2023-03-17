import com.assessment.ui.actions.BaseActions;
import com.assessment.ui.actions.RetryStrategy;
import com.assessment.ui.actions.Waits;
import com.assessment.ui.config.EnvFactory;
import com.assessment.ui.factories.DriverFactory;
import com.typesafe.config.Config;
import net.datafaker.Faker;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.logging.Logger;

import static com.assessment.ui.pages.ConduitHomePage.articlesPreview;
import static com.assessment.ui.pages.ConduitHomePage.signInButton;
import static com.assessment.ui.pages.ConduitHomePage.userProfileButton;
import static com.assessment.ui.pages.ConduitProfilePage.profileSettingsButton;
import static com.assessment.ui.pages.ConduitProfilePage.userBio;
import static com.assessment.ui.pages.ConduitProfilePage.userName;
import static com.assessment.ui.pages.ConduitProfileSettingsPage.bioProfileSettings;
import static com.assessment.ui.pages.ConduitProfileSettingsPage.passwordProfileSettings;
import static com.assessment.ui.pages.ConduitProfileSettingsPage.profileSettingsPageTitle;
import static com.assessment.ui.pages.ConduitSignInPage.email;
import static com.assessment.ui.pages.ConduitSignInPage.password;
import static com.assessment.ui.pages.ConduitSignInPage.submitButton;

public class ConduitProfilePageTest {
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final String HOME_PAGE_URL = "https://" + "candidatex" + ":" + "qa-is-cool" + "@" + config.getString("HOME_PAGE_URL");
    private WebDriver driver;
    Logger logger = Logger.getLogger("Test Conduit Profile Page");
    private static final Faker faker = new Faker();

    @BeforeSuite(alwaysRun = true)
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
            BaseActions baseActions = new BaseActions();

            waits.waitforElementClickable(driver, signInButton);
            baseActions.clickButton(driver, signInButton);

            baseActions.sendKeys(driver, email, "dj111992@gmail.com");
            baseActions.sendKeys(driver, password, "Deepak@6");

            waits.waitforElementClickable(driver, submitButton);
            baseActions.clickButton(driver, submitButton);
        }
    }

    @Tag("regression")
    @Test(description = "Checking for user info on profile section")
    void assertForUserInfoUnderProfileSection() {
        BaseActions baseActions = new BaseActions();
        Waits waits = new Waits();
        SoftAssert softAssert = new SoftAssert();

        //Checking user profile info
        waits.waitforElementClickable(driver, userProfileButton);
        baseActions.clickButton(driver, userProfileButton);

        softAssert.assertEquals(driver.findElement(userName).getText(), "deepak",
                "User title is not correct");

        softAssert.assertTrue(driver.findElements(articlesPreview).size() > 6,
                "Articles list is not available with size: " + driver.findElements(articlesPreview).size());
        logger.info("Number of articles on page are: " + driver.findElements(articlesPreview).size());

        softAssert.assertAll();
    }

    @Tag("regression")
    @Test(description = "Checking for user profile settings screen")
    void assertForUserProfileSettingsSection() {
        BaseActions baseActions = new BaseActions();
        Waits waits = new Waits();
        SoftAssert softAssert = new SoftAssert();

        waits.waitforElementClickable(driver, userProfileButton);
        baseActions.clickButton(driver, userProfileButton);

        //Checking user profile settings
        waits.waitforElementClickable(driver, profileSettingsButton);
        baseActions.clickButton(driver, profileSettingsButton);

        softAssert.assertEquals(driver.findElement(profileSettingsPageTitle).getText(), "Your Settings",
                "Page title is not correct");

        //softAssert.assertEquals(driver.findElement(userNameProfileSettings).getText(), "deepak",
        //        "User title is not correct");

        //Checking change of profile settings
        String userBioText = faker.text().text(10);

        driver.findElement(bioProfileSettings).clear();
        baseActions.sendKeys(driver, bioProfileSettings, userBioText);

        baseActions.sendKeys(driver, passwordProfileSettings, "Deepak@6");

        waits.waitforElementClickable(driver, submitButton);
        baseActions.clickButton(driver, submitButton);

        softAssert.assertEquals(driver.findElement(userBio).getText(), userBioText,
                "User Bio is not correct and not saved");
        logger.info("User bio is correct and is: " + driver.findElement(userBio).getText());
        softAssert.assertAll();
    }

    @AfterSuite(alwaysRun = true)
    public void postCondition() {
        driver.quit();
        driver = null;
    }
}
