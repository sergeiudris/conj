#!/bin/bash


echo hello

cd codeq
lein uberjar
java -server -Xmx1g -jar target/codeq-0.1.0-SNAPSHOT-standalone.jar datomic:free://datomicdbfree:4334/codeq
tail -f /dev/null
"$@"