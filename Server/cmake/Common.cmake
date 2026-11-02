add_library(etlforge_common_xml_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/CrXmlException.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMAttrPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMCDATASectionPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMCharacterDataPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMDocument_ptr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMDocumentPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMDocumentTypePtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMElementListPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMElementPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMNamedNodeMapPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMNodeListPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMNodePtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMProcessingInstructionPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/DOMTextPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/RCDocPtr.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/SimpleDOMErrorHandler.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/SimpleSAXHandler.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/XercesXmlUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/XmlNamespaceDeclarations.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/XMLOstreamWriter.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/XmlParsingErrors.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/XmlSchemaUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/XmlSchemaValidator.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/XmlStringBuilder.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Xml/XmlUtils.cpp"
)
target_include_directories(etlforge_common_xml_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_xml_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_soap_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Soap/SoapEncoding.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Soap/SoapEnvelope.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Soap/SoapEnvelopeCleaner.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Soap/SoapFault.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Soap/SoapFault_11.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Soap/SoapFault_12.cpp"
)
target_include_directories(etlforge_common_soap_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_soap_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_characterencodingconversion_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/CharacterEncodingConversion/EncodingStringUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/CharacterEncodingConversion/TextUnicodeConversion.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/CharacterEncodingConversion/Utf8Conv.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/CharacterEncodingConversion/Utf8TextInput.cpp"
)
target_include_directories(etlforge_common_characterencodingconversion_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_characterencodingconversion_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_log_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Log/logging.cpp"
)
target_include_directories(etlforge_common_log_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_log_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_networking_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/AutoCleanupNetworking.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/BasicCommands.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/CommandListenerUsingSockets.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/CommandUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/Communicator.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/CrStreamSocket.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/IPUtils.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/Packet.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/Request.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/Response.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/ServerStreamSocket.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Networking/SockProtocol.cpp"
)
target_include_directories(etlforge_common_networking_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_networking_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_crnet_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/CrNet/CrNet.cpp"
)
target_include_directories(etlforge_common_crnet_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_crnet_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_ldap_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Ldap/konga_ldap.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Ldap/LdapFilter.cpp"
)
target_include_directories(etlforge_common_ldap_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_ldap_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_jbparser_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/JbParser/JbNode.cpp"
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/JbParser/JbParser.cpp"
)
target_include_directories(etlforge_common_jbparser_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_jbparser_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_zip_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_ROOT}/Server/src/native/common/konga/Zip/ZipUtils.cpp"
)
target_include_directories(etlforge_common_zip_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}"
 ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include
 ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include
 ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src
 ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen
 ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_zip_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_custom_target(etlforge_common_probes DEPENDS etlforge_common_xml_probe etlforge_common_soap_probe etlforge_common_characterencodingconversion_probe etlforge_common_log_probe etlforge_common_networking_probe etlforge_common_crnet_probe etlforge_common_ldap_probe etlforge_common_jbparser_probe etlforge_common_zip_probe)

set_property(TARGET etlforge_common_characterencodingconversion_probe PROPERTY CXX_STANDARD 17)
target_compile_definitions(etlforge_common_crnet_probe PRIVATE CRNET_EXPORTS)
