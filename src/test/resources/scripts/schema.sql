CREATE SCHEMA PIN_SCHEMA;


CREATE TABLE PIN_SCHEMA.PIN_MNGR (
    MSISDN varchar(50),
    PIN varchar(4),
    UPDT_TSTP timestamp,
    FAIL_ATMPT int
);

ALTER TABLE PIN_SCHEMA.PIN_MNGR ADD PRIMARY KEY (MSISDN, PIN);