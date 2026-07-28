import pytest


def create_order(client, base_url, quantity=1):
    return client.post(
        f"{base_url}/api/orders",
        json={
            "studentName": "测试同学",
            "pickupSlot": "12:00-12:20",
            "foodId": 1,
            "quantity": quantity,
        },
        timeout=5,
    )


@pytest.mark.smoke
def test_create_order_returns_preparing_status(client, base_url):
    response = create_order(client, base_url)
    assert response.status_code == 200
    body = response.json()
    assert body["orderNo"].startswith("CF")
    assert body["status"] == "PREPARING"
    assert body["totalAmount"] == 18


@pytest.mark.regression
def test_order_rejects_zero_quantity(client, base_url):
    response = create_order(client, base_url, quantity=0)
    assert response.status_code == 400


@pytest.mark.regression
def test_order_rejects_unavailable_food(client, base_url):
    client.put(f"{base_url}/api/admin/foods/1/stock", params={"stock": 0}, timeout=5)
    try:
        response = create_order(client, base_url)
        assert response.status_code == 400
        assert "不可购买" in response.json()["message"] or "库存" in response.json()["message"]
    finally:
        # Keep the suite order-independent for local reruns and CI workers.
        client.put(f"{base_url}/api/admin/foods/1/stock", params={"stock": 24}, timeout=5)


@pytest.mark.regression
def test_cancel_order_restores_inventory(client, base_url):
    before = client.get(f"{base_url}/api/foods", timeout=5).json()
    before_stock = next(item["stock"] for item in before if item["id"] == 1)
    order_response = create_order(client, base_url)
    assert order_response.status_code == 200
    order_id = order_response.json()["id"]

    cancel_response = client.post(f"{base_url}/api/orders/{order_id}/cancel", timeout=5)
    assert cancel_response.status_code == 200
    assert cancel_response.json()["status"] == "CANCELLED"

    after = client.get(f"{base_url}/api/foods", timeout=5).json()
    after_stock = next(item["stock"] for item in after if item["id"] == 1)
    assert after_stock == before_stock
