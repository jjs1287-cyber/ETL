add_library(etlforge_common_debugger_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_COMMON}/konga/Debugger/CallStack.cpp"
  "${ETLFORGE_COMMON}/konga/Debugger/Debuggee.cpp"
)
target_include_directories(etlforge_common_debugger_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_debugger_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_filetransfer_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_COMMON}/konga/FileTransfer/CurlTransferLibWrapper.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/FileRetriever.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/Ftp.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/ftpparse.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/Http.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/HttpBase.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/HttpOptions.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/Https.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/InetBase.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/Protocol.cpp"
  "${ETLFORGE_COMMON}/konga/FileTransfer/Samba.cpp"
)
target_include_directories(etlforge_common_filetransfer_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_filetransfer_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_mailclient_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_COMMON}/konga/MailClient/EmailAddress.cpp"
  "${ETLFORGE_COMMON}/konga/MailClient/EmailProperties.cpp"
  "${ETLFORGE_COMMON}/konga/MailClient/rfc822Date.cpp"
  "${ETLFORGE_COMMON}/konga/MailClient/SmtpClientCurl.cpp"
)
target_include_directories(etlforge_common_mailclient_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_mailclient_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_mime_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_COMMON}/konga/Mime/Attachment.cpp"
  "${ETLFORGE_COMMON}/konga/Mime/CrMime.cpp"
  "${ETLFORGE_COMMON}/konga/Mime/Encoder.cpp"
  "${ETLFORGE_COMMON}/konga/Mime/mhtml.cpp"
  "${ETLFORGE_COMMON}/konga/Mime/Mime.cpp"
)
target_include_directories(etlforge_common_mime_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_mime_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_library(etlforge_common_webservice_probe STATIC EXCLUDE_FROM_ALL
  "${ETLFORGE_COMMON}/konga/WebService/RFC822HeadersWithParameters.cpp"
  "${ETLFORGE_COMMON}/konga/WebService/SimpleWebServiceClientSoap11.cpp"
  "${ETLFORGE_COMMON}/konga/WebService/SimpleWebServiceClientSoap12.cpp"
  "${ETLFORGE_COMMON}/konga/WebService/SimpleWebServiceClientSoapBase.cpp"
)
target_include_directories(etlforge_common_webservice_probe PRIVATE "${ETLFORGE_COMMON}" "${ETLFORGE_INTEGRATION}" ${ETLFORGE_ROOT}/Dependencies/boost_1_86_0 ${ETLFORGE_ROOT}/Dependencies/windows-x64/include ${ETLFORGE_ROOT}/Tools/vcpkg/installed/x64-windows/include ${ETLFORGE_ROOT}/Dependencies/icu-78.3-x64/include ${ETLFORGE_ROOT}/Dependencies/xalan_c-1.12/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src ${ETLFORGE_ROOT}/Dependencies/build/xalan-x64/src/xalanc/NLS/gen ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include ${ETLFORGE_ROOT}/Tools/jdk8u504-b01/include/win32)
target_compile_definitions(etlforge_common_webservice_probe PRIVATE WIN32 NOMINMAX _WIN32_WINNT=0x0602 _CRT_SECURE_NO_WARNINGS BOOST_ALL_NO_LIB)
add_custom_target(etlforge_remaining_common_probes DEPENDS etlforge_common_debugger_probe etlforge_common_filetransfer_probe etlforge_common_mailclient_probe etlforge_common_mime_probe etlforge_common_webservice_probe)
target_link_libraries(etlforge_common_filetransfer_probe PUBLIC etlforge_ssh_keys)
