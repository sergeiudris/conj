FROM ubuntu:18.04


RUN apt-get update && \
    apt-get install -y sudo && \
    apt-get install -y curl && \
    apt-get install -y wget && \
    apt-get install -y git-core && \
    apt-get install -y gpg && \
    apt-get install -y rlwrap && \
    apt-get install -y software-properties-common

# RUN add-apt-repository -y ppa:webupd8team/java && \
#     apt-get update && \
#     # sudo apt-get install oracle-java8-installer
#     apt-get install -y oracle-java11-set-default

    # apt install -y openjdk-11-jdk
    # apt install -y oracle-java10-set-default
    # add-apt-repository -y ppa:linuxuprising/java && \
    # apt update && \
    # apt install -y oracle-java10-installer && \
    # apt install -y oracle-java10-set-default
    # apt install default-jre

RUN apt-get update && \
    apt-get install -y openjdk-11-jdk

RUN curl -O https://download.clojure.org/install/linux-install-1.10.0.411.sh && \
    chmod +x linux-install-1.10.0.411.sh && \
    sudo ./linux-install-1.10.0.411.sh

# WORKDIR /tmp

WORKDIR /tmp

ENV LEIN_VERSION=2.8.3
ENV LEIN_INSTALL=/usr/local/bin/

# Download the whole repo as an archive
RUN mkdir -p $LEIN_INSTALL \
  && wget -q https://raw.githubusercontent.com/technomancy/leiningen/$LEIN_VERSION/bin/lein-pkg \
  # && echo "Comparing lein-pkg checksum ..." \
  # && echo "019faa5f91a463bf9742c3634ee32fb3db8c47f0 *lein-pkg" | sha1sum -c - \
  && mv lein-pkg $LEIN_INSTALL/lein \
  && chmod 0755 $LEIN_INSTALL/lein \
  && wget -q https://github.com/technomancy/leiningen/releases/download/$LEIN_VERSION/leiningen-$LEIN_VERSION-standalone.zip \
  && wget -q https://github.com/technomancy/leiningen/releases/download/$LEIN_VERSION/leiningen-$LEIN_VERSION-standalone.zip.asc \
  && rm leiningen-$LEIN_VERSION-standalone.zip.asc \
  && mkdir -p /usr/share/java \
  && mv leiningen-$LEIN_VERSION-standalone.zip /usr/share/java/leiningen-$LEIN_VERSION-standalone.jar

ENV PATH=$PATH:$LEIN_INSTALL
ENV LEIN_ROOT 1


ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz


RUN mkdir -p /opt/code/app
RUN mkdir -p /root/.m2
WORKDIR /opt/code/app

ADD ./.m2/settings.xml /root/.m2/

# ONBUILD RUN clojure -Spath
# ONBUILD COPY ./deps.edn /opt/code/app/
# ONBUILD RUN clojure -Stree

EXPOSE 35543
EXPOSE 7888

