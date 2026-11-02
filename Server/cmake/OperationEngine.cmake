add_library(etlforge_operation_engine_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/SalesforceOperationSession.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/HandleWebServiceCallImpl.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/OperationCreatorDb.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/OperationEngine.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/OperationEngineDataElements.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/OperationEngineFinalProcessing.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/OperationEngineNextOperation.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/OperationScriptRunner.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/OperationUtils.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/SignalHandler.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/TestOperationImpl.cpp"
  "${ETLFORGE_INTEGRATION}/konga/OperationEngine/TransformationOperation.cpp"
)
target_include_directories(etlforge_operation_engine_probe PRIVATE
 "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 "${ETLFORGE_ROOT}/Dependencies/boost_1_86_0" "${ETLFORGE_ROOT}/Dependencies/windows-x64/include"
 "${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include" "${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32")
target_compile_definitions(etlforge_operation_engine_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
 target_include_directories(etlforge_operation_engine_probe PRIVATE
 "${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src"
 "${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src"
 "${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen")
target_include_directories(etlforge_operation_engine_probe PRIVATE "${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include")
