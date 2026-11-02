/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                              

#include <stdlib.h>
#include <string>
#include <memory>					                

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/util.h>
#include <konga/Utils/ErrorResolver.h>

#include <konga/Server/WDIPathFinder.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Ldap/konga_ldap.h>
#include <konga/Ldap/LdapFilter.h>
#include <konga/Ldap/LdapTreeNode.h>

#include <konga/DataElement/DataElement.h>

#include <konga/SystemVariables/SystemVariables.h>	                       

#include <konga/TransformationUtils/LdapExecutor.h>
#include <konga/TransformationUtils/TranDBUtils.h>

#include "XmlReader.h"
#include "CROMData.h"
#include "TreeMapper.h"
#include "TextDocument.h"
#include "LdapDataConverter.h"

using namespace konga;
using std::string;
using std::map;
using std::vector;

#ifdef _DEBUG
#define debug_ldap
#endif

namespace {
	bool isArray(const char* s){
		return strncmp(s,"<JB_Array>",10)==0;
	}

	void parseArray(const char*s, vector<string>& v){
		string stXmlHeader;
		konga::XmlReader reader("");
		reader.parse(s);
		JbNode* root=reader.getRoot();
		v.clear();
		for(vector<JbNode*>::const_iterator it=root->getChildren().begin(); it!=root->getChildren().end(); ++it){
			JbNode*child=*it;
			v.push_back(child->getValue());
		}
	}

	bool ValuesAreEqual(const char* lhs, const char* rhs, bool bLdapMatchIgnoreCase) {
		return bLdapMatchIgnoreCase ?
			stricmp(lhs, rhs)==0 :
			strcmp(lhs, rhs)==0;
	}

	bool ValueIsInArray(const vector<string>& v, const char* value, bool bLdapMatchIgnoreCase) {
		for( vector<string>::const_iterator cit = v.begin(); cit != v.end(); ++cit ) {
			if ( ValuesAreEqual(cit->c_str(), value, bLdapMatchIgnoreCase) )
				return true;
		}
		return false;
	}
}

typedef map<string,string> AttrMap;
class LdapAttributeMap {
public:
	LdapAttributeMap(bool bLdapMatchIgnoreCase) : m_LdapMatchIgnoreCase(bLdapMatchIgnoreCase) {}
	static string get_key(const string&name, int k){
		if(k==0)return string(name);
		else return name+"["+konga::toString(k)+"]";
	}
	void clear(){m_attr_map.clear();}
	void reset(konga::LdapTreeNode*pTreeNode_Entry, FILE*debug_log);
	const char* find_existing_value(const string& key);
	void delete_attribute_value(const string& attribute_name, const char* attribute_value);
	void add_attribute_value(const string& attribute_name, const char* attribute_value);

                           

private:
	bool m_LdapMatchIgnoreCase;
	AttrMap m_attr_map;
};

  
                                           
          
                                                                                          
                             
                                                          
  
          
 
  
void LdapAttributeMap::reset(konga::LdapTreeNode*pTreeNode_Entry, FILE*out){
	m_attr_map.clear();
	for(konga::LdapTreeNode::const_iterator it=pTreeNode_Entry->begin();it!=pTreeNode_Entry->end();it++){
		const std::string& sName((*it)->GetName());
		if(m_attr_map.find(sName)!=m_attr_map.end()){
			for(int k=1;true;k++){
				const std::string sKey(get_key(sName,k));
				if(m_attr_map.find(sKey)==m_attr_map.end()){
					m_attr_map[sKey]=(*it)->GetData();
					if(out!=0){
						fprintf(out,"  attribute: %s=\"%s\"\n",sKey.c_str(),((*it)->GetData()).c_str());
					}
					break;
				}
			}
		}
		else {
			m_attr_map[sName]=(*it)->GetData();
			if(out!=0){
				fprintf(out,"  attribute:%s=\"%s\"\n",sName.c_str(),((*it)->GetData()).c_str());
			}
		}
	}
}

const char* LdapAttributeMap::find_existing_value(const std::string& key) {
	AttrMap::const_iterator it=m_attr_map.find(key);
	if(it==m_attr_map.end())return 0;
	return it->second.c_str();
}

void LdapAttributeMap::delete_attribute_value(const std::string& attribute_name, const char* attribute_value){
	for(int k=0; true; k++){
		const std::string sKey(get_key(attribute_name,k));
		const char*existing_value=find_existing_value(sKey);
		if(!existing_value) return;
		if(strcmp(attribute_value,existing_value)==0){
			m_attr_map[sKey]="";                                                       
			return;
		}
	}
}

void LdapAttributeMap::add_attribute_value(const std::string& attribute_name, const char* attribute_value){
	for(int k=0; true; k++){
		const std::string sKey(get_key(attribute_name,k));
		const char*existing_value=find_existing_value(sKey);
		if(existing_value){
			if(ValuesAreEqual(attribute_value,existing_value, m_LdapMatchIgnoreCase))return;
		}
		else {
			m_attr_map[sKey]=attribute_value;
			return;
		}
	}
}

namespace {
	const short bit_add=1, bit_delete=2;

	class LdapValueMap : public map<string,short> {
	public:
		LdapValueMap(bool bLdapMatchIgnoreCase) : m_bLdapMatchIgnoreCase(bLdapMatchIgnoreCase) {}

		                                                                                          
		iterator find(const key_type& _Keyval) {
			if ( m_bLdapMatchIgnoreCase ) {
				map<string,short>::iterator it = this->begin();
				for( ; it != this->end(); ++it) {
					if( ValuesAreEqual(it->first.c_str(), _Keyval.c_str(), m_bLdapMatchIgnoreCase) )
						return it;
				}
				return it;
			}
			else {
				return map<string,short>::find(_Keyval);
			}
		}

		void update_value_map(const vector<string>& vValue, short bit) {
			for(vector<string>::const_iterator cit=vValue.begin();cit!=vValue.end();cit++){
				const string& value(*cit);
				map<string,short>::iterator it=this->find(value);
				if(it==this->end()){
					this->insert( map<string,short>::value_type(value,bit) );
				}
				else {
					it->second |= bit;
				}
			}
		}

		void update_value_map(const std::string& value, short bit){
			if(!isArray(value.c_str())){
				map<string,short>::iterator it=this->find(value);
				if(it==this->end()){
					this->insert( map<string,short>::value_type(value, bit) );
				}
				else {
					it->second |= bit;
				}
			}
			else {
				vector<string>vValue;
				parseArray(value.c_str(), vValue);
				update_value_map(vValue, bit);
			}
		}

	private:
		const bool m_bLdapMatchIgnoreCase;
	};

	enum AttrCommand {att_add, att_remove, att_replace};

	string GetDisplayString(AttrCommand cmd) {
		switch(cmd) {
			case att_add:
				return "add";
			case att_remove:
				return "remove";
			case att_replace:
				return "replace";
			default:
				return "unknown";
		}
	}

	                                                                              
	string addBaseDN(const string& sRelativeDN, const string& sBaseDN){
		                                              
		if(sRelativeDN.empty()) {
			return sBaseDN;
		}

		                                                       
		if(sBaseDN.empty()) {
			return sRelativeDN;
		}

		                                                                                
		if( sRelativeDN.size() >= sBaseDN.size() ) {
			const string tmp( sRelativeDN.substr(sRelativeDN.size() - sBaseDN.size()) );
			if( EqualsIgnoreCase(tmp, sBaseDN) )
				return sRelativeDN;
		}

		                                                      
		return string(sRelativeDN+","+sBaseDN);
	}

	                                                                                                                                
	void split(const std::string& distinquished_name, std::string& path, std::string& filter, char delimiter)
	{
		for(int i=0,n=(int)distinquished_name.size();i<n;i++){
			char c;
			if((c=distinquished_name.at(i))=='\\'){
				i++;
				continue;
			}
			if(c==delimiter){
				filter=distinquished_name.substr(0,i);
				path=distinquished_name.substr(i+1);
				return;
			}
		}
		path=distinquished_name;
		filter="";
	}

	void get_value(CROMData* grandChild, std::string& value, bool& isValueNull){
		                                                     
		                                                               
		if(grandChild->IsNull()){
			isValueNull=true;
			value="";
		} else {
			isValueNull=false;
			value=(const char*)grandChild->GetValue();
			if(grandChild->get_DataElementType() == DataElement::t_bool ) {
				value = atoi(value.c_str()) ? "TRUE" : "FALSE";
			}
		}
	}

	const std::string get_key(const std::string&name, int k){
		if(k==0)return std::string(name);
		else return name+"["+konga::toString(k)+"]";
	}

	bool mapped(CROM*attribute){
		if(attribute->IsLeaf()){
			return attribute->getSourceExpr()!=0;
		}
		for(CROM*child=attribute->GetFirstChild();child;child=child->GetNextSibling()){
			if(mapped(child))return true;
		}
		return false;
	}

	int countAttributes(CROMData*entry){
		int nAttribute=0;
		for(CROMData*child=entry->GetFirstChild();child;child=child->GetNextSibling()){
			if(child->IsLeaf())continue;
			CROMData*attribute=child;
			std::string attribute_name=(const char*)attribute->GetDisplayName(), value, old_value, cmd;
			bool isValueNull=true, isOldValueNull=true, isAttrCmdNull=true;
			for(CROMData*grandChild=attribute->GetFirstChild();grandChild;grandChild=grandChild->GetNextSibling()){
				if(grandChild->IsLeaf()){
					if(grandChild->GetDisplayName().IsEmpty()){
						get_value(grandChild, value, isValueNull);
					}
					else if(stricmp((const char*)grandChild->GetDisplayName(),"Old value")==0){
						get_value(grandChild, old_value, isOldValueNull);
					}
					else if(stricmp((const char*)grandChild->GetDisplayName(),"Attribute command")==0){
						get_value(grandChild, cmd, isAttrCmdNull);
					}
					if(!isValueNull || isOldValueNull || isAttrCmdNull)nAttribute++;
				}
			}
		}
		return nAttribute;

	}
}


unsigned long TreeMapper::ProcessLdapTarget(CROMData* pDataRoot){
	PROFILING doPrf(this,"ProcessLdapTarget");

	const bool bLdapMatchIgnoreCase = konga::GetOptionAsBoolean("TransformationEngine", "LdapMatchIgnoreCase", false);
	FILE*out=0;
	konga::AutoFileHandle adopt_out(out);
	if(getViewLog()->IsDebugLogEnabled())
	{
		KString WDIDebugPath;
		if(WDIPathFinder::GetWDIDebugPath(WDIDebugPath)){
			WDIDebugPath+=getTransformationName__("_ldapTarget");
			out = fopen(WDIDebugPath, "w+");
			if(out!=0)fprintf(out,"ProcessLdapTarget()\n");
		}
	}

	enum LdapCommand {en_add, en_update, en_delete_entry, en_update_only};
	LdapExecutor& ldap_exec = *getLdapExecutor();
	try {
		const string psswrd(konga::DecryptString(m_targetDbConnectionInfo.Password));
		OperationDataElementHolder* pDEHolder = this->getOperationDataElementHolder();
		if ( pDEHolder ) {
			konga::replaceGlobalDataElementsInDbLocationConnectionInfo(m_targetDbConnectionInfo, *pDEHolder);
		}
		ldap_exec.LdapConnect(m_targetDbConnectionInfo.Server,m_targetDbConnectionInfo.User,psswrd,m_targetDbConnectionInfo.Secured,m_targetDbConnectionInfo.Port);
	}
	catch(const CrException& e){
		std::string stErrMsg = e.GetErrorString();
		AppendErrorMessage(stErrMsg.c_str());
		return E_FAIL;
	}

	CROMData*pData_ldap=pDataRoot;
	int nEntries=0, nSuccess=0;
	std::vector<std::string> vAttr;
	std::auto_ptr<LdapTreeNode> pResult;
	LdapTreeNode* pTreeNode_Entry;
	LdapAttributeMap attr_map(bLdapMatchIgnoreCase);

	CROM*pCROM_Entry_saved=0;
	for(CROMData*obj=pData_ldap->GetFirstChild();obj!=0;obj=obj->GetNextSibling()){
		if(obj->IsNotALeaf()){
			CROMData*entry=obj;
			try {
				LdapCommand entry_command=en_update;
				std::string className=(const char*)entry->GetDTDNode()->GetDisplayName();
				if(out)fprintf(out,"process entry:%s\n",className.c_str());
				std::string distinquished_name;
				for(CROMData*child=entry->GetFirstChild();child;child=child->GetNextSibling()){
					if(!child->IsLeaf())continue;
					if(child->GetDisplayName()=="Distinguished name"){
						distinquished_name=( addBaseDN((const char*)child->GetValue(),m_targetDbConnectionInfo.Path) );
					}
					else if(stricmp((const char*)child->GetDisplayName(),"Entry command")==0){
						std::string cmd=(const char*)child->GetValue();
						if(stricmp(cmd.c_str(),"Add")==0)entry_command=en_add;
						else if(stricmp(cmd.c_str(),"Delete")==0)entry_command=en_delete_entry;
						else if(stricmp(cmd.c_str(),"Update")==0)entry_command=en_update;
						else if(stricmp(cmd.c_str(),"Update Only")==0)entry_command=en_update_only;
						else {
							getViewLog()->LogMessage("Invalid Entry Command: %s. Only Add, Delete, Update and Update Only are allowed. Default update command (Update) will be used.", cmd.c_str());
						}
					}
				}
				++nEntries;
				if(out)fprintf(out,"	entry command: %s, Entry Number %d, DN='%s'\n",
					entry_command==en_add?"add":
					(entry_command==en_delete_entry?"delete":
					 (entry_command==en_update?"update":
					 (entry_command==en_update_only?"update only":""))),nEntries, distinquished_name.c_str());
				switch(entry_command){
					case en_delete_entry:
						processLdapDelete(distinquished_name, nSuccess, out); break;
					case en_add:
						processLdapAdd(entry, distinquished_name, className, nSuccess, out); break;
					case en_update:
					case en_update_only:
					{
						CROM*pEntry_CROM=entry->GetDTDNode();
						if(out)fprintf(out, "	Attributes:");
						if(pCROM_Entry_saved==0||pCROM_Entry_saved!=pEntry_CROM){
							pCROM_Entry_saved=pEntry_CROM;
							vAttr.clear();
							for(CROM*child=pCROM_Entry_saved->GetFirstChild();child;child=child->GetNextSibling()){
								if(child->IsElement() && mapped(child)){
									vAttr.push_back((const char*)child->GetDisplayName());
									if(out)fprintf(out, "%s,", (*(vAttr.end()-1)).c_str());
								}
							}
						}
						if(out)fprintf(out, "<end_attr>\n");
						std::string path(distinquished_name),filter("objectClass=*");

						if(out)fprintf(out,"	LdapSearch: path=%s, filter=%s\n", path.c_str(), filter.c_str());
						pResult.reset(ldap_exec.LdapSearch(path,filter,vAttr,konga::LdapSearchParameters(konga::LdapClassFilter::LDAP_SEARCH_SCOPE_BASE)));
						pTreeNode_Entry = pResult->empty() ? 0 : *pResult->begin();
						if(out)fprintf(out, "	LdapSearch result: %s\n", pTreeNode_Entry?pTreeNode_Entry->GetName().c_str():"<not found>");

						if(pTreeNode_Entry){
							attr_map.reset(pTreeNode_Entry, out);

							processLdapUpdate(entry, distinquished_name, attr_map, nSuccess, bLdapMatchIgnoreCase, out);
						} else if(entry_command != en_update_only) {                                  
							if(out)fprintf(out, "attr_map.clear()\n");
							attr_map.clear();
							processLdapAdd(entry, distinquished_name, className, nSuccess, out);
						}
						else {
							if(out)fprintf(out,"	Nothing found to update (Update Only)\n");
							++nSuccess;
						}
					}
					break;
				}
			}
			catch(const CrException&e){
				const string& stErrMsg = e.GetErrorString();
				getViewLog()->LogMessage("%s", stErrMsg.c_str());
				AppendErrorMessage(stErrMsg.c_str());
			}
		}
	}

	if( nEntries>0 && nSuccess==0) {
		if(out)fprintf(out, "No entries were changed. nEntries=%d, nSuccess=%d", nEntries, nSuccess);
		getViewLog()->LogMessage("No LDAP entries were changed. Number of target entries: %d", nEntries);
	}
	return S_OK;
}

void TreeMapper::processLdapDelete(const std::string& distinquished_name, int& nSuccess, FILE*out){
	if(out)fprintf(out,"	processLdapDelete: distinquished_name=%s\n", distinquished_name.c_str());
	LdapExecutor& ldap_exec = *getLdapExecutor();
	ldap_exec.LdapDeleteEntry(distinquished_name, out);
	ldap_exec.LdapExecute(distinquished_name, out);
	nSuccess++;
}

namespace {
	void LogAttributeArrayWarning(CViewLog* pViewLog, const string& attribute_name, const string& value) {
		if(pViewLog==0)return;
		pViewLog->LogMessage("Arrays can't be passed to an LDAP single value attribute, the attribute is ignored." \
			" Attribute name is '%s', value is '%s'.", attribute_name.c_str(), value.c_str());
	}
}

void TreeMapper::processLdapAdd(CROMData*entry, const std::string& distinquished_name, const std::string& className, int&nSuccess, FILE*out){
	if(out)fprintf(out,"	processLdapAdd: distinquished_name=%s, className=%s\n", distinquished_name.c_str(), className.c_str());
	LdapExecutor& ldap_exec = *getLdapExecutor();
	ldap_exec.LdapAdd("objectClass", className, out);
	for(CROMData*child=entry->GetFirstChild();child;child=child->GetNextSibling()){
		if(child->IsLeaf())continue;
		CROMData*attribute=child;
		std::string attribute_name=(const char*)attribute->GetDisplayName(), value;
		bool isValueNull=true;

		for(CROMData*grandChild=attribute->GetFirstChild();grandChild;grandChild=grandChild->GetNextSibling()){
			if(grandChild->IsLeaf()){
				if(grandChild->GetDisplayName().IsEmpty()){
					get_value(grandChild, value, isValueNull);
				}
			}
		}

		if(isValueNull)continue;
		if(isArray(value.c_str())) {
			if(attribute->IsLoopNode()) {
				vector<string> values;
				parseArray(value.c_str(), values);
				ldap_exec.LdapAdd(attribute_name, values, out);
			}
			else {
				LogAttributeArrayWarning(getViewLog(), attribute_name, value);
			}
		}
		else
			ldap_exec.LdapAdd(attribute_name, value, out);
	}
	ldap_exec.LdapExecute(distinquished_name, out);
	nSuccess++;
}

void TreeMapper::processLdapUpdate(CROMData*entry, const std::string& distinquished_name, LdapAttributeMap& attr_map, int& nSuccess, bool bLdapMatchIgnoreCase, FILE*out){
	if(out)fprintf(out,"	processLdapUpdate: distinquished_name=%s\n", distinquished_name.c_str());
	std::string multiple_value_attribute_name;
	std::vector<CROMData*>multiple_value_attributes;
	for(CROMData*child=entry->GetFirstChild();child;child=child->GetNextSibling()){
		if(child->IsLeaf())continue;
		CROMData*attribute=child;
		std::string attribute_name=(const char*)attribute->GetDisplayName(), value, old_value, cmd;
		if(attribute->IsLoopNode()){
			if(attribute_name!=multiple_value_attribute_name){
				if(!multiple_value_attributes.empty())ldapUpdateMultiValueAttribute(multiple_value_attributes, multiple_value_attribute_name, attr_map, bLdapMatchIgnoreCase, out);
				multiple_value_attribute_name.clear();
				multiple_value_attributes.clear();
			}
			if(multiple_value_attribute_name.empty()){
				multiple_value_attribute_name=attribute_name;
			} 
			multiple_value_attributes.push_back(attribute);
		}
		else {
			if(!multiple_value_attributes.empty()){
				ldapUpdateMultiValueAttribute(multiple_value_attributes, multiple_value_attribute_name, attr_map, bLdapMatchIgnoreCase, out);
				multiple_value_attribute_name.clear();
				multiple_value_attributes.clear();
			}
			ldapUpdateSingleValueAttribute(attribute, attribute_name, attr_map, bLdapMatchIgnoreCase, out);
		}
	}
	if(!multiple_value_attributes.empty())
		ldapUpdateMultiValueAttribute(multiple_value_attributes, multiple_value_attribute_name, attr_map, bLdapMatchIgnoreCase, out);
	getLdapExecutor()->LdapExecute(distinquished_name, out);
	nSuccess++;
}


void TreeMapper::ldapUpdateSingleValueAttribute(CROMData*attribute, const std::string& attribute_name, LdapAttributeMap& attr_map, bool bLdapMatchIgnoreCase, FILE*out)
{
	if(out)fprintf(out,"	ldapUpdateAttribute: attribute_name=%s\n", attribute_name.c_str());
	LdapExecutor& ldap_exec = *getLdapExecutor();
	std::string value, cmd;
	bool isValueNull=true, isAttrCmdNull=true;
	AttrCommand att_command=att_replace;

	for(CROMData*child=attribute->GetFirstChild();child;child=child->GetNextSibling()){
		if(child->IsLeaf()){
			if(child->GetDisplayName().IsEmpty()){
				get_value(child, value, isValueNull);
			}
			else if(stricmp((const char*)child->GetDisplayName(),"Attribute command")==0){
				get_value(child, cmd, isAttrCmdNull);
				if(stricmp(cmd.c_str(),"Remove")==0)att_command=att_remove;
			}
		}
	}

	                            
	if(out)fprintf(out, "Process Attribute Command: %s, Value=%s, IsNull=%s\n", GetDisplayString(att_command).c_str(), value.c_str(), isValueNull?"yes":"no");
	switch(att_command){
	case att_remove:
		{
			const char*existing_value=attr_map.find_existing_value(attribute_name);
			if( existing_value ) {
				ldap_exec.LdapRemove(attribute_name, existing_value,out);
				attr_map.delete_attribute_value(attribute_name, existing_value);
			}
		}
		break;
	case att_replace:
		if(isValueNull)break;
		{
			if(isArray(value.c_str())) {
				LogAttributeArrayWarning(getViewLog(), attribute_name, value);
				break;
			}
			const char* existing_value=attr_map.find_existing_value(attribute_name);
			if(existing_value!=0){
				if ( !ValuesAreEqual(existing_value, value.c_str(), bLdapMatchIgnoreCase) ) {
					ldap_exec.LdapReplace(attribute_name, value, out);
				}
			}
			else {
				ldap_exec.LdapAdd(attribute_name, value, out);
			}
		}
		break;
	}
}

void TreeMapper::ldapUpdateMultiValueAttribute(const std::vector<CROMData*>attributes, const std::string& attribute_name, LdapAttributeMap& attr_map, bool bLdapMatchIgnoreCase, FILE*out)
{
	if(out)fprintf(out,"	ldapUpdateMultiValueAttribute: attribute_name=%s\n", attribute_name.c_str());
	LdapValueMap value_map(bLdapMatchIgnoreCase);

	                                                                            
	                                                                               
	                               
	                                                       
	bool isDeleteAll = false;
	for(vector<CROMData*>::const_iterator it=attributes.begin(); it!=attributes.end();++it){
		CROMData* attribute=*it;
		string value, old_value;
		AttrCommand att_command=att_replace;
		bool isValueNull=true, isOldValueNull=true, isAttrCmdNull=true, isReplaceAll=false;
		for(CROMData*child=attribute->GetFirstChild();child;child=child->GetNextSibling()){
			if(child->IsLeaf()){
				if(child->GetDisplayName().IsEmpty()){
					get_value(child, value, isValueNull);
				}
				else if(stricmp((const char*)child->GetDisplayName(),"Old value")==0){
					get_value(child, old_value, isOldValueNull);
				}
				else if(stricmp((const char*)child->GetDisplayName(),"Attribute command")==0){
					string cmd;
					get_value(child, cmd, isAttrCmdNull);
					if(stricmp(cmd.c_str(),"Remove")==0)att_command=att_remove;
					if(stricmp(cmd.c_str(),"Replace all")==0)isReplaceAll=true;
				}
			}
		}

		vector<string> vOldValue;
		if( isArray(old_value.c_str()) ) {
			parseArray(old_value.c_str(), vOldValue);
		}
		else if ( !isOldValueNull && !old_value.empty() ) {
			vOldValue.push_back(old_value);
		}

		switch(att_command){
			case att_remove:
				if ( isOldValueNull || vOldValue.empty() )
					isDeleteAll = true;

				                                                               
				if ( !isDeleteAll ) {
					                                              
					for(int k=0;true;k++){
						const char*existing_value=attr_map.find_existing_value(LdapAttributeMap::get_key(attribute_name,k));
						if(existing_value==0)break;
						if(ValueIsInArray(vOldValue, existing_value, bLdapMatchIgnoreCase))
							value_map.update_value_map(existing_value, bit_delete);
					}
				}
				break;
			case att_replace:
			{
				vector<string> vValue;
				if ( !isValueNull ) {
					if( isArray(value.c_str()) ) {
						parseArray(value.c_str(), vValue);
					}
					else {
						vValue.push_back(value);
					}
				}

				                             
				value_map.update_value_map(vValue, bit_add);

				                                                                                              
				if ( isReplaceAll ) {
					isDeleteAll = true;
				}

				                                                               
				if ( !isDeleteAll ) {
					                                                                                                                    
					for(int k=0;true;k++){
						const char*existing_value=attr_map.find_existing_value(LdapAttributeMap::get_key(attribute_name,k));
						if(existing_value==0)break;
						if(ValueIsInArray(vValue,existing_value,bLdapMatchIgnoreCase)){
							value_map.update_value_map(existing_value, bit_delete);
						}
					}

					                                                                 
					if(isOldValueNull)break;
					for(int k=0;true;k++){
						const char*existing_value=attr_map.find_existing_value(LdapAttributeMap::get_key(attribute_name,k));
						if(!existing_value)break;
						if(ValueIsInArray(vOldValue,existing_value,bLdapMatchIgnoreCase)){
							value_map.update_value_map(existing_value, bit_delete);
						}
					}
				}
				break;
			}
			default:
				throw CrException("Invalid attribute command in this context. Only remove and replace are allowed.");
		}
	}

	                                                                        
	LdapExecutor& ldap_exec = *getLdapExecutor();
	if ( isDeleteAll ) {
		                                                                                           
		                                                        
		vector<string> vec_replace;
		for(map<string,short>::iterator it=value_map.begin();it!=value_map.end();++it){
			if ( it->second & bit_add ) vec_replace.push_back(it->first);
		}
		if ( !vec_replace.empty() )
			ldap_exec.LdapReplace(attribute_name, vec_replace, out);
		else {
			vector<string> vec_delete;
			for(int k=0;true;k++){
				const char*existing_value=attr_map.find_existing_value(LdapAttributeMap::get_key(attribute_name,k));
				if(existing_value==0)break;
				vec_delete.push_back(existing_value);
			}
			if ( !vec_delete.empty() )
				ldap_exec.LdapDelete(attribute_name, vec_delete, out);
		}
	}
	else {
		vector<string> vec_add, vec_delete;
		for(map<string,short>::iterator it=value_map.begin();it!=value_map.end();++it){
			if(it->first.empty())continue;
			if( (it->second == bit_add) )vec_add.push_back(it->first);
			else if (it->second == bit_delete) vec_delete.push_back(it->first);
		}

		if(!vec_delete.empty()) ldap_exec.LdapDelete(attribute_name,vec_delete,out);
		if(!vec_add.empty()) ldap_exec.LdapAdd(attribute_name,vec_add,out);
	}
}

unsigned long TreeMapper::ldapConnect(LdapExecutor*p){
	OperationDataElementHolder* pDEHolder = getOperationDataElementHolder();
	try {
		const string psswrd(konga::DecryptString(m_sourceDbConnectionInfo.Password));
		if ( pDEHolder ) {
			konga::replaceGlobalDataElementsInDbLocationConnectionInfo(m_sourceDbConnectionInfo, *pDEHolder);
		}
		p->LdapConnect(m_sourceDbConnectionInfo.Server, m_sourceDbConnectionInfo.User, psswrd, 
			m_sourceDbConnectionInfo.Secured, m_sourceDbConnectionInfo.Port);
	}
	catch(const CrException& e){
		std::string stErrMsg = e.GetErrorString();
		AppendErrorMessage(stErrMsg.c_str());
		return E_FAIL;
	}
	return S_OK;
}

konga::LdapTreeNode* TreeMapper::loadLdapSource(konga::LdapExecutor*p, konga::LdapClassFilter*pClassFilter, FILE*out)
{
	OperationDataElementHolder* pDEHolder = getOperationDataElementHolder();
	konga::LdapSearchParameters ldapParams;
	ldapParams.includeDnInSearchResult = GetSystemVariableAsBool("etlforge.scripting.ldap.include_dn_in_results", pDEHolder, false);
	ldapParams.usePagedSearch = GetSystemVariableAsBool("etlforge.scripting.ldap.use_paged_search", pDEHolder, false);
	ldapParams.iMaxNumRecords = GetSystemVariableAsLong("etlforge.scripting.ldap.max_search_results", pDEHolder, -1);

	CROM*pStructRoot=m_sourceDTDParser->FindRoot();
	CROM*pCROM_Entry=pStructRoot->FindChildFolder(pClassFilter->getClassName().c_str());
	if(out)fprintf(out,"	process entry: %s()\n",(const char*)pCROM_Entry->GetDisplayName());
	ldapParams.scope = pClassFilter->getScope();
	std::vector<std::string> vAttr;
	for(CROM*child=pCROM_Entry->GetFirstChild();child;child=child->GetNextSibling()){
		if(child->IsElement()){
			vAttr.push_back((const char*)child->GetDisplayName());
		}
	}
	const string& filter = pClassFilter->getFilter();
	if(filter.empty())return 0;
	const string path( addBaseDN(pClassFilter->getPath(), m_sourceDbConnectionInfo.Path) );
	return p->LdapSearch(path, filter, vAttr, ldapParams, m_nChunkSize);
}



unsigned long TreeMapper::dumpLdapSourceToFile(const string& stSourceDataFileName){
	FILE*out=fopen(stSourceDataFileName.c_str(), "w+");
	if(out==0) {
		const long lErr = GetLastError();
		throw CrException("Failed to open LDAP source image file '" + stSourceDataFileName + "'. " + ErrorResolver(lErr).Resolve(lErr));
	}
	konga::AutoFileHandle adopt_out(out);

	FILE*debug_log=0;
	konga::AutoFileHandle adopt_debug_log(debug_log);
	if(getViewLog()->IsDebugLogEnabled()) {
		KString WDIDebugPath;
		if(WDIPathFinder::GetWDIDebugPath(WDIDebugPath)){
			WDIDebugPath+=getTransformationName__("_ldapSource");
			debug_log = fopen(WDIDebugPath, "w+");
			if(debug_log!=0)fprintf(debug_log,"dumpLdapSourceToFile(\"%s\"+)\n", stSourceDataFileName.c_str());
		}
	}
	OperationDataElementHolder* pDEHolder = this->getOperationDataElementHolder();

	CROM*pStructRoot=m_sourceDTDParser->FindRoot();
	CROMData*pDataRoot=get_SourceDataRoot();
	if (pDataRoot)
	{
		pDataRoot->Clear();
		pDataRoot->SetDTDNode(pStructRoot);
	}
	std::auto_ptr<LdapTreeNode> pResult;
	LdapAttributeMap attr_map(false);

	if ( pDEHolder ) {
		konga::replaceGlobalDataElementsInCollection(*m_pLdapFilterCollection, *pDEHolder);
	}

	for(LdapClassFilter*pClassFilter=m_pLdapFilterCollection->getFirstLdapClassFiler(); pClassFilter; pClassFilter=m_pLdapFilterCollection->getNextLdapClassFilter()){
		CROM*pCROM_Entry=pStructRoot->FindChildFolder(pClassFilter->getClassName().c_str());
		if(debug_log)fprintf(debug_log,"	process entry: %s()\n",(const char*)pCROM_Entry->GetDisplayName());

		konga::LdapTreeNode* pLdapTreeNode=getTransformationEngine()->retrieveLdapSource(this, pClassFilter, debug_log);
		if(pLdapTreeNode==0){
			return E_FAIL;
		}
		pResult.reset(pLdapTreeNode);
		if(pResult->empty())continue;
		for(konga::LdapTreeNode::iterator it_Entry=pResult->begin(); it_Entry!=pResult->end(); it_Entry++){
			LdapTreeNode* pTreeNode_Entry = *it_Entry;
			attr_map.reset(pTreeNode_Entry,0);

			for(CROM*pChild=pCROM_Entry->GetFirstChild(); pChild && pChild->IsLeaf(); pChild=pChild->GetNextSibling()){
				if(pChild->GetDisplayName()=="Distinguished name"){
					std::string value=pTreeNode_Entry->GetDistinguishedName();
					pChild->SetValue(value.c_str());
				}
			}
			LdapDataConverter::writeCromElement(out, pCROM_Entry);

			for(CROM*pChild=pCROM_Entry->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
				if(pChild->IsLeaf())continue;
				                    
				CROM* pCROM_Attribute=pChild;
				if(pCROM_Attribute->isLoopNode()){
					for(int k=0;true;k++){
						const char* value=attr_map.find_existing_value(LdapAttributeMap::get_key((const char*)pCROM_Attribute->GetDisplayName(),k));
						if(value==0)break;
						for(CROM*pCROM_Value=pCROM_Attribute->GetFirstChild(); pCROM_Value; pCROM_Value=pCROM_Value->GetNextSibling()){
							if(pCROM_Value->IsValueLeaf()){
								pCROM_Value->SetValue(value);
								break;
							}
						}
						LdapDataConverter::writeCromElement(out, pCROM_Attribute);
					}
				}
				else {
					const char* value=attr_map.find_existing_value((const char*)pCROM_Attribute->GetDisplayName());
					if( value && value[0] ) {                                      
						for(CROM*pCROM_Value=pCROM_Attribute->GetFirstChild();pCROM_Value;pCROM_Value=pCROM_Value->GetNextSibling()){
							if(pCROM_Value->IsValueLeaf()){
								pCROM_Value->SetValue(value);
								break;
							}
						}
						LdapDataConverter::writeCromElement(out, pCROM_Attribute);
					}
				}
			}
		}
	}

	return S_OK;
}

