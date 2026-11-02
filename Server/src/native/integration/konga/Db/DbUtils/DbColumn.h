/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBCOLUMN_H
#define KONGA_DBCOLUMN_H

#include <string>

namespace konga
{

struct DbColumn
{
    DbColumn() : DataType(0), Size(0L), BufferLength(0L), DecimalDigits(0),
                    Radix(0), Nullable(0), HasDefaultValue(false), SqlDataType(0), DateTimeSub(0),
                    OctetLength(0L), OrdinalPosition(0L) {}
    std::string Catalog;
    std::string Schema;
    std::string Name;
    std::string Table;
    int         DataType;
    std::string DataTypeName;
    long        Size;
    long        BufferLength;
    int         DecimalDigits;
    int         Radix;
    int         Nullable;
    std::string Remarks;
    bool        HasDefaultValue;
    std::string DefaultValue;
    int         SqlDataType;
    std::string SqlDataTypeName;
    int         DateTimeSub;
    long        OctetLength;
    long        OrdinalPosition;
    bool		IsNullable;
};

}                   

#endif                    
