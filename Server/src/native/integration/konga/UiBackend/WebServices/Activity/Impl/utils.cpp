/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <konga/Utils/StringUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/Db/TranDb/interchangeprojectoperationstab.h>
#include <konga/Db/TranDb/interchangeprojectfolderstab.h>

#include "utils.h"

using namespace std;
using namespace konga;
using namespace TranDb;

std::string konga::Id_Container::getFilter(const std::string& sID)
{
	int size=m_map.size();
	if ( size == 0 )
		return "(" + sID + " = 0)";

	std::map<int,bool>::iterator it=m_map.begin();
	if(size==1)
		return "("+sID+" = "+konga::toString(it->first)+")";

	std::string s="("+sID+" IN (";
	for(int i=0;i<size;i++,it++){
		if(i>0)s+=",";
		s+=konga::toString(it->first);
	}
	s+="))";
	return s;
}


konga::CollectOpids::CollectOpids(DbConnection*pTranDbConn, int id, int entity_type_id) :
	m_pTranDbConn(pTranDbConn), m_pFolderItemTab(0)
{
	switch(entity_type_id){
		case OperationEntityId:
			addOp(id); break;
		case FolderEntityId:
			processFolderItems(id); break;
		case InterchangeProjectEntityId:
			processInterchangeProject(id);
	}
}

void konga::CollectOpids::processFolderItems(int folder_id)
{
	if(m_pFolderItemTab==0){
		m_pFolderItemTab=FolderItemTab::MakeInstance(m_pTranDbConn);
	}
	std::vector<int> folder_id_list;                                       
	const std::string sFilter = "WHERE (ParentFolder_ID = "+konga::toString(folder_id)+")";
	m_pFolderItemTab->Select(sFilter);
	while(m_pFolderItemTab->Next()){
		int EntityType_ID=m_pFolderItemTab->GetEntityType_ID();
		int Item_ID=m_pFolderItemTab->GetItem_ID();
		if(EntityType_ID==OperationEntityId)addOp(Item_ID);
		if(EntityType_ID==FolderEntityId)folder_id_list.push_back(Item_ID);
	}
	for(std::vector<int>::iterator it=folder_id_list.begin();
		it!=folder_id_list.end();it++){
			const int child_folder_id=*it;
			if(addFolder(child_folder_id))
				processFolderItems(child_folder_id);
	}
}

void konga::CollectOpids::processInterchangeProject(int project_id)
{
	{
		SmartDbPtr<interchangeprojectoperationstab> tab( interchangeprojectoperationstab::MakeInstance(m_pTranDbConn) );
		std::string sFilter="WHERE (interchangeproject_id = "+konga::toString(project_id)+")";
		tab->Select(sFilter);
		while(tab->Next()) {
			addOp(tab->Getitem_id());
		}
	}

	SmartDbPtr<interchangeprojectfolderstab> tab( interchangeprojectfolderstab::MakeInstance(m_pTranDbConn) );
	std::string sFilter="WHERE (interchangeproject_id = "+konga::toString(project_id)+")";
	tab->Select(sFilter);
	while(tab->Next()){
		processFolderItems(tab->Getitem_id());
	}

}

konga::CollectOpids::~CollectOpids()
{
	if(m_pFolderItemTab){
		m_pFolderItemTab->Close();
		delete m_pFolderItemTab;
		m_pFolderItemTab = 0;
	}
}

