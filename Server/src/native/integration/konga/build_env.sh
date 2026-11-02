# Sets CPATH, LIBRARY_PATH for building the integration projects.
#

# Edit this for your environment if needed.

# Load common local build environments.
. ../../../local_build_env.sh

PWD=`pwd`
ROOT=${PWD%"/integration/cpp/konga"}

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

# Add this to the LD_LIBRARY_PATH.
if [ -z "$LIBRARY_PATH" ]; then
    LD_LIBRARY_PATH=/usr/local/etlforge/lib
else
    LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/etlforge/lib
fi
export LD_LIBRARY_PATH
echo "LD_LIBRARY_PATH=$LIBRARY_PATH"

# On Linux, the linker needs this option to find the compiled libraries.
platform=`uname`
if [ "$platform" = "Linux" ]; then
#    export LD_FLAGS_EXE="-Wl,-rpath $ROOT/lib/konga"
#    echo "Use this linker flag for executables: $LD_FLAGS_EXE"
    export LD_RUN_PATH=$LD_RUN_PATH:$ROOT/lib/konga
    echo LD_RUN_PATH=$LD_RUN_PATH
fi
