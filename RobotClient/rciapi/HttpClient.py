import requests
import urllib3
from .CookieManager import CookieManager

# Suppress only the single InsecureRequestWarning from urllib3
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

class HttpClient:
    """
    A class to represent an HTTP client with GET and POST request methods.
    """
    def __init__(self, base_url: str):
        """
        Initializes the HttpClient with the given base URL.
        :param str base_url: The base URL of the server.
        """
        if not base_url.startswith('https://'):
            raise ValueError("Base URL must start with 'https://'")
        self.base_url = base_url
        self.cookies = None

    def POST(self, endpoint: str, data) -> str | None:
        """
        Sends a POST request to the server.
        :param str endpoint: The endpoint to send the POST request to.
        :param data: The data to be sent in the POST request.
        :return: The response text.
        """
        try:
            response = requests.post(
                url=f'{self.base_url}{endpoint}',
                data=data,
                headers={'Content-Type': 'application/x-www-form-urlencoded'},
                verify=False  # Disable SSL verification
            )

            response.raise_for_status()
            self.cookies = CookieManager(response.text).get_cookie_dict()
            return response.text

        except requests.exceptions.RequestException as e:
            print(f"POST request failed: {e}")
            return None

    def GET(self, endpoint: str, data = None) -> str | None:
        """
        Sends a GET request to the server.
        :param endpoint: The endpoint to send the GET request to.
        :param data: The data to be sent in the GET request.
        :return: The response text.
        """
        try:
            response = requests.get(
                url=f'{self.base_url}{endpoint}',
                params=data,
                cookies=self.cookies,
                verify=False  # Disable SSL verification
            )

            response.raise_for_status()
            return response.text

        except requests.exceptions.RequestException as e:
            print(f"GET request failed: {e}")
            return None