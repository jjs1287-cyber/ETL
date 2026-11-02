/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBNULLEXCEPTION_H
#define KONGA_DBNULLEXCEPTION_H

#include <string>
                          
#include "DbException.h"

namespace konga
{
                                                         
class DbNullException : public DbException
{
public:
    DbNullException(const std::string& stField) : DbException(), m_stField(stField)
    {
        SetErrorString("An attempt was made to access the NULL value of the field " + stField + ".");
    }

    std::string GetField() const {
        return m_stField;
    }

private:
    std::string m_stField;
};

}

#endif

