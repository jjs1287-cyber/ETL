from pathlib import Path
import shutil
root=Path('C:/ETL')
backup=root/'Archives/sftp-keys-20261029'
paths=['Server/src/native/common/konga/FileTransfer/Ftp.h','Server/src/native/common/konga/FileTransfer/Ftp.cpp','Server/src/native/common/konga/FileTransfer/TransferLibWrapper.h','Server/src/native/common/konga/FileTransfer/CurlTransferLibWrapper.h','Server/src/native/common/konga/FileTransfer/CurlTransferLibWrapper.cpp','Server/src/native/integration/konga/OperationObjects/FtpLocation.cpp','Server/src/native/audit/AuditService.cpp','Server/cmake/RemainingCore.cmake','Server/cmake/FrontendServices.cmake','Studio/src/main/java/org/etlforge/integration/data/location/FtpLocation.java','Studio/src/main/java/org/etlforge/integration/client/ui/interchange/location/ftp/FtpLocationPanel.java','Studio/src/main/resources/etlforge-client-config/ApplicationContext.Admin.Common.xml']
for p in paths:
 d=backup/p;d.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(root/p,d)
from pathlib import Path
root=Path('C:/ETL')
def edit(p,f):
 p=root/p;s=p.read_text(encoding='utf-8');p.write_text(f(s),encoding='utf-8')
base='Server/src/native/common/konga/FileTransfer/'
for file in ['TransferLibWrapper.h','CurlTransferLibWrapper.h']:
 edit(base+file,lambda s:s.replace('void setSshPublicKeyFileName',('virtual void configureSftp(const std::string& keyFile, const std::string& publicFile, const std::string& passphrase, const std::string& fingerprint) = 0;\n\t\tvirtual void setSshPublicKeyFileName') if file=='TransferLibWrapper.h' else 'void configureSftp(const std::string& keyFile, const std::string& publicFile, const std::string& passphrase, const std::string& fingerprint);\n\t\tvoid setSshPublicKeyFileName',1).replace('virtual virtual','virtual'))
edit(base+'CurlTransferLibWrapper.cpp',lambda s:s.replace('void CurlTransferLibWrapper::setSshPublicKeyFileName', '''void CurlTransferLibWrapper::configureSftp(const string& keyFile,const string& publicFile,const string& passphrase,const string& fingerprint) {
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_SSH_HOST_PUBLIC_KEY_SHA256,fingerprint.c_str()));
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_SSH_AUTH_TYPES,keyFile.empty()?CURLSSH_AUTH_PASSWORD:CURLSSH_AUTH_PUBLICKEY));
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_SSH_PRIVATE_KEYFILE,keyFile.empty()?"":keyFile.c_str()));
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_SSH_PUBLIC_KEYFILE,publicFile.empty()?"":publicFile.c_str()));
 HandleCurlError(curl_easy_setopt(m_handle,CURLOPT_KEYPASSWD,passphrase.c_str()));
}

void CurlTransferLibWrapper::setSshPublicKeyFileName''',1))
edit(base+'Ftp.h',lambda s:s.replace('#include "InetBase.h"','#include "InetBase.h"\n#include <memory>\n#include "../../../../ssh/SshKeys.h"').replace('void setTransferType','void configureSftp(const std::string& keyId, const std::string& fingerprint);\n\n\t\tvoid setTransferType',1).replace('bool m_bUsePassiveMode;','std::shared_ptr<etlforge::ssh::KeyLease> m_sshKey;\n\t\tstd::string m_sshFingerprint;\n\t\tbool m_bUsePassiveMode;'))
edit(base+'Ftp.cpp',lambda s:s.replace('void Ftp::setSSHKeyFileOptions() {','''void Ftp::configureSftp(const string& keyId,const string& fingerprint) {
 try {
  m_sshFingerprint=etlforge::ssh::validateHostFingerprint(fingerprint);
  m_sshKey=keyId.empty()?nullptr:etlforge::ssh::lease(konga::GetServerHomeWithSlash()+"private/ssh-keys",keyId);
 } catch(const std::exception& ex) {throw CrException(ex.what());}
}

void Ftp::setSSHKeyFileOptions() {''').replace('const string& pwd = GetPassword();\n\t\tif ( pwd.empty() ) setSSHKeyFileOptions();','''if(m_sshFingerprint.empty())throw CrException("SFTP requires a verified SHA256 host fingerprint in the source or target settings.");
        m_pTransferLib->configureSftp(m_sshKey?m_sshKey->privateFile:"",m_sshKey?m_sshKey->publicFile:"",m_sshKey?m_sshKey->passphrase:"",m_sshFingerprint);'''))
edit('Server/src/native/integration/konga/OperationObjects/FtpLocation.cpp',lambda s:s.replace('ftp.SetPassword(getDecryptedPassword());','ftp.SetPassword(m_extendedProperties.GetString("sftp_key_id", "").empty()?getDecryptedPassword():"");').replace('return stPath;','if(ftp.isSftp())ftp.configureSftp(m_extendedProperties.GetString("sftp_key_id", ""),m_extendedProperties.GetString("sftp_host_sha256", ""));\n\treturn stPath;',1))
edit('Server/src/native/audit/AuditService.cpp',lambda s:s.replace('#include "AuditService.h"','#include "AuditService.h"\n#include "../ssh/SshKeys.h"\n#include <konga/Utils/Settings.h>').replace('AuditSession(const char* user,const char* password):','AuditSession(const char* user,const char* password,bool requireAdmin=true):').replace('if(!m_UserInfo.IsAdministrator())','if(requireAdmin&&!m_UserInfo.IsAdministrator())').replace('long long id()const','bool administrator()const{return m_UserInfo.IsAdministrator();}\n    long long id()const').replace('    AuditSession session(user,password);\n','').replace('    auto request=boost::json::parse(payload).as_object();','''    auto request=boost::json::parse(payload).as_object();
    auto requestedAction=request.if_contains("action");
    bool listKeys=requestedAction&&requestedAction->is_string()&&requestedAction->as_string()=="ssh.list";
    AuditSession session(user,password,!listKeys);
    if(requestedAction&&requestedAction->is_string()&&requestedAction->as_string().starts_with("ssh."))
        return etlforge::ssh::request(konga::GetServerHomeWithSlash()+"private/ssh-keys",payload,session.administrator());'''))
# boost string_view starts_with supported
edit('Server/cmake/RemainingCore.cmake',lambda s:s+'''
add_library(etlforge_ssh_keys STATIC src/native/ssh/SshKeys.cpp)
target_compile_features(etlforge_ssh_keys PRIVATE cxx_std_17)
target_compile_definitions(etlforge_ssh_keys PRIVATE NOMINMAX WIN32_LEAN_AND_MEAN BOOST_ALL_NO_LIB)
target_include_directories(etlforge_ssh_keys PRIVATE C:/ETL/Dependencies/boost_1_86_0 C:/ETL/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_ssh_keys PUBLIC crypt32 advapi32 ole32 C:/ETL/Tools/vcpkg/installed/x64-windows/lib/libcrypto.lib)
target_link_libraries(etlforge_common_filetransfer_probe PUBLIC etlforge_ssh_keys)
''')
edit('Server/cmake/FrontendServices.cmake',lambda s:s+'\ntarget_link_libraries(etlforge_frontend_admin PRIVATE etlforge_ssh_keys)\n')
