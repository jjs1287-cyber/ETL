#pragma once
#include "TemporaryStorageSource.h"
#include "TemporaryStorageTarget.h"
#include "OperationEngineContext.h"
#include "OperationEngineException.h"
#include "ProcessedFiles.h"
#include <konga/DataElement/DataElement.h>
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>
#include <fstream>
#include <iterator>

namespace konga {
inline std::string etlforgeEndpointVariableName(std::string name) {
    if (!name.empty() && name[0]=='$') name.erase(0,1);
    if (name.empty() || name.size()>256 || name.compare(0,2,"__")==0)
        throw OperationEngineException("Invalid global variable endpoint name.");
    for (size_t i=0;i<name.size();++i) {
        unsigned char c=name[i];
        if (!((c>='A'&&c<='Z') || (c>='a'&&c<='z') || c=='_' || (i>0 && ((c>='0'&&c<='9') || c=='.'))))
            throw OperationEngineException("Invalid global variable endpoint name.");
    }
    return name;
}
inline void etlforgeEndpointText(const std::string& text) {
    try { etlforge::requireUtf8Text(text.data(),text.size()); }
    catch (const std::exception& error) { throw OperationEngineException(error.what()); }
    if (text.find('\0')!=std::string::npos) throw OperationEngineException("Global variable endpoints require text without NUL bytes.");
}
class GlobalVariableSource : public TemporaryStorageSource {
public:
    std::vector<std::string> getFileList() const { return std::vector<std::string>(1,etlforgeEndpointVariableName(m_CommonData.m_stLocator)); }
    void doPostTransformation(const OperationEngineContext&,ProcessedFiles&) const {}
    void getFirstSourceFile(const OperationEngineContext& context,ProcessedFiles& files) const { materialize(context,files); }
protected:
    std::string getFileFilter() const { return etlforgeEndpointVariableName(m_CommonData.m_stLocator); }
    void doTransferImpl(const OperationEngineContext& context,ProcessedFiles& files,OperationProgress&) const { materialize(context,files); }
private:
    void materialize(const OperationEngineContext& context,ProcessedFiles& files) const {
        const std::string name=etlforgeEndpointVariableName(m_CommonData.m_stLocator);
        if (!m_pDataElementHolder || !m_pDataElementHolder->isOperationDataElementDefined(name))
            throw OperationEngineException("Global variable source $"+name+" is not set. Set it before reading the source.");
        const DataElement* value=m_pDataElementHolder->getOperationDataElement(name);
        if (value->getType()!=DataElement::t_string || value->IsNull())
            throw OperationEngineException("Global variable source $"+name+" must contain text.");
        int error=0; const KString content=value->GetString(error);
        const std::string text(static_cast<const char*>(content),content.GetLength());
        if (error) throw OperationEngineException("Cannot read global variable source $"+name+".");
        etlforgeEndpointText(text);
        const std::string path=context.getLocalTmpDirForOperation()+"global_source_"+std::to_string(getSourceId())+".txt";
        std::ofstream output(etlforge::textPathWide(path).c_str(),std::ios::binary|std::ios::trunc);
        output.write(text.data(),text.size()); output.close();
        if (!output) throw OperationEngineException("Cannot create global variable source working file.");
        ProcessedFile file; file.setFileChangeTimeNow(); file.setInHistory(false);
        file.setLocalSourceLocation(path); file.setRemoteSourceLocation(name+".txt");
        file.setSourceSize(text.size()); file.setTransferedNow(); files.add(file);
    }
};
class GlobalVariableTarget : public TemporaryStorageTarget {
public:
    std::string getFileName() const { return etlforgeEndpointVariableName(m_CommonData.m_stLocator)+".txt"; }
    void testFileTargetConnection(bool) const { etlforgeEndpointVariableName(m_CommonData.m_stLocator); }
    void doTransfer(const OperationDataElementHolder& holder,ProcessedFiles& files) const {
        const std::string name=etlforgeEndpointVariableName(m_CommonData.m_stLocator);
        std::vector<ProcessedFile>& outputs=files.getProcessedFilesByRef();
        ProcessedFile* selected=0;
        for (auto& file:outputs) if (!doNotTransfer(file)) {
            if (selected) throw OperationEngineException("Global variable target requires a single output file; multiple outputs would overwrite the variable.");
            selected=&file;
        }
        if (!selected) return;
        std::ifstream input(etlforge::textPathWide(selected->getLocalTargetLocation()).c_str(),std::ios::binary);
        if (!input) throw OperationEngineException("Cannot open global variable target output.");
        std::string text((std::istreambuf_iterator<char>(input)),std::istreambuf_iterator<char>());
        if (input.bad()) throw OperationEngineException("Cannot read global variable target output.");
        etlforgeEndpointText(text);
        const OperationDataElementHolder& operation=m_pDataElementHolder ? *m_pDataElementHolder : holder;
        DataElement* variable=operation.getOperationDataElement(name);
        variable->setType(DataElement::t_string); variable->setValue(text.c_str());
        selected->setRemoteTargetLocation("$"+name);
    }
};
}
