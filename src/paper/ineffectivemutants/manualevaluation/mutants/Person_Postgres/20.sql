-- 20
-- UCColumnA
-- Added UNIQUE to column last_name in table person

CREATE TABLE "person" (
	"id"	INT	PRIMARY KEY	NOT NULL,
	"last_name"	VARCHAR(45)	UNIQUE	NOT NULL,
	"first_name"	VARCHAR(45)	NOT NULL,
	"gender"	VARCHAR(6)	NOT NULL,
	"date_of_birth"	DATE	NOT NULL,
	CHECK ("gender" IN ('Male', 'Female', 'Uknown'))
)

