/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                                            

#include <string>
#include <memory>	                

#include <konga/Utils/CrException.h>
#include <konga/Utils/Timing.h>

#include "crom.h"
#include "TreeMapper.h"
#include "NodePatternMatch.h"

using std::string;

NodePatternMatch::NodePatternMatch(std::map<std::string,CROM*>&elementTypes0, std::map<std::string,CROM*>&elements0, FILE*LOG_)
	: g__err(false), g__mark(false), g__retrack(false), g__bIsGroup(false), g__i(0), nmax(0),
	dtd_list(0), dtd0_list(0), dtd_saved(0), dtd0(0), retrack_saved(0), mark_list(0), dtd_entries(0), LOG(LOG_),
	elementTypes(elementTypes0), elements(elements0)
{
}

NodePatternMatch::~NodePatternMatch() {
	reinitialize(0);
}

void NodePatternMatch::reinitialize(int n) {
	if(dtd_list)delete[] dtd_list;
	if(dtd0_list)delete[] dtd0_list;
	if(dtd_saved)delete[] dtd_saved;
	if(retrack_saved)delete[] retrack_saved;
	if(mark_list)delete[] mark_list;
	if(dtd_entries)delete[] dtd_entries;

	if ( n > 0 ) {
		dtd_list=new CROM*[n];
		dtd0_list=new CROM*[n];
		dtd_saved=new CROM*[n];

		retrack_saved=new bool[n];
		mark_list=new bool[n];

		dtd_entries=new std::vector<CROM*>[n];

		for(int i=0;i<n;i++) {
			retrack_saved[i]=false;
			mark_list[i]=false;
			dtd_list[i]=0;
			dtd0_list[i]=0;
			dtd_saved[i]=0;
		}

	}
}

void NodePatternMatch::initArrays(int n){
	if(n > nmax){
		reinitialize(n);
		nmax = n;
	}
}

namespace {
	std::string convertToReadable(const char* path){
		std::string str;
		konga::StringTokenizer tk(path,"$.");
		while(tk.NextToken()){
			const char* name=tk.token();
			char c=name[0];
			if(c=='$'||c=='.')continue;
			if(CROM::isNum(name)==false){
				str+="/";
				str+=name;
			}
		}
		return str;
	}
}

void NodePatternMatch::findMatchingCrom(const std::vector<std::string>& name_list, CROM*groupDtd0)
{
	const int n=name_list.size();
	dtd0=0;
	initArrays(n);

	std::string header1="";
	CROM*dtd=0;

	if(groupDtd0->IsArray()){
		CROM*item_dtd=groupDtd0->GetFirstChild();
		for(int i=0;i<n;i++){
			dtd_list[i]=item_dtd;
			dtd0_list[i]=item_dtd;
		}
		return;
	}

	if(groupDtd0->IsComplexPatternMatching()){
		dtd0=groupDtd0;
		for(int i=0;i<n;i++){
			g__i=i;
			g__retrack=false;
	redo:	
			name=name_list.at(i);
			g__err=false;
			g__mark=false;
			dtd_saved[i]=dtd;
			retrack_saved[i]=g__retrack;
			if( (dtd=find_next_dtd(groupDtd0,dtd,header1)) !=0)
			{
				g__retrack=false;
				if(LOG){
					fprintf(LOG,"%d\t%s\t%s\t%s\n",i,g__mark?"*":" ",name.c_str(),(const char*)dtd->get_Path());
					fflush(LOG);
				}
				dtd_list[i]=dtd;
				dtd0_list[i]=dtd0;
				mark_list[i]=g__mark;

				CROM*parent=dtd->GetParent();
				if(!dtd->isLoopNode()){
					while((dtd->GetNextSibling()==0&&!dtd->isLoopNode())||(parent&&parent->IsOneOfNode())){
						dtd=parent;
						if(dtd->isLoopNode()||dtd==groupDtd0)break;
						parent=dtd->GetParent();
						if(parent==groupDtd0)break;
					}
				}
				dtd0=parent->IsOneOfNode()? parent->GetParent(): parent;
				              
			} else {
				for(;i>=0;i--){
					std::vector<CROM*>& dtd_entry=dtd_entries[i];
					if(mark_list[i]){
						dtd=dtd_list[i];
						dtd_entry.push_back(dtd_saved[i]);
						if(dtd->isLoopNode()==false){
							while(dtd&&(dtd->m_Type&CROM_t_Optional)==0){
								CROM*parent=dtd->GetParent();
								if(parent&&parent->IsOneOfNode())break;
								dtd=parent;
							}
						}
						g__retrack=true;
						if(LOG){
							fprintf(LOG,"retrack to i=%d,dtd=%s\n",i,(dtd==0?"null":(const char*)dtd->get_Path()));
							fflush(LOG);
						}
						int j,m=(int)dtd_entry.size();
						for(j=0;j<m;j++){
							CROM*tmp=dtd_entry.at(j);
							if(tmp==dtd&&retrack_saved[i])break;
						}
						if(j==m){
							g__i=i;
							goto redo;
						}
					}
					dtd_entry.clear();
				}                                     
				string sNodeNames;
				for(unsigned int iNode=0, nNode=name_list.size(); iNode < nNode;) {
					sNodeNames += name_list[iNode];
					if( ++iNode < nNode )
						sNodeNames += ", ";
					if( iNode == 20 && nNode != 21 ) {
						sNodeNames += "...";
						break;
					}
				}
				std::string path=convertToReadable((const char*)groupDtd0->get_Path());
				throw konga::CrException("XML data does not match schema/dtd. "
					"The sequence \"" + sNodeNames + "\" is not allowed under the node " + 
					path + ".");
			}                                  
		}                        
		                                        
	}
	else {                                                                  
		g__retrack=false;
		g__err=false;
		g__mark=false;
		                 
		CROM*nonValueDtd_saved=0, *nonValueDtd0_saved=groupDtd0;
		for(int i=0, n=(unsigned int)name_list.size();i<n;i++){
			g__i=i;
			name=name_list.at(i);
			CROM*dtd_save=dtd;
			dtd=nonValueDtd_saved;
			dtd0=nonValueDtd0_saved;
			if(groupDtd0->IsAllGroup())dtd=0;
			if( (dtd=find_next_dtd(groupDtd0, dtd, header1)) !=0)
			{
				dtd_list[i]=dtd;
				dtd0_list[i]=dtd0;
				if(name.empty()==false){
					nonValueDtd_saved=dtd;
				}

				CROM*parent=dtd->GetParent();
				if(!dtd->isLoopNode()){
					while((dtd->GetNextSibling()==0&&!dtd->isLoopNode())){
						dtd=parent;
						if(dtd->isLoopNode()||dtd==groupDtd0)break;
						parent=dtd->GetParent();
						if(parent==groupDtd0)break;
					}
				}
				dtd0=parent;
				if(name.empty()==false){
					nonValueDtd0_saved=dtd0;
				}
			} else {
				TreeMapper*pTreeMapper=groupDtd0->getTreeMapper();
				if (!name.empty ())
				{
					std::string warning="Warning: extra subelement \""+name+"\" under the element \""+(const char*)groupDtd0->GetDisplayName()+"\"";
					pTreeMapper->addWarning(warning);
				}
				dtd_list[i]=0;
				dtd=dtd_save;
			}
		}
	}
	                                                                                
	if(n>1){
		CROM*dtd_list_last=dtd_list[n-1];
		if(dtd_list_last!=0){
			CROM*dtd_next=dtd_list_last->GetNextSibling();
			if(dtd_list_last->isLoopNode() && dtd_next!=0 && !dtd->IsOptional()){
				if(dtd_list_last->GetName()==dtd_next->GetName()){
					if(dtd_list_last->IsCollection()|| dtd_list[n-2]==dtd_list_last){
						dtd_list[n-1]=dtd_next;
					}
				}
			}
		}
	}
}

CROM* NodePatternMatch::find_next_dtd(CROM*parent,CROM*current,const std::string&header)
{
	CROM*p=0,*p1;
	if(name.empty()){
		p=parent->FindChildLeaf(name.c_str());
		if(p!=0)return p;
	}
	if(current==0){                    
		if(parent==0){
			return 0;
		}
		p=parent->GetFirstChild();
		if(p&&loopThruChild(p, header)){
			return p;
		}
	} else {
		if(current->isLoopNode()&&g__retrack==false){
			p=current;
		}
		else {
			p=current->GetNextSibling();
		}
		if(p&&loopThruChild(p, header))return p;
		p=current->GetParent();
		if(p!=0)dtd0=p->GetParent();
		if(p==parent||p==0){
			if(parent&&parent->isLoopNode()){
				p=parent->GetFirstChild();
				if(p&&loopThruChild(p,header)){
					return p;
				}
			}
			return 0;
		}
		if(p->isLoopNode()&&g__retrack==false){
			if((p1=p->GetFirstChild())!=0)loopThruChild(p1, header);
			if(p1){
				return p1;
			}
			g__err=false;
		}
		if((p1=p->GetNextSibling())==0){
			p1=p->GetParent();
			if(p1==parent){
				if(parent&&parent->isLoopNode()){
					p=parent->GetFirstChild();
					if(p&&loopThruChild(p,header)){
						return p;
					}
				}
				g__err=true;
				return 0;
			}
		}
		p=find_next_dtd(parent,p, header+"  ");
		if(p){
			return p;
		}
		if(p1==0){
			g__err=true;
			return 0;
		}
		if((p1->m_Type&CROM_t_Optional)||p1->IsCollection()||(p1->IsAtLeastOne()&&current==p1))g__err=false;   
	}
	return 0;
}

                                         
bool NodePatternMatch::loopThruChild(CROM*&p,const std::string&header)
{
	std::string header1=header+"  ", header2=header+"    ";
	CROM*parent=p->GetParent(),*p1,*p0=(g__i!=0)?(dtd_list[g__i-1]):0;
	bool isOneOf=false;
	if(parent){
		isOneOf=parent->IsOneOfNode()||parent->IsSubstitutionGroupHead()||parent->IsAllGroup();
		if(parent->IsAllGroup()){
			p=parent->GetFirstChild();
		}
	}

	while(p){
		if(p->IsAnyElement()){
			return true;
		}
		if(p->m_Type&CROM_t_SubstitutionGroupHead){
			CROM*substitution;
			for(substitution=p->GetFirstChild();substitution;substitution=substitution->GetNextSibling()){
				if(substitution->IsElement()&&name==(const char*)substitution->GetDisplayName()){
					g__mark=true;
					p=substitution;
					return true;
				}
			}
			                                                                
			substitution=p->expandHoldersTypeNode(name.c_str(),elementTypes, elements);
			if(substitution!=0){
				g__mark=true;
				p=substitution;
				return true;
			}
		}
		if(p->IsValueLeaf()&&name.empty()){
			if(isOneOf)g__mark=true;
			return true;
		}
		if(p->IsElement()&&name==(const char*)p->GetDisplayName()){
			if(isOneOf)g__mark=true;
			if(p->m_Type&CROM_t_Optional)g__mark=true;
			if(p->isLoopNode())g__mark=true;
			return true;
		}
		if(p->IsGroupNode()){
			if((p1=p->GetFirstChild())!=0)loopThruChild(p1, header2);
			if(p1){
				p=p1;
				if(isOneOf)g__mark=true;
				if(p->m_Type&CROM_t_Optional)g__mark=true;
				return true;
			}
			if((p->m_Type&CROM_t_Optional)||p->IsCollection()||p->ChildrenCanBeSkipped()){
				g__err=false;
				p=p->GetNextSibling();;
				continue;
			}
			if(p->IsAtLeastOne()&&p==p0)g__err=false;
			if(g__err){
				if(p->GetParent()->IsOneOfNode()){
					if((p1=p->GetNextSibling())!=0)loopThruChild(p1, header2);
					if(p1){
						p=p1;
						g__mark=true;
						if(p->m_Type&CROM_t_Optional)g__mark=true;
						return true;
					}
				}
				if(g__i==0||!isOneOf){
					p=0;
					return true;
				}
				if(p0->get_Path().Find((const char*)parent->get_Path())==0){
					g__err=false;
				} else {
					p=0;
					return true;
				}
			}			
		}
		if(p->isLoopNode()||p->IsPrecondition()||p->IsAttribute()||
			(p->m_Type&CROM_t_Optional)||isOneOf){
			p=p->GetNextSibling();
		} else {
			g__err=true;
			p=0;
			return true;
		}
	}
	if(p==0&&parent&&parent->IsSubstitutionGroupHead()||parent->IsDerivedTypeHolder()){
		if(parent->FindChildFolder(name.c_str())==0)
			parent->expandHoldersTypeNode(name.c_str(),elementTypes,elements);
		for(CROM*substitution=parent->GetFirstChild();substitution;substitution=substitution->GetNextSibling()){
			if(substitution->IsElement()&&name==(const char*)substitution->GetDisplayName()){
				if(isOneOf)g__mark=true;
				p=substitution;
				return true;
			}
		}
	}
	return false;
}
