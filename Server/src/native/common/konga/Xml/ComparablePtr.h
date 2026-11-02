/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_COMPARABLE_H
#define KONGA_COMPARABLE_H

  
                                                                                                                 
   
namespace konga {

    template<class C> class ComparablePtr {
    public:
        ComparablePtr(C* p = 0) : m_p(p) {}
        ComparablePtr(const ComparablePtr<C>& ptr) : m_p(ptr.m_p) {}
        virtual ~ComparablePtr() {}

    public:
        virtual bool operator!() const { return this->m_p == 0; }

        virtual bool operator==(const ComparablePtr& rhs) const {
            return this->m_p == rhs.m_p;
        }
        virtual bool operator!=(const ComparablePtr& rhs) const {
			return !(*this == rhs);
		}

                                                             
        virtual bool operator==(int rhs) const { return ( rhs == 0 ) ? this->m_p==0 : false; }
        virtual bool operator!=(int rhs) const { return !(*this == rhs); }

    protected:
        C* m_p;
    };

	                                              
    template<class C>
    bool operator==(int lhs, const ComparablePtr<C>& rhs) {
        return (rhs == lhs);
    }

    template<class C>
    bool operator!=(int lhs, const ComparablePtr<C>& rhs) {
        return !(lhs == rhs);
    }
}
#endif                      

