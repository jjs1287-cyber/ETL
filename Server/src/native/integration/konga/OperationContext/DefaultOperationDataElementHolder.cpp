/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <ctype.h>	          

#include <memory>	           
#include <cassert>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>		              
#include <konga/Utils/Settings.h>
#include <konga/Utils/Matcher.h>

#include <konga/SysInfo/SysInfo.h>		                

#include <konga/DataElement/DataElement.h>

#include <konga/ServerVersion/ServerVersion.h>	                     

#include "DataElementHandler.h"
#include "DefaultOperationDataElementHolder.h"

using namespace konga;
using std::map;
using std::string;
using std::stack;

DefaultOperationDataElementHolder::DefaultOperationDataElementHolder(const UserInfo& currentUser) :
m_CurrentUser(currentUser),
m_protectedDataElements(m_CurrentUser)
{
	konga::AccessProtectedDataElementsHandler::RegisterAll(*this, m_protectedDataElements);
	map<string, string> map;
	const string sectionName("PredefinedServerGlobalDataElement");
	const string::size_type len=sectionName.size();

	konga::LoadSectionNameValuePairs(sectionName, map);
	for( std::map<string, string>::const_iterator it=map.begin(); it!=map.end(); ++it ){
		const string name(it->first.substr(len+1));
		const string deName("$"+name);
		const string& value=it->second;
		DataElement* const de = new DataElement(deName.c_str());
		de->setType(DataElement::t_string);
		de->setValue(value.c_str());
		m_DataElements.insert( IDataElementMapT::value_type(name, de) );
	}
}

DefaultOperationDataElementHolder::~DefaultOperationDataElementHolder(void) {
	konga::AccessProtectedDataElementsHandler::UnRegisterAll(*this);

	for( IDataElementMapT::iterator it = m_DataElements.begin(); it != m_DataElements.end(); ++it ) {
		delete it->second;
                   
	}
                          
}

bool DefaultOperationDataElementHolder::empty() const {
	return m_DataElements.empty();
}

DataElement* DefaultOperationDataElementHolder::getOperationDataElement(const string& sDataElementName) const {
	assert(!sDataElementName.empty());
	                                                         
	{
		DataElement* const externallyHandledDe = getExternallyHandledDataElement(sDataElementName);
		if ( externallyHandledDe != 0 )
			return externallyHandledDe;
	}

	                                                           
	IDataElementMapT::iterator it = m_DataElements.find(sDataElementName);
	if ( it == m_DataElements.end() ) {
		                                                        
		DataElement* de = new DataElement();
		de->setName(sDataElementName.c_str());
		m_DataElements.insert( IDataElementMapT::value_type(sDataElementName, de) );
		return de;
	}
	else {
		return it->second;
	}
}

void DefaultOperationDataElementHolder::registerDataElementHandler(const string& deName,
																   DataElementHandler* pDEHandler)
{
	IDataElementHandlerMapT::iterator it = m_DataElementHandlers.find(deName);
	if ( it != m_DataElementHandlers.end() ) {
		stack<DataElementHandler*>& st = it->second;
		if ( pDEHandler == 0 ) {
			                           
			if ( !st.empty() ) st.pop();
		}
		else {
			                        
			st.push(pDEHandler);
		}

		                                                         
		if ( st.empty() ) m_DataElementHandlers.erase(it);
	}
	else {
		if ( pDEHandler != 0 ) {
			stack<DataElementHandler*> st;
			st.push(pDEHandler);
			m_DataElementHandlers.insert(
				IDataElementHandlerMapT::value_type(deName, st)
			);
		}
	}
}

void DefaultOperationDataElementHolder::InitCounter(const std::string& stCounter, long long initial_value) {
	DataElement* de = this->getOperationDataElement(stCounter);
	de->setLong(initial_value);

	AddOrUpdate<CounterMapT, string, bool>(m_counters, stCounter, true);
}

std::string DefaultOperationDataElementHolder::getCountersAsString(const std::string& separator) const {
	string counters;
	for( CounterMapT::const_iterator cit = m_counters.begin(); cit != m_counters.end(); ++cit ) {
		if ( !counters.empty() ) counters += separator;
		counters += cit->first;
	}
	return counters;
}

OperationDataElementHolder* DefaultOperationDataElementHolder::clone() const {
	boost::mutex::scoped_lock lock(m_cloneLock);

	std::auto_ptr<DefaultOperationDataElementHolder> clone( new DefaultOperationDataElementHolder(this->m_CurrentUser) );
	clone->m_DataElementHandlers = this->m_DataElementHandlers;
	IDataElementMapT::const_iterator cit = m_DataElements.begin();
	for( ; cit != m_DataElements.end(); ++cit ) {
		DataElement* clonedDe = clone->getOperationDataElement(cit->first);

		                                                    
		const string& key = cit->first;
		if ( isCounter(key) ) {
			clonedDe->setLong(0);
		}
		else {
			clonedDe->set(cit->second);
		}
	}

	clone->m_counters = this->m_counters;

	return clone.release();
}

bool DefaultOperationDataElementHolder::isCounter(const std::string& key) const {
	return m_counters.find(key) != m_counters.end();
}

void DefaultOperationDataElementHolder::merge(const OperationDataElementHolder& clone) {
	boost::mutex::scoped_lock lock(m_cloneLock);

	const map<string, DataElement*>& m = clone.getOperationDataElements();
	map<string, DataElement*>::const_iterator cit = m.begin();
	for( ; cit != m.end(); ++cit ) {
		const string& key = cit->first;
		if ( !this->isOperationDataElementDefined(key) ) {
			DataElement* de = this->getOperationDataElement(key);
			de->set(cit->second);
		}
		else if ( isCounter(key) ) {
			int err;
			DataElement* deThis = this->getOperationDataElement(key);
			const long long thisCounter = deThis->GetLong(err);
			const long long cloneCounter = cit->second->GetLong(err);
			deThis->setLong(thisCounter + cloneCounter);
		}
	}
}

DataElement* DefaultOperationDataElementHolder::getExternallyHandledDataElement(const string& sDataElementName) const {
	const IDataElementHandlerMapT::const_iterator cit = m_DataElementHandlers.find(sDataElementName);
	if ( cit != m_DataElementHandlers.end() ) {
		const stack<DataElementHandler*>& st = cit->second;
		assert(!st.empty());
		if ( st.empty() )
			return 0;	                                      

		std::auto_ptr<DataElement> de( st.top()->getDataElement(sDataElementName) );
		if ( de.get() == 0 ) {
			                                                            
			                                 
			de.reset( new DataElement() );
		}

		const IDataElementMapT::iterator it = m_DataElements.find(sDataElementName);
		if ( it != m_DataElements.end() ) {
			it->second->set(de.get());
			return it->second;
		}
		else {
			DataElement* pRetVal = 0;
			m_DataElements.insert(
				IDataElementMapT::value_type(sDataElementName, pRetVal=de.release())
			);
			return pRetVal;
		}
	}
	return 0;
}

const DataElement* DefaultOperationDataElementHolder::getReadOnlyDataElement(const string& sDataElementName) const {
	{
		DataElement* de = getExternallyHandledDataElement(sDataElementName);
		if ( de != 0 ) return de;
	}

	IDataElementMapT::const_iterator cit = m_DataElements.find(sDataElementName);
	if ( cit != m_DataElements.end() ) {
		return cit->second;
	}
	return 0;
}

bool DefaultOperationDataElementHolder::isOperationDataElementDefined(const string& sDataElementName) const {
	return ( m_DataElements.find(sDataElementName) != m_DataElements.end() );
}

void DefaultOperationDataElementHolder::removeOperationDataElement(const string& sDataElementName) {
	IDataElementMapT::iterator it = m_DataElements.find(sDataElementName);
	if ( it == m_DataElements.end() )
		return;	                                    
	delete it->second;
	m_DataElements.erase(it);
}

void DefaultOperationDataElementHolder::setCurrentUser(const UserInfo& currentUser) {
	m_CurrentUser = currentUser;
}

namespace {
	const string sStartQuote("[");
	const string sEndQuote("]");
	const string sCharsToEscape("[]");
	const string sDefaultCharsToEscape("{}");

	const char escapeChar = '\\';
	const char startDefaultChar = '{';
	const char endDefaultChar = '}';

	string::size_type FindEndQuote(const string& stValue, string::size_type startPos) {
		const string::size_type startQSize = sStartQuote.size(), endQSize = sEndQuote.size();
		string::size_type startQ = 0, endQ = 0, retVal = startPos;
		int level = 1;

		while(level>0)
		{
			startQ=stValue.find(sStartQuote, startPos);
			endQ=stValue.find(sEndQuote, startPos);
			if ( startQ < endQ && stValue.at(startQ-1)!=escapeChar ) {
				++level;
				startPos = startQ + startQSize;
			}
			else if ( endQ < startQ && stValue.at(endQ-1)!=escapeChar ) {
				--level;
				startPos = endQ + endQSize;
			}
			else {
				                                             
				startPos = endQ + endQSize;
			}
			retVal = endQ;
			if ( endQ == string::npos )
				break;
		}

		                                                                                                
		if ( level > 0 )
			return string::npos;
		return retVal;
	}

	                                          
	                                       
	bool GetDefaultValue(string& key, string& defaultValue) {
		                                                                             
		                                      
		if ( key.size() < 3 )
			return false;

		if ( *(key.end()-1) == endDefaultChar ) {
			                          
			if ( *(key.end()-2) == escapeChar )
				return false;	          
			else if ( *(key.end()-2) == startDefaultChar ) {
				key = key.substr(0, key.size()-2);
				return true;	                      
			}
			else {
				string::size_type startPos = key.find_last_of(startDefaultChar, key.size()-2);
				if ( startPos == string::npos )
					return false;
				else if ( startPos == 0 )
					return false;	                                                      
				else if ( key.at(startPos-1) == escapeChar )
					return false;	          
				else {
					defaultValue = key.substr(startPos+1, key.size()-startPos-2);
					key = key.substr(0, startPos);
					return true;
				}
			}
		}
		return false;
	}
}

                                                                                 
                                                                                             
                                                    
string DefaultOperationDataElementHolder::replaceOperationDataElementInString(const string& stValue) const {
	                                                                              
	const string::size_type startQSize = sStartQuote.size(), endQSize = sEndQuote.size();

	const string::size_type nSize = stValue.size();
	if ( nSize <= startQSize+endQSize )
		return stValue;

	string sRetVal;
	string::size_type startQ = 0, endQ = 0, startPos = 0;
	while ( startPos < nSize-startQSize-endQSize &&
		  ((startQ=stValue.find(sStartQuote, startPos)) != string::npos) )
	{
		const bool bDoubleEscape = (startQ > 1 && stValue.at(startQ-1)==escapeChar && stValue.at(startQ-2)==escapeChar);
		if ( bDoubleEscape )
			sRetVal += stValue.substr(startPos, startQ-startPos-1);
		else
			sRetVal += stValue.substr(startPos, startQ-startPos);	                       
		
		if ( startQ==0 || stValue.at(startQ-1)!=escapeChar || bDoubleEscape )
		{
			endQ = FindEndQuote(stValue, startQ+startQSize);
			if ( endQ == string::npos ) {
				                                                        
				return sRetVal + stValue.substr(startQ);
			}
			else if ( endQ > startQ+startQSize+endQSize-1 ) {
				                                                                  
				string sKey = stValue.substr(startQ+startQSize, endQ-startQ-startQSize);
				const string sKeyOrig(sKey);
				if ( sKey.size() >= 2 && sKey.at(0) == '$' ) {
					                                                        
					                                                                 
					                                  
					sKey = sKey.substr(1);
				}
				sKey = replaceOperationDataElementInString(sKey);
				RemoveEscapeSequences(sKey, sCharsToEscape, escapeChar);
				string sDefaultValue;
				const bool bHasDefault = GetDefaultValue(sKey, sDefaultValue);
				if ( !bHasDefault )
					RemoveEscapeSequences(sKey, sDefaultCharsToEscape, escapeChar);

				string sDeVal;
				const DataElement* const de = getReadOnlyDataElement(sKey);
				if ( de ) {
					int err = 0;

					sDeVal = (const char*)de->GetString(err);
					sRetVal += sDeVal;
				}
				                                                                                     
				                                     
				else if ( strcasecmp(sKey.c_str(), "GetPlatform()") == 0 ) {
					sRetVal += GetPlatform();
				}
				else if ( sKey.size()==13 &&
						  strncasecmp(sKey.c_str(), "GetVersion(", 11) == 0 &&
						  *(sKey.end()-1) == ')' &&
						  isdigit(static_cast<int>(*(sKey.end()-2))) )
				{
					char buf[2];
					buf[0] = *(sKey.end()-2);
					buf[1] = 0;
					int i = atoi(buf);
					sRetVal += GetServerVersion().GetVersionString(i);
				}
				else {
					                                                               
					if ( bHasDefault )
						sRetVal += sDefaultValue;
					else 
						sRetVal += (sStartQuote + sKeyOrig + sEndQuote);
				}
				startPos = endQ + endQSize;
			}
			else {
				                                                                        
				sRetVal += sStartQuote;
				sRetVal += sEndQuote;
				startPos = (startQ + startQSize + endQSize);
			}
		}
		else {
			sRetVal += sStartQuote;
			startPos = startQ+startQSize;
		}
	}
	sRetVal += stValue.substr(startPos);

	return sRetVal;
}

const map<string, DataElement*>& DefaultOperationDataElementHolder::getOperationDataElements() const {
	m_cache.clear();
	for( IDataElementMapT::const_iterator cit = m_DataElements.begin();
		cit != m_DataElements.end(); ++cit )
	{
		m_cache.insert( map<string, DataElement*>::value_type(cit->first, cit->second) );
	}

	return m_cache;
}

namespace {

	class StartingWithMatcher : public Matcher {
	public:
		StartingWithMatcher(const string& startsWith) : m_startsWithLower(startsWith) {
			konga::ToLower(m_startsWithLower);
		}
		virtual ~StartingWithMatcher(void) {}

		bool isMatch(const std::string& nameLower) const {
			return nameLower.find(m_startsWithLower) == 0;
		}

	private:
		string m_startsWithLower;
	};
}

void DefaultOperationDataElementHolder::getOperationDataElementsStartingWith(const string& startsWith, map<string, DataElement*>& filtered) const {
	StartingWithMatcher matcher(startsWith);
	this->getMatchingOperationDataElements(matcher, filtered);
}

void DefaultOperationDataElementHolder::getMatchingOperationDataElements(const Matcher& matcher, std::map<std::string, DataElement*>& filtered) const {
	const map<string, DataElement*>& all = getOperationDataElements();
	if ( all.empty() ) return;
	for( map<string, DataElement*>::const_iterator cit = all.begin(); cit != all.end(); ++cit ) {
		const string& name = cit->first;
		string nameLower(name);
		konga::ToLower(nameLower);
		if( matcher.isMatch(nameLower) ) {
			filtered[name] = cit->second;
		}
	}
}
