https://github.com/kubernetes-sigs/kubebuilder

https://zhuanlan.zhihu.com/p/283092480

https://book.kubebuilder.io/quick-start.html

# download kubebuilder and install locally.
curl -L -o kubebuilder https://go.kubebuilder.io/dl/latest/$(go env GOOS)/$(go env GOARCH)
chmod +x kubebuilder && mv kubebuilder /usr/local/bin/


mkdir -p ~/projects/guestbook
cd ~/projects/guestbook

kubebuilder init --domain k8s.io --repo k8s.io/kubeeccontroller


kubebuilder create api --group kubeeccontroller.k8s.io --version v1 --kind Model

GITHUB_TOKEN=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

make manifests


make install


make run
