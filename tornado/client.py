import requests
import json

# url = 'http://localhost:8080/api/node/join'
# url1 = 'http://localhost:8080/api/node/list'
# header = {"Authorization":"eyJhbGciOiJSUzI1NiIsImtpZCI6Im1mUWhMd2RiVE5TRWNvNWhwU3gxNFdrOURCS1Fod3MycmZTVnZEdGNaQ1UifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJlYy1hY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImVjLWFkbWluLXRva2VuLThraDZ6Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImVjLWFkbWluIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiMmI2YzUyZDEtODY0ZC00ZjA4LWE0MzMtOGQ5ZjVhZjdkNzYzIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmVjLWFjY291bnQ6ZWMtYWRtaW4ifQ.UGNgx3gmIdz9H8XttmWfZol2BNn46bTPOsqIUDlJgLqamYmSjfiws29zIE6qQMk9KsFbQCbDMfMwwseGRkG1xJB-9LNSbXObfXQAQLT8m-c6CJLIN7IZdCqoY8h9D3xXBC4gPQKmL6555qOFTXF9_qc4lmFNuWbOR6QoUGPg8HbNchLSAvAIjuXXzigoPdgTnqjkms2Tm5iU0mpP7QGMSr47ZKzXGacTR_nPQ-9M266V50aXzuunv8CqMQ5BKBItUWiYH6_BOxNhkhv3P4eN0UFA_JDGf6QBlaa4HzGcchyNlDSeeIZRg4Swbi1macORvwh51kq482bMXhf9Hc63nA"}




def get_encoding_from_reponse(reponse):
    encoding = requests.utils.get_encodings_from_content(reponse.text)
    if encoding:
        return encoding[0]
    else:
        return requests.utils.get_encoding_from_headers(reponse.headers)

url = "http://0.0.0.0:9888/api/v1/model/hello"

dic = {
    
}

r = requests.post(url,json=dic)

print(r.status_code)

r.encoding = get_encoding_from_reponse(r)

print(r.text)