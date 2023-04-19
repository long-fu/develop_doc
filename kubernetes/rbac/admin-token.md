
# 查看所有的集群，因为你的 .kubeconfig 文件中可能包含多个上下文
kubectl config view -o jsonpath='{"Cluster name\tServer\n"}{range .clusters[*]}{.name}{"\t"}{.cluster.server}{"\n"}{end}'

# 从上述命令输出中选择你要与之交互的集群的名称
export CLUSTER_NAME="kubernetes"

# 指向引用该集群名称的 API 服务器
APISERVER=$(kubectl config view -o jsonpath="{.clusters[?(@.name==\"$CLUSTER_NAME\")].cluster.server}")

# 创建一个 secret 来保存默认服务账户的令牌
kubectl apply -f - <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: default-token
  annotations:
    kubernetes.io/service-account.name: default
type: kubernetes.io/service-account-token
EOF

# 等待令牌控制器使用令牌填充 secret:
while ! kubectl describe secret default-token | grep -E '^token' >/dev/null; do
  echo "waiting for token..." >&2
  sleep 1
done

# 获取令牌
TOKEN=$(kubectl get secret default-token -o jsonpath='{.data.token}' | base64 --decode)

# 使用令牌玩转 API
curl -X GET $APISERVER/api --header "Authorization: Bearer $TOKEN" --insecure


kubectl get secret --namespace=default

kubectl get secret default-token-lhf7s --namespace=default -o yaml

kubectl config set-credentials sa-user --token=$(kubectl get secret default-token -o jsonpath={.data.token} | base64 -d)

TOKEN=$(kubectl get secret default-token -o jsonpath='{.data.token}' | base64 --d)

kubectl config set-context sa-context --user=sa-user

curl -k -H "Authorization:Bearer $TOKEN" $APISERVER
