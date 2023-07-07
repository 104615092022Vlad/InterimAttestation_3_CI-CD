package ru.aptekaeconom.test;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


public class AptekaTest {
    @BeforeEach
    public void setUp() {
        Configuration.baseUrl = "https://aptekaeconom.com/";
        Cookie region = new Cookie("current_region", "119202");
        open("/");
        Selenide.webdriver().driver().getWebDriver().manage().addCookie(region);
        refresh();
        $(".region_wrapper .confirm_region").shouldNotBe(Condition.visible);
        executeJavaScript("window.scrollBy(0,400)");
    }

    @Test
    @DisplayName("Выбор подкатегории из каталога товаров")
    public void selectAnySubcategory() {
        Random r = new Random();
        int indexOfCategory = r.nextInt(8);
        PageTop pageTop = new PageTop();
        ProductsPage productsPage = new ProductsPage();
        String categoryName;
        String subcategoryName;

        SelenideElement category = pageTop.selectCategory(indexOfCategory);
        int subcategoryListSize = pageTop.subcategoriesCounter(category, indexOfCategory);
        int indexOfSubcategory = r.nextInt(subcategoryListSize);
        SelenideElement subcategory = pageTop.selectSubcategory(category, indexOfCategory, indexOfSubcategory);

        categoryName = category.$("span.name").getAttribute("innerText");
        subcategoryName = subcategory.$("span.name").getAttribute("innerText");

        step("Выбор подкатегории", () -> {
            subcategory.click();
        });

        step("В списке товаров есть хотя бы один", () -> {
            assertThat(productsPage.productsGridItems.size()).isGreaterThanOrEqualTo(1);
        });

        step("Отображение хлебных крошек", () -> {
            assertThat(pageTop.breadCrumbs.get(0).getText()).isEqualTo("Главная");
            assertThat(pageTop.breadCrumbs.get(1).getText()).isEqualTo("Каталог");
            assertThat(pageTop.breadCrumbs.get(2).getText()).isEqualTo(categoryName);
            assertThat(pageTop.breadCrumbs.get(3).getText()).isEqualTo(subcategoryName);
        });

        step("Отображение подкатегории в каталогах", () -> {
            assertThat(subcategory.
                    $(" span.name").getAttribute("innerText")).isEqualTo(subcategoryName);
            assertThat(productsPage.selectSideSubcategory(indexOfCategory).get(indexOfSubcategory).
                    $("span").getAttribute("innerText")).isEqualTo(subcategoryName);

        });

        closeWebDriver();
    }

    @Test
    @DisplayName("Откладывание товара")
    public void saveProduct() {
        Random r = new Random();
        int indexOfCategory = r.nextInt(8);
        PageTop pageTop = new PageTop();
        ProductsPage productsPage = new ProductsPage();
        SelenideElement category = pageTop.selectCategory(indexOfCategory);

        step("Выбор непустой подкатегории из каталога", () -> {
            int indexOfSubcategory = 0;
            do {
                executeJavaScript("window.scrollBy(0,400)");
                pageTop.catalog.hover();
                pageTop.selectSubcategory(category, indexOfCategory, indexOfSubcategory).click();
                indexOfSubcategory++;
            }
            while (!productsPage.productsGrid.exists());
        });

        int indexOfProduct = r.nextInt(productsPage.productsGridItems.size());
        SelenideElement product = productsPage.productsGridItems.filter(text("В наличии")).get(indexOfProduct);

        step("Добавление товара в список отложенных", () -> {
            product.$(".wish_item_button [title]").shouldHave(attribute("title", "Отложить"));
            product.$(".wish_item_button").click();
        });

        String productPrice = product.$(" .price_value").getAttribute("innerText");

        step("Текст кнопки желаемого корректен", () -> {
            pageTop.wishButton.
                    shouldHave(attribute("title", "В отложенных товаров на " + productPrice + " руб."));
        });

        step("Переход в корзину", () -> {
            pageTop.wishButton.click();
            assertThat(pageTop.pageTitle.getAttribute("innerText")).isEqualTo("Корзина");
        });

        CartPage cartPage = new CartPage();

        step("Товар имеет статус отложенного", () -> {
            cartPage.alertMessage.shouldHave(partialText("Товар отложен"));
        });

        step("Итоговая сумма не изменилась", () -> {
            assertThat(cartPage.totalSum.getAttribute("innerText")).isEqualTo("0 руб.");
        });

        closeWebDriver();
    }

    @Test
    @DisplayName("Добавление отложенного товара в корзину")
    public void addToCart() {
        Random r = new Random();
        int indexOfCategory = r.nextInt(8);
        PageTop pageTop = new PageTop();
        ProductsPage productsPage = new ProductsPage();
        SelenideElement category = pageTop.selectCategory(indexOfCategory);

        step("Выбор непустой подкатегории из каталога", () -> {
            int indexOfSubcategory = 0;
            do {
                executeJavaScript("window.scrollBy(0,400)");
                pageTop.catalog.hover();
                pageTop.selectSubcategory(category, indexOfCategory, indexOfSubcategory).click();
                indexOfSubcategory++;
            }
            while (!productsPage.productsGrid.exists());
        });

        int indexOfProduct = r.nextInt(productsPage.productsGridItems.size());
        SelenideElement product = productsPage.productsGridItems.filter(text("В наличии")).get(indexOfProduct);

        step("Добавление товара в список отложенных", () -> {
            product.$(".wish_item_button").click();
        });

        String productPrice = product.$(" .price_value").getAttribute("innerText");

        step("Переход в корзину", () -> {
            pageTop.wishButton.click();
        });

        CartPage cartPage = new CartPage();

        step("Добавление отложенного товара к заказу", () -> {
            cartPage.addToOrderButton.click();
        });

        step("Проверка деталей заказа", () -> {
            assertThat(cartPage.productCountMessage.getAttribute("innerText")).isEqualTo("В корзине 1 товар");
            cartPage.productCounter.shouldHave(attribute("value", "1"));
            assertThat(cartPage.productPrice.getAttribute("innerText")).isEqualTo(productPrice + " руб.");
        });

        closeWebDriver();
    }
}