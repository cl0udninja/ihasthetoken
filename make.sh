#!/bin/sh

cd ihasthetoken_ui
yarn install
yarn test
yarn build
cd ../ihasthetoken
mvn clean install
java -Djava.security.egd=file:/dev/./urandom -jar target/ihasthetoken-0.0.1-SNAPSHOT.jar