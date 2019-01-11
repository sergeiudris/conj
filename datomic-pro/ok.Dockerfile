# FROM clojure:lein-2.6.1-alpine

# https://github.com/Quantisan/docker-clojure/blob/91f3d11eaf065104ec5d3f2a75a27650ebda2878/target/openjdk-11/debian/lein/Dockerfile
FROM clojure:openjdk-11-lein-2.8.3  
# PRETTY_NAME="Debian GNU/Linux 9 (stretch)"
# NAME="Debian GNU/Linux"
# VERSION_ID="9"
# VERSION="9 (stretch)"
# ID=debian
# HOME_URL="https://www.debian.org/"
# SUPPORT_URL="https://www.debian.org/support"
# BUG_REPORT_URL="https://bugs.debian.org/"


# MAINTAINER Christian Romney "cromney@pointslope.com"

ENV DATOMIC_VERSION 0.9.5561
# ENV DATOMIC_VERSION 0.9.5786
ENV DATOMIC_HOME /opt/datomic-pro-$DATOMIC_VERSION
ENV DATOMIC_DATA $DATOMIC_HOME/data

# RUN apk add --no-cache unzip curl
# RUN hostnamectl
RUN cat /etc/os-release

# RUN apk add --no-cache unzip curl
RUN apt-get install -y  unzip curl

# Datomic Pro Starter as easy as 1-2-3
# 1. Create a .credentials file containing user:pass
# for downloading from my.datomic.com
 ADD .credentials /tmp/.credentials

# 2. Make sure to have a config/ folder in the same folder as your
# Dockerfile containing the transactor property file you wish to use
 RUN curl -u $(cat /tmp/.credentials) -SL https://my.datomic.com/repo/com/datomic/datomic-pro/$DATOMIC_VERSION/datomic-pro-$DATOMIC_VERSION.zip -o /tmp/datomic.zip \
  && unzip /tmp/datomic.zip -d /opt \
  && rm -f /tmp/datomic.zip

 ADD config $DATOMIC_HOME/config

WORKDIR $DATOMIC_HOME
RUN echo DATOMIC HOME: $DATOMIC_HOME
VOLUME $DATOMIC_DATA

EXPOSE 4334 4335 4336 9000
