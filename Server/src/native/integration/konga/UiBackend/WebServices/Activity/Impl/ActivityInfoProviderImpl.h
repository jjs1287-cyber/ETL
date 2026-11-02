/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ws_ACTIVITYINFOPROVIDER_H
#define KONGA_ws_ACTIVITYINFOPROVIDER_H

#include <string>
#include <vector>
#include <map>

#include <axis/AxisUserAPI.hpp>
#include <axis/AxisUserAPIArrays.hpp>

AXIS_CPP_NAMESPACE_USE 

#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidMappings.h>
#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include "../WsOperationInstanceFilter.hpp"
#include "../WsOperationLogInstance_Array.hpp"
#include "../WsOperationProgress_Array.hpp"

namespace konga {

	class DbConnection;

	struct OpLog {
		OpLog(int Log_ID,const std::string& line)
			: m_Log_ID(Log_ID), m_line(strsave(line.c_str()))
		{}
		~OpLog(){delete[]m_line;}
		int m_Log_ID;
		char*m_line;
	};

	class VOpLog : public std::vector<OpLog*> {
	public:
		~VOpLog(){
			for(iterator it=begin();it!=end();it++){
				delete *it;
			}
		}
	};

	class DbConnection;

	class ProjectOperation {
	public:
		ProjectOperation(int Project_IDin,int Operation_IDin, const std::string& Operation_Namein)
			: Project_ID(Project_IDin), Operation_ID(Operation_IDin), Operation_Name(Operation_Namein)
		{}
		int Project_ID, Operation_ID;
		std::string Operation_Name;
	};

	class OperationMap
	{
	public:
		OperationMap(DbConnection*pTranDbConn,int id,int idType, const string& stOperationName);
		~OperationMap();

		const ProjectOperation* findProjectOperation(int Operation_ID) const
		{
			std::map<int, ProjectOperation*>::const_iterator cit=op_map.find(Operation_ID);
			if(cit==op_map.end())return 0;
			return (cit->second);
		}

		bool empty() const {
			return op_map.empty();
		}

		std::string getOpId(int Operation_ID) const {
			std::map<int,std::string>::const_iterator cit=opId_map.find(Operation_ID);
			std::string opId;
			if(cit==opId_map.end()){
				opId=GetGuidForId(Operation_ID, OperationEntityId);
				opId_map[Operation_ID]=opId;
			} else {
				opId=cit->second;
			}
			return opId;
		}

		std::string getSqlStringForOperationIds() const {
			std::map<int, ProjectOperation*>::const_iterator cit = op_map.begin();
			string stRetVal;
			for( ; cit != op_map.end(); ++cit ) {
				if ( cit == op_map.begin() )
					stRetVal += "(";
				else
					stRetVal += ",";
				stRetVal += toString(cit->first);
			}
			if ( !stRetVal.empty() )
				stRetVal += ")";
			return stRetVal;
		}
	private:
		std::map<int, ProjectOperation*> op_map;

		                                                 
		mutable std::map<int,std::string> opId_map;
	};

	class ActivityInfoProviderImpl : public WsImplBaseAuthenticator
	{
	public:
		ActivityInfoProviderImpl(const char* user, const char* password);
		~ActivityInfoProviderImpl();

		WsOperationLogInstance_Array* getOperationLog(WsOperationInstanceFilter& filter);

		WsOperationProgress_Array* getOperationProgress(WsOperationInstanceFilter& filter);

		xsd__long_Array* deleteOperationLogEntry(xsd__long_Array id);

		xsd__string_Array* getSourceFileHistoryLog(WsOperationInstanceFilter& filter);

		xsd__long_Array* deleteSourceFileHistory(xsd__long_Array id);

                                                                                                                                               
		xsd__long_Array* cancelOperationQueue(xsd__long_Array logid);

	private:
		void CancelOperationQueueImpl(const std::vector<long long>& vArgs, DbConnection& tranDbConn, DbConnection& msgDbConn);

	private:
		const std::string m_sPassword;
	};
}
#endif                                   


