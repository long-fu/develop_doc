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
    body = '{"username": "haoshuai", "password": "19920105"}'
    conn = client.HTTPConnection("0.0.0.0:2342")
    headers = {"Accept": "application/json, text/plain, */*","Content-Type": "application/json; charset=utf-8"}
    conn.request("POST","/api/v1/session",body.encode('utf-8'),headers=headers)
    r2 = conn.getresponse()
    print(r2.status)
    # print(r2.status, r2.reason)
    # token = r2.headers.get('X-Session-Id')
    print(r2.read())
    # print(token)
    # conn.close()
    # body1 = '{"username": "admin", "password": "admin"}'
    # headers1 = {"Accept": "application/json, text/plain, */*","Content-Type": "application/json; charset=utf-8"}
    # url = "https://demo-zh.photoprism.app/api/v1/api/v1/session"
    # r = requests.post(url, data=body1, headers=headers1)
    # print(r.status_code)
    # print(r.text)
	# r = requests.post("https://demo-zh.photoprism.app/api/v1/api/v1/session", body=body, headers=headers1)
    # print(r.status_code)
    # print(r.text)
    
    # conn = client.HTTPConnection("127.0.0.1:2342")
    # header = {
    #     "Accept": "application/json, text/plain, */*",
    #     #"Content-Type": "application/json; charset=utf-8",
    #     "X-Session-Id":"c59f9778b8e93daa841d094ee94914e25527893b89335ad8"
    #     }
    # print("/api/v1/photos?count=60&offset=0")
    # conn.request("GET", '''/api/v1/photos?count=60&offset=0''',headers=header)
    # r2 = conn.getresponse()
    # print(r2)
    # print(r2.headers)
	# # print(r2.getheaders())
    # print(r2.status, r2.reason)
    # url = "https://demo-zh.photoprism.app/api/v1/albums?count=24&offset=0&q=&category=&order=favorites&year=&type=album"
    # r = requests.get(url, headers=header)
    # print("-----------------")
    # print(r.status_code)
    # print(r.text)
    
    
    
    
    # r2 = conn.getresponse()
    # print(r2.status, r2.reason)
    # data2 = r2.read()
    # print(data2.decode('utf-8'))
    # conn.close()
    # pass

# import requests
# #用open的方式打开文件，作为字典的值。file是请求规定的参数，每个请求都不一样。
# files = {'file': open("/Users/haoshuai/Desktop/8C13A368-9FE6-4F3D-B379-CBAE5E662019_1_105_c.jpeg", 'rb')}
# #请求的地址，这个地址中规定请求文件的参数必须是file
# url = 'http://127.0.0.1:2342/api/v1/users/urpl5sn1qmoiucq9/upload/ajskd'
# header = {
# 	"Accept": "application/json, text/plain, */*",
# 	"Content-Type": "application/json; charset=utf-8",
# 	"X-Session-Id":"c59f9778b8e93daa841d094ee94914e25527893b89335ad8"
# 	}
# #用files参数接收
# res = requests.post(url, headers=header,files=files)
# print(res.status_code)
# print(res.request.headers)
# print(res.headers)
# print(res.text)
# upload()
login()

# import base64
# from requests import session
# from requests_toolbelt import MultipartEncoder
# sessions = session()

# with open(file=file_path, mode='rb') as fis:
#     file_content = fis  # base64.b64encode().decode() 有些需要编码
#     files = {
#         'filename': filename,
#         'Content-Disposition': 'form-data;',
#         'Content-Type': 'image/jpeg',
#         'file': (filename, file_content, 'image/jpeg')  
#     }
#     form_data = MultipartEncoder(files)  # 格式转换
#     sessions.headers['Content-Type'] = form_data.content_type
#     response = sessions.post(url=upload_img_url, data=form_data)
