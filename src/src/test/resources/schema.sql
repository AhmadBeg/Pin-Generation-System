CREATE SCHEMA PIN_SCHEMA;


CREATE TABLE PIN_SCHEMA.PIN_MNGR (
    MSISDN varchar(50) NOT NULL,
    PIN varchar(4) NOT NULL,
    UPDT_TSTP timestamp,
    FAIL_ATMPT int
);

ALTER TABLE PIN_SCHEMA.PIN_MNGR ADD PRIMARY KEY (MSISDN, PIN);