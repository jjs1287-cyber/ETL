/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_MAIL_CLIENT_EMAIL_ADDRESS
#define ETLFORGE_MAIL_CLIENT_EMAIL_ADDRESS

#include <string>

namespace konga {

	class EmailAddress {
	public:
		EmailAddress(void) { }

		                                                                          
		explicit EmailAddress(const std::string& sEmailAddress) {
			parseEmail(sEmailAddress);
		}

		~EmailAddress(void) {};

		                                      
		std::string getPersonalName() const {
			return m_sPersonalName;
		}

		                                                                  
		std::string getMailBox() const {
			return m_sMailBox;
		}

		                                                   
		std::string getHost() const {
			return m_sHost;
		}

		                                                                          
		                                                       
		std::string getEmailAddress() const;

		                                   
		std::string getMailBoxAtHost() const;

		bool empty() const;

	protected:

		                                                                        
		void parseEmail(const std::string& sEmail);

	private:
		std::string m_sPersonalName;
		std::string m_sMailBox;
		std::string m_sHost;
	};

	                                                                          
	                                                                            
	                                               
	bool operator==(const EmailAddress& lhs, const EmailAddress& rhs);
}

#endif                                      

