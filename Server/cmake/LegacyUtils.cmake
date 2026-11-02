add_library(etlforge_legacy_utils_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_COMMON}/konga/Utils/AutoFileCleanup.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/BinaryData.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/BinaryDataFileIo.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/CancelHandlers.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/CsvUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/date.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/DateUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/DBConnectionInfo.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/DbTypeUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/EbcdicToAscii.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/ErrorResolver.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/FileContents.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/FileLineReadWriter.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/FilesTraverser.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/FileTokenizer.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/FileUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/HashTable.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/KeyValues.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/KString.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/MemUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/ObjectAccessControl.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/OdbcConnectionStringUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/ProcessUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/Random.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/RCStringTable.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/RegExUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/Settings.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/SimpleFileFilterFileMatcher.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/StringBuilder.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/StringTokenizer.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/StringUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/ThreadUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/Timer.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/Timing.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/Url.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/util.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/VectorUtils.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/Version.cpp"
  "${ETLFORGE_COMMON}/konga/Utils/XmlInit.cpp"
)
target_include_directories(etlforge_legacy_utils_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}")
target_compile_definitions(etlforge_legacy_utils_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS)
if(ETLFORGE_DEPENDENCY_INCLUDE_DIRS)
  target_include_directories(etlforge_legacy_utils_probe PRIVATE ${ETLFORGE_DEPENDENCY_INCLUDE_DIRS})
endif()
if(EXISTS "${ETLFORGE_ROOT}/Dependencies/boost_1_86_0/boost/version.hpp")
  target_include_directories(etlforge_legacy_utils_probe PRIVATE "${ETLFORGE_ROOT}/Dependencies/boost_1_86_0")
  target_compile_definitions(etlforge_legacy_utils_probe PRIVATE BOOST_ALL_NO_LIB)
endif()

target_include_directories(etlforge_legacy_utils_probe PRIVATE "${ETLFORGE_ROOT}/Dependencies/getpot-legacy")
if(EXISTS "${ETLFORGE_ROOT}/Dependencies/windows-x64/include/xercesc/util/XercesVersion.hpp" AND CMAKE_SIZEOF_VOID_P EQUAL 8)
  target_include_directories(etlforge_legacy_utils_probe PRIVATE "${ETLFORGE_ROOT}/Dependencies/windows-x64/include")
endif()


if(EXISTS "${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/Include/PlatformDefinitions.hpp")
  target_include_directories(etlforge_legacy_utils_probe PRIVATE
    "${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src"
    "${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src")
endif()
target_include_directories(etlforge_legacy_utils_probe PRIVATE "${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen")


set_source_files_properties("${ETLFORGE_COMMON}/konga/Utils/FileUtils.cpp" PROPERTIES COMPILE_OPTIONS "/std:c++17" COMPILE_DEFINITIONS "_HAS_AUTO_PTR_ETC=1")
set_source_files_properties("${ETLFORGE_COMMON}/konga/Utils/FilesTraverser.cpp" PROPERTIES COMPILE_OPTIONS "/std:c++17" COMPILE_DEFINITIONS "_HAS_AUTO_PTR_ETC=1")
