/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <map>

#include <konga/Utils/AutoFileCleanup.h>	                  
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>			                                          

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Server/Server.h>

#include "TreeMapper.h"
#include "DataElementUtils.h"
#include "DiffConstants.h"
#include "Diff.h"
#include "SystemVariableDefs.h"

using namespace konga;
using namespace konga::diff;
using std::string;
using std::vector;
using std::map;

namespace {
	const char READ_MODE[] = "r";
	const char WRITE_MODE[] = "w+";

	void writeLine(const string& line, FILE*out){
		fprintf(out,"%s", line.c_str());
	}

	void writeLine(const char* line, FILE*out){
		fprintf(out,"%s", line);
	}
}

DiffSnapShot::DiffSnapShot(TreeMapper*pTreeMapper_) : pTreeMapper(pTreeMapper_),
strQual(0), delimiter(0), numQual(0), bIsCsv(true), bIsDB(true)
{
	assert(pTreeMapper!=0);
}

void DiffSnapShot::init() {
	const int sourceType=pTreeMapper->GetSourceType();
	assert(sourceType == TextTypeId || sourceType == DbTypeId);
	assert(pTreeMapper->IsFlatSource());

	CROM*pSourceRoot=pTreeMapper->GetSourceDTDParser()->FindRoot();
	assert(pSourceRoot!=0);
	CROM* flatFolder=pSourceRoot->GetFirstChild();
	assert(!flatFolder->IsLeaf() && flatFolder->isLoopNode());

	                                                                                  
	DataElement* PK_LIST=pTreeMapper->getGlobalDataElement(diff::PK_LIST_DENAME.c_str());
	assert(PK_LIST!=0);
	DataElement* ORDER_LIST=pTreeMapper->getGlobalDataElement(diff::ORDER_LIST_DENAME.c_str());
	
	for(CROM*child=flatFolder->GetFirstChild(); child!=0; child=child->GetNextSibling()){
		const KString& fieldName(child->GetDisplayName());
		DataElement*de=new DataElement(fieldName);
		de->setPosBegin(child->GetFieldPosition());
		de->setPosLength(child->GetFieldLength());
		de->setFormatStr(child->GetFormat());
		de->setDefaultString(child->GetDefaultValue());
		de->setType(child->get_DataElementType());
		old_delist.push_back(de);
		new_delist.push_back(new DataElement(*de));
	}

	                       
	for(size_t i=0, n=PK_LIST->getDECollectionSize(), j, m=old_delist.size(); i<n; ++i){
		int err;
		const KString& name(PK_LIST->getDECollectionOf(i)->GetString(err));
		for(j=0; j<m; ++j){
			if(strcmp(name,old_delist.at(j)->getName())==0){
				old_pklist.push_back(old_delist.at(j));
				new_pklist.push_back(new_delist.at(j));
				break;
			}
		}
		                                           
		if(j<m && ORDER_LIST->isCollection() && j<ORDER_LIST->getDECollectionSize()){
			order_list.push_back(ORDER_LIST->getDECollectionOf(j)->GetBool(err));
		}
		else {
			order_list.push_back(true);                     
		}
	}

	delimiter = flatFolder->get_delimiter();
	strQual = flatFolder->get_strQual();
	numQual = flatFolder->get_numQual();
	bIsDB = (sourceType == DbTypeId);
	bIsCsv = (strQual=='"' && delimiter!=0)||bIsDB;
}

DiffSnapShot::~DiffSnapShot(){
	for(vector<DataElement*>::const_iterator cit=old_delist.begin();
		cit!=old_delist.end(); ++cit)
	{
		delete *cit;
	}
	for(vector<DataElement*>::const_iterator cit=new_delist.begin();
		cit!=new_delist.end(); ++cit)
	{
		delete *cit;
	}
}

namespace {
	                                      
	void resetGlobalDiffDE(konga::OperationDataElementHolder* pDeHolder) {
		pDeHolder->removeOperationDataElement(diff::DIFF_GUID);
		pDeHolder->removeOperationDataElement(diff::ORDERED_DENAME);
		pDeHolder->removeOperationDataElement(diff::STATUS_DENAME);
	}
}

void DiffSnapShot::Initialize(const string& stDiffId) {
	konga::OperationDataElementHolder* pDeHolder = pTreeMapper->getOperationDataElementHolder();
	resetGlobalDiffDE(pDeHolder);

	                         
	DataElement* de=pDeHolder->getOperationDataElement(diff::DIFF_GUID);
	de->setType(DataElement::t_string);
	de->setValue(stDiffId.c_str());

	if ( hasProcessed() ) {
		de->setValue("");
		throw CrException("There is already a new snapshot on disk. This means that there is either a diff processes already in progress " \
			"or a previous diff process unexpectedly terminated. If you want to run a new diff process then please remove the file '"
			+ getNewSnapshotFileName() + "'.");
	}
}

void DiffSnapShot::Reset(const string& stDiffId, DiffResetAction action) {
	OperationDataElementHolder* pDeHolder = pTreeMapper->getOperationDataElementHolder();
	DataElement* de=pDeHolder->getOperationDataElement(diff::DIFF_GUID.c_str());
	de->setType(DataElement::t_string);
	de->setValue(stDiffId.c_str());

	switch(action) {
		case DiffResetAction_Reset:
			kDeleteFile(getOldSnapshotFileName());
			kDeleteFile(getNewSnapshotFileName());
			kDeleteFile(getAddFileName());
			kDeleteFile(getDeleteFileName());
			kDeleteFile(getUpdateFileName());
			break;
		case DiffResetAction_Purge:
			kDeleteFile(getNewSnapshotFileName());
			kDeleteFile(getAddFileName());
			kDeleteFile(getDeleteFileName());
			kDeleteFile(getUpdateFileName());
			break;
		default:
			throw InternalErrorException("DiffResetAction " + toString(action) + " is not handled.");
	};
	resetGlobalDiffDE(pDeHolder);
}

bool DiffSnapShot::ProcessSnapShot(string& newFileName)
{
	if(hasProcessed()) {
		                        
	}
	else if ( !hasOldSnapshot() ) {
		                                                                                    
		assert(!newFileName.empty());
		kCopyFile(newFileName, getAddFileName(), true);
		truncate_file(getUpdateFileName());
		truncate_file(getDeleteFileName());
		kMoveFile(newFileName, getNewSnapshotFileName(), true);
	}
	else {
		                                      
		assert(!newFileName.empty());
		kMoveFile(newFileName, getNewSnapshotFileName(), true);
		newFileName.clear();
		DataElement* ORDERED=pTreeMapper->getGlobalDataElement(diff::ORDERED_DENAME.c_str());
		int err=0;
		if(ORDERED && ORDERED->GetBool(err)){
			ProcessSnapShot_ordered();
		}
		else {
			ProcessSnapShot_chunked();
		}
	}

	newFileName = getCurrentFile();
	return true;
}

namespace {
	const int DEFAULT_CHUNK_SIZE = 10000;

	FILE* fopen_handle_error(const string& fileName, const char* mode) {
		FILE* f = fopen(fileName.c_str(), mode);
		if ( f == 0 ) {
			const long lErr = GetLastError();
			throw CrException("Failed to open the file '" + fileName + "'. Reason: " +
				ErrorResolver(lErr).Resolve(lErr));
		}
		return f;
	}
}

bool DiffSnapShot::ProcessSnapShot_chunked() {
	init();

	FILE *oldSnapShot = oldSnapShot=fopen_handle_error(getOldSnapshotFileName(), READ_MODE);
	AutoFileHandle adopt_old(oldSnapShot);

	FILE* del=fopen_handle_error(getDeleteFileName(), WRITE_MODE);
	AutoFileHandle adopt_del(del);

	FILE* update=fopen_handle_error(getUpdateFileName(), WRITE_MODE);
	AutoFileHandle adopt_update(update);

	const int buf_max = RECORD_SIZE;
	int buf_len = buf_max;
	AutoArrayDelete<char> buf( new char[buf_len+1] );

	int nChunkSize=DEFAULT_CHUNK_SIZE;
	DataElement* chunk_size=pTreeMapper->getGlobalDataElement(diff::CHUNK_SIZE_DENAME.c_str());
	if(!chunk_size->IsNull()) {
		int err=0;
		const int itmp=chunk_size->GetInteger(err);
		if(!err && itmp > 0 )nChunkSize=itmp;
	}

	bool eof=false, bSwitched=false;
	int iChunk=0;
	const string tmpFileName(getTempFileName());
	const string newSnapshotFileName(getNewSnapshotFileName());
	const string addFileName(getAddFileName());
	FILE *newSnapShot = 0, *add = 0;
	for(; !eof; ++iChunk){
		bSwitched=(iChunk & 1);
		string line;
		map<string,string> old_lines_map;

		for(int j=0; j<nChunkSize; j++){
			if((eof=parseRecord(buf, buf_len, oldSnapShot, old_delist, line)))break;
			old_lines_map[getPKString(old_pklist)]=line;
		}

		{                                     
			if(iChunk==0)newSnapShot=fopen_handle_error(newSnapshotFileName, READ_MODE);
			else newSnapShot=fopen_handle_error(bSwitched?addFileName:tmpFileName, READ_MODE);
			AutoFileHandle adopt_new(newSnapShot);

			add=fopen_handle_error(bSwitched?tmpFileName:addFileName, WRITE_MODE);
			AutoFileHandle adopt_add(add);

			while(!parseRecord(buf, buf_len, newSnapShot, new_delist, line)){
				map<string, string>::iterator it=old_lines_map.find(getPKString(new_pklist));
				if(it!=old_lines_map.end()){
					if(line!=it->second)writeLine(line, update);
					old_lines_map.erase(it);
				}
				else {
					writeLine(line, add);
				}
			}
		}

		                                                            
		for(map<string,string>::const_iterator it=old_lines_map.begin(); it!=old_lines_map.end(); ++it){
			writeLine(it->second, del);
		}
	}

	                                                                                              
	if(bSwitched)kMoveFile(tmpFileName, getAddFileName(), true);
	else if(iChunk)kDeleteFile(tmpFileName);
	return true;
}

bool DiffSnapShot::ProcessSnapShot_ordered()
{
	init();

	FILE *oldSnapShot=fopen_handle_error(getOldSnapshotFileName(), READ_MODE);
	AutoFileHandle adopt_old(oldSnapShot);

	FILE *newSnapShot=fopen_handle_error(getNewSnapshotFileName(), READ_MODE);
	AutoFileHandle adopt_new(newSnapShot);

	FILE* add=fopen_handle_error(getAddFileName(), WRITE_MODE);
	AutoFileHandle adopt_add(add);

	FILE* update=fopen_handle_error(getUpdateFileName(), WRITE_MODE);
	AutoFileHandle adopt_update(update);

	FILE* del=fopen_handle_error(getDeleteFileName(), WRITE_MODE);
	AutoFileHandle adopt_del(del);

	const int buf_max = RECORD_SIZE;
	int old_line_len = buf_max, new_line_len = buf_max;
	AutoArrayDelete<char> old_line_buf( new char[old_line_len+1] );
	AutoArrayDelete<char> new_line_buf( new char[new_line_len+1] );

	string new_line, old_line;
	bool old_line_eof=parseRecord(old_line_buf, old_line_len, oldSnapShot, old_delist, old_line);
	int cmp=1;
	while((!parseRecord(new_line_buf, new_line_len, newSnapShot, new_delist, new_line))){
		while(!old_line_eof && (cmp=compareKeys())<0){
			writeLine(old_line, del);
			old_line_eof=parseRecord(old_line_buf, old_line_len, oldSnapShot, old_delist, old_line);
		}

		if(old_line_eof)cmp=1;
		if(cmp==0){
			if(old_line!=new_line){
				writeLine(new_line, update);
			}
			old_line_eof=parseRecord(old_line_buf, old_line_len, oldSnapShot, old_delist, old_line);
		}
		else if(cmp>0){
			writeLine(new_line,add);
		}
	}
	while(!old_line_eof){
		writeLine(old_line, del);
		old_line_eof=parseRecord(old_line_buf, old_line_len, oldSnapShot, old_delist, old_line);
	}
	return true;
}

void DiffSnapShot::DiffComplete() {
	kMoveFile(getNewSnapshotFileName(), getOldSnapshotFileName(), true);
	kDeleteFile(getAddFileName());
	kDeleteFile(getUpdateFileName());
	kDeleteFile(getDeleteFileName());

	konga::OperationDataElementHolder* pDeHolder = pTreeMapper->getOperationDataElementHolder();
	resetGlobalDiffDE(pDeHolder);
}

diff::DiffStatus DiffSnapShot::getDiffStatus() const {
	DataElement*de=pTreeMapper->getGlobalDataElement(diff::STATUS_DENAME.c_str());
	if(de->IsNull()) return diff::DiffStatus_None;

	int err = 0;
	diff::DiffStatus diff_status=static_cast<diff::DiffStatus>(de->GetInteger(err));
	if(err)diff_status=diff::DiffStatus_None;
	return diff_status;
}

bool DiffSnapShot::hasProcessed() const {
	return FileExists(getNewSnapshotFileName());
}

bool DiffSnapShot::hasOldSnapshot() const {
	return FileExists(getOldSnapshotFileName());
}

string DiffSnapShot::getCurrentFile() const {
	switch(getDiffStatus()) {
		case DiffStatus_Add:
			return getAddFileName();
		case DiffStatus_Update:
			return getUpdateFileName();
		case DiffStatus_Delete:
			return getDeleteFileName();
		default:
			throw CrException("Internal Error: Unknown diff status.");
	}
}

string DiffSnapShot::getAddFileName() const {
	if ( m_addFileName.empty() ) {
		m_addFileName = getBaseDirectoryWithSlash() + "add.csv";
	}
	return m_addFileName;
}

string DiffSnapShot::getDeleteFileName() const {
	if ( m_deleteFileName.empty() ) {
		m_deleteFileName = getBaseDirectoryWithSlash() + "delete.csv";
	}
	return m_deleteFileName;
}

string DiffSnapShot::getUpdateFileName() const {
	if ( m_updateFileName.empty() ) {
		m_updateFileName = getBaseDirectoryWithSlash() + "update.csv";
	}
	return m_updateFileName;
}

string DiffSnapShot::getOldSnapshotFileName() const {
	if ( m_oldFileName.empty() ) {
		m_oldFileName = getBaseDirectoryWithSlash() + "OldSnapshot.csv";
	}
	return m_oldFileName;
}

string DiffSnapShot::getNewSnapshotFileName() const {
	if ( m_newFileName.empty() ) {
		m_newFileName = getBaseDirectoryWithSlash() + "NewSnapshot.csv";
	}
	return m_newFileName;
}

string DiffSnapShot::getTempFileName() const {
	if ( m_tmpFileName.empty() ) {
		m_tmpFileName = getBaseDirectoryWithSlash() + "tmp.csv";
	}
	return m_tmpFileName;
}

                                                   
                                              
                               
string DiffSnapShot::getBaseDirectoryWithSlash() const {
	if ( base_dir.empty() ) {
		string dir(GetDataDirectoryWithSlash());
		dir += "Diff";
		AppendPathSeparator(dir);
		dir += md5(getDiffGuid());
		kCreateDirectory(dir);
		AppendPathSeparator(dir);
		base_dir = dir;
	}
	return base_dir;
}

string DiffSnapShot::getDiffGuid() const {
	if ( diff_guid.empty() ) {
		DataElement* de = pTreeMapper->getGlobalDataElement(diff::DIFF_GUID.c_str());
		int err = 0;
		diff_guid = (const char*)de->GetString(err);
		if (err) diff_guid.clear();
	}
	if ( diff_guid.empty() )
		throw CrException("Empty diff ID was specified. A valid diff ID has to be provided.");

	return diff_guid;
}

                                
bool DiffSnapShot::parseRecord(AutoArrayDelete<char>& buf, int& buf_len, FILE*in, const vector<DataElement*>& delist, string& line){
	if(FGETS(buf, buf_len, in)==0)return true;               
	line.assign(buf.Get());
	int len=get_length(buf.Get());

	bool bRTrimCharData = pTreeMapper->isTrimSourceWs();

	const size_t nSize = delist.size();
	int pos=0;
	for(size_t iDE=0; iDE<nSize; ++iDE){
		DataElement*de=delist.at(iDE);
		bool bTrim=bRTrimCharData && de->getType()==DataElement::t_string;
		DataElementRead(*de, in, buf.Get(), buf_len, len, pos, strQual, numQual, delimiter, bIsDB, bTrim, &line);
	}
	return false;
}

                                                                        
string DiffSnapShot::getPKString(const vector<DataElement*>& delist) const
{
	string str;
	int err=0;
	for(vector<DataElement*>::const_iterator cit = delist.begin(); cit != delist.end(); ++cit) {
		const DataElement* de = *cit;
		string tmp((const char*)de->GetString(err));
		if(tmp.find("\t")!=string::npos){
			string out;
			ReplaceCharWithString(tmp, out, '\t', "\t?");
			tmp=out;
		}
		str+=(tmp+"\t");
	}
	return str;
}

int DiffSnapShot::compareKeys(){
	assert(old_pklist.size()==new_pklist.size());
	for(int i=0, n=old_pklist.size(); i<n; ++i){
		const DataElement *old_pk=old_pklist.at(i), *new_pk=new_pklist.at(i);
		if(*old_pk==*new_pk)continue;
		return ((*old_pk < *new_pk)==order_list.at(i))? -1:1;
	}
	return 0;
}
