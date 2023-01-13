import urllib.request
# import http.client
from http import client

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


def login():
    conn = client.HTTPConnection("192.168.8.30:2342")
    body = '{"username": "haoshuai", "password": "19920105"}'
    header = {"Accept": "application/json, text/plain, */*","Content-Type": "application/json; charset=utf-8"}
    conn.request("POST","/api/v1/session",body.encode('utf-8'),headers=header)
    r2 = conn.getresponse()
    print(r2.status, r2.reason)
    token = r2.headers.get('X-Session-Id')
    conn.close()
    
    conn = client.HTTPConnection("192.168.8.30:2342")
    header = {"Accept": "application/json, text/plain, */*",
    "Content-Type": "application/json; charset=utf-8","X-Session-Id":token}
    conn.request("GET", "/api/v1/photos?count=60&offset=0",headers=header)
    r2 = conn.getresponse()
    print(r2.status, r2.reason)
    data2 = r2.read()
    print(data2.decode('utf-8'))
    conn.close()
    pass

login()
