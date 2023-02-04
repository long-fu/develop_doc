import urllib.request
# import http.client
from http import client
import requests
	# cfg := &Config{
	# 	Config: &Options{},
	# }
	# bytes, err := ioutil.ReadAll(resp.Body)
	# if err != nil {
	# 	return fmt.Errorf("unable to parse auth body: %v", err)
	# }
	# err = json.Unmarshal(bytes, &cfg)
	# if err != nil {
	# 	return fmt.Errorf("unable to json unmarshal auth body: %v", err)
	# }

	# token := resp.Header.Get(APIAuthHeaderKey)
	# if token == "" {
	# 	return fmt.Errorf("missing auth token from successful login")
	# }
	# c.v1client = v1.New(c.connectionURL, token, cfg.Config.DownloadToken)
	# APIContentType string = "application/json; charset=utf-8"

	# // Default Host Configuration
	# APIAuthHeaderKey string = "X-Session-Id"

	# downloadToken string
	# token         string


# body = '{"username": "admin", "password": "admin"}'
# headers1 = {"Accept": "application/json, text/plain, */*","Content-Type": "application/json; charset=utf-8"}
# r = requests.post("https://demo-zh.photoprism.app/api/v1/api/v1/session", body=body, headers=headers1)
# print(r.status_code)
# print(r.text)

def login():
    # conn = client.HTTPConnection("159.223.86.35:443")
    # conn.request("POST","/api/v1/session",body.encode('utf-8'),headers=header)
    # r2 = conn.getresponse()
    # print(r2)
    # print(r2.status, r2.reason)
    # token = r2.headers.get('X-Session-Id')
    # conn.close()
    body1 = '{"username": "admin", "password": "admin"}'
    headers1 = {"Accept": "application/json, text/plain, */*","Content-Type": "application/json; charset=utf-8"}
    url = "https://demo-zh.photoprism.app/api/v1/api/v1/session"
    # r = requests.post(url, data=body1, headers=headers1)
    # print(r.status_code)
    # print(r.text)
	# r = requests.post("https://demo-zh.photoprism.app/api/v1/api/v1/session", body=body, headers=headers1)
    # print(r.status_code)
    # print(r.text)
    
    # conn = client.HTTPConnection("demo-zh.photoprism.app")
    header = {
        "Accept": "application/json, text/plain, */*",
        "Content-Type": "application/json; charset=utf-8",
        # "X-Session-Id":"234200000000000000000000000000000000000000000000"
        }
    # conn.request("GET", "/api/v1/photos?count=60&offset=0",headers=header)
    url = "https://demo-zh.photoprism.app/api/v1/albums?count=24&offset=0&q=&category=&order=favorites&year=&type=album"
    r = requests.get(url, headers=header)
    print("-----------------")
    print(r.status_code)
    print(r.text)
    
    
    
    
    # r2 = conn.getresponse()
    # print(r2.status, r2.reason)
    # data2 = r2.read()
    # print(data2.decode('utf-8'))
    # conn.close()
    # pass


login()
