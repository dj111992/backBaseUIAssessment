package com.assessment.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ConduitSignInPage {
    public static By email = By.cssSelector("[placeholder='Email']");
    public static By password = By.cssSelector("[placeholder='Password']");
    public static By submitButton = By.cssSelector("[type='submit']");

}
