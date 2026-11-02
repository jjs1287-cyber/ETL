set(hosted_dir "${ETLFORGE_INTEGRATION}/konga/mod_konga_ws")
set(apache_root "${ETLFORGE_ROOT}/Server/runtime/apache-httpd-2.4.69/Apache24")
add_library(etlforge_hosted_webservice MODULE EXCLUDE_FROM_ALL
 "${hosted_dir}/ETLForgeHostedModule.cpp"
 "${hosted_dir}/ApacheUtils.cpp"
 "${hosted_dir}/HandleHostedWebService.cpp"
 "${hosted_dir}/HandleHttpTrigger.cpp"
 "${hosted_dir}/HandleHttpEndpoint.cpp"
 "${ETLFORGE_INTEGRATION}/konga/HttpEndpoint/HttpTrigger.cpp"
 "${ETLFORGE_INTEGRATION}/konga/HttpEndpoint/HttpEndpoint.cpp"
 "${ETLFORGE_INTEGRATION}/konga/HttpEndpoint/HttpEndpointSelector.cpp"
 "${ETLFORGE_INTEGRATION}/konga/HttpEndpoint/HttpEndpointResponseMode.cpp"
 "${hosted_dir}/WsdlUtils.cpp"
 "${ETLFORGE_INTEGRATION}/konga/Authentication/Authentication.cpp")
get_target_property(hosted_includes etlforge_operation_worker INCLUDE_DIRECTORIES)
get_target_property(hosted_libraries etlforge_operation_worker LINK_LIBRARIES)
target_include_directories(etlforge_hosted_webservice PRIVATE ${hosted_includes} "${apache_root}/include")
target_compile_definitions(etlforge_hosted_webservice PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_compile_options(etlforge_hosted_webservice PRIVATE /FIwinsock2.h /FIws2tcpip.h)
target_link_libraries(etlforge_hosted_webservice PRIVATE ${hosted_libraries} "${apache_root}/lib/libhttpd.lib" "${apache_root}/lib/libapr-1.lib" "${apache_root}/lib/libaprutil-1.lib")
set_target_properties(etlforge_hosted_webservice PROPERTIES PREFIX "" SUFFIX ".so" OUTPUT_NAME mod_etlforge_hosted RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin" LIBRARY_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/dist/bin")
add_dependencies(etlforge_frontend_services etlforge_hosted_webservice)
