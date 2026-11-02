# External build dependencies

This repository contains application source and test fixtures. The existing development machine already has the following external dependencies. A source-only clone needs the same dependency layout before compilation; copying this repository alone does not install those SDKs or libraries.

| Location relative to workspace | Purpose |
| --- | --- |
| Tools/jdk8u504-b01 | Java 8 compiler/runtime and JNI headers |
| Tools/apache-ant-1.10.15 | Studio build |
| Tools/vcpkg/installed/x64-windows | curl, OpenSSL, libssh2 and their native dependencies |
| Dependencies/boost_1_86_0 and Dependencies/boost-x64 | Boost headers and compiled x64 libraries |
| Dependencies/windows-x64 | Rebuilt Xerces and other native headers/libraries |
| Dependencies/xerces-c-3.2.5 | Xerces source |
| Dependencies/xalan_c-1.12 and Dependencies/build/xalan-x64 | Xalan source and compiled libraries |
| Dependencies/dtl-3.7/work | DTL sources used by the native build, including project fixes |
| Dependencies/getpot-legacy | Legacy GetPot header |
| Dependencies/icu-78.3-x64 | ICU headers and x64 libraries |
| Dependencies/axis-cpp-x64 | Rebuilt Axis C++ headers/runtime |
| Dependencies/quickjs-ng-0.17.0 | QuickJS-NG source |
| Dependencies/saxonc-12.10.1 | SaxonC native XSLT SDK |
| Dependencies/java, including salesforce and xslt | Java dependency JARs |
| Studio/lib | Third-party Studio JARs referenced by .classpath and Ant |
| Reference/v5/server-windows and Reference/v5/studio-windows | Third-party JAR inputs to the recovered server Java build; original product JARs are explicitly excluded |
| Server/runtime/postgresql-18/pgsql/lib and Server/runtime/cloud-logging/include | PostgreSQL client library/headers for the cloud-logging build |

Native dependency build helpers are under Tools (Build-NativeDependencies, Build-BoostX64, Build-Xerces, Build-Xalan, Build-AxisCppX64). Existing prepared dependencies must be preserved, particularly modified DTL sources and the compatible legacy headers. The source upload does not replace them with unverified downloads.

Use Tools/Verify-CleanConfigure.cmd to validate dependency discovery without trusting an old CMake cache. Tools/Build-Native.cmd verifies both compilation and registered local tests. Explicit machine overrides: ETLFORGE_PYTHON, JAVA_HOME, ANT_HOME, ECLIPSE_HOME. Native wrappers discover Visual Studio with vswhere; no particular Community installation path is required.
