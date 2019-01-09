#!/bin/bash

export COMPOSE_CONVERT_WINDOWS_PATHS=1


dc(){
  # export COMPOSE_CONVERT_WINDOWS_PATHS=1
  # docker-compose \
  #   -f ./clojure.yml \
  #   -f ./datomic.yml \
  #   -f ./ide.yml \
  #   "$@"

      docker-compose \
    -f ./clj.yml \
    -f ./datomic.yml \
    "$@"
}

datomic(){
  # export COMPOSE_CONVERT_WINDOWS_PATHS=1
  docker-compose \
    -f ./datomic.yml \
    "$@"
}

clj(){
  # export COMPOSE_CONVERT_WINDOWS_PATHS=1
  docker-compose \
    -f ./clj.yml \
    "$@"
}

cljup(){
  clj up -d --build
}

cljdown(){
  clj down
}

dc_ide(){
  # export COMPOSE_CONVERT_WINDOWS_PATHS=1
  docker-compose \
    -f ./ide.yml \
    "$@"
}

dc_theia(){
  # export COMPOSE_CONVERT_WINDOWS_PATHS=1
  docker-compose \
    -f ./theia.yml \
    "$@"
}

dc_metabase(){
  # export COMPOSE_CONVERT_WINDOWS_PATHS=1
  docker-compose \
    -f ./metabase.yml \
    "$@"
}

dc_swarmpit(){
  # export COMPOSE_CONVERT_WINDOWS_PATHS=1
  docker stack deploy -c swarmpit.yml swarmpit
}
st_swarmpit_rm(){
  # export COMPOSE_CONVERT_WINDOWS_PATHS=1
  docker stack rm swarmpit
}


up(){
  dc up -d --build
  # emacs_up
}

down(){
  dc down
  # emacs_down
}

prune(){
  down
  docker system prune -a
  docker volume prune

  # docker image prune # dangling
  # docker image prune -a # all unused
  # docker container  prune

}

cljsh(){
  # docker-compose exec -e TERM clojure sh
  docker-compose -f clj.yml exec clj sh
}

clj_lein(){
  # docker-compose exec -e TERM clojure sh
  docker-compose -f clj.yml exec clj-lein sh
}


emacs_up(){
  # docker run -d \
  # --name x11-bridge \
  # -e MODE="tcp" \
  # -e XPRA_HTML="yes" \
  # -e DISPLAY=:14 \
  # -p 10000:10000 \
  # jare/x11-bridge

  docker run -d \
  --name x11-bridge \
  -e MODE="tcp" \
  -e XPRA_HTML="yes" \
  -e DISPLAY=:14 \
  --net=host \
  jare/x11-bridge

  docker run -d \
  --name emacs-1 \
  --volumes-from x11-bridge \
  -e DISPLAY=:14 \
  jare/emacs emacs

  # docker run -d \
  # --name emacs-2 \
  # --volumes-from x11-bridge \
  # -e DISPLAY=:14 \
  # jare/emacs emacs
}

emacs_down(){
  docker stop emacs-1
  # docker stop emacs-2
  docker stop x11-bridge
  docker rm emacs-1
  # docker rm emacs-2
  docker rm x11-bridge
}

winpty_emacs(){
  DISPLAY=10.0.75.1:0.0
  startxwin -- -listen tcp &
  xhost + 10.0.75.1
  winpty docker run -ti --name emacs\
  -e DISPLAY="$DISPLAY"\
  -e UNAME="emacser"\
  -e GNAME="emacsers"\
  -e UID="1000"\
  -e GID="1000"\
  -v C:\code\conj\emacs.d:/home/emacs/.emacs.d\
  -v C:\code\conj:/mnt/workspace\
  jare/emacs emacs
}


theia(){
  DIR=$(pwd)
  echo $DIR
  docker run -it -p 3000:3000 -v "/c/code/conj:/home/project:cached" theiaide/theia:next
}



repl(){
  # lein repl :headless :host 0.0.0.0 :port 35543
  cd clj-lein
  lein repl :start :host 0.0.0.0 :port 35543
}

conn(){
  lein repl :connect 0.0.0.0:35543
}


transactor(){
  datomic-free-0.9.5703/bin/transactor config/samples/free-transactor-template.properties # Replace path/to/ with the path to the file.
}

import_codeq(){
  cd codeq
  java -server -Xmx1g -jar target/codeq-0.1.0-SNAPSHOT-standalone.jar datomic:free://localhost:4334/git
  cd ..
}

datomic_rest(){
  cd datomic-free-0.9.5703
  bin/rest -p 8080 free datomic:free://localhost:4334/
  cd ..
}

# emacs(){
#   docker run -d \
#  --name x11-bridge \
#  -e MODE="tcp" \
#  -e XPRA_HTML="yes" \
#  -e DISPLAY=:14 \
#  -p 10000:10000 \
#  jare/x11-bridge

# docker run -d \
#  --name emacs-1 \
#  --volumes-from x11-bridge \
#  -e DISPLAY=:14 \
#  jare/emacs emacs

# docker run -d \
#  --name emacs-2 \
#  --volumes-from x11-bridge \
#  -e DISPLAY=:14 \
#  jare/emacs emacs
# }

"$@"