import pytest


@pytest.mark.smoke
def test_health(client, base_url):
    response = client.get(f"{base_url}/api/health", timeout=5)
    assert response.status_code == 200
    assert response.json()["status"] == "UP"

