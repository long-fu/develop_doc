
# file_uploader.py
from minio import Minio
from minio.error import S3Error
import json
import os

def main():
    # Create a client with the MinIO server playground, its access key
    # and secret key.
    client = Minio(
        "119.23.231.199:9000",
        access_key="uS3k1G3yxa7NXTvh",
        secret_key="F4X1wp0JG7dz6V6kBWo14n7n8mzogLol",
        secure = False
    )
    # events = client.listen_bucket_notification("test")
    # events = client.listen_bucket_notification('my-bucket', 'my-prefix/',
    #                                        '.my-suffix',
    #                                        ['s3:ObjectCreated:*',
    #                                         's3:ObjectRemoved:*',
    #                                         's3:ObjectAccessed:*'])
    # for event in events:
    #     print(event)

    # bs = client.list_buckets()
    # for i in bs:
    #     print(bs)
    
    # os = client.list_objects("test")
    # for i in os:
    #     print(i)

    # config = client.get_bucket_tags("test")
    # print(config)

    # info = client.stat_object("test","ec-manager-0419.tar",extra_query_params={"name":"h"})
    # dic_obj = info.__dict__
    # print(dic_obj)
    # print(json.dumps(dic_obj))
    # print(json.load(info))
    # print(json.dumps(info))
    
    # return
    
    # Make 'asiatrip' bucket if not exist.
    found = client.bucket_exists("test")
    if not found:
        client.make_bucket("test")
    else:
        print("Bucket 'test' already exists")

    # Upload '/home/user/Photos/asiaphotos.zip' as object name
    # 'asiaphotos-2015.zip' to bucket 'asiatrip'.
    client.fput_object(
        "test", "minio/test.yaml", "/home/haoshuai/code/develop_doc/minio/test.yaml",
    )

    # 上传文件夹
    # folder_path = ""
    # files = os.listdir(folder_path)

    print(
        "'/home/haoshuai/code/develop_doc/ec-manager.tar' is successfully uploaded as "
        "object 'ec-manager-0419.tar' to bucket 'test'."
    )

allfiles = []

def getAllFiles(path, path_1):
    childFiles = os.listdir(path)
    for file in childFiles:
        filepath = os.path.join(path, file)
        if os.path.isdir(filepath):
            getAllFiles(filepath, path_1 + file + "/")
        else:
            # 打印出完整的绝对路径
            print(path_1 + file)
    return

if __name__ == "__main__":
    folder_path = "/home/haoshuai/code/develop_doc/minio"
    rec = getAllFiles(folder_path,"")
    # print(rec)
    # for j in rec:
    #     if j == None:
    #         break
    #     print(j)
    # files = os.listdir(folder_path)
    # for file in files :
    #     print(file)
    # try:
    #     main()
    # except S3Error as exc:
    #     print("error occurred.", exc)