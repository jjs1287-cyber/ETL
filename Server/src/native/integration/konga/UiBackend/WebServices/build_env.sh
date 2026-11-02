# This file can be sourced to set the environment variables needed to 
# compile this branch.
#
# E.g
#
# source build_env.sh

# Edit this for your environment if needed.

# Load common local build environment.
. ../../../../../local_build_env.sh

PWD=`pwd`
ROOT=${PWD%"/integration/cpp/konga/UiBackend/WebServices"}

# Add common and integration.
if [ -z "$CPATH" ]; then
    CPATH="$ROOT/common/cpp:$ROOT/integration/cpp"
else
    CPATH="$CPATH:$ROOT/common/cpp:$ROOT/integration/cpp"
fi
export CPATH
echo "CPATH=$CPATH"

# Add this to the path to search for libraries.
if [ -z "$LIBRARY_PATH" ]; then
    LIBRARY_PATH=/usr/local/etlforge/lib
else
    LIBRARY_PATH=$LIBRARY_PATH:/usr/local/etlforge/lib
fi
export LIBRARY_PATH
echo "LIBRARY_PATH=$LIBRARY_PATH"
