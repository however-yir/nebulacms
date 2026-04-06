FROM eclipse-temurin:21-jre as builder

WORKDIR application
ARG JAR_FILE=application/build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

################################

FROM ibm-semeru-runtimes:open-21-jre
LABEL maintainer="Nebula Labs <opensource@nebulacms.io>"
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

ENV JVM_OPTS="" \
    NEBULACMS_WORK_DIR="/root/.nebulacms" \
    HALO_WORK_DIR="/root/.nebulacms" \
    SPRING_CONFIG_LOCATION="optional:classpath:/;optional:file:/root/.nebulacms/" \
    TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

Expose 8090

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} org.springframework.boot.loader.launch.JarLauncher ${0} ${@}"]
