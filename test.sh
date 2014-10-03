#!/bin/sh

[ -d TestServer ] || mkdir TestServer

cd TestServer

[ -f minecraft_server.jar ] || wget https://s3.amazonaws.com/Minecraft.Download/versions/1.8/minecraft_server.1.8.jar -O minecraft_server.jar

java -Xmx512M -jar ../Granite/target/Granite-*.jar
