add_library(etlforge_common_xml_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/Xml/CrXmlException.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMAttrPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMCDATASectionPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMCharacterDataPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMDocument_ptr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMDocumentPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMDocumentTypePtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMElementListPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMElementPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMNamedNodeMapPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMNodeListPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMNodePtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMProcessingInstructionPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/DOMTextPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/RCDocPtr.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/SimpleDOMErrorHandler.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/SimpleSAXHandler.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/XercesXmlUtils.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/XmlNamespaceDeclarations.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/XMLOstreamWriter.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/XmlParsingErrors.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/XmlSchemaUtils.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/XmlSchemaValidator.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/XmlStringBuilder.cpp"
  "C:/ETL/Server/src/native/common/konga/Xml/XmlUtils.cpp"
)
target_include_directories(etlforge_common_xml_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_xml_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_soap_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/Soap/SoapEncoding.cpp"
  "C:/ETL/Server/src/native/common/konga/Soap/SoapEnvelope.cpp"
  "C:/ETL/Server/src/native/common/konga/Soap/SoapEnvelopeCleaner.cpp"
  "C:/ETL/Server/src/native/common/konga/Soap/SoapFault.cpp"
  "C:/ETL/Server/src/native/common/konga/Soap/SoapFault_11.cpp"
  "C:/ETL/Server/src/native/common/konga/Soap/SoapFault_12.cpp"
)
target_include_directories(etlforge_common_soap_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_soap_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_characterencodingconversion_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/CharacterEncodingConversion/EncodingStringUtils.cpp"
  "C:/ETL/Server/src/native/common/konga/CharacterEncodingConversion/TextUnicodeConversion.cpp"
  "C:/ETL/Server/src/native/common/konga/CharacterEncodingConversion/Utf8Conv.cpp"
  "C:/ETL/Server/src/native/common/konga/CharacterEncodingConversion/Utf8TextInput.cpp"
)
target_include_directories(etlforge_common_characterencodingconversion_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_characterencodingconversion_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_log_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/Log/logging.cpp"
)
target_include_directories(etlforge_common_log_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_log_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_networking_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/Networking/AutoCleanupNetworking.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/BasicCommands.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/CommandListenerUsingSockets.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/CommandUtils.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/Communicator.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/CrStreamSocket.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/IPUtils.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/Packet.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/Request.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/Response.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/ServerStreamSocket.cpp"
  "C:/ETL/Server/src/native/common/konga/Networking/SockProtocol.cpp"
)
target_include_directories(etlforge_common_networking_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_networking_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_crnet_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/CrNet/CrNet.cpp"
)
target_include_directories(etlforge_common_crnet_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_crnet_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_ldap_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/Ldap/konga_ldap.cpp"
  "C:/ETL/Server/src/native/common/konga/Ldap/LdapFilter.cpp"
)
target_include_directories(etlforge_common_ldap_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_ldap_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_jbparser_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/JbParser/JbNode.cpp"
  "C:/ETL/Server/src/native/common/konga/JbParser/JbParser.cpp"
)
target_include_directories(etlforge_common_jbparser_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_jbparser_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_zip_probe STATIC EXCLUDE_FROM_ALL
  "C:/ETL/Server/src/native/common/konga/Zip/ZipUtils.cpp"
)
target_include_directories(etlforge_common_zip_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Dependencies/windows-x64/include
 C:/ETL/Dependencies/icu-78.3-x64/include C:/ETL/Tools/vcpkg/installed/x64-windows/include
 C:/ETL/Dependencies/xalan_c-1.12/src C:/ETL/Dependencies/build/xalan-x64/src
 C:/ETL/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 C:/ETL/Tools/jdk8u504-b01/include C:/ETL/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_zip_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_custom_target(etlforge_common_probes DEPENDS etlforge_common_xml_probe etlforge_common_soap_probe etlforge_common_characterencodingconversion_probe etlforge_common_log_probe etlforge_common_networking_probe etlforge_common_crnet_probe etlforge_common_ldap_probe etlforge_common_jbparser_probe etlforge_common_zip_probe)

set_property(TARGET etlforge_common_characterencodingconversion_probe PROPERTY CXX_STANDARD 17)
target_compile_definitions(etlforge_common_crnet_probe PRIVATE CRNET_EXPORTS)
