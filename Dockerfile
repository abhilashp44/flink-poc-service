FROM flink:1.19.0-scala_2.12
RUN apt-get update -y && apt-get upgrade -y
RUN mkdir -p $FLINK_HOME/usrlib
RUN rm $FLINK_HOME/lib/log4j-slf4j-impl-2.17.1.jar
COPY target/flink-service-1.0.0.jar $FLINK_HOME/lib/flink-service.jar
COPY target/flink-service-1.0.0.jar $FLINK_HOME/usrlib/flink-service.jar