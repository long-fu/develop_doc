https://github.com/kubernetes-sigs/kubebuilder

https://zhuanlan.zhihu.com/p/283092480

https://book.kubebuilder.io/quick-start.html

# download kubebuilder and install locally.
curl -L -o kubebuilder https://go.kubebuilder.io/dl/latest/$(go env GOOS)/$(go env GOARCH)
chmod +x kubebuilder && mv kubebuilder /usr/local/bin/


mkdir -p ~/projects/guestbook
cd ~/projects/guestbook

domain 不能是k8s.io

group 也不能包含k8s.io

kubebuilder init --domain haoshuai32.github.io --repo haoshuai32.github.io/kubeeccontroller


kubebuilder create api --group kubeec --version v1 --kind Model

GITHUB_TOKEN=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

make manifests


make install


make run
