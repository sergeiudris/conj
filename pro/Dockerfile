FROM ubuntu:18.04



RUN apt-get update && \
    apt-get install -y sudo && \
    apt-get install -y curl && \
    apt-get install -y wget && \
    apt-get install -y git-core && \
    apt-get install -y gpg && \
    apt-get install -y rlwrap && \
    apt-get install -y software-properties-common && \
    unzip curl


RUN apt-get install -y openjdk-11-jdk

RUN curl -O https://download.clojure.org/install/linux-install-1.10.0.411.sh && \
    chmod +x linux-install-1.10.0.411.sh && \
    sudo ./linux-install-1.10.0.411.sh

ENV DATOMIC_VERSION 0.9.5786
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

EXPOSE 4334 4335 4336