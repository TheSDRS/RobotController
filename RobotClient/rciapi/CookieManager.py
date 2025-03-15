import re

class CookieManager:
    def __init__(self, response_text=None):
        self.cookie_data = self.parse_cookie_data(response_text) if response_text else None

    def parse_cookie_data(self, response_text):
        session_id_match = re.search(r"sessionId='([^']+)'", response_text)
        path_match = re.search(r"path='([^']+)'", response_text)
        domain_match = re.search(r"domain='([^']+)'", response_text)

        if not session_id_match or not path_match or not domain_match:
            return None

        return {
            'sessionId': session_id_match.group(1),
            'path': path_match.group(1),
            'domain': domain_match.group(1)
        }

    def set_cookie(self):
        if not self.cookie_data:
            return None

        session_id = self.cookie_data.get('sessionId')
        path = self.cookie_data.get('path')
        domain = self.cookie_data.get('domain')

        cookie_string = f"sessionId={session_id}; path={path}; domain={domain}; SameSite=Lax"
        return cookie_string

    def get_cookie_dict(self):
        if not self.cookie_data:
            return None

        return {'sessionId': self.cookie_data['sessionId']}