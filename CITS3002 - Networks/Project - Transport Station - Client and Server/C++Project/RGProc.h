#ifndef RGPROC_H
#define RGPROC_H

#include "stringfix.h"
#include "StatGram.h"

/**
 * Processes route grams
 */
class RGProc {
public:

    // Construction
    RGProc();
    RGProc(const RGProc& orig);
    virtual ~RGProc();

    // Methods
    void processRG(StatGram);
    void processSearchingRG(StatGram);
    void processArrivedRG(StatGram);
    void processFurtherRG(StatGram, String, RouteStop);
    void processBackTrackingRG(StatGram sg);

private:

};

#endif /* RGPROC_H */

