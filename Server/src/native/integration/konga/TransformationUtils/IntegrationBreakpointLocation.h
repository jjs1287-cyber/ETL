/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef _KONGA_INTEGRATION_BREAKPOINT_LOCATION_H
#define _KONGA_INTEGRATION_BREAKPOINT_LOCATION_H

#include <string>
#include <set>

#include <konga/Utils/KeyValues.h>

#include <konga/Debugger/BreakpointLocation.h>

namespace konga {

	class DataElement;

	class IntegrationBreakpointLocation : public BreakpointLocation {
	public:
		IntegrationBreakpointLocation(long lOperationId, long lTransformationId, long lScriptId, long lLineNumber,
			const std::string& stAttributePath)
			: m_lOperationId(lOperationId), m_lTransformationId(lTransformationId), m_lScriptId(lScriptId),
			m_lLineNumber(lLineNumber), m_stAttributePath(stAttributePath) {}
		~IntegrationBreakpointLocation(void) {}

		virtual std::string toString() const;
		
		virtual void wasHandled();

		long getOperationId() const;

		long getTransformationId() const;

		long getScriptId() const;

		long getLineNumber() const;
		void setLineNumber(long lineNumber);

		std::string getResult() const;
		void setResult(const std::string& res);

		std::string getAttributePath() const;

		void setSourceDataElements(std::set<DataElement*> souce_de);
		void getSourceDataElements(std::set<DataElement*>& souce_de) const;

		                       
		static const std::string INSTANCE_STRING;
		static const std::string GENERATOR_PATH;

		void setProperty(const std::string& key, const std::string& value);
		std::string getProperty(const std::string& key) const;
		void getProperties(KeyValues& properties) const;

	private:
		const long m_lOperationId;
		const long m_lTransformationId;
		const long m_lScriptId;
		long m_lLineNumber;

		const std::string m_stAttributePath;
		
		std::string m_stResult;

		KeyValues m_properties;
		std::set<DataElement*> m_source_data_elements;
	};
}

#endif                                            

