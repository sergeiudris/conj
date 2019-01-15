FROM datomic-pro

ADD ./mbrainz.sh $DATOMIC_HOME

RUN wget https://s3.amazonaws.com/mbrainz/datomic-mbrainz-1968-1973-backup-2017-07-20.tar -O mbrainz.tar && \
    tar -xvf mbrainz.tar
