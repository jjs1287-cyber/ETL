/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef ActivityInfoProviderImpl_UTILS_H
#define ActivityInfoProviderImpl_UTILS_H

#include <vector>
#include <string>
#include <map>

#include <boost/noncopyable.hpp>

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Db/TranDb/FolderItemTab.h>
#include <konga/Db/DbUtils/DbUtils.h>

namespace konga
{
	class Id_Container : boost::noncopyable
	{
	public:
		Id_Container(){}
		bool add(int id){
			if(m_map.find(id)==m_map.end()){
				m_map[id]=true;
				return true;
			} else {
				return false;
			}
		}
		std::string getFilter(const std::string& sID);

	private:
		std::map<int,bool> m_map;
	};

	class CollectOpids : boost::noncopyable
	{
	public:
		CollectOpids(DbConnection*pTranDbConn, int id, int entity_type_id);
		~CollectOpids();

		bool addOp(int id){return m_opContainer.add(id);}
		bool addFolder(int id){return m_folderContainer.add(id);}
		void processInterchangeProject(int project_id);
		void processFolderItems(int folder_id);
		std::string getOpFilter(){return m_opContainer.getFilter("Operation_ID");}

	private:
		konga::DbConnection* m_pTranDbConn;
		TranDb::FolderItemTab* m_pFolderItemTab;
		Id_Container m_opContainer, m_folderContainer;
	};
}

#endif                                    

