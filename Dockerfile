FROM amazoncorretto:17-alpine as builder

WORKDIR /app
COPY build/libs/runner.jar /app/
RUN java -Djarmode=layertools -jar runner.jar extract

##################################

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

COPY localtime /etc/

VOLUME /data

EXPOSE 8089

ENV SERVER_PORT=8089 \
    PROFILE=dev \
	LOG_PATH=/data/logs/cloudlab \
    TZ=Asia/Shanghai

ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]
