/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_MAIL_CLIENT_EMAIL_PROPERTIES
#define ETLFORGE_MAIL_CLIENT_EMAIL_PROPERTIES

#include <vector>

#include "EmailAddress.h"

namespace konga {

	class EmailProperties {
	public:
		EmailProperties(const std::string& sFrom)
			: m_From(sFrom) {}
		EmailProperties(const EmailAddress& From)
			: m_From(From) {}
		~EmailProperties(void) {};

		void setFrom(const std::string& sFrom);
		void setReplyTo(const std::string& sReplyTo);
		void setReturnPath(const std::string& sReturnPath);

		void addToAddress(const std::string& sEmail);
		void addCCAddress(const std::string& sEmail);
		void addBCCAddress(const std::string& sEmail);

		void addCommaSeparatedToAddresses(const std::string& stAddresses);
		void addCommaSeparatedCCAddresses(const std::string& stAddresses);
		void addCommaSeparatedBCCAddresses(const std::string& stAddresses);

		const EmailAddress& getFromByRef() const {
			return m_From;
		}

		const EmailAddress& getReplyToByRef() const {
			return m_ReplyTo;
		}

		const EmailAddress& getReturnPathByRef() const {
			return m_ReturnPath;
		}

		typedef std::vector<EmailAddress> vAddrT;

		const vAddrT& getToAddressesByRef() const {
			return m_vTo;
		}

		const vAddrT& getCCAddressesByRef() const {
			return m_vCC;
		}

		const vAddrT& getBCCAddressesByRef() const {
			return m_vBCC;
		}

	private:
		EmailAddress m_From, m_ReplyTo, m_ReturnPath;
		vAddrT m_vTo, m_vCC, m_vBCC;
	};
}

#endif                                         

