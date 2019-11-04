@echo off
set JSON_LIB=%HOMEDRIVE%%HOMEPATH%\.m2\repository\org\json\json\20190722\json-20190722.jar

pushd target\classes
java -classpath "%JSON_LIB%;." pl.put.poznan.scenario.ScenarioQualityChecker
popd

