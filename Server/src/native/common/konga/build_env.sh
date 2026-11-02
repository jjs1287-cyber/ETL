# Sets CPATH, LIBRARY_PATH for building the common projects,
#
# source build_env.sh

# Edit this for your environment if needed.

PWD=`pwd`
ROOT=${PWD%"/common/cpp/konga"}

# Add the common/cpp directory to the path to search for include files.
if [ -z "$CPATH" ]; then
    CPATH=${PWD%"/konga"}
else
    CPATH=$CPATH:${PWD%"/konga"}
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
#if [ "$platform" = "Linux" ]; then
#    export LD_FLAGS_EXE="-Wl,-rpath $ROOT/lib/konga"
#    echo "Use this linker flag for executables: $LD_FLAGS_EXE"
#fi

# Solaris needs -lsocket.
LIB_SOCKET=
if [ $platform == "SunOS" ]; then
    LIB_SOCKET="-lsocket"
    echo "Linking to socket library with $LIB_SOCKET"
fi
export LIB_SOCKET

# Don't edit below this line - set OPENSSL_HOME or SAMBA_HOME to tell
# gcc where to find OpenSSL and Samba headers/libraries.
#
if [ -z "$OPENSSL_HOME" ]; then
    OPENSSL_INCLUDE="/usr/include"
    OPENSSL_LIBDIR=
else
    OPENSSL_INCLUDE="$OPENSSL_HOME/include"
    OPENSSL_LIBDIR="-L$OPENSSL_HOME/lib"
    echo "Looking for OpenSSL headers in $OPENSSL_INCLUDE"
    echo "Linking to OpenSSL with: $OPENSSL_LIBDIR"
fi
export OPENSSL_INCLUDE
export OPENSSL_LIBDIR

if [ -z "$SAMBA_HOME" ]; then
    SAMBA_INCLUDE="/usr/include"
    SAMBA_LIBDIR=
else
    SAMBA_INCLUDE="$SAMBA_HOME/include"
    SAMBA_LIBDIR="-L$SAMBA_HOME/lib"
    echo "Looking for Samba headers in $SAMBA_INCLUDE"
    echo "Linking to Samba with: $SAMBA_LIBDIR"
fi
export SAMBA_INCLUDE
export SAMBA_LIBDIR
