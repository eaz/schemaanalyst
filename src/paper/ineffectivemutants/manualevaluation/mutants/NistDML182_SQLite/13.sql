-- 13
-- FKCColumnPairR
-- ListElementRemover with ChainedSupplier with ForeignKeyConstraintSupplier and ForeignKeyColumnSupplier - Removed Pair(CODE13, CODE13)

CREATE TABLE "ID_CODES" (
	"CODE1"	INT,
	"CODE2"	INT,
	"CODE3"	INT,
	"CODE4"	INT,
	"CODE5"	INT,
	"CODE6"	INT,
	"CODE7"	INT,
	"CODE8"	INT,
	"CODE9"	INT,
	"CODE10"	INT,
	"CODE11"	INT,
	"CODE12"	INT,
	"CODE13"	INT,
	"CODE14"	INT,
	"CODE15"	INT,
	PRIMARY KEY ("CODE1", "CODE2", "CODE3", "CODE4", "CODE5", "CODE6", "CODE7", "CODE8", "CODE9", "CODE10", "CODE11", "CODE12", "CODE13", "CODE14", "CODE15")
)

CREATE TABLE "ORDERS" (
	"CODE1"	INT,
	"CODE2"	INT,
	"CODE3"	INT,
	"CODE4"	INT,
	"CODE5"	INT,
	"CODE6"	INT,
	"CODE7"	INT,
	"CODE8"	INT,
	"CODE9"	INT,
	"CODE10"	INT,
	"CODE11"	INT,
	"CODE12"	INT,
	"CODE13"	INT,
	"CODE14"	INT,
	"CODE15"	INT,
	"TITLE"	VARCHAR(80),
	"COST"	NUMERIC(5, 2),
	FOREIGN KEY ("CODE1", "CODE2", "CODE3", "CODE4", "CODE5", "CODE6", "CODE7", "CODE8", "CODE9", "CODE10", "CODE11", "CODE12", "CODE14", "CODE15") REFERENCES "ID_CODES" ("CODE1", "CODE2", "CODE3", "CODE4", "CODE5", "CODE6", "CODE7", "CODE8", "CODE9", "CODE10", "CODE11", "CODE12", "CODE14", "CODE15")
)

