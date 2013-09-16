#!/bin/bash

CLASSPATH='lib/*:build/'

while getopts a:c:t:p: option
do
	case "${option}"
		in
		c) CASESTUDY=${OPTARG};;
		t) TRIALS=${OPTARG};;
		a) APPROACH=${OPTARG};;
		p) PIPELINE=${OPTARG};;
	esac
done

if [ -z $CASESTUDY ] || [ -z $TRIALS ] || [ -z $APPROACH ] ; then
	echo "Experiment failed - requires -c CASESTUDY -t TRIALS -a APPROACH"
	exit 1
fi

java -Xmx3G -cp $CLASSPATH org.schemaanalyst.mutation.analysis.util.GenerateResultsFromGenerator $CASESTUDY

for (( t=1; t<=$TRIALS; t++ )) do
	if [ -z $PIPELINE ] ; then
		java -Xmx3G -cp $CLASSPATH $APPROACH $CASESTUDY $t
	else
		java -Xmx3G -cp $CLASSPATH $APPROACH $CASESTUDY $t '--mutationPipeline='$PIPELINE
	fi
done
