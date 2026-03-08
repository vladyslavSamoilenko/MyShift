FROM eclipse-temurin:21-jre-alpine

WORKDIR /srv
COPY target/*.jar .
COPY docker/backend.sh .

RUN sed -i 's/\r$//' backend.sh && \
    chmod +x backend.sh

EXPOSE 8087

ENTRYPOINT ["./backend.sh"]