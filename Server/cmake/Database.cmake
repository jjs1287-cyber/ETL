add_library(etlforge_db_dbmaintenance_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbMaintenance/DbMaintenance.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbMaintenance/PgSqlBackupImpl.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbMaintenance/PgSqlMaintenance.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbMaintenance/PgSqlRestoreImpl.cpp"
)
target_include_directories(etlforge_db_dbmaintenance_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dbmaintenance_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dbmaintenance_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dbutils_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/ConnectionString.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/DbConnection.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/DbConnectionHolder.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/DbProcedureColumn.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/DbUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/DynamicDbRecordSet.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/OdbcBoundValue.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/OdbcConnectionHolder.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/OdbcDbLookup.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/Sql_ODBC_Ext.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/StoredProcedureCaller.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DbUtils/TableColumnsOdbc.cpp"
)
target_include_directories(etlforge_db_dbutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dbutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dbutils_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dtldbutils_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlDbUtils/DtlDbConnection.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlDbUtils/DtlDbLookup.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlDbUtils/DtlDbUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlDbUtils/DtlDynamicDbRecordSet.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlDbUtils/RefCountedDtlDbConnection.cpp"
)
target_include_directories(etlforge_db_dtldbutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dtldbutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dtldbutils_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dtlidb_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlIDb/DtlSpInsertProcessTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlIDb/DtlSpInsertRouteTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlIDb/DtlSpInsertSessionIdTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlIDb/DtlSpInsertTaskTab.cpp"
)
target_include_directories(etlforge_db_dtlidb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dtlidb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dtlidb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dtlmsgdb_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlMsgDb/DtlSpInsertIncomingMsgTab.cpp"
)
target_include_directories(etlforge_db_dtlmsgdb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dtlmsgdb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dtlmsgdb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dtltrandb_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlTranDb/DtlSpInsertLogTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/DtlTranDb/DtlSpInsertOperationInstanceTab.cpp"
)
target_include_directories(etlforge_db_dtltrandb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dtltrandb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dtltrandb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_idb_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ActivityTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ArchiveActivityTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ArchiveCRIDBFinal.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ArchiveDataElementTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ArchiveDataElementTextTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ArchiveProcessTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ArchiveRouteTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ArchiveTaskTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/DataElementTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/DataElementTextTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/InstanceInfoTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/OperationDataElementTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/ProcessTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/RouteDataElementTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/RouteDataElementTextTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/RouteTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/SpInsertProcessTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/SpInsertRouteTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/SpInsertSessionIdTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/SpInsertTaskTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/StatusTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/TaskTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/IDb/TemporaryFileTab.cpp"
)
target_include_directories(etlforge_db_idb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_idb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_idb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_msgdb_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ArchiveAttachmentTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ArchiveCRMSGDBFinal.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ArchiveDataElementTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ArchiveDataElementTextTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ArchiveDistributionJobInstanceTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ArchiveMsgTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ArchiveOutgoingMsgTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/AttachmentTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/DataElementTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/DataElementTextTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/DistributionJobInstanceTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/DistributionJobTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/DistributionTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ErrorDataElementTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ErrorDataElementTextTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/ErrorMsgTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/EventMsgTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/IncomingMsgTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/OutgoingMsgTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/PermanentErrorMsgTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/POPAccountsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/RecommendationsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/SettingsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/SpInsertIncomingMsgTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/MsgDb/TransientErrorMsgTab.cpp"
)
target_include_directories(etlforge_db_msgdb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_msgdb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_msgdb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_pdb_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/ActivityRuleTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/ActivityTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/ArchiveCRPDBFinal.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/ArgumentTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/AttachmentTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/CryptoTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/DocumentTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/FunctionTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/GetLogGUIDProcID.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/MultiServerSetupTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/ObjectPermissionsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/OrganizationStructureTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/ProcessTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/RouteTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/RoutingRuleTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/TaskDataElementsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/TaskDisplayAttributes.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/TaskTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/PDb/TransferTab.cpp"
)
target_include_directories(etlforge_db_pdb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_pdb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_pdb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_schemainfo_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/SchemaInfo/DatabaseObject.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/SchemaInfo/SchemaInfo.cpp"
)
target_include_directories(etlforge_db_schemainfo_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_schemainfo_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_schemainfo_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_trandb_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/DBFieldsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/DBTablesTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/Document_DefinitionsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/DocumentsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/FolderItemTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/HistoryLogsView.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/interchangeprojectfolderstab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/interchangeprojectoperationstab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/JmsProvidersTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/Key_Values.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/LogAuxTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/MessagesTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/OperationLogsView.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/OperationsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/PipelinePluginInstanceTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/PipelinePluginPositionTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/PluginsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/ProjectOperationsView.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/ScheduledOperationView.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/ScheduleTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/ScriptsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/SourceDtdView.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/SourcesTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/SpInsertLogTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/SpInsertOperationInstanceTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/SqlScriptTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/TargetDtdView.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/TargetsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/Tran_RelationsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/Trans_FilterTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/Transformation_DefinitionsTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/TransformationChainLinksTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/TransformationFunctionTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/TRANSFORMATIONSTAB_V.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/UsersTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/WebServicesTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/WebServiceView2.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/WsdlTab.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/XferHist.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/XferHistoryLogsView.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDb/XmlDtdChildrenView.cpp"
)
target_include_directories(etlforge_db_trandb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_trandb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_trandb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_trandbutils_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/integration/konga/Db/TranDbUtils/TranDbUtils.cpp"
)
target_include_directories(etlforge_db_trandbutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_trandbutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_trandbutils_probe PUBLIC etlforge_dtl)
add_custom_target(etlforge_database_probes DEPENDS etlforge_db_dbmaintenance_probe etlforge_db_dbutils_probe etlforge_db_dtldbutils_probe etlforge_db_dtlidb_probe etlforge_db_dtlmsgdb_probe etlforge_db_dtltrandb_probe etlforge_db_idb_probe etlforge_db_msgdb_probe etlforge_db_pdb_probe etlforge_db_schemainfo_probe etlforge_db_trandb_probe etlforge_db_trandbutils_probe)
