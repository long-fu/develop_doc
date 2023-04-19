# 开源音乐服务器

[navidrome](https://www.navidrome.org/)
[github](https://github.com/navidrome/navidrome/)

## 文件上传管理

[filebrowser](https://filebrowser.org/)
[github](https://github.com/filebrowser/filebrowser)

```sh
vim docker-compose.yml
```

```yml
version: "3"
services:
  navidrome:
    image: deluan/navidrome:latest
    user: 1000:1000 # should be owner of volumes
    ports:
      - "4533:4533"
    restart: unless-stopped
    environment:
      # Optional: put your config options customization here. Examples:
      ND_SCANSCHEDULE: 1h
      ND_LOGLEVEL: info  
      ND_SESSIONTIMEOUT: 24h
      ND_BASEURL: ""
    volumes:
      - "/home/haoshuai/music/data:/data"
      - "/home/haoshuai/music/folder:/music:ro"
#下面是安装一个可以通过web界面上传音乐文件程序，根据自己需要
  # miniserve:
  #   image: svenstaro/miniserve:latest
  #   depends_on:
  #     - navidrome
  #   ports:
  #     - "4534:8080"
  #   volumes:
  #     - "/home/haoshuai/music/folder:/downloads"  # 冒号左边修改成自己本地的音乐文件夹路径
  #   command: "-r -z -u -q -p 8080 -a admin:123456 /downloads"  # test:test 修改成自己的账号:密码
  #   restart: unless-stopped
  filebrowser:
    image: filebrowser/filebrowser:latest
    depends_on:
      - navidrome
    ports:
      - "8080:80"
    volumes:
      - "/home/haoshuai/music:/srv"
      - "/home/haoshuai/filebrowser/filebrowser.db:/database/filebrowser.db"
      # - "/home/haoshuai/filebrowser/settings.json:/config/settings.json"
    environment:
      PUID: $(id -u)
      PGID: $(id -g)
    # command: "-p 80"
    restart: unless-stopped

        # -v /path/to/root:/srv \
    # -v /path/filebrowser.db:/database.db \
    # -v /path/.filebrowser.json:/.filebrowser.json \

# docker run \
#     -v /home/haoshuai/music:/srv \
#     -v /home/haoshuai/filebrowser/filebrowser.db:/database/filebrowser.db \
#     -e PUID=$(id -u) \
#     -e PGID=$(id -g) \
#     -p 8080:80 \
#     filebrowser/filebrowser:s6
```


```sh

docker-compose up

docker-compose up -d

docker-compose stop

```