wget https://github.com/goharbor/harbor/releases/download/v2.7.1/harbor-offline-installer-v2.7.1.tgz

tar xzvf harbor-offline-installer-v2.7.1.tgz

openssl genrsa -out ca.key 4096

openssl req -x509 -new -nodes -sha512 -days 3650 \
 -subj "/C=CN/ST=Beijing/L=Beijing/O=shuai/OU=Personal/CN=hub.docker.haoshuai.com" \
 -key ca.key \
 -out ca.crt

openssl genrsa -out hub.docker.haoshuai.com.key 4096

openssl req -sha512 -new \
    -subj "/C=CN/ST=Beijing/L=Beijing/O=shuai/OU=Personal/CN=hub.docker.haoshuai.com" \
    -key hub.docker.haoshuai.com.key \
    -out hub.docker.haoshuai.com.csr

cat > v3.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=hub.docker.haoshuai.com
DNS.2=hub.docker.haoshuai
DNS.3=docker.haoshuai
EOF

openssl x509 -req -sha512 -days 3650 \
    -extfile v3.ext \
    -CA ca.crt -CAkey ca.key -CAcreateserial \
    -in hub.docker.haoshuai.com.csr \
    -out hub.docker.haoshuai.com.crt

mkdir -p /data/cert/
cp hub.docker.haoshuai.com.crt /data/cert/
cp hub.docker.haoshuai.com.key /data/cert/

openssl x509 -inform PEM -in hub.docker.haoshuai.com.crt -out hub.docker.haoshuai.com.cert

mkdir -p /etc/docker/certs.d/hub.docker.haoshuai.com/
cp hub.docker.haoshuai.com.cert /etc/docker/certs.d/hub.docker.haoshuai.com/
cp hub.docker.haoshuai.com.key /etc/docker/certs.d/hub.docker.haoshuai.com/
cp ca.crt /etc/docker/certs.d/hub.docker.haoshuai.com/


docker tag ec-manager-web:v0.0.1 hub.docker.haoshuai.com/test/ec-manager-web:v0.0.1

docker images

docker login hub.ec.com

docker push hub.docker.haoshuai.com/test/hello-springboot:v0.0.1