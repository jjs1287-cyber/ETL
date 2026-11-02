add_library(etlforge_axis_client SHARED IMPORTED GLOBAL)
set_target_properties(etlforge_axis_client PROPERTIES IMPORTED_IMPLIB ${ETLFORGE_ROOT}/Dependencies/axis-cpp-x64/lib/axis_client.lib IMPORTED_LOCATION ${ETLFORGE_ROOT}/Dependencies/axis-cpp-x64/bin/axis_client.dll INTERFACE_INCLUDE_DIRECTORIES ${ETLFORGE_ROOT}/Dependencies/axis-cpp-1.6b/axis-c-1.6b-Win32-trace-src/include)
add_library(etlforge_soap_bulkloadengine STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/BulkLoadEngine.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/BulkLoadEngineWrapperCPP.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/WsBulkLoadError.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/WsBulkLoadParams.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/WsBulkLoadResult.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/WsBulkLoadSessionId.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/WsKeyValue.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/WsKeyValue_Array.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/WsSalesforceEndpoint.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/BulkLoadEngine/WsStartNewBulkLoadSessionResult.cpp"
)
target_include_directories(etlforge_soap_bulkloadengine PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen)
target_compile_definitions(etlforge_soap_bulkloadengine PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB USE_AXIS_CPP)
target_link_libraries(etlforge_soap_bulkloadengine PUBLIC etlforge_axis_client)
add_library(etlforge_soap_jsonengine STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/NativeJson/NativeJsonBackend.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JsonEngine/JsonEngine.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JsonEngine/JsonEngineWrapperCPP.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JsonEngine/WsConversionParams.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JsonEngine/WsJsonError.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JsonEngine/WsJsonSessionId.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JsonEngine/WsStartNewJsonSessionResult.cpp"
)
target_include_directories(etlforge_soap_jsonengine PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen)
target_compile_definitions(etlforge_soap_jsonengine PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB USE_AXIS_CPP)
target_link_libraries(etlforge_soap_jsonengine PUBLIC etlforge_axis_client)
add_library(etlforge_native_json STATIC "${ETLFORGE_INTEGRATION}/konga/NativeJson/EtlForgeJsonConversion.cpp" "${ETLFORGE_INTEGRATION}/konga/NativeJson/EtlForgeJsonRuntime.cpp")
set_target_properties(etlforge_native_json PROPERTIES CXX_STANDARD 17)
target_include_directories(etlforge_native_json PRIVATE ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include)
target_compile_definitions(etlforge_native_json PRIVATE NOMINMAX BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_native_json PUBLIC XercesC::XercesC ole32)
target_link_libraries(etlforge_soap_jsonengine PUBLIC etlforge_native_json)
add_library(etlforge_soap_jdbcengine STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/ArrayOf_xsd_string.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/ArrayOfWsKeyValue.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/ArrayOfWsResultSetRow.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/JdbcEngine.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsConnectionParams.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsDbExecuteResult.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsDbLookupParams.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsDbLookupResult.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsJdbcError.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsJdbcSessionId.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsJdbcStartNewSessionResult.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsKeyValue.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsKeyValue_Array.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsReadSourceParams.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsResultSet.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsResultSetRow.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsResultSetRow_Array.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JdbcEngine/WsWriteTargetParams.cpp"
)
target_include_directories(etlforge_soap_jdbcengine PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen)
target_compile_definitions(etlforge_soap_jdbcengine PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB USE_AXIS_CPP)
target_link_libraries(etlforge_soap_jdbcengine PUBLIC etlforge_axis_client)
add_library(etlforge_soap_jdbcutils STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/JdbcUtils/JdbcDbLookup.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JdbcUtils/JdbcEngineWrapper.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JdbcUtils/ReadSource.cpp"
  "${ETLFORGE_INTEGRATION}/konga/JdbcUtils/WriteTarget.cpp"
)
target_include_directories(etlforge_soap_jdbcutils PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen)
target_compile_definitions(etlforge_soap_jdbcutils PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB USE_AXIS_CPP)
target_link_libraries(etlforge_soap_jdbcutils PUBLIC etlforge_axis_client)
target_compile_definitions(etlforge_runtime_javabackendconfiguration PRIVATE USE_AXIS_CPP)
target_link_libraries(etlforge_runtime_javabackendconfiguration PUBLIC etlforge_axis_client)
