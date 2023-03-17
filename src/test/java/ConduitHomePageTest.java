import com.assessment.ui.actions.RetryStrategy;
import com.assessment.ui.actions.Waits;
import com.assessment.ui.config.EnvFactory;
import com.assessment.ui.factories.DriverFactory;
import com.typesafe.config.Config;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.logging.Logger;

import static com.assessment.ui.pages.ConduitHomePage.articlesPreview;
import static com.assessment.ui.pages.ConduitHomePage.paginationItems;
import static com.assessment.ui.pages.ConduitHomePage.tagElements;
import static com.assessment.ui.pages.ConduitHomePage.tagTabs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConduitHomePageTest {
    SingletonClass sc1 = SingletonClass.getInstanceOfSingletonClass();
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final String HOME_PAGE_URL = "https://" + "candidatex" + ":" + "qa-is-cool" + "@" + config.getString("HOME_PAGE_URL");
    private WebDriver driver;
    Logger logger = Logger.getLogger("Test Conduit Home Page");

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
    @Test(description = "Checking if number of list tags is greater than 100")
    void assertThatListTagsGreaterThan100() {
        assertTrue(driver.findElements(tagElements).size() == 100, "Tag list items are less than 100");
        logger.info("Number of tag elements is greater than 100 and equals: " + driver.findElements(tagElements).size());
    }

    @Tag("regression")
    @Test(description = "Checking if number of articles equals 10 in global Feed")
    void assertThatNumberOfArticlesEquals10GlobalFeed() {
        assertEquals(10, driver.findElements(articlesPreview).size(),
                "Articles count is not 10 in global feed");
        logger.info("Number of articles on global feed equals 10 and are: " + driver.findElements(articlesPreview).size());
    }

    @Tag("regression")
    @Test(description = "Checking number of articles on clicking specific tag and tag tab is opened")
    void assertThatNumberOfArticlesInSpecificTag() {
        SoftAssert softAssert = new SoftAssert();
        driver.findElement(tagElements).click();

        WebElement selectElement = driver.findElements(tagTabs).stream()
                .filter(e -> e.getAttribute("class").contains("active")).findFirst().get();
        softAssert.assertEquals(selectElement.getText(), "matrix",
                "Current tab is the selected one");
        logger.info("Current selected tab is correct and is: " + selectElement.getText());

        softAssert.assertEquals(10, driver.findElements(articlesPreview).size(),
                "Articles count is not 10 in specific tag");
        softAssert.assertAll();
        logger.info("Number of articles on particular tag are 10 and are: " + driver.findElements(articlesPreview).size());
    }

    @Tag("regression")
    @Test(description = "Check for pagination and user should be able to click on pages")
    void assertPaginationHomePage() {
        SoftAssert softAssert = new SoftAssert();

        WebElement selectElement = driver.findElements(paginationItems).stream()
                .filter(e -> e.getText().contains("3")).findFirst().get();
        selectElement.click();

        softAssert.assertTrue(selectElement.findElement(By.xpath("ancestor::li")).getAttribute("class").contains("active"),
                "Correct page in pagination menu is selected");
        logger.info("Current pagination is correct and is: " + selectElement.getText());

        softAssert.assertEquals(10, driver.findElements(articlesPreview).size(),
                "Articles count is not 10 in specific tag");
        softAssert.assertAll();
        logger.info("Number of articles on particular tag are 10 and are: " + driver.findElements(articlesPreview).size());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quit();
        driver = null;
    }

}
