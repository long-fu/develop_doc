from kubernetes import client, config
import openpyxl

# from kubernetes import client, config
# import openpyxl
import urllib
# import urllib.parse 
from urllib import parse

txt = "name%253D~web"
new_text = urllib.parse.unquote(txt)
print(new_text)
print(urllib.parse.unquote("=~"))
print(urllib.parse.quote("name=~"))
print(urllib.parse.quote("name%3D~web"))
# Configs can be set in Configuration class directly or using helper utility
# config.load_kube_config("E:\github\develop_doc\kubernetes\config")

# v1 = client.CoreV1Api()
# print("Listing pods with their IPs:")
# ret = v1.list_pod_for_all_namespaces(watch=False)
# for i in ret.items:
#     print("%s\t%s\t%s" % (i.status.pod_ip, i.metadata.namespace, i.metadata.name))


# body = {
#     "metadata": {
#         "labels": {
#             "foo": "bar",
#             "baz": None}
#     }
# }


# info = v1.patch_node("k8s-master02",body)

# print(info)

# 68c86fb38f047d680175e161496a4d832d433079e127076ae1971d1aff9d9657
