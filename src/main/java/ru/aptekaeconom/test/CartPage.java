package ru.aptekaeconom.test;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {
    SelenideElement alertMessage = $(".text-center");

    SelenideElement productCountMessage = $$("[data-entity = 'basket-items-count']").get(0);

    SelenideElement addToOrderButton = $(".text-center [href]");

    SelenideElement productPrice = $$(".basket-item-price-current-text").get(0);

    SelenideElement productCounter = $(".basket-item-amount-filed");

    SelenideElement totalSum = $(".basket-coupon-block-total-price-current");
}
