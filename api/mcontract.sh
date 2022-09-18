#!/bin/bash
sudo kill -9 $(sudo lsof -t -i:8080)
sudo kill -9 $(sudo lsof -t -i:9092)
cd /var/www/digital-signature-api && sudo mvn clean install spring-boot:run
