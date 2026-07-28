import pytest
from appium.webdriver.common.appiumby import AppiumBy


@pytest.mark.smoke
def test_food_home_page_is_visible(driver):
    assert driver.find_element(AppiumBy.XPATH, "//*[contains(@text, '今天也要好好吃饭')]").is_displayed()


@pytest.mark.smoke
def test_user_can_open_orders(driver):
    driver.find_element(AppiumBy.XPATH, "//*[contains(@text, '订单')]").click()
    assert driver.find_element(AppiumBy.XPATH, "//*[contains(@text, '我的订单')]").is_displayed()

