
FROM clj AS clj-image

FROM datomic-pro

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

RUN curl -O https://download.clojure.org/install/linux-install-1.10.0.411.sh && \
    chmod +x linux-install-1.10.0.411.sh && \
    sudo ./linux-install-1.10.0.411.sh

RUN wget https://s3.amazonaws.com/mbrainz/datomic-mbrainz-1968-1973-backup-2017-07-20.tar -O mbrainz.tar && \
    tar -xvf mbrainz.tar

# ADD ./mbrainz.sh $DATOMIC_HOME
COPY . $DATOMIC_HOME
COPY --from=clj-image /root/.m2/ /root/.m2/


# COPY ./deps.edn /opt/code/app/
RUN clojure -Stree

EXPOSE 8890