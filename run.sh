#!/bin/bash
pushd target &> /dev/null
java -jar scenario-quality-checker-0.1.jar
popd &> /dev/null
