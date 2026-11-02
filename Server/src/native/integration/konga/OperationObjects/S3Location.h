#pragma once
#include "FileTypeSourceLocation.h"
#include "FileTypeTargetLocation.h"
#include "DataLocation.h"
#include "OperationEngineContext.h"
#include "ProcessedFiles.h"
#include "RenameFile.h"
#include "OperationEngineException.h"
#include "../../../s3/S3Client.h"
#include <konga/Utils/SimpleFileFilterFileMatcher.h>
#include <konga/OperationLog/OperationProgress.h>
#include <set>
#include <filesystem>
#include <map>
#include <ctime>
#include <cstdio>

namespace konga {
class S3Location : public DataLocation {
protected:
    etlforge::s3::Configuration configuration() const {
        etlforge::s3::Configuration c;c.bucket=replace_de(m_CommonData.m_stLocator);c.region=replace_de(m_extendedProperties.GetString("s3_region","us-east-1"));
        c.accessKey=etlforge::s3::decryptProjectSecret(m_extendedProperties.GetString("s3_access_key_protected",""));
        c.secretKey=etlforge::s3::decryptProjectSecret(m_extendedProperties.GetString("s3_secret_key_protected",""));
        if(m_extendedProperties.GetString("s3_authentication","access_key")=="temporary"){c.sessionToken=etlforge::s3::decryptProjectSecret(m_extendedProperties.GetString("s3_session_token_protected",""));if(c.sessionToken.empty())throw OperationEngineException("Temporary S3 credentials require a session token");}return c;
    }
    std::string prefix() const {return replace_de(m_CommonData.m_stPath);}
    std::string bucket() const {return replace_de(m_CommonData.m_stLocator);}
    std::string uri(const std::string& key) const {return "s3://"+bucket()+"/"+key;}
    std::string keyFromUri(const std::string& value) const {
        std::string base="s3://"+bucket()+"/";
        if(value.compare(0,base.size(),base)!=0)throw OperationEngineException("S3 source location does not belong to the configured bucket");return value.substr(base.size());
    }
};
class S3Source : public FileTypeSourceLocation, public S3Location {
    mutable std::map<std::string,std::string> originalEtags;
    std::vector<etlforge::s3::Object> matching(etlforge::s3::Client& client) const {
        SimpleFileFilterFileMatcher matcher(getFileFilter(),!useCaseInsensitiveFileFilter(),useRegExFileFilter());
        std::vector<etlforge::s3::Object> result;
        auto objects=client.list(prefix(),m_extendedProperties.GetBool("s3_recursive",true));
        for(auto& o:objects) {auto relative=o.key.substr(prefix().size());auto slash=relative.find_last_of('/');auto leaf=relative.substr(slash==std::string::npos?0:slash+1);if(matcher.isMatch(leaf))result.push_back(o);}
        return result;
    }
    void transfer(const OperationEngineContext& context,ProcessedFiles& files,bool first,OperationProgress* progress) const {
        try {
            etlforge::s3::Client client(configuration());auto objects=matching(client);std::size_t index=0;int limit=getFileListLimit(m_pDataElementHolder);
            for(auto& o:objects) {
                if(limit>0&&index>=static_cast<std::size_t>(limit))break;
                ProcessedFile file;file.setRemoteSourceLocation(uri(o.key));file.setSourceSize(o.size);
                int year=0,month=0,day=0,hour=0,minute=0,second=0;
                if(std::sscanf(o.modified.c_str(),"%d-%d-%dT%d:%d:%d",&year,&month,&day,&hour,&minute,&second)!=6)throw OperationEngineException("Invalid S3 LastModified timestamp");
                file.month(month);file.day(day);file.time_or_year(hour*10000+minute*100+second);
                if(!first&&context.isAlreadyTransferred(file)){files.add(file);continue;}
                ++index;std::string local=context.getLocalTmpDirForOperation()+"s3_"+std::to_string(index)+".data";
                if(progress)progress->UpdateProgress("Amazon S3","Downloading "+uri(o.key),static_cast<int>(100*(index-1)/std::max<std::size_t>(objects.size(),1)),-1);
                try{client.download(o.key,local,o.etag);}catch(const std::exception& error){throw OperationEngineException("Cannot download "+uri(o.key)+": "+error.what());}originalEtags[o.key]=o.etag;file.setLocalSourceLocation(local);file.setTransferedNow();files.add(file);if(first)return;
            }
            if(first)throw OperationEngineException("No S3 objects match the prefix and file pattern");
        }catch(const std::exception& e){throw OperationEngineException(e.what());}
    }
public:
    std::vector<std::string> getFileList() const {
        try {etlforge::s3::Client client(configuration());std::vector<std::string> out;for(auto& o:matching(client))out.push_back(o.key);return out;}catch(const std::exception& e){throw OperationEngineException(e.what());}
    }
    void getFirstSourceFile(const OperationEngineContext& context,ProcessedFiles& files) const {transfer(context,files,true,nullptr);}
    void doPostTransformation(const OperationEngineContext&,ProcessedFiles& files) const {
        if(!deleteAfterProcessing())return;
        try {etlforge::s3::Client client(configuration());files.clearSourceProcessedFlags();
            for(auto& file:files.getProcessedFilesByRef()) {
                if(file.hasError()||file.isInHistory()||files.hasSourceFileBeenProcessed(file))continue;
                try {auto key=keyFromUri(file.getRemoteSourceLocation());auto found=originalEtags.find(key);if(found==originalEtags.end())throw OperationEngineException("S3 source version is unknown; source object was kept");client.remove(key,found->second);}catch(const std::exception& e){file.addWarning(e.what());}files.flagSourceFileAsProcessed(file);
            }
        }catch(const std::exception& e){throw OperationEngineException(e.what());}
    }
protected:
    std::string getFileFilter() const {auto value=getOverrideFileFilter();if(value.empty())value=replace_de(m_CommonData.m_stFileName);return value.empty()?"*":value;}
    void doTransferImpl(const OperationEngineContext& context,ProcessedFiles& files,OperationProgress& progress) const {transfer(context,files,false,&progress);}
};
class S3Target : public FileTypeTargetLocation, public S3Location {
public:
    std::string getFileName() const {auto name=getOverrideFileName();return name.empty()?replace_de(m_CommonData.m_stFileName):name;}
    void testFileTargetConnection(bool) const {try {etlforge::s3::Client client(configuration());client.list(prefix(),false,1);}catch(const std::exception& e){throw OperationEngineException(e.what());}}
    void doTransfer(const OperationDataElementHolder&,ProcessedFiles& files) const {
        try {etlforge::s3::Client client(configuration());RenameFile renamer(getFileName());std::set<std::string> keys;std::vector<std::pair<ProcessedFile*,std::string>> pending;
            for(auto& file:files.getProcessedFilesByRef())if(!doNotTransfer(file)) {
                std::string key=prefix()+getRenamedRemoteName(renamer,file);if(!keys.insert(key).second)throw OperationEngineException("Several output files resolve to the same S3 object key. Use a filename substitution or combined archive.");pending.push_back(std::make_pair(&file,key));
            }
            for(auto& output:pending){client.upload(output.second,output.first->getLocalTargetLocation(),m_extendedProperties.GetBool("s3_overwrite",true));output.first->setRemoteTargetLocation(uri(output.second));}
        }catch(const std::exception& e){throw OperationEngineException(e.what());}
    }
};
}
