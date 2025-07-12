#!/usr/bin/env bash

apt-get update && apt-get install -y libfreetype6 fonts-dejavu-core

./mvnw clean package -DskipTests
