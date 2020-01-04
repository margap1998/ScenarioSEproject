#!/bin/bash
pushd target &> /dev/null
java -jar scenario-quality-checker-0.5.jar
popd &> /dev/null
