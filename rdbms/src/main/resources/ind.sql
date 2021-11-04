--liquibase formatted sql

--changeset kgvasilyev:init

CREATE INDEX id_ver ON account1 (id, version);