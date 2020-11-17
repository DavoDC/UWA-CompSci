
#ifndef LOCALTIME_H
#define LOCALTIME

#include "stringfix.h"

class LocalTime {
public:
    // Construction
    LocalTime();
    LocalTime(int hourIn, int minIn);
    LocalTime(String);
    LocalTime(const LocalTime& orig);
    virtual ~LocalTime();

    // Methods
    String toString();
    void saveIntoFields(int, int);
    LocalTime getDiff(LocalTime);
    LocalTime getDiffBetween(LocalTime, LocalTime);
    struct tm getTMStruct();
    bool isNegative();

private:

    // Separator
    String sep = ":";

    // Time
    struct tm tmStruct;

};

#endif /* LOCALTIME */

