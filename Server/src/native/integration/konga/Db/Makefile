# Copyright (c) 1998-2011 Jitterbit, Inc.
#
# Builds database access projects.
#

MAKE=gmake
SHELL=/bin/bash

ifndef DEBUG
MYCFG=Release
else
MYCFG=Debug
endif


db:
	@CWD=`pwd`; \
	. ../../../../local_build_env.sh; \
	. ./build_env.sh; \
	cd $$CWD/DtlDbUtils/project/SlickEdit; $(MAKE) -f DtlDbUtils.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/DbUtils/project/SlickEdit; $(MAKE) -f DbUtils.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/DbMaintenance/project/SlickEdit; $(MAKE) -f DbMaintenance.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/DtlIDb/project/SlickEdit; $(MAKE) -f DtlIDb.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/DtlMsgDb/project/SlickEdit; $(MAKE) -f DtlMsgDb.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/DtlPDb/project/SlickEdit; $(MAKE) -f DtlPDb.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/DtlTranDb/project/SlickEdit; $(MAKE) -f DtlTranDb.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/IDb/project/SlickEdit; $(MAKE) -f IDb.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/MsgDb/project/SlickEdit; $(MAKE) -f MsgDb.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/PDb/project/SlickEdit; $(MAKE) -f PDb.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/TranDb/project/SlickEdit; $(MAKE) -f TranDb.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/SchemaInfo/project/SlickEdit; $(MAKE) -f SchemaInfo.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/../DbRelations/project/SlickEdit; $(MAKE) -f DbRelations.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/TranDbUtils/project/SlickEdit; $(MAKE) -f TranDbUtils.mak CFG=$(MYCFG) || exit 1

depend:
	@sh depends.sh

clean:
	@cd project/SlickEdit; $(MAKE) -f Db.mak cleanall CFG=$(MYCFG) || exit 1

test-dbutils:
	@CWD=`pwd`; \
	. ../../../../local_build_env.sh; \
	. ./build_env_test.sh; \
	cd $$CWD/DbUtils/Test/project/SlickEdit; $(MAKE) -f TestDbUtils.mak CFG=$(MYCFG) || exit 1
