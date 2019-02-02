FROM clj

# COPY ./core ./core
# ADD . /tmp
# COPY ./deps.edn ./deps.edn



# COPY ./deps.edn /opt/code/app/
# RUN clojure -Stree
COPY project.clj /opt/code/app/
RUN lein deps

EXPOSE 8890
EXPOSE 8080
EXPOSE 8081

