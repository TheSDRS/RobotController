from .HttpClient import HttpClient
import json

class RobotClient:
    def __init__(self, api_key):
        self.api_key = api_key
        self.httpClient = HttpClient("https://localhost:8080")
        self.__register_robot()

    def __register_robot(self):
        # Send POST request
        post_data = {
            'username': "RobotClient",
            'password': self.api_key,
        }
        response_text = self.httpClient.POST('/api/register', post_data)

        if response_text:
            print("API successfully registered.")

    def get_all_information(self):
        data = {
            'receiver': 'robot',
        }
        response_text = self.httpClient.GET('/api/update', data)
        if response_text:
            return json.loads(response_text)
        return None