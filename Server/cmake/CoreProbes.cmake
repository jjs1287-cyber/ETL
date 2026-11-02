add_library(etlforge_processengine_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/ProcessEngine/Globals.cpp"
  "${ETLFORGE_INTEGRATION}/konga/ProcessEngine/ProcessEngine.cpp"
)
target_include_directories(etlforge_processengine_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_processengine_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_scheduler_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/Scheduler/ScheduledOp.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Scheduler/ScheduledOpCollector.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Scheduler/Scheduler.cpp"
)
target_include_directories(etlforge_scheduler_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_scheduler_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_schedulerproc_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/SchedulerProc/SchedulerMain.cpp"
)
target_include_directories(etlforge_schedulerproc_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_schedulerproc_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_server_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/Server/DefaultHttpProxyConfiguration.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Server/lmcheck.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Server/Server.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Server/ServerHttpProxyConfiguration.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Server/WDILogFileUtils.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Server/WDIPathFinder.cpp"
)
target_include_directories(etlforge_server_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_server_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_operationcontext_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/OperationContext/AccessProtectedDataElementsHandler.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationContext/DefaultErrorReporter.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationContext/DefaultOperationContext.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationContext/DefaultOperationDataElementHolder.cpp"
)
target_include_directories(etlforge_operationcontext_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_operationcontext_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_operationpipeline_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/OperationPipeline/ActivityType.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationPipeline/OperationPipeline.cpp"
)
target_include_directories(etlforge_operationpipeline_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_operationpipeline_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_operationlog_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/OperationLog/BillingTabUtils.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationLog/OperationInstance.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationLog/OperationLog.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationLog/OperationProgress.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationLog/TransferHistoryUtils.cpp"
)
target_include_directories(etlforge_operationlog_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_operationlog_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_dataelement_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/DataElement/DataElement.cpp"
  "${ETLFORGE_INTEGRATION}/konga/DataElement/DefaultDataElementSerializer.cpp"
  "${ETLFORGE_INTEGRATION}/konga/DataElement/FindDataElement.cpp"
  "${ETLFORGE_INTEGRATION}/konga/DataElement/Token.cpp"
# TokenArray.cpp is absent from both v4 Windows DataElement project lists; obsolete parser API.
)
target_include_directories(etlforge_dataelement_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_dataelement_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_transformationdatastore_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/TransformationDataStore/TransformationDataStore.cpp"
  "${ETLFORGE_INTEGRATION}/konga/TransformationDataStore/TransformationDataStoreInstance.cpp"
)
target_include_directories(etlforge_transformationdatastore_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" "C:/ETL/Dependencies/boost_1_86_0" "C:/ETL/Dependencies/windows-x64/include" "C:/ETL/Dependencies/xalan_c-1.12/src" "C:/ETL/Dependencies/build/xalan-x64/src" "C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen" "C:/ETL/Tools/jdk8u504-b01/include" "C:/ETL/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_transformationdatastore_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_custom_target(etlforge_core_probes DEPENDS etlforge_processengine_probe etlforge_scheduler_probe etlforge_schedulerproc_probe etlforge_server_probe etlforge_operationcontext_probe etlforge_operationpipeline_probe etlforge_operationlog_probe etlforge_dataelement_probe etlforge_transformationdatastore_probe)
