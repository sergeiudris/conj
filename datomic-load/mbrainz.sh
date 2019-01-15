#!/bin/bash

# wget https://s3.amazonaws.com/mbrainz/datomic-mbrainz-1968-1973-backup-2017-07-20.tar -O mbrainz.tar
# tar -xvf mbrainz.tar
bin/datomic restore-db file:///opt/datomic-pro/mbrainz-1968-1973 datomic:dev://datomicdb:4334/mbrainz-1968-1973

"$@"