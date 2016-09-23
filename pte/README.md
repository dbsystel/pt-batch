# PT Batch Client for Public Transport Enabler

[Public Transport Enabler](https://github.com/schildbach/public-transport-enabler) is a library for accessing public transport information from several online providers.

# Building Public Transport Enabler

```
git clone https://github.com/schildbach/public-transport-enabler.git
cd public-transport-enabler
grade clean build
mvn install:install-file -Dfile=enabler/build/libs/enabler.jar -DgroupId=de.schildbach.pte -DartifactId=public-transport-enabler -Dversion=1.0.0 -Dpackaging=jar
```

# License

This module is licensed under GPLv3.