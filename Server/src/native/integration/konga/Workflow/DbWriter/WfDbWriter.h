/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef WF_DB_WRITER_H
#define WF_DB_WRITER_H

#include <string>

namespace konga {
	class Process;
	class Task;
	class DbConnection;

	class WfDbWriter
	{
	public:
		enum WorkflowDbDeployType {
			dtNEW_VERSION = 0,
			dtREPLACE_LAST_VERSION = 1,
			dtDELETE_PREVIOUS = 2
		};
	public:
		WfDbWriter(void);
		virtual ~WfDbWriter(void);

		                                                                                 
		                                           
		void WriteProcess(Process& process, const std::string& stConnectionString, WorkflowDbDeployType dt);

	private:
		void WriteProcess(Process& process, WorkflowDbDeployType dt);
		void WriteProcessDataElements(const Process& process);
		void WriteTasks(const Process& process);
		void WriteTaskDataElements(const Task& task);
		void WriteRoutes(const Process& process);
		void WriteRoutingRules(const Process& process);
		void WriteActivities(const Process& process);
		void WriteActivityRules(const Process& process);
		void WriteDocuments(const Process& process);
		void DeleteFromTable(const std::string& stTableName) const;

	private:
		DbConnection* m_pDbConnection;
		int m_iProcessId, m_iProcessVersion;
		bool m_bReplace, m_bDelete;
	};
}

#endif                  
