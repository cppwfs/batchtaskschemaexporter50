#!/bin/bash

#### First param is the sequence base number for the test
#### Second Param is the insert statements DB TYPE 2 valid values are MARIADB or POSTGRESQL

sequenceNum=$1
if [ -z "$1" ] ; then
sequenceNum=740
fi
export sequenceval=$sequenceNum
export prefixval=BOOT3


#
dbType=$2
if [ -z "$2" ] ; then
dbType=MARIADB
fi

export databasetypeval=$dbType
echo $databasetypeval
mvn -Dtest=SingleJobSingleStepTest test

((++sequenceNum))
((++sequenceNum))

export sequenceval=$sequenceNum
mvn -Dtest=SingleJobMultiStepTest test

((++sequenceNum))
((++sequenceNum))

export sequenceval=$sequenceNum
mvn -Dtest=SingleFailedTaskTest test

((++sequenceNum))
((++sequenceNum))

export sequenceval=$sequenceNum
mvn -Dtest=SingleTaskTest test


((++sequenceNum))
((++sequenceNum))

export sequenceval=$sequenceNum
mvn -Dtest=SingleJobMultiStepFailedSecondStepTest test

((++sequenceNum))
((++sequenceNum))

export sequenceval=$sequenceNum
mvn -Dtest=SingleJobSingleStepJobParamTest test

((++sequenceNum))
((++sequenceNum))

export sequenceval=$sequenceNum
mvn -Dtest=SingleJobMultiStepExitCodeTest test
