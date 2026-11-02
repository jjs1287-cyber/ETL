add_executable(etlforge_cloud_logging src/native/cloudlogging/Main.cpp src/native/audit/AuditJson.cpp)
target_compile_features(etlforge_cloud_logging PRIVATE cxx_std_17)
target_compile_definitions(etlforge_cloud_logging PRIVATE NOMINMAX WIN32_LEAN_AND_MEAN _WIN32_WINNT=0x0602 BOOST_ALL_NO_LIB)
target_include_directories(etlforge_cloud_logging PRIVATE
 "${CMAKE_CURRENT_SOURCE_DIR}/../Dependencies/boost_1_86_0"
 "${CMAKE_CURRENT_SOURCE_DIR}/../Tools/vcpkg/installed/x64-windows/include"
 "${CMAKE_CURRENT_SOURCE_DIR}/runtime/cloud-logging/include")
target_link_libraries(etlforge_cloud_logging PRIVATE
 "${CMAKE_CURRENT_SOURCE_DIR}/../Tools/vcpkg/installed/x64-windows/lib/libcurl.lib"
 "${CMAKE_CURRENT_SOURCE_DIR}/runtime/postgresql-18/pgsql/lib/libpq.lib"
 advapi32 ole32 ws2_32 mpr
 "${CMAKE_CURRENT_SOURCE_DIR}/../Tools/vcpkg/installed/x64-windows/lib/libcrypto.lib")
set_target_properties(etlforge_cloud_logging PROPERTIES OUTPUT_NAME ETLForgeCloudLogging RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/runtime/cloud-logging")
add_executable(etlforge_cloud_logging_tests EXCLUDE_FROM_ALL tests/native/cloud_logging_tests.cpp src/native/audit/AuditJson.cpp)
get_target_property(cloud_includes etlforge_cloud_logging INCLUDE_DIRECTORIES)
get_target_property(cloud_libraries etlforge_cloud_logging LINK_LIBRARIES)
get_target_property(cloud_definitions etlforge_cloud_logging COMPILE_DEFINITIONS)
target_include_directories(etlforge_cloud_logging_tests PRIVATE ${cloud_includes})
target_link_libraries(etlforge_cloud_logging_tests PRIVATE ${cloud_libraries})
target_compile_definitions(etlforge_cloud_logging_tests PRIVATE ${cloud_definitions})
target_compile_features(etlforge_cloud_logging_tests PRIVATE cxx_std_17)
set_target_properties(etlforge_cloud_logging_tests PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/runtime/cloud-logging")
