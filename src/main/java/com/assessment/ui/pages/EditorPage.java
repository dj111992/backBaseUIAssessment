package com.assessment.ui.pages;

import org.openqa.selenium.By;

public class EditorPage {
    public static By articleTitle = By.cssSelector("[placeholder='Article Title']");
    public static By articleDescription = By.cssSelector("[formcontrolname='description']");
    public static By articleBody = By.cssSelector("[formcontrolname='body']");
    public static By publishArticleButton = By.xpath("//button[contains(text(),' Publish Article ')]");
    public static By savedArticleBody = By.cssSelector("[class='container page'] p");
    public static By editArticleButton = By.linkText("Edit Article");
    public static By deleteArticleButton = By.xpath("//button[contains(text(),' Delete Article ')]");
    public static By commentSection = By.cssSelector(".comment-form");
    public static By commentDescription = By.cssSelector("[placeholder='Write a comment...']");
    public static By postCommentButton = By.xpath("//button[contains(text(),' Post Comment ')]");
    public static By savedComment = By.cssSelector(".card p");
    public static By deleteCommentButton = By.cssSelector(".card-footer .mod-options");

}
