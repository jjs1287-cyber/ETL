#!/usr/bin/env bash
set -euo pipefail
export PATH=/ucrt64/bin:/usr/bin:$PATH
export PKG_CONFIG_PATH=/ucrt64/lib/pkgconfig
cd /c/ETL/Dependencies/pgbouncer-1.26.1
CC=gcc CFLAGS='-O2 -std=gnu11 -D_WIN32_WINNT=0x0602' ./configure --host=x86_64-w64-mingw32 --prefix=/c/ETL/Server/runtime/pgbouncer-1.26.1
make -j4 pgbouncer.exe
