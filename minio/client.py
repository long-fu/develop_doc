
# file_uploader.py
from minio import Minio
from minio.error import S3Error


def main():
    # Create a client with the MinIO server playground, its access key
    # and secret key.
    client = Minio(
        "119.23.231.199:9000",
        access_key="uS3k1G3yxa7NXTvh",
        secret_key="F4X1wp0JG7dz6V6kBWo14n7n8mzogLol",
        secure = False
    )

    # Make 'asiatrip' bucket if not exist.
    found = client.bucket_exists("test")
    if not found:
        client.make_bucket("test")
    else:
        print("Bucket 'test' already exists")

    # Upload '/home/user/Photos/asiaphotos.zip' as object name
    # 'asiaphotos-2015.zip' to bucket 'asiatrip'.
    client.fput_object(
        "test", "ec-manager-0419.tar", "/home/haoshuai/code/develop_doc/ec-manager.tar",
    )
    print(
        "'/home/haoshuai/code/develop_doc/ec-manager.tar' is successfully uploaded as "
        "object 'ec-manager-0419.tar' to bucket 'test'."
    )


if __name__ == "__main__":
    try:
        main()
    except S3Error as exc:
        print("error occurred.", exc)