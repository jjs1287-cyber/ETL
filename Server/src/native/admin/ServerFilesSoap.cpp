#include "AuditDatabase.h"
#include "ServerFiles.h"
#include <filesystem>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/UiBackend/WebServices/Admin/AdminWrapper.hpp>
#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <memory>

namespace {
const char *serverFilesNamespace =
    "http://"
    "serveradmin.webservice.implementation.server.integration.etlforge.invalid";
std::string argument(IWrapperSoapDeSerializer *reader, const char *name) {
  xsd__string value = reader->getElementAsString(name, 0);
  std::string result = value ? value : "";
  if (value)
    Axis::AxisDelete(value, XSD_STRING);
  return result;
}
class FileAdministrator : public konga::WsImplBaseAuthenticator {
public:
  FileAdministrator(const std::string &user, const std::string &password)
      : WsImplBaseAuthenticator(user.c_str(), password.c_str()) {
    if (!m_UserInfo.IsAdministrator())
      throw konga::CrException("Server log and configuration administration "
                               "requires an administrator account.");
  }
};
using Locators = std::vector<etlforge::admin::FileLocator>;
void destroyLocators(Locators *values, bool array = false, int = 0) {
  if (array)
    delete[] values;
  else
    delete values;
}
int serializeLocators(Locators *values, IWrapperSoapSerializer *writer,
                      bool array = false) {
  bool fresh = false;
  const char *prefix = writer->getNamespacePrefix(serverFilesNamespace, fresh);
  if (!array && fresh)
    writer->serialize(" xmlns:", prefix, "=\"", serverFilesNamespace, "\"",
                      NULL);
  writer->serialize(">", NULL);
  writer->serialize("<", prefix, ":locators>", NULL);
  for (const auto &value : *values) {
    writer->serialize("<", prefix, ":item>", NULL);
    writer->serializeAsElement("path", serverFilesNamespace,
                               const_cast<char *>(value.path.c_str()),
                               XSD_STRING);
    writer->serializeAsElement("lastModified", serverFilesNamespace,
                               const_cast<char *>(value.modified.c_str()),
                               XSD_STRING);
    xsd__long size = value.size;
    writer->serializeAsElement("sizeInBytes", serverFilesNamespace, &size,
                               XSD_LONG);
    writer->serialize("</", prefix, ":item>", NULL);
  }
  writer->serialize("</", prefix, ":locators>", NULL);
  if (!array && fresh)
    writer->removeNamespacePrefix(serverFilesNamespace);
  return AXIS_SUCCESS;
}
std::vector<std::wstring> registeredBackups() {
  konga::SmartDbPtr<konga::DbConnection> connection;
  connection.Attach(konga::GetBackEndDbConnection("TranDb"));
  etlforge::audit::Database db(connection->GetConnectionHandle());
  std::vector<std::wstring> result;
  auto home =
      std::filesystem::path(etlforge::admin::wide(konga::GetServerHome()));
  for (const auto &row :
       db.query("SELECT encode(convert_to(uploadedfilename,'UTF8'),'hex') FROM "
                "projectbackuptab WHERE uploadedfilename IS NOT NULL")) {
    if (!row[0])
      continue;
    size_t length = 0;
    std::unique_ptr<unsigned char[]> bytes(
        konga::fromHexString(*row[0], length));
    auto relative = std::filesystem::path(etlforge::admin::wide(
        std::string(reinterpret_cast<char *>(bytes.get()), length)));
    if (relative.empty() || relative.is_absolute() || relative.has_root_name())
      continue;
    bool safe = true;
    for (const auto &part : relative)
      if (part == L".." || part == L".")
        safe = false;
    if (safe)
      result.push_back((home / relative).wstring());
  }
  return result;
}
} // namespace
int AdminWrapper::serverFiles(void *message, const char *method) {
  IMessageData *data = static_cast<IMessageData *>(message);
  IWrapperSoapSerializer *writer = 0;
  IWrapperSoapDeSerializer *reader = 0;
  data->getSoapSerializer(&writer);
  data->getSoapDeSerializer(&reader);
  if (!writer || !reader)
    return AXIS_FAIL;
  if (reader->checkMessageBody(method, serverFilesNamespace) != AXIS_SUCCESS)
    return AXIS_FAIL;
  std::string response = std::string(method) + "Response";
  writer->createSoapMethod(response.c_str(), serverFilesNamespace);
  try {
    const auto user = argument(reader, "user"),
               password = argument(reader, "password");
    FileAdministrator authenticated(user, password);
    etlforge::admin::ServerFiles files(
        etlforge::admin::wide(konga::GetServerHome()),
        etlforge::admin::wide(konga::GetOptionAsString("TransformationEngine",
                                                       "WriteLogDir", "")));
    std::string name(method), result;
    if (name == "downloadServerConfigurationFile") {
      if (reader->getStatus() != AXIS_SUCCESS)
        return AXIS_FAIL;
      result = etlforge::admin::pack(files.configuration());
    } else if (name == "uploadServerConfigurationFile") {
      auto encoded = argument(reader, "zippedAndBase64EncodedContents");
      if (reader->getStatus() != AXIS_SUCCESS)
        return AXIS_FAIL;
      files.saveConfiguration(etlforge::admin::unpackConfiguration(encoded));
      return AXIS_SUCCESS;
    } else if (name == "downloadFile") {
      auto path = argument(reader, "path");
      if (reader->getStatus() != AXIS_SUCCESS)
        return AXIS_FAIL;
      auto requested = etlforge::admin::wide(path);
      auto backups = files.isStandardDownload(requested)
                         ? std::vector<std::wstring>{}
                         : registeredBackups();
      result = etlforge::admin::pack(files.download(requested, backups));
    } else {
      std::string kind = name == "listServerLogFiles"      ? "server"
                         : name == "listPluginLogFiles"    ? "plugin"
                         : name == "listOperationLogFiles" ? "operation"
                                                           : "debug",
                  start, end;
      if (kind == "operation" || kind == "debug") {
        start = argument(reader, "startDate");
        end = argument(reader, "endDate");
      }
      if (reader->getStatus() != AXIS_SUCCESS)
        return AXIS_FAIL;
      std::unique_ptr<Locators> list(
          new Locators(files.list(kind, start, end)));
      std::string output = name + "Return";
      return writer->addOutputCmplxParam(
          list.release(), (void *)serializeLocators, (void *)destroyLocators,
          output.c_str(), serverFilesNamespace);
    }
    std::string output = name + "Return";
    writer->addOutputParam(output.c_str(), const_cast<char *>(result.c_str()),
                           XSD_STRING);
    return AXIS_SUCCESS;
  } catch (konga::CrException &error) {
    konga::SetSoapFault(*writer, method, error);
  } catch (const std::exception &error) {
    konga::SetSoapFault(*writer, method, serverFilesNamespace,
                        "Server file administration failed.", error.what());
  }
  throw AxisServiceException(AXISC_SERVICE_THROWN_EXCEPTION);
}
