/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_MESSAGING_PROCESS_MESSAGE_H
#define ETLFORGE_MESSAGING_PROCESS_MESSAGE_H

#include <vector>
#include <string>

#include "Message.h"

namespace konga {

	enum DE_TYPE { DET_UNDEFINED=0, DET_BOOL, DET_INT, DET_LONG, DET_FLOAT, DET_DOUBLE, DET_STRING, DET_DATE, DET_TEXT };

	class ProcessMessage : public Message {
	public:
		ProcessMessage(void) {}
		virtual ~ProcessMessage(void) {}

		void SetProcessName(const std::string& stName);
		std::string GetProcessName() const;
		void SetTaskName(const std::string& stName);
		std::string GetTaskName() const;

		void SetProcessVariables(const std::string& stProcessVariables);
		std::string GetProcessVariables() const;

		void SetOwner(const std::string& sOwner);
		std::string GetOwner() const;

		void Validate() const;

		static DE_TYPE TypeStringToEnum(const std::string& sTypeName);

		struct DataElement {
			DataElement(const std::string& name, const std::string& value, DE_TYPE t) :
				stName(name), stValue(value), type(t) {}
				std::string stName, stValue;
				DE_TYPE type;
				std::string DataTypeAsString() const;
		};
		typedef std::vector<ProcessMessage::DataElement> DEVector;

		void AddDataElement(const std::string& stName, const std::string& stValue, DE_TYPE deType = DET_UNDEFINED);
		const DEVector& GetDataElements() const;

	private:
		static const char* const DataTypeStrings[];

		std::string m_sProcessName, m_sTaskName;
		std::string m_sProcessVariables;
		std::string m_sOwner;

		std::vector<ProcessMessage::DataElement> m_vDataElements;
	};
}

#endif                                        
