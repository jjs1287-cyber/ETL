# Windows platform support used by the future service hosts.
add_library(etlforge_windows_support STATIC
  "${ETLFORGE_COMMON}/konga/WinUtils/RegistryUtils.cpp"
  "${ETLFORGE_COMMON}/konga/WinUtils/ServiceUtils.cpp"
  "${ETLFORGE_COMMON}/konga/WinUtils/WinErrorUtils.cpp"
  "${ETLFORGE_COMMON}/konga/WinUtils/StackWalker.cpp"
  "${ETLFORGE_COMMON}/konga/WinUtils/KongaStackWalker.cpp"
  "${ETLFORGE_COMMON}/konga/WinUtils/WinStringUtils.cpp"
  "${ETLFORGE_COMMON}/konga/JavaUtils/JavaVersionUtils.cpp"
  "${ETLFORGE_COMMON}/konga/SysInfo/SysInfo.cpp"
  "${ETLFORGE_COMMON}/konga/SysInfo/arch/windows/WinSysInfo.cpp")
target_include_directories(etlforge_windows_support PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_ROOT}/Dependencies/boost_1_86_0")
target_compile_definitions(etlforge_windows_support PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
target_link_libraries(etlforge_windows_support PUBLIC advapi32 shlwapi ole32 oleaut32 wbemuuid)
if(BUILD_TESTING)
  add_executable(etlforge_windows_support_tests tests/native/windows_support_tests.cpp)
  target_include_directories(etlforge_windows_support_tests PRIVATE "${ETLFORGE_COMMON}")
  target_link_libraries(etlforge_windows_support_tests PRIVATE etlforge_windows_support)
  add_test(NAME native.windows_support COMMAND etlforge_windows_support_tests)
endif()
