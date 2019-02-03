FROM clj

# COPY .  /opt/code/app
COPY project.clj  /opt/code/app
RUN lein install

EXPOSE 35543
EXPOSE 4000
EXPOSE 8890
EXPOSE 8080
EXPOSE 8081

# CMD ["bash","c", "nrepl_clj"]

