import com.assessment.ui.actions.BaseActions;
import com.assessment.ui.actions.RetryStrategy;
import com.assessment.ui.actions.Waits;
import com.assessment.ui.config.EnvFactory;
import com.assessment.ui.factories.DriverFactory;
import com.typesafe.config.Config;
import net.datafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.logging.Logger;

import static com.assessment.ui.pages.ConduitHomePage.articlesPreview;
import static com.assessment.ui.pages.ConduitHomePage.editorButton;
import static com.assessment.ui.pages.ConduitHomePage.homeButton;
import static com.assessment.ui.pages.ConduitHomePage.signInButton;
import static com.assessment.ui.pages.ConduitHomePage.tagElements;
import static com.assessment.ui.pages.ConduitSignInPage.email;
import static com.assessment.ui.pages.ConduitSignInPage.password;
import static com.assessment.ui.pages.ConduitSignInPage.submitButton;
import static com.assessment.ui.pages.EditorPage.articleBody;
import static com.assessment.ui.pages.EditorPage.articleDescription;
import static com.assessment.ui.pages.EditorPage.articleTitle;
import static com.assessment.ui.pages.EditorPage.commentDescription;
import static com.assessment.ui.pages.EditorPage.commentSection;
import static com.assessment.ui.pages.EditorPage.deleteArticleButton;
import static com.assessment.ui.pages.EditorPage.deleteCommentButton;
import static com.assessment.ui.pages.EditorPage.editArticleButton;
import static com.assessment.ui.pages.EditorPage.postCommentButton;
import static com.assessment.ui.pages.EditorPage.publishArticleButton;
import static com.assessment.ui.pages.EditorPage.savedArticleBody;
import static com.assessment.ui.pages.EditorPage.savedComment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ConduitEditorPageTest {

    SingletonClass sc1 = SingletonClass.getInstanceOfSingletonClass();
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final String HOME_PAGE_URL = "https://" + "candidatex" + ":" + "qa-is-cool" + "@" + config.getString("HOME_PAGE_URL");
    private WebDriver driver;
    Logger logger = Logger.getLogger("Test Conduit Editor Page");
    private static final Faker faker = new Faker();

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
            BaseActions baseActions = new BaseActions();

            waits.waitforElementClickable(driver, signInButton);
            baseActions.clickButton(driver, signInButton);

            baseActions.sendKeys(driver, email, "dj111992@gmail.com");
            baseActions.sendKeys(driver, password, "Deepak@6");

            waits.waitforElementClickable(driver, submitButton);
            baseActions.clickButton(driver, submitButton);
        }
    }

    @AfterMethod()
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test(description = "Checking for user adding a new article")
    void assertForAddingNewArticle() {
        BaseActions baseActions = new BaseActions();
        Waits waits = new Waits();

        baseActions.clickButton(driver, homeButton);
        //Adding new Article
        waits.waitforElementClickable(driver, editorButton);
        baseActions.clickButton(driver, editorButton);

        String title = faker.text().text(10);
        String body = faker.text().text(15);

        baseActions.sendKeys(driver, articleTitle, title);
        baseActions.sendKeys(driver, articleDescription, faker.text().text(10));
        baseActions.sendKeys(driver, articleBody, body);
        waits.waitforElementClickable(driver, publishArticleButton);
        baseActions.clickButton(driver, publishArticleButton);

        assertEquals(driver.findElement(savedArticleBody).getText(), body,
                "Editor title description is not correct");
    }

    @Test(description = "Checking for user editing a saved article")
    void assertForEditingSavedArticle() {
        BaseActions baseActions = new BaseActions();
        Waits waits = new Waits();

        baseActions.clickButton(driver, homeButton);
        //Adding new Article
        waits.waitforElementClickable(driver, editorButton);
        baseActions.clickButton(driver, editorButton);

        String title = faker.text().text(10);
        String body = faker.text().text(15);

        baseActions.sendKeys(driver, articleTitle, title);
        baseActions.sendKeys(driver, articleDescription, faker.text().text(10));
        baseActions.sendKeys(driver, articleBody, body);
        waits.waitforElementClickable(driver, publishArticleButton);
        baseActions.clickButton(driver, publishArticleButton);

        //Editing saved article
        waits.waitforElementClickable(driver, editArticleButton);
        baseActions.clickButton(driver, editArticleButton);

        String newBody = faker.text().text(15);

        driver.findElement(articleBody).clear();
        baseActions.sendKeys(driver, articleBody, newBody);
        waits.waitforElementClickable(driver, publishArticleButton);
        baseActions.clickButton(driver, publishArticleButton);

        assertEquals(driver.findElement(savedArticleBody).getText(), newBody,
                "Editor title description is not correct");
    }

    @Test(description = "Checking for deleting a saved article")
    void assertForDeletingArticle() {
        BaseActions baseActions = new BaseActions();
        Waits waits = new Waits();
        SoftAssert softAssert = new SoftAssert();

        baseActions.clickButton(driver, homeButton);
        //Adding new Article
        waits.waitforElementClickable(driver, editorButton);
        baseActions.clickButton(driver, editorButton);

        String title = faker.text().text(10);
        String body = faker.text().text(15);

        baseActions.sendKeys(driver, articleTitle, title);
        baseActions.sendKeys(driver, articleDescription, body);
        baseActions.sendKeys(driver, articleBody, faker.text().text(10));
        waits.waitforElementClickable(driver, publishArticleButton);
        baseActions.clickButton(driver, publishArticleButton);

        //Deleting saved article and user is taken to home page
        softAssert.assertTrue(driver.findElement(deleteArticleButton).isDisplayed(),
                "Delete article button is not displayed to author");

        waits.waitforElementClickable(driver, deleteArticleButton);
        baseActions.clickButton(driver, deleteArticleButton);

        softAssert.assertTrue(driver.findElements(tagElements).size() == 100, "Tag list items are less than 100");
        softAssert.assertAll();
    }

    @Test(description = "Checking for delete article button not visible to non author")
    void assertForDeleteArticleButtonNotVisibleNonAuthorArticle() {
        Waits waits = new Waits();
        BaseActions baseActions = new BaseActions();

        baseActions.clickButton(driver, homeButton);
        waits.waitforElementClickable(driver, tagElements);
        driver.findElement(tagElements).click();

        waits.waitforElementClickable(driver, articlesPreview);
        driver.findElement(articlesPreview).click();

        try {
            assertFalse(driver.findElement(deleteArticleButton).isDisplayed(), "Delete article button is displayed");
        } catch (Exception e) {
            logger.info("Delete article button is not visible");
        }
    }

    @Test(description = "Checking for comment section in a saved article")
    void assertForCommentSectionSavedArticle() {
        Waits waits = new Waits();
        BaseActions baseActions = new BaseActions();

        baseActions.clickButton(driver, homeButton);
        waits.waitforElementClickable(driver, tagElements);
        driver.findElement(tagElements).click();

        waits.waitforElementClickable(driver, articlesPreview);
        driver.findElement(articlesPreview).click();

        assertTrue(driver.findElement(commentSection).isDisplayed(), "Comments section is not displayed");
    }

    @Test(description = "Checking for new comment saving from comment section in a saved article")
    void assertForSavingOfNewCommentUsingCommentSection() throws InterruptedException {
        Waits waits = new Waits();
        BaseActions baseActions = new BaseActions();
        SoftAssert softAssert = new SoftAssert();

        baseActions.clickButton(driver, homeButton);
        waits.waitforElementClickable(driver, tagElements);
        driver.findElement(tagElements).click();

        waits.waitforElementClickable(driver, articlesPreview);
        driver.findElement(articlesPreview).click();

        softAssert.assertTrue(driver.findElement(commentSection).isDisplayed(), "Comments section is not displayed");

        //Saving and validating new comment
        String commentDescriptionText = faker.text().text(10);

        baseActions.sendKeys(driver, commentDescription, commentDescriptionText);
        baseActions.clickButton(driver, postCommentButton);

        softAssert.assertEquals(driver.findElement(savedComment).getText(), commentDescriptionText, "Comment is not successfully saved");

        //Deleting saved comment
        baseActions.clickButton(driver, deleteCommentButton);
        Thread.sleep(2000);

        softAssert.assertTrue(! driver.findElement(By.cssSelector("[class='container page']")).getText()
                .contains(commentDescriptionText), "Comment is still visible");
        softAssert.assertAll();
    }
}
