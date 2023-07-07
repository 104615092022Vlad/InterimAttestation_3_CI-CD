package ru.aptekaeconom.test;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PageTop {
    SelenideElement wishButton = $(".basket-link.delay.with_price");

    SelenideElement catalog = $(".catalog .dropdown-toggle");
    ElementsCollection categories = $$(".wide_menu .dropdown-submenu");

    public SelenideElement selectCategory(int n) {
        SelenideElement category;
        catalog.hover();

        category = switch (n) {
            case 0 -> categories.get(0);
            case 1 -> categories.get(1);
            case 2 -> categories.get(2);
            case 3 -> categories.get(3);
            case 4 -> categories.get(4);
            case 5 -> categories.get(5);
            case 6 -> categories.get(6);
            case 7 -> categories.get(7);
            default -> $("");
        };

        return category;
    }

    public int subcategoriesCounter(SelenideElement category, int n) {
        int amount = switch (n) {
            case 0, 3, 4, 6, 7 -> category.$$(" ul li").size();
            case 1, 2, 5 -> category.$$(" ul li").size() - 1;
            default -> 0;
        };

        return amount;
    }

    public SelenideElement selectSubcategory(SelenideElement category, int n, int m) {
        SelenideElement subcategory;

        switch (n) {
            case 0, 3, 4, 6, 7 -> subcategory = category.$$(" ul li").get(m);
            case 1, 2, 5 -> {
                category.$(" ul li .more_items").click();
                subcategory = category.$$(" ul li").get(m);
            }
            default -> subcategory = $("");
        }

        return subcategory;
    }

    SelenideElement pageTitle = $("#pagetitle");
    ElementsCollection breadCrumbs = $$(".bx-breadcrumb-item--mobile");
}
