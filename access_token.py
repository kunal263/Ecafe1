import requests;
import json;

client_id = "Exj972JlK9uoTVPk3lBWoTTPk0UaA3ecfpKryqYo";
client_secret = "TjaeYVlVZBO3pSid6WF9TKukV1D5qxTpFZxNyvTxKAWJIG5xc3lgkkJKhMkB8uAJk2VVq0rAYj5mjrcdYLyTULAo8B02GHCyB8qWrMOUOXgRJEl86SwyB0XJN9drBROz";
env = "production";

if (client_id.startswith("test")):
    url = "https://test.instamojo.com/oauth2/token/";
    env = "test";

payload = "grant_type=client_credentials&client_id=" + client_id + "&client_secret=" + client_secret;
headers = {
    'content-type': "application/x-www-form-urlencoded",
    'cache-control': "no-cache"
    }

response = requests.request("POST", url, data=payload, headers=headers);
token = env + json.loads(response.text)["access_token"];
print(token);
