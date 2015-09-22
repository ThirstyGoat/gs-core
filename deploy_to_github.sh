#!/bin/sh

# Script for the deployment of releases to github 
#
# Expecting a "settings-mvvmfx.xml" file in maven_home with the authentication configuration for the github repository.
#
# The settings file should look something like this:
#
# <!-- NOTE: MAKE SURE THAT settings.xml IS NOT WORLD READABLE! -->
# <settings>
#   <servers>
#     <server>
#       <id>github</id>
#       <username>YOUR-USERNAME</username>
#       <password>YOUR-PASSWORD</password>
#     </server>
#   </servers>
# </settings>

mvn clean deploy -DskipTests=true -Pdeploy-to-github --settings ~/.m2/settings-mvvmfx.xml
