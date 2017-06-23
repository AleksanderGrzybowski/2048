#!/usr/bin/env bash


docker run --rm -v `pwd`:/source -u `stat -c "%u:%g" build.gradle` java:8-jdk /bin/bash \
 -c "cd /source && ./gradlew clean test shadowJar"
