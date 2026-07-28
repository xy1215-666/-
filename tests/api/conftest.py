import pytest
import requests


@pytest.fixture(scope="session")
def base_url(request):
    return request.config.getoption("--base-url")


def pytest_addoption(parser):
    parser.addoption("--base-url", action="store", default="http://localhost:8080")


@pytest.fixture(scope="session")
def client():
    session = requests.Session()
    session.headers.update({"Accept": "application/json", "Content-Type": "application/json"})
    yield session
    session.close()

