https://github.com/kubernetes/ingress-nginx
v1.4.0

https://juejin.cn/post/7156175481526550536

```sh

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.4.0/deploy/static/provider/cloud/deploy.yaml

kubectl get pods --namespace=ingress-nginx



kubectl get pod,svc -o wide

kubectl exec -it -n ingress-nginx ingress-nginx-controller-6d4556cf9-wmr4q /bin/sh

kubectl get ingress
```