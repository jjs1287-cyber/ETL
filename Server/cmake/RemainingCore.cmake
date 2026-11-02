add_library(etlforge_operationobjects_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/ArchiveOperation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/DatabaseLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/DataLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/EmailMessage.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/EmailMessageSender.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/FileShareLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/FileShareSource.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/FileShareTarget.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/FileTypeSourceLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/FileTypeTargetLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/FtpLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/FtpSource.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/FtpTarget.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/HttpLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/HttpSource.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/HttpTarget.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/JmsLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/JmsSource.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/JmsSourceCreator.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/JmsTarget.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/LdapLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/LocationCreatorDb.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/NoSource.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/Operation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/OperationEngineContext.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/OperationHistoryWriter.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/OperationObjectCreatorDb.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/OperationObjectCreatorKeyValues.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/OperationSummaryWriter.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/OverrideFileTypeSourceLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/PreviousTargetAsSource.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/ProcessedFiles.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/RenameFile.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/SalesforceBulkLoadOperation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/SourceDataLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/SourceFromHostedWebServiceCall.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/SourceTargetPipelinePluginExecution.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/SourceXmlDataStructure.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/TargetDataLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/TargetXmlDataStructure.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/TemporaryStorageLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/TemporaryStorageSource.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/TemporaryStorageTarget.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/TransferHistory.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/Transformation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationObjects/XmlDataStructure.cpp"
)
target_include_directories(etlforge_operationobjects_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_operationobjects_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_transformationutils_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/CachedDbLookup.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/DateFormatParser.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/DateFormatUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/DbLocationConnectionInfo.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/DbLookupCache.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/FileTypeTargetCache.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/IntegrationBreakpointLocation.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/IntegrationDebugUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/LdapExecutor.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/ReadFileImpl.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/RunOperationImpl.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/ScriptCache.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/SendTransformationEmail.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/TranDBUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/TransformationDataHolder.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/TransformationStatistics.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/TransformationWebServiceInfo.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationUtils/TransformFcnUtils.cpp"
)
target_include_directories(etlforge_transformationutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_transformationutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_interchangeproject_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/InterchangeProject/InterchangeProject.cpp"
)
target_include_directories(etlforge_interchangeproject_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_interchangeproject_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_dbrelations_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/DbRelations/DatabaseTableNameMappings.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/DbRelations/DbRelations.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/DbRelations/FunctionIdMapper.cpp"
)
target_include_directories(etlforge_dbrelations_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_dbrelations_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_operationdataelements_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/OperationDataElements/OperationDataElementsManager.cpp"
)
target_include_directories(etlforge_operationdataelements_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_operationdataelements_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_scheduleutils_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/ScheduleUtils/EnableDisableSchedules.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/ScheduleUtils/UpdateScheduler.cpp"
)
target_include_directories(etlforge_scheduleutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_scheduleutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_systemvariables_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/SystemVariables/SystemVariables.cpp"
)
target_include_directories(etlforge_systemvariables_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_systemvariables_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_transformationlogging_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationLogging/ErrorConfiguration.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationLogging/ErrorLog.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/TransformationLogging/ViewLog.cpp"
)
target_include_directories(etlforge_transformationlogging_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_transformationlogging_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_serverversion_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/ServerVersion/ServerVersion.cpp"
)
target_include_directories(etlforge_serverversion_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_serverversion_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_custom_target(etlforge_remaining_core_probes DEPENDS etlforge_operationobjects_probe etlforge_transformationutils_probe etlforge_interchangeproject_probe etlforge_dbrelations_probe etlforge_operationdataelements_probe etlforge_scheduleutils_probe etlforge_systemvariables_probe etlforge_transformationlogging_probe etlforge_serverversion_probe)

add_library(etlforge_endpoint_archive STATIC src/native/compression/EndpointArchive.cpp)
target_compile_features(etlforge_endpoint_archive PRIVATE cxx_std_17)
target_compile_definitions(etlforge_endpoint_archive PRIVATE NOMINMAX)
target_include_directories(etlforge_endpoint_archive PUBLIC "${CMAKE_CURRENT_SOURCE_DIR}/src/native/compression" PRIVATE ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_endpoint_archive PUBLIC ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/lib/archive.lib)
target_link_libraries(etlforge_operationobjects_probe PUBLIC etlforge_endpoint_archive)
target_sources(etlforge_operationobjects_probe PRIVATE src/native/integration/konga/OperationObjects/EndpointCompression.cpp)

add_executable(etlforge_endpoint_archive_tests tests/EndpointArchiveTests.cpp)
target_link_libraries(etlforge_endpoint_archive_tests PRIVATE etlforge_endpoint_archive)

set_source_files_properties(src/native/integration/konga/OperationObjects/EndpointCompression.cpp PROPERTIES COMPILE_OPTIONS "/std:c++17" COMPILE_DEFINITIONS "_HAS_AUTO_PTR_ETC=1")
target_compile_features(etlforge_endpoint_archive_tests PRIVATE cxx_std_17)

target_compile_options(etlforge_endpoint_archive_tests PRIVATE /utf-8)

add_library(etlforge_s3 STATIC src/native/s3/S3Client.cpp)
target_compile_features(etlforge_s3 PRIVATE cxx_std_17)
target_compile_options(etlforge_s3 PRIVATE /utf-8)
target_compile_definitions(etlforge_s3 PRIVATE BOOST_ALL_NO_LIB NOMINMAX)
target_include_directories(etlforge_s3 PRIVATE ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_s3 PUBLIC CURL::libcurl ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/lib/libcrypto.lib)
add_executable(etlforge_s3_tests EXCLUDE_FROM_ALL tests/native/s3_tests.cpp src/native/audit/AuditJson.cpp)
target_compile_features(etlforge_s3_tests PRIVATE cxx_std_17)
target_compile_options(etlforge_s3_tests PRIVATE /utf-8)
target_compile_definitions(etlforge_s3_tests PRIVATE BOOST_ALL_NO_LIB NOMINMAX)
target_include_directories(etlforge_s3_tests PRIVATE ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_s3_tests PRIVATE etlforge_s3)

add_library(etlforge_ssh_keys STATIC src/native/ssh/SshKeys.cpp src/native/ssh/PgpFiles.cpp)
target_compile_features(etlforge_ssh_keys PRIVATE cxx_std_17)
target_compile_definitions(etlforge_ssh_keys PRIVATE NOMINMAX WIN32_LEAN_AND_MEAN BOOST_ALL_NO_LIB)
target_include_directories(etlforge_ssh_keys PRIVATE ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_ssh_keys PUBLIC odbc32 advapi32 ole32 ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/lib/libcrypto.lib)

add_executable(etlforge_ssh_keys_tests tests/native/ssh_keys_tests.cpp src/native/audit/AuditJson.cpp)
target_compile_features(etlforge_ssh_keys_tests PRIVATE cxx_std_17)
target_include_directories(etlforge_ssh_keys_tests PRIVATE ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_ssh_keys_tests PRIVATE etlforge_ssh_keys)
target_compile_definitions(etlforge_ssh_keys_tests PRIVATE BOOST_ALL_NO_LIB NOMINMAX)
