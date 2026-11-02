if(BUILD_TESTING)
 add_executable(etlforge_xml_dom_tests tests/native/xml_dom_tests.cpp)
 target_include_directories(etlforge_xml_dom_tests PRIVATE "${ETLFORGE_COMMON}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src)
 target_compile_definitions(etlforge_xml_dom_tests PRIVATE BOOST_ALL_NO_LIB NOMINMAX)
 target_link_libraries(etlforge_xml_dom_tests PRIVATE etlforge_common_xml_probe etlforge_legacy_utils_probe etlforge_windows_support XercesC::XercesC
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/Xalan-C_1.lib)
 foreach(component filesystem thread chrono regex date_time system atomic)
  target_link_libraries(etlforge_xml_dom_tests PRIVATE "${ETLFORGE_ROOT}/Dependencies/boost-x64/lib/libboost_${component}-vc143-mt-x64-1_86.lib")
 endforeach()
 target_link_libraries(etlforge_xml_dom_tests PRIVATE advapi32 bcrypt synchronization rpcrt4 shlwapi psapi pdh version ws2_32 netapi32)
 add_test(NAME native.xml_dom COMMAND etlforge_xml_dom_tests)
 set_tests_properties(native.xml_dom PROPERTIES ENVIRONMENT_MODIFICATION
 "PATH=path_list_prepend:${ETLFORGE_ROOT}/Dependencies/windows-x64/bin;PATH=path_list_prepend:${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc;PATH=path_list_prepend:${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/Utils/XalanMsgLib")
endif()
