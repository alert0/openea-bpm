#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${DIR}
# mvn
mvn clean -DskipTests
mvn package -DskipTests

cd target
artifactId=$(ls bpm*.jar |awk -F '[-]' '{print $1"-"$2}')
version=$(ls bpm*.jar |awk -F '[-]' '{print $3}')

# unpack
cd ..
mkdir -p target/dependency
(cd target/dependency; jar -xf ../*.jar)

# docker
docker build -t ${artifactId}:latest -f ./Dockerfile .
docker save -o ./target/${artifactId}-latest-image.tar ${artifactId}:latest
echo "${artifactId}:latest镜像构建成功,已导出到target目录"