add_library(etlforge_operationobjects_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/ArchiveOperation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/DatabaseLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/DataLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/EmailMessage.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/EmailMessageSender.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/FileShareLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/FileShareSource.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/FileShareTarget.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/FileTypeSourceLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/FileTypeTargetLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/FtpLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/FtpSource.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/FtpTarget.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/HttpLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/HttpSource.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/HttpTarget.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/JmsLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/JmsSource.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/JmsSourceCreator.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/JmsTarget.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/LdapLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/LocationCreatorDb.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/NoSource.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/Operation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/OperationEngineContext.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/OperationHistoryWriter.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/OperationObjectCreatorDb.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/OperationObjectCreatorKeyValues.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/OperationSummaryWriter.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/OverrideFileTypeSourceLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/PreviousTargetAsSource.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/ProcessedFiles.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/RenameFile.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/SalesforceBulkLoadOperation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/SourceDataLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/SourceFromHostedWebServiceCall.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/SourceTargetPipelinePluginExecution.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/SourceXmlDataStructure.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/TargetDataLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/TargetXmlDataStructure.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/TemporaryStorageLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/TemporaryStorageSource.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/TemporaryStorageTarget.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/TransferHistory.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/Transformation.cpp"
  "C:/ETL/Server/src/native/integration/konga/OperationObjects/XmlDataStructure.cpp"
)
target_include_directories(etlforge_operationobjects_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_operationobjects_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_transformationutils_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/CachedDbLookup.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/DateFormatParser.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/DateFormatUtils.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/DbLocationConnectionInfo.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/DbLookupCache.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/FileTypeTargetCache.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/IntegrationBreakpointLocation.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/IntegrationDebugUtils.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/LdapExecutor.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/ReadFileImpl.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/RunOperationImpl.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/ScriptCache.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/SendTransformationEmail.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/TranDBUtils.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/TransformationDataHolder.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/TransformationStatistics.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/TransformationWebServiceInfo.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationUtils/TransformFcnUtils.cpp"
)
target_include_directories(etlforge_transformationutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_transformationutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_interchangeproject_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/InterchangeProject/InterchangeProject.cpp"
)
target_include_directories(etlforge_interchangeproject_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_interchangeproject_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_dbrelations_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/DbRelations/DatabaseTableNameMappings.cpp"
  "C:/ETL/Server/src/native/integration/konga/DbRelations/DbRelations.cpp"
  "C:/ETL/Server/src/native/integration/konga/DbRelations/FunctionIdMapper.cpp"
)
target_include_directories(etlforge_dbrelations_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_dbrelations_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_operationdataelements_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/OperationDataElements/OperationDataElementsManager.cpp"
)
target_include_directories(etlforge_operationdataelements_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_operationdataelements_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_scheduleutils_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/ScheduleUtils/EnableDisableSchedules.cpp"
  "C:/ETL/Server/src/native/integration/konga/ScheduleUtils/UpdateScheduler.cpp"
)
target_include_directories(etlforge_scheduleutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_scheduleutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_systemvariables_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/SystemVariables/SystemVariables.cpp"
)
target_include_directories(etlforge_systemvariables_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_systemvariables_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_transformationlogging_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/TransformationLogging/ErrorConfiguration.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationLogging/ErrorLog.cpp"
  "C:/ETL/Server/src/native/integration/konga/TransformationLogging/ViewLog.cpp"
)
target_include_directories(etlforge_transformationlogging_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_transformationlogging_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_serverversion_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/ServerVersion/ServerVersion.cpp"
)
target_include_directories(etlforge_serverversion_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_serverversion_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_custom_target(etlforge_remaining_core_probes DEPENDS etlforge_operationobjects_probe etlforge_transformationutils_probe etlforge_interchangeproject_probe etlforge_dbrelations_probe etlforge_operationdataelements_probe etlforge_scheduleutils_probe etlforge_systemvariables_probe etlforge_transformationlogging_probe etlforge_serverversion_probe)

add_library(etlforge_endpoint_archive STATIC src/native/compression/EndpointArchive.cpp)
target_compile_features(etlforge_endpoint_archive PRIVATE cxx_std_17)
target_compile_definitions(etlforge_endpoint_archive PRIVATE NOMINMAX)
target_include_directories(etlforge_endpoint_archive PUBLIC "${CMAKE_CURRENT_SOURCE_DIR}/src/native/compression" PRIVATE C:/ETL/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_endpoint_archive PUBLIC C:/ETL/Tools/vcpkg/installed/x64-windows/lib/archive.lib)
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
target_include_directories(etlforge_s3 PRIVATE C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_s3 PUBLIC CURL::libcurl C:/ETL/Tools/vcpkg/installed/x64-windows/lib/libcrypto.lib)
add_executable(etlforge_s3_tests EXCLUDE_FROM_ALL tests/native/s3_tests.cpp src/native/audit/AuditJson.cpp)
target_compile_features(etlforge_s3_tests PRIVATE cxx_std_17)
target_compile_options(etlforge_s3_tests PRIVATE /utf-8)
target_compile_definitions(etlforge_s3_tests PRIVATE BOOST_ALL_NO_LIB NOMINMAX)
target_include_directories(etlforge_s3_tests PRIVATE C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_s3_tests PRIVATE etlforge_s3)

add_library(etlforge_ssh_keys STATIC src/native/ssh/SshKeys.cpp src/native/ssh/PgpFiles.cpp)
target_compile_features(etlforge_ssh_keys PRIVATE cxx_std_17)
target_compile_definitions(etlforge_ssh_keys PRIVATE NOMINMAX WIN32_LEAN_AND_MEAN BOOST_ALL_NO_LIB)
target_include_directories(etlforge_ssh_keys PRIVATE C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_ssh_keys PUBLIC odbc32 advapi32 ole32 C:/ETL/Tools/vcpkg/installed/x64-windows/lib/libcrypto.lib)

add_executable(etlforge_ssh_keys_tests tests/native/ssh_keys_tests.cpp src/native/audit/AuditJson.cpp)
target_compile_features(etlforge_ssh_keys_tests PRIVATE cxx_std_17)
target_include_directories(etlforge_ssh_keys_tests PRIVATE C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_ssh_keys_tests PRIVATE etlforge_ssh_keys)
target_compile_definitions(etlforge_ssh_keys_tests PRIVATE BOOST_ALL_NO_LIB NOMINMAX)
