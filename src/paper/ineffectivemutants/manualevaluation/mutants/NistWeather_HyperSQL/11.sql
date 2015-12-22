-- 11
-- PKCColumnA
-- ListElementAdder with ChainedSupplier with PrimaryKeyConstraintSupplier and PrimaryKeyColumnsWithAlternativesSupplier - Added RAIN_I

CREATE TABLE "Station" (
	"ID"	INT	PRIMARY KEY,
	"CITY"	CHAR(20),
	"STATE"	CHAR(2),
	"LAT_N"	INT	NOT NULL,
	"LONG_W"	INT	NOT NULL,
	CHECK ("LAT_N" BETWEEN 0 AND 90),
	CHECK ("LONG_W" BETWEEN -180 AND 180)
)

CREATE TABLE "Stats" (
	"ID"	INT	 REFERENCES "Station" ("ID"),
	"MONTH"	INT	NOT NULL,
	"TEMP_F"	INT	NOT NULL,
	"RAIN_I"	INT	NOT NULL,
	PRIMARY KEY ("ID", "MONTH", "RAIN_I"),
	CHECK ("MONTH" BETWEEN 1 AND 12),
	CHECK ("TEMP_F" BETWEEN 80 AND 150),
	CHECK ("RAIN_I" BETWEEN 0 AND 100)
)

