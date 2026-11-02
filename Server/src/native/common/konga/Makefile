# Copyright (c) 1998-2010 Jitterbit, Inc.
#
# Builds the common libraries needed by everything else
#

MAKE=gmake
SHELL=/bin/bash

ifndef DEBUG
MYCFG=Release
else
MYCFG=Debug
endif

common:
	@CWD=`pwd`; \
	. ../../../local_build_env.sh; \
	. ./build_env.sh; \
	cd $$CWD/Utils/project/SlickEdit; $(MAKE) -f Utils.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/SysInfo/project/SlickEdit; $(MAKE) -f SysInfo.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Xml/project/SlickEdit; $(MAKE) -f Xml.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Debugger/project/SlickEdit; $(MAKE) -f Debugger.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Soap/project/SlickEdit; $(MAKE) -f Soap.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Crypto/project/SlickEdit; $(MAKE) -f Crypto.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Zip/project/SlickEdit; $(MAKE) -f Zip.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/CharacterEncodingConversion/project/SlickEdit; $(MAKE) -f CharacterEncodingConversion.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/MailClient/project/SlickEdit; $(MAKE) -f MailClient.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Ldap/project/SlickEdit; $(MAKE) -f Ldap.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Log/project/SlickEdit; $(MAKE) -f Log.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Mime/project/SlickEdit; $(MAKE) -f Mime.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Networking/project/SlickEdit; $(MAKE) -f Networking.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/FileTransfer/project/SlickEdit; $(MAKE) -f FileTransfer.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/WebService/project/SlickEdit; $(MAKE) -f WebService.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/JbParser/project/SlickEdit; $(MAKE) -f JbParser.mak CFG=$(MYCFG) || exit 1

depend:
	@ . ../../../local_build_env.sh; \
	sh depends.sh

clean:
	@cd project/SlickEdit; $(MAKE) -f Common.mak cleanall CFG=$(MYCFG) || exit 1
