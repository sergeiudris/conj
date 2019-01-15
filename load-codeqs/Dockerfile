FROM clj


RUN git clone https://github.com/Datomic/codeq
RUN git clone https://github.com/clojure/clojure


COPY ./load.sh $DATOMIC_HOME

# RUN wget https://s3.amazonaws.com/mbrainz/datomic-mbrainz-1968-1973-backup-2017-07-20.tar -O mbrainz.tar && \
#     tar -xvf mbrainz.tar
