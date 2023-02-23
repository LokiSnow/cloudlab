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

EXPOSE 8689

ENV SERVER_PORT=8689 \
    PROFILE=dev \
	LOG_PATH=/data/logs/cloudlab \
    TIME_ZONE=Asia/Shanghai

ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]
