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

RUN apt-get install -y openjdk-11-jdk

# RUN curl -O https://download.clojure.org/install/linux-install-1.9.0.397.sh && \
#     chmod +x linux-install-1.9.0.397.sh && \
#     sudo ./linux-install-1.9.0.397.sh

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
  # && gpg --batch --keyserver pool.sks-keyservers.net --recv-key 2B72BF956E23DE5E830D50F6002AF007D1A7CC18 \
  # && echo "Verifying Jar file signature ..." \
  # && gpg --verify leiningen-$LEIN_VERSION-standalone.zip.asc \
  && rm leiningen-$LEIN_VERSION-standalone.zip.asc \
  && mkdir -p /usr/share/java \
  && mv leiningen-$LEIN_VERSION-standalone.zip /usr/share/java/leiningen-$LEIN_VERSION-standalone.jar

ENV PATH=$PATH:$LEIN_INSTALL
ENV LEIN_ROOT 1


RUN mkdir -p /opt/code/app
WORKDIR /opt/code/app

# COPY .  /opt/code/app
COPY project.clj  /opt/code/app
RUN lein install

EXPOSE 35543
EXPOSE 4000
EXPOSE 8890


# CMD ["bash","c", "nrepl_clj"]

