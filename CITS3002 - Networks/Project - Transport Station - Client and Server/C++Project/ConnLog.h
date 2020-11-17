
#ifndef CONNLOG_H
#define CONNLOG_H

#include "stringfix.h"

/**
 * Represents a log of connections
 */
class ConnLog {
public:
    // Construction
    ConnLog();
    ConnLog(String);
    ConnLog(const ConnLog& orig);
    virtual ~ConnLog();

    // Methods
    String toString();
    Connection getLast();
    Connection getFirst();
    String getSep();

private:
    // Separator
    String sep = ">>";

    // Underlying list
    vector<Connection> conns;

};

#endif /* CONNLOG_H */

