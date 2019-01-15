#!/bin/bash


echo hello

import_codeq(){
    java -server -Xmx1g -jar target/codeq-0.1.0-SNAPSHOT-standalone.jar datomic:free://datomicdbfree:4334/codeq
}

import_clojure(){
    java -server -Xmx1g -jar codeq-0.1.0-SNAPSHOT-standalone.jar datomic:free://datomicdbfree:4334/git
}

cd codeq
lein uberjar
import_codeq &
# java -server -Xmx1g -jar target/codeq-0.1.0-SNAPSHOT-standalone.jar datomic:free://datomicdbfree:4334/codeq
cd ../clojure
# echo $pwd
cp ../codeq/target/codeq-0.1.0-SNAPSHOT-standalone.jar ./
import_clojure &

# ls
# java -server -Xmx1g -jar codeq-0.1.0-SNAPSHOT-standalone.jar datomic:free://datomicdbfree:4334/git




tail -f /dev/null
"$@"