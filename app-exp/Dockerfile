FROM clj

# COPY .  /opt/code/app
COPY project.clj  /opt/code/app
RUN lein install

EXPOSE 35543
EXPOSE 7888
EXPOSE 8080

# CMD ["bash","c", "nrepl_clj"]

