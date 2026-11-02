/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef WF_DATA_ELEMENT_H
#define WF_DATA_ELEMENT_H

#include <string>
#include <vector>

#include "WorkflowIdTypes.h"

namespace konga {
namespace workflow {

	class DataElement {
	public:
		DataElement(data_element_id_t id = -1) : m_id(id), m_bIsRequired(false) {}
		virtual ~DataElement() {}

		data_element_id_t GetId() const { return m_id; }
		void SetId(data_element_id_t id) { m_id = id; }

		void SetName(const std::string& stName) { m_stName = stName; }
		std::string GetName() const { return m_stName; }

		void SetDescription(const std::string& stDescription) { m_stDescription = stDescription; }
		std::string GetDescription() const { return m_stDescription; }

		void SetDisplayName(const std::string& stDisplayName) { m_stDisplayName = stDisplayName; }
		std::string GetDisplayName() const { return m_stDisplayName; }

		void SetType(const std::string& stType) { m_stType = stType; }
		std::string GetType() const { return m_stType; }

		void SetOriginType(const std::string& stOriginType) { m_stOriginType = stOriginType; }
		std::string GetOriginType() const { return m_stOriginType; }

		void SetInitialValue(const std::string& stInitialValue) { m_stInitialValue = stInitialValue; }
		std::string GetInitialValue() const { return m_stInitialValue; }

		bool IsRequired() const { return m_bIsRequired; }
		void SetRequired(bool b) { m_bIsRequired = b; }

	private:
		data_element_id_t m_id;
		bool m_bIsRequired;

		std::string m_stName;
		std::string m_stDescription;
		std::string m_stDisplayName;
		std::string m_stType;		            
		
		std::string m_stOriginType;	                    
		std::string m_stInitialValue;

	};

}                      
}                   

#endif                     
