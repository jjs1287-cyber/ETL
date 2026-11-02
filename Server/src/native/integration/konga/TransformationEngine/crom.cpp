/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/Settings.h>
#include <konga/Utils/StringTokenizer.h>

#include <konga/Xml/ExpandedName.h>

#include <konga/TransformationLogging/ViewLog.h>

#include "resource.h"
#include "TreeMapper.h"
#include "Transform.h"
#include "CROM_Text.h"
#include "CROM_DB.h"
#include "CROM_Ldap.h"
#include "CROM_Xml.h"
#include "crom.h"
#include "CROMData.h"

using namespace std;
using namespace konga::xml;

namespace
{	
	int GetDtdMaxTreeDepth() {
		static int order_max = konga::GetOptionAsInt("TreeMapper", "DtdMaxTreeDepth", 30);
		return order_max;
	}

	const bool INCLUDE_FORMAT_IN_WRITE = false;

	void CloneCROMChildrenInsertFirst(CROM* source, CROM* target)
	{
		CROM* child = source->GetFirstChild();
		if (child == 0)
		{
			return;
		}
		CROM* originalFirstChild = target->GetFirstChild();
		target->SetFirstChild(0);
		while( child != 0 )
		{
			CROM* clone = new CROM(child);
			CROM::AddToParent(target, clone);
			child = child->GetNextSibling();
		}
		if (originalFirstChild != 0)
		{
			CROM* lastChild = target->GetLastChild();                               
													                                                     
			lastChild->SetNextSibling(originalFirstChild);
			originalFirstChild->SetPreviousSibling(lastChild);
		}
	}

	CROM* findRecursiveParent(CROM*parent,const char*name){
		while(parent){
			if(parent->m_Name==name)return parent;
			parent=parent->GetParent();
		}
		return 0;
	}
	CROM* findChildNodeToClone(CROM*parent,const char*name){
		for(CROM*child=parent->GetFirstChild();child;child=child->GetNextSibling()){
			if(child->GetFirstChild()==0)continue;
			if(child->m_Name==name)return child;
			CROM*obj=findChildNodeToClone(child,name);
			if(obj!=0)return obj;
		}
		return 0;                      
	}

	bool vector_contains(const string&exploded_id, vector<string>&exploded){
		for(vector<string>::iterator it=exploded.begin();it!=exploded.end();it++){
			if(*it==exploded_id)return true;
		}
		return false;
	}

	void put_to_set(map<string,bool> &set, map<string,bool> &set0){
		for(map<string,bool>::iterator it=set.begin();it!=set.end();it++){
			map<string,bool>::iterator it0=set0.find(it->first);
			if(it0==set0.end()){
				set0[it->first]=true;
			}
		}
	}
}

char SetQualDelimiterChar(const char*s)
{
	char c;
	if(s==0)c=0;
	else if(stricmp(s,"^t")==0 || stricmp(s,"\\t")==0)c='\t';
	else if(s[0]=='\\'&&isdigit((unsigned char)s[1]))c=atoi(&s[1]);		
	else c=s[0];
	return c;
}

string CharToString(unsigned char c){
	if(c!=0)return string(1,c);
	return "";
}


CROM::CROM(TreeMapper*p,CROM *parent, CROM *prevSibling,
		   const char *name, const char *value, int type, CROM_ext_type ext_type) :
	m_Type(type),
	m_Type2(0),
	m_pTreeMapper(p),
	m_Generator(0),
	m_deList(0),
	m_instanceNumber(0),
	m_Parent(parent), m_FirstChild(0), m_NextSibling(0), m_PrevSibling(prevSibling)
{
	m_mapInfo.trans=0;

	if(name==0&&type==CROM_t_Precondition){
		name=c_precondition;
		m_display_name="";
	}
	else if(name){
		m_display_name=name;
		m_Name=convertIllegalCharacters(name);
		if(m_display_name==m_Name)m_display_name="";
	}

	if(m_PrevSibling){
		CROM*tmp=m_PrevSibling->m_NextSibling;
		m_PrevSibling->m_NextSibling=this;
		m_NextSibling=tmp;
		if(tmp)tmp->m_PrevSibling=this;
	} else if(m_Parent){
		m_NextSibling=m_Parent->m_FirstChild;
		m_Parent->m_FirstChild=this;
		if(m_NextSibling)m_NextSibling->m_PrevSibling=this;
	} else {
		m_NextSibling=0;
		m_PrevSibling=0;
	}

	m_CROM_ext_type=ext_type;
	if(IsLeaf()){
		m_ext.attr=new CROM_Attr(value);
		setHasDefaultFlag();
		if(ext_type==t_CROM_Text && !IsIdentifier()){
			AddType(CROM_t_Optional);
		}
	} else {
		switch(ext_type){
			case t_CROM_Unknown: m_ext.p=0; break;
			case t_CROM_Text: m_ext.text=new CROM_Text(); break;
			case t_CROM_DB: m_ext.db=new CROM_DB(); break;
			case t_CROM_Xml: m_ext.xml=new CROM_Xml(); break;
		};
	}
}

KString CROM::convertIllegalCharacters(const char*displayName)
{
	KString name;
	if(displayName){
		konga::StringTokenizer t(displayName," +-*/^$.#'&");
		const char* token;
		while(t.NextToken()){
			token=t.token();
			switch(token[0])
			{
			case ' ':
			case '+':
			case '-':
			case '*':
			case '/':
			case '^':
			case '$':
			case '.':
			case '#':
			case '\'':
			case '&':
				name+='?';
				break;
			default:
				name+=token;
			}
		}
	}
	return name;
}

CROM::CROM(CROM *to_be_cloned, const char*recursive_name) :
	m_Name(to_be_cloned->m_Name),
	m_Type(to_be_cloned->m_Type),
	m_Type2(to_be_cloned->m_Type2),
	m_CROM_ext_type(to_be_cloned->m_CROM_ext_type),
	m_pTreeMapper(to_be_cloned->m_pTreeMapper),
	m_Generator(0),
	m_Parent(0),
	m_FirstChild(0),
	m_NextSibling(0),
	m_PrevSibling(0),
	m_deList(0),
	m_instanceNumber(0),
	m_display_name(to_be_cloned->m_display_name),
	m_Comment(to_be_cloned->m_Comment)
{
	m_mapInfo.trans=0;

	if(IsLeaf()){
		m_ext.attr=new CROM_Attr(to_be_cloned->m_ext.attr, m_CROM_ext_type==t_CROM_Xml);
		setHasDefaultFlag();
	} else {
		switch(m_CROM_ext_type){
			case t_CROM_Unknown: break;
			case t_CROM_Text: m_ext.text=new CROM_Text(to_be_cloned->m_ext.text); break;
			case t_CROM_DB: m_ext.db=new CROM_DB(to_be_cloned->m_ext.db); break;
			case t_CROM_Xml: m_ext.xml=new CROM_Xml(to_be_cloned->m_ext.xml); break;
		}
		if(recursive_name==0 || GetName()!=recursive_name){
			for(CROM*_child=to_be_cloned->GetFirstChild(), *cloned_child=0, *prev_cloned_child;
				_child;
				_child=_child->GetNextSibling()){
				prev_cloned_child=cloned_child;
				cloned_child=new CROM(_child, recursive_name);
				cloned_child->SetParent(this);
				if(prev_cloned_child==0)m_FirstChild=cloned_child;
				else prev_cloned_child->SetNextSibling(cloned_child);
				cloned_child->SetPreviousSibling(prev_cloned_child);
			}
		}
	}
}


CROM::~CROM(){
	if(m_deList){
		for(vector<const char*>::iterator it=m_deList->begin();it!=m_deList->end();it++){
			delete[](*it);
		}
		delete m_deList;
		m_deList=0;
	}

	if(IsLeaf()){
		if(m_ext.attr)delete m_ext.attr;
	} else {
		switch(m_CROM_ext_type){
			case t_CROM_Text: if(m_ext.text)delete m_ext.text; break;
			case t_CROM_DB: if(m_ext.db)delete m_ext.db; break;
			case t_CROM_Ldap: if(m_ext.ldap)delete m_ext.ldap; break;
			case t_CROM_Xml: if(m_ext.xml)delete m_ext.xml; break;
		}
	}
	if(!IsLeaf()){
		if(m_mapInfo.trans!=0) {
			delete m_mapInfo.trans;
			m_mapInfo.trans = 0;
		}
	}
	else {
		if(m_mapInfo.item!=0) {
			delete m_mapInfo.item;
			m_mapInfo.item = 0;
		}
	}
}

bool CROM::ChildrenCanBeSkipped() const{
	if(IsOneOfNode()){
		for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
			if(child->IsOptional())return true;
			if(child->IsCollection())return true;
			if(child->IsGroupNode()){
				if(child->ChildrenCanBeSkipped())return true;
			}
		}
		return false;
	}
	else {
		for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
			if(!child->IsOptionalNode() && !child->IsCollection()){
				if(child->IsGroupNode()){
					if(child->ChildrenCanBeSkipped()==false)return false;
				}
			}
		}
		return true;
	}
}


CROM *CROM::GetLastChild()
{
	CROM*child=GetFirstChild(),*child_saved=0;
	while(child){
		child_saved=child;
		child=child->GetNextSibling();
	}
	return child_saved;
}

CROM *CROM::GetValueNode()
{
	CROM*child=GetFirstChild();
	while(child){
		if((child->m_Type&CROM_t_Value)!=0)return child;
		child=child->GetNextSibling();
	}
	return 0;
}

CROM *CROM::GetLastAttributeChild()
{
	CROM*child=GetFirstChild(),*child_saved=0;
	while(child){
		if((child->m_Type&CROM_t_AttValuePrecondition)!=0)child_saved=child;
		child=child->GetNextSibling();
	}
	return child_saved;
}

CROM *CROM::GetFirstSibling(){
	if(m_Parent)return m_Parent->GetFirstChild();
	CROM*child=this,*first_child=0;
	while(child){
		first_child=child; 
		child=child->GetPreviousSibling();
	}
	return first_child;
}

CROM *CROM::GetRoot()
{
	CROM*parent=m_Parent,*tmp=this;
	while(parent){
		tmp=parent;
		parent=parent->GetParent();
	}
	return tmp;
}

int CROM::CountChildren() const {
	return m_FirstChild?m_FirstChild->CountSibling():0;
}

int CROM::CountSibling() const {
	int n=1;
	CROM*child=m_PrevSibling;
	while(child){
		n++;
		child=child->m_PrevSibling;
	}
	child=m_NextSibling;
	while(child){
		n++;
		child=child->m_NextSibling;
	}
	return n;
}

CROM *CROM::FindFirstObject(const char* name,int type,CROM*exclusion)
{
	if((type==CROM_t_AnyThing||type==m_Type||(m_Type&type)!=0)&&(m_Name==name||get_Path()==name)&&(exclusion==0||exclusion!=this))
		return this;
	if((type==CROM_t_AnyThing||type==m_Type||(m_Type&type)!=0)&&GetDisplayName()==name&&(exclusion==0||exclusion!=this))
		return this;
	CROM*child=GetFirstChild(),*obj;
	while(child){
		if((obj=child->FindFirstObject(name,type))!=0)return obj;
		child=child->GetNextSibling();
	}
	return child;                      
}

CROM *CROM::FindObjectInChild(const char* name,int type) const
{
	CROM*child=GetFirstChild();
	while(child){
		if((child->m_Name==name||child->GetDisplayName()==name)&&
			((CROM_t_AnyThing==type||child->m_Type==type||(child->m_Type&type)!=0)||
			(type==CROM_t_NonLeaf&&child->m_Type==0)))
			return child;
		child=child->GetNextSibling();
	}
	return child;                      
}

void CROM::DeleteNode()
{
	CROM*child,*next;
	child=GetFirstChild();
	while(child){
		next=child->GetNextSibling();
		child->DeleteNode();
		child=next;
	}
	if(m_NextSibling)m_NextSibling->m_PrevSibling=m_PrevSibling;
	if(m_PrevSibling){
		m_PrevSibling->m_NextSibling=m_NextSibling;
	} else if(m_Parent){
		m_Parent->m_FirstChild=m_NextSibling;
	}

	delete this;
}

int CROM::RemoveExtraNode(int&n_loop,bool&modified)
{
	int n=0;
	CROM*child=m_FirstChild,*grand_child,*prev_child,*next_child;
	modified=false;
	bool child_modified;

	while(child){

		while((child->m_Type&CROM_t_Element)==0&&child->m_Name==""&&
			child->CountChildren()==1){
			grand_child=child->GetFirstChild();
			prev_child=child->GetPreviousSibling();
			next_child=child->GetNextSibling();

			if(prev_child==0)m_FirstChild=grand_child;
			else prev_child->SetNextSibling(grand_child);

			if(next_child)next_child->SetPreviousSibling(grand_child);

			grand_child->SetParent(this);
			grand_child->SetPreviousSibling(prev_child);
			grand_child->SetNextSibling(next_child);
			grand_child->m_Type|=child->m_Type;
			delete child;
			child=grand_child;
			n++;
			modified=true;
		}
		if(child->m_Name==""&&!child->IsValueLeaf()){
			n_loop++;
			child->m_Name.Format("%d",n_loop);
		}
		child->RemoveExtraNode(n_loop,child_modified);
		if(child_modified==true)modified=true;
		child=child->GetNextSibling();
	}
	if(CountChildren()==1){
		child=m_FirstChild;
		if(child->m_Type==CROM_t_Unknown){
			grand_child=child->GetFirstChild();
			m_FirstChild=grand_child;
			while(grand_child){
				grand_child->SetParent(this);
				grand_child=grand_child->GetNextSibling();
			}
			delete child;
		}
	}
	return n++;
}


                                                   
                                          
int CROM::Exploding(CROM*obj,int order,int&err)
{
	int n=0;
	if(obj->m_Name=="")return n;
	if(order>=GetDtdMaxTreeDepth()){
		err=EXPLODING_EXCEED_MAX_ORDER;
		return n;
	}

	                                                
	bool bRecursive=false;
	CROM*parent=this;
	const KString& name(obj->GetDisplayName());
	while(parent){
		if(parent->GetDisplayName()==name){
			bRecursive=true;
			break;
		}
		parent=parent->m_Parent;
	}
	if(bRecursive)return n;

	if(m_Name==obj->m_Name&&(obj->m_Type&CROM_t_Element)!=0){
		                                         
		int n_samename=0;
		CROM*parent=GetParent();
		KString tmp=m_Name+"->"+obj->m_Name;
		while(parent){
			tmp=parent->m_Name+"/"+tmp;
			if(parent->m_Name==m_Name){
				n_samename++;
				getTreeMapper()->getViewLog()->DebugString( "CROM::Exploding - detect recursive, %s\n", (const char*)tmp);
			}
			parent=parent->GetParent();
		}
		if(n_samename>0){
			getTreeMapper()->getViewLog()->DebugString(" %s\n",(const char*)tmp);
		}
		if(n_samename<1){
			CROM*clone=new CROM(obj),*cloned_child,
				*last_child=GetLastChild();

			                                                   
			cloned_child=clone->GetFirstChild();
			while(cloned_child)
			{
				cloned_child->SetParent(this);
				cloned_child->SetPreviousSibling(last_child);
				if(last_child==0)m_FirstChild=cloned_child;
				else last_child->SetNextSibling(cloned_child);
				last_child=cloned_child;
				cloned_child=cloned_child->GetNextSibling();
			}
			delete clone;
			
			n++;
		}
	}

	CROM*child=m_FirstChild;
	while(child){
		n+=child->Exploding(obj,order+1,err);
		if(err)break;
		child=child->GetNextSibling();
	}
	return n;
}

int CROM::ExplodingAttribute(CROM*obj,int order,int&err)
{
	int n=0;
	if(obj->m_Name=="")return n;
	if(order>=GetDtdMaxTreeDepth()){
		err=EXPLODING_EXCEED_MAX_ORDER;
		return n;
	}

	if(m_Name==obj->m_Name&&(obj->m_Type&CROM_t_Attribute)==0){
		CROM*clone=new CROM(obj),*cloned_child,
			*insert_child=0,*next_child,*first_child=GetFirstChild();
		if(first_child&&(first_child->m_Type&CROM_t_Value)!=0)insert_child=first_child;
		if(insert_child)next_child=insert_child->GetNextSibling();
		else next_child=first_child;
		                                                   
		cloned_child=clone->GetFirstChild();
		while(cloned_child)
		{
			cloned_child->SetParent(this);
			cloned_child->SetPreviousSibling(insert_child);
			
			if(insert_child==0)m_FirstChild=cloned_child;
			else insert_child->SetNextSibling(cloned_child);
			insert_child=cloned_child;
			cloned_child=cloned_child->GetNextSibling();
		}
		                     
		if(insert_child)insert_child->SetNextSibling(next_child);
		if(next_child)next_child->SetPreviousSibling(insert_child);
		delete clone;
		
		n++;
	}

	CROM*child=m_FirstChild;
	while(child){
		if((child->m_Type&CROM_t_Attribute)==0)
		n+=child->ExplodingAttribute(obj,order+1,err);
		if(err)break;
		child=child->GetNextSibling();
	}
	return n;
}



KString&CROM::GetTreeText(bool showNodeType)
{
	getTreeMapper()->g_TreeText="";
	if(showNodeType){
		KString name;
		if((m_Type&CROM_t_Element)!=0)name+="E";
		if((m_Type&CROM_t_Value)!=0)name+="V";
		if((m_Type&CROM_t_Attribute)!=0)name+="A";
		if((m_Type&CROM_t_Precondition)!=0)name+="P";
		if((m_Type&CROM_t_DerivedTypeHolder)!=0)name+="d";
		if((m_Type&CROM_t_SubstitutionGroupHead)!=0)name+="h";
		if((m_Type&CROM_t_TypeDefinition)!=0)name+="t";
		if((m_Type==CROM_t_Unknown||m_Type==CROM_t_Collection) || (m_Type&CROM_t_AnonymousGroup)!=0) name="~";
		if((m_Type&CROM_t_OneOf)!=0)name+="|";
		if((m_Type&CROM_t_Collection)!=0)name+="*";
		if((m_Type&CROM_t_Optional)!=0)name+="?";
		if((m_Type&CROM_t_AtLeastOne)!=0)name+="+";
		if(IsLeaf()){
			if(IsPrecondition()){
				getTreeMapper()->g_TreeText.Format("[%s] %s %s",
					(const char*)name,(const char*)c_precondition, get_Value());
			}
			else if(GetValueType()[0]!=0){
				if(get_Value()[0]!=0){
					getTreeMapper()->g_TreeText.Format("[%s] %s (%s) =\"%s\"",
						(const char*)name,(const char*)GetDisplayName(),GetValueType(),get_Value());
				} else {
					getTreeMapper()->g_TreeText.Format("[%s] %s (%s)",
						(const char*)name,(const char*)GetDisplayName(),GetValueType());
				}
			}
			else {
				if(get_Value()[0]!=0){
					getTreeMapper()->g_TreeText.Format("[%s] %s = \"%s\"",
						(const char*)name,(const char*)GetDisplayName(),get_Value());
				} else {
					getTreeMapper()->g_TreeText.Format("[%s] %s",
						(const char*)name,(const char*)GetDisplayName());
				}
			}
		} else {
			getTreeMapper()->g_TreeText.Format("[%s] %s",
				(const char*)name,(const char*)GetDisplayName());
		}
	} else {
		if(IsLeaf()){
			                                                                                                     
			bool bMarkOptional=IsOptional();
			const char* optionalStr=bMarkOptional?"?":"";
			if(IsPrecondition()){
				getTreeMapper()->g_TreeText.Format("%s %s",
					(const char*)c_precondition,get_Value());
			}
			else if(GetValueType()[0]!=0){
				if(get_Value()[0]!=0){
					getTreeMapper()->g_TreeText.Format("%s%s (%s) = \"%s\"",(const char*)GetDisplayName(),(const char*)optionalStr,GetValueType(),get_Value());
				} else {
					getTreeMapper()->g_TreeText.Format("%s%s (%s)",(const char*)GetDisplayName(),(const char*)optionalStr,GetValueType());
				}
			} else {
				if(get_Value()[0]!=0){
					getTreeMapper()->g_TreeText.Format("%s%s = \"%s\"",(const char*)GetDisplayName(),(const char*)optionalStr,get_Value());
				} else {
					getTreeMapper()->g_TreeText.Format("%s%s",(const char*)GetDisplayName(),(const char*)optionalStr);
				}
			}
		} else {
			getTreeMapper()->g_TreeText.Format("%s",(const char*)GetDisplayName());
		}
	}
	return getTreeMapper()->g_TreeText;
}

KString&CROM::GetTreeText(const KString&value,bool showNodeType){
	getTreeMapper()->g_TreeText="";
	if(showNodeType){
		KString name;
		if((m_Type&CROM_t_Element)!=0)name+="E";
		if((m_Type&CROM_t_Attribute)!=0)name+="A";
		if((m_Type&CROM_t_Precondition)!=0)name+="P";
		if((m_Type&CROM_t_Value)!=0)name+="V";
		if(m_Type==CROM_t_Unknown) name="~";
		if((m_Type&CROM_t_Collection)!=0)name+="*";
		if((m_Type&CROM_t_OneOf)!=0)name+="|";
		if((m_Type&CROM_t_Optional)!=0)name+="?";
		if((m_Type&CROM_t_AtLeastOne)!=0)name+="+";
		if(m_Type&&((m_Type&CROM_t_Precondition)==0)){
			if(IsLeaf()){
				if(value.IsEmpty()==false){
					getTreeMapper()->g_TreeText.Format("[%s]%s (%s) =\"%s\"",(const char*)name,(const char*)GetDisplayName(),GetValueType(),(const char*)value);
				} else {
					getTreeMapper()->g_TreeText.Format("[%s]%s (%s)",(const char*)name,(const char*)GetDisplayName(),GetValueType());
				}
			} else {
				if(value.IsEmpty()==false){
					getTreeMapper()->g_TreeText.Format("[%s] %s = \"%s\"",(const char*)name,(const char*)GetDisplayName(),(const char*)value);
				} else {
					getTreeMapper()->g_TreeText.Format("[%s] %s",(const char*)name,(const char*)GetDisplayName());
				}
			}
		} else if(m_Type!=0){
			getTreeMapper()->g_TreeText.Format("[%s] %s %s",(const char*)name,(const char*)c_precondition,(const char*)value);
		} else {
			getTreeMapper()->g_TreeText.Format("[%s] %s",(const char*)name,(const char*)value);
		}
	} else {
		if(IsLeaf()){
			if((m_Type&CROM_t_Precondition)!=0){
				getTreeMapper()->g_TreeText.Format("%s %s",(const char*)c_precondition,(const char*)value);
			}
			else {
				if(GetValueType()[0]){
					if(value.IsEmpty()==false){
						getTreeMapper()->g_TreeText.Format("%s (%s) = \"%s\"",(const char*)GetDisplayName(),GetValueType(),(const char*)value);
					} else {
						getTreeMapper()->g_TreeText.Format("%s (%s)",(const char*)GetDisplayName(),GetValueType());
					}
				} else {
					if(value.IsEmpty()==false){
						getTreeMapper()->g_TreeText.Format("%s = \"%s\"",(const char*)GetDisplayName(),(const char*)value);
					} else {
						getTreeMapper()->g_TreeText.Format("%s",(const char*)GetDisplayName());
					}
				}
			}
		} else {
			if(value.IsEmpty()==false){
				getTreeMapper()->g_TreeText.Format("%s = \"%s\"",(const char*)GetDisplayName(),(const char*)value);
			} else {
				getTreeMapper()->g_TreeText.Format("%s",(const char*)GetDisplayName());
			}
		}
	}
	return getTreeMapper()->g_TreeText;
}

KString GetVerboseType(int Type)
{
	KString TypeDesc;

	if ((Type & CROM_t_Unknown) != 0)
		TypeDesc = "Unknown ";
	if ((Type & CROM_t_Element) != 0)
		TypeDesc += "Element ";
	if ((Type & CROM_t_Value) != 0)
		TypeDesc += "Value ";
	if ((Type & CROM_t_Attribute) != 0)
		TypeDesc += "Attribute ";
	if ((Type & CROM_t_Collection) != 0)
		TypeDesc += "Collection ";
	if ((Type & CROM_t_OneOf) != 0)
		TypeDesc += "OneOf ";
	if ((Type & CROM_t_Optional) != 0)
		TypeDesc += "Optional ";
	if ((Type & CROM_t_AtLeastOne) != 0)
		TypeDesc += "AtLeastOne ";
	if ((Type & CROM_t_PrimaryKey) != 0)
		TypeDesc += "PrimaryKey ";
	if ((Type & CROM_t_ForeignKey	) != 0)
		TypeDesc += "ForeignKey ";
	if ((Type & CROM_t_UseDbFcn) != 0)
		TypeDesc += "UserDefinedFn ";
	if ((Type & CROM_t_Precondition) != 0)
		TypeDesc += "Precondition ";
	if ((Type & CROM_t_Null) != 0)
		TypeDesc += "Null ";
	if ((Type & CROM_t_AllGroup) != 0)
		TypeDesc += "SequenceIgnored ";
	if ((Type & CROM_t_Hidden) != 0)
		TypeDesc += "Hidden ";
	if ((Type & CROM_t_AnyElement) != 0)
		TypeDesc += "AnyElement ";

	return TypeDesc;
}

void CROM::definePath(bool bShouldNotAddNodeChar          ){
	get_Path();
	CROM*child=GetFirstChild();
	while(child)
	{
		child->definePath(bShouldNotAddNodeChar);
		child=child->GetNextSibling();
	}
}

void CROM::write(FILE*out) {
	CROM*parent=m_Parent;
	KString header;
	while(parent)
	{
		header="   "+header;
		parent=parent->GetParent();
	}

	                                                   
	if (0 == out)
		return;
	if( INCLUDE_FORMAT_IN_WRITE )
	{
		if(IsXml()){
			fprintf(out,"%s%s 0x%p 0x%X(%s) %s path=%s namespace=%s typenamespace=%s typename=%s format=%s %s",
				(const char*)header, (const char*)GetTreeText(true), this, m_Type, (const char*)GetVerboseType(m_Type),((m_Type&CROM_t_Mapped)?"mapped":""),
				(const char*)get_Path(), GetNamespace().c_str(), GetSchemaTypeNamespace().c_str(), 
				GetSchemaType().c_str(), 
				get_Format(), IsArray() ? (const char*)("(Array: " + GetArrayType() + ")") : "");
		}
		else {
			fprintf(out,"%s%s 0x%p 0x%X(%s) %s path=%s typename=%s format=%s",
				(const char*)header, (const char*)GetTreeText(true), this, m_Type, (const char*)GetVerboseType(m_Type),((m_Type&CROM_t_Mapped)?"mapped":""),
				(const char*)get_Path(), GetSchemaType().c_str(),
				get_Format());
		}
	}
	else
	{
		if(IsXml()){
			if ( IsLeaf() ) {
				fprintf(out,"%s%s 0x%p 0x%X(%s) %s path=%s",
					(const char*)header, (const char*)GetTreeText(true), this, m_Type,
					(const char*)GetVerboseType(m_Type),((m_Type&CROM_t_Mapped)?"mapped":""),
					(const char*)get_Path());
			}
			else {
				fprintf(out,"%s%s 0x%p 0x%X(%s) %s path=%s namespace=%s typenamespace=%s typename=%s %s",
					(const char*)header, (const char*)GetTreeText(true), this, m_Type, (const char*)GetVerboseType(m_Type), ((m_Type&CROM_t_Mapped)?"mapped":""),
					(const char*)get_Path(), GetNamespace().c_str(), GetSchemaTypeNamespace().c_str(),
					GetSchemaType().c_str(), 
					IsArray() ? (const char*)("(Array: " + GetArrayType() + ")") : "");
			}
		}
		else {
			fprintf(out,"%s%s 0x%p 0x%X(%s) %s path=%s",
				(const char*)header, (const char*)GetTreeText(true), this, m_Type, (const char*)GetVerboseType(m_Type), ((m_Type&CROM_t_Mapped)?"mapped":""),
				(const char*)get_Path());

		}
	}
	if(m_Generator)fprintf(out," Generator=%s",(const char*)m_Generator->get_Path());
	if(IsAttribute()){
		const char* sourceExpr=getSourceExpr();
		if(sourceExpr)fprintf(out," sourceExpr=%s",sourceExpr);
	}
	fprintf(out,"\n");
	CROM*child=GetFirstChild();
	while(child)
	{
		child->write(out);
		child=child->GetNextSibling();
	}
}

bool CROM::isRecursiveFolder(){
	if((m_Type&CROM_t_Value)!=0)return false;
	if((m_Type&CROM_t_Element)==0&&GetDisplayName()!="")return false;
	CROM* parent=m_Parent;
	while(parent){
		if(parent->m_Name==m_Name)return true;
		parent=parent->m_Parent;
	}
	return false;
}


CROM* CROM::get_ElementType(const string &schemaTypeName, const map<string,CROM*>&schemaTypes){
	map<string,CROM*>::const_iterator it=schemaTypes.find(schemaTypeName);
	if(it==schemaTypes.end())return 0;
	return it->second;
}

string CROM::get_SchemaTypeExpandedName(){
	if(!IsXml())return "";
	const konga::xml::ExpandedName schemaType(GetSchemaTypeNamespace(), GetSchemaType());
	return schemaType.toString();
}

string CROM::get_exploding_id(){
	return get_Name()+string(",")+get_SchemaTypeExpandedName();
}


map<string,bool>& CROM::get_TypeExtensions(){
	if(IsLeaf()||!IsXml()){
		throw konga::InternalErrorException("CROM::get_TypeExtensions() may only be called from XML nodes.");
	}
	return m_ext.xml->m_TypeExtensionContainer;
}
map<string,bool>& CROM::get_SubstitutionGroup(){
	if(IsLeaf()||!IsXml()){
		throw konga::InternalErrorException("CROM::get_SubstitutionGroup() may only be called from XML nodes.");
	}
	return m_ext.xml->m_SubstitutionGroupContainer;
}

void CROM::addTypeExtensions(map<string, bool> &new_set)
{
	if(IsLeaf()||!IsXml()|| new_set.size()==0)return;
	bool bHasDervedType=m_ext.xml->m_TypeExtensionContainer.size()>0;
	put_to_set(new_set, get_TypeExtensions());
	if(!bHasDervedType){
		if(m_ext.xml->m_TypeExtensionContainer.size()>0)setDerivedTypeHolder();
	}
}

void CROM::addSubstitutionGroup(map<string, bool> &new_set)
{
	if(IsLeaf())return;
	if(!IsXml())return;
	put_to_set(new_set, get_SubstitutionGroup());
}

void CROM::addChild(CROM*child){
	if(child==0)return;
	child->m_Parent=this;
	if(m_FirstChild==0){
		                           
		child->m_PrevSibling=0;
		child->m_NextSibling=0;
		m_FirstChild=child;
	}
	else if(child->IsPrecondition()){
		                      
		child->m_PrevSibling=0;
		child->m_NextSibling=m_FirstChild;
		m_FirstChild->m_PrevSibling=child;
		m_FirstChild=child;
	}
	else if(child->IsLeaf()){
		CROM*att=0;
		if(child->IsValueLeaf()){
			                                 
			CROM*ch=GetFirstChild();
			if(ch->IsPrecondition())att=ch;
		}
		else {
			                      
			for(CROM* ch=GetFirstChild();ch!=0;ch=ch->GetNextSibling()){
				if(ch->IsLeaf())att=ch;
				else break;
			}
		}
		child->m_PrevSibling=att;
		if(att==0){
			child->m_NextSibling=m_FirstChild;
			m_FirstChild->m_PrevSibling=child;
			m_FirstChild=child;
		}
		else {
			child->m_NextSibling=att->m_NextSibling;
			att->m_NextSibling=child;
		}
	}
	else {
		                    
		CROM*att=GetFirstChild();
		for(CROM* ch=att->GetNextSibling();ch!=0;ch=ch->GetNextSibling())att=ch;
		child->m_PrevSibling=att;
		child->m_NextSibling=0;
		att->m_NextSibling=child;
	}
}


   
                                                           
   
CROM::CROM(CROM* parent, CROM* to_be_cloned, const map<string,CROM*>&schemaTypes, vector<string>&exploded) :
		m_Type(to_be_cloned->m_Type),
		m_Type2(to_be_cloned->m_Type2),
		m_pTreeMapper(to_be_cloned->m_pTreeMapper),
		m_Generator(0),
		m_deList(0),
		m_instanceNumber(0),
		m_Parent(parent), 
		m_FirstChild(0), 
		m_NextSibling(0), 
		m_PrevSibling(0)
{
	if(parent!=0)parent->addChild(this);
	set_Name(to_be_cloned->get_Name());
	SetDisplayName(to_be_cloned->GetDisplayName());
	m_Comment=to_be_cloned->m_Comment;
	m_mapInfo.trans=0;

	m_CROM_ext_type=to_be_cloned->m_CROM_ext_type;
	if(IsLeaf()){
		m_ext.attr=new CROM_Attr(to_be_cloned->m_ext.attr, m_CROM_ext_type==t_CROM_Xml);
		setHasDefaultFlag();
	} else {
		switch(m_CROM_ext_type){
			case t_CROM_Xml: m_ext.xml=new CROM_Xml(to_be_cloned->m_ext.xml); break;
			case t_CROM_Text: m_ext.text=new CROM_Text(to_be_cloned->m_ext.text); break;
			case t_CROM_DB: m_ext.db=new CROM_DB(to_be_cloned->m_ext.db); break;
		}
	}

	string schemaTypeName=to_be_cloned->get_SchemaTypeExpandedName();
	if(!schemaTypeName.empty()){
		CROM*element_type=get_ElementType(schemaTypeName, schemaTypes);
		if(element_type!=0){
			exploding(element_type, schemaTypes, exploded);
		} else {
			createValueCrom(to_be_cloned);
		}
	}
	else if(!to_be_cloned->IsLeaf()&&!to_be_cloned->IsAbstract()){	
		for(CROM*child=to_be_cloned->GetFirstChild();child!=0;child=child->GetNextSibling()){
			new CROM(this, child, schemaTypes, exploded);
		}
	}
}


                                                                                      
                                             
  
void CROM::exploding(CROM*schemaType, const map<string,CROM*>&schemaTypes, vector<string>&exploded)
{
	if(IsSubstitutionGroup()){
		m_Type=1;
	} else {
		addTypeExtensions(schemaType->get_TypeExtensions());
		addSubstitutionGroup(schemaType->get_SubstitutionGroup());
	}
	if(!schemaType->IsTypeDefinition()){
		exploded.pop_back();
		return;
	}

	if(exploded.size()>0)return;                                
	string exploding_id(get_exploding_id());
	if(vector_contains(exploding_id,exploded))return;

	exploded.push_back(exploding_id);

	if(!IsDerivedTypeHolder()&&!schemaType->IsAbstract()){
		if(schemaType->IsArray())SetArrayFlag();
		for(CROM*child=schemaType->GetFirstChild();child;child = child->GetNextSibling()){
			CROM* clone=new CROM(this, child, schemaTypes, exploded);
			if(clone->GetFirstChild()==0){
				string child_type_def(child->get_SchemaTypeExpandedName());
				if(!child_type_def.empty()){
					CROM*child_element_type=get_ElementType(child_type_def,schemaTypes);
					if(child_element_type!=0){
						clone->exploding(child_element_type, schemaTypes, exploded);
					} else {
						clone->createValueCrom(child);
					}
				}
			}
		}
		
	}
	exploded.pop_back();
	return;
}

void CROM::copyRecursiveParent(const map<string,CROM*>&schemaTypes, const map<string,CROM*>&schemas){
	CROM*child=GetFirstChild();
	if(child!=0)return;
	string schemaTypeName=get_SchemaTypeExpandedName();
	if(!schemaTypeName.empty()){
		                                                                                            
		vector<string>exploded;
		CROM*schemaType=get_ElementType(schemaTypeName, schemaTypes);
		exploding(schemaType, schemaTypes, exploded);
		return;
	}
	CROM*parent=findRecursiveParent(m_Parent,(const char*)m_Name);
	if(parent==0){
		parent=findChildNodeToClone(GetRoot(),(const char*)m_Name);
		if(parent==0)return;
	}
	CROM*child0=parent->GetFirstChild();
	while(child0){
		child=new CROM(m_pTreeMapper,this,child,child0->m_Name,child0->get_Value(),
			child0->m_Type,m_CROM_ext_type);
		child->definePath();
		child0=child0->GetNextSibling();
	}
}

CROM* CROM::expandHoldersTypeNode(const char*element_type_name, map<string,CROM*>&element_type_map, map<string,CROM*>&element_map){
	if(IsLeaf())return 0;
	CROM* node=0;
	if(IsSubstitutionGroupHead()){
		                                                                                  
		for(map<string,CROM*>::iterator it=element_map.begin();it!=element_map.end();it++){
			if(strcmp(it->second->get_Name(),element_type_name)==0){
				vector<string> exploded;
				node=new CROM(this, it->second, element_type_map, exploded);
				break;
			}
		}
	}
	else if(IsDerivedTypeHolder()){
		                                                                                  
		for(map<string,CROM*>::iterator it=element_type_map.begin();it!=element_type_map.end();it++){
			if(strcmp(it->second->get_Name(),element_type_name)==0){
				vector<string> exploded;
				         
				node=new CROM(this, it->second, element_type_map, exploded);
				break;
			}
		}
	}
	return node;
}


void CROM::AddConditions(bool bCheckForRecursive          )
{
	if( bCheckForRecursive && (m_Type&CROM_t_Element)!=0 ){ 
		                                                             
		if(isRecursiveFolder()){
			CROM*child=GetFirstChild();
			while(child){
				CROM*next=child->GetNextSibling();
				child->DeleteNode();
				child=next;
			}
			return;
		}
	}
	if((m_Type&CROM_t_Element)!=0&&!m_Comment.IsEmpty()){
		m_Name+=ConditionChar;
		m_Name+=m_Comment;
		m_Name.Replace('.','?');
	}

	bool add_precondition=(m_Type&CROM_t_OneOf)!=0;
	CROM*child=GetFirstChild(),*sibling;
	while(child){                                                       
		if((child->m_Type&CROM_t_Element)!=0&&!child->GetDisplayName().IsEmpty()){
			if(child->m_Comment.IsEmpty()||isNum(child->m_Comment)==false){                                            
				char*name=konga::strsave(child->m_Name);
				int i=1;
				sibling=child->GetNextSibling();
				while(sibling){
					if(strcmp(name,sibling->m_Name)==0){
						i++;
						if(sibling->m_Comment.IsEmpty())sibling->m_Comment.Format("%d",i);
					}
					sibling=sibling->GetNextSibling();
				}
				if(i>1&&child->m_Comment.IsEmpty())child->m_Comment="1";
				delete[]name;
			}
		}
		child=child->GetNextSibling();
	}
	child=GetFirstChild();
	bool hasSibling=true;
	while(child){
		sibling=child->GetNextSibling();
		hasSibling=sibling!=0;
		int type=child->GetType();
		if(type==CROM_t_Value&&add_precondition){
			new CROM(m_pTreeMapper,this,0,0,hasSibling?0:"true",CROM_t_Precondition,m_CROM_ext_type);
		} else if(type==0&&add_precondition){
			new CROM(m_pTreeMapper,child,0,0,hasSibling?0:"true",CROM_t_Precondition,m_CROM_ext_type);
		} else if((type&CROM_t_Element)!=0&&add_precondition){
			new CROM(m_pTreeMapper,child,0,0,hasSibling?0:"true",CROM_t_Precondition,m_CROM_ext_type);
		} else if(type&CROM_t_Attribute){
			if(!m_Comment.IsEmpty()){				
				konga::StringTokenizer t((const char*)m_Comment,".");
				while(t.NextToken()){
					const char* token=t.token();
					if(token[0]=='.')continue;
					if(strcmp(token,m_Name)==0)continue;
					string value="|";
					value+=token;
					value+='|';
					if(strstr(child->get_Domain(),value.c_str())){
						child->m_ext.attr->m_Value=token;                                       
						break;
					}
				}
			}
		}

		child->AddConditions(bCheckForRecursive);
		child=sibling;
	}
}
char CROM::GetNodeChar()
{
	return ((m_Type&CROM_t_Loop)!=0)?LoopChar:NonLoopChar;
}

bool CROM::isNum(const char*str)
{
	for(int i=0,l=(int)strlen(str);i<l;i++)if(isdigit(str[i])==false)return false;
	return true;
}


CROM*CROM::getPrecondition(){
	CROM*child=GetFirstChild();
	while(child){
		if(child->m_Type&CROM_t_Precondition)return child;
		child=child->GetNextSibling();
	}
	return 0;
}

void CROM::checkLoopNode(){
	if((m_Type&CROM_t_Loop)!=0){
		setLoopNode();
	}
	CROM*child=GetFirstChild();
	while(child){
		if((m_Type&CROM_t_AttValuePrecondition)==0)child->checkLoopNode();
		child=child->GetNextSibling();
	}
}


CROM*CROM::GetLoopNode(){
	CROM*parent=this,*parent0=this;
	while(parent){
		if((parent->m_Type&CROM_t_LoopNode)!=0)return parent;
		parent0=parent;
		parent=parent->GetParent();
	}
	return parent0;                                        
}


bool CROM::hasValue() const {
	CROM*child=GetFirstChild();
	while(child){
		if(child->m_Type&CROM_t_Value)return true;
		child=child->GetNextSibling();
	}
	return false;
}

bool CROM::HasSubElement() {
	if(m_Type2&CROM_t_DefineHasSubElement)return (m_Type2&CROM_t_HasSubElement)!=0;
	CROM*child=GetFirstChild();
	while(child){
		if(child->m_Type&CROM_t_Element){
			m_Type2 |= (CROM_t_DefineHasSubElement|CROM_t_HasSubElement);
			return true;
		}
		child=child->GetNextSibling();
	}
	m_Type2 |= CROM_t_DefineHasSubElement;
	return false;
}


void CROM::setLoopNode(bool flag){
	if(flag){
		m_Type|=CROM_t_LoopNode;
	} else {
		m_Type&=~CROM_t_LoopNode;
	}
}

int CROM::updateGenerator(CROM*source){
	if(source==0)return 0;
	source=source->GetLoopNode();
	if(m_Generator==0){
		m_Generator=source;
		return 0;
	}
	int n1=(int)source->get_Path().GetLength(),n2=(int)m_Generator->get_Path().GetLength();
	if(n1>n2){
		if(strncmp(source->get_Path(),m_Generator->get_Path(),n2)==0){
			m_Generator=source;
			return 0;
		} else {
			return UPDATE_GENERATOR_ERR1;                     
		}
	} else {
		if(strncmp(source->get_Path(),m_Generator->get_Path(),n1)==0){
			return 0;
		} else {
			return UPDATE_GENERATOR_ERR1;                     
		}
	}
	return 0;
}

KString CROM::getPropertyStr(int type){
	KString tmp;
	KString typeStr="";
	if(type>0)typeStr.Format("%d",type);
	if(IsLeaf()){
		                                                                           
		if(IsPrimaryKey())tmp+='p';
		if(IsForeignKey())tmp+='f';
		if(IsPrecondition())tmp+='P';
		if(IsOptionalNode())tmp+='?';
		if(IsOuterJoin())tmp+='*';
	} else {
		                                                                      
		if(IsOneToOne())tmp+='$';
		if(IsOuterJoin()||IsCollection())tmp+='*';
		if(IsOptionalNode())tmp+='?';
		if(IsAtLeastOne())tmp+='+';
		tmp+=typeStr;
	}
	return tmp;
}

int	CROM::GetNodeLevel()
{
	CROM* pTrav = this->GetParent();
	int iLevel = 0;

	while(0 != pTrav)
	{
		iLevel++;
		pTrav = pTrav->GetParent();
	}

	return iLevel - 1;
}




CROM *CROM::FindObjectInChild(int type)
{
	CROM*child=GetFirstChild();
	while(child){
		if((child->m_Type&type)!=0)
			return child;
		child=child->GetNextSibling();
	}
	return child;                      
}

                                                                 
                                                                
                                                                                
                                 
CROM* CROM::DelUnMappedColumns(bool& bDeletedColumn, bool bSourceIsFlat)
{
	if (isManualSqlStatement()) {
		                                                      
		return 0;
	}
	if( IsAttribute() && !(IsFieldMapped() || IsPrimaryKey() || IsForeignKey() || IsUpdateKey() || IsSourceUpdateColumn()) )
	{
		bool bDelete = true;
		if(HasSQLOrderBy())
		{
			if(bSourceIsFlat)		                                                                     
				bDelete = false;	                                              
			else
			{
				                                                                                      
				                                                                                   
				                         
				if(m_Parent->GetParent() == GetRoot())
				{
					CROM* pNext = m_NextSibling;
					while(bDelete && pNext != 0)
					{
						if(pNext->IsElement() && (pNext->IsAtLeastOne() || pNext->IsOneToOne()))
							bDelete = false;
						pNext = pNext->GetNextSibling();
					}
				}
			}
		}
		if(bDelete)
		{
			CROM* pNode = GetNextSibling();
			KString csParentName = this->GetParent()->GetDisplayName();
			getTreeMapper()->getViewLog()->DebugString("Deleting the column  %s  %s", (const char*)(this->GetDisplayName()), 
				csParentName.IsEmpty() ? "" : (const char*)("from  " + csParentName));
			this->DeleteNode();
			bDeletedColumn = true;
			return pNode;
		}
	}

	CROM* pNext = GetFirstChild();
	while(pNext != 0)
	{
		CROM* pSibling = pNext->GetNextSibling();	
		CROM* pNew = pNext->DelUnMappedColumns(bDeletedColumn, bSourceIsFlat);
		if(pSibling != 0)	                                            
		{						                                             
			if(pNew != 0)	                                              
				pNext = pNew;	                                                              
			else
				pNext = pNext->GetNextSibling();
		}
		else
			break;
	}

	return 0;
}

                                                             
bool CROM::HasSQLOrderBy() const
{
	if (m_CROM_ext_type != t_CROM_DB) {
		throw konga::InternalErrorException("CROM::HasSQLOrderBy may only be called from CROM_DB nodes.");
	}
	if(IsAttribute())
		return m_Parent!=0 && m_Parent->ChildColumnInOrderBy(GetDisplayName());

	return get_sqlOrderBy()[0]!=0;
}

bool CROM::ChildColumnInOrderBy(const char* szColumnName)
{
	if(IsElement())
	{
		const char* sqlOrderBy(get_sqlOrderBy());
		if(sqlOrderBy[0]!=0){
			konga::StringTokenizer tk(sqlOrderBy, ", ");
			while(tk.NextToken()){
				if(strcmp(tk.token(), szColumnName)==0)return true;
			}
		}
	}
	return false;
}

KString CROM::GetSQLOrderBy() const
{
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetSQLOrderBy may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type != t_CROM_DB) {
		throw konga::InternalErrorException("CROM::GetSQLOrderBy may only be called from Text/DB nodes.");
	}
	if(!m_ext.db->m_SQLFilter.IsEmpty())
	{
		int k = m_ext.db->m_SQLFilter.Find("ORDER BY");
		if(k != -1)
			return m_ext.db->m_SQLFilter.Mid(k);
	}
	return KString("");
}

void CROM::clearFilter_recursive(){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::clearFilter_recursive may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type != t_CROM_DB) {
		throw konga::InternalErrorException("CROM::clearFilter_recursive may only be called from Text/DB nodes.");
	}
	if(!m_ext.db->m_SQLFilter.IsEmpty())m_ext.db->m_SQLFilter.Empty();
	if(!m_ext.db->m_SQLOrderBy.IsEmpty())m_ext.db->m_SQLOrderBy.Empty();
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf()==false)child->clearFilter_recursive();
	}

}


bool CROM::HasXMLTypeColumn() const
{
	if(IsElement())
	{
		CROM* pColumn = m_FirstChild;
		while(pColumn != 0 && pColumn->IsAttribute())
		{
			if(pColumn->IsXMLType())
				return true;
			pColumn = pColumn->GetNextSibling();
		}
	}
	return false;
}

                                                      
bool CROM::IsXMLType()
{
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::IsXmlType may only be called from leaf nodes.");
	}
	return (IsAttribute() && stricmp(m_ext.attr->m_ValueType, "XMLType")==0 );
}


CROM* CROM::FindChild(int nodeType, const string& stNodeName)
{
	CROM* child = GetFirstChild();
	while( child )
	{
		if( (strcmp(child->m_Name, stNodeName.c_str()) == 0 || strcmp(child->GetDisplayName(), stNodeName.c_str()) == 0)
			&& (nodeType == CROM_t_AnyThing || (child->m_Type & nodeType) != 0) )
		{
			return child;
		}
		child = child->GetNextSibling();
	}
	return 0;
}

CROM* CROM::FindChild(int nodeType, const string& stNodeName, const string& stNamespace)
{
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::FindChild(int, const string&m const string&) may only be called from Xml nodes.");
	}

	CROM* child = GetFirstChild();
	while( child )
	{
		if( (strcmp(child->m_Name, stNodeName.c_str()) == 0 || strcmp(child->GetDisplayName(), stNodeName.c_str()) == 0)
			&& strcmp(child->GetNamespace().c_str(), stNamespace.c_str()) == 0 &&
			(nodeType == CROM_t_AnyThing || (child->m_Type & nodeType) != 0) )
		{
			return child;
		}
		child = child->GetNextSibling();
	}
	return 0;
}

CROM* CROM::FindDescendant(int nodeType, const string& stNodeName, const string& stNamespace)
{
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::FindDescendant(int, const string&m const string&) may only be called from Xml nodes.");
	}
	CROM* pDesc = FindChild(nodeType, stNodeName, stNamespace);
	if( pDesc == 0 )
	{
		CROM* child = GetFirstChild();
		while( child )
		{
			pDesc = child->FindDescendant(nodeType, stNodeName, stNamespace);
			if( pDesc != 0 )
				break;
			child = child->GetNextSibling();
		}
	}
	return pDesc;
}

int CROM::CountChildren(int childType)
{
	int n = 0;
	CROM* child = GetFirstChild();
	while( child )
	{
		if( childType == CROM_t_AnyThing || (child->m_Type & childType) != 0 )
			++n;
		child = child->GetNextSibling();
	}
	return n;
}

void CROM::CloneCROMChildren(const CROM* source, CROM* target, const char* recursive_name)
{
	for(CROM* child = source->GetFirstChild(); child != 0 ; child = child->GetNextSibling())
	{
		CROM* clone = new CROM(child, recursive_name);
		AddToParent(target, clone);
	}
}

void CROM::AddToParent(CROM* parent, CROM* child)
{
	CROM* prevSibling = parent->GetLastChild();
	child->SetParent(parent);
	if (prevSibling == 0)
	{
		parent->SetFirstChild(child);
	}
	else
	{
		child->SetPreviousSibling(prevSibling);
		prevSibling->SetNextSibling(child);
	}
}

void CROM::AddExtension(const string& qualifiedTypeName) {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::AddExtension may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::AddExtension may only be called from Xml nodes.");
	}
	m_ext.xml->m_TypeExtensionContainer[qualifiedTypeName] = true;
}

void CROM::GetAllDerivedTypes(vector<string>& derivedTypeNames) const {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetAllDerivedTypes may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::GetAllDerivedTypes may only be called from Xml nodes.");
	}
	for (map<string, bool>::const_iterator cit = m_ext.xml->m_TypeExtensionContainer.begin(); 
						cit != m_ext.xml->m_TypeExtensionContainer.end(); ++cit) {
		derivedTypeNames.push_back(cit->first);
	}
}

void CROM::AddSubstitutionGroupElement(const string& stElementName) {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::AddSubstitutionGroupElement may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::AddSubstitutionGroupElement may only be called from Xml nodes.");
	}
	m_ext.xml->m_SubstitutionGroupContainer[stElementName] = true;
}

void CROM::GetSubstitutionGroupElements(vector<string> &elementNames) const {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetSubstitutionGroupElements may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::GetSubstitutionGroupElements may only be called from Xml nodes.");
	}
	for (map<string, bool>::const_iterator cit = m_ext.xml->m_SubstitutionGroupContainer.begin(); 
						cit != m_ext.xml->m_SubstitutionGroupContainer.end(); ++cit) {
		elementNames.push_back(cit->first);
	}
}

                                                       
                                                 
                                                     
CROM*CROM::getNextAncestor(CROM*ancestor){
	if(!IsLeaf() && m_Parent==ancestor)return this;
	KString tmp=get_Path().Mid(ancestor->get_Path().GetLength());
	int i=(int)strcspn(tmp,"$.");
	tmp=tmp.Left(i);
	for(CROM*child=ancestor->GetFirstChild();child;child=child->GetNextSibling()){
		if(child->IsPrecondition()||child->IsAttribute())continue;
		if(child->m_Name==tmp)return child;
	}
	return 0;
}

string CROM::getUniversalName(){
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::getUniversalName may only be called from Xml nodes.");
	}
	konga::xml::ExpandedName expName(GetNamespace(), (const char*) GetDisplayName());
	return expName.toString();
}

string CROM::GetSchemaType() const {
	if (IsLeaf()) {
		return m_ext.attr->get_SchemaType();
	}
	else if (IsXml()) {
		if (m_ext.xml->get_SchemaType().length() > 0) {
			return m_ext.xml->get_SchemaType();
		}
		CROM* typeDef = m_ext.xml->m_pComplexTypeDefinition;
		if (typeDef != 0) {
			return typeDef->GetSchemaType();
		}
		return "";
	}
	else {
		throw konga::InternalErrorException("CROM::GetSchemaType may only be called from a leaf or Xml node.");
	}
}

string CROM::GetSchemaTypeNamespace() const {
	if (IsLeaf()) {
		return m_ext.attr->get_SchemaTypeNamespace();
	}
	else if (IsXml()) {
		if (m_ext.xml->get_SchemaTypeNamespace().length() > 0) {
			return m_ext.xml->get_SchemaTypeNamespace();
		}
		CROM* typeDef = m_ext.xml->m_pComplexTypeDefinition;
		if (typeDef != 0) {
			return typeDef->GetSchemaTypeNamespace();
		}
		return "";
	}
	else {
		throw konga::InternalErrorException("CROM::GetSchemaTypeNamespace may only be called from a leaf or Xml node.");
	}
}

void CROM::SetArray(KString sArrayType) { 
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetArray may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::SetArray may only be called from Xml nodes.");
	}
	m_Type |= CROM_t_Array; m_ext.xml->m_sArrayType = sArrayType; 
}

KString& CROM::GetArrayType() const { 
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetArrayType may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::GetArrayType may only be called from Xml nodes.");
	}
	return m_ext.xml->m_sArrayType; 
}

int CROM::get_SqlDataType() const{ 
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::get_SqlDataType may only be called from leaf nodes.");
	}
	if(m_CROM_ext_type!=t_CROM_DB){
		return 0;
	}
	return m_ext.attr->m_DbColType;
}

void CROM::set_SqlDataType(int DbTypeId) {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_SqlDataType may only be called from leaf nodes.");
	}
    m_ext.attr->m_DbColType = DbTypeId;
}

int CROM::get_DbColSize() const {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetDbColSize may only be called from leaf nodes.");
	}
	if(m_CROM_ext_type!=t_CROM_DB){
		return 0;
	}
	return m_ext.attr->m_DbColSize;
}

void CROM::set_DbColSize(int DbColSize) {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetDbColSize may only be called from leaf nodes.");
	}
	if(m_CROM_ext_type == t_CROM_DB){
		m_ext.attr->m_DbColSize = DbColSize;
	}
}

int CROM::get_DbDecimalDigits() const {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetDbDecimalDigits may only be called from leaf nodes.");
	}
	if(m_CROM_ext_type != t_CROM_DB){
		return 0;
	}
	return m_ext.attr->m_DbDecimalDigits;
}

void CROM::set_DbDecimalDigits(int DbDecimalDigits) {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetDbDecimalDigits may only be called from leaf nodes.");
	}
	if(m_CROM_ext_type == t_CROM_DB){
		m_ext.attr->m_DbDecimalDigits = DbDecimalDigits;
	}	
}

CROM* CROM::get_PkLink(){
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::get_PkLink may only be called from leaf nodes.");
	}
	if(m_CROM_ext_type != t_CROM_DB){
		return 0;
	}
	return m_ext.attr->m_PkLink;
}

void CROM::set_PkLink(CROM* pk) {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_PkLink may only be called from leaf nodes.");
	}
	if(m_CROM_ext_type == t_CROM_DB){
		m_ext.attr->m_PkLink = pk;
	}	
}



void CROM::set_dbSchema(const char*schema)
{
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_dbSchema may not be called from leaf nodes.");
	}
	if(m_CROM_ext_type == t_CROM_DB){
		m_ext.db->set_dbSchema(schema);
	}	
}

const string& CROM::get_manualSQL() const {
	if (IsLeaf() || m_CROM_ext_type!=t_CROM_DB) {
		throw konga::InternalErrorException("CROM::get_manualSQL may only be called from DB nodes.");
	}
	return m_ext.db->get_manualSQL();
}

bool CROM::isManualSqlStatement() const {
	if (IsLeaf() || m_CROM_ext_type!=t_CROM_DB) {
		return false;
	}
	return !m_ext.db->get_manualSQL().empty();
}

void CROM::set_manualSQL(const string& sql) {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_manualSQL may not be called from leaf nodes.");
	}
	if(m_CROM_ext_type == t_CROM_DB){
		m_ext.db->set_manualSQL(sql);
	}	
}

void CROM::set_sqlTableName(const char*tableName)
{
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_dbSchema may not be called from leaf nodes.");
	}
	if(m_CROM_ext_type == t_CROM_DB){
		m_ext.db->set_sqlTableName(tableName);
	}	
}


const KString& CROM::GetValue() const {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetValue may only be called from leaf nodes.");
	}
	return m_ext.attr->m_Value;
}

namespace {
	const char defaultValueType[] = "String";
}

const char* CROM::GetValueType() const {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetValueType may only be called from leaf nodes.");
	}
	if(m_ext.attr->m_ValueType.IsEmpty())return defaultValueType;
	return (const char*)m_ext.attr->m_ValueType;
}

bool CROM::IsStringType() const {
	return (IsAttribute() && stricmp(m_ext.attr->m_ValueType, "String")==0 );
}

void CROM::SetValue(const char*str){
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetValue may only be called from leaf nodes.");
	}
	m_ext.attr->m_Value=str;
}

short CROM::GetFieldLength() const {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetFieldLength may only be called from leaf nodes.");
	}
	return m_ext.attr->m_fieldLength;
}

void CROM::SetFieldLength(short iLen) { 
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetFieldLength may only be called from leaf nodes.");
	}
	m_ext.attr->m_fieldLength = iLen;
}

short CROM::GetFieldPosition() const { 
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetFieldPosition may only be called from leaf nodes.");
	}
	return m_ext.attr->m_fieldPos;
}
void CROM::SetFieldPosition(short iPos) { 
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetFieldPosition may only be called from leaf nodes.");
	}
	m_ext.attr->m_fieldPos = iPos;
}

void CROM::SetValueType(KString ValType) { 
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetValueType may only be called from leaf nodes.");
	}
	m_ext.attr->m_ValueType = ValType;
}

void CROM::setDefault(const char*sDefault){
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::setDefault may only be called from leaf nodes.");
	}
	m_ext.attr->m_DefaultValue=sDefault;
	setHasDefault();
}

void CROM::SetMinUse(int minUse){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetMinUse may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::SetMinUse may only be called from Xml nodes.");
	}
	m_ext.xml->m_iMinUse=minUse;
}

int CROM::GetMinUse(){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetMinUse may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::GetMinUse may only be called from Xml nodes.");
	}
	return m_ext.xml->m_iMinUse;
}

void CROM::SetMaxUse(int maxUse){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetMaxUse may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::SetMaxUse may only be called from Xml nodes.");
	}
	m_ext.xml->m_iMaxUse=maxUse;
}

int CROM::GetMaxUse(){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetMaxUse may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::GetMaxUse may only be called from Xml nodes.");
	}
	return m_ext.xml->m_iMaxUse;
}

void CROM::SetDBSchema(const char* db_schema){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetDBSchema may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type != t_CROM_DB) {
		throw konga::InternalErrorException("CROM::SetDBSchema may only be called from CROM_DB nodes.");
	}
	m_ext.db->m_Schema = db_schema;
}

void CROM::AddSQLFilter(const char* sWhere){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::AddSQLFilter may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type != t_CROM_DB) {
		throw konga::InternalErrorException("CROM::AddSQLFilter may only be called from CROM_DB nodes.");
	}
	m_ext.db->m_SQLFilter += sWhere;
}

KString& CROM::GetDefaultValue() const {
	static KString blank;
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetDefaultValue may only be called from leaf nodes.");
	}
	return hasDefault() ? m_ext.attr->m_DefaultValue : blank;
}
KString& CROM::GetFormat() const {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetFormat may only be called from leaf nodes.");
	}
	return m_ext.attr->m_Format;
}
void CROM::SetFormat(const char* format) { 
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetFormat may only be called from leaf nodes.");
	}
	m_ext.attr->m_Format = format; 
}

void CROM::SetNamespace(const string& sNamespace) { 
	if (IsLeaf()) {
		m_ext.attr->set_Namespace(sNamespace);
	}
	else if (IsXml()) {
		m_ext.xml->set_Namespace(sNamespace); 
	}
	else {
		throw konga::InternalErrorException("CROM::SetNamespace may only be called from a leaf or Xml node.");
	}
}

string CROM::GetNamespace() const {
	if (IsLeaf()) {
		return m_ext.attr->get_Namespace();
	}
	else if (IsXml()) {
		return m_ext.xml->get_Namespace();
	}
	else {
		throw konga::InternalErrorException("CROM::GetNamespace may only be called from a leaf or Xml node.");
	}
}

bool CROM::IsNamespaceQualified() const {
	if (IsLeaf()) {
		return m_ext.attr->get_NSQualify();
	}
	else if (IsXml()) {
		return m_ext.xml->get_NSQualify();
	}
	else {
		throw konga::InternalErrorException("CROM::IsNamespaceQualified may only be called from a leaf or Xml node.");
	}
}

void CROM::SetNamespaceQualified(bool value) {
	if (IsLeaf()) {
		m_ext.attr->set_NSQualify(value);
	}
	else if (IsXml()) {
		m_ext.xml->set_NSQualify(value);
	}
	else {
		throw konga::InternalErrorException("CROM::SetNamespaceQualified may only be called from a leaf or Xml node.");
	}
}

void CROM::setSchemaType(const string& sTypeNamespace, const string& sType) {
	if (IsLeaf()) {
		m_ext.attr->set_SchemaType(sType);
		m_ext.attr->set_SchemaTypeNamespace(sTypeNamespace);
	}
	else if (IsXml()) {
		m_ext.xml->set_SchemaType(sType);
		m_ext.xml->set_SchemaTypeNamespace(sTypeNamespace);
	}
	else {
		throw konga::InternalErrorException("CROM::SetSchemaType may only be called from a leaf or Xml node.");
	}
}

void CROM::MarkAsAnyElement(const string& stNamespace) {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::MarkAsAnyElement may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::MarkAsAnyElement may only be called from Xml nodes.");
	}
	m_Type |= CROM_t_AnyElement;
	m_ext.xml->set_AnyElementNamespace(stNamespace);
}

string CROM::GetAnyElementNamespace() { 
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::GetAnyElementNamespace may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::GetAnyElementNamespace may only be called from Xml nodes.");
	}
	return m_ext.xml->get_AnyElementNamespace(); 
}

bool CROM::hasExtension() const {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::hasExtension may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::hasExtension may only be called from Xml nodes.");
	}
	return !m_ext.xml->m_TypeExtensionContainer.empty();
}

void CROM::SetAbstract() { 
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::SetAbstract may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::SetAbstract may only be called from Xml nodes.");
	}
	m_ext.xml->set_Abstract(true); 
}

bool CROM::IsAbstract() { 
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::IsAbstract may not be called from leaf nodes.");
	}
	if (!IsXml()) {
		throw konga::InternalErrorException("CROM::IsAbstract may only be called from Xml nodes.");
	}
	return m_ext.xml->IsAbstract(); 
}

void CROM::set_strQual(char c) {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_strQual may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type == t_CROM_Text) {
		if(m_ext.text==0)m_ext.text=new CROM_Text();
		m_ext.text->set_strQual(c);
	}
	else if(m_CROM_ext_type == t_CROM_DB) {
	}
	else {
		throw konga::InternalErrorException("CROM::set_strQual may only be called from CROM_Text or CROM_DB nodes.");
	}
}

void CROM::set_delimiter(char c) {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_delimiter may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type == t_CROM_Text) {
		if(m_ext.text==0)m_ext.text=new CROM_Text();
		m_ext.text->set_delimiter(c);
	}
	else if(m_CROM_ext_type == t_CROM_DB){
	}
	else {
		throw konga::InternalErrorException("CROM::set_delimiter may only be called from CROM_Text or CROM_DB nodes.");
	}
}

char CROM::get_strQual() const {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::get_strQual may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type == t_CROM_Text) {
		if(m_ext.text==0)return 0;
		return m_ext.text->get_strQual();
	}
	else if(m_CROM_ext_type == t_CROM_DB || m_CROM_ext_type == t_CROM_Ldap){
		return '"';
	}
	else {
		throw konga::InternalErrorException("CROM::get_strQual may only be called from CROM_Text or CROM_DB nodes.");
	}
}

char CROM::get_numQual() const {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::get_numQual may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type == t_CROM_Text) {
		if(m_ext.text==0)return 0;
		return m_ext.text->get_numQual();
	}
	else if(m_CROM_ext_type == t_CROM_DB || m_CROM_ext_type==t_CROM_Ldap){
		return 0;
	}
	else {
		throw konga::InternalErrorException("CROM::get_numQual may only be called from CROM_Text or CROM_DB nodes.");
	}
}

char CROM::get_delimiter() const {
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::get_delimiter may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type == t_CROM_Text) {
		if(m_ext.text==0)return 0;
		return m_ext.text->get_delimiter();
	}
	else if(m_CROM_ext_type == t_CROM_DB || m_CROM_ext_type==t_CROM_Ldap){
		return DB_DELIMITER_CHAR;
	}
	else {
		throw konga::InternalErrorException("CROM::get_delimiter may only be called from CROM_Text or CROM_DB nodes.");
	}
}

void CROM::set_fieldPos(short pos) {
	if (IsLeaf()) {
		m_ext.attr->set_fieldPos(pos);
	}
	else if (m_CROM_ext_type==t_CROM_Text) {
		m_ext.text->set_identPos(pos);
	}
	else {
		throw konga::InternalErrorException("CROM::set_fieldPos may only be called from leaf nodes or CROM_Text nodes.");
	}
}

void CROM::set_fieldLength(short length) {
	if (IsLeaf()) {
		m_ext.attr->set_fieldLength(length);
	}
	else if (m_CROM_ext_type==t_CROM_Text) {
		m_ext.text->set_identLength(length);
	}
	else {
		throw konga::InternalErrorException("CROM::set_fieldLength may only be called from leaf nodes or CROM_Text nodes.");
	}
}

short CROM::get_fieldPos() const {
	if (IsLeaf()) {
		return m_ext.attr->get_fieldPos();
	}
	else if (m_CROM_ext_type==t_CROM_Text) {
		return m_ext.text->get_identPos();
	}
	else if(m_CROM_ext_type==t_CROM_DB || m_CROM_ext_type==t_CROM_Ldap){
		return 0;
	}
	else {
		throw konga::InternalErrorException("CROM::get_fieldPos may only be called from leaf nodes or CROM_Text nodes.");
	}
}

short CROM::get_fieldLength() const {
	if (IsLeaf()) {
		return m_ext.attr->get_fieldLength();
	}
	else if (m_CROM_ext_type==t_CROM_Text) {
		return m_ext.text->get_identLength();
	}
	else if(m_CROM_ext_type==t_CROM_DB || m_CROM_ext_type==t_CROM_Ldap){
		return 0;
	}
	else {
		throw konga::InternalErrorException("CROM::get_fieldLength may only be called from leaf nodes or CROM_Text nodes.");
	}
}

void CROM::set_LinkToPrimaryKey(const char* LinkToPrimaryKey){
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_LinkToPrimaryKey may only be called from leaf nodes.");
	}
	m_ext.attr->m_LinkToPrimaryKey=LinkToPrimaryKey;
}

const char* CROM::get_LinkToPrimaryKey(){
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::get_LinkToPrimaryKey may only be called from leaf nodes.");
	}
	return (const char*)m_ext.attr->get_LinkToPrimaryKey();
}

const char* CROM::get_sqlFilter() const {
	if (IsLeaf() || m_CROM_ext_type!=t_CROM_DB) {
		throw konga::InternalErrorException("CROM::get_sqlFilter may only be called from DB nodes.");
	}
	return (const char*)m_ext.db->get_sqlFilter();
}

void CROM::set_sqlFilter(const char* filter){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_sqlFilter may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type != t_CROM_DB) {
		throw konga::InternalErrorException("CROM::set_sqlFilter may only be called from CROM_DB nodes.");
	}
	m_ext.db->set_sqlFilter(filter);
}

const char* CROM::get_sqlOrderBy() const {
	if (IsLeaf() || m_CROM_ext_type!=t_CROM_DB) {
		throw konga::InternalErrorException("CROM::get_sqlSortBy may only be called from DB nodes.");
	}
	return (const char*)m_ext.db->get_sqlOrderBy();
}

void CROM::set_sqlOrderBy(const char* sOrderBy){
	if (IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_sqlOrderBy may not be called from leaf nodes.");
	}
	if (m_CROM_ext_type != t_CROM_DB) {
		throw konga::InternalErrorException("CROM::set_sqlOrderBy may only be called from CROM_DB nodes.");
	}
	m_ext.db->set_sqlOrderBy(sOrderBy);
}

const char* CROM::get_sqlTableName() const {
	if (IsLeaf() || m_CROM_ext_type!=t_CROM_DB) {
		throw konga::InternalErrorException("CROM::get_sqlTableName may only be called from DB nodes.");
	}
	return m_ext.db->get_sqlTableName();
}

const char* CROM::get_dbSchema() const {
	if (IsLeaf() || m_CROM_ext_type!=t_CROM_DB) {
		throw konga::InternalErrorException("CROM::get_dbSchema may only be called from DB nodes.");
	}
	return (const char*)m_ext.db->get_dbSchema();
}

void CROM::set_Domain(const char*domain) {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_Domain may only be called from leaf nodes.");
	}
	m_ext.attr->set_Domain(domain);
}

void CROM::set_ValueType(const char*value_type) {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_ValueType may only be called from leaf nodes.");
	}
	m_ext.attr->set_ValueType(value_type);
	if(m_CROM_ext_type==t_CROM_Xml){
		m_ext.attr->patch();
	}
}

void CROM::set_Format(const char*Format) {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_Format may only be called from leaf nodes.");
	}
	m_ext.attr->set_Format(Format);
}

void CROM::set_Value(const char*value) {
	if (!IsLeaf()) {
		throw konga::InternalErrorException("CROM::set_Value may only be called from leaf nodes.");
	}
	m_ext.attr->set_Value(value);
}

const KString& CROM::get_Path() {
	if(m_Path.IsEmpty()){
		if(IsFlat()){
			m_Path=get_Name();
		}
		else {
			stack<CROM*>st;
			CROM*parent=this;
			if((m_Type&CROM_t_AttValuePrecondition)!=0)parent=GetParent();
			while(parent){
				st.push(parent);
				parent=parent->GetParent();
			}
			m_Path="";
			while(st.size()>0)
			{
				parent=st.top();
				if(!parent->m_Name.IsEmpty()){
					m_Path+=parent->m_Name;
					m_Path+=parent->GetNodeChar();
				}
				st.pop();
			}
			if((m_Type&CROM_t_AttValuePrecondition)!=0){
				if((m_Type&CROM_t_Precondition)==0){
					m_Path+=m_Name;
				} else {
					m_Path+=c_precondition;
				}
			}
		}
	}
	return m_Path;
}

void CROM::SetFlatFlag(bool b){
	if(b){
		m_Type |= CROM_t_Flat;
	}
	else {
		m_Type &= ~CROM_t_Flat;
	}
	CROM* child = GetFirstChild();
	while( child ) {
		child->SetFlatFlag(b);
		child=child->GetNextSibling();
	}
}

                                                                         
                                                          
void CROM::resetAddedFlag(){
	m_Type&=(~CROM_t_Added);
	for(CROM*child=GetFirstChild();child;child=child->GetNextSibling()){
		child->resetAddedFlag();
	}
}

CROM* CROM::FindChildFolder(const char* name){
	for(CROM*child=GetFirstChild(); child ;child=child->GetNextSibling()){
		if(child->IsLeaf())continue;
		if(!child->IsElement()){
			CROM*child1=child->FindChildFolder(name);
			if(child1!=0)return child1;
		}
		if(strcmp(child->get_Name(),name)==0 || child->m_display_name==name){
			return child;
		}
	}
	return 0;
}


CROM* CROM::FindChildFolder(const char* name, bool isLoop){
	for(CROM*child=GetFirstChild(); child ;child=child->GetNextSibling()){
		if(child->IsLeaf())continue;
		if(strcmp(child->get_Name(),name)==0 && child->isLoopNode()== isLoop){
			return child;
		}
	}
	return 0;
}

CROM* CROM::FindChildLeaf(const char* name) const {
	if(strcmp(name,c_precondition)==0){
		for(CROM*child=GetFirstChild(); child && child->IsLeaf(); child=child->GetNextSibling()){
			if(child->IsPrecondition())return child;
		}
	} else {
		for(CROM*child=GetFirstChild();child;child=child->GetNextSibling()){
			if(!child->IsLeaf())break;
			if(child->IsPrecondition())continue;
			if(name[0]==0 && child->IsValueLeaf())return child;
			if(strcmp(child->get_Name(), name)==0 || child->m_display_name==name){
				return child;
			}
		}
	}
	return 0;
}

CROM* CROM::FindPrecondition() const {
	for(CROM*child=GetFirstChild(); child && child->IsLeaf(); child=child->GetNextSibling()){
		if(child->IsPrecondition())return child;
	}
	return 0;
}

CROM* CROM::getChildFolder_recursive(const string&name){
	if(get_Name()==name && !IsLeaf() && GetFirstChild()!=0)return this;
	CROM* node=0;
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if((node=child->getChildFolder_recursive(name))!=0)return node;
	}
	return 0;
}

const CROM* CROM::getParent_recursive(const string&name) const {
	const CROM* parent=m_Parent;
	int iFound=0;
	while(parent!=0){
		if(parent->GetName()==name){
			if(parent->isAddedCopy()==false)iFound++;
			if(iFound==2)return parent;
		}
		parent=parent->m_Parent;
	}
	if(parent==0){
		CROM*root=m_Parent;
		while(root->m_Parent)root=root->m_Parent;
		parent=root->getChildFolder_recursive(name);
	}
	return parent;
}


void CROM::expandRecursiveNode(){
	string name=get_Name();
	if(name.length()==0)return;
	                                                                
	const CROM* parent=getParent_recursive(name);
	if(parent==0)return;
	CloneCROMChildren(parent, this, name.c_str());
}

CROM* CROM::createValueCrom(CROM* schema_type){
	                                                                                                                              
	CROM* value=new CROM(m_pTreeMapper,this, 0, "", "", CROM_t_Value, t_CROM_Xml);
	value->set_ValueType(schema_type->GetSchemaType().c_str());
	return value;
}

bool CROM::IsExpanded(){
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf() && child->m_Name=="xsi:nil")continue;
		return true;
	}
	return false;
}


void CROM::expandCROM(map<string,CROM*>&schemaTypes, map<string,CROM*>&elements){
	if(IsLeaf()||IsExpanded()!=0)return;
	if(IsXml()&&schemaTypes.size()>0){
		string complexTypeName = get_SchemaTypeExpandedName();
		CROM*complexType=get_ElementType(complexTypeName,schemaTypes);
		if(complexType!=0){
			vector<string> exploded;
			exploding(complexType, schemaTypes, exploded);
		} else {
			createValueCrom(this);
		}
		return;
	}
	else {
		expandRecursiveNode();
	}
}

CROM* CROM::insertLdapAttributeCmd(){
	CROM*prev=0;
	for(CROM*child=GetFirstChild();child;child=child->GetNextSibling()){
		prev=child;
	}
	CROM*attributeCmd=new CROM(m_pTreeMapper,this,prev,"Attribute command","Remove",
		CROM_t_Attribute|CROM_t_Optional, t_CROM_Ldap);
	return attributeCmd;
}


CROM*CROM::insertElement(CROM*to_be_copied, CROM*previous, int i_mapping){
	CROM*new_element=new CROM(this,previous,to_be_copied,i_mapping);
	if(to_be_copied->IsSubstitutionGroupHead()||
		to_be_copied->IsDerivedTypeHolder())return new_element;
	CROM*newChild=0;
	if(IsFlat()){
		for(CROM*oldChild=to_be_copied->GetFirstChild();oldChild!=0;oldChild=oldChild->GetNextSibling()){
			newChild=new CROM(new_element,newChild,oldChild,i_mapping);
		}
	}
	else {
		for(CROM*oldChild=to_be_copied->GetFirstChild();oldChild!=0;oldChild=oldChild->GetNextSibling()){
			if(oldChild->IsLeaf()){
				newChild=new CROM(new_element,newChild,oldChild,0);
			} else {
				if(oldChild->m_Name.Find('#')<0){
					newChild=new_element->insertElement(oldChild,newChild,0);
				}
			}
		}
	}
	return new_element;
}

int CROM::getMultipleMappingNumber(){
	const char* name=(const char*)m_Name;
	int k=konga::findk(name,'#');
	if(name[k]==0){
		return 0;
	} else {
		return atoi(&name[k+1]);
	}
}

CROM* CROM::findMultipleMapping(int i_mapping){
	const KString& displayName=GetDisplayName();
	for(CROM*child=m_Parent->GetFirstChild();child;child=child->GetNextSibling()){
		if((displayName[0]==0||child->GetDisplayName()==displayName)&&
			child->getMultipleMappingNumber()==i_mapping)return child;
	}
	return 0;
}

CROM* CROM::insertMultipleMapping(int i_mapping)
{
	CROM*node=findMultipleMapping(i_mapping);
	if(node!=0)return node;
	CROM*prev_sibling=findLastMultipleMapping(i_mapping);
	CROM* new_node=m_Parent->insertElement(this,prev_sibling,i_mapping);
	return new_node;
}

CROM*CROM::findLastMultipleMapping(int i_mapping){
	CROM*last_sibling=this;
	int last_i_mapping=getMultipleMappingNumber();
	if(last_i_mapping!=0){
		throw konga::InternalErrorException(string("CROM::findLastMultipleMapping(): initial last_i_mapping should be zero, path=")+(const char*)get_Path()); 
	}
	const char*display_name=GetDisplayName();
	for(CROM*child=m_Parent->GetFirstChild();child;child=child->GetNextSibling()){
		                                                                                                                                           
		if(display_name[0]==0||child->GetDisplayName()==display_name){
			int current_i_mapping=child->getMultipleMappingNumber();
			if(current_i_mapping<i_mapping&&current_i_mapping>last_i_mapping){
				last_i_mapping=current_i_mapping;
				last_sibling=child;
			}
		}
	}
	return last_sibling;
}

string CROM::getMultipleMappingName(int i_mapping){
	string name=get_Name();
	int k=(int)name.find('#');
	if(k<0)k=(int)name.length();
	if(i_mapping==0)return name.substr(0,k);
	else return name.substr(0,k)+"#"+konga::toString(i_mapping);
}

                                                            

CROM::CROM(CROM *parent, CROM *prevSibling, CROM*to_be_cloned, int i_mapping) :
	m_Type(to_be_cloned->m_Type),
	m_Type2(to_be_cloned->m_Type2),
	m_CROM_ext_type(to_be_cloned->m_CROM_ext_type),
	m_pTreeMapper(to_be_cloned->getTreeMapper()),
	m_Generator(0),
	m_deList(0),
	m_instanceNumber(0),
	m_Parent(parent), m_FirstChild(0), m_NextSibling(0), m_PrevSibling(prevSibling)
{
	m_display_name=to_be_cloned->GetDisplayName();
	set_Name(to_be_cloned->getMultipleMappingName(i_mapping).c_str());
	m_mapInfo.trans=0;

	if(m_PrevSibling){
		CROM*tmp=m_PrevSibling->m_NextSibling;
		m_PrevSibling->m_NextSibling=this;
		m_NextSibling=tmp;
		if(tmp)tmp->m_PrevSibling=this;
	} else if(m_Parent){
		m_NextSibling=m_Parent->m_FirstChild;
		m_Parent->m_FirstChild=this;
		if(m_NextSibling)m_NextSibling->m_PrevSibling=this;
	} else {
		m_NextSibling=0;
		m_PrevSibling=0;
	}


	if(IsLeaf()){
		m_ext.attr=new CROM_Attr(to_be_cloned->m_ext.attr, m_CROM_ext_type==t_CROM_Xml);
		setHasDefaultFlag();
	} else {
		switch(m_CROM_ext_type){
			case t_CROM_Unknown: m_ext.p=0; break;
			case t_CROM_Text: m_ext.text=new CROM_Text(to_be_cloned->m_ext.text); break;
			case t_CROM_DB: m_ext.db=new CROM_DB(to_be_cloned->m_ext.db); break;
			case t_CROM_Xml: m_ext.xml=new CROM_Xml(to_be_cloned->m_ext.xml); break;
		};
	}
}

void CROM::RemoveChild(CROM* child) {
	if (child->GetParent() != this) {
		throw konga::InternalErrorException("RemoveChild called from the wrong parent.");
	}
	CROM* prevSibling = child->GetPreviousSibling();
	CROM* nextSibling = child->GetNextSibling();
	if (prevSibling != 0) {
		prevSibling->SetNextSibling(nextSibling);
	}
	else {
		m_FirstChild = nextSibling;
	}
	if (nextSibling != 0) {
		nextSibling->SetPreviousSibling(prevSibling);
	}
	child->SetNextSibling(0);
	child->SetPreviousSibling(0);
	child->SetParent(0);
}

void CROM::SetNullFlag(bool b){
	if(b)m_Type |= CROM_t_Null;
	else m_Type &= ~CROM_t_Null;
}

void CROM::markMapFlag(){
	m_Type |= CROM_t_Mapped;
	for(CROM*parent=m_Parent; parent; parent=parent->m_Parent){
		if((parent->m_Type&CROM_t_Mapped)!=0)break;
		parent->m_Type |= CROM_t_Mapped;
	}
}

bool CROM::IsNillable(){
	if(IsLeaf()||!IsXml()){
		throw konga::InternalErrorException("CROM::IsNillable() may only be called from XML EEMENT nodes.");
	}
	return (m_Type&CROM_t_Null)&&(m_Type&CROM_t_Element);
}

bool CROM::CouldBeRoot(){
	return (m_Type&CROM_t_CouldBeRoot)==CROM_t_CouldBeRoot;
}

void CROM::SetNillable(bool b)
{
	if(IsLeaf()||!IsXml()){
		throw konga::InternalErrorException("CROM::IsNillable() may only be called from XML EEMENT nodes.");
	}
	if(b){
		m_Type |= CROM_t_Null;
	}
	else {
		m_Type &= ~CROM_t_Null;
	}
}

CROM*CROM::findFirstLoopNode(int&i0){
	CROM*node1=0;
	int i1=0,i2=0;
	for(CROM*child=GetFirstChild();child;child=child->GetNextSibling()){
		if(!child->IsLeaf()){
			if(child->isLoopNode()){
				i0=i0+1;
				return child;
			} else {
				i2=i0;
				CROM*node2=child->findFirstLoopNode(i2);
				if(node2==0)continue;
				if(node1){
					if(i2<i1){
						i1=i2;
						node1=node2;
					}
				}
				else {
					node1=node2;
					i1=i2;
				}
			}
		}
	}
	return node1;
}

bool CROM::foundSegmentIdentifier(const string&id){
	if(m_CROM_ext_type == t_CROM_DB){
		return id==(const char*)GetName();
	}
	else if(m_CROM_ext_type==t_CROM_Ldap){
		return id==(const char*)GetName();
	}
	else {
		if((m_Type&CROM_t_Root)!=0)return false;
		CROM* child=GetFirstChild();
		if(child->IsPrecondition()){
			child=child->GetNextSibling();
		}
		if(!child->IsIdentifier()){
			throw konga::CrException(string("Failed to find segment identifier. segment_name='")+
			(const char*)GetDisplayName()+"'. Current identifier is '" + id + "'");
		}
		return id==(const char*)child->GetFormat();
	}
}

bool CROM::hasChoiceChildren(){
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf() || child->IsElement())continue;
		if(child->IsOneOfNode())return true;
		if(child->hasChoiceChildren())return true;
	}
	return false;
}

void CROM::SetAllGroup(bool bIsAllGroup) {
	if (bIsAllGroup) {
		m_Type |= CROM_t_AllGroup;
	}
	else {
		m_Type &= ~CROM_t_AllGroup;
	}
}

bool CROM::IsAllNSQualified(){
	if(!IsLeaf() && !IsNamespaceQualified()){
		return false;
	}
	for(CROM*child=GetFirstChild();child;child=child->GetNextSibling()){
		if(!child->IsAllNSQualified())return false;
	}
	return true;
}

bool CROM::hasMapped(){
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf()){
			const char* sourceExpr=child->getSourceExpr();
			if(sourceExpr!=0 && sourceExpr[0]!=0)return true;
		}
		else {
			if(child->hasMapped())return true;
		}
	}
	return false;
}

void CROM::setupPatternMatchingFlag()
{
	if(IsElement())setPatternMatchingFlag(hasChoiceChildren());
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf())continue;
		child->setupPatternMatchingFlag();
	}
}

void CROM::setPatternMatchingFlag(bool b){
	if(b)m_Type2|=CROM_t_PatternMatching_2;
	else m_Type2&=(~CROM_t_PatternMatching_2);
}

void CROM::checkForChildrenWithSameName()
{
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf())continue;
		KString name=child->GetName();
		bool hasChildWithSameName=false;
		for(CROM*sibling=child->GetNextSibling();sibling;sibling=sibling->GetNextSibling()){
			if(name==sibling->GetName()){
				hasChildWithSameName=true;
				break;
			}
		}
		int i=0;
		for(CROM*sibling=child;sibling;sibling=sibling->GetNextSibling()){
			if(sibling->m_Name==name){
				i++;
				sibling->m_Name+="@"+konga::toString(i);
			}
		}
		child->checkForChildrenWithSameName();
	}
}

void CROM::setBoolTypeFormat(const char*format){
	if(IsValueLeaf()){
		if(strnicmp(GetValueType(),"bool",4)==0)SetFormat(format);
	}
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		child->setBoolTypeFormat(format);
	}
}
bool CROM::IsKindOfString(){
	if((m_Type2&CROM_t_KindOfStringDefined)==0){
		const konga::DataElement::Type deType = get_DataElementType();
		switch(deType)
		{
			                                            
			case konga::DataElement::t_int:
			case konga::DataElement::t_long:
			case konga::DataElement::t_float:
			case konga::DataElement::t_double:
			case konga::DataElement::t_bool:
				m_Type2|=(CROM_t_KindOfStringDefined);
				break;

			                                                          
			default:
				m_Type2|=(CROM_t_KindOfString|CROM_t_KindOfStringDefined);
				break;
		}
	}
	return (m_Type2&CROM_t_KindOfString)!=0;
}

                                                                                                                                                  
string CROM::toString(CROM_ext_type crom_type){
	switch(crom_type){
		case t_CROM_Text: return "Text";
		case t_CROM_DB: return "DB";
		case t_CROM_Ldap: return "Ldap";
		case t_CROM_Xml: return "Xml";
	}
	return "Unknown";
}


bool CROM::GetComplexTextDelimiterQualifiers(unsigned char &cDelimiter,  unsigned char &cStrQual, unsigned char &cNumQual, int &iIdColNum, int &iStartPos, int &iLen, bool &bIsDatabase)
{
	CROM* pObj = this;
	if ( pObj != 0 )
	{
		while(pObj->m_Parent)pObj=pObj->m_Parent;
		CROM* pElemPtr = pObj->FindFirstObject(COMPLEXT_TEXT_ROOT, CROM_t_Element);
		if (0 != pElemPtr)
		{
			cDelimiter = pElemPtr->get_delimiter();
			cStrQual = pElemPtr->get_strQual();
			cNumQual = pElemPtr->get_numQual();
			iStartPos = pElemPtr->get_fieldPos();
			iLen = pElemPtr->get_fieldLength();

			                                                                                               
			                                                                                                  
			CROM* pSegPtr = pElemPtr->GetFirstChild();
			if (0 != pSegPtr)
			{
				CROM* pSegCols = pSegPtr->GetFirstChild();
				while (pSegCols != 0)
				{
					if (strcmp(pSegCols->GetValueType(),"Identifier")==0 && pSegCols->IsAttribute())
						break;
					iIdColNum++;
					pSegCols = pSegCols->GetNextSibling();
				}
			}
			bIsDatabase=false;
			return true;
		}
		else {                                         
			pElemPtr=pObj->GetFirstChild();
			if(pElemPtr->GetName().IsEmpty()){                          
				cDelimiter = pElemPtr->get_delimiter();
				cStrQual = pElemPtr->get_strQual();
				cNumQual = pElemPtr->get_numQual();
				iStartPos = pElemPtr->get_fieldPos();
				iLen = pElemPtr->get_fieldLength();
				bIsDatabase=false;
				return false;
			}
			else {                                
				cDelimiter=DB_DELIMITER_CHAR;
				cStrQual='"';
				cNumQual=0;
				iIdColNum=-1;
				iLen=0;
				iStartPos=0;
				bIsDatabase=true;
				return false;
			}
		}
	}
	return false;
}

const char* CROM::GetForeignKeyValue()
{
	if(!IsForeignKey() || !m_Parent)return 0;
	CROM* pParent = m_Parent->m_Parent;
	if(!pParent)return 0;
	CROM* pTemp = pParent->FindChildLeaf(get_LinkToPrimaryKey());
	if(pTemp==0){
		throw konga::CrException(string("CROM::GetForeignKeyValue() failed, FK")+(const char*)m_Parent->get_sqlTableName()+"."+(const char*)GetName()+", PK="+
			(const char*)pParent->get_sqlTableName()+"."+(const char*)get_LinkToPrimaryKey());
	}
	if(!pTemp->IsNull())
		return pTemp->GetValue();
	else
		return pParent->GetForeignKeyValue();
}

bool CROM::ParentIsLoopNode()
{
	CROM*parent=this;
	while(parent){
		if(parent->m_Type&CROM_t_Collection){                                             
			return true;
		}
		parent=parent->GetParent();
	}
	return false;
}

bool CROM::IsIdentifier() const {
	if(m_CROM_ext_type!=t_CROM_Text)return false;
	if(!IsLeaf())return false;
	return m_ext.attr->get_ValueType()=="Identifier";
}

bool CROM::IsSourceUpdateColumn() const {
	if(isDB() && IsLeaf())return (m_Type&CROM_t_SourceUpdateColumn)!=0;
	return false;
}

void CROM::SetComplexTypeDefinition(CROM* pDef)
{
	m_ext.xml->m_pComplexTypeDefinition = pDef;
}

const char* CROM::get_Value() const {return IsLeaf()?(const char*)m_ext.attr->m_Value:"";}

const char* CROM::get_DefaultValue() const {return IsLeaf()?(const char*)m_ext.attr->m_DefaultValue:"";}

const char* CROM::get_Format() const {return IsLeaf()?(const char*)m_ext.attr->m_Format:"";}

const char* CROM::get_Domain() const {return IsLeaf()?(const char*)m_ext.attr->m_Domain:"";}

CROM* CROM::GetComplexTypeDefinition() const {
	if(IsLeaf()||m_CROM_ext_type!=t_CROM_Xml)return 0;
	return m_ext.xml->m_pComplexTypeDefinition;
}

konga::DataElement::Type CROM::get_DataElementType(){
	int itype=(m_Type2& 0XF);
	if(itype)return (konga::DataElement::Type)itype;
	if(IsLeaf()){
		KString valueType(GetValueType());
		int k=valueType.Find('[');
		if(k>0){
			valueType.Left(k);
		}
		konga::DataElement::Type type=(konga::DataElement::Type)konga::DataElement::get_type(valueType, get_SqlDataType());
		m_Type2 |= (int)type;
		return type;
	}
	else {
		throw konga::InternalErrorException("CROM::get_DataElementType() may only be called from leaf nodes.");
	}
}

namespace {
	string getSegmentInWrongPositionErrorMessage(CROM* node, const string& id) {
		return string("A record was encountered in an unexpected position.")+
			" Please make sure the format of the source text file is correct, in particular that that all records appear under the correct parent." +
			" Details: undefined record identifier=\""+id+
			"\", appeared under record \""+ (const char*)node->GetName()+ "\"";
	}

	CROM* findSegmentImpl(CROM* node, const string& id) {
		for(CROM*pChild=node->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
			if(pChild->IsElement()){
				if(pChild->foundSegmentIdentifier(id)){
					return pChild;
				}
			}
		}
		return 0;
	}
}

CROM* CROM::findSegment(const string& id)
{
	if(foundSegmentIdentifier(id)){
		return this;
	}
	CROM* pTableNode=this;
	while(pTableNode){
		CROM* segment=findSegmentImpl(pTableNode, id);
		if(segment){
			return segment;
		}
		pTableNode=pTableNode->GetParent();
	}
	throw konga::CrException(getSegmentInWrongPositionErrorMessage(this, id));
}

   
                                            
                                                           
                                          
                                                                      
   
CROM* CROM::findSegment(const string& id, CROMData*&pDataNode)
{
	if(foundSegmentIdentifier(id)){
		if(pDataNode)pDataNode=pDataNode->GetParent();
		return this;
	}
	CROM* segment=findSegmentImpl(this, id);
	if(segment){
		return segment;
	}

	CROM* pTableNode=GetParent();
	if(pDataNode){
		pDataNode=pDataNode->GetParent();
		if(pDataNode && pDataNode->GetDTDNode()!=pTableNode){
			throw konga::InternalErrorException("CROM::findSegment() was called with inconsistent data and structure node.");
		}
	}
	while(pTableNode){
		segment=findSegmentImpl(pTableNode, id);
		if(segment) {
			return segment;
		}
		pTableNode=pTableNode->GetParent();
		if(pDataNode)pDataNode = pDataNode->GetParent();
		if(pTableNode==0){
			throw konga::CrException(getSegmentInWrongPositionErrorMessage(this, id));
		}
		else if(pDataNode && pDataNode->GetDTDNode() != pTableNode){
			throw konga::CrException("Error in calling CROM::findSegment(), the data and the structure node are inconsistent.");
		}
	}
	throw konga::CrException(getSegmentInWrongPositionErrorMessage(this, id));
}

void CROM::addUpdateKeyIfNoneDefined(){
	bool hasPK=false;
	bool hasUpdateKey=false;
	for(CROM*child=GetFirstChild(); child && !child->IsElement(); child=child->GetNextSibling()){
		if(child->IsPrimaryKey())hasPK=true;
		if(child->IsUpdateKey())hasUpdateKey=true;
	}
	if(!hasUpdateKey && hasPK){
		for(CROM*child=GetFirstChild(); child && !child->IsElement(); child=child->GetNextSibling()){
			if(child->IsPrimaryKey())child->AddType(CROM_t_UpdateKey);
		}
	}
	for(CROM*child=GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsElement()){
			child->addUpdateKeyIfNoneDefined();
		}
	}
}

void CROM::setHasDefaultFlag(){
	if(IsLeaf() && m_ext.attr->get_DefaultValue()!="")m_Type|=CROM_t_HasDefault;
}

bool CROM::isCsv() const {
	return ( get_delimiter() == ',' && get_strQual() == '"' );
}


