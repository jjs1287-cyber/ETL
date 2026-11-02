# Sets CPATH, LIBRARY_PATH for building the Db projects.
#
# source build_env.sh

# Edit this for your environment if needed.

PWD=`pwd`
ROOT=${PWD%"/integration/cpp/konga/Db"}

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

# On Linux, the linker needs this option to find the compiled libraries.
platform=`uname`
if [ "$platform" = "Linux" ]; then
    export LD_RUN_PATH=$LD_RUN_PATH:$ROOT/lib/konga:/usr/local/etlforge/lib
    echo LD_RUN_PATH=$LD_RUN_PATH
fi
