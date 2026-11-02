add_library(etlforge_dtl STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/bind_basics.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/BoundIO.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/clib_fwd.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/CountedPtr.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/date_util.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/DB_Base.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/DBConnection.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/DBException.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/DBStmt.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/DBView.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/dtl_base_types.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/LocalBCA.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/RootException.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/string_util.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/validate.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/variant_row.cpp"
  "C:/ETL/Dependencies/dtl-3.7/work/dtl/VariantException.cpp"
)
target_include_directories(etlforge_dtl PUBLIC C:/ETL/Dependencies/dtl-3.7/work)
target_compile_definitions(etlforge_dtl PRIVATE WIN32 NOMINMAX _CRT_SECURE_NO_WARNINGS)
target_link_libraries(etlforge_dtl PUBLIC odbc32 odbccp32)
if(BUILD_TESTING)
 add_executable(etlforge_dtl_tests tests/native/dtl_tests.cpp)
 target_compile_definitions(etlforge_dtl_tests PRIVATE WIN32 NOMINMAX _CRT_SECURE_NO_WARNINGS)
 target_link_libraries(etlforge_dtl_tests PRIVATE etlforge_dtl)
 add_test(NAME native.dtl COMMAND etlforge_dtl_tests)
endif()
