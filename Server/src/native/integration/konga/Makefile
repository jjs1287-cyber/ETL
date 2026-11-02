# Copyright (c) 1998-2011 Jitterbit, Inc.
#
# Makefile for building the ETLForge integration projects.
# Uses auto-generated Makefiles from the other projects.
#

ETLFORGE_HOME=$(DESTDIR)/etlforge
BINDIR=$(ETLFORGE_HOME)/bin
LIBDIR=$(ETLFORGE_HOME)/lib

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

integration: db libs apache-mods ws services utils

db:
	@(cd Db; $(MAKE) || exit 1)

libs:
	@CWD=`pwd`; \
	. ./build_env.sh; \
	cd $$CWD/JavaBackend/JdbcEngineC/project/SlickEdit; $(MAKE) -f JdbcEngineC.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/JavaBackend/JsonEngineC/project/SlickEdit; $(MAKE) -f JsonEngineC.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/JavaBackend/BulkLoadEngineC/project/SlickEdit; $(MAKE) -f BulkLoadEngineC.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Server/project/SlickEdit; $(MAKE) -f Server.mak CFG=$(MYCFG2) || exit 1; \
	cd $$CWD/Deployer/project/SlickEdit; $(MAKE) -f Deployer.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/DataElement/project/SlickEdit; $(MAKE) -f DataElement.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/JmsUtils/project/SlickEdit; $(MAKE) -f JmsUtils.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/HttpEndpoint/project/SlickEdit; $(MAKE) -f HttpEndpoint.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/UiBackend/GuidMappings/project/SlickEdit; $(MAKE) -f GuidMappings.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/InterchangeProject/project/SlickEdit; $(MAKE) -f InterchangeProject.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/ItemAccess/project/SlickEdit; $(MAKE) -f ItemAccess.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/ItemProperties/project/SlickEdit; $(MAKE) -f ItemProperties.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/ServerVersion/project/SlickEdit; $(MAKE) -f ServerVersion.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/ProjectBackup/project/SlickEdit; $(MAKE) -f ProjectBackup.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/OperationContext/project/SlickEdit; $(MAKE) -f OperationContext.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/OperationDataElements/project/SlickEdit; $(MAKE) -f OperationDataElements.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/OperationLog/project/SlickEdit; $(MAKE) -f OperationLog.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Messaging/project/SlickEdit; $(MAKE) -f Messaging.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/JavaBackend/JavaBackendConfiguration/project/SlickEdit; $(MAKE) -f JavaBackendConfiguration.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/OperationPipeline/project/SlickEdit; $(MAKE) -f OperationPipeline.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/SystemVariables/project/SlickEdit; $(MAKE) -f SystemVariables.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/PipelinePluginEntityManager/project/SlickEdit; $(MAKE) -f PipelinePluginEntityManager.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/PluginManager/project/SlickEdit; $(MAKE) -f PluginManager.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/PipelinePluginExecutor/project/SlickEdit; $(MAKE) -f PipelinePluginExecutor.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/SchemaManager/project/SlickEdit; $(MAKE) -f SchemaManager.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/UiBackend/UploadedFilesManager/project/SlickEdit; $(MAKE) -f UploadedFilesManager.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Workflow/project/SlickEdit; $(MAKE) -f Workflow.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Workflow/DbWriter/project/SlickEdit; $(MAKE) -f WfDbWriter.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Workflow/XmlReader/project/SlickEdit; $(MAKE) -f WfXmlReader.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Workflow/Validation/project/SlickEdit; $(MAKE) -f WorkflowValidation.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Authentication/project/SlickEdit; $(MAKE) -f Authentication.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/JdbcUtils/project/SlickEdit; $(MAKE) -f JdbcUtils.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/ScheduleUtils/project/SlickEdit; $(MAKE) -f ScheduleUtils.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/Scheduler/project/SlickEdit; $(MAKE) -f Scheduler.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/OperationObjects/project/SlickEdit; $(MAKE) -f OperationObjects.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/TransformationDataStore/project/SlickEdit; $(MAKE) -f TransformationDataStore.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/TransformationLogging/project/SlickEdit; $(MAKE) -f TransformationLogging.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/TransformationUtils/project/SlickEdit; $(MAKE) -f TransformationUtils.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/TransformationEngine/project/SlickEdit; $(MAKE) -f TransformationEngine.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/OperationEngine/project/SlickEdit; $(MAKE) -f OperationEngine.mak CFG=$(MYCFG2) || exit 1

apache-mods: libs
	@CWD=`pwd`; \
	. ./build_env.sh; \
	cd $$CWD/UiBackend/Workflow/mod_konga_wf/project/SlickEdit; $(MAKE) -f mod_konga_wf.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/mod_konga_ws/project/SlickEdit; $(MAKE) -f mod_konga_ws.mak CFG=$(MYCFG2) || exit 1

ws: libs
	@(cd UiBackend/WebServices; $(MAKE) || exit 1)

services: libs
	@CWD=`pwd`; \
	. ./build_env.sh; \
	cd $$CWD/OperationEngine/Process/project/SlickEdit; $(MAKE) -f OperationEngineProc.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/ProcessEngine/project/SlickEdit; $(MAKE) -f ProcessEngine.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/SchedulerProc/project/SlickEdit; $(MAKE) -f SchedulerProc.mak CFG=$(MYCFG) || exit 1; \
	cd $$CWD/FileCleanup/project/SlickEdit; $(MAKE) -f FileCleanup.mak CFG=$(MYCFG) || exit 1

utils: libs
	@CWD=`pwd`; \
	. ./build_env.sh; \
	cd $$CWD/ETLForgeUtils/project/SlickEdit; $(MAKE) -f ETLForgeUtils.mak CFG=$(MYCFG) || exit 1

sdk:
	(cd ../../plugins/SDK/cpp/project/SlickEdit; $(MAKE))

depend:
	@ . ./build_env.sh; \
	sh depends.sh

clean:
	@(cd UiBackend/WebServices; $(MAKE) clean || exit 1)
	@(cd project/SlickEdit; $(MAKE) -f Konga.mak cleanall CFG=$(MYCFG) || exit 1)
	@(cd Db; $(MAKE) clean)

install-deprecated:
	@if [ -z $(DESTDIR) ]; then \
	  echo "DESTDIR is not defined!"; \
	  echo "Usage: make install DESTDIR=/usr/local"; \
	   exit 1; \
	fi

	install -d $(ETLFORGE_HOME)
	install -d $(BINDIR)
	install -d $(LIBDIR)

# Executables
	cp -f ../../../bin/konga/Release/ETLForgeFileCleanup $(BINDIR)
	cp -f ../../../bin/konga/Release/ETLForgeOperationEngineProc $(BINDIR)
	cp -f ../../../bin/konga/Release/ETLForgeProcessEngine $(BINDIR)
	cp -f ../../../bin/konga/Release/ETLForgeScheduler $(BINDIR)

# Libraries
	cp -f ../../../lib/konga/libkonga-*.so $(LIBDIR)
	cp -f ../../../lib/konga/mod_konga*.so $(LIBDIR)

# TransformationEngine resource files.
	cp -f TransformationEngine/MapTransform.rc $(ETLFORGE_HOME)/Resources/TransformationEngine/
	cp -f TransformationEngine/resource.h $(ETLFORGE_HOME)/Resources/TransformationEngine/
	cp -f TransformationEngine/ErrorCodes/ErrorCodes.rc $(ETLFORGE_HOME)/Resources/TransformationEngine/ErrorCodes/
	cp -f TransformationEngine/ErrorCodes/resource.h $(ETLFORGE_HOME)/Resources/TransformationEngine/ErrorCodes/

	cp -f PlatformSpecific/linux/scripts/?*.sh $(BINDIR)
	cp -f PlatformSpecific/linux/scripts/etlforge $(BINDIR)

# Create link for backwards compatability after changing name of control script.
	cd $(BINDIR); ln -sf etlforge ETLForge.sh
