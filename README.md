reddit-ctf
==========

Just a quick map overlay so I can know where I am when playing capture the flag.

# Setup

This is a maven project so basically all the maven things apply.  The only extra thing you need to do is install gwt-maps 1.1.1 (gwt-maps 1.0.4 is in Maven but is too old to work).

Here's how to install gwt-maps 1.1.1

    wget http://gwt-google-apis.googlecode.com/files/gwt-maps-1.1.1.zip
    unzip gwt-maps-1.1.1.zip
    mvn install:install-file -Dfile=gwt-maps-1.1.1/gwt-maps.jar -DgroupId=com.google.gwt.google-apis -Dversion=1.1.1 -DartifactId=gwt-maps -Dpackaging=jar
    rm -rf gwt-maps-1.1.1.zip gwt-maps-1.1.1 #Clean up.

# Debugging (AKA Running in Hosted Mode)

    mvn gwt:run