/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stdio.h>
#include <string.h>		         

#include <stdexcept>
#include <cassert>

#include <konga/Utils/RegExUtils.h>
#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/BinaryData.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/HashTable.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>				                  
#include <konga/Utils/ProcessUtils.h>		             

#include <konga/JbParser/JbNode.h>

#include <konga/SysInfo/SysInfo.h>			                   

#include <konga/Crypto/Crypto.h>

#include <konga/Debugger/Debuggee.h>

#include <konga/Messaging/MessageSender.h>	                          
#include <konga/Messaging/ProcessMessage.h>
#include <konga/Messaging/CheckOperationQueue.h>

#include <konga/Networking/IPUtils.h>

#include <konga/Db/DbUtils/DbLookup.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbRowSetData.h>	                                  

#include <konga/Db/TranDbUtils/TranDbUtils.h>	              

#include <konga/DbRelations/EntityIdConstants.h>                     

#include <konga/Ldap/LdapTreeNode.h>
#include <konga/Ldap/konga_ldap.h>

#include <konga/DataElement/DataElement.h>
#include <konga/DataElement/FindDataElement.h>
#include <konga/DataElement/Token.h>
#include <konga/DataElement/TransException.h>

#include <konga/OperationContext/OperationContext.h>
#include <konga/OperationDataElements/OperationDataElementsManager.h>

#include <konga/OperationObjects/LocationCreatorDb.h>	                      
#include <konga/OperationObjects/LdapTargetLocation.h>	                     
#include <konga/OperationObjects/LdapSourceLocation.h>	                     

#include <konga/Server/WDIPathFinder.h>

#include <konga/SystemVariables/SystemVariables.h>	                       

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ViewLog.h>

#include <konga/TransformationUtils/TranDBUtils.h>				                  
#include <konga/TransformationUtils/ReadFileImpl.h>
#include <konga/TransformationUtils/SendTransformationEmail.h>
#include <konga/TransformationUtils/TransformationDataHolder.h>
#include <konga/TransformationUtils/CachedDbLookup.h>
#include <konga/TransformationUtils/DbLookupCache.h>
#include <konga/TransformationUtils/LdapExecutor.h>
#include <konga/TransformationUtils/FileTypeTargetCache.h>
#include <konga/TransformationUtils/DateFormatUtils.h>
#include <konga/TransformationUtils/RunOperationImpl.h>			                     
#include <konga/TransformationUtils/TransformFcnUtils.h>
#include <konga/TransformationUtils/IntegrationDebugUtils.h>

#include "ErrorCodes/resource.h"
#include "Diff.h"
#include "CRXmlData.h"
#include "RaiseErrorException.h"
#include "Transform.h"
#include "TreeMapper.h"
#include "TxtParser.h"
#include "DataElementUtils.h"
#include "TransformXMLFunctions.h"
#include "RunScript.h"
#include "EtlForgeXsltAssets.h"
#include "EtlForgeXsltRunner.h"
#include <konga/Utils/Settings.h>
#include <konga/TransformationUtils/ScriptCache.h>
#include "RunPlugin.h"
#include "Transform.h"

#include <konga/CharacterEncodingConversion/EncodingStringUtils.h>



using namespace konga;
using namespace std;

namespace {

	                                                                                 
	class WarningException {
	public:
		WarningException(const std::string& warning) : m_warning(warning) {}
		~WarningException(void) {}

		std::string GetWarning() const {
			return m_warning;
		}

	private:
		std::string m_warning;
	};

	class LTCompare : public konga::DataElementComparer {
	public:
		LTCompare(void) {}
		virtual ~LTCompare(void) {}
		bool Compare(const DataElement& lhs, const DataElement& rhs) const {
			return lhs < rhs;
		}
	};

	class GTCompare : public konga::DataElementComparer {
	public:
		GTCompare(void) {}
		virtual ~GTCompare(void) {}
		bool Compare(const DataElement& lhs, const DataElement& rhs) const {
			return lhs > rhs;
		}
	};

	DataElement* FindDataElementImpl(const DataElement* const collection,
		const konga::DataElementComparer& comparer)
	{
		if ( collection == 0 ){
			                                                        
			return new DataElement();
		}
		DataElement* de = konga::FindDataElement(*collection, comparer);
		if ( de == 0 ){
			                                                                                         
			return new DataElement();
		}
		return de;
	}

	DataElement* Max(DataElement* collection){
		return FindDataElementImpl(collection, GTCompare());
	}

	DataElement* Min(DataElement* collection){
		return FindDataElementImpl(collection, LTCompare());
	}

	DataElement* Exist(const KString&s,DataElement*r,int&err, KString& errmsg){
		if(r->isCollection()){
			DataElement::Type type=r->getDataElementTypeForArray(err,errmsg);
			if(err)throw konga::ExeFcnException((const char*)errmsg);
			int n=r->getDECollectionSize();
			DataElement*ds;
			if(type==DataElement::t_collection){
				std::auto_ptr<DataElement> de( new DataElement() );
				de->initCollection(r->IsNull());
				for(int i=0; i<n; i++){
					ds=r->getDECollectionOf(i);
					de->addToCollection(Exist(s, ds, err, errmsg));
					if(err){
						throw konga::ExeFcnException((const char*)errmsg);
					}
				}
				return de.release();
			}
			for(int j=0,n=(int)r->getDECollectionSize(); j<n; j++){
				ds=r->getDECollectionOf(j);
				KString si=ds->GetString(err);
				if(err==0&&si==s)return new DataElement(DataElement::t_bool,true);
			}
			return new DataElement(DataElement::t_bool,false);
		}
		err=TRANSFORM_NOT_INSTANCE;
		return new DataElement();
	}

	DataElement* sum(DataElement*r,int&err, KString& errmsg){
		if(r->isCollection()){
			const DataElement::Type type=r->getDataElementTypeForArray(err, errmsg);
			if(err!=0)throw konga::ExeFcnException((const char*)errmsg); 
			const size_t n=r->getDECollectionSize();
			switch(type){
			case DataElement::t_int:
			case DataElement::t_long:
				                               
				{
					long long sum=0;
					int ignoreError=0;
					for(size_t j=0; j<n; j++){
						DataElement*ds=r->getDECollectionOf(j);
						sum+=ds->GetLong(ignoreError);
					}
					return new DataElement(type, sum);
				}
				break;
			case DataElement::t_float:
			case DataElement::t_double:
				                               
				{
					double sum=0;
					int ignoreError=0;
					for(size_t j=0; j<n; j++){
						DataElement*ds=r->getDECollectionOf(j);
						sum+=ds->GetDouble(ignoreError);
					}
					return new DataElement(type,sum);
				}
				break;
			case DataElement::t_string:
				{
					KString sum;
					int ignoreError=0;
					for(size_t i=0;i<n;i++){
						DataElement*ds=r->getDECollectionOf(i);
						sum+=ds->GetString(ignoreError);
					}
					return new DataElement(type,sum);
				}
				break;
			case DataElement::t_collection:
				{
					std::auto_ptr<DataElement> de( new DataElement() );
					de->initCollection(r->IsNull());
					for(size_t i=0; i<n; i++){
						DataElement*ds=r->getDECollectionOf(i);
						de->addToCollection(sum(ds, err, errmsg));
					}
					return de.release();
				}
				break;
			default:
				err=TRANSFORM_SUM_ERR;
				return new DataElement();
			}

		}
		else if(r->IsNull()){
			return new DataElement(*r);
		}
		err=TRANSFORM_NOT_INSTANCE;
		return new DataElement();
	}

	DataElement*SumString(DataElement*r,char delimiter, bool bOmitLast, int&err, KString&err_msg){
		if(r->isCollection()){
			const DataElement::Type type=r->getDataElementTypeForArray(err, err_msg);
			if(err!=0)throw konga::ExeFcnException((const char*)err_msg);
			if(type==DataElement::t_collection){
				std::auto_ptr<DataElement> de(new DataElement());
				de->initCollection(r->IsNull());
				for(size_t j=0, n=r->getDECollectionSize(); j<n; j++){
					de->addToCollection(SumString(r->getDECollectionOf(j),delimiter,bOmitLast,err,err_msg));
				}
				return de.release();
			}
			KString sum;
			int ignoreError=0;
			for(size_t j=0, n=r->getDECollectionSize(); j<n; j++){
				sum+=r->getDECollectionOf(j)->GetString(ignoreError);
				if(j<(n-1) || !bOmitLast)sum+=delimiter;
			}
			return new DataElement(DataElement::t_string,sum);
		}
		else if(r->IsNull()){
			return new DataElement(DataElement::t_string,"");
		}
		else {
			return new DataElement(DataElement::t_string, r->GetString(err));
		}
	}

	DataElement*SumCsv(DataElement*r,int&err, KString&err_msg){
		if(r->isCollection()){
			const DataElement::Type type=r->getDataElementTypeForArray(err, err_msg);
			if(err!=0)throw konga::ExeFcnException((const char*)err_msg);
			if(type==DataElement::t_collection){
				std::auto_ptr<DataElement> de(new DataElement());
				de->initCollection(r->IsNull());
				for(size_t j=0, n=r->getDECollectionSize(); j<n; j++){
					de->addToCollection(SumCsv(r->getDECollectionOf(j),err,err_msg));
				}
				return de.release();
			}

			KString sum;
			int ignoreError=0;
			for(size_t j=0, n=r->getDECollectionSize(); j<n; j++){
				if(j>0)sum+=",";
				KString s_i(r->getDECollectionOf(j)->GetString(ignoreError));
				konga::qualifyCsvStringK(s_i,',');
				sum+=s_i;
			}
			return new DataElement(DataElement::t_string,sum);
		}
		if(r->IsNull()){
			return new DataElement(DataElement::t_string,"");
		}
		err=TRANSFORM_NOT_INSTANCE;
		return new DataElement();
	}

	DataElement*Count(DataElement*r, int&err, KString&err_msg){
		if(r->isCollection()){
			DataElement::Type type=r->getDataElementTypeForArray(err, err_msg);
			if(err!=0)throw konga::ExeFcnException((const char*)err_msg);
			if(type==DataElement::t_collection){
				std::auto_ptr<DataElement> de(new DataElement());
				de->initCollection(r->IsNull());
				for(size_t j=0, n=r->getDECollectionSize(); j<n; j++){
					de->addToCollection(Count(r->getDECollectionOf(j),err,err_msg));
				}
				return de.release();
			}
			int err1;
			int k=0;
			for(size_t j=0, n=r->getDECollectionSize(); j<n; j++){
				DataElement*ds=r->getDECollectionOf(j);
				DataElement::Type type=ds->getType();
				if(type==DataElement::t_unknown||ds->getError()!=0)continue;
				if(type==DataElement::t_string&&ds->GetString(err1)=="")continue;
				k++;
			}
			return new DataElement(DataElement::t_int,k);
		}
		err=TRANSFORM_NOT_INSTANCE;
		return new DataElement();
	}

	DataElement*FindByPosition(int pos, DataElement* r,int&err, KString&err_msg){
		if(r->isCollection()){
			const DataElement::Type collectionOfType=r->getDataElementTypeForArray(err, err_msg);
			if(err!=0)throw konga::ExeFcnException((const char*)err_msg);
			if(collectionOfType==DataElement::t_collection){
				std::auto_ptr<DataElement> de(new DataElement());
				de->initCollection(r->IsNull());
				for(int j=0, n=(int)r->getDECollectionSize(); j<n; j++){
					de->addToCollection(FindByPosition(pos, r->getDECollectionOf(j), err, err_msg));
				}
				return de.release();
			}
			if(r->IsNull())return new DataElement();	                             

			const int size=(int)r->getDECollectionSize();
			                                               
			                                           
			                                                    
			if(pos<0)pos=size+pos+1;
			if( pos>0 && pos<=size ){
				return new DataElement(*r->getDECollectionOf(pos-1));
			}
			else {
				                                   
				                                                                    
				const string& sSize = konga::toString(size);
				string error("Index " + konga::toString(pos) + " out of range. Size of collection is " + sSize + ". ");
				if ( size == 0 )
					error += "There are no valid indexes for an empty collection.";
				else if ( size == 1 )
					error += "Valid indexes are -1 and 1.";
				else {
					error += "Valid indexes are -" + sSize + " to -1 and "
						+ "1 to " + sSize + ".";
				}
				throw CrException(error);
			}
		}
		else if(r->IsNull()){
			return new DataElement();
		}
		err=TRANSFORM_NOT_INSTANCE;
		return new DataElement();                                                                            
	}

	DataElement*CResolve(const KString&s1, DataElement*r2, DataElement*r3, int&err, KString&err_msg){
		if(r2->IsNull() || r3->IsNull())return new DataElement();
		if(r2->isCollection() && r3->isCollection()){
			int n2=(int)r2->getDECollectionSize(), n3=(int)r3->getDECollectionSize();
			DataElement::Type type2=r2->getDataElementTypeForArray(err,err_msg), type3;
			if(err==0)type3=r3->getDataElementTypeForArray(err,err_msg);
			if(err!=0)throw konga::ExeFcnException((const char*)err_msg);
			KString result,check;
			if(n2!=n3){
				err=INCONSISTANCE_INSTANCE_IN_CRESOLVE;
				return new DataElement();
			}
			if(type2==DataElement::t_collection){
				std::auto_ptr<DataElement> de(new DataElement());
				de->initCollection(r2->IsNull());
				for(int i=0; i<n2; i++){
					de->addToCollection(CResolve(s1,r2->getDECollectionOf(i), r3->getDECollectionOf(i), err, err_msg));
					if(err!=0)throw konga::ExeFcnException((const char*)err_msg);
				}
				return de.release();
			}
			for(int i=0;i<n2;i++){
				DataElement*ds2=r2->getDECollectionOf(i);
				check=ds2->GetString(err);
				if(err==0 && check==s1){
					DataElement*ds3=r3->getDECollectionOf(i);
					if(!r3->isTokenDE())return new DataElement(*ds3);
					ds3->copyToken(r3);
					return ds3;
				}
			}
			return new DataElement();
		}
		err_msg=std::string("Error: argument 2 or 3 is not an array type data element.\narg2: ");
		err=-1;
		return new DataElement();
	}

	int Days(int n){
		return n*24*60*60;
	}
	int Hours(int n){
		return n*60*60;
	}
	int Minutes(int n){
		return n*60;
	}
	int Seconds(int n){
		return n;
	}

	std::string SourceFileName(){
		return "Not implemented yet";
	}
	bool isValidString(const char* s){
		assert(s!=0);
		for(unsigned char c=*s++; c; c=*s++){
			if(c=='\r' || c=='\n' || c=='\t')continue;
			if(c<32 || c>126)return false;
		}
		return true;
	}

	void SetLastErrorImpl(const string& stNewError, TransformationDataHolder* tdHolder) {
		                                                                    
		CViewLog* pViewLog = tdHolder->getViewLog();
		if ( !stNewError.empty() )
			pViewLog->LogMessage("%s", stNewError.c_str());
		else
			pViewLog->ResetLastLogMsg(stNewError);
		                                               
	}

#ifdef iiWIN32
	LONG WINAPI jbUnhandledExceptionFilter(PEXCEPTION_POINTERS pExceptionInfo) {
		PEXCEPTION_RECORD pExceptionRecord = pExceptionInfo->ExceptionRecord;
		char szModule[MAX_PATH];
		if( GetModuleFileName( (HMODULE)pExceptionRecord->ExceptionAddress, szModule, sizeof szModule) ) {
			fprintf(stderr, szModule);
		}
		return EXCEPTION_CONTINUE_SEARCH;
	}
#endif

	std::string binary2uuid(konga::BinaryData*pbd){
		assert(pbd!=0);
		size_t len=pbd->Size();
		if(len!=16)throw CrException("Input is not 16-byte binary data. The size of your input is " + konga::toString(len) + ".");
		                                       
		                                       
		char uuid[37];
		const unsigned char* b=(const unsigned char*)pbd->GetPointerToData();
		for(int i=0, n=16, j=0; i<n; ++i){
			sprintf(&uuid[j],"%02x", b[i]);
			j+=2;
			if(j==8||j==13||j==18||j==23)uuid[j++]='-';
		}
		uuid[36]=0;
		return string(uuid);
	}

	konga::BinaryData* uuid2binary(const char* uuid){
		assert(uuid!=0);
		if(strlen(uuid)!=36)
			throw CrException("Invalid UUID string '" + string(uuid) + "'. A UUID string has to be 36 characters long.");
		if(uuid[8]!='-'||uuid[13]!='-'||uuid[18]!='-'||uuid[23]!='-')
			throw CrException("Invalid UUID string '" + string(uuid) + "'. A UUID has to be of the format 2f46dad9-e5c2-457e-b1fd-ad1b49b99aff.");

		                                       
		                                       
		unsigned char* b=new unsigned char[17];
		unsigned int k1,k2;
		for(int j=0, m=36, i=0; j<m; j+=2){
			if(j==8||j==13||j==18||j==23)j++;
			if(sscanf(&uuid[j],"%01x%01x",&k1,&k2)!=2)
				throw CrException("Invalid UUID string '" + string(uuid) + "'. A UUID has to be of the format 2f46dad9-e5c2-457e-b1fd-ad1b49b99aff.");
			b[i++]=k1*16+k2;
		}
		return new konga::BinaryData((const char*)b,16,false);
	}
}

konga::TransformationDataHolder*CTransform::getTransformationDataHolder() const{
	if(m_pTreeMapper==0){
		throw konga::InternalErrorException("CTransform::getTransformationDataHolder() was called with m_pTreeMapper==0");
	}
	return m_pTreeMapper->getTransformationDataHolder();
}

namespace {
	                  
	const string DB_ROW_COUNT_DE_NAME("etlforge.scripting.db.rows_affected");
	const string DB_AUTO_COMMIT_DE_NAME("etlforge.scripting.db.auto_commit");
	const string DB_TRANSACTION_DE_NAME("etlforge.scripting.db.transaction");

	inline void SetRowCountDE(OperationDataElementHolder* pDEHolder, long rowCount) {
		DataElement* const de = pDEHolder->getOperationDataElement(DB_ROW_COUNT_DE_NAME);
		de->setType(DataElement::t_int);
		de->setValue(toString(rowCount).c_str());
	}

	                                                                                                    
	inline void GetTransactionFlags(const OperationDataElementHolder* const pDEHolder, DbLookup* pDbLookup,
		bool& bAutoCommit, bool& bTransaction)
	{
		bAutoCommit = GetSystemVariableAsBool(DB_AUTO_COMMIT_DE_NAME, pDEHolder, true);
		bTransaction = GetSystemVariableAsBool(DB_TRANSACTION_DE_NAME, pDEHolder, false);
		if( bAutoCommit == true && bTransaction == true ) {
			throw CrException("Inconsistent transaction flags. Auto commit and transaction cannot both be set to true. " \
				"Set auto commit to false and choose transaction support or or set auto commit to true and transaction support to false " \
				"to disable any kind of transactions (this is the default).");
		}
		if ( pDbLookup ) pDbLookup->UseAutoCommit(bAutoCommit);
	}

	                                                                                           
	inline void CommitIfNeeded(bool bAutoCommit, bool bTransaction, DbLookup& dbLookup) {
		if( !bAutoCommit && !bTransaction )
			dbLookup.CommitTransaction();
	}
}

void CTransform::CacheLookup(const string& stSourceTargetGuid, const string& stSQL, string& stValue, bool& bValueIsNull)
{
	bValueIsNull = true;
	stValue.clear();
	konga::TransformationDataHolder*holder=getTransformationDataHolder();
	if(holder==0){
		throw konga::InternalErrorException("CTransform::CacheLookup() was called with TransformationDataHolder==0");
	}
	DbLookupCache* pDLookupCache = holder->getDbLookupCache();
	CachedDbLookup* pCachedDbLookup = holder->getCachedDbLookup();
	bool bAutoCommit, bTransaction;
	GetTransactionFlags(getTransformationDataHolder()->getOperationDataElementHolder(), 0, bAutoCommit, bTransaction);

	try {
		DbLookupResultState retVal = pCachedDbLookup->lookupValue(getTreeMapper()->getOperationContext()->getCurrentUser(),
			*pDLookupCache, stSourceTargetGuid, stSQL, stValue, bAutoCommit, bTransaction);
		if ( retVal == DBLRS_LOOKUP_SUCCESS )
			bValueIsNull = false;
		else if ( retVal == DBLRS_NULL_RESULT )
			bValueIsNull = true;
		                                                   
		else if ( retVal == DBLRS_OPEN_DB_FAILED )
			throw WarningException("CacheLookup: Failed to open database.");
		else
			throw WarningException("CacheLookup: Unknown error.");
	}
	catch ( const konga::CrException& ex ) {
		throw WarningException(
			"CacheLookup() failed to execute SQL statement \""
			+ stSQL + "\".\n" + ex.GetErrorString()
			);
	}
}

DataElement* CTransform::getDataElement(int ia,const Token::FNC*f,Token*ts[],std::vector<int>&ind,konga::AutoDeleteStack<DataElement>&tmp)
{
	if(ia>= (int)ind.size()){
		throw konga::ExeFcnException("error occurred in evaluating non existence argument ("+konga::toString(ia)+").\n");
	}
	const int i0=ind.at(ia-1)+1;
	int err=0;
	DataElement*de0=eval(&ts[i0], ind.at(ia)-i0, err);

	                                                                                    
	if(!de0->isTokenDE())tmp.push(de0);

	const int ifcn=f->iFunction;

	if(err==-1)
		throw konga::ExeFcnException("error occurred when evaluating argument "+konga::toString(ia)+", DataElement:"+de0->getName()+" not found.\n");
	if(err){
		KString tmp1;
		if(!tmp1.LoadString(err))tmp1.Format("ERR%d",err);
		throw konga::ExeFcnException("error occurred when evaluating argument "+konga::toString(ia)+", err="+(const char*)tmp1+"\n");
	}
	return de0;
}


DataElement* RowsetToDataElement(DbRowSetData& rows) {
	DataElement* deRows = new DataElement(DataElement::t_collection);

	for(vector<DbRowData*>::const_iterator row_it = rows.rowset.begin(); row_it!=rows.rowset.end(); ++row_it) {
		DataElement* deRow = new DataElement(DataElement::t_collection);
		for(vector<DbData*>::const_iterator data_it = (*row_it)->row.begin(); data_it!=(*row_it)->row.end(); ++data_it) {
			if( (*data_it)->isNull )
				deRow->addToCollection( new DataElement() );
			else
				deRow->addToCollection( new DataElement(DataElement::t_string, (*data_it)->data.c_str()) );
		}
		deRows->addToCollection(deRow);
	}
	return deRows;
}

DataElement* LdapTreeNodeToDataElement(LdapTreeNode* root, bool bReturnNullIfNoResults) {
	if( !root || (root->begin() == root->end() && bReturnNullIfNoResults) ) return new DataElement();
	DataElement* deRows = new DataElement(DataElement::t_collection);
	for(LdapTreeNode::const_iterator entry_it = root->begin(); entry_it!=root->end(); ++entry_it) {
		DataElement* deRow = new DataElement(DataElement::t_collection);
		if( !(*entry_it)->GetDistinguishedName().empty() ) {
			DataElement* pDe = new DataElement(DataElement::t_string, (*entry_it)->GetDistinguishedName().c_str());
			deRow->addToCollection(pDe);
			                                                                                                              
			                                                               
		}
		for(LdapTreeNode::const_iterator attr_it = (*entry_it)->begin(); attr_it!=(*entry_it)->end(); ++attr_it) {
			DataElement* pDe = new DataElement(DataElement::t_string, (*attr_it)->GetData().c_str());
			                                               
			deRow->addToCollection(pDe);
		}
		deRows->addToCollection(deRow);
	}
	return deRows;
}

  
       

                                                             
                                                             

                                                                          

                           
                    
                         
                  
                                                      
                                           
                                   
                                   
                                            
                                    
                                     
                                                        
                                     
                                                                             
                                                 
                                                                             
                                         
                                         
                                                    
                                       
                                                                            
               

                                                                                     

  
#define ERRCODE(a,i) a*1000+i

#define CheckInputDEOn
#define CheckOutputDEOn

void CTransform::handle_err(DataElement*const de, int& err) const {
	if(err==-1&&de->getName()[0]){
		throw konga::ExeFcnException("DataElement '"+string(de->getName())+"' not found.");
	}

	if(err==TARGET_MAPPED_TO_NULL||err==TRANSFORM_INPUT_EMPTY){
		                                                               
		de->setError(0);
		err = 0;
		return;
	}

                                                                 
	                                                                                   
	throw err;
}

std::string CTransform::readStringToDE(konga::DataElement*dsi, const std::string&value, konga::DataElement::Type type, const std::string&format, const std::string& header, int i){
	int err=0;
	FILE*debug_log=getTreeMapper()->m_debug_log;
	dsi->setType(type);
	dsi->setFormatStr(format);
	dsi->setValue(value.c_str());
	if((err=dsi->getError())!=0&&err!=TRANSFORM_INPUT_EMPTY){
		if(debug_log){
			fprintf(debug_log,"TestRun failed to read %s, err=%d\n", dsi->getName(), err);
		}
		getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_TEST_RUN_READ_FAILED, dsi->getName(), err);
		KString tmp;
		tmp.LoadString(err);
		m_lastErr="ERROR: "+tmp;
		return (const char*)m_lastErr;
	}
	
	if(debug_log){
		if(i==0)
			fprintf(debug_log,"TestRun %s=%s, err=%d\n", dsi->getName(), (const char*)dsi->GetString(err),err);
		else
			fprintf(debug_log,"TestRun %s/%d %s=%s, err=%d\n", header.c_str(), i, dsi->getName(), (const char*)dsi->GetString(err),err);
	}
	return "";
}

std::string CTransform::readArrayString(konga::DataElement*de, const std::string& str, konga::DataElement::Type type, const std::string&format, const std::string& header, int i){
	                                                    
	                                                                           
	                                                                                                           
	konga::StringTokenizer tk(str.c_str(), "{},\"\n ");
	tk.NextToken();
	de->initCollection();
	return readArrayString(de, tk, type, format, header, i);
}


std::string CTransform::readArrayString(konga::DataElement*de, konga::StringTokenizer& tk, konga::DataElement::Type type, const std::string&format, const std::string& header, int i){
	std::string header1=header+"/"+konga::toString(i);
	std::string value;
	std::string err_msg;
	bool isValueDefined=false, isDQuoted=false;
	while(tk.NextToken()){
		std::string token(tk.token());
		char c=token.at(0);
		switch(c){
			case '{':
				{
					DataElement*ds=new DataElement();
					de->addToCollection(ds);
					ds->initCollection();
					err_msg=readArrayString(ds,tk,type,format, header1, 1);
				}
				break;
			case '}':
			case ',':
				{
					DataElement*ds=new DataElement();
					de->addToCollection(ds);
					if(isValueDefined){
						value=trim(value);
						err_msg=readStringToDE(ds, value, type, format, header1, i);
						if(!isDQuoted && value=="NULL")ds->setNull();
					}
					else {
						ds->setNull();
						ds->setType(type);
					}
					if(c!=',')return err_msg;
					i++;
					value.clear();
					isDQuoted=false;
					isValueDefined=false;
				}
				break;
			case '\n':
			case ' ':
				if(isValueDefined)value+=token;
				break;
			case '"':
				tk.SpanTo(c);
				tk.NextToken();
				value=tk.token();
				isDQuoted=true;
				isValueDefined=true;
				break;
			default:
				value+=token;
				isValueDefined=true;
				break;
		}
		if(err_msg.length()>0)break;
	}
	if(err_msg.length()==0)
		err_msg=std::string("Error: failed in reading array data element, unterminated braces. ValueString:\"")+tk.getString()+"\"";
	return err_msg;
}

                                                   
                                                            
DataElement*CTransform::processOneArg(int ifcn, DataElement*de0, int&err, KString&err_msg){
	if(de0->isCollection()){
		std::auto_ptr<DataElement> de(new DataElement());
		de->initCollection(de0->IsNull());
		for(int i=0,n=de0->getDECollectionSize(); i<n; i++){
			de->addToCollection(processOneArg(ifcn,de0->getDECollectionOf(i),err,err_msg));
		}
		return de.release();
	}
	DataElement*de=0;
	switch(ifcn){
		case 2:                  
		case 3:                   
			{
				long long k=de0->GetLong(err);
				if(err)handle_err(de0,err);
				de=new DataElement((ifcn==2)?DataElement::t_int:DataElement::t_long,k);
			}
			break;
		case 4:                    
		case 5:                     
			{
				double d1=de0->GetDouble(err);
				if(err)handle_err(de0,err);
				de=new DataElement((ifcn==4)?DataElement::t_float:DataElement::t_double,d1);
			}
			break;
		case 6:                     
			{
				                                              
				                                                                      
				std::string string_rep;
				if( de0->getType() == DataElement::t_binary ) {
					const BinaryData* const bd = de0->GetBinaryDataPointer(err);
					if ( bd != 0 && err == 0 && !bd->HasNulls() ) {
						string_rep = bd->GetStringRep();
					}
					                     
					err = 0;
				}

				if ( string_rep.empty() ) {
					string_rep = (const char*)de0->GetString(err);
					if(err)handle_err(de0,err);
				}
				de = new DataElement(DataElement::t_string, string_rep.c_str());
			}
			break;
		case 7:                   
			{
				bool b1=de0->GetBool(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_bool,b1);
			}
			break;
		case 8:                   
			{
				konga::CrDateTime t1=de0->GetDate(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_date,t1);
			}
			break;
		case 9:                  
		case 10:                     
			{
				double f1=de0->GetDouble(err);
				if(err)handle_err(de0,err);
				if(f1>0.0)
					de=new DataElement(DataElement::t_double,(ifcn==9?log(f1):log(f1)/log(10.0)));
				else if( f1==0) {
					throw konga::ExeFcnException("The logarithm of 0 is undefined.");
				}
				else {
					throw konga::ExeFcnException("The logarithm of a negative number is undefined.");
				}
			}
			break;
		case 12:                   
			{
				double f1=de0->GetDouble(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_double,exp(f1));
			}
			break;
		case 24:                           
			{
				konga::CrDateTime t1=de0->GetDate(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, t1.toString("%m/%d/%Y %I:%M:%S %p").c_str());
			}
			break;
		case 25:                        
			{
				konga::CrDateTime t1=de0->GetDate(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, t1.toString("%A, %B %d, %Y").c_str());
			}
			break;
		case 26:                          
			{
				konga::CrDateTime t1=de0->GetDate(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, t1.toString("%d-%b-%y").c_str());
			}
			break;
		case 27:                         
			{
				konga::CrDateTime t1=de0->GetDate(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, t1.toString("%m/%d/%y").c_str());
			}
			break;
		case 28:                        
			{
				konga::CrDateTime t1=de0->GetDate(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, t1.toString("%I:%M:%S %p").c_str());
			}
			break;
		case 29:                          
			{
				konga::CrDateTime t1=de0->GetDate(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, t1.toString("%I:%M %p").c_str());
			}
			break;
		case 30:                         
			{
				konga::CrDateTime t1=de0->GetDate(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, t1.toString("%H:%M").c_str());
			}
			break;
		case 201:                     
			{
				KString s=de0->GetString(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, konga::md5((const char*)s).c_str());
			}
			break;
		case 311:                                 
			{
				const string str((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);
				long long low = 0, high = 0;
				konga::md5(str, low, high);
				de = new DataElement(DataElement::t_collection);
				de->addToCollection( new DataElement(DataElement::t_long, low) );
				de->addToCollection( new DataElement(DataElement::t_long, high) );
			}
			break;
		case 202:                           
			{
				KString s=de0->GetString(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_bool, isPresent(m_sourceData,s));
			}
			break;
		case 203:                       
			{
				KString s=de0->GetString(err);
				if (err==-1 || err == TRANSFORM_INPUT_EMPTY || err == TARGET_MAPPED_TO_NULL) {
					                                            
					err = 0;
					de0->setError(0);
				}
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_bool, isNil(m_sourceData,de0->getName()));
			}
			break;
		case 213:                             
			{
				KString s=de0->GetString(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_bool, getTransformationDataHolder()->getLdapExecutor()->LdapExecute((const char*)s));
			}
			break;
		case 14:                     
			{
				KString s=de0->GetString(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, trim((const char*)s).c_str());
			}
			break;
		case 15:                      
			{
				KString s=de0->GetString(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, rtrim((const char*)s).c_str());
			}
			break;
		case 16:                      
			{
				KString s=de0->GetString(err);
				if(err)handle_err(de0,err);
				de=new DataElement(DataElement::t_string, ltrim((const char*)s).c_str());
			}
			break;
		case 33:                      
		case 34:                       
			{
				                          
				KString s=de0->GetString(err);
				err=0;
				KString s2;
				char c=ifcn==33?'\'':'"';
				s2.Format("%c%s%c",c,(const char*)s,c);
				de=new DataElement(DataElement::t_string,(const char*)s2);
			}
			break;
		case 68:                             
			{
				if(getTreeMapper()->useJbNode()){
					konga::JbNode* jbNode=de0->getJbNode();
					std::string value=jbNode->getXmlString();
					de = new DataElement(DataElement::t_string, value.c_str());
				}
				else {
					const xml::DOMNodePtr pNode = de0->getXmlNode();
					if(pNode!=0) {
						de = new DataElement(DataElement::t_string, pNode->getXml().c_str());
					}
					else {
						const KString& s=de0->GetString(err);
						if(err)handle_err(de0,err);
						de=new DataElement(DataElement::t_string, getXmlString(m_sourceData,s).c_str());
						if(err)err_msg.Format("From GetXmlString() source=%s path=\"%s\"\n",
							(const char*)m_sourceData->GetDTDNode()->get_Path(),(const char*)s);
					}
				}
			}
			break;
		case 205:                     
		case 206:                      
		case 207:                        
			{
				int n=de0->GetInteger(err);
				if(err)handle_err(de0,err);
				switch(ifcn){
					case 205: n=Days(n); break;
					case 206: n=Hours(n); break;
					case 207: n=Minutes(n); break;
				}
				if(ifcn==207)n=Minutes(n);
				de=new DataElement(DataElement::t_int, n);
			}
			break;
		case 233:                         
		case 234:                         
			{
				KString s(de0->GetString(err));
				err = 0;	                                                   
				if ( ifcn == 233 ) { s.MakeLower(); } else { s.MakeUpper(); }
				de=new DataElement(DataElement::t_string, (const char*)(s));
			}
			break;
		case 235:                              
			{
				unsigned char* bytes=0;
				unsigned long n=0;
				KString s;
				if(de0->getType()==konga::DataElement::t_binary){
					konga::BinaryData* pbd=de0->GetBinaryDataPointer(err);
					if(pbd)bytes=reinterpret_cast<unsigned char*>(const_cast<char*>(pbd->GetPointerToData(n)));
				}
				else {
					s=de0->GetString(err);
					bytes=(unsigned char*)((const char*)s);
					n=(int)s.GetLength();
				}
				if(err)handle_err(de0,err);
				const string& base64Encoded = konga::EncodeBase64(bytes, n);
				de=new DataElement(DataElement::t_string, base64Encoded.c_str());
			}
			break;
		case 236:                              
			{
				                                    
				string base64Encoded((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);
				konga::WrapLines(base64Encoded, 64);
				int decodedLength = 0;
				unsigned char* bytes=konga::DecodeBase64(base64Encoded,decodedLength);
				konga::BinaryData*pbd=new konga::BinaryData((const char*)bytes, decodedLength, false);
				de=new DataElement(pbd);
			}
			break;
		case 284:                          
			{
				string proper( (const char*)de0->GetString(err) );
				if(err)handle_err(de0,err);
				konga::ToProper(proper);
				de=new DataElement(DataElement::t_string, proper.c_str());
			}
			break;
		default:
			throw InternalErrorException("processOneArg called with invalid ifcn = " + toString(ifcn));
	}
	assert(de!=0);
	return de;
}

DataElement*CTransform::if_Array(DataElement*b1, DataElement*r2, DataElement* r3, int&err, KString& err_msg){
	if(b1->isCollection()){
		std::auto_ptr<DataElement> de(new DataElement());
		de->initCollection(r2->IsNull());
		int n1=b1->getDECollectionSize(), n2=0, n3=0;
		if(r2->isCollection())n2=r2->getDECollectionSize();
		if(r3->isCollection())n3=r3->getDECollectionSize();
		if((n2!=0 && n2!=n1) || (n3!=0 && n3!=n1)){
			err_msg.Format("Error: inconsistent array size for the arguments of If() function, n1/n2/n3=%d/%d/%d",n1,n2,n3);
			err=-1;
			return 0;
		}
		for(int i=0; i<n1; i++){
			DataElement*ds1=b1->getDECollectionOf(i), *ds2, *ds3;
			ds2=n2?r2->getDECollectionOf(i):r2;
			ds3=n3?r3->getDECollectionOf(i):r3;
			DataElement *ds=if_Array(ds1,ds2,ds3,err,err_msg);
			if(err)break;
			de->addToCollection(ds);
		}
		return de.release();
	}
	else {
		bool b=b1->GetBool(err);
		if(err)handle_err(b1, err);
		return new DataElement(*(b?r2:r3));
	}
}

DataElement* CTransform::processTwoArgs(int ifcn, konga::DataElement*de1, konga::DataElement*de2, int&err, KString&err_msg){
	if(err)return 0;
	assert(de1);
	assert(de2);
	const bool b1=de1->isCollection(), b2=de2->isCollection();
	if(b1 || b2){
		int n1=0, n2=0;
		if(b1)n1=de1->getDECollectionSize();
		if(b2)n2=de2->getDECollectionSize();
		if(n1!=0 && n2!=0 && n1!=n2){
			err=-1;
			err_msg="Error: inconsistent size of the array argument n1/n2 %d/%d";
		}
		std::auto_ptr<DataElement> de(new DataElement);
		de->initCollection();
		for(int i=0, n=b1?n1:n2; i<n; i++){
			DataElement*ds1=b1?de1->getDECollectionOf(i):de1,
				*ds2=b2?de2->getDECollectionOf(i):de2;
			de->addToCollection(processTwoArgs(ifcn,ds1,ds2,err,err_msg));
		}
		return de.release();
	}

	DataElement*de=0;
	switch(ifcn){
		case 11:                    
			{
				double f1=0.0,f2=0.0;
				f1=de1->GetDouble(err);
				if(err)handle_err(de1,err);
				f2=de2->GetDouble(err);
				if(err)handle_err(de2,err);
				de=new DataElement(DataElement::t_double,pow(f1,f2));
			}
			break;
		case 17:                      
			{
				KString s1=de1->GetString(err);
				if(err)handle_err(de1,err);
				int i2=de2->GetInteger(err);
				if(err)handle_err(de2,err);
				if(i2<0)i2+=(int)s1.GetLength();
				                                                                        
				string utf8string((const char*)s1);
				string utf8substring = EncodingStringUtils::Utf8Right(utf8string,i2);
				de=new DataElement(DataElement::t_string,(const char*)(utf8substring.c_str()));

			}
			break;
		case 18:                     
			{
				KString s1=de1->GetString(err);
				if(err)handle_err(de1,err);
				int i2=de2->GetInteger(err);
				if(err)handle_err(de2,err);
				if(i2<0)i2+=(int)s1.GetLength();
				                                                                       
				string utf8string((const char*) s1);
				string utf8substring = EncodingStringUtils::Utf8Left (utf8string,i2);
				de=new DataElement(DataElement::t_string,(const char*)(utf8substring.c_str()));
			}
			break;
		case 21:                     
			{
				KString s1=de1->GetString(err);
				if(err)handle_err(de1,err);
				int i2=de2->GetInteger(err);
				if(err)handle_err(de2,err);
				de=new DataElement(DataElement::t_string,(const char*)lpad(s1,i2));
			}
			break;
		case 22:                     
			{
				KString s1=de1->GetString(err);
				if(err)handle_err(de1,err);
				int i2=de2->GetInteger(err);
				if(err)handle_err(de2,err);
				de=new DataElement(DataElement::t_string,(const char*)rpad(s1,i2));
			}
			break;
		case 36:                       
			{
				const string format_str((const char*)de1->GetString(err));
				err=0;	                                                               
				if(err)handle_err(de2,err);
				if ( de2 )
					de=new DataElement(DataElement::t_string, de2->Format(format_str.c_str(), err));
				else
					de=new DataElement(DataElement::t_string, "");
			}
			break;
		case 317:                     
			{
				const long long num = de1->GetLong(err);
				if(err)handle_err(de1,err);
				const long long den = de2->GetLong(err);
				if(err)handle_err(de2,err);

				const long long rem = ( den == 0 ? num : num%den );
				de = new DataElement(DataElement::t_long, rem);
			}
			break;
		default:
			throw InternalErrorException("processTwoArgs called with invalid ifcn = " + toString(ifcn));
	}
	assert(de!=0);
	return de;
}

DataElement* CTransform::processThreeArgs(int ifcn, konga::DataElement*de1, konga::DataElement*de2, konga::DataElement*de3, int&err, KString&err_msg){
	bool b1=de1->isCollection(), b2=de2->isCollection(), b3=de3->isCollection();
	if(b1 || b2 || b3){
		int n1=0, n2=0, n3=0;
		if(b1)n1=de1->getDECollectionSize();
		if(b2)n2=de2->getDECollectionSize();
		if(b3)n3=de3->getDECollectionSize();
		int n=n1?n1:(n2?n2:n3);
		if((n1!=0 && n1!=n) || (n2!=0 && n2!=n) || (n3!=0 && n3!=n)){
			err=-1;
			err_msg="Error: inconsistent size of the array argument n1/n2/n3 %d/%d/%d";
		}
		std::auto_ptr<DataElement> de(new DataElement);
		de->initCollection();
		for(int i=0, n=b1?n1:n2; i<n; i++){
			DataElement*ds1=b1?de1->getDECollectionOf(i):de1,
				*ds2=b2?de2->getDECollectionOf(i):de2,
				*ds3=b3?de3->getDECollectionOf(i):de3;
			de->addToCollection(processThreeArgs(ifcn,ds1,ds2,ds3,err,err_msg));
			if(err)break;
		}
		return de.release();
	}
	DataElement*de=0;
	switch(ifcn){
		case 19:                     
			{
				if(de1->IsNull()){
					de=new DataElement();
				}
				else {
					KString str=de1->GetString(err);
					if(err)str.Empty();
					const int i2=de2->GetInteger(err);
					if(err)handle_err(de2,err);
					const int i3=de3->GetInteger(err);
					if(err)handle_err(de3,err);
					                                                                          
					string utf8string((const char*)str);
					string utf8substring = EncodingStringUtils::Utf8SubString(utf8string,i2,i3);
					de=new DataElement(DataElement::t_string,(const char*)(utf8substring.c_str()));
					err=0;
				}
			}
			break;
		case 20:                         
			{
				if(de1->IsNull()){
					de=new DataElement();
				}
				else {
					KString st=de1->GetString(err);
					if(err)st.Empty();
					KString sOld=de2->GetString(err);
					if(err)sOld.Empty();
					KString sNew=de3->GetString(err);
					if(err)sNew.Empty();
					st.Replace((const char*)sOld,(const char*)sNew);
					de=new DataElement(DataElement::t_string,(const char*)st);
					err=0;
				}
			}
			break;
		case 35:                         
			{
				KString s2=de2->GetString(err);
				if(err)handle_err(de2,err);
				KString s3=de3->GetString(err);
				if(err)handle_err(de3,err);
				std::string stErrMsg;
				KString output_data_str=CVTDate(de1,s2,s3,err,stErrMsg);
				if(!stErrMsg.empty()){
					int err1;
					err_msg.Format("\"%s\",\"%s\",\"%s\") failed. ERROR: %s\n",
						(const char*)de1->GetString(err1), (const char*)s2, (const char*)s3, stErrMsg.c_str());
					throw konga::ExeFcnException((const char*)err_msg);
				}
				de=new DataElement(DataElement::t_string,(const char*)output_data_str);
			}
			break;
		case 247:                           
		case 248:                           
			{
				const std::string in_string((const char*)de1->GetString(err));
				err=0;	                      

				const std::string pad_string((const char*)de2->GetString(err));
				err=0;	                      

				const int Npad = de3->GetInteger(err);
				if(err)handle_err(de3, err);

				char pad_char = ' ';	                         
				if ( !pad_string.empty() )
					pad_char = *pad_string.begin();
				switch(ifcn) {
				case 247:            
					if ( Npad >= 0 )
						de=new DataElement(DataElement::t_string, konga::LPadChar(in_string, pad_char, Npad).c_str());
					else
						de=new DataElement(DataElement::t_string, konga::RPadChar(in_string, pad_char, -Npad).c_str());
					break;
				case 248:            
					if ( Npad >= 0 )
						de=new DataElement(DataElement::t_string, konga::RPadChar(in_string, pad_char, Npad).c_str());
					else
						de=new DataElement(DataElement::t_string, konga::LPadChar(in_string, pad_char, -Npad).c_str());
					break;
				};
			}
			break;
		default:
			throw InternalErrorException("processThreeArgs called with invalid ifcn = " + toString(ifcn));
	}
	assert(de!=0);
	return de;
}

int CTransform::createArray(Token*t, DataElement*&de, konga::Token*ts[], vector<int>&ind, konga::AutoDeleteStack<konga::DataElement>&tmp)
{
	tmp.push(de=new DataElement());
	int err=0;
	try {
		de->initCollection();
		for(vector<int>::size_type i=1; i<ind.size();i++){
			const int i0=ind.at(i-1)+1;
			err=0;
			if(ind.at(i)>i0){
				DataElement*de_i=eval(&ts[i0], ind.at(i)-i0, err);
				if(de_i->isTokenDE())de_i=new DataElement(*de_i);
				if(err!=0)throw err;
				de->addToCollection(de_i);
			}
		}
	}
	catch( const WarningException& e ) {
		const string& warning = e.GetWarning()+
			"\nError occurred when creating array "+t->source_location()+"\n";
		getTreeMapper()->getViewLog()->LogMessage("%s", warning.c_str());
		getTreeMapper()->m_lastErr += warning;

		if( m_pDebuggee && m_pDebuggee->breakOnWarning() ) {
			m_pDebuggee->setWarning(warning);
			IntegrationDebugBreakpointHitHelper(t);
		}

		throw konga::CrException(warning);
	}
	catch(int err){
		string err_msg("Error occurred when creating array "+t->source_location());
		if(*(err_msg.end()-1) == '(') err_msg.erase(err_msg.end()-1);
		err_msg += ": ";
		KString tmp;
		if(!tmp.LoadString(err)){
			tmp=konga::toString(err).c_str();
		}
		string sErr = err_msg+(const char*)tmp;

		if( m_pDebuggee && m_pDebuggee->breakOnError() ) {
			m_pDebuggee->setError(sErr);
			IntegrationDebugBreakpointHitHelper(t);
		}

		throw CrException(sErr);
	}

	return err;
}

namespace {
	string fixFncName(const char* fnc_name) {
		string out;
		konga::ReplaceCharWithString(fnc_name, out, '(', "");
		return out;
	}

	                                                                        
	bool nullInputIsOkForOneArgFcn(const int ifcn) {
		switch(ifcn){
			case 33:
			case 34:
			case 203:
				return true;
			default:
				return false;
		}
	}

	string truncate_sql_for_timing(Token*t, const CROM* const pTargetNode, const string& stSql) {
		string s( (t==0||pTargetNode==0) ? "unknown_" :
			string(string((const char*)pTargetNode->GetName()) + "_" + toString(t->line_number()) + "_")
			);
		s += stSql.substr(0, 50);

		                                    
		const string tokens(" \"'\r\n().,=:*");
		string::size_type iPos = 0;
		while ( (iPos = s.find_first_of(tokens, iPos)) != string::npos ) {
			s.replace(iPos, 1, 1, '_');
			++iPos;
		}
		return s;
	}

	bool IsDataElementNullImpl(DataElement* const de0, int err) {
		return err==NULL_DATA_ELEMENT ||
			de0->getType()==DataElement::t_unknown ||
			(de0->getError()!=0 && de0->getError()!=TRANSFORM_INPUT_EMPTY) ||
			de0->IsNull();
	}
}

int CTransform::ExecFunction(Token*t,DataElement*& de,
							 Token*ts[],
							 vector<int>& ind,
							 konga::AutoDeleteStack<DataElement>& tmp)
{
	const Token::FNC*f=&Token::fnc[t->data()];

	std::auto_ptr<EnterAccumulativeScope> scope_profiling(
		getTreeMapper()->m_pProfilingKit == 0 ? 0 :
		new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "FBFunctions", fixFncName(f->fnc_name))
		);

	DataElement*de0=0;
	DataElement*de1=0,*de2=0,*de3=0,*de4=0;
	de=0;
	KString err_msg;
	int err=0;
	int argn=(int)ind.size()-1;
	if(argn==1){
		if((1+ind.at(0))==ind.at(1))argn=0;
	}

	konga::OperationContext* const pOperationContext = getTreeMapper()->getOperationContext();
	assert(pOperationContext);

	try {
		if(f==0)throw konga::ExeFcnException("TRANSFORM_FUNCTION_DOES_NOT_EXIST");
		const int argn0=(int)strlen(f->fnc_args);
		if(f->isFixedArgumentCount() && argn!=argn0 )
			throw konga::ExeFcnException("Wrong number of arguments, argn="+konga::toString(argn)+". It should be "+konga::toString(argn0));
		const int ifcn=f->iFunction;
		switch(ifcn)
		{
		                                                    
		case 1:                         
			tmp.push(de=new DataElement(DataElement::t_int, GetRecord()));
			break;
		case 2:                  
		case 3:                   
		case 4:                    
		case 5:                     
		case 6:                     
		case 7:                   
		case 8:                   
		case 9:                  
		case 10:                     
		case 12:                   
		case 14:                     
		case 15:                      
		case 16:                      
		case 24:                           
		case 25:                        
		case 26:                          
		case 27:                         
		case 28:                        
		case 29:                          
		case 30:                         
		case 33:                      
		case 34:                       
		case 68:                             
		case 201:                     
		case 311:                                 
		case 202:                           
		case 203:                       
		case 213:                             
		case 233:                         
		case 234:                         
		case 235:                              
		case 236:                              
		case 284:                          
			de0=getDataElement(1,f,ts,ind,tmp);
			if( de0->IsNull() && !nullInputIsOkForOneArgFcn(ifcn) ){
				tmp.push(de = new DataElement());
				break;
			}

			de=processOneArg(ifcn, de0, err, err_msg);
			if(err_msg.IsEmpty()==false)err_msg+=inputArgumentsDescription(1, de0);
			if(!de->isTokenDE())tmp.push(de);
			break;
		case 13:                    
			{
				if(argn<2)
					throw WarningException("Only one argument was passed to the If function. Two or three are required.");
				if(argn>3)
					throw WarningException("Too many arguments were passed to the If function. Only two or three are allowed.");

				de1=getDataElement(1,f,ts,ind,tmp);
				if(!de1->isCollection()){
					const bool b=de1->GetBool(err);
					if(err)handle_err(de1,err);
					if( b ) {
						de = getDataElement(2,f,ts,ind,tmp);
					}
					else {
						if ( argn > 2 ) {
							de = getDataElement(3,f,ts,ind,tmp);
						}
						else {
							                                                    
							tmp.push( de = new DataElement() );
						}
					}
					break;
				}

				                                              
				if(argn!=3)
					throw WarningException("An array was passed to the If function. This use of the If function requires exactly three arguments.");

				de2=getDataElement(2,f,ts,ind,tmp);
				de3=getDataElement(3,f,ts,ind,tmp);
				de=if_Array(de1,de2,de3,err,err_msg);
				if(err!=0){
					err_msg+=inputArgumentsDescription(3, de1, de2, de3);
					throw konga::ExeFcnException((const char*)err_msg);
				}
				if(!de->isTokenDE())tmp.push(de);
			}
			break;
		case 11:                    
		case 17:                      
		case 18:                     
		case 21:                     
		case 22:                     
		case 36:                       
		case 317:                     
			{
				de1=getDataElement(1,f,ts,ind,tmp);
				de2=getDataElement(2,f,ts,ind,tmp);
				de=processTwoArgs(ifcn, de1, de2, err, err_msg);
				if(err!=0){
					err_msg+=inputArgumentsDescription(2, de1, de2);
					throw konga::ExeFcnException((const char*)err_msg);
				}
				if(!de->isTokenDE())tmp.push(de);
			}
			break;
		case 19:                     
		case 20:                         
		case 35:                         
		case 247:                           
		case 248:                           
			{
				de1=getDataElement(1,f,ts,ind,tmp);
				de2=getDataElement(2,f,ts,ind,tmp);
				de3=getDataElement(3,f,ts,ind,tmp);
				de=processThreeArgs(ifcn,de1,de2,de3,err, err_msg);
				if(err_msg.IsEmpty()==false){
					err_msg+=inputArgumentsDescription(3, de1, de2, de3);
				}
				if(!de->isTokenDE())tmp.push(de);
			}
			break;
		case 23:                  
		case 209:                    
			{
				const bool bTruncateFractionSeconds=(ifcn==23);
				tmp.push(de=new DataElement(DataElement::t_date,konga::CrDateTime(bTruncateFractionSeconds)));
			}
			break;
		case 31:                         
			{
				std::auto_ptr<EnterAccumulativeScope> scope_profiling(
					getTreeMapper()->m_pProfilingKit == 0 ? 0 :
					new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "FBFunctions", "DB_LOOKUP_TOTAL")
					);
				de0=getDataElement(1,f,ts,ind,tmp);
				const string stSourceTargetGuid((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);

				de0=getDataElement(2,f,ts,ind,tmp);
				const string stSql((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);				

				std::auto_ptr<EnterAccumulativeScope> scope_profiling2(
					getTreeMapper()->m_pProfilingKit == 0 ? 0 :
					new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "DbLookups", truncate_sql_for_timing(t, m_pTargetNode, stSql))
					);

				konga::DbLookup* pDbLookup = 0;
				try {
					pDbLookup = &getTransformationDataHolder()->getDbLookupCache()->getDbLookup(pOperationContext->getCurrentUser(), stSourceTargetGuid);

					bool bAutoCommit, bTransaction;
					GetTransactionFlags(getTransformationDataHolder()->getOperationDataElementHolder(), pDbLookup, bAutoCommit, bTransaction);

					string stDbLookupValue;
					konga::DbLookupResultState resState = pDbLookup->LookupSingleValue(stSql, stDbLookupValue);
					CommitIfNeeded(bAutoCommit, bTransaction, *pDbLookup);

					if(resState==konga::DBLRS_LOOKUP_SUCCESS) {
						SetRowCountDE(getTransformationDataHolder()->getOperationDataElementHolder(), pDbLookup->GetRowCount());
						tmp.push(de=new DataElement(DataElement::t_string, stDbLookupValue.c_str()));
					}
					else if(resState==konga::DBLRS_NULL_RESULT) {
						SetRowCountDE(getTransformationDataHolder()->getOperationDataElementHolder(), pDbLookup->GetRowCount());
						tmp.push(de=new DataElement());
					}
					else {
						err = resState;
						throw WarningException("DBLookup() failed to execute SQL statement \""
							+ stSql + "\".");
					}
				} catch( const CrException& e ) {
					                                            
					if ( pDbLookup )
						pDbLookup->Close();
					throw WarningException("DBLookup() failed to execute SQL statement \""
						+ stSql + "\".\n" + e.GetErrorString());
				}
			}
			break;
		case 37:                         
			tmp.push(de=new DataElement(DataElement::t_int, m_AutoNumber));
			break;
		case 38:                   
			tmp.push(de=new DataElement());
			break;
		case 39:                      
			{
				                                                                                                                  
				const int i0=ind.at(0)+1;
				de0=eval(&ts[i0],ind.at(1)-i0,err);
				if(!de0->isTokenDE())tmp.push(de0);
				if(err&&err!=NULL_DATA_ELEMENT){
					handle_err(de0,err);
				}

				const bool isNull = IsDataElementNullImpl(de0, err);
				tmp.push(de=new DataElement(DataElement::t_bool, isNull));
				err=0;
			}
			break;
		case 321:                        
			{
				const int i0=ind.at(0)+1;
				de0=eval(&ts[i0],ind.at(1)-i0,err);
				if(!de0->isTokenDE())tmp.push(de0);
				if(err&&err!=NULL_DATA_ELEMENT){
					handle_err(de0,err);
				}
				const bool isNull = IsDataElementNullImpl(de0, err);

				if ( isNull ) {
					de = getDataElement(2,f,ts,ind,tmp);
				}
				else {
					de = de0;
				}
				err=0;
			}
			break;
		case 322:                         
			{
				const int i0=ind.at(0)+1;
				de0=eval(&ts[i0],ind.at(1)-i0,err);
				if(!de0->isTokenDE())tmp.push(de0);
				if(err&&err!=NULL_DATA_ELEMENT){
					handle_err(de0,err);
				}
				const bool isNull = IsDataElementNullImpl(de0, err);

				bool isEmpty = true;
				if ( !isNull ) {
					isEmpty = (de0->GetStringLength(err) == 0);
				}

				if ( isNull || isEmpty ) {
					de = getDataElement(2,f,ts,ind,tmp);
				}
				else {
					de = de0;
				}
				err=0;
			}
			break;
		case 40:                       
			{
				try {
					de0=getDataElement(1,f,ts,ind,tmp);
					bool isNull=de0->getType()==DataElement::t_unknown||de0->getError()!=0;
					tmp.push(de=new DataElement(DataElement::t_bool,!isNull));
				}
				catch(...){
					tmp.push(de=new DataElement(DataElement::t_bool,false));
				}
			}
			break;
		case 41:                     
			{
				try {
					int i0=ind.at(0)+1;
					de1=eval(&ts[i0],ind.at(1)-i0,err);
					if(!de1->isTokenDE())tmp.push(de1);
					if(err==0){
						tmp.push(de=new DataElement(DataElement::t_string,de1->GetString(err)));
						err = 0;
					}
					else
						throw konga::CrException("");                                                                                                        
				}
				                                                
				catch( const konga::CrFatalException& ) {
					throw;
				}
				catch( const RaiseErrorException& ) {
					throw;
				}
				catch(...){
					                                       
					int i0=ind.at(1)+1;
					de2=eval(&ts[i0],ind.at(2)-i0,err);
					if(!de2->isTokenDE())tmp.push(de2);
					if(err)handle_err(de2,err);
					tmp.push(de=new DataElement( DataElement::t_string,de2->GetString(err)));
				}
			}
			break;
		case 42:                       
			{
				if(argn < 2) {
					throw WarningException("Index takes at least two arguments.");
				}
				de0=getDataElement(2,f,ts,ind,tmp);
				const KString& needle=de0->GetString(err);
				err = 0;	                                                
				int nInstance = 1;
				if (argn == 3) {
					de0=getDataElement(3,f,ts,ind,tmp);
					nInstance=de0->GetInteger(err);
					if ( nInstance == 0 ) nInstance = 1;	                                                  
					if(err)handle_err(de0,err);
				}
				de0=getDataElement(1,f,ts,ind,tmp);
				if(de0->isCollection()){
					de=new DataElement();
					de->initCollection(de0->IsNull());					
					for(int j=0, n=(int)de0->getDECollectionSize(); j<n; j++){
						DataElement* de_i=de0->getDECollectionOf(j);
						const KString& haystack=de_i->GetString(err);
						err = 0;	                      
						if(nInstance==0){
							de_i=new DataElement(DataElement::t_int,0);
						} else {
							de_i=new DataElement(DataElement::t_int,IndexOfStringUtf8Character(haystack,needle,nInstance));
						}
						de->addToCollection(de_i);
					}
					tmp.push(de);
				} else {
					const KString& haystack=de0->GetString(err);
					err = 0;	                                               
					if(nInstance==0){
						tmp.push(de=new DataElement(DataElement::t_int,0));
					} else {
						tmp.push(de=new DataElement(DataElement::t_int,IndexOfStringUtf8Character(haystack,needle,nInstance)));
					}
				}
			}
			break;
		case 43:                            
			{
				std::auto_ptr<EnterAccumulativeScope> scope_profiling(
					getTreeMapper()->m_pProfilingKit == 0 ? 0 :
					new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "FBFunctions", "DB_LOOKUP_TOTAL")
					);
				de0=getDataElement(1,f,ts,ind,tmp);
				const string sSourceTargetGuid( (const char*)de0->GetString(err) );
				if(err)handle_err(de0,err);

				de0=getDataElement(2,f,ts,ind,tmp);
				const string sql( (const char*)de0->GetString(err) );
				if(err)handle_err(de0,err);

				std::auto_ptr<EnterAccumulativeScope> scope_profiling2(
					getTreeMapper()->m_pProfilingKit == 0 ? 0 :
					new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "CacheLookups", truncate_sql_for_timing(t, m_pTargetNode, sql))
					);

				string stResult;
				bool bValueIsNull = true;

				CacheLookup(sSourceTargetGuid, sql, stResult, bValueIsNull);

				if ( bValueIsNull ) {
					tmp.push( de=new DataElement() );
				}
				else {
					tmp.push( de=new DataElement(DataElement::t_string, stResult.c_str()) );
				}
			}
			break;
		case 44:                             
		case 100:                            
			{
				KString s1;
				de1=getDataElement(1,f,ts,ind,tmp);
				s1=de1->GetString(err);
				if(err)handle_err(de1,err);
				de2=getDataElement(2,f,ts,ind,tmp);
				de3=getDataElement(3,f,ts,ind,tmp);
				de=CResolve(s1,de2,de3,err,err_msg);
				if(err_msg.IsEmpty()==false)err_msg+=inputArgumentsDescription(3,de1,de2,de3);
				if(!de->isTokenDE())tmp.push(de);
			}
			break;
		case 45:                    
			{
				int k,n=(int)ind.size()-1;
				for(k=1;k<n;k+=2){
					int i0=ind.at(k-1)+1;
					de0=eval(&ts[i0],ind.at(k)-i0,err);
					if(!de0->isTokenDE())tmp.push(de0);
					if(err==0){
						if(de0->GetBool(err)&&err==0){
							de=getDataElement(k+1,f,ts,ind,tmp);
							k=-1;
							break;
						}
					}
				}
				if(k>=0){
					tmp.push(de=new DataElement());
				}
			}
			break;
		case 46:                      
			{
				int k=0;
				de0=getDataElement(1,f,ts,ind,tmp);
				KString s1=de0->GetString(err);
				if(err==0){
					for(k=1;k<argn;k++){
						int i0=ind.at(k)+1;
						de0=eval(&ts[i0],ind.at(k+1)-i0,err);
						if(!de0->isTokenDE())tmp.push(de0);
						if(err==0){
							KString sk=de0->GetString(err);
							if(err==0&&s1==sk){
								tmp.push(de=new DataElement(DataElement::t_int,k));
								k=-1;
								break;
							}
						}
					}
				}
				if(k>=0)tmp.push(de=new DataElement(DataElement::t_int,0));
				if(err)handle_err(de0,err);
				break;
			}
		case 48:                   
			{
				DataElement*de1=getDataElement(1,f,ts,ind,tmp);
				tmp.push(de=Max(de1));
			}
			break;
		case 49:                   
			{
				DataElement*de1=getDataElement(1,f,ts,ind,tmp);
				tmp.push(de=Min(de1));
			}
			break;
		case 50:                      
			{
				KString s1;
				de0=getDataElement(1,f,ts,ind,tmp);
				s1=de0->GetString(err);
				if(err==0){
					de0=getDataElement(2,f,ts,ind,tmp);
					tmp.push(de=Exist(s1,de0,err,err_msg));
					if(err_msg.IsEmpty()==false)err_msg+=inputArgumentsDescription(1,de0);
				}
				if(err)handle_err(de0,err);
			}
			break;
		case 51:                   
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				de=sum(de0,err,err_msg);
				if(err_msg.IsEmpty()==false)err_msg+=inputArgumentsDescription(1,de0);
				if(!de->isTokenDE())tmp.push(de);
			}
			break;
		case 52:                     
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				tmp.push(de=Count(de0, err, err_msg));
				if(err_msg.IsEmpty()==false)err_msg+=inputArgumentsDescription(1,de0);
			}
			break;
		case 53:                          
			{
				de1=getDataElement(1,f,ts,ind,tmp);
				de2=0;
				int i1=de1->GetInteger(err);
				if(err==0){
					de2=getDataElement(2,f,ts,ind,tmp);
					try {
						tmp.push(de=FindByPosition(i1, de2, err, err_msg));
						if(err_msg.IsEmpty()==false)err_msg+=inputArgumentsDescription(2,de1,de2);
					}
					catch( const CrException& e ) {
						err_msg=e.GetErrorString()+inputArgumentsDescription(2,de1,de2);
						throw ExeFcnException((const char*)err_msg);
					}
				}
			}
			break;
		case 54:                           
			{
				char delimiter=';';
				bool bOmitLast=false;
				switch(argn){
				case 3:
					de3=getDataElement(3,f,ts,ind,tmp);
					bOmitLast=de3->GetBool(err);
					if(err)handle_err(de3,err);
					           
				case 2:
					{
						de2=getDataElement(2,f,ts,ind,tmp);
						const KString& sDelimiter=de2->GetString(err);
						if(err)handle_err(de2,err);
						delimiter=konga::getDelimiterChar((const char*)sDelimiter);
					}
					           
				case 1:
					de1=getDataElement(1,f,ts,ind,tmp);
					tmp.push(de=SumString(de1, delimiter, bOmitLast, err, err_msg));
					if(err_msg.IsEmpty()==false){
						err_msg+=inputArgumentsDescription(3,de1,de2,de3);
					}
					break;
				default:
					err_msg="Wrong number of arguments, argn="+konga::toString(argn)+". It should be "+konga::toString(argn0)+inputArgumentsDescription(3,de1,de2,de3);
					throw konga::ExeFcnException((const char*)err_msg);
				}
			}
			break;
		case 69:                          
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const KString& str=de0->GetString(err);
				err = 0;	                     
				de0=getDataElement(2,f,ts,ind,tmp);
				const int nfirst=de0->GetInteger(err);
				if(err)handle_err(de0,err);
				de0=getDataElement(3,f,ts,ind,tmp);
				const int nlast=de0->GetInteger(err);
				if(err)handle_err(de0,err);
				if(nfirst<0 || nlast<0){
					err=CALL_TRUNCATE_WITH_NEG_LENGTH;
					throw err;
				}
				                                                   
				                   
				                                                          
				          
				                                                                                                
				   
				string utf8string((const char*)str);
				string utf8substring = EncodingStringUtils::Utf8Truncate(utf8string,nfirst,nlast);
				tmp.push(de=new DataElement(DataElement::t_string,(const char*)(utf8substring.c_str())));
			}
			break;
		case 70:                          
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const string stErrorStr((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);
				throw RaiseErrorException(stErrorStr);
			}
			break;
		case 246:                                      
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const string stMsg((const char*)de0->GetString(err));	                
				if ( pOperationContext ) {
					pOperationContext->CancelOperationChain(stMsg);
				}
				tmp.push(de=new DataElement());	                       
			}
			break;
		case 200:                        
			{
				de3=getDataElement(3,f,ts,ind,tmp);
				try {
					int i0=ind.at(0)+1;
					                                     
					de1=eval(&ts[i0],ind.at(1)-i0,err);
					if(!de1->isTokenDE())tmp.push(de1);
					if(err)throw konga::CrException("");
					bool isNull=de1->getType()==DataElement::t_unknown||de1->getError()!=0 || de1->IsNull();
					if(isNull)throw konga::CrException("");                                           
					if(err==0){
						const KString& sTmp=de3->GetString(err);
						if(err==0)de1->setFormatStr(sTmp);
						if(err)handle_err(de1,err);
					}
					tmp.push(de=new DataElement(DataElement::t_string,de1->GetString(err)));
				}
				catch(...){
					                   
					de1=getDataElement(2,f,ts,ind,tmp);
					tmp.push(de=new DataElement(DataElement::t_string,de1->GetString(err)));
				}
			}
			break;
		case 47:	                      
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				size_t size = 0;
				if (de0->IsNull()){
					size=0;
				}
				else if ( de0->isCollection() ) {
					size = de0->getDECollectionSize();
				}
				else if ( de0->getType() == DataElement::t_binary ) {
					                                                                                            
					konga::BinaryData* bd = de0->GetBinaryDataPointer(err);
					if ( bd && err==0 ) {
						size = bd->Size();
					}
				}
				else {
					                                                                         
					size = de0->GetStringLengthUtf8Character(err);
					if(err) {
						err = 0;
						size = 0;
					}
				}
				tmp.push( de=new DataElement(DataElement::t_int, static_cast<int>(size)) );
			}
			break;
		case 60:                   
		{
			if ( argn < 2 )
				throw ExeFcnException("Set() requires at least two arguments.");

			de0 = getDataElement(1,f,ts,ind,tmp);
			string stDataElementName((const char*)de0->getName());
			DataElement* deValue = getDataElement(2,f,ts,ind,tmp);

			if ( !de0->isCollection() ) {
				                                   
				const string tmpDEName((const char*)de0->GetString(err));
				if ( err == 0 && !tmpDEName.empty() ) {
					stDataElementName = tmpDEName;
					de0=getTreeMapper()->getGlobalDataElement(stDataElementName.c_str());
					if ( !de0->isCollection() ) {
						if ( argn == 2 ) {
							                                                                     
							de0->set(deValue);
							de=de0;
							FILE*f=getDebugLog();
							if(f) {
								int tmperr=0;
								fprintf(f, "Set(%s,%s)\n", stDataElementName.c_str(), static_cast<const char*>(deValue->GetString(tmperr)));
							}
							break;
						}
						else {
							                                                     
							de0->initCollection();
						}
					}
				}
				else {
					                                     
					                                              
					if ( argn > 2 ) {
						de0->initCollection();
						err = 0;
					}
					else if ( err && de0->isGlobalDE() ) {
						                                                                           
						de0->set(deValue);
						de0->setName(stDataElementName.c_str());
						de=de0;
						err = 0;
						break;
					}
					else {
						if ( err )
							throw CrException("Error in Set() function: The name of the data element is null. Please pass the name of a data element or a global data element as the first argument");
						else
							throw CrException("Error in Set() function: The name of the data element is empty. Please pass the name of a data element or a global data element as the first argument");
					}
				}
			}

			                                                              
			if ( argn < 3 )
				throw CrException("Set() requires three or more arguments if the first argument is an array. " \
					"The third argument should be the index (or indices) of the array entry you want to set.");

			for( int i = 3; i <= argn; ++i ) {
				DataElement* const deIndex = getDataElement(i,f,ts,ind,tmp);
				const int index = deIndex->GetInteger(err);
				if(err)handle_err(deIndex,err);

				const size_t arr_size = de0->getDECollectionSize();
				if ( index < 0 || index == arr_size ) {
					                      
					if ( i < argn ) {
						                            
						DataElement* newArray = new DataElement();
						newArray->initCollection();
						de0->addToCollection(newArray);
						de0 = newArray;
					}
					else {
						                    
						DataElement* newDataElement = new DataElement();
						de0->addToCollection(newDataElement);
						de0 = newDataElement;
						break;	                    
					}
				}
				else {
					                                       
					de0 = de0->getDECollectionOf(index);
					                                                      
					if ( i < argn && !de0->isCollection() ) {
						throw ExeFcnException("Incompatible array dimension for data element '"
							+ stDataElementName + "'. The requested data element is of dimension "
							+ toString(i-1) + " but the requested array dimension was "
							+ toString(argn-1) + ".");
					}
				}
			}
			de0->set(deValue);
			de=de0;
		}
		break;
		case 61:                   
		{
			if( argn < 1 )
				throw ExeFcnException("Get() needs at least one argument.");

			de0=getDataElement(1,f,ts,ind,tmp);
			if( !de0->isCollection() ) {
				                              
				const KString& sDataElementName = de0->GetString(err);
				if(err)handle_err(de0,err);

				if ( sDataElementName.IsEmpty() )
					throw CrException("An empty string was passed to the Get method. This method requires the name of a global data element as input.");

				de0 = getTreeMapper()->getGlobalDataElement(sDataElementName);
				                                                             
				if ( !de0->isCollection() ) {
					de=de0;
					break;
				}
				                                                                      
			}

			                                                  
			                       
			assert(de0->isCollection());
			if ( argn < 2 )
				throw ExeFcnException("Get() requires two or more arguments if the first argument is an array." \
					"The second argument should be the index or indices of the desired array entry.");

			for( int i = 2; i <= argn; ++i ) {
				DataElement* const deIndex = getDataElement(i,f,ts,ind,tmp);
				const int index = deIndex->GetInteger(err);
				if(err)handle_err(deIndex,err);

				if( index > ((int)de0->getDECollectionSize()-1) ) {
					                                               
					tmp.push( de0 = new DataElement() );
					break;
				}
				else
					de0 = de0->getDECollectionOf(index);

				                                                      
				if ( i < argn && ! de0->isCollection() ) {
					const string stDataElementName(de0->getName());
					throw ExeFcnException("Incompatible array dimension for data element '"
						+ stDataElementName + "'. The requested data element is of dimension "
						+ toString(i-1) + " but the requested array dimension was "
						+ toString(argn-1) + ".");
				}
			}
			de=de0;
		}
		break;
		case 204:                                 
		case 211:                          
		case 212:                             
		case 215:                              
		case 262:                        
		case 281:                            
		case 282:                             
		case 283:                             
			{
				de1=getDataElement(1,f,ts,ind,tmp);
				const string s1((const char*)de1->GetString(err));
				de2=getDataElement(2,f,ts,ind,tmp);
				const string s2((const char*)de2->GetString(err));
				err = 0;	                                                                            

				FILE* debug_log = getDebugLog();
				switch(ifcn){
				case 204:                                 
					tmp.push(de=new DataElement(DataElement::t_int,
						CountSubString(s1.c_str(),s2.c_str())));
					break;
				case 211:                          
					tmp.push(de=new DataElement(DataElement::t_bool,
						getTransformationDataHolder()->getLdapExecutor()->LdapAdd(s1,s2,debug_log)));
					break;
				case 212:                             
					tmp.push(de=new DataElement(DataElement::t_bool,
						getTransformationDataHolder()->getLdapExecutor()->LdapRemove(s1,s2,debug_log)));
					break;
				case 215:                              
					tmp.push(de=new DataElement(DataElement::t_bool,
						getTransformationDataHolder()->getLdapExecutor()->LdapReplace(s1,s2,debug_log)));
					break;
				case 262:                        
					if ( s2.empty() )
						throw konga::ExeFcnException("Invalid argument. Delimiter is empty.");
					tmp.push(de=konga::SplitStringReturnDataElementArray(s1, s2));
					break;
				case 281:
					tmp.push(de=new DataElement(DataElement::t_string, konga::trim_chars(s1, s2).c_str()));
					break;
				case 282:
					tmp.push(de=new DataElement(DataElement::t_string, konga::ltrim_chars(s1, s2).c_str()));
					break;
				case 283:
					tmp.push(de=new DataElement(DataElement::t_string, konga::rtrim_chars(s1, s2).c_str()));
					break;
				}
			}
			break;
		case 223:                                
			{
				KString s1,s2;
				std::vector<std::string>vs4;
				de0=getDataElement(1,f,ts,ind,tmp);
				s1=de0->GetString(err);
				if(err==0){
					de0=getDataElement(2,f,ts,ind,tmp);
					s2=de0->GetString(err);
					if(err==0){
						for(int ia=2;ia<argn;ia++){
							int i0=ind.at(ia)+1;
							de0=eval(&ts[i0],ind.at(ia+1)-i0,err);
							if(!de0->isTokenDE())tmp.push(de0);
							if(err==0){
								KString s3=(const char*)(de0->GetString(err));
								if(err==0&&!s3.IsEmpty()){
									vs4.push_back((const char*)s3);
								}
							}
						}
					}
				}
				if(err)handle_err(de0,err);

				tmp.push(de=new DataElement(DataElement::t_string,CreateNode((const char*)s1,(const char*)s2,vs4).c_str())); 
				if(err){
					err_msg.Format("%s) function failed, namespace=\"%s\", node_name=\"%s\", ",f->fnc_name,(const char*)s1,(const char*)s2);
					for(int i=0,n=(int)vs4.size();i<n;i++){
						err_msg+=", ";
						err_msg+=vs4.at(i);
					}
					err_msg+="\n";
					throw konga::ExeFcnException((const char*)err_msg);
				}
			}
			break;
		case 224:                            
			{
				KString s1,s2;
				std::vector<const char*>vs3;
				de1=getDataElement(1,f,ts,ind,tmp);
				s1=de1->GetString(err);
				if(err==0){
					de2=getDataElement(2,f,ts,ind,tmp);
					s2=de2->GetString(err);
				}
				if(err)handle_err(de0,err);

				tmp.push(de=new DataElement(DataElement::t_string,Attribute((const char*)s1,(const char*)s2).c_str())); 
				if(err){
					err_msg.Format("%s) function failed, attribute_name=\"%s\", value=\"%s\"\n",f->fnc_name,(const char*)s1,(const char*)s2);
					err_msg+=inputArgumentsDescription(2,de1,de2);
					throw konga::ExeFcnException((const char*)err_msg);
				}
			}
			break;
		case 210:	                            
			{
				if ( argn <= 0 )
					throw CrException("LdapConnect requires at least one argument.");

				string host, user, pwd;
				int iSecure = 0;
				long lPort = -1;	                                    
				de0=getDataElement(1,f,ts,ind,tmp);
				const string location_guid((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);
				const string tag(location_guid.substr(0,2));
				OperationDataElementHolder* pDEHolder = getTransformationDataHolder()->getOperationDataElementHolder();
				if( argn == 1 && tag == "t."){                                                                                    
					konga::SmartDbPtr<DbConnection> pTranDbConnection( konga::GetBackEndDbConnection("TranDb") );
					const long id = getLocationId(location_guid, *pTranDbConnection);
					TargetDataLocation* pTargetLocation = LocationCreatorDb().getTargetLocation(id, *pTranDbConnection, *pDEHolder, 0, 0);
					LdapTargetLocation* pLdapTarget = dynamic_cast<LdapTargetLocation*>(pTargetLocation);
					if( !pLdapTarget ){
						throw CrException("A non-LDAP target was passed to LDAPConnect(). The specified target '" 
							+ pTargetLocation->getTargetName() + "' is not an LDAP target.");
					}
					host = pLdapTarget->getServer();
					user = pLdapTarget->getUid();
					pwd = pLdapTarget->getDecryptedPassword();
					iSecure = pLdapTarget->getSecured();
					lPort = pLdapTarget->getPort();
				} else if( argn == 1 && tag == "s." ){
					konga::SmartDbPtr<DbConnection> pTranDbConnection( konga::GetBackEndDbConnection("TranDb") );
					long id = getLocationId(location_guid, *pTranDbConnection);
					SourceDataLocation* pSourceLocation = LocationCreatorDb().getSourceLocation(id, *pTranDbConnection,
						*pDEHolder, 0, false);
					LdapSourceLocation* pLdapSource = dynamic_cast<LdapSourceLocation*>(pSourceLocation);
					if( !pLdapSource ){
						throw CrException("A non-LDAP source was passed to LDAPConnect(). The specified source '" 
							+ pSourceLocation->getSourceName() + "' is not an LDAP source.");
					}
					host = pLdapSource->getServer();
					user = pLdapSource->getUid();
					pwd = pLdapSource->getDecryptedPassword();
					iSecure = pLdapSource->getSecured();
					lPort = pLdapSource->getPort();
				} else {
					if ( argn < 3 )
						throw CrException("If the first argument is not an LDAP source/target, LdapConnect requires three arguments: hostname, user and password.");

					                                                            
					host = location_guid;

					de0=getDataElement(2,f,ts,ind,tmp);
					user=(const char*)de0->GetString(err);
					if(err)handle_err(de0,err);

					de0=getDataElement(3,f,ts,ind,tmp);
					pwd=(const char*)de0->GetString(err);
					if(err)handle_err(de0,err);

					if ( argn > 3 ) {
						de0=getDataElement(4,f,ts,ind,tmp);
						iSecure=de0->GetInteger(err);
						if(err)handle_err(de0,err);
					}
					if ( argn > 4 ) {
						de0=getDataElement(5,f,ts,ind,tmp);
						lPort=(long)de0->GetLong(err);
						if(err)handle_err(de0,err);
					}
				}

				tmp.push(de=new DataElement(DataElement::t_bool,
					getTransformationDataHolder()->getLdapExecutor()->LdapConnect(
					host, user, pwd, iSecure, lPort)));
			}
			break;	
		case 214:                              
			{
				if(argn<3){                                                           
					throw konga::ExeFcnException("LdapSearch() takes at least 3 arguments");
				}
				std::vector<std::string> vAttr;
				std::string path,filter;
				int iDetail=0;
				KString s; 
				for(int k=0,n=argn;k<n;k++){
					de0=getDataElement(k+1, f, ts, ind, tmp);
					switch(k){
					case 0:
						s=de0->GetString(err);
						de1=de0;
						if(err){
							err_msg="LdapSearch() failed to evaluate path\n";
							throw konga::ExeFcnException((const char*)err_msg);
						}
						path=(const char*)s;
						break;
					case 1:
						s=de0->GetString(err);
						de2=de0;
						if(err){
							err_msg="LdapSearch() failed to evaluate filter\n";
							throw konga::ExeFcnException((const char*)err_msg);
						}
						filter=(const char*)s;
						break;
					case 2:
						iDetail=de0->GetInteger(err);
						de3=de0;
						if(err){
							err_msg="LdapSearch() failed to evaluate detail\n";
							throw konga::ExeFcnException((const char*)err_msg);
						}
						break;
					default:
						s=de0->GetString(err);
						de4=de0;
						if(err){
							err_msg="LdapSearch() failed to evaluate attr#"+konga::toString(k-1)+"\n";
							throw konga::ExeFcnException((const char*)err_msg);
						}
						vAttr.push_back((const char*)s);
						break;
					}
				}

				const OperationDataElementHolder* const pDEHolder = getTransformationDataHolder()->getOperationDataElementHolder();
				assert(pDEHolder);
				LdapClassFilter::SearchScope scope = LdapClassFilter::SearchScope(GetSystemVariableAsLong("etlforge.scripting.ldap.scope", 
										pDEHolder, konga::LdapClassFilter::DEFAULT_LDAP_SEARCH_SCOPE));
				if( scope < 0 || scope > konga::LdapClassFilter::LDAP_SEARCH_SCOPE_SUBTREE ) {
					scope = konga::LdapClassFilter::DEFAULT_LDAP_SEARCH_SCOPE;
				}

				LdapSearchParameters params;
				params.scope = scope;
				params.includeDnInSearchResult = GetSystemVariableAsBool("etlforge.scripting.ldap.include_dn_in_results", pDEHolder, false);
				params.usePagedSearch = GetSystemVariableAsBool("etlforge.scripting.ldap.use_paged_search", pDEHolder, false);
				params.iMaxNumRecords = GetSystemVariableAsLong("etlforge.scripting.ldap.max_search_results", pDEHolder, -1);
				params.iDetail = iDetail;
				const bool bReturnNullIfNoResults = GetSystemVariableAsBool("etlforge.scripting.ldap.return_null_if_no_results", pDEHolder, false);

				try{
					if( iDetail == 2 ) {                             
						tmp.push(de=LdapTreeNodeToDataElement(getTransformationDataHolder()->getLdapExecutor()->LdapSearch(path,filter,vAttr,params),bReturnNullIfNoResults));
					} else {
						const string& searchRes = getTransformationDataHolder()->getLdapExecutor()->LdapSearchWithResultAsXml(path,filter,vAttr,params,0,bReturnNullIfNoResults);
						if( bReturnNullIfNoResults && searchRes.empty() )
							tmp.push(de=new DataElement());
						else
							tmp.push(de=new DataElement(DataElement::t_string,searchRes.c_str()));
					}
				}
				catch(const CrException& e){
					throw WarningException(e.GetErrorString());
				}
			}
			break;
		case 220:                                       
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const string node_name((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);
				DataElement*any_collection=getDataElement(2,f,ts,ind,tmp);
				if(err)handle_err(any_collection,err);
				de=SelectNodeFromXmlAny(node_name,any_collection);
				if(de==0)tmp.push(de=new DataElement());
			}
			break;
		case 221:                             
		case 222:                              
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				if(de0->isCollection()){
					tmp.push(de=new DataElement());
					de->initCollection();
					for(int i=0, n=de0->getDECollectionSize(); i<n; ++i){
						switch(ifcn){
						case 221:                            
							de->addToCollection(new DataElement(DataElement::t_string, de0->getDECollectionOf(i)->getXmlNodeName().c_str()));
							break;
						case 222:                             
							de->addToCollection(new DataElement(DataElement::t_string, de0->getDECollectionOf(i)->getXmlNodeValue().c_str()));
							break;
						}
					}
					break;
				}
				switch(ifcn){
				case 221:                            
					tmp.push(de=new DataElement(DataElement::t_string,de0->getXmlNodeName().c_str()));
					break;
				case 222:                             
					tmp.push(de=new DataElement(DataElement::t_string,de0->getXmlNodeValue().c_str()));
					break;
				}
			}
			break;
		case 225:                                   
		case 264:                             
			{
				DataElement*node=getDataElement(1,f,ts,ind,tmp);
				DataElement*deXPath=getDataElement(2,f,ts,ind,tmp);
				const string xpath((const char*)deXPath->GetString(err));
				if(err)handle_err(deXPath, err);
				std::vector<string>vNamespaces;
				for(int i=3;i<=argn;i++){
					de3=getDataElement(i,f,ts,ind,tmp);
					const string ns((const char*)de3->GetString(err));
					if(err)handle_err(de3, err);
					vNamespaces.push_back(ns);
				}
				try {
					if(ifcn==225) {
						tmp.push(de=SelectSingleNode(node,xpath,vNamespaces,err));
					}
					else {
						tmp.push(de=SelectNodes(node,xpath,vNamespaces,err));
					}
				}
				catch( const CrException& ex ) {
					throw WarningException(ex.GetErrorString());
				}
			}
			break;
		case 226:                             
			{
				de1=getDataElement(1,f,ts,ind,tmp);
				const konga::CrDateTime& dt=de1->GetDate(err);
				if(err)throw konga::ExeFcnException("failed to convert first argument to a DateTime object.");
				de2=getDataElement(2,f,ts,ind,tmp);
				const KString& sFormat=de2->GetString(err);
				if(err){
					err_msg="failed to convert 2nd argument to a DateTime format string."+inputArgumentsDescription(2, de1, de2);
					throw konga::ExeFcnException((const char*)err_msg);
				}
				tmp.push(de=new DataElement(DataElement::t_string,FormatDate(dt,(const char*)sFormat).c_str()));
			}
			break;
		case 227:                     
			{
				std::string sGuid;
				konga::GetGUIDString(sGuid);
				tmp.push(de=new DataElement(DataElement::t_string,sGuid.c_str()));
			}
			break;
		case 228:                           
		case 229:                            
		case 230:                             
		case 231:                                
			{
				de1=getDataElement(1,f,ts,ind,tmp);
				const konga::CrDateTime& t(de1->GetDate(err));
				if(err)throw konga::ExeFcnException("failed to convert first argument to a DateTime object.");
				if(!t.dateIsValid()){
					err_msg="the argument does not have a valid date."+inputArgumentsDescription(1,de1);
					throw konga::ExeFcnException((const char*)err_msg);
				}
				switch(ifcn){
					case 228:                           
						de=new DataElement(DataElement::t_int,(int)t.GetDayOfWeek());
						break;
					case 229:                            
						de=new DataElement(DataElement::t_int,(int)t.GetDay());
						break;
					case 230:                             
						de=new DataElement(DataElement::t_int,(int)t.GetMonth());
						break;
					case 231:                                
						de=new DataElement(DataElement::t_date,t.GetLastDayOfMonth());
						break;
				}
				assert(de);
				tmp.push(de);
			}
			break;
		case 232:                               
			{
				tmp.push(de=new DataElement(DataElement::t_string,SourceFileName().c_str()));
			}
			break;
		case 237:                                 
			{
				DataElement* to=getDataElement(1,f,ts,ind,tmp);
				DataElement* subject=getDataElement(2,f,ts,ind,tmp);
				DataElement* message=getDataElement(3,f,ts,ind,tmp);
				int err = 0;
				KString sTo = to->GetString(err);
				KString sSubject = subject->GetString(err);
				KString sMessage = message->GetString(err);
				try {
					konga::SendSystemEmail(getTransformationDataHolder()->getOperationDataElementHolder(),
						(const char*)sTo, (const char*)sSubject, (const char*)sMessage);
					tmp.push(de=new DataElement(DataElement::t_string,""));
				}
				catch( const CrException& ex ) {
					tmp.push(de=new DataElement(
						DataElement::t_string, ex.GetErrorString().c_str()));
				}
			}
			break;
		case 238:                         
			{
				if ( argn < 4 )
					throw konga::ExeFcnException("Method SendEmail needs at least 4 arguments.");
				else if ( argn > 10 )
					throw konga::ExeFcnException("Too many arguments supplied to SendEmail. This method accepts 1 or 4 to 10 arguments, see documentation.");

				                          
				DataElement* from=getDataElement(1,f,ts,ind,tmp);
				DataElement* to=getDataElement(2,f,ts,ind,tmp);
				DataElement* subject=getDataElement(3,f,ts,ind,tmp);
				DataElement* message=getDataElement(4,f,ts,ind,tmp);

				int err;
				string stFrom, stTo, stSubject, stMessage;
				string stSmtpServers, stAccount, stAccountPwd, stCC, stBCC, stReplyTo;
				stFrom = (const char*)from->GetString(err);
				stTo = (const char*)to->GetString(err);
				stSubject = (const char*)subject->GetString(err);
				stMessage = (const char*)message->GetString(err);

				                                           
				switch (argn) {
					                                                           
					case 10:
						stReplyTo = (const char*)getDataElement(10,f,ts,ind,tmp)->GetString(err);
					case 9:
						stBCC = (const char*)getDataElement(9,f,ts,ind,tmp)->GetString(err);
					case 8:
						stCC = (const char*)getDataElement(8,f,ts,ind,tmp)->GetString(err);
					case 7:
						stAccountPwd = (const char*)getDataElement(7,f,ts,ind,tmp)->GetString(err);
					case 6:
						stAccount = (const char*)getDataElement(6,f,ts,ind,tmp)->GetString(err);
					case 5:
						stSmtpServers = (const char*)getDataElement(5,f,ts,ind,tmp)->GetString(err);
					default:
						break;
				};

				                               
				err = 0;
				try {
					konga::SendEmail(getTransformationDataHolder()->getOperationDataElementHolder(),
						stFrom, stTo, stSubject, stMessage, stSmtpServers, stAccount,
						stAccountPwd, stCC, stBCC, stReplyTo);
					tmp.push(de=new DataElement(DataElement::t_string,""));
				}
				catch( const CrException& ex ) {
					tmp.push(de=new DataElement(
						DataElement::t_string, ex.GetErrorString().c_str()));
				}
			}
			break;
		case 265:                                  
			{
				de1 = getDataElement(1,f,ts,ind,tmp);
				const string guid( de1->GetString(err) );
				if(err)handle_err(de0, err);
				try {
					konga::SendPreDefinedEmail(guid, *getTransformationDataHolder()->getOperationDataElementHolder());
					tmp.push(de=new DataElement(DataElement::t_string,""));
				}
				catch( const CrException& ex ) {
					tmp.push(de=new DataElement(
						DataElement::t_string, ex.GetErrorString().c_str()));
				}
			}
			break;
		case 239:                               
			{	                                                        
				if ( argn < 2 )
					throw konga::ExeFcnException("Method RegExMax needs at least 2 arguments.");

				                          
				const DataElement* const input_de=getDataElement(1,f,ts,ind,tmp);
				const DataElement* const regex_de=getDataElement(2,f,ts,ind,tmp);

				int err = 0;
				const string sInput(static_cast<const char*>(input_de->GetString(err)));
				const string sRegEx(static_cast<const char*>(regex_de->GetString(err)));
				err = 0;	                     

				try {
					vector<string> matches;
					konga::RegExMatch(sInput, sRegEx, matches);
					int numMatchedTotal = (int)matches.size();
					int numMatched = 0;
					for( ; numMatched < numMatchedTotal && numMatched < (argn-2); ++numMatched ) {
						const DataElement* const matched_de_name = getDataElement(numMatched+3,f,ts,ind,tmp);
						DataElement* const matched_de = getTreeMapper()->getGlobalDataElement(matched_de_name->GetString(err));
						matched_de->setType(DataElement::t_string);
						matched_de->setTypedValue(matches.at(numMatched).c_str(), "string", 0);
					}
					tmp.push(de=new DataElement(DataElement::t_int, static_cast<int>(numMatchedTotal)));
				} catch(const CrException& e) {
					throw konga::ExeFcnException(e.GetErrorString());
				}
			}
			break;
		case 240:                          
			{
				std::auto_ptr<EnterAccumulativeScope> scope_profiling(
					getTreeMapper()->m_pProfilingKit == 0 ? 0 :
					new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "FBFunctions", "DB_LOOKUP_TOTAL")
					);
				de0=getDataElement(1,f,ts,ind,tmp);
				const string stSourceTargetGuid((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);

				de0=getDataElement(2,f,ts,ind,tmp);
				const string stSql((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);

				std::auto_ptr<EnterAccumulativeScope> scope_profiling2(
					getTreeMapper()->m_pProfilingKit == 0 ? 0 :
					new EnterAccumulativeScope(getTreeMapper()->m_pProfilingKit, "DbExecutes", truncate_sql_for_timing(t, m_pTargetNode, stSql))
					);

				konga::DbLookup* pDbLookup = 0;
				try {
					pDbLookup = &getTransformationDataHolder()->getDbLookupCache()->getDbLookup(pOperationContext->getCurrentUser(), stSourceTargetGuid);

					const bool bReturnFullRowSet = (argn == 2);
					konga::DbLookupResultState resState;

					bool bAutoCommit, bTransaction;
					GetTransactionFlags(getTransformationDataHolder()->getOperationDataElementHolder(), pDbLookup, bAutoCommit, bTransaction);

					if( bReturnFullRowSet ) {
						const int default_max_rows = 10000;
						int lMaxRows = (int)GetSystemVariableAsLong("etlforge.scripting.db.max_rows", getTransformationDataHolder()->getOperationDataElementHolder(), default_max_rows);
						if( lMaxRows <= 0 )
							lMaxRows = default_max_rows;

						DbRowSetData rows;
						resState = pDbLookup->Lookup(stSql, rows, lMaxRows);
						CommitIfNeeded(bAutoCommit, bTransaction, *pDbLookup);

						                                                                 
						const long iRowCount = pDbLookup->GetRowCount();
						DataElement* pTmpDe = getTransformationDataHolder()->getOperationDataElementHolder()->getOperationDataElement("etlforge.script.db.row_count");
						pTmpDe->setType(DataElement::t_string);
						pTmpDe->setValue(toString(iRowCount).c_str());
						  
						                   
						SetRowCountDE(getTransformationDataHolder()->getOperationDataElementHolder(), iRowCount);
						  

						tmp.push(de=RowsetToDataElement(rows));
					} else {
						DbRowSetData rows;
						resState = pDbLookup->Lookup(stSql, rows, 1);
						CommitIfNeeded(bAutoCommit, bTransaction, *pDbLookup);

						if(resState==konga::DBLRS_LOOKUP_SUCCESS) {
							vector<DbData*> first_row;
							if ( !rows.rowset.empty() ) {
								if ( rows.rowset.at(0) ) {
									first_row = rows.rowset.at(0)->row;
								}
							}

							SetRowCountDE(getTransformationDataHolder()->getOperationDataElementHolder(), pDbLookup->GetRowCount());
							                                                                       
							if(err==0){
								for(int k=2; k<argn && (k-2) < (int)first_row.size();k++){
									de0=getDataElement(k+1,f,ts,ind,tmp);
									DataElement* globalDe = 0;
									if(de0->isGlobalDE()) {
										                                                         
										globalDe=de0;
									}
									else {
										                                                           
										const KString& globalDeName = de0->GetString(err);
										if ( err == 0 && !globalDeName.IsEmpty() )
											globalDe = getTreeMapper()->getGlobalDataElement((const char*)globalDeName);
									}
									if( globalDe ) {
										const konga::DbData* const atom = first_row.at(k-2);
										if ( atom == 0 || atom->isNull ) {
											globalDe->setNull();
										}
										else {
											globalDe->setType(DataElement::t_string);
											globalDe->setValue(atom->data.c_str());
										}
									}
								}
							}

							                                                                                                                 
							                                                                      
							if ( rows.rowset.empty() ) {
								tmp.push(de=new DataElement(DataElement::t_int, 0));
							}
							else {
								tmp.push(de=new DataElement(DataElement::t_int, 1));
							}
						}
						else if(resState==konga::DBLRS_NULL_RESULT) {
							SetRowCountDE(getTransformationDataHolder()->getOperationDataElementHolder(), pDbLookup->GetRowCount());
							tmp.push(de=new DataElement(DataElement::t_int, 0));	                                                                    
						}
					}

					if( resState!=konga::DBLRS_LOOKUP_SUCCESS && resState!=konga::DBLRS_NULL_RESULT ) {
						err = resState;
						if(err)err_msg="DBExecute failed to execute SQL statement (2) \""
							+ stSql + "\".";
						tmp.push(de=new DataElement(DataElement::t_int, 0));
					}
				} catch( const konga::CrException& e ) {
					                                            
					if ( pDbLookup )
						pDbLookup->Close();
					throw WarningException("DBExecute failed to execute SQL statement \""
							+ stSql + "\".\n" + e.GetErrorString());
				}
			}
			break;
		case 402:                                   
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const string stSourceTargetGuid((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);

				try {
					DbLookupCache* pDbLookupCache = getTransformationDataHolder()->getDbLookupCache();
					DbLookup& dbLookup = pDbLookupCache->getDbLookup(pOperationContext->getCurrentUser(), stSourceTargetGuid);
					pDbLookupCache->closeOne(dbLookup);
					tmp.push(de=new DataElement());
				}
				catch(const CrException& e ) {
					throw WarningException("DbCloseConnection failed to close the connection:\n" + e.GetErrorString());
				}
			}
			break;
		case 403:                                       
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const string stSourceTargetGuid((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);

				try {
					DbLookupCache* pDbLookupCache = getTransformationDataHolder()->getDbLookupCache();
					DbLookup& dbLookup = pDbLookupCache->getDbLookup(pOperationContext->getCurrentUser(), stSourceTargetGuid);
					pDbLookupCache->rollbackOne(dbLookup);
					tmp.push(de=new DataElement());
				}
				catch(const CrException& e ) {
					throw WarningException("DbRollbackTransaction failed to roll back the transaction:\n" + e.GetErrorString());
				}
			}
			break;
		case 241:                                  
			{
				if ( argn < 3 )
					throw konga::ExeFcnException("Method RegExReplace needs at least 3 arguments.");

				                          
				const DataElement* const input_de=getDataElement(1,f,ts,ind,tmp);
				const DataElement* const regex_de=getDataElement(2,f,ts,ind,tmp);
				const DataElement* const format_de=getDataElement(3,f,ts,ind,tmp);

				int err = 0;
				const string sInput(static_cast<const char*>(input_de->GetString(err)));
				const string sRegEx(static_cast<const char*>(regex_de->GetString(err)));
				const string sFormat(static_cast<const char*>(format_de->GetString(err)));
				err = 0;	                      

				try {
					string sRes;
					konga::RegExReplace(sInput, sRegEx, sFormat, sRes);
					tmp.push(de=new DataElement(DataElement::t_string, sRes.c_str()));
				} catch(const CrException& e) {
					throw konga::ExeFcnException(e.GetErrorString());
				}	
			}
			break;
			case 242:                                    
				try {
					                                                                           
					                                                          
					de = CallStoredProcedure(argn,f,ts,ind,tmp,getTransformationDataHolder()->getOperationDataElementHolder());
				}
				catch( const CrException& e ) {
					                                
					throw WarningException(e.GetErrorString());
				}
				break;
			case 243:                                 
			{				
				de0=getDataElement(1,f,ts,ind,tmp);
				KString s1=de0->GetString(err);
				if(err)handle_err(de0,err);

				tmp.push(de=new DataElement(DataElement::t_bool,
					getTransformationDataHolder()->getLdapExecutor()->LdapDeleteEntry((const char*)s1)));

				if(err){
					err_msg.Format("%s function failed, DN=\"%s\"\n",f->fnc_name,(const char*)s1);
					throw konga::ExeFcnException((const char*)err_msg);
				}
			}
			break;
			case 244:                           
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const std::string sql( (const char*)de0->GetString(err) );
				if(err)handle_err(de0,err);

				tmp.push( de=new DataElement(DataElement::t_string, konga::SqlEscape(sql).c_str()) );
			}
			break;
			case 245:                                    
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const KString& msg = de0->GetString(err);
				getTransformationDataHolder()->getViewLog()->OpLog( (const char*)msg );
				tmp.push( de=new DataElement(DataElement::t_string, (const char*)msg) );
			}
			break;
			case 249:                         
			case 269:                                  
				try {
					if ( argn < 1 )
						throw CrException("ReadFile() is missing required first argument: source tag.");
					else if ( argn > 2 )
						throw CrException("Too many arguments to function ReadFile(). "
						+ toString(argn) + " were supplied, max is 2.");

					de0=getDataElement(1,f,ts,ind,tmp);
					const std::string source_id((const char*)de0->GetString(err));
					if (err) handle_err(de0, err);

					string sOverrideFileName;
					if ( argn > 1 ) {
						de0=getDataElement(2,f,ts,ind,tmp);
						sOverrideFileName = (const char*)de0->GetString(err);
						if (err) handle_err(de0, err);
					}

					std::string stContents;
					if ( ifcn == 249 ) {
						konga::ReadFromSource(source_id, stContents, getTransformationDataHolder()->getOperationDataElementHolder(),
							sOverrideFileName, pOperationContext->getCurrentUser());
					}
					else {
						konga::ReadFromSourceAndBase64Encode(source_id, stContents, getTransformationDataHolder()->getOperationDataElementHolder(),
							sOverrideFileName, pOperationContext->getCurrentUser());
					}
					tmp.push( de=new DataElement(DataElement::t_string, stContents.c_str()) );				
				}
				catch( const CrException& e ) {
					                                                      
					throw WarningException(e.GetErrorString());
				}
			break;
			case 250:                          
			{
				try {
					                                               
					                                  
					                                            
					if ( argn < 1 )
						throw CrException("WriteFile() is missing required first argument: target tag.");
					else if ( argn > 3 )
						throw CrException("Too many arguments to function WriteFile. "
						+ toString(argn) + " arguments were supplied, max is 3.");

					de0 = getDataElement(1,f,ts,ind,tmp);
					const std::string sTargetId((const char*)de0->GetString(err));
					if (err) handle_err(de0, err);

					if ( argn < 2 )
						throw CrException("WriteFile() is missing required second argument: contents.");
					konga::DataElement* const contents = getDataElement(2,f,ts,ind,tmp);
					err = contents->getError();
					if (err) handle_err(de0, err);

					string sOverrideFileName;
					if ( argn > 2 ) {
						de0 = getDataElement(3,f,ts,ind,tmp);
						sOverrideFileName = (const char*)de0->GetString(err);
						if (err) handle_err(de0, err);
					}

					getTransformationDataHolder()->getFileTypeTargetCache()->WriteToBuffer(pOperationContext->getCurrentUser(),
						sTargetId, sOverrideFileName,
						getTransformationDataHolder()->getOperationDataElementHolder(), *contents);
					tmp.push( de=new DataElement() );
				}
				catch( const CrException& e ) {
					                                                      
					throw WarningException(e.GetErrorString());
				}
			}
			break;
			case 254:	                           
			{
				                                               
				                                            

				try {
					if ( argn < 1 )
						throw CrException("FlushFile() is missing required first argument: target tag.");
					else if ( argn > 2 )
						throw CrException("Too many arguments passed to method FlushFile(). "
						+ toString(argn) + " were supplied, max is 2");

					de0=getDataElement(1,f,ts,ind,tmp);
					const string sTargetId((const char*)de0->GetString(err));
					if (err) handle_err(de0, err);

					string sOverrideFileName;
					if ( argn > 1 ) {
						de0 = getDataElement(2,f,ts,ind,tmp);
						sOverrideFileName = (const char*)de0->GetString(err);
						if (err) handle_err(de0, err);
					}

					getTransformationDataHolder()->getFileTypeTargetCache()->FlushBuffer(pOperationContext->getCurrentUser(), sTargetId, sOverrideFileName);
					tmp.push( de=new DataElement() );
				}
				catch( const CrException& ex ) {
					                                                      
					throw WarningException(ex.GetErrorString());
				}
			}
			break;
			case 258:	                              
				                                          
				if ( argn > 1 ) {
					throw WarningException("Too many arguments passed to function FlushAllFiles. "
					+ toString(argn) + " were supplied, max is 1.");
				}

				try {
					if ( argn == 0 ) {
						                   
						getTransformationDataHolder()->getFileTypeTargetCache()->FlushAll(pOperationContext->getCurrentUser());
					}
					else {
						                           
						de0=getDataElement(1,f,ts,ind,tmp);
						const string sTargetId((const char*)de0->GetString(err));
						if (err) handle_err(de0, err);

						getTransformationDataHolder()->getFileTypeTargetCache()->FlushAll(pOperationContext->getCurrentUser(), sTargetId);
					}
					tmp.push( de=new DataElement() );
				}
				catch( const CrException& ex ) {
					throw WarningException(ex.GetErrorString());
				}
			break;
			case 251:	                            
				tmp.push( de=new DataElement(DataElement::t_string, (const char*)getTreeMapper()->m_lastErr) );
			break;
			case 252:	                             
			case 253:	                              
			{
				string stNewError;
				if ( ifcn == 252 ) {
					de0=getDataElement(1,f,ts,ind,tmp);
					stNewError = (const char*)de0->GetString(err);
					if (err) handle_err(de0, err);
				}
				getTreeMapper()->m_lastErr = stNewError;
                                                                  
				tmp.push( de=new DataElement() );
			}
			break;
			case 255:	                       
			{
				const int DEFAULT_MAX_IT = 50000;                                 
				const string max_iterations_de("etlforge.scripting.while.max_iterations");
				int lMaxIterations = GetSystemVariableAsLong(max_iterations_de, getTransformationDataHolder()->getOperationDataElementHolder(), DEFAULT_MAX_IT);
				if( lMaxIterations <= 0 ) lMaxIterations = DEFAULT_MAX_IT;

				for(int it=0; true; ++it) {
					if( it>=lMaxIterations ) {
						throw konga::EvalException("While-loop reached maximum number of iterations (" +
							konga::toString(it) + "). You can control the number of iterations allowed by setting the " +
							"$" + max_iterations_de + " variable.");
					}
					konga::AutoDeleteStack<konga::DataElement> privateGarbage;
					de0=getDataElement(1,f,ts,ind,privateGarbage);
					const bool b=de0->GetBool(err);
					if (err) handle_err(de0, err);
					if(b)
						de=getDataElement(2,f,ts,ind,privateGarbage);
					else
						break;
				}
				tmp.push( de=new DataElement() );
			}
			break;
			case 256:	                                      
			{
				if ( argn < 2 )
					throw CrException("SelectNodesFromXmlAny requires at least two arguments: XPath query and a path to an Any XML node.");
				de0=getDataElement(1,f,ts,ind,tmp);
				const string xpath((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);

				const DataElement* const any_collection=getDataElement(2,f,ts,ind,tmp);

				std::vector<string> vNamespaces;
				for(int i=3;i<=argn;i++){
					DataElement*de3=getDataElement(i,f,ts,ind,tmp);
					const string ns((const char*)de3->GetString(err));
					if(err)handle_err(de3, err);
					vNamespaces.push_back(ns);
				}

				tmp.push( de=SelectNodesFromXmlAny(xpath, any_collection, vNamespaces) );
			}
			break;
			case 257:                               
			{				
				if(argn < 2)
					throw konga::ExeFcnException("LdapRename() takes at least 2 arguments");

				de0=getDataElement(1,f,ts,ind,tmp);
				KString sDn=de0->GetString(err);
				if(err)handle_err(de0,err);

				de0=getDataElement(2,f,ts,ind,tmp);
				KString sNewRdn=de0->GetString(err);
				if(err)handle_err(de0,err);

				KString sNewParent;
				if(argn > 2) {
					de0=getDataElement(3,f,ts,ind,tmp);
					sNewParent=de0->GetString(err);
					if(err)handle_err(de0,err);
				}

				bool bDeleteOldRdn = false;
				if(argn > 3) {
					de0=getDataElement(4,f,ts,ind,tmp);
					bDeleteOldRdn=de0->GetBool(err);
					if(err)handle_err(de0,err);
				}

				tmp.push(de=new DataElement(DataElement::t_bool,
					getTransformationDataHolder()->getLdapExecutor()->LdapRename((const char*)sDn,(const char*)sNewRdn,(const char*)sNewParent,bDeleteOldRdn)));

				if(err){
					err_msg.Format("%s function failed, DN=\"%s\", DN=\"%s\", DN=\"%s\", DN=\"%s\", DN=\"%s\"\n",f->fnc_name, 
						(const char*)sDn, (const char*)sNewRdn, (const char*)sNewParent, bDeleteOldRdn?"true":"false");
					throw konga::ExeFcnException((const char*)err_msg);
				}
			}
			break;
			case 259:                              
			{				
				if(argn < 1)
					throw WarningException("RunOperation() takes at least one argument.");

				de0=getDataElement(1,f,ts,ind,tmp);
				const string op_tag( (const char*)de0->GetString(err) );
				if(err)handle_err(de0,err);
				const int syncArgPos = 2;
				bool isSuccess = processRunOperationCall(argn, pOperationContext, op_tag, syncArgPos, f, ts, ind, tmp, err);
				tmp.push( de=new DataElement(DataElement::t_bool, isSuccess) );
			}
			break;
			case 260:                      
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				tmp.push(de=SumCsv(de0,err,err_msg));
			}
			break;
			            case 465:
            {
                if(argn < 2) throw konga::ExeFcnException("RunXSLT requires a stylesheet and at least one XML document.");
                de0=getDataElement(1,f,ts,ind,tmp);
                string stylesheet((const char*)de0->GetString(err));
                if(err)handle_err(de0,err);
                try {
                    if(stylesheet.compare(0,3,"xs.")==0){ etlforge::XsltAssetStore assets; stylesheet=assets.loadStylesheet(stylesheet.substr(3)); }
                    else if(stylesheet.compare(0,3,"sc.")==0){
                        string name;long id=0;string guid=stylesheet.substr(3);
                        getTransformationDataHolder()->getScriptCache()->getScript(guid,name,stylesheet,id);
                    }
                    vector<string> inputs;
                    for(int i=2;i<=argn;++i){
                        de0=getDataElement(i,f,ts,ind,tmp);
                        inputs.push_back((const char*)de0->GetString(err));
                        if(err)handle_err(de0,err);
                    }
                    const string java=GetOptionAsString("XSLT","JavaPath","C:/ETL/Tools/jdk8u504-b01/bin/java.exe");
                    const string cp=GetOptionAsString("XSLT","ClassPath","C:/ETL/Server/dist/lib/xslt/*");
                    const string worker=GetOptionAsString("XSLT","NativeWorker","C:/ETL/Server/runtime/xslt-native/ETLForgeXsltWorker.exe");
                    const string backend=GetOptionAsString("XSLT","Backend","Native");
                    if(backend!="Native" && backend!="Java")throw konga::ExeFcnException("Unknown XSLT backend: "+backend);
                    const string output=etlforge::executeXslt(java,cp,stylesheet,inputs,worker,backend=="Java");
                    std::auto_ptr<DataElement> result(new DataElement());result->initCollection();
                    result->addToCollection(new DataElement(DataElement::t_string,output.c_str()));
                    tmp.push(de=result.release());
                }catch(const std::exception& failure){throw konga::ExeFcnException(failure.what());}
            }
            break;
            case 466:
            {
                for(int index=1;index<=argn;++index){
                    const int begin=ind.at(index-1)+1;
                    if(ind.at(index)-begin!=1 || ts[begin]->getType()!=Token::LocalDE)
                        throw ExeFcnException("ArgumentList arguments must be local variable names.");
                    DataElement* target=getLocalVariable(ts[begin]->GetName());
                    DataElement* argument=getLocalVariable("_"+konga::toString(index));
                    const string name=target->getName();
                    target->set(argument);
                    target->setName(name.c_str());
                    local_variable_map->markAssigned(target);
                }
                tmp.push(de=new DataElement());
            }
            break;
            case 261:                           
			{				
				if(argn < 1)
					throw konga::ExeFcnException("RunScript() takes at least 1 arguments.");

				de0=getDataElement(1,f,ts,ind,tmp);
				const string script_tag( (const char*)de0->GetString(err) );
				if(err)handle_err(de0,err);

				if( script_tag.length() < 4 || !EqualsIgnoreCase(script_tag.substr(0, 3), "sc.") )
					throw konga::ExeFcnException("Invalid Script GUID (" + script_tag + ") passed to RunScript(). Expected: 'sc.guid'");

				const string& stScriptGuid = script_tag.substr(3);                 

				std::vector<konga::DataElement*>new_arguments;
                AutoPtrDeleteVector<konga::DataElement> argumentCleanup(new_arguments);
				int ARGI=0;
				for(int argi=2; argi<=argn; ++argi){
					de0=getDataElement(argi,f,ts,ind,tmp);
					de1=new DataElement(*de0);
					std::string deName="_"+konga::toString(++ARGI);
					de1->setName(deName.c_str());
					new_arguments.push_back(de1);
				}
				LocalScope scope(*this);
                struct ScriptErrorScope {
                    KString& target;
                    KString saved;
                    explicit ScriptErrorScope(KString& value):target(value),saved(value){target.Empty();}
                    ~ScriptErrorScope(){target=saved;}
                } errorScope(m_lastErr);
				local_variable_map->loadArguments(new_arguments);
                new_arguments.clear();
				de = konga::RunScript(stScriptGuid, *getTransformationDataHolder()->getScriptCache(), m_pTreeMapper, this);
				tmp.push(de);
			}
			break;
			case 315:                           
			{				
				if(argn != 1) throw konga::ExeFcnException("RunPlugin takes 1 argument.");

				de0=getDataElement(1,f,ts,ind,tmp);
				const string plugin_tag( (const char*)de0->GetString(err) );
				if(err)handle_err(de0,err);

				string stNewError;
				konga::RunPlugin(plugin_tag, *getTransformationDataHolder(), stNewError);
				if ( !stNewError.empty() ) {
					TreeMapper* pTreeMapper = getTreeMapper();
					pTreeMapper->getViewLog()->LogMessage("Run Plugin failed: %s", stNewError.c_str());
					getTreeMapper()->m_lastErr = stNewError;
				}

				de = new DataElement(DataElement::t_bool);
				de->setTypedValue(stNewError.empty());
				tmp.push( de );
			}
			break;
			case 316:                                
				if(argn!=1) throw konga::ExeFcnException("GetInputString takes 1 argument.");
				de0=getDataElement(1,f,ts,ind,tmp);
				tmp.push( de = new DataElement(DataElement::t_string, (const char*)de0->input()) );
				break;
			case 263:                             
			{				
				if(argn != 1)
					throw konga::ExeFcnException("GetHostByIp() takes 1 argument.");

				de0=getDataElement(1,f,ts,ind,tmp);
				const string ip_addr( (const char*)de0->GetString(err) );
				if(err)handle_err(de0,err);

				const string ip_name( konga::GetHostByIp(ip_addr) );
				tmp.push( de=new DataElement(DataElement::t_string, ip_name.c_str()) );
			}
			break;
			case 286:                              
				tmp.push(de=new DataElement(DataElement::t_string, konga::getMachineName().c_str()));
				break;
			case 266:                                       
			{
				if ( argn < 1 )
					throw konga::ExeFcnException("ArrayToMultipleValues requires one array argument argument.");
				de0=getDataElement(1,f,ts,ind,tmp);
				if ( !de0->isCollection() )
					throw konga::ExeFcnException("ArrayToMultipleValues requires an array argument.");

				const string& xml = konga::ArrayToMultipleValuesXML(*de0);
				tmp.push(de=new DataElement(DataElement::t_string, xml.c_str()));
			}
			break;
			case 267:                            
			{
				                                                                              
				if ( m_pDebuggee != 0 && m_pDebuggee->isDebugging() && m_pDebuggee->isObservingBreakpoints() ) {
					bool shouldBreak = true;
					if(argn > 0) {
						de0=getDataElement(1,f,ts,ind,tmp);
						shouldBreak = de0->GetBool(err);
						if(err)handle_err(de0,err);
					}

					if( shouldBreak ) {
						IntegrationDebugBreakpointHitHelper(t);
					}
					tmp.push( de = new DataElement(DataElement::t_bool, shouldBreak) );
				}
				else {
					tmp.push( de = new DataElement(DataElement::t_bool, false) );
				}
			}
			break;
			case 268:                                  
				{
					if (argn==0) {
						throw CrException("ReadArrayString was called without arguments. The first string argument is required.");
					}
					de0=getDataElement(1,f,ts,ind,tmp);
					std::string s1=(const char*)de0->GetString(err);
					if(err)handle_err(de0,err);
					DataElement::Type type=DataElement::t_string;
					if(argn>1){
						de0=getDataElement(2,f,ts,ind,tmp);
						std::string s2=(const char*)de0->GetString(err);
						if(err)handle_err(de0,err);
						type=DataElement::get_type(s2.c_str(),0);
					}
					tmp.push(de=new DataElement());
					err_msg=readArrayString(de, s1, type);
				}
				break;
			case 270:                           
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const std::string source_id((const char*)de0->GetString(err));
				if (err) handle_err(de0, err);

				std::vector<std::string> list;
				konga::GetFileNameListFromSource(source_id, getTransformationDataHolder()->getOperationDataElementHolder(),
						pOperationContext->getCurrentUser(), list);
				tmp.push(de=new DataElement());
				de->initCollection();
				for(std::vector<std::string>::const_iterator it=list.begin(); it!=list.end(); ++it){
					de->addToCollection(new DataElement(DataElement::t_string, it->c_str()));
				}
			}
			break;
			case 271:                       
			{
				de0=getDataElement(1,f,ts,ind,tmp);
				const int iSecond=de0->GetInteger(err);
				if(err)handle_err(de0,err);
				if ( iSecond > 0 )
					konga::MilliSleep(iSecond*1000);
				tmp.push( de=new DataElement() );
			}
			break;
			case 272:                                
				{
					de0=getDataElement(1,f,ts,ind,tmp);
					const std::string diff_id((const char*)de0->GetString(err));
					if(err)handle_err(de0, err);
					try {
						diff::DiffSnapShot diff(getTreeMapper());
						diff.Initialize(diff_id);
					}
					catch( const CrException& ex ) {
						throw WarningException(ex.GetErrorString());
					}

					tmp.push( de=new DataElement() );
				}
				break;
			case 273:                                   
				{
					de0=getDataElement(1,f,ts,ind,tmp);
					int k=de0->GetInteger(err);
					if(err)handle_err(de0, err);
					de=getTreeMapper()->getGlobalDataElement(konga::diff::CHUNK_SIZE_DENAME.c_str());
					de->setType(konga::DataElement::t_int);
					de->setTypedValue((double)k);
					tmp.push( de=new DataElement() );
				}
				break;
			case 274:                                                                                
				{
					de=getTreeMapper()->getGlobalDataElement(konga::diff::PK_LIST_DENAME.c_str());
					de->initCollection();
					for(int k=1;k<=argn;k++){
						de0=getDataElement(k,f,ts,ind,tmp);
						KString s(de0->GetString(err));
						if(err)handle_err(de0, err);
						de->addToCollection(new DataElement(konga::DataElement::t_string, s));
					}
					tmp.push( de=new DataElement() );
				}
				break;
			case 275:                                    
				                                                           
				                                                                                               
				                                                                     
				{
					de=getTreeMapper()->getGlobalDataElement(konga::diff::PK_LIST_DENAME.c_str());
					de0=getTreeMapper()->getGlobalDataElement(konga::diff::ORDER_LIST_DENAME.c_str());
					de->initCollection();
					de0->initCollection();
					for(int k=1;k<=argn;k+=2){
						konga::DataElement*pk_name=getDataElement(k,f,ts,ind,tmp);
						KString s(pk_name->GetString(err));
						if(err)handle_err(de0, err);
						de->addToCollection(new DataElement(konga::DataElement::t_string, s));
						if(k<argn) {
							konga::DataElement*pk_order=getDataElement(k+1,f,ts,ind,tmp);
							const bool flag=pk_order->GetBool(err);
							if(err)handle_err(de0, err);
							de0->addToCollection(new DataElement(konga::DataElement::t_bool, flag));
						}
						else {
							de0->addToCollection(new DataElement(konga::DataElement::t_bool, true));
						}
					}
					de0=getTreeMapper()->getGlobalDataElement(konga::diff::ORDERED_DENAME.c_str());
					de0->setType(DataElement::t_bool);
					de0->setTypedValue(true);
					tmp.push( de=new DataElement() );
				}
				break;
			case 276:                         
			case 277:                            
			case 278:                            
				de=getTreeMapper()->getGlobalDataElement(konga::diff::STATUS_DENAME.c_str());
				de->setType(konga::DataElement::t_int);
				if(ifcn==276) de->setTypedValue((double)konga::diff::DiffStatus_Add);
				else if(ifcn==277)de->setTypedValue((double)konga::diff::DiffStatus_Delete);
				else de->setTypedValue((double)konga::diff::DiffStatus_Update);
				break;
			case 279:                              
				{
					konga::diff::DiffSnapShot diff(getTreeMapper());
					diff.DiffComplete();
					tmp.push( de=new DataElement() );
				}
				break;
			case 280:                           
				{
					de0=getDataElement(1,f,ts,ind,tmp);
					const string diff_id((const char*)de0->GetString(err));
					if(err)handle_err(de0, err);
					de0=getDataElement(2,f,ts,ind,tmp);
					const int iAction = de0->GetInteger(err);
					if(err)handle_err(de0, err);

					diff::DiffResetAction action;
					switch( iAction ) {
					case 0:
						action = diff::DiffResetAction_Reset;
						break;
					case 1:
						action = diff::DiffResetAction_Purge;
						break;
					default:
						throw CrException("Invalid argument passed to ResetDiff. Invalid action " + konga::toString(iAction)
							+ ". Valid options are 0 (reset) and 1 (purge) see ResetDiff documentation.");
					};

					konga::diff::DiffSnapShot diff(getTreeMapper());
					diff.Reset(diff_id, action);
					tmp.push( de=new DataElement() );
				}
				break;
			case 285:                           
				de0=getDataElement(1,f,ts,ind,tmp);
				if(err)handle_err(de0, err);
				tmp.push(de=new DataElement(DataElement::t_bool, konga::RepresentsAnInteger(*de0)));
				break;
			case 287:                                
				{
					de0=getDataElement(1,f,ts,ind,tmp);
					KString s(de0->GetString(err));
					if(err)handle_err(de0, err);
					tmp.push(de=new DataElement(DataElement::t_bool, isValidString((const char*)s)));
				}
				break;
			case 300:                                   
				tmp.push(de=new DataElement(DataElement::t_int, m_pTrans_targetLoopNode->m_sourceInstanceCount));
				break;
			case 301:                                   
				tmp.push(de=new DataElement(DataElement::t_int, m_pTrans_targetLoopNode->m_AutoNumber));
				break;
			case 302:                                
				if(m_pTargetNode==0){
					throw konga::ExeFcnException("CreateInstance() failed, the current expression is not associated with a target node.");
				}
				if(m_pTargetNode->IsXml()){
					getTreeMapper()->createXmlInstance(m_pTargetNode);
				}
				else {
					getTreeMapper()->createHfInstance(m_pTargetNode);
				}
				tmp.push(de=new DataElement());
				break;
			case 303:                            
				if(m_pTargetNode!=0){
					m_pTargetNode->setDbMode(CROM::DB_Insert);
				}
				tmp.push(de=new DataElement());
				break;
			case 304:                            
				if(m_pTargetNode!=0){
					m_pTargetNode->setDbMode(CROM::DB_Update);
				}
				tmp.push(de=new DataElement());
				break;
			case 305:                      
				if(m_pTargetNode!=0){
					m_pTargetAttribute->setUnmapped();
				}
				tmp.push(de=new DataElement());
				break;
			case 306:                              
				{
				de0=getDataElement(1,f,ts,ind,tmp);
				if(de0->getType()!=konga::DataElement::t_binary){
					throw konga::ExeFcnException("BinaryToUuid() failed, the argument must be binary data.");
				}
				const string& uuid=binary2uuid(de0->GetBinaryDataPointer(err));
				if(err)handle_err(de0,err);
				tmp.push(de=new DataElement(konga::DataElement::t_string, uuid.c_str()));
				}
				break;
			case 307:                              
				{
				de0=getDataElement(1,f,ts,ind,tmp);
				const string uuid(de0->GetString(err));
				if(err)handle_err(de0,err);
				tmp.push(de=new DataElement(uuid2binary(uuid.c_str())));
				}
				break;
			case 308:                             
				{
				de0=getDataElement(1,f,ts,ind,tmp);
				if(de0->getType()!=konga::DataElement::t_binary){
					throw konga::ExeFcnException("BinaryToHexString() failed, the argument must be binary data.");
				}
				konga::BinaryData* bd = de0->GetBinaryDataPointer(err);
				if(err)handle_err(de0,err);
				assert(bd);
				const string& hex = konga::getHexString((const char*)bd->GetPointerToData(), bd->Size());
				tmp.push( de=new DataElement(konga::DataElement::t_string, hex.c_str()) );
				}
				break;
			case 309:                             
				{
				de0=getDataElement(1,f,ts,ind,tmp);
				string hex(de0->GetString(err));
				konga::ToLower(hex);
				if(err)handle_err(de0,err);
				size_t len = 0;
				unsigned char* pd = konga::fromHexString(hex, len);
				tmp.push( de=new DataElement(new BinaryData((const char*)pd, len, false)) );
				}
				break;
			case 310:                                
			{				
				if(argn > 1)
					throw WarningException("ReRunOperation() takes at most one argument.");
				const string opGuid(getTreeMapper()->getOperationContext()->getOperationGuid());
				if (opGuid.empty()) {
					throw konga::ExeFcnException("ReRunOperation must be called from inside a running operation. It cannot be tested in a stand-alone script.");
				}
				const string op_tag("op."+opGuid);	
				const int syncArgPos = 1;
				bool isSuccess = processRunOperationCall(argn, pOperationContext, op_tag, syncArgPos, f, ts, ind, tmp, err);
				tmp.push( de=new DataElement(DataElement::t_bool, isSuccess) );
			}
			break;
			case 318:                            
			{
				if(argn==0) throw WarningException("UrlEncode can't be called without arguments.");
				if(argn>2) throw WarningException("UrlEncode was called with too many arguments. Two is maximum, " + konga::toString(argn) + " was passed.");

				de0 = getDataElement(1,f,ts,ind,tmp);
				string str(de0->GetString(err));
				err=0;
				int option = 0;
				if ( argn == 2 ) {
					de0 = getDataElement(2,f,ts,ind,tmp);
					option = de0->GetInteger(err);
					if(err)handle_err(de0,err);
				}

				bool bEncodeUnsafe = true, bEncodeReserved = true;
				switch(option) {
					case 0:
						break;
					case 1:
						bEncodeUnsafe = false;
						break;
					case 2:
						bEncodeReserved = false;
						break;
					case 3:
						bEncodeUnsafe = false;
						bEncodeReserved = false;
						break;
					default:
						throw WarningException("Invalid option passed to UrlEncode: " + konga::toString(option) + ". Valid values are 0, 1, 2 or 3, see function documentation.");
				};

				konga::UrlEncode(str, bEncodeReserved, bEncodeUnsafe);
				tmp.push(de=new DataElement(DataElement::t_string, str.c_str()));
			}
			break;
			case 319:                                  
			{
				if ( argn > 1 ) throw WarningException("GetOperationQueue takes zero or one argument only.");
				string op_tag;
				if ( argn == 1 ) {
					de0 = getDataElement(1,f,ts,ind,tmp);
					op_tag = (const char*)de0->GetString(err);
					if(err)handle_err(de0,err);
				}

				try {
					SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
					long lOperationId = -1;
					if ( !op_tag.empty() ) {
						lOperationId = konga::GetOperationIdFromTag(op_tag, *pTranDb);
					}
					vector<OperationQueueEntry> queue = konga::GetOperationQueue(getTransformationDataHolder()->getOperationContext()->getCurrentUser(), lOperationId, 0, pTranDb);

					DbGuidToIdMapper guidMapper(pTranDb);
					std::auto_ptr<DataElement> deQueue( new DataElement(DataElement::t_collection) );
					for ( vector<OperationQueueEntry>::const_iterator cit = queue.begin(); cit != queue.end(); ++cit ) {
						const OperationQueueEntry& entry = *cit;
						std::auto_ptr<DataElement> deEntry( new DataElement(DataElement::t_collection) );
						lOperationId = entry.getOperationId();
						const string& stOperationGUID = guidMapper.GetGuid(lOperationId, OperationEntityId);
						deEntry->addToCollection( new DataElement(DataElement::t_string, stOperationGUID.c_str()) );
						deEntry->addToCollection( new DataElement(DataElement::t_bool, entry.isExecuting() ? 1 : 0) );
						deEntry->addToCollection( new DataElement(DataElement::t_date, entry.receivedAt()) );

						deQueue->addToCollection( deEntry.release() );
					}
					tmp.push( de=deQueue.release() );
				}
				catch( const CrException& ex ) {
					throw WarningException(ex.GetErrorString());
				}
			}
			break;
			case 320:                                   
			{
				if ( argn < 1 ) throw WarningException("WaitForOperation needs at least one argument.");
				if ( argn > 3 ) throw WarningException("Too many arguments passed to WaitForOperation. Expected 1, 2 or 3 arguments.");

				de0 = getDataElement(1,f,ts,ind,tmp);
				const string op_tag((const char*)de0->GetString(err));
				if(err)handle_err(de0,err);
				if( op_tag.empty() ) throw WarningException("Empty operation tag argument passed to WaitForOperation.");
				SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
				const long lOperationId = konga::GetOperationIdFromTag(op_tag, *pTranDb);

				long lTimeOutSeconds = 3600;
				if ( argn >=2 ) {
					de0 = getDataElement(2,f,ts,ind,tmp);
					long itmp = de0->GetInteger(err);
					err = 0;
					if ( itmp >= 0 ) lTimeOutSeconds = itmp;
				}

				long lPollTimeSeconds = 10;
				if ( argn == 3 ) {
					de0 = getDataElement(3,f,ts,ind,tmp);
					long itmp = de0->GetInteger(err);
					err = 0;
					if ( itmp >= 0 ) lPollTimeSeconds = itmp;
				}

				try {
					OPERATION_QUEUE_WAIT_RESULT res = 
						konga::WaitForOperation(getTransformationDataHolder()->getOperationContext()->getCurrentUser(), lOperationId, lPollTimeSeconds, lTimeOutSeconds, 0, pTranDb);
					if ( res == OPERATION_QUEUE_WAIT_RESULT_TIME_OUT ) {
						throw WarningException("Timed out while waiting for the operation \"" + konga::getItemName(lOperationId, OperationEntityId, *pTranDb) + "\" to complete.");
					}
				}
				catch( const CrException& ex ) {
					throw WarningException(ex.GetErrorString());
				}

				tmp.push( de=new DataElement() );
			}
			break;
			case 323:                              
			{
				if ( argn == 0 ) throw CrException("Not enough arguments passed to InitCounter. At least one argument is required.");
				if ( argn > 2 ) throw CrException("Too many arguments passed to InitCounter. At most two arguments can be passed.");

				de0=getDataElement(1,f,ts,ind,tmp);
				string counter_name;
				if ( de0->isGlobalDE() ) {
					counter_name = de0->getName();
					if ( !counter_name.empty() && *counter_name.begin() == '$' ) {
						counter_name.erase(counter_name.begin());
					}
				}
				else {
					counter_name = (const char*)de0->GetString(err);
				}

				if ( counter_name.empty() ) throw CrException("Invalid argument passed to InitCounter. The name of the counter is empty.");

				long long initial_value = 0;
				if ( argn == 2 ) {
					DataElement* const de1=getDataElement(2,f,ts,ind,tmp);
					const long long tmp_long = de1->GetLong(err);
					if(err)handle_err(de1,err);
					initial_value = tmp_long;
					err = 0;
				}

				if ( de0->isGlobalDE() ) {
					de0->setLong(initial_value);
				}

				OperationDataElementHolder* deHolder = getTransformationDataHolder()->getOperationDataElementHolder();
				deHolder->InitCounter(counter_name, initial_value);
				tmp.push(de=new DataElement(DataElement::t_long, initial_value));
				err = 0;
			}
			break;
			case 401:                                 
			{
				if(argn == 0) {
					throw WarningException("RunGroovyScript() takes at least one argument.");
				}
				de0=getDataElement(1,f,ts,ind,tmp);
				string scriptName(de0->GetString(err));
				                    
				tmp.push(de = new DataElement(DataElement::t_string, "You just called RunGroovyScript."));
			}
			break;
			case 460:                   
				if(argn>0)throw CrException("Map() function should not have input argument.");
				tmp.push(de=new DataElement(DataElement::t_map));
				break;
			case 461:                          
				if(argn>0)throw CrException("Map() function should not have input argument.");
				tmp.push(de=new DataElement(DataElement::t_collection));
				break;
			case 462:                                     
			case 463:                                   
			case 464:                              
				break;
			default:
				throw InternalErrorException("The function " + string(f->fnc_name?f->fnc_name:"")
					+ " (ifcn = " + konga::toString(ifcn) + ") has not been implemented.");
		}
		if ( de == 0 ) {
			throw konga::InternalErrorException("Function \"" + std::string(f->fnc_name)
			+ "\" did not set any return data element. Please report this");
		}
	}
	catch( const konga::ExeFcnException& e){
		string sErr = "Error occurred when evaluating function "+t->source_location()+"\n"+e.GetErrorString();

		if( m_pDebuggee && m_pDebuggee->breakOnError() ) {
			m_pDebuggee->setError(sErr);
			IntegrationDebugBreakpointHitHelper(t);
		}
		throw konga::CrException(sErr);
	}
	catch( const WarningException& e ) {
		const std::string& warning = e.GetWarning()+
			"\nError occurred when evaluating function "+t->source_location()+"\n";
		getTreeMapper()->getViewLog()->LogMessage("%s", warning.c_str());
		getTreeMapper()->m_lastErr += warning;

		if( m_pDebuggee && m_pDebuggee->breakOnWarning() ) {
			m_pDebuggee->setWarning(warning);
			IntegrationDebugBreakpointHitHelper(t);
		}

		throw konga::CrException(warning);
	}
	catch(int err){
		std::string err_msg("Error occurred when evaluating function "+t->source_location());
		if(*(err_msg.end()-1) == '(') err_msg.erase(err_msg.end()-1);
		err_msg += ": ";
		KString sErr;
		if(!sErr.LoadString(err)){
			sErr=konga::toString(err);
		}
		sErr = err_msg + sErr;

		if( m_pDebuggee && m_pDebuggee->breakOnError() ) {
			m_pDebuggee->setError((const char*)sErr);
			IntegrationDebugBreakpointHitHelper(t);
		}

		throw CrException((const char*)sErr);
	}

	if(err)
	{
		string sErr;
		tmp.push(de=new DataElement());
		sErr=sourceTargetDescription()+err_msg;


		if(err>0)
		{
			KString sLastErr;
			sLastErr.LoadString(err);
			sErr += sLastErr;
		}

		if ( err != TRANSFORM_INPUT_EMPTY && err != TARGET_MAPPED_TO_NULL) {

			KString tmp1=f->fnc_name,tmp2;
			KString sMsg;
			sMsg.Format("Error - %s\nin ExecFunction \"%s\"", (const char*)err_msg,(const char*)tmp1.SpanExcluding("("));
  			getTreeMapper()->getViewLog()->LogMessage("%s", (const char*)sMsg);
			m_lastErr += sMsg;
			sErr = sMsg;
		}
		de->setError(err);

		if( m_pDebuggee && m_pDebuggee->breakOnWarning() ) {
			m_pDebuggee->setWarning(sErr);
			IntegrationDebugBreakpointHitHelper(t);
		}
	}
	return err;
}

std::string CTransform::inputArgumentsDescription(int nArg,...){
	konga::DataElement*de;
	va_list argList;
	va_start(argList, nArg);
	std::string str;
	for(int iArg=0; iArg<nArg; ++iArg){
		de=va_arg(argList, konga::DataElement*);
		str.append("\nArg").append(konga::toString(iArg)).append(":");
		if(de!=0)str.append(de->toString());
	}
	va_end(argList);
	str.append("\n");
	return str;
}


bool CTransform::processRunOperationCall(int argn, konga::OperationContext* const pOperationContext, const string& opTag, int syncArgPos, 
										 const Token::FNC*f, Token*ts[], vector<int>& ind, konga::AutoDeleteStack<DataElement>& tmp, int& err) {
	bool bSync = true;
	if ( argn >= syncArgPos ) {
		DataElement*de0=getDataElement(syncArgPos,f,ts,ind,tmp);
		bSync = de0->GetBool(err);
		if(err)handle_err(de0,err);
	}

	OperationDataElementHolder& deHolder = *getTransformationDataHolder()->getOperationDataElementHolder();

	string sRunOpError;
	const bool isSuccess = RunOperationImpl(opTag, bSync, &deHolder,
		*pOperationContext, m_pDebuggee, sRunOpError);
	
	if ( !isSuccess ) {
		string sLogMsg;
		if ( bSync ) {
			sLogMsg = sRunOpError;
		}
		else {
			                                                                                 
			                                       
			sLogMsg = "RunOperation could not add operation to the queue: " + sRunOpError;
			getTreeMapper()->getViewLog()->LogMessage("%s", sLogMsg.c_str());
		}
		getTreeMapper()->m_lastErr = sLogMsg;
	}
	return isSuccess;
}
