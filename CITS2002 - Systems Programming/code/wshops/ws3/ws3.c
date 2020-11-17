#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
//  Bottom 3 gives access to M_PI
#if     defined(__linux__)
#define __USE_XOPEN
#endif
#include <math.h>

#define EARTH_RADIUS_IN_METRES          6372797

#define MIN_LAT    -90.0
#define MAX_LAT     90.0
#define MIN_LON    -180.0
#define MAX_LON     180.0

/// Requires SPECIAL COMPILATION
//  mycc ws3 ws3.c -lm
// 
//  Arg Example   
// ./haversine -31.977537 115.816509 -31.982831 115.818898

double degrees_to_radians(double degrees)
{
    return(degrees * M_PI / 180.0);
}

double radians_to_degrees(double radians)
{
    return(radians * 180.0 / M_PI);
}

bool valid_location(double latitude, double longitude)
{
    // Location values must be above/equal to mins AND below/equal to maxes
    return(latitude >= MIN_LAT && latitude <= MAX_LAT &&
            longitude >= MIN_LON && longitude <= MAX_LON);
}

double haversine(double lat1, double lon1, double lat2, double lon2)
{
    double deltalat = (lat2 - lat1) / 2.0;
    double deltalon = (lon2 - lon1) / 2.0;

    double sin1 = sin(degrees_to_radians(deltalat));

    double cos1 = cos(degrees_to_radians(lat1));
    double cos2 = cos(degrees_to_radians(lat2));

    double sin2 = sin(degrees_to_radians(deltalon));

    double x = sin1 * sin1 + cos1 * cos2 * sin2*sin2;

    // in metres
    return(2.0 * EARTH_RADIUS_IN_METRES * asin(sqrt(x)));
}

int main(int argc, char *argv[])
{
    int result;

    //  CHECK THE NUMBER OF ARGUMENTS
    if (argc != 5) {
        printf("Usage: %s lat1 lon1 lat2 lon2\n", argv[0]);
        result = EXIT_FAILURE;
    } else {
        //  CONVERT EACH COMMAND-LINE ARGUMENT TO A FLOATING-POINT NUMBER
        double lat1 = atof(argv[1]);
        double lon1 = atof(argv[2]);

        double lat2 = atof(argv[3]);
        double lon2 = atof(argv[4]);

        if (valid_location(lat1, lon1) == false) {
            //  ENSURE THAT THE 2 PROVIDED LOCATIONS ARE EACH VALID
            printf("%s: location 1 is invalid!\n", argv[0]);
            result = EXIT_FAILURE;
        } else if (valid_location(lat2, lon2) == false) {
            printf("%s: location 2 is invalid!\n", argv[0]);
            result = EXIT_FAILURE;
        } else {
            //  ALL GOOD - CALCULATE AND PRINT DISTANCE BETWEEN LOCATIONS
            printf("distance = %lfm\n", haversine(lat1, lon1, lat2, lon2));
            result = EXIT_SUCCESS;
        }
    }
    return result;
}
