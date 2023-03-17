package com.assessment.ui.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Waits {

    public void waitforElementClickable(WebDriver driver, By by){
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public void waitForElementVisibility(WebDriver driver, By by){
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public boolean waitForElementIsDisplayed(WebDriver driver, By by){
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        return wait.until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
    }

    public void waitForSometime(WebDriver driver, long time){
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }
}
