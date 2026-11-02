/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef ACTIVITY_H
#define ACTIVITY_H

#include <string>

#include "ActivityItem.h"
#include "WorkflowIdTypes.h"

namespace konga {

	class Process;

	   
                                               
                                                                                  
    
	class Activity : public ActivityItem {
	public:
		Activity(Process& process, activity_id_t id = -1) : m_process(process), m_bHasInstance(false), m_id(id) {}
		virtual ~Activity() {}

		bool HasInstance() const { return m_bHasInstance; }
		void SetHasInstance(bool b) { m_bHasInstance = b; }

		activity_id_t GetId() const { return m_id; }
		void SetId(activity_id_t id) { m_id = id; }

		virtual void SetName(const std::string& stName) { m_stName = stName; }
		virtual std::string GetName() const { return m_stName; }

		virtual std::string GetMethod() const = 0;

		void SetLocation(const std::string& stLocation) { m_stLocation = stLocation; }
		std::string GetLocation() const { return m_stLocation; }

		std::string GetActivityListItemAsString() const;

	protected:
		Process& m_process;

	private:
		bool m_bHasInstance;	                                                                              
		activity_id_t m_id;

		std::string m_stName;
		std::string m_stType;
		std::string m_stLocation;
	};

	class SimpleActionActivity : public Activity {
	public:
		SimpleActionActivity(Process& process, activity_id_t id = -1) : Activity(process, id) {}

		std::string GetMethod() const { return "Script"; }

	private:
	};

	class IntegrationActivity : public Activity {
	public:
		IntegrationActivity(Process& process, activity_id_t id = -1) : Activity(process, id) {}

		std::string GetMethod() const { return "Integration"; }

	private:
	};

	class TrueActionActivity : public Activity {
	public:
		TrueActionActivity(Process& process, activity_id_t id = -1) : Activity(process, id) {}

		std::string GetMethod() const { return "Boolean Script"; }

	private:
	};

	struct DBAttribute {
		DBAttribute(void) : m_bIsConstantValue(false), m_bIsConstantFilter(false) {}
		~DBAttribute(void) {}

		std::string m_stName;
		std::string m_stDataType;
		std::string m_stIO;
		std::string m_stValue;
		bool m_bIsConstantValue;
		std::string m_stFilter;
		bool m_bIsConstantFilter;
	};

	class OdbcActivity : public SimpleActionActivity {
		enum StatementType {
			stNONE = 0,
			stSELECT = 1,
			stINSERT = 2,
			stUPDATE = 3,
			stDELETE = 4
		};
	public:
		OdbcActivity(Process& process, activity_id_t id) :
		  SimpleActionActivity(process, id), m_bIsDsn(false), m_bIsAdvancedWhere(false), m_lTimeOut_Seconds(-1) {}

		void SetName(const std::string& stName) {                                 }
		std::string GetName() const;

		                               
		void SetEntityType(const std::string& stEntityType) { m_stEntityType = stEntityType; }
		void SetEntityName(const std::string& stEntityName) { m_stEntityName = stEntityName; }

		void SetConnectString(const std::string& stConnectString, bool bIsDsn) {
			m_stConnectString = stConnectString;
			m_bIsDsn = bIsDsn;
		}

		void SetStatementType(const std::string& stStatementType);
		void SetStatementType(StatementType st) { m_StatementType = st; }
		StatementType GetStatementType() const { return m_StatementType; }

		bool HasOutput() const;

		void SetAdvancedWhere(const std::string& stAdvancedWhere) {
			m_bIsAdvancedWhere = true;
			m_stAdvancedWhere = stAdvancedWhere;
		}

		void SetTimeOutSeconds(long lTimeOut) { m_lTimeOut_Seconds = lTimeOut; }

		void SetLocation(const std::string& stLocation) {                                 }
		std::string GetLocation() const;

		void AddOutputDataElement(data_element_id_t deId) { m_vOutputDataElement.push_back(deId); }
		void AddDBAttribute(const DBAttribute& dbAttribute) { m_vDbAttribute.push_back(dbAttribute); }

	private:
		std::string GetNameCall() const;
		std::string GetNameStatement() const;
		bool m_bIsDsn;
		bool m_bIsAdvancedWhere;
		long m_lTimeOut_Seconds;
		std::string m_stEntityType, m_stEntityName;
		std::string m_stConnectString;
		StatementType m_StatementType;
		std::string m_stAdvancedWhere;
		std::vector<data_element_id_t> m_vOutputDataElement;
		std::vector<DBAttribute> m_vDbAttribute;
	};

	class InternalFunctionActivity : public Activity {
	public:
		InternalFunctionActivity(Process& process, activity_id_t id = -1) : Activity(process, id) {}
		virtual ~InternalFunctionActivity(void) {}

		std::string GetMethod() const { return "Internal"; }

	private:
	};

	class RaiseErrorActivity : public InternalFunctionActivity {
	public:
		RaiseErrorActivity(Process& process, activity_id_t id,
			bool bTerminateTask, const std::string& stArgument)
			: InternalFunctionActivity(process, id), m_bTerminateTask(bTerminateTask), m_stArgument(stArgument) { }

		void SetName(const std::string& stName) {                                }
		std::string GetName() const;

	private:
		bool m_bTerminateTask;
		std::string m_stArgument;
	};

	class CreateScheduledEventActivity : public InternalFunctionActivity {
	public:
		CreateScheduledEventActivity(Process& process, activity_id_t id) : InternalFunctionActivity(process, id) {}
		~CreateScheduledEventActivity(void) {}

		void SetName(const std::string& stName) {                                }
		std::string GetName() const;

		void SetTaskName(const std::string& stTaskName) { m_stTaskName = stTaskName; }
		std::string GetTaskName() const { return m_stTaskName; }

		void SetTaskId(task_id_t id) {
			m_stTaskName.erase();
			m_TaskId = id;
		}
		void SetTime(const std::string& stTime) { m_stTime = stTime; }
		void SetDataElementName(const std::string& stDeName) { m_stDataElementName = stDeName; }

	private:
		task_id_t m_TaskId;
		std::string m_stTaskName;
		std::string m_stTime;
		std::string m_stDataElementName;
	};

	class FileRetrievalActivity : public InternalFunctionActivity {
	public:
		FileRetrievalActivity(Process& process, activity_id_t id = -1) : InternalFunctionActivity(process, id) {}

		void SetName(const std::string& stName) {                                }
		std::string GetName() const;

	public:
		std::string m_stUrl, m_stFileOrString;
		int m_iFileMode, m_iResponseMode;
		std::string m_stResponseName;
		std::string m_stUserName, m_stPassword, m_stCertName;
		long m_lTimeOut;
	};

}                   

#endif              
