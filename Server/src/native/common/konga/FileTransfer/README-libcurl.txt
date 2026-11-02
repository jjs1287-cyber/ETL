How to build libcurl on windows (VS 2005)
-------------------------------

The source distribution of libcurl needs some tweaking to enable
ssl, compression and sftp support.

Build libSSH2:

* Get the latest libSSH2 source code distribution
  http://www.libssh2.org.

* Open Win32 project libssh2_dll and convert to VS2005.
  We use the libssh2_dll project for building the DLL.
  Add LIBSSH2_HAVE_ZLIB to the preprocessor defs and link to zdll.lib
  instead of libz.lib which is what they use for the name.

* Add a version resource.

Build libCurl:

* Get the latest source zip bundle from http://curl.haxx.se/download.html.

* Unzip (I will call this directory CURL_HOME)

* Open the CURL_HOME\lib\curllib.vcproj and convert to VS2005 (we may want to use VS2008 for the next major release?)

* Add the following to the very top of the setup.h file (CURL_HOME\lib\setup.h)

// Enable https support
#define USE_SSLEAY
#define USE_OPENSSL
// Enable compression encoding
#define HAVE_LIBZ
#define HAVE_ZLIB_H
// Enable SSH2 library
#define USE_LIBSSH2
#define HAVE_LIBSSH2_H
// Don't need ldap compiled into curl.
#define CURL_DISABLE_LDAP

(Alternatively you can set it in the Preprocessor section of the project)

* Make sure the following is set in the project settings for the Release configuration:
  - General -> Configuration Type: Build as DLL
  - C/C++ -> Code Generation -> Runtime Library: /MD
  - Linker -> General -> Output File: $(OutDir)\etlforge-libcurl.dll
  - Linker -> Advanced -> Import Library: $(TargetDir)etlforge-libcurl.lib
  - Linker -> Input -> Additional Dependencies:
ws2_32.lib
winmm.lib
etlforge-libeay32.lib
etlforge-ssleay32.lib
zdll.lib
etlforge-libssh2.lib

* Add version resource.

* Make sure that openssl, zlib and libssh2 are all in your INCLUDE and LIBRARY paths.

* Compile Release

