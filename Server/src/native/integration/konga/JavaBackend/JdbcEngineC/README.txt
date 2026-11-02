Axis2/C installation instructons for use with ETLForge.

Currently we are using Axis2/C version 1.6.0.

Windows
-------

Use pre-build binaries.

The following DLLs are needed:

axiom.dll
axis2_engine.dll
axis2_parser.dll
axutil.dll
guththila.dll
neethi.dll

The TransformationEngine needs to link to the following lib-files:
axutil.lib
axiom.lib
axis2_engine.lib


Linux
-----
Compile from source since pre-built binaries are compiled agains glibc 2.4:

AXIS2C_HOME=/usr/local/axis2/c/axis2c-1.6.0-linux
export AXIS2C_HOME

./configure --prefix=${AXIS2C_HOME}
make

su
make install

Files will be installed in $AXIS2C_HOME


Common
------

Some of the configuration files from the axis2c home directory needs to be in $ETLFORGE_HOME/axis2

These will be put in svn or copied by Makefiles (on Linux).

