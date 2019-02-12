FROM ubuntu:18.04


RUN apt-get update && \
    apt-get install -y sudo && \
    apt-get install -y curl && \
    apt-get install -y wget && \
    apt-get install -y git-core && \
    apt-get install -y gpg && \
    apt-get install -y rlwrap && \
    apt-get install -y software-properties-common && \
    apt-get install -y build-essential  uuid-dev libncurses5-dev libncursesw5-dev libx11-dev && \
    apt-get install -y lsb-release build-essential libssl-dev python

RUN git clone https://github.com/cisco/ChezScheme

RUN cd ChezScheme && \
    git checkout 5aba39c40b46cee61a388d71ef4e5eebfa717108 && \
    ./configure && \
    sudo make install

RUN mkdir -p /opt/code/app
WORKDIR /opt/code/app
