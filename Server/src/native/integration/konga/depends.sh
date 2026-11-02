#!/bin/sh

gen_dep_file() {
    cd "$1"
    ls -l ../../*.cpp > /dev/null 2> /dev/null
    if [ $? -ne 0 ]; then
	return 0
    fi
    echo ${1%"project/SlickEdit"}
    echo "
myall: all

ifeq \"\$(CFG)\" \"Debug\"
OUTDIR=Debug
endif
ifeq \"\$(CFG)\" \"Release\"
OUTDIR=Release
endif
" > .include

    SRC_PAT="../../*.cpp"
    if [ -d ../../Impl ]; then
	SRC_PAT="$SRC_PAT ../../Impl/*.cpp"
    fi

    gcc -MM -I"$2" -I"$3" $SRC_PAT | \
	sed '/^\S/s/^/\$(OUTDIR)\//; /\\/!s/$/\n\t\$(COMPILE)/' >> .include
    if [ $? -ne 0 ]; then
	exit 1
    fi
    mv .include depend.inc
}

# $1 = Directory to check
# $2, $3 = Include directories to use if $1 needs dependencies to be build
gen_deps() {
    this_dir="$1"

    if [ ! -d "$this_dir" ]; then
	return 0
    fi

    base_name=`basename "$this_dir"`

    if [ "$base_name" = "SlickEdit" ]; then
	gen_dep_file "$this_dir" "$2" "$3"
	return 0
    fi

    for file in $this_dir/*
    do
	gen_deps "$file" "../$2" "../$3"
    done
}

gen_deps "`pwd`" "../../../common/cpp" ".."
