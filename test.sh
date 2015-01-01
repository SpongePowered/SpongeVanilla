#!/bin/sh

[ -d TestServer ] || mkdir TestServer

cd TestServer

java -Xmx512M -jar ../Granite/target/Granite-*.jar
