# Copyright (c) 1998-2009 Jitterbit, Inc.
#
# Makefile for building the UiBackend web services
#

MAKE=gmake
SHELL=/bin/bash

ifndef DEBUG
MYCFG=Release
ifdef PRIVATE_RELEASE
MYCFG2=Private_Release
else
MYCFG2=Release
endif
else
MYCFG=Debug
MYCFG2=Debug
endif

ws:
	@. ./build_env.sh; \
	cd Utils/project/SlickEdit; $(MAKE) -f WsUtils.mak CFG=$(MYCFG) || exit 1; \
	cd ../../../Authenticate/project/SlickEdit; $(MAKE) -f AuthenticateWs.mak CFG=$(MYCFG2) || exit 1; \
	cd ../../../DeployInterchangeData/project/SlickEdit; $(MAKE) -f DeployInterchangeDataWs.mak CFG=$(MYCFG2) || exit 1; \
	cd ../../../TreeMapperInfoProvider/project/SlickEdit; $(MAKE) -f TreeMapperInfoProviderWs.mak CFG=$(MYCFG2) || exit 1; \
	cd ../../../CheckoutInterchangeData/project/SlickEdit; $(MAKE) -f CheckoutInterchangeDataWs.mak CFG=$(MYCFG2) || exit 1; \
	cd ../../../ProjectAdministration/project/SlickEdit; $(MAKE) -f ProjectAdministrationWs.mak CFG=$(MYCFG2) || exit 1; \
	cd ../../../project/SlickEdit; $(MAKE) -f UiBackendWebServices.mak CFG=$(MYCFG) || exit 1;

clean:
	@(cd project/SlickEdit; $(MAKE) -f UiBackendWebServices.mak cleanall CFG=$(MYCFG))
	@(cd Authenticate/project/SlickEdit; $(MAKE) -f AuthenticateWs.mak cleanall CFG=$(MYCFG2))
	@(cd DeployInterchangeData/project/SlickEdit; $(MAKE) -f DeployInterchangeDataWs.mak cleanall CFG=$(MYCFG2))
	@(cd TreeMapperInfoProvider/project/SlickEdit; $(MAKE) -f TreeMapperInfoProviderWs.mak cleanall CFG=$(MYCFG2))
	@(cd CheckoutInterchangeData/project/SlickEdit; $(MAKE) -f CheckoutInterchangeDataWs.mak cleanall CFG=$(MYCFG2))
	@(cd ProjectAdministration/project/SlickEdit; $(MAKE) -f ProjectAdministrationWs.mak cleanall CFG=$(MYCFG2))
	@(cd Utils/project/SlickEdit; $(MAKE) -f WsUtils.mak cleanall CFG=$(MYCFG))
