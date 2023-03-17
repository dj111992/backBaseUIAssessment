package com.assessment.ui.pages;

import org.openqa.selenium.By;

public class ConduitProfilePage {

    public static By userName = By.cssSelector(".user-info h4");
    public static By userBio = By.cssSelector(".user-info p");
    public static By profileSettingsButton = By.xpath("//a[contains(@href,'/settings')]");

}
