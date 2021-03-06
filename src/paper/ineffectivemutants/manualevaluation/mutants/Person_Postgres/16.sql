-- 16
-- NNCR
-- Removed NOT NULL to column first_name in table person

CREATE TABLE "person" (
	"id"	INT	PRIMARY KEY	NOT NULL,
	"last_name"	VARCHAR(45)	NOT NULL,
	"first_name"	VARCHAR(45),
	"gender"	VARCHAR(6)	NOT NULL,
	"date_of_birth"	DATE	NOT NULL,
	CHECK ("gender" IN ('Male', 'Female', 'Uknown'))
)

