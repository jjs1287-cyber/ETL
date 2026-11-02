/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <stdlib.h>

#include <cassert>
#include <memory>		                
#include <vector>

#include <konga/Utils/StringUtils.h>				           

#include <konga/Db/DbUtils/DbUtils.h>				             

#include <konga/Db/IDb/OperationDataElementTab.h>
#include <konga/Db/IDb/SpInsertSessionIdTab.h>

#include <konga/DataElement/DefaultDataElementSerializer.h>
#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include "OperationDataElementsManager.h"

using namespace konga;
using std::string;
using std::map;
using std::vector;

namespace {
	const string stTableName("OperationDataElementTab");
}

OperationDataElementsManager::OperationDataElementsManager(DbConnection* pIdbConn) :
DbConnectionHolder(pIdbConn)
{
	this->SetInternalDbName("IDb");
}

long OperationDataElementsManager::AddDataElement(const string& stName,
												  const string& stValue,
												  const string& stArrayIndex,
												  long lSessionId)
{	
	if ( lSessionId < 1 ) {
		                                    
		DbConnection* pDbConn = &GetDbConnection();
		konga::SetClientEncoding(*pDbConn);

		SmartDbPtr<IDb::SpInsertSessionIdTab> pSpInsertSessionIdTab( IDb::SpInsertSessionIdTab::MakeInstance(pDbConn) );
		pSpInsertSessionIdTab->SetNote("o");
		pSpInsertSessionIdTab->Execute();
		lSessionId = pSpInsertSessionIdTab->GetSessionId();

		AddDataElementToDbImpl(stName, stValue, stArrayIndex, lSessionId);
	}
	else {
		AddDataElementToDbImpl(stName, stValue, stArrayIndex, lSessionId);
	}

	return lSessionId;
}

long OperationDataElementsManager::AddDataElement(const string& stName,
												  const DataElement& de,
												  const string& stArrayIndex,
												  long lSessionId)
{
	if ( de.isCollection() ) {
		const size_t arrSize = de.getDECollectionSize();
		if ( arrSize == 0 )
			return lSessionId;

		for( size_t i = 0; i < arrSize; ++i ) {
			const DataElement* const arrItem = de.getDECollectionOf(i);
			const string stNewIndex(stArrayIndex.empty() ? toString(i) :
				stArrayIndex + "," + toString(i));
			lSessionId = AddDataElement(stName, *arrItem, stNewIndex, lSessionId);
		}
	}
	else if ( de.getType() != DataElement::t_unknown &&
		de.getError() == 0 && !de.IsNull() )
	{
		DefaultDataElementSerializer serializer;
		lSessionId = AddDataElement(stName, serializer.Serialize(stName, de), stArrayIndex, lSessionId);
	}
	return lSessionId;
}

long OperationDataElementsManager::AddDataElements(const OperationDataElementHolder& deHolder) {
	long lSessionId = 0;
	const map<string, DataElement*>& dataElements = deHolder.getOperationDataElements();
	for( map<string, DataElement*>::const_iterator cit = dataElements.begin();
		cit != dataElements.end(); ++cit )
	{
		const string& stName = cit->first;
		const DataElement* const de = cit->second;
		if ( stName.find("__Salesforce.SessionID") == 0 || stName == "salesforce.sessionID" ) continue;
		if ( stName.empty() || de == 0 )
			continue;
		lSessionId = AddDataElement(stName, *de, "", lSessionId);
	}
	return lSessionId;
}

namespace {
	                                                             
	                                                      
	                                               
	const string NOT_AN_ARRAY_INDEX = "-";
}

void OperationDataElementsManager::AddDataElementToDbImpl(const string& stName,
														  const string& stValue,
														  const string& stArrayIndex,
														  long lSessionId)
{
	                                                                                  
	                                                   
	assert(lSessionId>0);
	SmartDbPtr<IDb::OperationDataElementTab> pOperationDataElementTab( IDb::OperationDataElementTab::MakeInstance(&GetDbConnection()) );

	pOperationDataElementTab->AddNew();
	pOperationDataElementTab->SetSessionId(lSessionId);
	pOperationDataElementTab->SetDataElementName(stName);

	                                                                     
	                                          
	pOperationDataElementTab->SetArrayIndex(stArrayIndex.empty()? NOT_AN_ARRAY_INDEX : stArrayIndex);

	pOperationDataElementTab->SetStringValue(stValue);
	pOperationDataElementTab->Update();
}

namespace {

	void AddArrayItem(DataElement& arr_in, const DataElement& newDe, const string& stArrayIndex) {
		assert(arr_in.isCollection());
		vector<string> indices = konga::SplitString(stArrayIndex, ",");
		assert(!indices.empty());
		DataElement* arr = &arr_in;
		for( vector<string>::const_iterator cit = indices.begin();
			cit != indices.end(); ++cit )
		{
			const size_t index = atoi(cit->c_str());
			const size_t currentSize = arr->getDECollectionSize();

			                                                                            
			for( size_t i = currentSize; i <= index; ++i ) {
				arr->addToCollection(new DataElement());
			}

			DataElement* de = arr->getDECollectionOf(index);
			if ( cit == indices.end()-1 ) {
				                                            
				de->set(&newDe);
			}
			else {
				                           
				                                    
				if ( !de->isCollection() )
					de->initCollection();
				arr = de;
			}
		}
	}
}

                                                  
void OperationDataElementsManager::GetDataElements(OperationDataElementHolder& deHolder, long lSessionId) {
	if ( lSessionId < 1 )
		throw InternalErrorException("SessionId has to be a positive integer.");

	                                                                                                                   
	DbConnection* pDbConn = &GetDbConnection();
	konga::SetClientEncoding(*pDbConn);

	SmartDbPtr<IDb::OperationDataElementTab> pOperationDataElementTab( IDb::OperationDataElementTab::MakeInstance(pDbConn) );
	pOperationDataElementTab->Select("WHERE SessionId = " + toString(lSessionId)
		+ " ORDER BY DataElementName");
	string stArrName;
	DataElement* arr = 0;
	DefaultDataElementSerializer serializer;
	while( pOperationDataElementTab->Next() ) {
		const string& deName = pOperationDataElementTab->GetDataElementName();
		if ( deName.empty() )
			continue;

		const string& stArrayIndex = pOperationDataElementTab->GetArrayIndex();
		const string& stValue = pOperationDataElementTab->GetStringValue();
		std::auto_ptr<DataElement> pNewDe( serializer.DeSerialize(stValue) );

		if ( stArrayIndex == NOT_AN_ARRAY_INDEX ) {
			if ( !stArrName.empty() ) {
				stArrName.clear();
				arr = 0;
			}
			pNewDe->setName(deName.c_str());
			DataElement* de = deHolder.getOperationDataElement(deName);
			de->set(pNewDe.get());
		}
		else {
			                 
			if ( stArrName.empty() || stArrName != deName ) {
				             
				if ( !stArrName.empty() ) {
					stArrName.clear();
					arr = 0;
				}
				stArrName = deName;
				arr = deHolder.getOperationDataElement(stArrName);
				arr->initCollection();
				arr->setName(stArrName.c_str());
			}
			assert(arr);
			assert(pNewDe.get());
			assert(!stArrayIndex.empty());
			AddArrayItem(*arr, *pNewDe, stArrayIndex);
		}
	}
}

void OperationDataElementsManager::DeleteSession(long lSessionId) {
	const string stWhere(" WHERE SessionId = " + toString(lSessionId));
	this->ExecuteSql("DELETE FROM " + stTableName + stWhere);
	this->ExecuteSql("DELETE FROM SessionIdTab" + stWhere);
}
