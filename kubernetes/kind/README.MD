https://kind.sigs.k8s.io/

```sh
kind create cluster # Default cluster context name is `kind`.
...
kind create cluster --name kind-2

kind get clusters

kind delete cluster 

kind delete cluster --name kind-2


kubectl cluster-info --context kind-kind

kubectl cluster-info --context kind-kind-2

kind build node-image

kind create cluster --image kindest/node:latest

# kind中镜像要自己手动家在
kind load docker-image my-custom-image-0 my-custom-image-1

kind load docker-image quay.io/tigera/operator:v1.29.0 quay.io/tigera/operator:v1.29.0

kind load docker-image quay.io/tigera/operator:v1.28.5 quay.io/tigera/operator:v1.28.5

kind load docker-image eclipse-mosquitto:1.6.15 eclipse-mosquitto:1.6.15
```
