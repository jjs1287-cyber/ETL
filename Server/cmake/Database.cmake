add_library(etlforge_db_dbmaintenance_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/DbMaintenance/DbMaintenance.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbMaintenance/PgSqlBackupImpl.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbMaintenance/PgSqlMaintenance.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbMaintenance/PgSqlRestoreImpl.cpp"
)
target_include_directories(etlforge_db_dbmaintenance_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dbmaintenance_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dbmaintenance_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dbutils_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/ConnectionString.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/DbConnection.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/DbConnectionHolder.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/DbProcedureColumn.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/DbUtils.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/DynamicDbRecordSet.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/OdbcBoundValue.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/OdbcConnectionHolder.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/OdbcDbLookup.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/Sql_ODBC_Ext.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/StoredProcedureCaller.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DbUtils/TableColumnsOdbc.cpp"
)
target_include_directories(etlforge_db_dbutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dbutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dbutils_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dtldbutils_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/DtlDbUtils/DtlDbConnection.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DtlDbUtils/DtlDbLookup.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DtlDbUtils/DtlDbUtils.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DtlDbUtils/DtlDynamicDbRecordSet.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DtlDbUtils/RefCountedDtlDbConnection.cpp"
)
target_include_directories(etlforge_db_dtldbutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dtldbutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dtldbutils_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dtlidb_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/DtlIDb/DtlSpInsertProcessTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DtlIDb/DtlSpInsertRouteTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DtlIDb/DtlSpInsertSessionIdTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DtlIDb/DtlSpInsertTaskTab.cpp"
)
target_include_directories(etlforge_db_dtlidb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dtlidb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dtlidb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dtlmsgdb_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/DtlMsgDb/DtlSpInsertIncomingMsgTab.cpp"
)
target_include_directories(etlforge_db_dtlmsgdb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dtlmsgdb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dtlmsgdb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_dtltrandb_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/DtlTranDb/DtlSpInsertLogTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/DtlTranDb/DtlSpInsertOperationInstanceTab.cpp"
)
target_include_directories(etlforge_db_dtltrandb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_dtltrandb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_dtltrandb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_idb_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ActivityTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ArchiveActivityTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ArchiveCRIDBFinal.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ArchiveDataElementTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ArchiveDataElementTextTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ArchiveProcessTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ArchiveRouteTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ArchiveTaskTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/DataElementTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/DataElementTextTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/InstanceInfoTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/OperationDataElementTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/ProcessTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/RouteDataElementTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/RouteDataElementTextTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/RouteTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/SpInsertProcessTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/SpInsertRouteTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/SpInsertSessionIdTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/SpInsertTaskTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/StatusTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/TaskTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/IDb/TemporaryFileTab.cpp"
)
target_include_directories(etlforge_db_idb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_idb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_idb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_msgdb_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ArchiveAttachmentTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ArchiveCRMSGDBFinal.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ArchiveDataElementTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ArchiveDataElementTextTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ArchiveDistributionJobInstanceTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ArchiveMsgTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ArchiveOutgoingMsgTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/AttachmentTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/DataElementTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/DataElementTextTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/DistributionJobInstanceTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/DistributionJobTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/DistributionTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ErrorDataElementTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ErrorDataElementTextTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/ErrorMsgTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/EventMsgTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/IncomingMsgTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/OutgoingMsgTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/PermanentErrorMsgTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/POPAccountsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/RecommendationsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/SettingsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/SpInsertIncomingMsgTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/MsgDb/TransientErrorMsgTab.cpp"
)
target_include_directories(etlforge_db_msgdb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_msgdb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_msgdb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_pdb_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/ActivityRuleTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/ActivityTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/ArchiveCRPDBFinal.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/ArgumentTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/AttachmentTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/CryptoTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/DocumentTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/FunctionTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/GetLogGUIDProcID.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/MultiServerSetupTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/ObjectPermissionsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/OrganizationStructureTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/ProcessTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/RouteTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/RoutingRuleTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/TaskDataElementsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/TaskDisplayAttributes.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/TaskTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/PDb/TransferTab.cpp"
)
target_include_directories(etlforge_db_pdb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_pdb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_pdb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_schemainfo_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/SchemaInfo/DatabaseObject.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/SchemaInfo/SchemaInfo.cpp"
)
target_include_directories(etlforge_db_schemainfo_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_schemainfo_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_schemainfo_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_trandb_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/DBFieldsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/DBTablesTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/Document_DefinitionsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/DocumentsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/FolderItemTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/HistoryLogsView.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/interchangeprojectfolderstab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/interchangeprojectoperationstab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/JmsProvidersTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/Key_Values.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/LogAuxTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/MessagesTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/OperationLogsView.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/OperationsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/PipelinePluginInstanceTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/PipelinePluginPositionTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/PluginsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/ProjectOperationsView.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/ScheduledOperationView.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/ScheduleTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/ScriptsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/SourceDtdView.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/SourcesTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/SpInsertLogTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/SpInsertOperationInstanceTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/SqlScriptTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/TargetDtdView.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/TargetsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/Tran_RelationsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/Trans_FilterTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/Transformation_DefinitionsTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/TransformationChainLinksTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/TransformationFunctionTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/TRANSFORMATIONSTAB_V.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/UsersTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/WebServicesTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/WebServiceView2.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/WsdlTab.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/XferHist.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/XferHistoryLogsView.cpp"
  "C:/ETL/Server/src/native/integration/konga/Db/TranDb/XmlDtdChildrenView.cpp"
)
target_include_directories(etlforge_db_trandb_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_trandb_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_trandb_probe PUBLIC etlforge_dtl)
add_library(etlforge_db_trandbutils_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/integration/konga/Db/TranDbUtils/TranDbUtils.cpp"
)
target_include_directories(etlforge_db_trandbutils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_db_trandbutils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_db_trandbutils_probe PUBLIC etlforge_dtl)
add_custom_target(etlforge_database_probes DEPENDS etlforge_db_dbmaintenance_probe etlforge_db_dbutils_probe etlforge_db_dtldbutils_probe etlforge_db_dtlidb_probe etlforge_db_dtlmsgdb_probe etlforge_db_dtltrandb_probe etlforge_db_idb_probe etlforge_db_msgdb_probe etlforge_db_pdb_probe etlforge_db_schemainfo_probe etlforge_db_trandb_probe etlforge_db_trandbutils_probe)
