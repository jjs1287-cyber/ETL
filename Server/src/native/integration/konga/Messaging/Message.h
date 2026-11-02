/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_MESSAGING_MESSAGE_H
#define ETLFORGE_MESSAGING_MESSAGE_H

#include <string>

#include "MsgPriority.h"	                    

namespace konga {

	class Message {
	public:
		Message(void);
		virtual ~Message(void);

		void SetFrom(const std::string& sFrom);
		std::string GetFrom() const;

		void SetTo(const std::string& sTo);
		std::string GetTo() const;

		void SetSubject(const std::string& sSubject);
		std::string GetSubject() const;

		void SetPriority(MSG_PRIORITY p);
		MSG_PRIORITY GetPriority() const;

		                                                           
		  

		                                                                             
		virtual void Validate() const = 0;

	private:
		MSG_PRIORITY m_priority;
		std::string m_sFrom, m_sTo, m_sSubject;
	};
}

#endif                                

