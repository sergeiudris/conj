#!/bin/bash


echo hello

cd codeq
lein uberjar
java -server -Xmx1g -jar target/codeq-0.1.0-SNAPSHOT-standalone.jar datomic:free://datomicdbfree:4334/codeq
cd ../clojure
echo $pwd
cp ../codeq/target/codeq-0.1.0-SNAPSHOT-standalone.jar ./
ls
java -server -Xmx1g -jar codeq-0.1.0-SNAPSHOT-standalone.jar datomic:free://datomicdbfree:4334/git
tail -f /dev/null
"$@"
