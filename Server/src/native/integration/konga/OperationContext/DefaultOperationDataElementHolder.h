/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DEFAULT_OPERATION_DATA_ELEMENT_HOLDER_H
#define KONGA_DEFAULT_OPERATION_DATA_ELEMENT_HOLDER_H

#include <map>
#include <stack>
#include <string>

#include <boost/noncopyable.hpp>
#include <boost/thread/mutex.hpp>

#include <konga/Utils/MapUtils.h>

#include <konga/ItemAccess/UserInfo.h>

#include "AccessProtectedDataElementsHandler.h"
#include "OperationDataElementHolder.h"

namespace konga {

	class DefaultOperationDataElementHolder : public OperationDataElementHolder, boost::noncopyable {
	public:
		DefaultOperationDataElementHolder(const UserInfo& currentUser);
		virtual ~DefaultOperationDataElementHolder(void);

		                                                          
		virtual bool empty() const;

		virtual DataElement* getOperationDataElement(const std::string& sDataElementName) const;

		virtual bool isOperationDataElementDefined(const std::string& sDataElementName) const;

		virtual void removeOperationDataElement(const std::string& sDataElementName);

		virtual void setCurrentUser(const UserInfo& currentUser);

		virtual const std::map<std::string, DataElement*>& getOperationDataElements() const;

		virtual void getOperationDataElementsStartingWith(const std::string& startsWith, std::map<std::string, DataElement*>& filtered) const;

		virtual void getMatchingOperationDataElements(const Matcher& matcher, std::map<std::string, DataElement*>& filtered) const;

		virtual std::string replaceOperationDataElementInString(const std::string& stValue) const;

		virtual void registerDataElementHandler(const std::string& deName,
			DataElementHandler* pDEHandler);

		virtual void InitCounter(const std::string& stCounter, long long initial_value);

		virtual std::string getCountersAsString(const std::string& separator) const;

		virtual OperationDataElementHolder* clone() const;

		virtual void merge(const OperationDataElementHolder& opHolder);

	protected:	                                           
		                                                                  
		                                  
		const DataElement* getReadOnlyDataElement(const std::string& sDataElementName) const;

	private:
		                                                             
		                                                           
		DataElement* getExternallyHandledDataElement(const std::string& sDataElementName) const;

		bool isCounter(const std::string& key) const;

	private:
		UserInfo m_CurrentUser;
		AccessProtectedDataElementsHandler m_protectedDataElements;

		typedef std::map<std::string, DataElement*, icasestr_less> IDataElementMapT;
		typedef std::map<std::string, std::stack<DataElementHandler*>, icasestr_less> IDataElementHandlerMapT;
		mutable IDataElementMapT m_DataElements;	                                        
		mutable std::map<std::string, DataElement*> m_cache;
		IDataElementHandlerMapT m_DataElementHandlers;

		typedef std::map<std::string, bool, icasestr_less> CounterMapT;
		CounterMapT m_counters;

		mutable boost::mutex m_cloneLock;
	};
}

#endif                                                 

