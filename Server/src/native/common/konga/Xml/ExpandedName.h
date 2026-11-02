/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_EXPANDED_NAME
#define KONGA_XML_EXPANDED_NAME

#include <string>

namespace konga {

	namespace xml {

		class ExpandedName {
		public:
			ExpandedName(const std::string& uri, const std::string& local)
				: m_uri(uri), m_local(local) {
					if (m_uri.empty()) {
						m_stringRep = local;
					}
					else {
						m_stringRep = std::string("{") + m_uri + std::string("}") + m_local;
					}
			}

			static ExpandedName fromString(const std::string& s) {
				std::string::size_type pos = s.find_last_of('}');
				if (pos == std::string::npos) {
					return ExpandedName("", s);
				}
				return ExpandedName(s.substr(1, pos-1), s.substr(pos + 1));
			}

			std::string uri() const {
				return m_uri;
			}

			std::string local() const {
				return m_local;
			}

			std::string toString() const {
				return m_stringRep;
			}

			bool operator == (const ExpandedName& rhs) const {
				return strcmp(m_stringRep.c_str(), rhs.m_stringRep.c_str()) == 0;
			}

			bool operator < (const ExpandedName& rhs) const {
				return strcmp(m_stringRep.c_str(), rhs.m_stringRep.c_str()) < 0;
			}

		private:
			std::string m_uri;
			std::string	m_local;
			std::string m_stringRep;

		};

	}

}

#endif

