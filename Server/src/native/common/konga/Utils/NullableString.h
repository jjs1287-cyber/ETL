/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_NULLABLE_STRING_H
#define KONGA_UTILS_NULLABLE_STRING_H

#include <string>

#include "Nullable.h"

namespace konga {

	                                                             
	class NullableString : public Nullable<std::string> {
	public:
		                                        
		NullableString(void) : Nullable<std::string>() {}

		                                                                                                               
		                                                                                                
		explicit NullableString(const char* p) : Nullable<std::string>() {
			setNull(p==0);
			if ( p ) getT().assign(p);
		}

		NullableString(const std::string& s) : Nullable<std::string>(s) {}

		NullableString(const NullableString& nullableString) : Nullable<std::string>(nullableString) {}

		virtual ~NullableString(void) {}

		                         
		NullableString& operator=(const char* p) {
			setNull(p==0);
			if ( p==0 ) {
				getT().assign("");	                                          
			}
			else {
				getT().assign(p);
			}
			return *this;
		}

		                                                             
		bool operator==(const char* p) const {
			return equals(p);
		}

		bool operator!=(const char* p) const {
			return !equals(p);
		}
		bool operator==(const NullableString& nullable) const {
			return Nullable<std::string>::operator==(nullable);
		}

		bool operator!=(const NullableString& nullable) const {
			return !(*this==nullable);
		}

		const std::string& str() const {
			return const_ref();
		}

	private:
		bool equals(const char* p) const {
			if ( isNull() ) {
				return p == 0;
			}
			else {
				if ( p == 0 )
					return false;
				return const_ref() == p;
			}
		}
	};

	namespace {
		const NullableString null_string(0);
	}
}

#endif                                 
