# PT Batch Client for Public Transport Enabler

[Public Transport Enabler](https://github.com/schildbach/public-transport-enabler) is a library for accessing public transport information from several online providers.

# Purpose

This tool allows you to run a bunch of queries (defined in a CSV file) agains a PTE provider (like bahn.de) and save results in CSV in JSON files.

# Usage

First you need to build

Three phases:

* Generate mappings of stops from the GTFS file to locations from the PTE provider 
* Create or generate queries you'd like to run
* Execute queries via PTE provider and save results

## Generate stop/location mappings

First we need to find locations from the PTE provider which correspond to the stops from the GTFS file. This is done using the coordinate lookup.
Since there may be no exact match, we're using some max distance - we start with 50m and go up to 1000m with 50m increments.
If there are more than one  locations within the given distance, we'll take the closes one.

To generate stop/location mappings, execute the following from the command line:

```
java -cp target/pt-batch-pte-<VERSION>.jar org.hisrc.ptbatch.pte.app.GenerateStopLocationMappings -g <GTFS_FILE> -slm-csv <STOP_LOCATION_MAPPINGS_CSV_FILE>
```

For example:

```
java -cp target/pt-batch-pte-1.0.0-SNAPSHOT.jar org.hisrc.ptbatch.pte.app.GenerateStopLocationMappings -g files/rnv.zip -slm-csv files/rnv-stop-location-mappings.csv
```

You'll get a file like:

```
stop_id,stop_name,stop_lon,stop_lat,location_type,location_id,location_lon,location_lat,location_place,location_name,location_products,distance
202935,Ebertpark/Fichtestr.,8.42054,49.4917,STATION,507997,8420906,49491721,"Ludwigshafen a","Friesenheim Ebertpark/Fichtestraße",TRAM;BUS,50
243005,Landgüterhalle,8.450211,49.496218,STATION,508152,8450310,49496117,Mannheim,Landgüterhalle,BUS,50
...
```

The `stop_*` columns correspond to stops from you GTFS file.  
The `location_*` columns correspond to PTE locations retrieved from the PTE provider.  
The `distance` is the maximum distance used for the lookup. For example a `distance` of `650` would mean that the location lies between 600 and 650m from the stop.

## Create or generate queries

To generate queries, run:

```
java -cp target/pt-batch-pte-<VERSION>.jar org.hisrc.ptbatch.pte.app.GenerateQueries -slm-csv <STOP_LOCATION_MAPPINGS_CSV_FILE> -c <COUNT> -s <START_DATE> -e <END_DATE> -q-csv <QUERIES_CSV_FILE> -q-json <QUERIES_JSON_FILE>
```

For example:

```
java -cp target/pt-batch-pte-1.0.0-SNAPSHOT.jar org.hisrc.ptbatch.pte.app.GenerateQueries -slm-csv files/rnv-stop-location-mappings.csv -c 1000 -s 2016-10-01 -e 2016-10-11 -q-csv files/rnv-queries.csv -q-json files/rnv-queries.json
```

This generates a timestamp/from stop/to stop tuples like:

```
date_time,from_id,from_name,from_lon,from_lat,to_id,to_name,to_lon,to_lat
2016-10-01T00:33:00,604106,"MA Rathaus/REM",8.463582,49.489551,211206,Heinrich-Ries-Halle,8.426591,49.487647

```

Stops are taken from the stop/location mapping file.
The tool only uses mappings with distance between the stop and the location <= 50m.
Timestamp is chosen randomly between the start and the end date, rounded to whole minutes.
Pair of stops are chosen at random, it is guaranteed that from and to stops are not the same.

# Building the tool

First you need to build the [Public Transport Enabler](https://github.com/schildbach/public-transport-enabler) (this requires gradle) and install it into your local Maven repostory.

```
git clone https://github.com/schildbach/public-transport-enabler.git
cd public-transport-enabler
gradle clean build
mvn install:install-file -Dfile=enabler/build/libs/enabler.jar -DgroupId=de.schildbach.pte -DartifactId=public-transport-enabler -Dversion=1.0.0 -Dpackaging=jar
```

Now you can build this tool:

```
mvn clean install
```

# License

This module is licensed under GPLv3.