CREATE TABLE "LOG"."Server_Log"
(
    "Time"    TIMESTAMP(6) NULL DEFAULT NULL,
    "Type"    VARCHAR(30)       DEFAULT NULL,
    "Content" VARCHAR(300)      DEFAULT NULL
);

CREATE TABLE "LOG"."Client_Log"
(
    "Time"    TIMESTAMP(6) NULL DEFAULT NULL,
    "Type"    VARCHAR(30)       DEFAULT NULL,
    "Content" VARCHAR(300)      DEFAULT NULL
);