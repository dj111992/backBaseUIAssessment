package com.assessment.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ConduitHomePage {
    public static By spaceWebListPopUp = By.xpath("//ul[contains(@class,'nav nav-pills')]");
    public static By articlesPreview =By.cssSelector(".article-preview");

    public static By options =By.cssSelector("");

    public static By tagElements =By.xpath("//div[contains(@class, 'tag-list')]//a[position() <= 100]");

    public static By tagTabs = By.xpath("//ul[contains(@class,'nav nav-pills')]//a");
    public static By paginationItems = By.cssSelector(".pagination li a");
    public static By signInButton = By.cssSelector("[routerLink='/login']");

    public static By homeButton = By.cssSelector("[routerlinkactive='active']");

    public static By settingsButton = By.cssSelector("[routerLink='/settings']");
    public static By editorButton = By.cssSelector("[routerLink='/editor']");
    public static By userProfileButton = By.xpath("//a[contains(@href,'/profile/')]");
}
