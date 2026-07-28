import pytest


@pytest.mark.smoke
def test_available_foods_have_required_fields(client, base_url):
    response = client.get(f"{base_url}/api/foods", timeout=5)
    assert response.status_code == 200
    foods = response.json()
    assert foods
    for food in foods:
        assert food["id"]
        assert food["name"]
        assert food["price"] >= 0
        assert food["stock"] >= 0
        assert food["available"] is True


@pytest.mark.regression
@pytest.mark.parametrize("stock", [0, 5, 20])
def test_admin_can_update_stock(client, base_url, stock):
    response = client.put(f"{base_url}/api/admin/foods/1/stock", params={"stock": stock}, timeout=5)
    assert response.status_code == 200
    assert response.json()["stock"] == stock
    assert response.json()["available"] is (stock > 0)

