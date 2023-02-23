
import OpenSSL
from dateutil import parser
from hashlib import sha256
from cryptography.x509 import load_pem_x509_certificate

from cryptography.hazmat.backends import default_backend

import base64
import decorator

res_str = "68c86fb38f047d680175e161496a4d832d433079e127076ae1971d1aff9d9657"

cert_text = """
-----BEGIN CERTIFICATE-----
MIIC/jCCAeagAwIBAgIBADANBgkqhkiG9w0BAQsFADAVMRMwEQYDVQQDEwprdWJl
cm5ldGVzMB4XDTIzMDIxMDA2MDkwMloXDTMzMDIwNzA2MDkwMlowFTETMBEGA1UE
AxMKa3ViZXJuZXRlczCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOph
dhYLOLlLZiI2jeoqp35FWJUaZcJ1pVQZ6r9WFN2yH0GKH1h2juwHuDD+Lbdsllc1
yPXgzIdLjIYQtA07M23SozbzoFnJz7R7Nnb2KEmtlsC9IjA+AG3NN7BYzrkI0JzI
lUBBKwbEaXbI9RLPXXAyJUNH15GHonoWXOmLqC5LRtN6yo5dG9D3ifvhpLNm9hk/
o29Hm2A2bxmMb1Tdz1YmW4zxKJ8fJpQhgCWbMup/XmnLERymcEVw5mhBJR6TPsXo
mwOPviabpLdzXbBOn2QZ+aQghkL2aiV/ArRBqi67lwMdtguVNidf4w/fhSy0wAyY
3+gndVq4DO9M0k1n3sECAwEAAaNZMFcwDgYDVR0PAQH/BAQDAgKkMA8GA1UdEwEB
/wQFMAMBAf8wHQYDVR0OBBYEFD10mc7gCAaLlpNjzFTOg7apTM3oMBUGA1UdEQQO
MAyCCmt1YmVybmV0ZXMwDQYJKoZIhvcNAQELBQADggEBALfTbeogXE0NnairMTro
RbDRDeZJjCbED5+YuZc0z9aM6JHy4qgY5Ocx1oG9qfqqIfSfvwD/kGTK9kDCqvB6
KaPOubSveGbYi52eaWxonzYxY0YjgvVIgXm0EMYzrdKvNRqpfIvN+YLZbIlGh3bU
XscZKEo/asqH6Rq4Po6ArgJFuVlgh7f9/YA0Ed4WdNnH5PHjd+3yBkdBVAZzX+V8
lAntWW14YMATC3qyX8mzcJgAkmCVQrumzq6phq1+aYfODLcBgmkpP5HT4wLEW0Nv
bBpHyyGxLCxKUwcWUgxUTSmo97aslU2gVlLhG5dmot/Bf+oOQzkuGTPRc6OMo0yx
Ky4=
-----END CERTIFICATE-----
"""


def get_pub_key(cert_str):
    x509 =OpenSSL.crypto.load_certificate(OpenSSL.crypto.FILETYPE_PEM,cert_str )
    pubkey=x509.get_pubkey()
    pub_str = OpenSSL.crypto.dump_publickey(OpenSSL.crypto.FILETYPE_PEM, pubkey).decode("utf-8")
    print(pub_str)
    print("-------------------------")
    bt = OpenSSL.crypto.dump_publickey(OpenSSL.crypto.FILETYPE_ASN1, pubkey)
    print(len(bt),bt)
    encryptor = sha256()
    encryptor.update(bt)  
    hashCode = encryptor.hexdigest()
    print(hashCode)

    return hashCode

if get_pub_key(cert_text) == res_str:
    print("Cchengg ")
else:
    print("失败")