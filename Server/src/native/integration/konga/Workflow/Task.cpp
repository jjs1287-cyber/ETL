/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "DataElement.h"
#include "Task.h"

using namespace std;
using namespace konga::workflow;

namespace konga {

	void Task::AddDataElement(DataElement* pDataElement) { 
		m_Process.AddDataElement(pDataElement);
		m_vDataElements.push_back(pDataElement->GetId());
	}

	void Task::AddActivity(Activity* pActivity) {
		m_Process.AddActivity(pActivity);
		m_vActivities.push_back(pActivity->GetId());
	}

	bool Task::HasDataElement(const string& stName) {
		for( DataElementListT::const_iterator it = m_vDataElements.begin(); it != m_vDataElements.end(); ++it ) {
			if ( m_Process.GetDataElement(*it).GetName() == stName ) {
				return true;
			}
		}
		return false;
	}

	vector<const DataElement*> Task::GetDataElements() const {
		vector<const DataElement*> vDE;
		for( DataElementListT::const_iterator it = m_vDataElements.begin(); it != m_vDataElements.end(); ++it ) {
			vDE.push_back( &m_Process.GetDataElement(*it) );
		}
		return vDE;
	}

	std::string Task::GetDocumentIdListAsString() const {
		if ( m_vDocuments.empty() ) return string("-1");

		             
		                                                                                                                
		return toString<document_id_t>(m_vDocuments.at(0));
	}
}                   
