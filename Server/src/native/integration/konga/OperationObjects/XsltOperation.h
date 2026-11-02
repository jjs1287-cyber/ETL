#ifndef ETLFORGE_XSLT_OPERATION_H
#define ETLFORGE_XSLT_OPERATION_H

#include <fstream>
#include <iterator>
#include <konga/TransformationEngine/EtlForgeXsltRunner.h>
#include <konga/TransformationEngine/EtlForgeXsltAssets.h>
#include <konga/Utils/Settings.h>
#include <konga/SystemVariables/SystemVariables.h>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>
#include "Operation.h"
#include "ProcessedFiles.h"
#include "OperationEngineContext.h"
#include "OperationEngineException.h"

namespace konga {
class XsltOperation : public Operation {
public:
    virtual ActivityType getActivityType() const { return ActivityType_XsltTransform; }
    virtual void doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& elements) {
        try {
        etlforge::XsltAssetStore assets;
        const std::string stylesheet = assets.loadOperationStylesheet(getId());
        const std::string java = GetOptionAsString("XSLT", "JavaPath", "C:/ETL/Tools/jdk8u504-b01/bin/java.exe");
        const std::string classpath = GetOptionAsString("XSLT", "ClassPath", "C:/ETL/Server/dist/lib/xslt/*");
        const std::string worker = GetOptionAsString("XSLT", "NativeWorker", "C:/ETL/Server/runtime/xslt-native/ETLForgeXsltWorker.exe");
        const std::string backend = GetOptionAsString("XSLT", "Backend", "Native");
        if(backend!="Native" && backend!="Java")throw CrException("Unknown XSLT backend: "+backend);
        std::vector<ProcessedFile>& entries = files.getProcessedFilesByRef();
        for (std::vector<ProcessedFile>::iterator item = entries.begin(); item != entries.end(); ++item) {
            if (item->isInHistory()) continue;
            const std::string source = item->getLocalSourceLocation();
            etlforge::Utf8TextInput utf8(source,GetSystemVariable("etlforge.source.text.character_encoding",&elements,""));
            std::ifstream input(etlforge::xsltWide(utf8.path()).c_str(), std::ios::binary);
            if (!input) throw CrException("Cannot open XSLT source file: " + source);
            std::string xml((std::istreambuf_iterator<char>(input)), std::istreambuf_iterator<char>());
            if (input.bad()) throw CrException("Cannot read XSLT source file: " + source);
            input.close();
            etlforge::xmlDeclarationToUtf8(xml);
            const std::string result = etlforge::executeXslt(java, classpath, stylesheet, std::vector<std::string>(1, xml),worker,backend=="Java");
            const std::string target = source + ".xslt.xml";
            std::ofstream output(etlforge::xsltWide(target).c_str(), std::ios::binary | std::ios::trunc);
            if (!output) throw CrException("Cannot open XSLT result file: " + target);
            output.write(result.data(), result.size());
            output.close();
            if (!output) throw CrException("Cannot write XSLT result file: " + target);
            item->setLocalTargetLocation(target);
            item->setSourceSize(xml.size());
            item->setTargetSize(result.size());
            context.setSourceSize(xml.size());
        }
        } catch (const std::exception& failure) { throw OperationEngineException(std::string("XSLT transformation failed: ") + failure.what(), getId()); }
    }
};
}
#endif
