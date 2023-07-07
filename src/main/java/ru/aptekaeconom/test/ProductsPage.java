package ru.aptekaeconom.test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductsPage {

    ElementsCollection sideCategories = $$(".left_block .menu .full");

    SelenideElement productsGrid = $(".block_list");
    ElementsCollection productsGridItems = productsGrid.$$(" .item_wrap");


    public ElementsCollection selectSideSubcategory(int n) {

        ElementsCollection sideSubcategories = switch (n + 1) {
            case 1 -> sideCategories.get(n).$$(":nth-child(1) ul li");
            case 2 -> sideCategories.get(n).$$(":nth-child(2) ul li");
            case 3 -> sideCategories.get(n).$$(":nth-child(3) ul li");
            case 4 -> sideCategories.get(n).$$(":nth-child(4) ul li");
            case 5 -> sideCategories.get(n).$$(":nth-child(5) ul li");
            case 6 -> sideCategories.get(n).$$(":nth-child(6) ul li");
            case 7 -> sideCategories.get(n).$$(":nth-child(7) ul li");
            case 8 -> sideCategories.get(n).$$(":nth-child(8) ul li");
            default -> $$("");
        };
        return sideSubcategories;
    }


}
