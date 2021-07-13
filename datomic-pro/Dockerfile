
FROM ubuntu:18.04



RUN apt-get update && \
    apt-get install -y sudo && \
    apt-get install -y curl && \
    apt-get install -y wget && \
    apt-get install -y git-core && \
    apt-get install -y gpg && \
    apt-get install -y rlwrap && \
    apt-get install -y software-properties-common && \
    apt-get install -y unzip curl net-tools lsof


RUN apt-get install -y openjdk-11-jdk

RUN curl -O https://download.clojure.org/install/linux-install-1.10.0.411.sh && \
    chmod +x linux-install-1.10.0.411.sh && \
    sudo ./linux-install-1.10.0.411.sh

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

# ENV DATOMIC_VERSION 0.9.5786  no
# ENV DATOMIC_VERSION 0.9.5561 yes 
# ENV DATOMIC_VERSION 0.9.5783 no
# ENV DATOMIC_VERSION 0.9.5703 no
# ENV DATOMIC_VERSION 0.9.5697 no  https://forum.datomic.com/t/important-security-update-0-9-5697/379
ENV DATOMIC_VERSION 0.9.5661



ENV DATOMIC_HOME /opt/datomic-pro
# ENV DATOMIC_DATA $DATOMIC_HOME/data


WORKDIR /tmp
ADD .credentials /tmp/.credentials

RUN curl -u $(cat /tmp/.credentials) -SL https://my.datomic.com/repo/com/datomic/datomic-pro/$DATOMIC_VERSION/datomic-pro-$DATOMIC_VERSION.zip -o /tmp/datomic.zip \
  && unzip /tmp/datomic.zip -d /opt \
  && mv /opt/datomic-pro-${DATOMIC_VERSION} $DATOMIC_HOME \
  && rm -f /tmp/datomic.zip

# RUN mkdir -p /opt/datomic-pro
WORKDIR $DATOMIC_HOME
ADD config $DATOMIC_HOME/config
ADD c $DATOMIC_HOME

# ADD wait.sh $DATOMIC_HOME


EXPOSE 4334 4335 4336 9000



