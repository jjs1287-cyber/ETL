add_library(etlforge_runtime_itemaccess STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/ItemAccess/AccessControl.cpp"
  "${ETLFORGE_INTEGRATION}/konga/ItemAccess/AccessControlUtils.cpp"
  "${ETLFORGE_INTEGRATION}/konga/ItemAccess/AccessSqlGenerator.cpp"
  "${ETLFORGE_INTEGRATION}/konga/ItemAccess/GroupAccess.cpp"
  "${ETLFORGE_INTEGRATION}/konga/ItemAccess/GroupInfo.cpp"
  "${ETLFORGE_INTEGRATION}/konga/ItemAccess/UserAccess.cpp"
  "${ETLFORGE_INTEGRATION}/konga/ItemAccess/UserInfo.cpp"
)
target_include_directories(etlforge_runtime_itemaccess PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_runtime_itemaccess PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_runtime_itemproperties STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/ItemProperties/ItemProperties.cpp"
)
target_include_directories(etlforge_runtime_itemproperties PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_runtime_itemproperties PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_runtime_messaging STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/Messaging/CheckOperationQueue.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Messaging/JmsMessage.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Messaging/JmsMessageSender.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Messaging/Message.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Messaging/MessageSender.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Messaging/OperationMessageUtils.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Messaging/ProcessMessage.cpp"
)
target_include_directories(etlforge_runtime_messaging PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_runtime_messaging PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_runtime_schemamanager STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/SchemaManager/SchemaManager.cpp"
)
target_include_directories(etlforge_runtime_schemamanager PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_runtime_schemamanager PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_runtime_guidmappings STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/UiBackend/GuidMappings/CachedGuidToIdMapper.cpp"
  "${ETLFORGE_INTEGRATION}/konga/UiBackend/GuidMappings/DbGuidToIdMapper.cpp"
  "${ETLFORGE_INTEGRATION}/konga/UiBackend/GuidMappings/GuidMappings.cpp"
  "${ETLFORGE_INTEGRATION}/konga/UiBackend/GuidMappings/GuidToIdMapper.cpp"
)
target_include_directories(etlforge_runtime_guidmappings PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_runtime_guidmappings PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_runtime_workflow STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/Workflow/Activity.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Workflow/ActivityRule.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Workflow/Document.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Workflow/Escalation.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Workflow/node.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Workflow/Process.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Workflow/RuleTree.cpp"
  "${ETLFORGE_INTEGRATION}/konga/Workflow/Task.cpp"
)
target_include_directories(etlforge_runtime_workflow PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_runtime_workflow PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_runtime_javabackendconfiguration STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.cpp"
)
target_include_directories(etlforge_runtime_javabackendconfiguration PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_runtime_javabackendconfiguration PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_runtime_pluginmanager SHARED
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PipelinePluginInput.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PipelinePluginManifest.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PipelinePluginOutput.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PipelinePluginParser.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PipelinePluginSelector.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PipelinePosition.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PluginDataElementHolder.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PluginManager.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PluginManagerApi.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PluginManifestDbStore.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PluginManifestParser.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PluginManager/PluginSerializer.cpp"
)
target_include_directories(etlforge_runtime_pluginmanager PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_runtime_pluginmanager PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_compile_definitions(etlforge_runtime_pluginmanager PRIVATE FILTERMANAGER_EXPORTS)
add_library(etlforge_runtime_jmsutils STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/JmsUtils/JmsUtils.cpp"
)
target_include_directories(etlforge_runtime_jmsutils PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen)
target_compile_definitions(etlforge_runtime_jmsutils PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_runtime_pipelinepluginexecutor STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_INTEGRATION}/konga/PipelinePluginExecutor/OutputFiles.cpp"
  "${ETLFORGE_INTEGRATION}/konga/PipelinePluginExecutor/PipelinePluginExecutor.cpp"
)
target_include_directories(etlforge_runtime_pipelinepluginexecutor PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen)
target_compile_definitions(etlforge_runtime_pipelinepluginexecutor PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
