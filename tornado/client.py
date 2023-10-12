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

url = "http://0.0.0.0:9888/api/v1/model/spec"

# dic = {"kind":"Model","apiVersion":"inference.haoshuai32.github.com/v1","metadata":{"name":"model-sample","namespace":"default","uid":"c4d4c8a0-9123-49fc-b14b-922e498b7124","resourceVersion":"251628","generation":1,"creationTimestamp":"2023-05-11T02:59:15Z","labels":{"app.kubernetes.io/created-by":"inference","app.kubernetes.io/instance":"model-sample","app.kubernetes.io/managed-by":"kustomize","app.kubernetes.io/name":"model","app.kubernetes.io/part-of":"inference"},"managedFields":[{"manager":"kubectl-create","operation":"Update","apiVersion":"inference.haoshuai32.github.com/v1","time":"2023-05-11T02:59:15Z","fieldsType":"FieldsV1","fieldsV1":{"f:metadata":{"f:labels":{".":{},"f:app.kubernetes.io/created-by":{},"f:app.kubernetes.io/instance":{},"f:app.kubernetes.io/managed-by":{},"f:app.kubernetes.io/name":{},"f:app.kubernetes.io/part-of":{}}},"f:spec":{".":{},"f:data":{".":{},"f:name":{},"f:path":{},"f:retrymax":{},"f:sha":{},"f:url":{}},"f:nodeSelector":{".":{},"f:kubernetes.io/os":{}}}}}]},"spec":{"nodeSelector":{"kubernetes.io/os":"linux"},"data":{"retrymax":3,"url":"htttps://onelcat.github.com","name":"frace","path":"/data","sha":"b5ee974c3d0332a669a638fc7b64a155fe1ec0cfad03bde18a643a6724e188a2"}},"status":{}}
dic = {"kind":"Model","apiVersion":"inference.haoshuai32.github.com/v1","metadata":{"name":"model-sample-1","namespace":"default","uid":"983ed272-3de8-49c5-ba21-53d347d282fd","resourceVersion":"253613","generation":1,"creationTimestamp":"2023-05-11T03:22:24Z","labels":{"app.kubernetes.io/created-by":"inference","app.kubernetes.io/instance":"model-sample","app.kubernetes.io/managed-by":"kustomize","app.kubernetes.io/name":"model","app.kubernetes.io/part-of":"inference"},"managedFields":[{"manager":"kubectl-create","operation":"Update","apiVersion":"inference.haoshuai32.github.com/v1","time":"2023-05-11T03:22:24Z","fieldsType":"FieldsV1","fieldsV1":{"f:metadata":{"f:labels":{".":{},"f:app.kubernetes.io/created-by":{},"f:app.kubernetes.io/instance":{},"f:app.kubernetes.io/managed-by":{},"f:app.kubernetes.io/name":{},"f:app.kubernetes.io/part-of":{}}},"f:spec":{".":{},"f:data":{".":{},"f:name":{},"f:path":{},"f:retrymax":{},"f:sha":{},"f:url":{}},"f:nodeSelector":{".":{},"f:kubernetes.io/os":{}}}}}]},"spec":{"nodeSelector":{"kubernetes.io/os":"linux"},"data":{"retrymax":3,"url":"htttps://onelcat.github.com","name":"frace-1","path":"/data","sha":"b5ee974c3d0332a669a638fc7b64a155fe1ec0cfad03bde18a643a6724e188a2"}},"status":{}}
r = requests.post(url,json=dic)

print(r.status_code)

r.encoding = get_encoding_from_reponse(r)

print(r.text)