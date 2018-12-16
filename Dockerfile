FROM clojure
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN lein with-profile uberjar/all uberjar
