/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stack>
#include <map>

#include <boost/scoped_array.hpp>

#include <konga/Server/Server.h>
#include <konga/Server/WDIPathFinder.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/util.h>			                  
#include <konga/FileTransfer/FileRetriever.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DataElement/DataElement.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationUtils/Definitions.h>

#include <konga/Xml/XmlUtils.h>

#include "ErrorCodes/resource.h"
#include "crom.h"
#include "resource.h"
#include "TreeMapper.h"
#include "DTDParser.h"
#include "SchemaCollection.h"
#include "DTDTokenize.h"
#include "TransFilterTab.h"
#include "TextDocument.h"
#include "XmlCromToJtrWriter.h"
#include "XsdParsingResult.h"
#include "JtrReader.h"
#include "Transform.h"
#include "CROMData.h"

#define TURN_OFF_ENTITY_NAME_IN_COMMENT
#define DEBUG_DTD 0
#define DEBUG_RESTORE_XPATH 0
#define DEBUG_LOG 0

using namespace std;
using namespace konga;
using namespace konga::xml;

                                                                             
            

void cleanupMap(std::map<std::string,CROM*>& map, const char*tag){
	for(std::map<std::string,CROM*>::iterator it=map.begin();it!=map.end();it++){
		CROM*crom=it->second;
		crom->DeleteNode();
	}
	map.clear();
}


                                                                          
                                                                         
                                  
bool DTDParser::IsSingleTable(KString &TableName)
{
	                                                                                            
	CROM* pRootNode = FindRoot();
	if(pRootNode==0)return false;

	CROM* child = pRootNode->GetFirstChild();
	if (0==child){
		                                           
		return false;
	}

	                                                                     
	                                              
	{
		CROM* next=child->GetNextSibling();
		while(next){
			                                                    
			if(next->GetDisplayName()!=child->GetDisplayName()){
				return false;
			}
			next=next->GetNextSibling();
		}
	}

	                                           
	CROM* grandChild=child->GetFirstChild();
	while (grandChild)
	{
		if((grandChild->m_Type&CROM_t_Element)!=0)return false;
		grandChild=grandChild->GetNextSibling();
	}
	
	                                                                                
	                                                                                   
	                                                                        
	                                                                                         
	                                    
	TableName=child->GetDisplayName();

	                                                                                       
	                                                                                 
	                                                                         
	if (TableName.IsEmpty())
	{
		CROM*field = child->GetFirstChild();
		if(0!=field)
		{
			KString sFieldName = field->GetName();
			int iDotPos = sFieldName.Find('.');
			if (-1 != iDotPos)
				TableName = sFieldName.Left(iDotPos);
		}
	}

	return true;
}

void DTDParser::definePath(bool bIsFlat){
	FindRoot()->definePath(bIsFlat);
}

CROM*DTDParser::FindRoot(){
	if(m_root)return m_root;
	int i,n=(int)m_obj_list.size();
	for(i=0;i<n;i++)
		if(m_obj_list[i])return m_obj_list[i];
	return 0;
}


void DTDParser::cleanup(){
	cleanupMap(m_ElementType_map,"m_ElementType_map");
	cleanupMap(m_Element_map, "m_Element_map");
	m_Names.clear();
	if(m_root!=0)m_root->DeleteNode();
	m_root=0;
}

DTDParser::~DTDParser()
{
	                                                                           
	if(m_SchemaParser.GetRoot()==0){
		cleanup();
	}
}

void DTDParser::DeleteRoot()
{
	if(m_root){
		m_root->DeleteNode();
		m_root = 0;
	}
}

int DTDParser::fill_children(CROM*node, int order){
	KString name=node->GetName();

	                                                
	bool bRecursive=false;
	CROM*parent=node->GetParent();
	while(parent){
		if(parent->GetName()==name){
			bRecursive=true;
			break;
		}
		parent=parent->GetParent();
	}
	if(bRecursive)return 0;

	if((node->m_Type&CROM_t_Element)!=0){
		int i,j,m,n;
		
		n=(int)m_obj_list.size();
		m=(int)m_att_list.size();

		CROM*obj=0,*att=0,*tmp;

		for(i=0;i<n;i++){
			tmp=m_obj_list[i];
			if(tmp&&name==tmp->m_Name){
				obj=tmp;
				break;
			}
		}
		for(j=0;j<m;j++){
			tmp=m_att_list[j];
			if(tmp&&name==tmp->m_Name){
				att=tmp;
				break;
			}
		}

		if(obj){
			CROM*clone=new CROM(obj),*cloned_child,*last_child=node->GetLastChild();

			                                                   
			cloned_child=clone->GetFirstChild();
			while(cloned_child)
			{
				cloned_child->SetParent(node);
				cloned_child->SetPreviousSibling(last_child);
				if(last_child==0)node->SetFirstChild(cloned_child);
				else last_child->SetNextSibling(cloned_child);
				last_child=cloned_child;
				cloned_child=cloned_child->GetNextSibling();
			}
			delete clone;
		}
		if(att){
			CROM*clone=new CROM(att),*cloned_child,
				*insert_child=0,*next_child,*first_child=node->GetFirstChild();
			if(first_child&&(first_child->m_Type&CROM_t_Value)!=0)insert_child=first_child;
			if(insert_child)next_child=insert_child->GetNextSibling();
			else next_child=first_child;
			                                                   
			cloned_child=clone->GetFirstChild();
			while(cloned_child)
			{
				cloned_child->SetParent(node);
				cloned_child->SetPreviousSibling(insert_child);
				
				if(insert_child==0)node->SetFirstChild(cloned_child);
				else insert_child->SetNextSibling(cloned_child);
				insert_child=cloned_child;
				cloned_child=cloned_child->GetNextSibling();
			}
			                     
			if(insert_child)insert_child->SetNextSibling(next_child);
			if(next_child)next_child->SetPreviousSibling(insert_child);
			delete clone;
		}

		CROM*parent=node->GetParent();                         
		while(parent){
			if(name==parent->m_Name){
				m_comment+=node->get_Path();
				m_comment+="\n";
				return 0;
			}
			parent=parent->GetParent();
		}
	}

	CROM*child=node->GetFirstChild();
	while(child){
		if(child->IsLeaf()==false){
			fill_children(child, order+1);
		}
		child=child->GetNextSibling();
	}
	return 0;
}

                                                                                                                                       
                                                                              
int DTDParser::Create(const char*structure_file_name,const char*root_str,const char*jtr_file_name,const char*type_str0)
{
	                                                                      

	KString type_str( type_str0==0 ? konga::GetFileExtension(structure_file_name).c_str() : type_str0 );

	int err=0;
	if(stricmp((const char*)type_str,"dtd")==0||stricmp((const char*)type_str,"xsd")==0)                                               
	{
		m_xml=true;
		                                               
		CreateCromFromJtr(jtr_file_name);
	}
	else                   
	{
		throw konga::CrException(
			"Unexpected call DTDParser::Create(Structure_file_name=\""+std::string(structure_file_name)+
			"\", root_str=\""+std::string(root_str)+
			"\", jtr_file_name=\""+std::string(jtr_file_name)+
			"\", type_str0=\""+std::string(type_str0)+
			"\")\nThis method can be called only with structure_file_name with extention of xsd or dtd."+
			" to create jtr_file");
	}

	return err;
}

namespace {
	KString get_string(const char*s0)
	{
		int i=(int)strcspn(s0,"\""),n;
		if(s0[i])n=(int)strcspn(&s0[i+1],"\"");
		else return "";
		return KString(&s0[i+1],n);
	}
}

namespace {
	KString get_2nd_string(const char*s0)
	{
		int i=(int)strcspn(s0,"\""),n;               
		if(s0[i])n=(int)strcspn(&s0[i+1],"\"");      
		else return "";
		i+=(n+1);
		if(s0[i])n=(int)strcspn(&s0[i+1],"\"");      
		else return "";
		i+=(n+1);
		if(s0[i])n=(int)strcspn(&s0[i+1],"\"");      
		else return "";
		return KString(&s0[i+1],n);
	}
	int findClosingAngleBracket(const char*buf, int i, int&err){
		int j=i+1,square_bracket=0;
		char c;
		for(;(c=buf[j])!='>'||square_bracket;j++)
		{
			if(c==0){
				err=READ_ERR_CLOSING_TAG_MISSING;
				break;
			}
			if(c=='['){
				square_bracket++;
				if(square_bracket>2){
					err=READ_SYNTAX_ERROR;
					break;
				}
			}
			if(c==']'){
				square_bracket--;
				if(square_bracket<0){
					err=READ_ERR_CLOSING_SQAURE_BRACKET_MISSING;
					break;
				}
			}
		}
		return j;
	}
}

void DTDParser::write(FILE*out)
{
	CROM* const obj = FindRoot();
	if(obj)
		obj->write(out);
}

int DTDParser::ProcessImportDTD(const char* buf, const char* pathname, KString& location, const bool ignore_missing_imports, bool& bHasMissingImports){
	                                                       
	int j, err=0;
	const string stImportedDTD(location);

	KString path_name(location);
	if((j=konga::strcspn_backward(path_name,"\\/"))<0){
		path_name=pathname;
		location=pathname+location;
	} else {
		path_name=KString(path_name,j+1);                                     
	}

	                                                                       

	if( stImportedDTD.find("://") != std::string::npos ) {
		                    
		konga::FileRetriever retriever;
		string stContents;
		if ( retriever.GetAsString(stImportedDTD, stContents) != 0 ) {
			err=-1;
			m_lastErr=retriever.LastError();
			return err;
		}
		if((err=ReadDtdDef(stContents.c_str(),path_name,false,ignore_missing_imports))!=0)return err;
	} else {
		FILE*f=fopen(location,"rb");
		if(f==0){
			bHasMissingImports = true;
			if ( ignore_missing_imports ) {
				                                         
				if ( !stImportedDTD.empty() )
					m_vMissingDtdFiles.push_back( stImportedDTD );
			}
			else {
				m_lastErr="LINKED_DTD_MISSING ";
				m_lastErr+=location;
				m_extra_dtd_files=location;
				err=READ_SYSTEM_LOCAL_ERR;
			}
			return err;
		}

		AutoFileHandle adopt_f(f);
		fseek(f,0L,SEEK_END);
		int n=ftell(f);
		if(n==0){
			m_lastErr="DTDParser Error, dtdfile \"";
			m_lastErr+=location;
			m_lastErr+="\" is empty";
			err=READ_SYSTEM_LOCAL_ERR;
			return err;
		}
		rewind(f);
		KString buffObj;
		char*buff=buffObj.GetBuffer(n+1);
		fread(buff,1,n,f);
		buff[n]=0;
		adopt_f.Close();
		location.Replace(pathname,"");
		m_extra_dtd_files+=location;
		m_extra_dtd_files+=";";
		err=ReadDtdDef(buff,path_name,false,ignore_missing_imports);                                 
		if ( !stImportedDTD.empty() )
			m_vExistingDtdFiles.push_back( stImportedDTD );
	}
	return err;
}
                                                                            
int DTDParser::ReadDtdDef(const char*buf, const char*pathname,bool contains_root,
						  const bool ignore_missing_imports)
{
	int i=0,j,k;

	int err=0;
	CROM*obj;
	bool bHasMissingImports = false;

	m_vExistingDtdFiles.clear();
	m_vMissingDtdFiles.clear();
	while(buf[i]){
		i+=(int)strspn(&buf[i]," \t\r\n");
		if(buf[i]==0)break;
		if(strncmp(&buf[i],"<!--",4)==0){                                         
			j=i+4;
			while(buf[j]!=0&&strncmp(&buf[j],"-->",3))j++;
			i=j+3;
		} else if(strncmp(&buf[i],"<!ELEMENT",9)==0||
			strncmp(&buf[i],"<!ATTLIST",9)==0||
			strncmp(&buf[i],"<!ENTITY",8)==0||
			strncmp(&buf[i],"<![",3)==0||
			strncmp(&buf[i],"<!NOTATION",10)==0                       
			){
			                                                 
			j=findClosingAngleBracket(buf,i, err);
			if(err)goto Exit;
			KString tmp(&buf[i],j-i+1);
			if(strncmp(tmp,"<![",3)==0){
				ProcessCondition((const char*)tmp,j-i+1,pathname,contains_root,ignore_missing_imports,err);
			}
			else if(strncmp(tmp,"<!ENTITY",8)==0){
				ProcessEntity((const char*)tmp,j-i+1,pathname,ignore_missing_imports,err);
			}
			else if((obj=ParseELEMENT(tmp,err))!=0){
				bool modified;
				obj->RemoveExtraNode(n_NoNameLoop,modified);
				if(contains_root)obj->m_Type |= CROM_t_CouldBeRoot;
				m_obj_list.push_back(obj); 
			}
			else if((obj=ParseATTLIST(tmp,err))!=0){
				m_att_list.push_back(obj);
			}
			if(err)goto Exit;
			i=j+1;
		} else if(buf[i]=='%'){                                                   
			k=(int)strcspn(&buf[i+1],";");
			if(k>1){
				KString key=KString(&buf[i+1],k);
				STRMAP_T::const_iterator cit=strMap.find(key);
				if(cit==strMap.end()){
					err=ENTITY_REPLACE_ERR;
					goto Exit;
				}
				bool isEmptyDef = false;
				KString value=cit->second, location;
				if(value.Find("\"./"))value.Replace("\"./","\"");
				if(strncmp((const char*)value,"SYSTEM ",7)==0){
					location=get_string(value);
				} else if(strncmp((const char*)value,"PUBLIC ",7)==0){
					location=get_2nd_string(value);
				} else {
					                                              
					isEmptyDef = true;
				}
				if(!isEmptyDef){
					err=ProcessImportDTD((const char*)value, pathname, location, ignore_missing_imports, bHasMissingImports);
					if(err!=0)goto Exit;
				}
			}
			i+=k+2;
		}
		else if(strncmp(&buf[i],"<!DOCTYPE",9)==0||
			strnicmp(&buf[i],"<HTML>",6)==0||
			strnicmp(&buf[i],"<HEAD>",6)==0||
			strnicmp(&buf[i],"<META",5)==0||
			strnicmp(&buf[i],"<BODY>",6)==0||
			strnicmp(&buf[i],"<XMP>",5)==0||
			strnicmp(&buf[i],"</HTML>",6)==0||
			strnicmp(&buf[i],"</HEAD>",6)==0||
			strnicmp(&buf[i],"<META",5)==0||
			strnicmp(&buf[i],"</BODY>",6)==0||
			strnicmp(&buf[i],"</XMP>",5)==0||
			strnicmp(&buf[i],"<?xml ",6)==0)
		{
			k=(int)strcspn(&buf[i],">");
			i+=k+1;
		}
		else if(strncmp(&buf[i],"SYSTEM",6)==0){
			KString location=get_string(&buf[i]);
			err=ProcessImportDTD(&buf[i], pathname, location, ignore_missing_imports, bHasMissingImports);
			goto Exit;
		}
		else if(strncmp(&buf[i],"PUBLIC",6)==0){
			KString location=get_2nd_string(&buf[i]);
			err=ProcessImportDTD(&buf[i], pathname, location, ignore_missing_imports, bHasMissingImports);
			goto Exit;
			
		}
		else
		{
			err=READ_SYNTAX_ERROR;
			goto Exit;
		}
	}
Exit:
	if(err)
	{
		KString tmp;
        if(err==-1)
            tmp=m_lastErr;
        else 
            tmp.LoadString(err);
		getTreeMapper()->getViewLog()->DebugString("%d Error Message: %s --- at\n", err, (const char*)tmp);
		int len=(int)strlen(&buf[i]);
		tmp=KString(&buf[i],len<500?len:500);
		getTreeMapper()->getViewLog()->DebugString("%s\n", (const char*)tmp);
		m_lastErr += "\nNear: " + tmp;
	}

	                                      
	if ( bHasMissingImports )
		err = READ_SYSTEM_LOCAL_ERR;
	return err;
}

CROM* DTDParser::ParseELEMENT(const char*str,int&err)
{
	unsigned long hr = S_OK;
	err=0;
	if(str==0||(strncmp(str,"<!ELEMENT ",10)!=0&&strncmp(str,"<!ELEMENT\t",10)!=0))return 0;
#ifdef DEBUG_LOG
	if(getTreeMapper()->m_debug_log)
	{
		fprintf(getTreeMapper()->m_debug_log,"ParseELEMENT(%s)\n",str);
		fflush(getTreeMapper()->m_debug_log);
	}
#endif
#if DEBUG_DTD
	getTreeMapper()->getViewLog()->DebugString("ParseELEMENT, str=%s\n",str?str:"");
#endif
	CROM*parent=0,*obj=0,*root=0;
	std::stack<CROM*>obj_stack;
	DTDTokenize t(str," \t\r\n>()*?+%|,#");
	const char*token;
	char c;

	std::stack<const char*>saving_string;
	while(t.NextToken()){
		token=(const char*)t.token;
		switch(c=token[0])
		{
		case '(':
			obj=new CROM(getTreeMapper(),parent,obj,0,0,CROM_t_Unknown,CROM::t_CROM_Xml);
			obj_stack.push(parent);
			parent=obj;
			obj=0;
			break;
		case ')':
			{
				if(obj_stack.size()==0){
					err=PARSE_ELEMENT_EXTRA_RP;
					goto Exit;
				}
				obj=parent;
				parent=obj_stack.top();
				obj_stack.pop();
			}
			break;
		case '*':
		case '?':
		case '+':
			{
				if(obj==0){
					err=PARSE_ELEMENT_SYNTAX_ERR;
					goto Exit;
				}
				int type=CROM_t_AtLeastOne;
				switch(c)
				{
				case '+': type=CROM_t_AtLeastOne; break;
				case '*': type=CROM_t_Collection; break;
				case '?': type=CROM_t_Optional; break;
				}
				obj->m_Type|=type;
			}
			break;
		case '|':
			{
				if(obj==0){
					err=PARSE_ELEMENT_SYNTAX_ERR;
					goto Exit;
				}
				parent->m_Type|=CROM_t_OneOf;
			}
			break;
		case '#':
			if(t.NextToken()&&strcmp(t.token,"PCDATA")==0){
				obj=new CROM(getTreeMapper(),parent,obj,0,0,CROM_t_Value,CROM::t_CROM_Xml);
				obj->m_Type|=CROM_t_Optional;
			}
			break;
		case ',':
		case ' ':
		case '\t':
		case '\r':
		case '\n':
			break;
		case '>':
			if(obj_stack.size()==0){
				err=PARSE_ELEMENT_EXTRA_RP;
				goto Exit;
			}
			obj_stack.pop();
			if(obj&&obj->m_Type==CROM_t_Unknown){
				                                        
				CROM*child=obj->GetFirstChild();
				parent->SetFirstChild(child);
				while(child){
					child->SetParent(parent);
					child=child->GetNextSibling();
				}
				delete obj;
				obj=0;
			}
			break;
		case '%':
			{
				const char*str=t.get_current_str();
				int k=(int)strcspn(str,";");
				if(k>0){
					std::string key(str,k);
					STRMAP_T::const_iterator cit=strMap.find(key.c_str());
					if(cit==strMap.end()){
						err=ENTITY_REPLACE_ERR;
						goto Exit;
					}					
					std::string value=std::string(cit->second);
					t.push_macro(key.c_str(),value.c_str(),&str[k+1]);
#if DEBUG_DTD
					getTreeMapper()->getViewLog()->DebugString("push_macro key=%s value=%s\n",key.c_str(),value.c_str());
#endif
				} else {                     
					err=ENTITY_REPLACE_ERR;
					goto Exit;
				}
			}
			break;
		default:
			if(strcmp(token,"EMPTY")==0){
				obj=parent;
			} else if(strcmp(token,"ANY")==0){
				if(parent==0){
					                        
					err=PARSE_ENTITY_SYNTAX_ERROR;
					goto Exit;
				}
				obj=parent;
				obj->AddType(CROM_t_AnyElement);
				new CROM(getTreeMapper(),obj,0,0,0,CROM_t_Value,CROM::t_CROM_Xml);
			} else if(strcmp(token,"<!ELEMENT")!=0){
				obj=new CROM(getTreeMapper(),parent,obj,token,0,CROM_t_Element,CROM::t_CROM_Xml);
				if(root==0){
					root=obj;
					parent=obj;
					obj_stack.push(parent);
					obj=0;					
				}
#ifndef TURN_OFF_ENTITY_NAME_IN_COMMENT
				const char*macro_name=t.get_definition_name();
				if(macro_name&&macro_name[0]){
					if(obj)obj->m_Comment=macro_name;
				}
#endif
			}

			break;
		}
	}
Exit:
	if(err)
	{
		KString tmp;
		tmp.LoadString(err);
		tmp+="\nThe parsing error occurred at the end of ";
		tmp+=t.get_tokenized_str()+"\n";
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_FAILED_PARSEELEMENT_FAILED, err, (const char*)tmp);
		m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();
	}
	return root;
}


CROM*DTDParser::ParseATTLIST(const char*str,int&err)
{
	unsigned long hr = S_OK;
	err=0;
	if(str==0||(strncmp(str,"<!ATTLIST ",10)!=0&&strncmp(str,"<!ATTLIST\t",10)!=0))return 0;
#ifdef DEBUG_LOG
	if(getTreeMapper()->m_debug_log){
		fprintf(getTreeMapper()->m_debug_log,"ParseATTLIST(%s)\n",str);
		fflush(getTreeMapper()->m_debug_log);
	}
#endif
	std::string tmp;
	CROM*parent=0,*obj=0,*obj0=0;
	std::stack<CROM*>parent_stack;
	int name_def=0;
	DTDTokenize t(str,">(|),# \t\r\n\"'%;");
	const char*token;
	bool flag;
	char c;
	while(t.NextToken()){
		token=(const char*)t.token;
		switch(c=token[0])
		{
		case '>':
			obj=obj0;
			parent_stack.pop();
			if(parent_stack.size()!=0){
				err=PARSE_ATTLIST_STACK_ERR;
				goto Exit;
			} else if(t.NextToken()!=0){
				err=PARSE_ATTLIST_MORE_TEXT;
				goto Exit;
			}
			break;
		case '(':                                                   
			if(obj!=0){
				std::string domain="|";
				char c;
				while((flag=t.NextToken())&&(c=t.token[0])!=')'){
					if(c!=' '&&c!='\t'&&c!='\r'&&c!='\n')
					domain+=t.token;
				}
				domain+='|';
				obj->set_Domain(domain.c_str());
			} else {
				while((flag=t.NextToken())&&t.token[0]!=')');
			}
			if(!flag){
				err=PARSE_ATTLIST_MISSING_RP;
				goto Exit;
			}
			break;
		case '|':                                               
			             
			break;
		case ')':                                                  
			                   
			break;
		case ',':
			break;
		case '#':
			if(t.NextToken()){
				if(strcmp(t.token,"IMPLIED")==0){
					if(obj==0){
						err=PARSE_ATTLIST_ERROR;
						goto Exit;
					}
					obj->m_Type|=CROM_t_Optional;
				}
			}
			break;
		case ' ':
		case '\t':
		case '\r':
		case '\n':
			break;
		case '"':
		case '\'':
			{
				KString tmp;
				while(t.NextToken()&&t.token[0]!=c)tmp+=t.token;
				if(obj)obj->setDefault(tmp);
			}
			break;
		case '%':
			if(t.NextToken()){
				tmp=t.token;
				STRMAP_T::const_iterator cit;
				if(!t.NextToken()||t.token[0]!=';'||(cit=strMap.find(tmp))==strMap.end()){
					err=ENTITY_REPLACE_ERR;
					goto Exit;
				}
				tmp=cit->second;
				token=tmp.c_str();
			}
			else {
				err=ENTITY_REPLACE_ERR;
				goto Exit;
			}
			            
		default:
			if(strcmp(token,"<!ATTLIST")&&                       
				strcmp(token,"ENTITY")&&
				strcmp(token,"ID")&&
				strcmp(token,"IDREF")&&
				strcmp(token,"IDREFS")&&
				strcmp(token,"NMTOKEN")&&
				strcmp(token,"NMTOKENS")&&
				strcmp(token,"ENTITIES")&&
				strcmp(token,"CDATA")&&
				strcmp(token,"PCDATA")
				){
				name_def++;
				if(name_def==1){
					obj0=new CROM(getTreeMapper(),parent,obj,token,0,CROM_t_Element,CROM::t_CROM_Xml);
					obj=obj0;
					parent_stack.push(parent);
					parent=obj;
					obj=0;
				} else {
					obj=new CROM(getTreeMapper(),parent,obj,token,0,CROM_t_Attribute,CROM::t_CROM_Xml);
				}
			}
			break;
		}
	}
Exit:
	if(err)
	{
		KString tmp;
		tmp.LoadString(err);
		tmp+="\nThe parsing error occurred at the end of ";
		tmp+=t.get_tokenized_str()+"\n";
		hr = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_PARSEATTLIST_FAILED, err, (const char*)tmp);
		m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();
	}
	return obj;
}

void DTDParser::ProcessCondition(const char*str, int n, const char*pathname, const bool contains_root, const bool ignore_missing_imports,int&err)
{
	err=0;
	char c;
	const char*token;
	DTDTokenize t(str,"[]");
	if(strncmp(str,"<![",3)==0){                               
		std::string condition, expr;
		int iwhere=0;
		while(t.NextToken()){
			token=t.token;
			bool isConditionDefined=false;
			switch(c=token[0]){
				case '[':
					iwhere++;
					if(iwhere==2)isConditionDefined=true;
					if(iwhere>2)expr+=token;
					break;
				case ']':
					if(iwhere>2)expr+=token;
					iwhere--;
					break;
				default:
					if(iwhere==1&&!isConditionDefined)condition+=token;
					else if(iwhere==2) expr+=token;
			}
		}

		                          
		t.init(condition.c_str()," %;");

		bool isInclude=false;
		while(t.NextToken()){
			token=t.token;
			switch(c=token[0])
			{
			case ' ':
				break;
			case '%':
				{
					if(t.NextToken()){
						std::string key=t.token;
						STRMAP_T::const_iterator cit;
						if(t.NextToken()&&t.token[0]==';'){
							if((cit=strMap.find(key))!=strMap.end()){
								token=(const char*)cit->second;							
							} else {
								err=ENTITY_REPLACE_ERR;
								goto Exit;
							}
						}
					} else {
						                             
						err=ENTITY_REPLACE_ERR;
						goto Exit;
					}
				}
				                
			default:
				isInclude=strcmp(token,"INCLUDE")==0;
				break;
			}
		}
		if(isInclude){
			err = ReadDtdDef(expr.c_str(),pathname, contains_root,ignore_missing_imports);
			if(err) goto Exit;
		}
	}
Exit:
	if(err)
	{
		KString tmp;
		tmp.LoadString(err);
		tmp+="\nThe parsing error occurred at the end of ";
		tmp+=t.get_tokenized_str()+"\n";
		getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_PROCESS_ENTITY_FAILED,err,(const char*)tmp);
		m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();
	}
}

void DTDParser::ProcessEntity(const char*str,int n,const char*pathname,const bool ignore_missing_imports,int&err)
{
	err=0;
	DTDTokenize t(str," \t\r\n%\"';>");
	if(strncmp(str,"<!ENTITY ",9)==0||strncmp(str,"<!ENTITY\t",9)==0){
		const char*token;
		char c,cc;
		KString key,value;
		bool flg;
		while(t.NextToken()){
			token=(const char*)t.token;
			switch(c=token[0])
			{
			case '%':                                              
				t.NextToken();
				if(t.token[0]!=' ')
				{
					err=PARSE_ENTITY_SYNTAX_ERROR;
					goto Exit;
				}
				break;
			case '"':	                  
			case '\'':
				while((flg=t.NextToken())&&(cc=t.token[0])!=c){                          
					if(cc=='%'){
						t.NextToken();
						KString key=t.token;
						t.NextToken();
						if(t.token[0]!=';'){
							err=ENTITY_REPLACE_ERR;
							goto Exit;
						}
						STRMAP_T::const_iterator cit=strMap.find(key);
						if(cit==strMap.end()){
							err=ENTITY_REPLACE_ERR;
							goto Exit;
						}
						value+=cit->second;
					} else {
						value+=t.token;
					}
				}
				if(!flg){                  
					err=PARSE_ENTITY_UNBALANCE_QUOTE;
					goto Exit;
				}
				if(strncmp(value,"SYSTEM ",7)==0)value+="\"";                   
				else if(strncmp(value,"PUBLIC ",7)==0)                         
				{
					while((flg=t.NextToken())&&(cc=t.token[0])!=c){;}                                           
					value+="\" \"";
					while((flg=t.NextToken())&&(cc=t.token[0])!=c){                                         
						if(cc=='%'){
							t.NextToken();
							KString key=t.token;
							t.NextToken();
							if(t.token[0]!=';'){
								err=ENTITY_REPLACE_ERR;
								goto Exit;
							}
							STRMAP_T::const_iterator cit=strMap.find(key);
							if(cit==strMap.end()){
								err=ENTITY_REPLACE_ERR;
								goto Exit;
							}
							value+=cit->second;
						} else {
							value+=t.token;
						}
					}
					value+="\"";
				}
				strMap[key]=value;
#if DEBUG_DTD
				getTreeMapper()->getViewLog()->DebugString("Enter ENTITY key=\"%s\", value=\"%s\"\n",key,value);
#endif
				break;
			case ' ':
			case '\t':
			case '\n':
			case '\r':
			case ';':
				break;
			case '>':
				goto Exit;
			default:
				if(strcmp(token,"<!ENTITY")!=0&&
					strcmp(token,"CDATA")!=0
					){
					if(strcmp(token,"SYSTEM")==0)
					{
						value="SYSTEM \"";
					}
					else if(strcmp(token,"PUBLIC")==0)
					{
						value="PUBLIC \"";
					}
					else if(key!="")
					{
						err=PARSE_ENTITY_SYNTAX_ERROR;
						goto Exit;
					} else {
						key=token;
						value="";
					}
				}
				break;
			}
		}
	}
Exit:
	if(err)
	{
		KString tmp;
		tmp.LoadString(err);
		tmp+="\nThe parsing error occurred at the end of ";
		tmp+=t.get_tokenized_str()+"\n";
		getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_PROCESS_ENTITY_FAILED,err,(const char*)tmp);
		m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();
	}
}

class ParentChild
{
public:
	KString m_ParentTable, m_ParentId, m_ChildTable, m_ChildId;

	ParentChild(KString*str);
};
ParentChild::ParentChild(KString*str)
{
	const char*s0=str[0],*s2=str[2];
	int l0=(int)strlen(s0),l2=(int)strlen(s2);
	while(l0>0&&s0[l0-1]==' ')l0--;
	while(l2>0&&s2[l2-1]==' ')l2--;
	m_ParentTable=KString(s0,l0);
	m_ParentId=str[1];
	m_ChildTable=KString(s2,l2);
	m_ChildId=str[3];
}

namespace {
	bool inside(std::vector<int>*array,int k){
		if(array==0)return false;
		int i,n=(int)array->size();
		for(i=0;i<n;i++){
			if(array->at(i)==k)return true;
		}
		return false;
	}
}

CROM*DTDParser::FindLinkParent(const char*table_name,std::vector<int>*index_for_linked_objs,CROM*obj)
{
	KString tname=CROM::convertIllegalCharacters(table_name);

	CROM*parent=obj;
	while(parent){
		if(parent->m_Name==tname||strcmp(parent->m_Name,table_name)==0)return parent;
		parent=parent->GetParent();
	}

	int i,n=(int)m_obj_list.size();

	for(i=0;i<n;i++){
		if(m_obj_list[i]==0)continue;
		if(m_obj_list[i]->m_Name==tname||strcmp(m_obj_list[i]->m_Name,table_name)==0){
			if(!inside(index_for_linked_objs,i)){
				obj=m_obj_list[i];
				return obj;
			}
		}
	}
	for(i=0;i<n;i++){
		if(m_obj_list[i]==0)continue;
		if(m_obj_list[i]->m_Name==tname||strcmp(m_obj_list[i]->m_Name,table_name)==0){
				obj=m_obj_list[i];
				return obj;
		}
	}
	return 0;
}
CROM*DTDParser::FindLinkChild(const char*table_name,std::vector<int>*index_for_linked_objs,int&index)
{
	KString tname=CROM::convertIllegalCharacters(table_name);
	int i,n=(int)m_obj_list.size();
	for(i=0;i<n;i++){
		if(m_obj_list[i]==0)continue;
		if(m_obj_list[i]->m_Name==tname||strcmp(m_obj_list[i]->m_Name,table_name)==0){
			if(!inside(index_for_linked_objs,i)){
				index=i;
				return m_obj_list[i];
			}
		}
	}
	return 0;
}

int DTDParser::DefinePrimaryKey(CROM*tableNode,const char*keyStr)             
{
	DTDTokenize t(keyStr,",");
	const char*token;
	
	CROM*child;
	while(t.NextToken()){
		token=t.token;
		if(token[0]!=','){
			child=tableNode->FindObjectInChild(token,CROM_t_Attribute);
			if(child==0){
				return DEFINE_PRIMARY_KEY_FAIL;
			}
			child->m_Type|=CROM_t_PrimaryKey;
		}
	}
	return 0;
}

int DTDParser::DefineUpdateKey(CROM*tableNode,const char*keyStr)             
{
	DTDTokenize t(keyStr,",");
	const char*token;
	
	CROM*child;
	while(t.NextToken()){
		token=t.token;
		if(token[0]!=','){
			child=tableNode->FindObjectInChild(token,CROM_t_Attribute);
			if(child==0){
				return DEFINE_PRIMARY_KEY_FAIL;
			}
			child->m_Type|=CROM_t_UpdateKey;
		}
	}
	return 0;
}


int DTDParser::DefineForeignKey(CROM*tableNode,const char*pkeyStr,const char*fkeyStr)             
{
	DTDTokenize pt(pkeyStr,",");
	DTDTokenize ft(fkeyStr,",");
	const char*ptoken;
	KString ftoken0,ftoken;
	CROM*child;
	bool isOuterJoin;
	while(ft.NextToken()&&pt.NextToken()){
		ptoken=pt.token;
		ftoken=ft.token;
		isOuterJoin=false;
		if(ftoken.Right(1)=="*"){
			ftoken0=ftoken;
			isOuterJoin=true;
			int len=(int)ftoken0.GetLength();
			ftoken=ftoken0.Left(len-1);
		}
		if(ftoken[0]!=','){
			child=tableNode->FindObjectInChild(ftoken,CROM_t_Attribute);
			if(child==0){
				return DEFINE_FOREIGN_KEY_FAIL;
			}
			if(isOuterJoin){
				child->m_Type|=CROM_t_OuterJoin;
				tableNode->m_Type|=CROM_t_OuterJoin;
			}
			child->m_Type|=CROM_t_ForeignKey;
			child->set_LinkToPrimaryKey(ptoken);
		}
	}
	return 0;
}

int StringToVector(const char*strParentId,std::vector<KString*>& vId)
{
    konga::StringTokenizer tk(strParentId,",");
    int n=0;
    while(tk.NextToken()){
        const char*str=tk.token();
        if(str[0]==',')continue;
        vId.push_back(new KString(str));
        n++;
    }
    return n;
}

KString VectorToString(std::vector<KString*>&vId)
{
    KString s="";
    for(std::vector<KString*>::iterator it=vId.begin(); it!=vId.end(); it++){
        if(!s.IsEmpty())s+=",";
        s+=**it;
    }
    return s;
}


int DTDParser::AddLink(CROM*parent,CROM*child,
			const char*parentId,const char*childId)              
{
	int err=0;
	std::vector<KString*>pId,cId;
	int n_pId=StringToVector(parentId,pId),n_cId=StringToVector(childId,cId);
	if(n_pId==n_cId)
	{
		KString tmp;
		tmp=VectorToString(pId);
		CROM*last_sibling=parent->GetLastChild();
		if(last_sibling)last_sibling->SetNextSibling(child);
		else parent->SetFirstChild(child);
		child->SetParent(parent);
		child->SetPreviousSibling(last_sibling);
		child->SetNextSibling(0);

		int n1=(int)pId.size(),n2=(int)cId.size();
		if(n1==n2){
			for(int i=0;i<n1;i++)
			{
				KString*p_str=pId.at(i);
				CROM*obj=parent->FindObjectInChild(*p_str,CROM_t_Attribute);
				if(obj==0){
					err=DTDPARSERADDLINK_PARENT_INDEX_MISSING;
					break;
				}
				KString childName=*cId.at(i);
				if(childName.Right(1)=="*"){
					childName=childName.Left(childName.GetLength()-1);
				}
				obj=child->FindObjectInChild(childName,CROM_t_Attribute);
				if(obj){
					KString tmp;
					tmp.Format(" [Link %s]",(const char*)(*p_str));
					obj->set_ValueType((const char*)(obj->GetValueType()+tmp));
				} else {
					err=DTDPARSERADDLINK_CHILD_INDEX_MISSING;
					break;
				}
			}
		} else {
			err=DTDPARSERADDLINK_MISMATCH_INDEX;
		}
	}
	else
	{
		err=DTDPARSER_ADDLINK_MISMATCH_ID;
	}

	for(int i=0,n=(int)pId.size();i<n;i++)delete pId.at(i);
	for(int i=0,n=(int)cId.size();i<n;i++)delete cId.at(i);
	return err;
}

bool DTDParser::IsFlatFile()
{
	if(m_flat>0)return true;
	if(m_flat<0)return false;
	CROM*root = FindRoot();
	if (0 == root){
		m_flat=1;
		return true;
	}
	if(root->IsXml()){
		m_flat=-1;
		return false;
	}

	CROM* rootChild = root->GetFirstChild();
	if (0 == rootChild){
		m_flat=1;
		return true;
	}

	KString csTableName;
	bool bFlat=root->m_Name!=COMPLEXT_TEXT_ROOT && IsSingleTable(csTableName);

	if(rootChild&&(bFlat||rootChild->GetName()=="")){
		m_flat=1;
		return true;
	}

	m_flat=-1;
	return false;
}

CROM*DTDParser::FindTable(const char*table_name){
	CROM*root=FindRoot();
	CROM*table;
	if((table=root->FindFirstObject(table_name))!=0)return table;
	return 0;
}

CROM*DTDParser::FindFlatNode(){
	return FindRoot()->GetFirstChild();
	return 0;
}

             
int DTDParser::RestoreXPath(const char*path0,const char*path,
	std::vector<std::string>&str_vector,std::vector<CROM*>&node_vector)
{
#if DEBUG_RESTORE_XPATH
	getTreeMapper()->getViewLog()->DebugString("RestoreXPath path0=\"%s\", path=\"%s\"\n",
		path0,path);
#endif
	std::string str;
	int err=0;
	if(path==0)path="";
	DTDTokenize t(path,"$.");
	CROM*root=FindRoot(),*p=root,*p_saved=root;
	if(p==0){
		err=MISSING_ROOT;
		return err;
	}
	KString token;
	char c;
	if(path0==0||path0[0]==0){
		str="/";
	} else {
		DTDTokenize t0(path0,"$.");
		while(t0.NextToken()&&t.NextToken()){
			if(strcmp(t0.token,t.token)!=0)break;
			token=t0.token;
			if((c=token[0])!='$'&&c!='.'){
				if(p!=root||strcmp(token,root->m_Name)!=0)
					p=p->FindObjectInChild(stripInstanceTag(token),CROM_t_AnyThing);
				if(p==0){
					return err=1;
				}
			}
		}
		p_saved=p;
		if(t0.token[0]==0){
			str="./";
		} else {
			do {
				token=t0.token;
				if((c=t0.token[0])!='$'&&c!='.'){
					if(p!=root||strcmp(t0.token,root->m_Name)!=0)
						p=p->FindObjectInChild(stripInstanceTag(token),CROM_t_AnyThing);
					if(p==0){
						return err=2;
					}
					if(!p->GetDisplayName().IsEmpty()){
						str+="../";
					}
				}
			} while(t0.NextToken());
		}
	}

	p=p_saved;
	do {
		if(t.token==0){
			t.NextToken();
		}
		token=t.token;
		if((c=token.GetAt(0))!='$'&&c!='.'){
			if(p!=root||strcmp(token,root->m_Name)!=0){
				CROM*p0=p;
				p=p0->FindObjectInChild(stripInstanceTag(token),CROM_t_AnyThing);
				if(p==0)p=p0->FindFirstObject(stripInstanceTag(token),CROM_t_AnyThing);
			}
			if(p==0)return err=3;
			if(!p->GetDisplayName().IsEmpty()){
				if(hasInstanceTag(token)){
					str+=token;
					str+="/";
				} else if((p->m_Type&CROM_t_Element)!=0){
					str+=p->GetDisplayName();
					str+="/";
				} else {
					str+="@";
					str+=p->GetDisplayName();
				}
				if((p->m_Type&CROM_t_Element)!=0&&!p->m_Comment.IsEmpty()){                               
					node_vector.push_back(p);
					str_vector.push_back(str);
					str="./";
#if DEBUG_RESTORE_XPATH
					getTreeMapper()->getViewLog()->DebugString("vector pushed path=\"%s\", node=%s\n",
						str.c_str(),p?p->m_Name:"0");
#endif
				}
			}
		}
	} while(t.NextToken());

	if(strcmp(str.c_str(),"./")!=0){
		str_vector.push_back(str);
		node_vector.push_back(0);
#if DEBUG_RESTORE_XPATH
		getTreeMapper()->getViewLog()->DebugString("Last pushed path=\"%s\", node=%s\n",
			str.c_str(),"0");
#endif
	}
	return 0;
}


CROM* DTDParser::findSourceLeaf(const char* str)
{
	CROM*root=FindRoot(),*node=0,*parent=root,*attribute=0;
	if(parent==0)return 0;
	konga::StringTokenizer tk(str,"$.");
	const char*token;
	                                  
	if(tk.NextToken()){
		token=tk.token();
		KString rootName(root->GetName());
		if(rootName.IsEmpty()){
			node=root->FindObjectInChild(token,IsFlatFile()?CROM_t_AnyThing:CROM_t_Element);
		}
		else if(rootName==token){
			node=root;
		}
	}
	if(node==0)return 0;
	while(tk.NextToken()){
		const char*token=tk.token();
		switch(token[0]){
		case '.':
		case '$':
			parent=node;
			node=0;
			attribute=0;
			break;
		default:
			if(parent){
				node=parent->FindObjectInChild(token,CROM_t_NonLeaf);
				attribute=parent->FindObjectInChild(token,CROM_t_Attribute);
				if(node==0&&attribute==0){
					node=parent->FindObjectInChild(token,CROM_t_AnyThing);
				}
				if(node==0&&parent==root&&strcmp(token,parent->m_Name)==0){
					node=parent;
				}
			}
			break;
		}
	}
	if(attribute!=0)return attribute;
	if(node==0&&parent)node=parent->FindFirstObject("",CROM_t_Value);
	return node;
}

CROM* DTDParser::findSourceLoopNode(const char* str,int&err)
{
	CROM*root=FindRoot(),*node=0,*parent=root;
	err=0;
	if(parent==0){
		err=SOURCE_CROM_ROOT_NOT_FOUND;
		return 0;
	}
	konga::StringTokenizer tk(str,"$.#");
	const char*token;
	                                  
	if(tk.NextToken()){
		token=tk.token();
		KString rootName(root->GetName());
		if(rootName.IsEmpty()){
			node=root->FindObjectInChild(token,IsFlatFile()?CROM_t_AnyThing:CROM_t_Element);
		}
		else if(rootName==token){
			node=root;
		}
	}
	while(tk.NextToken()){
		std::string token=tk.token();
		const char* c1=tk.peek_next_token();
		bool isLoop=c1!=0&&(*c1=='.'||*c1=='#');
		switch(token.at(0)){
		case '.':
		case '$':
			if(node==0){
				err=FIND_SOURCE_LOOP_NODE_ERR1;
				return 0;
			} else if(!node->IsLeaf()){
				parent=node;
				node=0;
			} else if(tk.NextToken()){
				err=FIND_SOURCE_LOOP_NODE_ERR1;
				return 0;
			} else {
				return parent->GetLoopNode();
			}
			break;
		case '#':
			return parent->GetLoopNode();
		default:
			bool bIsLastToken=!tk.NextToken();
			if(!bIsLastToken)tk.StepBack();
			if(parent->GetFirstChild()==0){
				parent->copyRecursiveParent(m_ElementType_map, m_Element_map);
			}
			if(bIsLastToken){
				node=parent->FindObjectInChild(token.c_str(),CROM_t_AttValuePrecondition);
			}
			else {
				node=parent->FindChildFolder(token.c_str(), isLoop);
			}
			break;
		}
	}
	return parent->GetLoopNode();
}

CROM* DTDParser::findTargetNode(const char* str0)
{
	KString str;
	bool folder=false;
	if(str0[0]=='['){
		str=KString(&str0[1],strlen(str0)-2);
		folder=true;
	}
	CROM*parent=FindRoot(),*node=0;
	if(parent==0){
		return 0;
	}
	konga::StringTokenizer tk(str,"$.");
	while(tk.NextToken()){
		const char*token=tk.token();
		switch(token[0]){
		case '.':
		case '$':
			if(node==0){
				throw konga::CrException(std::string("Find target node failed, syntax error. ")+(const char*)str);
			}
			parent=node;
			node=0;
			break;
		default:
			node=parent->FindFirstObject(token,CROM_t_AnyThing);
			break;
		}
	}
	if(folder)return parent;
	else if(node==0)node=parent->FindFirstObject("",CROM_t_Value);
	return node;
}

CROM* DTDParser::findTargetNode_(const char* str,KString&tmp)
{
	CROM*parent=FindRoot(),*node=0,*root=parent;
	tmp="";
	konga::StringTokenizer tk(str,"$.");
	const char*token;
	                                  
	if(tk.NextToken()){
		token=tk.token();
		KString rootName(root->GetName());
		if(rootName.IsEmpty()){
			node=root->FindObjectInChild(token,IsFlatFile()?CROM_t_AnyThing:CROM_t_Element);
		}
		else if(rootName==token){
			node=root;
		}
	}
	if(node==0)return 0;
	tmp=token;

	while(tk.NextToken()){
		token=tk.token();
		switch(token[0]){
		case '.':
		case '$':
			parent=node;
			node=0;
			break;
		default:
			node=parent->FindObjectInChild(token,CROM_t_Element);
			if(node==0)node=parent->FindObjectInChild(token,CROM_t_AnyThing);
			if(node==0)return parent;
			break;
		}
		tmp+=token;
	}
	return parent;
}

KString DTDParser::stripInstanceTag(const KString&path)
{
	konga::StringTokenizer tk(path,"#.$");
	bool isInstanceTag=false;
	KString newPath;
	while(tk.NextToken()){
		const char*t=tk.token();
		switch(t[0]){
		case '#':
			isInstanceTag=true;
			break;
		case '.':
		case '$':
			newPath+=t;
			isInstanceTag=false;
			break;
		default:
			if(isInstanceTag==false)newPath+=t;
			isInstanceTag=false;
			break;
		}
	}
	return newPath;
}

int DTDParser::findGenerators(CROM*target){
	if(target==0)return 0;
	                                                                          
	                                                                                               
	int err;
	KString tmp;
	CROM* targetLoopNode=target->GetLoopNode();
	if(targetLoopNode){
		CROM* parent = targetLoopNode->GetParent();
		if(parent){
			parent=parent->GetLoopNode();
			if(parent->m_Generator==0)parent->m_Generator=FindRoot();
			err=targetLoopNode->updateGenerator(parent->m_Generator);
			if(err){
				m_conflict_target_loop_node=targetLoopNode;
				tmp.LoadString(err);
				m_lastErr.Format((const char*)tmp,
					(const char*)(targetLoopNode->m_Generator?targetLoopNode->m_Generator->get_Path():"<null>"),
					(const char*)(parent->m_Generator?parent->m_Generator->get_Path():"<null>"));
				return err;
			}
		}
		CTransform*pTrans = target->getTrans();
		for(int i=0, n=pTrans->getInputDEListSize(); i<n; ++i){
			const char* sourceDE = pTrans->getInputDEOf(i)->getName();
			CROM* sourceLoopNode = findSourceLoopNode(sourceDE, err);
			if(err){
				m_conflict_target_loop_node=targetLoopNode;
				tmp.LoadString(err);
				m_lastErr.Format("%s %s\n", (const char*)tmp, sourceDE);
				return err;
			}
			err=targetLoopNode->updateGenerator(sourceLoopNode);
			if(err){
				m_conflict_target_loop_node=targetLoopNode;
				tmp.LoadString(err);
				m_lastErr.Format((const char*)tmp,
					(const char*)(targetLoopNode->m_Generator?targetLoopNode->m_Generator->get_Path():"<null>"),
					(const char*)(sourceLoopNode?sourceLoopNode->get_Path():"<null>"));
				return err;
			}
		}
	}
	
	for(CROM*child=target->GetFirstChild(); child; child=child->GetNextSibling()){
		if((child->m_Type&CROM_t_AttValuePrecondition)==0){
			err=findGenerators(child);
			if(err)return err;
		}
		
	}
	return 0;
}


                                     
               

                                                                             
                                                                                      
                                                                                      
                                                                                    
               
void DTDParser::MarkOrderByColumns(const char* szSqlStmt)
{
	KString csSqlStmt(szSqlStmt);
	csSqlStmt.MakeUpper();
	int iPos = csSqlStmt.Find("ORDER BY");
	while(iPos != -1)
	{
		csSqlStmt.Delete(0, iPos+strlen("ORDER BY"));
		konga::StringTokenizer strTok(csSqlStmt, " ,");
		KString token;
		bool bStop = false;
		while(!bStop && strTok.NextToken())
		{
			token = strTok.token();
			switch(token[0])
			{
			case ' ':
			case ',':
				break;
			default:
				if(strcmp(token, "ASC")==0 || strcmp(token,"DESC")==0)
					break;
				                                                                            
				CROM* pColumn = FindColumnInFlatDB(token);
				if(pColumn == 0)
					bStop = true;
				else
					pColumn->AddSQLFilter("ORDER BY");	                                                   
														                                 
			}
		}
		iPos = csSqlStmt.Find("ORDER BY", iPos+8);
	}
}


CROM* DTDParser::FindColumnInFlatDB(const char* szColName)
{
	if(m_root == 0)
		return 0;

	KString csColName(szColName);
	csColName.Delete(0, csColName.Find('.')+1);

	CROM* pColumn = m_root->GetFirstChild();
	while(pColumn != 0 && pColumn->GetDisplayName().IsEmpty())
		pColumn = pColumn->GetFirstChild();

	while(pColumn != 0)
	{
		if(stricmp(pColumn->GetDisplayName().Mid(pColumn->GetDisplayName().Find('.')+1), csColName) == 0)
			break;
		pColumn = pColumn->GetNextSibling();
	}
	return pColumn;
}

void DTDParser::DelUnMappedColumns(bool& bDeletedColumn, bool bSourceIsFlat            )
{
	getTreeMapper()->getViewLog()->DebugString("DB as Source: Delete unnecessary columns from source structure tree");
	bDeletedColumn = false;
	if(m_root != 0)
	{
		                                        
		CROM* tmp = m_root->DelUnMappedColumns(bDeletedColumn, bSourceIsFlat);
	}
	if(!bDeletedColumn)
		getTreeMapper()->getViewLog()->DebugString("   No columns were deleted.");
}

KString DTDParser::CreateDbSelectList(const std::string&sBeginName,const std::string&sEndName)
{
	KString csSelect = "";
	if(m_root && m_root->GetFirstChild())
	{
		CROM* pTableNode=m_root->GetFirstChild();
		KString csColName, csTableName=pTableNode->get_sqlTableName();		
		for(CROM* pColumn = pTableNode->GetFirstChild(); pColumn && pColumn->IsLeaf();
			pColumn = pColumn->GetNextSibling())
		{
			if(pColumn->IsAttribute()){
				csColName = pColumn->GetDisplayName();
				if(!csSelect.IsEmpty())csSelect += ", ";
				csSelect += csTableName + "." + sBeginName + csColName + sEndName;
			}
		}
	}

	if(csSelect.IsEmpty())
		csSelect = "*";

	return csSelect;
}

unsigned long DTDParser::DoSchemaValidation(const string& stXmlFilePath, const string& stSchemaFilePath, string& sErr)
{
	bool valid = konga::XmlUtils::ValidateXmlInstanceAgainstSchema(stXmlFilePath, stSchemaFilePath, m_root->GetNamespace(), sErr);
	return valid ? S_OK : E_FAIL;
}

int DTDParser::parseDtd(const char *infile,const char *root_str, bool root_selected){
	int err=0;
	m_xml=true;
	KString path_name;
	try
	{
		FILE* f=fopen(infile,"rb");
		if(f==0){
			err = getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_DTD_PARSER_CREATE_OPEN_FILE_FAILED, infile);
			m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();
			m_extra_dtd_files=infile;
			return err;
		}

		konga::AutoFileHandle adopt_f(f);
		fseek(f,0L,SEEK_END);
		const int n=ftell(f);
		rewind(f);
		if(n==0){
			m_lastErr="DTDParser Error, dtdfile \"";
			m_lastErr+=infile;
			m_lastErr+="\" is empty";
			if(err)
			{
				getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_DTD_PARSER_REWIND_FILE_FAILED, infile);
				m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();
			}
			return err;
		}
		KString buffObj;
		char*buff=buffObj.GetBuffer(n+1);
		fread(buff,1,n,f);
		buff[n]=0;
		adopt_f.Close();

		const int last_slash=konga::strcspn_backward(infile,"/\\");
		if(last_slash>=0)path_name=KString(infile,last_slash+1);
		bool contains_root=true;
		err=ReadDtdDef(buff,path_name,contains_root);
		if(err)
		{
			getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_DTD_PARSER_READDTDDEF_FAILED, (const char*)KString(buff,100));
			return err;
		}
	}
	catch(const CrFatalException& e)
	{
		throw e;
	}
#ifndef _DEBUG
	catch(...)
	{
		KString tmp;
		tmp.LoadString(err);
		getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_DTD_PARSER_EXCEPTION, err, (const char*)tmp, (const char*)path_name);
		m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();
		return err;
	}
#endif
	int n=(int)m_obj_list.size(), m=(int)m_att_list.size();

#ifdef DEBUG_LOG
	if(getTreeMapper()->m_debug_log!=0){
		for(int i=0;i<n;i++){
			fprintf(getTreeMapper()->m_debug_log,"\ni=%d\n",i);
			if(m_obj_list[i])m_obj_list[i]->write(getTreeMapper()->m_debug_log);
		}
	}
	if(getTreeMapper()->m_debug_log!=0){
		for(int i=0;i<m;i++){
			fprintf(getTreeMapper()->m_debug_log,"\ni=%d\n",i);
			if(m_att_list[i])m_att_list[i]->write(getTreeMapper()->m_debug_log);
		}
	}
#endif

	for(int i=0;i<m;i++){                                               
		if(m_att_list[i]){
			KString name=m_att_list[i]->GetName();
			for(int j=i+1;j<m;j++){
				if(m_att_list[j]==0)continue;
				if(m_att_list[j]->GetName()==name){
					CROM*parent=m_att_list[i],
						*last_child=parent->GetLastChild(),
						*adopted_child=m_att_list[j]->GetFirstChild();

					while(adopted_child){
						adopted_child->SetParent(parent);
						adopted_child->SetPreviousSibling(last_child);
						if(last_child==0)parent->SetFirstChild(adopted_child);
						else last_child->SetNextSibling(adopted_child);
						last_child=adopted_child;
						adopted_child=adopted_child->GetNextSibling();
					}
					delete m_att_list[j];
					m_att_list[j]=0;
				}

			}
		}
	}

	if(root_str&&root_str[0]!=0){                                        
		int i;		
		for(i=0;i<n;i++){
			                                                                                             
			if(strcmp(root_str,m_obj_list[i]->GetDisplayName())==0)break;
		}
		if(i>=n){
			err=ROOT_NAME_NOT_FOUND;
			m_lastErr.LoadString(err);
			m_lastErr+=" root_name=";
			m_lastErr+=root_str;
		} else {
			m_root=new CROM(getTreeMapper(),0, 0, root_str, 0,
				CROM_t_Element, CROM::t_CROM_Xml);
			err=fill_children(m_root, 0);
			if(err){
				err=FILL_CHILDREN_ERR;
				m_lastErr.LoadString(err);
				m_root=0;
			} else {
				if(m_bIsTarget)m_root->AddConditions();
			}
		}
		                  
		for(int j=0;j<m;j++){
			if(m_att_list[j]!=0)m_att_list[j]->DeleteNode();
			m_att_list[j]=0;
		}
		m_att_list.clear();
		for(i=0;i<n;i++){
			if(m_obj_list[i]!=0){
				m_obj_list[i]->DeleteNode();
				m_obj_list[i]=0;
			}
		}
	} else {               
		                                
		for(int i=0;i<n;i++){
			if(m_obj_list[i]==0)continue;
			for(int j=i+1;j<n;j++){
				if(m_obj_list[j]==0)continue;
				if(strcmp(m_obj_list[i]->m_Name,m_obj_list[j]->m_Name)==0){
					if(m_obj_list[j]->CouldBeRoot()){
						m_obj_list[i]->m_Type |= CROM_t_CouldBeRoot;
					}
					m_obj_list[j]->DeleteNode();
					m_obj_list[j]=0;
				}
			}
		}
		                                                                                   
		for(int i=0,k;i<n;i++){
			k=0;
			if(m_obj_list[i]==0)continue;
			for(int j=0;j<n;j++){
				if(i!=j&&m_obj_list[j]){
					k+=m_obj_list[j]->Exploding(m_obj_list[i],0,err);
				}
				if(err){
					if(err==EXPLODING_EXCEED_MAX_ORDER){
						FILE*out=getTreeMapper()->m_debug_log;
						if(out!=0){
							fprintf(out,"Tree to deep\n");
							m_obj_list[i]->write(out);
						}
					}
					m_lastErr.LoadString(err);
					return err;
				}
			}
			if(root_selected){                                                                      
				if(k>0)
				{
					m_obj_list[i]->DeleteNode();
					m_obj_list[i]=0;
				}
			}
		}
		if(!root_selected){                                                  
			return err;
		}

		for(int i=0;i<m;i++)
		{
			if(m_att_list[i]){
				for(int j=0;j<n;j++)
				{
					if(m_obj_list[j])m_obj_list[j]->ExplodingAttribute(m_att_list[i],0,err);
					if(err){
						m_lastErr.LoadString(err);
						return err;
					}
				}
				m_att_list[i]->DeleteNode();
				m_att_list[i]=0;
			}
		}
		m_att_list.clear();
		                                                           
		
		for(int i=0;i<n;i++){
			CROM*root=m_obj_list[i];
			if(root==0)continue;
			if(root->CouldBeRoot()){
				root->DeleteNode();
			} else {
				if(m_bIsTarget)root->AddConditions();
				m_root=root;
			}
			m_obj_list[i]=0;
		}
	}
#ifdef DEBUG_LOG
	if(getTreeMapper()->m_debug_log!=0){
		write(getTreeMapper()->m_debug_log);
		fprintf(getTreeMapper()->m_debug_log,"%s",(const char*)m_comment);
		fflush(getTreeMapper()->m_debug_log);
	}
#endif
	return err;
}

unsigned long DTDParser::loadXSD(const std::string& stXsdFilePath, bool validate) {
	unsigned long hr = m_SchemaParser.LoadSchema(stXsdFilePath, m_SchemaCollection, true, validate);
	if (hr != S_OK) {
		m_lastErr = m_SchemaParser.GetLastError().c_str();
	}
	return hr;
}

unsigned long DTDParser::parseXSD(const char*xsd_file_name, const char*root_str, bool root_selected){
	unsigned long hr = loadXSD(xsd_file_name, false);
	if(hr!=S_OK){
		return hr;
	}
	hr=m_SchemaParser.Parse(root_str);
	if(hr!=S_OK){
		m_lastErr=m_SchemaParser.GetLastError().c_str();
		return hr;
	}
	XsdParsingResult result = m_SchemaParser.GetParsingResult();
	m_root=result.GetRoot();
	m_Element_map=result.GetSubstitutionGroupElements();
	m_ElementType_map=result.GetComplexTypes();
	return S_OK;
}

                                        
int DTDParser::createJtrFromDtd(const char *xsd_file_name, const char *root_str, const char *jtr_file_name){
	int err=parseDtd(xsd_file_name, root_str, true);
	if(err==0){
		XmlCromToJtrWriter jtrWriter;
		jtrWriter.WriteXml(m_root, true, jtr_file_name);
	}
	return err;
}

                                        
unsigned long DTDParser::createJtrFromXSD(const char *xsd_file_name, const char *root_str, const char *jtr_file_name){
	unsigned long hr=parseXSD(xsd_file_name,root_str,true);
	if(hr!=S_OK)return hr;
	XsdParsingResult result = m_SchemaParser.GetParsingResult();
	result.WriteXml(jtr_file_name);
	return S_OK;
}


unsigned long DTDParser::writeJtr(const char*jtr_file_name, const char*click, const char*file_extention){
	if(stricmp(file_extention,"dtd")==0){
		XmlCromToJtrWriter jtrWriter;
		jtrWriter.WriteXml(m_root, true, jtr_file_name);
	}
	else if(stricmp(file_extention,"xsd")==0){
		XsdParsingResult result = m_SchemaParser.GetParsingResult();
		result.WriteXml(jtr_file_name);
	}
	else {
		CromToJtrWriter jtrWriter;
		jtrWriter.createJtr(CROMHeader(m_root), jtr_file_name);
	}
	return S_OK;
}


                                                     
void DTDParser::CreateCromFromJtr(const char*jtr_file_name){
	cleanup();
	try {
		m_xml=true;
		JtrReader reader(m_pTreeMapper,m_ElementType_map,m_Element_map,m_Names,m_jmsProperties);
		m_root=reader.parse(jtr_file_name);
	}
	catch(CrException& e){
		throw e;
	}
}

CROM* DTDParser::findSource_expandTreeIfNotExist(const char*path){
	if(path==0||path[0]==0)return 0;
	bool isFlat=getTreeMapper()->IsFlatSource();
	if(isFlat){
		CROM*node=0, *root=m_root->GetFirstChild();
		const size_t len=strlen(path);
		if(path[0]=='['  && path[len-1]==']'){
			KString path_(&path[1],len-2);
			if(len==2||root->GetName()==path_)return root;
		}
		else {
			for(CROM* child=root->GetFirstChild();child!=0;child=child->GetNextSibling()){
				if(strcmp(path,child->get_Name())==0){
					node=child;
					break;
				}
			}
		}
		if(node==0){
			throw konga::CrException(std::string("Fail to find the node:")+path+" for flat source structure.");
		}
		return node;
	}

	               
	  
	const bool isLeaf=path[0]!='[';
	const size_t len=strlen(path);

	                                                                      
	int k1=0, k2=0;
	if(!isLeaf){
		k1=1;
		k2=len-1;
		if(strcmp(path,"[]")==0)
			return m_root;
	} else {
		k1=0;
		char c;
		for(k2=len-1;k2>k1;k2--){
			if((c=path[k2-1])=='.'||c=='$')break;
		}
	}
	if(k2==k1)return 0;
	StringTokenizer tk(&path[k1], k2-k1,"$.");
	CROM *parent=m_root, *node=0;
	if(parent->GetName().GetLength()>0){
		if(tk.NextToken()){
			if(tk.token()!=parent->GetName())
				throw konga::CrException(
					"Find source structure node failed, the root structure node name conflict with the path. root name="+
					std::string((const char*)parent->GetName())+", path="+path);
		} else {
			return 0;
		}
	}
	char c;
	while(tk.NextToken() && parent!=0){
		string token(tk.token());
		const char* c1=tk.peek_next_token();
		bool isLoop=c1!=0&&(*c1=='.'||*c1=='#');
		if((c=token.at(0))=='.'||c=='$')continue;
		parent->expandCROM(m_ElementType_map, m_Element_map);
		int k=(int)token.find('#');                       
		if(k>=0)token=token.substr(0,k);
		node=parent->FindChildFolder(token.c_str(), isLoop);

		if(node==0&&(parent->IsDerivedTypeHolder()||parent->IsSubstitutionGroupHead())){
			                                                       
			parent->expandHoldersTypeNode(token.c_str(), m_ElementType_map, m_Element_map);
		}
		if(node==0){
			node=parent->FindChildFolder(token.c_str(), isLoop);
		}
		if(node==0){        
			throw konga::CrException(std::string("Fail to expand the source tree for the path:")+path+". The node:"+token+" cannot be created.");
		}
		parent=node;
	}
	if(parent!=0){
		parent->expandCROM(m_ElementType_map, m_Element_map);
	}
	bool isTarget=false;
	return findNode(path, isTarget);
}

CROM* DTDParser::findTarget_expandTreeIfNotExist(const char*path){
	if(path==0||path[0]==0)return 0;
	bool isFlat=getTreeMapper()->IsFlatTarget();
	if(isFlat){
		CROM*node=0;
		CROM*parent=m_root->GetFirstChild();
		int len=strlen(path);
		bool bIsPrecondition=false;
		if(path[0]=='[' && path[len-1]==']'){
			KString sPath(&path[1],len-2);
			path=(const char*)sPath;
			int k=konga::findk(path,'#');
			bool bMultipleMapping=path[k]=='#';
			if(bMultipleMapping){
				int i_mapping=atoi(&path[k+1]);
				parent=parent->findMultipleMapping(i_mapping);
				if(parent==0){
					parent=m_root->GetFirstChild()->insertMultipleMapping(i_mapping);
				}
			}
			if(parent && parent->GetName()==sPath)return parent;
		}
		else {
			int k=konga::findk(path,'#');
			bool bMultipleMapping=path[k]=='#';
			bIsPrecondition=strncmp(path,c_precondition,strlen(c_precondition))==0;
			if(bMultipleMapping){
				int i_mapping=atoi(&path[k+1]);
				parent=parent->findMultipleMapping(i_mapping);
				if(parent==0){
					parent=m_root->GetFirstChild()->insertMultipleMapping(i_mapping);
				}
			}
			for(CROM* child=parent->GetFirstChild();child!=0;child=child->GetNextSibling()){
				if(bIsPrecondition){
					if(child->IsPrecondition())return child;
				}
				else if(strcmp(path,child->get_Name())==0){
					return child;
				}
			}
		}
		if(bIsPrecondition){
			node=new CROM(m_pTreeMapper,parent,0,0,0,CROM_t_Precondition,parent->m_CROM_ext_type);
		}
		else {
			throw konga::CrException(std::string("Fail to find the node:")+path+" for flat target structure.");
		}
		return node;
	}
	bool isLeaf=path[0]!='[';
	                                                                     
	int len=(int)strlen(path), k1, k2;
	if(!isLeaf){
		k1=1;
		k2=len-1;
	} else {
		k1=0;
		char c;
		for(k2=len;k2>k1;k2--){
			if((c=path[k2-1])=='.'||c=='$')break;
		}
	}
	if(k2==k1)return 0;
	StringTokenizer tk(&path[k1], k2-k1,"$.");
	CROM *parent=m_root, *node=0;
	if(parent->GetName().GetLength()>0){
		if(tk.NextToken()){
			if(parent->GetName()!=tk.token()){
				throw konga::CrException(
					"Find target Structure node failed, the root structure node name conflict with the path. root name="+
					std::string((const char*)parent->GetName())+", path="+path);
			}
		} else {
			return 0;
		}
	}
	char c;
	while(tk.NextToken() && parent!=0){
		string token0(tk.token());
		const char* c1=tk.peek_next_token();
		bool isLoop=c1!=0&&(*c1=='.'||*c1=='#');
		if((c=token0.at(0))=='.'||c=='$')continue;
		int k=(int)token0.find('#');
		string token=token0.substr(0,k);

		parent->expandCROM(m_ElementType_map, m_Element_map);

		node=parent->FindChildFolder(token.c_str(), isLoop);

		if(node==0&&(parent->IsDerivedTypeHolder()||parent->IsSubstitutionGroupHead())){
			                                                       
			node=parent->expandHoldersTypeNode(token.c_str(), m_ElementType_map,m_Element_map);
			if(k>0){
				node->set_Name(token0.c_str());
				k=0;
			}
		}
		
		if(node==0){
			throw konga::CrException(std::string("Fail to expand the target tree for the path:")+path+". The node:"+token+" cannot be created.");
		}

		                             
		if(k>=0){
			CROM*node_saved=node;
			node=parent->FindChildFolder(token0.c_str(), isLoop);
			if(node==0){
				if((k+1)<(int)token0.length()){
					if(node_saved!=0){
						const int i_mapping=atoi(token0.substr(k+1).c_str());
						node_saved->expandCROM(m_ElementType_map, m_Element_map);
						node_saved->insertMultipleMapping(i_mapping);
					}
				} else {
					throw konga::CrException(std::string("Internal error, maplist syntax error target path:")+path+". The node:"+token0+" cannot be created.");
				}
				node=parent->FindChildFolder(token0.c_str(), isLoop);
				if(node==0){
					throw konga::CrException(std::string("Failed to create multiple mapping target, the path:")+path+". The node:"+token0+" cannot be created.");
				}
			}			
		}
		parent=node;
	}
	parent->expandCROM(m_ElementType_map, m_Element_map);

	if(strcmp(&path[len-strlen(c_precondition)],c_precondition)==0){
		CROM*precondition=parent->getPrecondition();
		if(precondition==0){
			new CROM(getTreeMapper(),parent,0,0,0,CROM_t_Precondition,parent->get_CROM_ext_type());
		}
	} 
	bool isTarget=true;
	node = findNode(path, isTarget);
	if(node==0&& parent->IsLdapAttributeRemove()){
		if(strcmp(&path[k2],"Attribute command")==0){
			node=parent->insertLdapAttributeCmd();
		}
	}
	return node;
}

CROM*DTDParser::findNode(const std::string&path, bool isTarget){
	return findNode(path.c_str(), isTarget);
}

CROM*DTDParser::findNode(const char*path, bool isTarget)
{
                                                           
	bool isLeaf=path[0]!='[';
	int len=(int)strlen(path),k1,k2;
	if(isLeaf){
		k1=0;
		char c;
		for(k2=len-1;k2>k1;k2--){
			if((c=path[k2])=='.'||c=='$')
				break;
		}
	}
	else {
		k1=1;
		k2=len-2;
	}
	CROM*root=FindRoot(),*node=root;
	if(node==0){
		return 0;
	}
	konga::StringTokenizer tk(&path[k1], k2+1-k1,"$.");
	if(node->GetName().GetLength()>0){
		if(tk.NextToken()){
			if(tk.token()!=node->GetName())
				throw konga::CrException(
					"Find structure node failed, the root structure node name conflict with the path. root name="+
					std::string((const char*)node->GetName())+", path="+path);
		} else {
			return 0;
		}
	}
	while(tk.NextToken()){
		const char*token=tk.token();
		const char* c1=tk.peek_next_token();
		bool isLoop=c1!=0&&(*c1=='.'||*c1=='#');
		switch(token[0]){
		case '.':
		case '$':
			break;
		default:
			{
				int k=konga::findk(token, '#');
				if(token[k]=='#'&&!isTarget){
					std::string name(token,k);
					node=node->FindChildFolder(name.c_str(), isLoop);
					if(node==0)return 0;
				} else {
					node=node->FindChildFolder(token, isLoop);
					if(node==0)return 0;
				}
				break;
			}
		}
	}
	if(isLeaf){
		const char*name=&path[k2+1];
		return node->FindChildLeaf(name);
	} else {
		return node;
	}
}

std::string DTDParser::getFirstMultiMappingInsertPath(const char*path){
	const bool isTarget=true;
	int len=(int)strlen(path),pos1=0,pos2=len;
	if(len>1 && path[0]=='[' && path[len-1]==']'){
		pos1=1;
		pos2=len-1;
	}

	for(int pos=pos1;pos<pos2;pos++){
		if(path[pos]!='#')continue;
		for(pos++;pos<pos2;pos++){
			if(!isdigit((unsigned char)path[pos])){
				pos++;                                         
				break;
			}
		}
		std::string newPath(&path[pos1],pos+1-pos1);
		CROM* target=findNode("["+newPath+"]", isTarget);
		if(target==0){
			return newPath;
		}
	}
	return "";
}


CROM* DTDParser::insertMultipleMapping(const char* path)
{
	const bool isTarget=true;
	const bool isFlat=IsFlatFile();	                                                                            
	CROM* node, *path_node=0;
	while(path_node==0) {
		std::string multiMappingInsertPath=getFirstMultiMappingInsertPath(path);
		if(multiMappingInsertPath.length()==0){
			throw konga::CrException(std::string("Insert multiple mapping failed, cannot find the insert path. path=")+path);
		}
		int pos1, pos2;
		pos1=(int)multiMappingInsertPath.find_last_of('#');
		pos2=(int)multiMappingInsertPath.length();
		if(pos2>0&&(multiMappingInsertPath.at(pos2-1)=='.'||multiMappingInsertPath.at(pos2-1)=='$'))pos2--;
		if((pos1+1)>pos2){
			throw konga::CrException(std::string("Error in target expression (pos1+1)>pos2 pos1=")+
				konga::toString(pos1)+", pos2="+konga::toString(pos2));
		}
			
		std::string str1=multiMappingInsertPath.substr(pos1+1,pos2-(pos1+1));
		int i_mapping=atoi(str1.c_str());
		char c=multiMappingInsertPath.at(multiMappingInsertPath.length()-1);
		std::string oldPath;
		oldPath=multiMappingInsertPath.substr(0,pos1);
		if(c=='.'||c=='$')oldPath+=c;
		node=findNode("["+oldPath+"]", isTarget);
			
		if(node==0){
			throw konga::CrException(std::string("Insert multiple mapping can't find the original node, path=")+oldPath);
		}
		node->insertMultipleMapping(i_mapping);
		node=findNode(std::string("[")+multiMappingInsertPath+"]", isTarget);
		if(node==0){
			throw konga::CrException(std::string("Insert multiple mapping failed(1). Can't create multiple mapping, path=")+multiMappingInsertPath);
		}
		path_node=findNode(std::string("[")+path+"]",isTarget);
	}
	return path_node;
}

bool DTDParser::GetComplexTextDelimiterQualifiers(unsigned char &cDelimiter,  unsigned char &cStrQual, unsigned char &cNumQual, int &iIdColNum, int &iStartPos, int &iLen, bool &bIsDatabase)
{
	CROM* pObj = FindRoot();
	if(pObj==0)return false;
	return pObj->GetComplexTextDelimiterQualifiers(cDelimiter,  cStrQual, cNumQual, iIdColNum, iStartPos, iLen, bIsDatabase);
}

namespace {
void addAttrNS2PrefixMap(CROM* dtd, std::map<std::string,std::string>&attrNS_prefix_map){
	for(CROM*child=dtd->GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf()){
			if(strncmp((const char*)child->GetDisplayName(),"xsi:",4)==0){
				attrNS_prefix_map[XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001]="xsi";
			}
			else if(child->IsNamespaceQualified()){
				const std::string& ns(child->GetNamespace());
				if(ns.empty())continue;
				if(attrNS_prefix_map.find(ns)==attrNS_prefix_map.end()){
					for(int i=0; true; i++){
						std::string prefix="ans"+toString(i);
						bool found=false;
						for(std::map<std::string,std::string>::iterator it=attrNS_prefix_map.begin();
							it!=attrNS_prefix_map.end(); ++it){
							if(prefix==it->second){
								found=true;
								break;
							}
						}
						if(found==false){
							attrNS_prefix_map[ns]=prefix;
							break;
						}
					}
				}
			}
		}
		else {
			if(child->IsDerivedTypeHolder()||child->IsSubstitutionGroupHead()){
				attrNS_prefix_map[XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001]="xsi";
			}
			addAttrNS2PrefixMap(child, attrNS_prefix_map);
		}
	}
}
}

void DTDParser::CreateAttrNS2PrefixMap(){
	CROM* root = FindRoot();
	if(root==0)return;
	addAttrNS2PrefixMap(root, attrNS_prefix_map);
}
