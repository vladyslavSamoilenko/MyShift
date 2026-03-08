#!/bin/sh

PROFILE=${PROFILE:-local-idea}

echo "Starting service with profile: $PROFILE"
exec java -jar /srv/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=$PROFILE