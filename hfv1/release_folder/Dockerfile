# Pull base image  
FROM docker.io/openjdk


# Expose ports.
EXPOSE 8888

COPY ./release_jars /release_jars

# Define default command.
ENTRYPOINT java -Djava.ext.dirs=/release_jars com.qingxin.server.JettyServer