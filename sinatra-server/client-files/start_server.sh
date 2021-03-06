#!/bin/bash

# Starts the sinatra lod-server for Zeidon projects.

DEBUG_FLAGS=
DEBUG_PORT=8000
SINATRA_PORT=4568

while getopts "cdxps:" option; do
    case $option in
        c)
            echo "Removing .tmpclasspath"
            rm .tmpclasspath > /dev/null
            ;;
        d)
            echo "Starting server in debug mode"
            DEBUG_FLAGS="-Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=n"
            ;;
        p)
            echo "Using DEBUG_PORT=$OPTARG"
            DEBUG_PORT="$OPTARG"
            ;;
        x)
            echo "Starting with debug in suspend mode"
            DEBUG_FLAGS="-Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=y"
            ;;
        s)
            echo "Starting Sinatra on port $OPTARG"
            SINATRA_PORT="$OPTARG"
            ;;
    esac
done

export SINATRA_PORT

# This tells Zeidon to look in current directory for .INI file.
export ZEIDON_HOME=`pwd`

#
# Create a temp file with the classpath using maven.
#

# If the temp file exists and is > 24 hours old delete it so we can recreate it.
if [ -f .tmpclasspath ]; then
    if test "`find .tmpclasspath -mmin +1440`"; then
        echo ".tmpclasspath is old and will be regenerated"
        rm .tmpclasspath
    fi
fi

if [ -f .tmpclasspath ]; then
    echo "Using cached classpath (.tmpclasspath)"
else
    echo "Getting classpath..."
    mvn dependency:build-classpath -Dmdep.outputFile=".tmpclasspath" > /dev/null

    # Prepend classpath with current directory to pick up jar-bootstrap.rb
    echo -e ".:$(cat .tmpclasspath)" > .tmpclasspath
fi

cp=`cat .tmpclasspath`
java -cp "$cp" $DEBUG_FLAGS org.jruby.JarBootstrapMain  | tee /tmp/lod-server.log

