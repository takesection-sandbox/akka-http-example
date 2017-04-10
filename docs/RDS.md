# RDS への接続の確認

## ECS のインスタンスに ssh でログインします

```
ssh -i YOUR_KEY_PEM YOUR_EC2_INSTANCE
```

## 実行中のDockerイメージを確認し、対話モードで接続します

```
docker ps
docker exec -it YOUR_CONTAINER_ID /bin/sh
```

## mysql-client をインストールして、RDS に接続できるか確認します

```
apk --update add mysql-client
mysql -h YOUR_RDS_ENDPOINT -u DBUSER -p
```