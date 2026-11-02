add_library(etlforge_axis_server SHARED IMPORTED GLOBAL)
set_target_properties(etlforge_axis_server PROPERTIES IMPORTED_IMPLIB ${ETLFORGE_ROOT}/Dependencies/axis-cpp-x64/lib/axis_server.lib IMPORTED_LOCATION ${ETLFORGE_ROOT}/Dependencies/axis-cpp-x64/bin/axis_server.dll INTERFACE_INCLUDE_DIRECTORIES ${ETLFORGE_ROOT}/Dependencies/axis-cpp-1.6b/axis-c-1.6b-Win32-trace-src/include)
add_library(etlforge_authenticate SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/ArrayOf_tns2_WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/Authenticate.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/AuthenticateResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/WsKeyValue_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/WsKeyValuePairs.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/Impl/AuthenticateImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/AuthenticateService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Authenticate/AuthenticateWrapper.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
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
target_include_directories(etlforge_frontend_http_fixture PRIVATE ${ETLFORGE_ROOT}/Dependencies/axis-cpp-1.6b/axis-c-1.6b-Win32-trace-src/include)
target_compile_definitions(etlforge_frontend_http_fixture PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS)
target_link_libraries(etlforge_frontend_http_fixture PRIVATE etlforge_axis_server ws2_32)

# Additional Studio project APIs, using the canonical upstream project source lists.
add_library(etlforge_frontend_projectadministration SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ArrayOf_tns2_WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ArrayOfScheduledOperation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/FindScheduledOperationsResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ProjectAdministration.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ScheduledOperation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ScheduledOperation_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/WsKeyValue_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/WsKeyValuePairs.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/Impl/ProjectAdministrationImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/Impl/TestScheduleImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ProjectAdministrationService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/ProjectAdministration/ProjectAdministrationWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
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
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOf_tns2_WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfCGroupPermissions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfCInterchangeProject.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfCPermission.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfCUserPermissions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfDownloadedItemProperties.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfInterchangeDataRow.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/ArrayOfRequiredFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CGroupPermissions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CGroupPermissions_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CheckoutInterchangeData.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CheckoutInterchangeDataService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CheckoutInterchangeDataWrapper.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CInterchangeProject.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CInterchangeProject_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CInterchangeProjects.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CPermission.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CPermission_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CUserPermissions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/CUserPermissions_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/DownloadDataHolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/DownloadedItemProperties.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/DownloadedItemProperties_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/DownloadedItems.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataHolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataRow.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataRow_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataTable.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/PermissionsHolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/RequiredFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/RequiredFile_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/RequiredFiles.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/WsKeyValue_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/WsKeyValuePairs.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/CheckoutInterchangeDataImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/CheckoutSpecialHandling.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/DefaultCheckoutSpecialHandling.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/FolderSpecialHandling.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSpecialHandling.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSpecialHandlingForInterchangeProject.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSpecialHandlingForOperation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSqlGeneratorForFolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSqlGeneratorForInterchangeProject.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSqlGeneratorForOperation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/OperationTreeCheckoutSqlGeneratorForSchedule.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_checkoutinterchangedata PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_checkoutinterchangedata PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_checkoutinterchangedata PRIVATE ${frontend_libraries} etlforge_axis_server)
set_target_properties(etlforge_frontend_checkoutinterchangedata PROPERTIES OUTPUT_NAME ETLForgeCheckoutInterchangeData RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_deployinterchangedata SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCDeployRename.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCDeployWarning.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCGroupPermissions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCJtrFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCLastUpdate.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCPermission.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCProjectMembership.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCRequiredFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfCUserPermissions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/ArrayOfInterchangeDataRow.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployProjectCheckResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployRename.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployRename_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployResults.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployWarning.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CDeployWarning_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CFileCheckResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CFileDependencies.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CGroupPermissions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CGroupPermissions_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CIntegrationEntity.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CIntegrationEntity_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CJtrFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CJtrFile_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CJtrFiles.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CLastUpdate.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CLastUpdate_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CPermission.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CPermission_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CProjectMembership.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CProjectMembership_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CRequiredFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CRequiredFile_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CRequiredFiles.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CUploadedJitterpak.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CUserPermissions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/CUserPermissions_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/DeployInterchangeData.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeDataHolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeDataRow.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeDataRow_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeDataTable.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/InterchangeIdHolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/PermissionsHolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/DatabaseDriverManager.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/DeployHistory.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/DeployInterchangeDataImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/DeployUtils.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/ExtendedItemProperties.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/FolderItemStruct.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleChangedDataImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleDeletedDataImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleExtendedProperties.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleOperationPipelines.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleRequiredFilesImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/InterchangeDataStructsImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/PermissionsHandler.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/PipelinePluginHandler.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SourceTargetXmlHandler.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployBase64Col.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployCallRestrictions.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployCrypto.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDelete.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteFolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteHttpEndpoint.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteJmsMessage.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteOperation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteSourceTarget.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteTransformation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployDeleteWebService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployFolder.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployHttpEndpoint.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployJmsMessage.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployMessage.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployOperation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceBulkLoad.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceCreate.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceDelete.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceQuery.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceUpdate.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySalesforceUpsert.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeploySourceTarget.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployTranChainLinks.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployTransformation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/SpecialHandlingDeployWebService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/TableHandler.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/UpdateOtherServicesImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/UploadJitterpak.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/UploadJtrFilesImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/DeployInterchangeDataService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/DeployInterchangeDataWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
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
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Admin/Admin.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Admin/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Admin/ServerStatus.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Admin/Impl/AdminImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Admin/AdminService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Admin/AdminWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
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
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ActivityInfoProvider.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ArrayOf_xsd_int.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ArrayOf_xsd_long.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ArrayOfWsOperationMessage.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationInstanceFilter.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationLogInstance.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationLogInstance_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationMessage.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationMessage_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationProgress.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/WsOperationProgress_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/Impl/ActivityInfoProviderImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/Impl/CancelOperationImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/Impl/utils.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ActivityInfoProviderService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Activity/ActivityInfoProviderWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_activity PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_activity PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_activity PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_activity PROPERTIES OUTPUT_NAME ETLForgeActivity RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_jobcontrol SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/JobControl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/WsRunOperationResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/Impl/JobControlImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/JobControlService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/JobControl/JobControlWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_jobcontrol PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_jobcontrol PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_jobcontrol PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_jobcontrol PROPERTIES OUTPUT_NAME ETLForgeJobControl RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_filemanagement SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/ArrayOfCDownloadedFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/CDownloadedFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/CDownloadedFile_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/CFileDownloadResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/FileManager.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/Impl/FileManagementImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/FileManagerService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/FileManagement/FileManagerWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_filemanagement PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_filemanagement PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_filemanagement PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_filemanagement PROPERTIES OUTPUT_NAME ETLForgeFileManagement RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_xmlinfoprovider SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/ArrayOfCXmlStructureFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFile_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFileDownloadResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFileUploadResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/XmlInfoProvider.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/XmlInfoProviderService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/XmlInfoProviderWrapper.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/XmlInfoProvider/Impl/XmlInfoProviderImpl.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_xmlinfoprovider PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_xmlinfoprovider PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_xmlinfoprovider PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_xmlinfoprovider PROPERTIES OUTPUT_NAME ETLForgeXmlInfoProvider RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_treemapperinfoprovider SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOf_tns2_WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfBreakLocation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfBreakpoint.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfPipelinePositionStruct.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfWsDataElement.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/ArrayOfWsDataFileInfo.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/BreakLocation.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/BreakLocation_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Breakpoint.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Breakpoint_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/DebuggingParameters.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/PipelinePositionStruct.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/PipelinePositionStruct_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/TreeMapperInfoProvider.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WebServiceMethodData.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataElement.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataElement_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFileIdentifier.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFileInfo.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFileInfo_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsDataFilesInfo.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsKeyValue_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsKeyValuePairs.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsLoadSourceDataInput.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsLoadSourceResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsTestOperationResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsTestResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/WsTestTransformationResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperDebug.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperInfoProviderImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperJtrGeneration.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperUtils.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/TreeMapperInfoProviderService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TreeMapperInfoProvider/TreeMapperInfoProviderWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_treemapperinfoprovider PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_treemapperinfoprovider PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_treemapperinfoprovider PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_treemapperinfoprovider PROPERTIES OUTPUT_NAME ETLForgeTreeMapperInfoProvider RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_odbcinfoprovider SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/ArrayOfWsDatabaseColumn.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/ArrayOfWsDatabaseObject.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/ArrayOfWsOdbcDriverDescriptor.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/OdbcInfoProvider.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseColumn.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseColumn_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseDriverInfo.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseInfo.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseObject.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsDatabaseObject_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsOdbcDriverDescriptor.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/WsOdbcDriverDescriptor_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/Impl/OdbcInfoProviderImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/OdbcInfoProviderService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/OdbcInfoProvider/OdbcInfoProviderWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_odbcinfoprovider PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_odbcinfoprovider PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_odbcinfoprovider PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_odbcinfoprovider PROPERTIES OUTPUT_NAME ETLForgeOdbcInfoProvider RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_testconnection SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/ConnectionInfo.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/FileList.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/TestConnection.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/Impl/TestConnectionImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/TestConnectionService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/TestConnection/TestConnectionWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_testconnection PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_testconnection PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_testconnection PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_testconnection PROPERTIES OUTPUT_NAME ETLForgeTestConnection RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_webserviceinfoprovider SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/ArrayOfCWsdlFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFile.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFile_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFileDownloadResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFiles.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/CWsdlFileUploadResult.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceInfoProvider.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceJtr.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceOperationData.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/Impl/WebServiceInfoProviderImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceInfoProviderService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceInfoProviderWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_webserviceinfoprovider PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_webserviceinfoprovider PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_webserviceinfoprovider PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_webserviceinfoprovider PROPERTIES OUTPUT_NAME ETLForgeWebServiceInfoProvider RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_pluginmanagement SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/FilterAsXml.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/FilterAsXml_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/PluginManager.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/Impl/PluginManagementImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/PluginManagerService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/PluginManagement/PluginManagerWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_pluginmanagement PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_pluginmanagement PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_pluginmanagement PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_pluginmanagement PROPERTIES OUTPUT_NAME ETLForgePluginManagement RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_library(etlforge_frontend_useradmin SHARED EXCLUDE_FROM_ALL
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOf_tns2_WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOf_xsd_string.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOfGroup.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOfUser.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/ArrayOfUserGroup.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/AxisServiceException.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/Group.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/Group_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/User.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/User_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserAdmin.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserGroup.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserGroup_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UsersAndGroups.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/WsKeyValue.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/WsKeyValue_Array.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/WsKeyValuePairs.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/Impl/UserAdminImpl.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/Impl/UserAdminUtils.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserAdminService.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/UserAdmin/UserAdminWrapper.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/UiBackendLogging.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.cpp"
 "${ETLFORGE_ROOT}/Server/src/native/integration/konga/UiBackend/WebServices/Utils/WsUtils.cpp"
)
target_include_directories(etlforge_frontend_useradmin PRIVATE ${frontend_includes})
target_compile_definitions(etlforge_frontend_useradmin PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_frontend_useradmin PRIVATE ${frontend_libraries} etlforge_axis_server etlforge_frontend_support ZLIB::ZLIB)
set_target_properties(etlforge_frontend_useradmin PROPERTIES OUTPUT_NAME ETLForgeUserAdmin RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_custom_target(etlforge_frontend_services DEPENDS etlforge_authenticate etlforge_frontend_projectadministration etlforge_frontend_checkoutinterchangedata etlforge_frontend_deployinterchangedata etlforge_frontend_admin etlforge_frontend_activity etlforge_frontend_jobcontrol etlforge_frontend_filemanagement etlforge_frontend_xmlinfoprovider etlforge_frontend_treemapperinfoprovider etlforge_frontend_odbcinfoprovider etlforge_frontend_testconnection etlforge_frontend_webserviceinfoprovider etlforge_frontend_pluginmanagement etlforge_frontend_useradmin)

target_link_libraries(etlforge_frontend_admin PRIVATE etlforge_ssh_keys etlforge_s3)
