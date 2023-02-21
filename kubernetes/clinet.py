from kubernetes import client, config

# Configs can be set in Configuration class directly or using helper utility
config.load_kube_config("E:\github\develop_doc\kubernetes\config")

v1 = client.CoreV1Api()
# print("Listing pods with their IPs:")
# ret = v1.list_pod_for_all_namespaces(watch=False)
# for i in ret.items:
#     print("%s\t%s\t%s" % (i.status.pod_ip, i.metadata.namespace, i.metadata.name))


body = {
    "metadata": {
        "labels": {
            "foo": "bar",
            "baz": None}
    }
}


info = v1.patch_node("k8s-master02",body)

print(info)