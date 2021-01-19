FROM mayan31370/openjdk-alpine-with-chinese-timezone:8-jdk
ENV MYPATH /home/server
#修改镜像时区
#RUN rm -f /etc/localtime \
#&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#&& echo "Asia/Shanghai" > /etc/timezone

WORKDIR $MYPATH
COPY . .
ADD target/auto-test-server-0.0.1-SNAPSHOT.jar app.jar

VOLUME $MYPATH

EXPOSE 8890

ENTRYPOINT ["java","-jar","app.jar"]
