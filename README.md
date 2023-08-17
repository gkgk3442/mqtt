```sh
sudo docker run -d \
--restart=always \
--name=mqtt \
-p 1883:1883 \
-p 9001:9001 \
-v /etc/timezone:/etc/timezone:ro \
-v /etc/localtime:/etc/localtime:ro \
eclipse-mosquitto
```


```sh
vi /mosquitto/config/mosquitto.conf
```


```sh
persistence false
allow_anonymous true
connection_messages true
log_type all
listener 1883
```
