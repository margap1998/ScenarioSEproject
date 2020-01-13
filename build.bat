@echo off
set MVN_HOME = C:\apache-maven-3.6.2\bin

call mvn compile
call mvn test
call javadoc:javadoc

@echo on