add_library(etlforge_axis_server SHARED IMPORTED GLOBAL)
set_target_properties(etlforge_axis_server PROPERTIES IMPORTED_IMPLIB C:/ETL/Dependencies/axis-cpp-x64/lib/axis_server.lib IMPORTED_LOCATION C:/ETL/Dependencies/axis-cpp-x64/bin/axis_server.dll INTERFACE_INCLUDE_DIRECTORIES C:/ETL/Dependencies/axis-cpp-1.6b/axis-c-1.6b-Win32-trace-src/include)
add_library(etlforge_authenticate SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/ArrayOf_tns2_WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/Authenticate.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/AuthenticateResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/WsKeyValue_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/WsKeyValuePairs.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/Impl/AuthenticateImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/AuthenticateService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/AuthenticateWrapper.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
get_target_property(frontend_includes etlforge_process_engine INCLUDE_DIRECTORIES)
get_target_property(frontend_libraries etlforge_process_engine LINK_LIBRARIES)
add_executable(etlforge_operation_queue_smoke EXCLUDE_FROM_ALL tests/native/operation_queue_smoke.cpp)
target_include_directories(etlforge_operation_queue_smoke PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_operation_queue_smoke PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_operation_queue_smoke PRIVATE ${frontend_libraries})
option(ETLFORGE_QUEUE_ASAN "Instrument queue database code for memory-error diagnosis" OFF)
if(ETLFORGE_QUEUE_ASAN)
 foreach(queue_target etlforge_operation_queue_smoke etlforge_dtl etlforge_db_dtldbutils_probe etlforge_db_dtltrandb_probe etlforge_db_dtlmsgdb_probe)
  target_compile_options(${queue_target} PRIVATE /fsanitize=address /Zi)
  target_compile_definitions(${queue_target} PRIVATE _DISABLE_VECTOR_ANNOTATION _DISABLE_STRING_ANNOTATION _DISABLE_OPTIONAL_ANNOTATION)
 endforeach()
 target_link_options(etlforge_operation_queue_smoke PRIVATE /DEBUG)
endif()
target_include_directories(etlforge_authenticate PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_authenticate PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB AUTHENTICATEWRAPPER_EXPORTS)
target_link_libraries(etlforge_authenticate PRIVATE ${frontend_libraries} etlforge_axis_server)
set_target_properties(etlforge_authenticate PROPERTIES OUTPUT_NAME ETLForgeAuthenticate RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
get_target_property(authentication_sources etlforge_authenticate SOURCES)
add_executable(etlforge_frontend_auth_smoke EXCLUDE_FROM_ALL tests/native/frontend_auth_smoke.cpp ${authentication_sources})
target_include_directories(etlforge_frontend_auth_smoke PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_auth_smoke PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB AUTHENTICATEWRAPPER_EXPORTS)
target_link_libraries(etlforge_frontend_auth_smoke PRIVATE ${frontend_libraries} etlforge_axis_server)
add_executable(etlforge_frontend_http_fixture EXCLUDE_FROM_ALL tests/native/axis-http/main.cpp tests/native/axis-http/SimpleAxisTransport.cpp)
target_include_directories(etlforge_frontend_http_fixture PRIVATE C:/ETL/Dependencies/axis-cpp-1.6b/axis-c-1.6b-Win32-trace-src/include)
target_compile_definitions(etlforge_frontend_http_fixture PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS)
target_link_libraries(etlforge_frontend_http_fixture PRIVATE etlforge_axis_server ws2_32)

# Additional Studio project APIs, using the canonical upstream project source lists.
add_library(etlforge_frontend_projectadministration SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ArrayOf_tns2_WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ArrayOfScheduledOperation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/FindScheduledOperationsResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ProjectAdministration.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ScheduledOperation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ScheduledOperation_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/WsKeyValue_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/WsKeyValuePairs.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/Impl/ProjectAdministrationImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/Impl/TestScheduleImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ProjectAdministrationService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ProjectAdministrationWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_projectadministration PRIVATE ${frontend_includes})
target_sources(etlforge_frontend_projectadministration PRIVATE
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditDeployment.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditComparison.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditDatabase.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditObjectReader.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditJson.cpp")
target_include_directories(etlforge_frontend_projectadministration PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit")
target_compile_features(etlforge_frontend_projectadministration PRIVATE cxx_std_17)
target_compile_definitions(etlforge_frontend_projectadministration PRIVATE _HAS_AUTO_PTR_ETC=1 _HAS_STD_BYTE=0)
target_link_libraries(etlforge_frontend_projectadministration PRIVATE bcrypt crypt32 ole32 odbc32)
target_compile_definitions(etlforge_frontend_projectadministration PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_projectadministration PRIVATE ${frontend_libraries} etlforge_axis_server)
set_target_properties(etlforge_frontend_projectadministration PROPERTIES OUTPUT_NAME ETLForgeProjectAdministration RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_checkoutinterchangedata SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOf_tns2_WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfCGroupPermissions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfCInterchangeProject.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfCPermission.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfCUserPermissions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfDownloadedItemProperties.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfInterchangeDataRow.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfRequiredFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CGroupPermissions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CGroupPermissions_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CheckoutInterchangeData.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CheckoutInterchangeDataService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CheckoutInterchangeDataWrapper.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CInterchangeProject.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CInterchangeProject_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CInterchangeProjects.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CPermission.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CPermission_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CUserPermissions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CUserPermissions_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/DownloadDataHolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/DownloadedItemProperties.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/DownloadedItemProperties_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/DownloadedItems.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataHolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataRow.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataRow_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataTable.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/PermissionsHolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/RequiredFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/RequiredFile_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/RequiredFiles.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/WsKeyValue_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/WsKeyValuePairs.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/CheckoutInterchangeDataImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/CheckoutSpecialHandling.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/DefaultCheckoutSpecialHandling.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/FolderSpecialHandling.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSpecialHandling.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSpecialHandlingForInterchangeProject.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSpecialHandlingForOperation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSqlGeneratorForFolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSqlGeneratorForInterchangeProject.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSqlGeneratorForOperation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSqlGeneratorForSchedule.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_checkoutinterchangedata PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_checkoutinterchangedata PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_checkoutinterchangedata PRIVATE ${frontend_libraries} etlforge_axis_server)
set_target_properties(etlforge_frontend_checkoutinterchangedata PROPERTIES OUTPUT_NAME ETLForgeCheckoutInterchangeData RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_deployinterchangedata SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCDeployRename.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCDeployWarning.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCGroupPermissions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCJtrFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCLastUpdate.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCPermission.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCProjectMembership.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCRequiredFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCUserPermissions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfInterchangeDataRow.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployProjectCheckResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployRename.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployRename_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployResults.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployWarning.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployWarning_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CFileCheckResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CFileDependencies.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CGroupPermissions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CGroupPermissions_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CIntegrationEntity.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CIntegrationEntity_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CJtrFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CJtrFile_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CJtrFiles.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CLastUpdate.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CLastUpdate_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CPermission.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CPermission_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CProjectMembership.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CProjectMembership_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CRequiredFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CRequiredFile_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CRequiredFiles.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CUploadedJitterpak.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CUserPermissions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CUserPermissions_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/DeployInterchangeData.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeDataHolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeDataRow.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeDataRow_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeDataTable.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeIdHolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/PermissionsHolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/DatabaseDriverManager.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/DeployHistory.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/DeployInterchangeDataImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/DeployUtils.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/ExtendedItemProperties.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/FolderItemStruct.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleChangedDataImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleDeletedDataImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleExtendedProperties.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleOperationPipelines.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleRequiredFilesImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/InterchangeDataStructsImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/PermissionsHandler.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/PipelinePluginHandler.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SourceTargetXmlHandler.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployBase64Col.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployCallRestrictions.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployCrypto.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDelete.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteFolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteHttpEndpoint.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteJmsMessage.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteOperation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteSourceTarget.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteTransformation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteWebService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployFolder.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployHttpEndpoint.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployJmsMessage.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployMessage.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployOperation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceBulkLoad.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceCreate.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceDelete.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceQuery.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceUpdate.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceUpsert.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySourceTarget.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployTranChainLinks.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployTransformation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployWebService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/TableHandler.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/UpdateOtherServicesImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/UploadJitterpak.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/UploadJtrFilesImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/DeployInterchangeDataService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/DeployInterchangeDataWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_deployinterchangedata PRIVATE ${frontend_includes})
target_sources(etlforge_frontend_deployinterchangedata PRIVATE
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditDeployment.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditComparison.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditDatabase.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditObjectReader.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditJson.cpp")
target_include_directories(etlforge_frontend_deployinterchangedata PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit")
target_compile_features(etlforge_frontend_deployinterchangedata PRIVATE cxx_std_17)
target_compile_definitions(etlforge_frontend_deployinterchangedata PRIVATE _HAS_AUTO_PTR_ETC=1 _HAS_STD_BYTE=0)
target_link_libraries(etlforge_frontend_deployinterchangedata PRIVATE bcrypt crypt32 ole32 odbc32)
target_compile_definitions(etlforge_frontend_deployinterchangedata PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_deployinterchangedata PRIVATE ${frontend_libraries} etlforge_axis_server)
set_target_properties(etlforge_frontend_deployinterchangedata PROPERTIES OUTPUT_NAME ETLForgeDeployInterchangeData RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
file(GLOB frontend_support_sources
 "${ETLFORGE_INTEGRATION}/konga/UiBackend/UploadedFilesManager/*.cpp"
 "${ETLFORGE_INTEGRATION}/konga/HttpEndpoint/*.cpp"
 "${ETLFORGE_INTEGRATION}/konga/PipelinePluginEntityManager/*.cpp"
 "${ETLFORGE_INTEGRATION}/konga/ProjectBackup/*.cpp")
add_library(etlforge_frontend_support STATIC EXCLUDE_FROM_ALL ${frontend_support_sources})
target_include_directories(etlforge_frontend_support PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_support PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
foreach(frontend IN ITEMS etlforge_frontend_projectadministration etlforge_frontend_checkoutinterchangedata etlforge_frontend_deployinterchangedata)
 target_link_libraries(${frontend} PRIVATE etlforge_frontend_support ZLIB::ZLIB)
endforeach()
get_target_property(checkout_sources etlforge_frontend_checkoutinterchangedata SOURCES)
add_executable(etlforge_frontend_checkout_smoke EXCLUDE_FROM_ALL tests/native/frontend_checkout_smoke.cpp ${checkout_sources})
target_include_directories(etlforge_frontend_checkout_smoke PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_checkout_smoke PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_checkout_smoke PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)

# Remaining Studio-facing native APIs.
add_library(etlforge_frontend_admin SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Admin/Admin.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Admin/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Admin/ServerStatus.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Admin/Impl/AdminImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Admin/AdminService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Admin/AdminWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_admin PRIVATE ${frontend_includes})
target_sources(etlforge_frontend_admin PRIVATE
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/admin/ServerFiles.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/admin/ServerFilesSoap.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/ProxySettingsSoap.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditService.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditDatabase.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditComparison.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit/AuditJson.cpp")
target_include_directories(etlforge_frontend_admin PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/src/native/audit")
target_compile_features(etlforge_frontend_admin PRIVATE cxx_std_17)
  target_compile_definitions(etlforge_frontend_admin PRIVATE _HAS_AUTO_PTR_ETC=1 _HAS_STD_BYTE=0)
  target_link_libraries(etlforge_frontend_admin PRIVATE bcrypt odbc32 crypt32 ole32)
target_compile_definitions(etlforge_frontend_admin PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_admin PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_admin PROPERTIES OUTPUT_NAME ETLForgeAdmin RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_executable(etlforge_server_admin_tests EXCLUDE_FROM_ALL
 "${CMAKE_CURRENT_SOURCE_DIR}/tests/native/ServerFilesTest.cpp"
 "${CMAKE_CURRENT_SOURCE_DIR}/src/native/admin/ServerFiles.cpp")
target_include_directories(etlforge_server_admin_tests PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/src/native/admin")
target_compile_features(etlforge_server_admin_tests PRIVATE cxx_std_17)
target_compile_definitions(etlforge_server_admin_tests PRIVATE NOMINMAX WIN32_LEAN_AND_MEAN)
target_link_libraries(etlforge_server_admin_tests PRIVATE ZLIB::ZLIB crypt32 ole32)
add_library(etlforge_frontend_activity SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ActivityInfoProvider.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ArrayOf_xsd_int.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ArrayOf_xsd_long.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ArrayOfWsOperationMessage.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationInstanceFilter.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationLogInstance.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationLogInstance_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationMessage.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationMessage_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationProgress.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationProgress_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/Impl/ActivityInfoProviderImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/Impl/CancelOperationImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/Impl/utils.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ActivityInfoProviderService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ActivityInfoProviderWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_activity PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_activity PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_activity PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_activity PROPERTIES OUTPUT_NAME ETLForgeActivity RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_jobcontrol SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/JobControl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/WsRunOperationResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/Impl/JobControlImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/JobControlService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/JobControlWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_jobcontrol PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_jobcontrol PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_jobcontrol PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_jobcontrol PROPERTIES OUTPUT_NAME ETLForgeJobControl RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_filemanagement SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/ArrayOfCDownloadedFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/CDownloadedFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/CDownloadedFile_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/CFileDownloadResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/FileManager.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/Impl/FileManagementImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/FileManagerService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/FileManagerWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_filemanagement PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_filemanagement PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_filemanagement PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_filemanagement PROPERTIES OUTPUT_NAME ETLForgeFileManagement RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_xmlinfoprovider SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/ArrayOfCXmlStructureFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFile_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFileDownloadResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFileUploadResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/XmlInfoProvider.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/XmlInfoProviderService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/XmlInfoProviderWrapper.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/Impl/XmlInfoProviderImpl.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_xmlinfoprovider PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_xmlinfoprovider PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_xmlinfoprovider PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_xmlinfoprovider PROPERTIES OUTPUT_NAME ETLForgeXmlInfoProvider RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_treemapperinfoprovider SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOf_tns2_WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfBreakLocation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfBreakpoint.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfPipelinePositionStruct.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfWsDataElement.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfWsDataFileInfo.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/BreakLocation.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/BreakLocation_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Breakpoint.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Breakpoint_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/DebuggingParameters.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/PipelinePositionStruct.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/PipelinePositionStruct_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/TreeMapperInfoProvider.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WebServiceMethodData.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataElement.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataElement_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFileIdentifier.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFileInfo.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFileInfo_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFilesInfo.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsKeyValue_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsKeyValuePairs.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsLoadSourceDataInput.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsLoadSourceResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsTestOperationResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsTestResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsTestTransformationResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperDebug.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperInfoProviderImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperJtrGeneration.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperUtils.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/TreeMapperInfoProviderService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/TreeMapperInfoProviderWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_treemapperinfoprovider PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_treemapperinfoprovider PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_treemapperinfoprovider PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_treemapperinfoprovider PROPERTIES OUTPUT_NAME ETLForgeTreeMapperInfoProvider RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_odbcinfoprovider SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/ArrayOfWsDatabaseColumn.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/ArrayOfWsDatabaseObject.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/ArrayOfWsOdbcDriverDescriptor.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/OdbcInfoProvider.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseColumn.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseColumn_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseDriverInfo.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseInfo.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseObject.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseObject_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsOdbcDriverDescriptor.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsOdbcDriverDescriptor_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/Impl/OdbcInfoProviderImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/OdbcInfoProviderService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/OdbcInfoProviderWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_odbcinfoprovider PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_odbcinfoprovider PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_odbcinfoprovider PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_odbcinfoprovider PROPERTIES OUTPUT_NAME ETLForgeOdbcInfoProvider RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_testconnection SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/ConnectionInfo.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/FileList.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/TestConnection.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/Impl/TestConnectionImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/TestConnectionService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/TestConnectionWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_testconnection PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_testconnection PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_testconnection PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_testconnection PROPERTIES OUTPUT_NAME ETLForgeTestConnection RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_webserviceinfoprovider SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/ArrayOfCWsdlFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFile.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFile_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFileDownloadResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFiles.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFileUploadResult.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceInfoProvider.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceJtr.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceOperationData.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/Impl/WebServiceInfoProviderImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceInfoProviderService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceInfoProviderWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_webserviceinfoprovider PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_webserviceinfoprovider PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_webserviceinfoprovider PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_webserviceinfoprovider PROPERTIES OUTPUT_NAME ETLForgeWebServiceInfoProvider RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_pluginmanagement SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/FilterAsXml.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/FilterAsXml_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/PluginManager.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/Impl/PluginManagementImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/PluginManagerService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/PluginManagerWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_pluginmanagement PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_pluginmanagement PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_pluginmanagement PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_pluginmanagement PROPERTIES OUTPUT_NAME ETLForgePluginManagement RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_useradmin SHARED EXCLUDE_FROM_ALL
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOf_tns2_WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOf_xsd_string.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOfGroup.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOfUser.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOfUserGroup.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/AxisServiceException.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/Group.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/Group_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/User.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/User_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserAdmin.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserGroup.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserGroup_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UsersAndGroups.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/WsKeyValue.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/WsKeyValue_Array.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/WsKeyValuePairs.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/Impl/UserAdminImpl.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/Impl/UserAdminUtils.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserAdminService.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserAdminWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "C:/ETL/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_useradmin PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_useradmin PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_useradmin PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_useradmin PROPERTIES OUTPUT_NAME ETLForgeUserAdmin RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_custom_target(etlforge_frontend_services DEPENDS etlforge_authenticate etlforge_frontend_projectadministration etlforge_frontend_checkoutinterchangedata etlforge_frontend_deployinterchangedata etlforge_frontend_admin etlforge_frontend_activity etlforge_frontend_jobcontrol etlforge_frontend_filemanagement etlforge_frontend_xmlinfoprovider etlforge_frontend_treemapperinfoprovider etlforge_frontend_odbcinfoprovider etlforge_frontend_testconnection etlforge_frontend_webserviceinfoprovider etlforge_frontend_pluginmanagement etlforge_frontend_useradmin)

target_link_libraries(etlforge_frontend_admin PRIVATE etlforge_ssh_keys etlforge_s3)
