/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_OPERATION_DATA_ELEMENT_HOLDER_H
#define KONGA_OPERATION_DATA_ELEMENT_HOLDER_H

#include <map>

#include <konga/Utils/Matcher.h>

namespace konga {

	class DataElement;
	class DataElementHandler;
	class UserInfo;

	                                                                     
	                     
	class OperationDataElementHolder {
	public:
		OperationDataElementHolder(void) {}
		virtual ~OperationDataElementHolder(void) {}

		virtual bool empty() const = 0;

		                                                                          
		virtual DataElement* getOperationDataElement(const std::string& stName) const = 0;

		virtual bool isOperationDataElementDefined(const std::string& stName) const = 0;

		virtual void removeOperationDataElement(const std::string& stName) = 0;

		virtual void setCurrentUser(const UserInfo& currentUser) = 0;

		                                                                     
		                                                                     
		                                                            
		virtual const std::map<std::string, DataElement*>& getOperationDataElements() const = 0;

		                                                               
		virtual void getOperationDataElementsStartingWith(const std::string& startsWith, std::map<std::string, DataElement*>& filtered) const = 0;

		virtual void getMatchingOperationDataElements(const Matcher& matcher, std::map<std::string, DataElement*>& filtered) const = 0;

		virtual std::string replaceOperationDataElementInString(const std::string& stValue) const = 0;

		                                                 
		virtual void registerDataElementHandler(const std::string& deName,
			DataElementHandler* pDEHandler) = 0;

		virtual void InitCounter(const std::string& stCounter, long long initial_value) = 0;

		virtual std::string getCountersAsString(const std::string& separator) const = 0;

		                                                    
		                                                 
		                                         
		virtual OperationDataElementHolder* clone() const = 0;

		                                                 
		                          
		virtual void merge(const OperationDataElementHolder& opHolder) = 0;
	};
}

#endif                                         

