package com.assessment.ui.actions;

import com.assessment.ui.pages.ConduitHomePage;
import com.assessment.ui.pages.ConduitSettingsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class BaseActions {
    Logger logger = Logger.getLogger("Base Actions");

    /*
    This method is for click on specific button element
     */
    public void clickButton(WebDriver driver, By webElement) {
        logger.info("Clicking on particular button element");
        RetryStrategy retryStrategy = new RetryStrategy();
        Waits waits = new Waits();

        while (retryStrategy.shouldRetry()) {
            try {
                if (waits.waitForElementIsDisplayed(driver, webElement)) {
                    driver.findElement(webElement).click();
                    waits.waitForSometime(driver, 2000);
                    retryStrategy.noErrorOccurred();
                    logger.info("Particular element is visible on the screen for clicking");
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                try {
                    retryStrategy.errorOccured();
                    logger.info("Error occurred and retrying the element locating for button click");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendKeys(WebDriver driver, By webElement, String text) {
        logger.info("Adding text in particular element");
        RetryStrategy retryStrategy = new RetryStrategy();
        Waits waits = new Waits();

        while (retryStrategy.shouldRetry()) {
            try {
                if (waits.waitForElementIsDisplayed(driver, webElement)) {
                    driver.findElement(webElement).sendKeys(text);
                    waits.waitForSometime(driver, 1000);
                    retryStrategy.noErrorOccurred();
                    logger.info("Particular element is visible on the screen for clicking");
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                try {
                    retryStrategy.errorOccured();
                    logger.info("Error occurred and retrying the element locating for button click");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public Object scrollToElement(WebElement element) {
        JavascriptExecutor js = null;
        logger.info("Scrolling to the element located at : " + element.getLocation().toString());
        return js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public Object scrollToElementSmoothly(WebElement element) {
        JavascriptExecutor js = null;
        logger.info("Scrolling to the element located at : " + element.getLocation().toString());
        final String scrollJSScript = "arguments[0]"
                + ".scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});";
        return js.executeScript(scrollJSScript, element);
    }

    public final BiConsumer<String, WebDriver> MAIN_PAGE_WEB_POP = (option, driver) -> {
        Waits waits = new Waits();
        waits.waitForElementVisibility(driver, ConduitHomePage.spaceWebListPopUp);
        RetryStrategy retryStrategy = new RetryStrategy();

        while (retryStrategy.shouldRetry()) {
            try {
                WebElement popUp = driver.findElement(ConduitHomePage.spaceWebListPopUp);
                List<WebElement> options = popUp.findElements(ConduitHomePage.options);

                WebElement selectedOption = null;
                selectedOption = options.stream()
                        .filter((element -> element.getText().contains(option)))
                        .findFirst()
                        .orElseThrow(() -> new Exception(
                                "Selected option either not present in UI or was not detected with the present locator "
                                        + popUp.getAttribute("innerHTML")));
                if (selectedOption == null) {
                    throw new Exception();
                } else {
                    selectedOption.click();
                    retryStrategy.noErrorOccurred();
                    logger.info("Particular element is visible on the screen for clicking");
                }
            } catch (Exception e) {
                try {
                    retryStrategy.errorOccured();
                    logger.info("Error occurred and retrying the element locating for button click");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    /*public static final BiConsumer<String, WebDriver> FILTER_DROPDOWN = (option, driver) -> {
        WebElement popUp = driver.findElement(ConduitSettingsPage.filterOptions);
        List<WebElement> options = popUp.findElements(ConduitHomePage.options);

        WebElement selectedOption = null;
        try {
            selectedOption = options.stream()
                    .filter((element -> element.getText().contains(option)))
                    .findFirst()
                    .orElseThrow(() -> new Exception(
                            "Selected option either not present in UI or was not detected with the present locator "
                                    + popUp.getAttribute("innerHTML")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedOption.click();
    };*/

    public void windowHandles(WebDriver driver) {
        String parent = driver.getWindowHandle();

        Set<String> s = driver.getWindowHandles();

        Iterator<String> I1 = s.iterator();

        while (I1.hasNext()) {
            String child_window = I1.next();

            if (!parent.equals(child_window)) {
                driver.switchTo().window(child_window);
                logger.info(driver.switchTo().window(child_window).getTitle());
            }
        }
        }
}
