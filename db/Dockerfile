
FROM mysql:8.0

WORKDIR /tmp

COPY test_db/*.sql /tmp/
COPY test_db/*.dump /tmp/

COPY test_db/employees.sql /docker-entrypoint-initdb.d/01-employees.sql

ENV MYSQL_ROOT_PASSWORD=example
ENV MYSQL_ROOT_HOST=%
