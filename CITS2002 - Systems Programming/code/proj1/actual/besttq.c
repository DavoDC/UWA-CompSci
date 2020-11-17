#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

// Project 1 2019

//  ----------------------------------------------------------------------
//  DEFINITIONS
//  ----------------------------------------------------------------------

// The constants defining the maximum sizes of any required data structures
#define MAX_DEVICES             4
#define MAX_DEVICE_NAME         20
#define MAX_PROCESSES           50
#define MAX_EVENTS_PER_PROCESS 100
#define TIME_CONTEXT_SWITCH     5
#define TIME_ACQUIRE_BUS        5

// Tracefile word constants
#define CHAR_COMMENT            '#'
#define MAXWORD                 20

//  ----------------------------------------------------------------------
//  STRUCTURES
//  ----------------------------------------------------------------------

// Structures modeling aspects of the simulation

struct Device
{
    // The device's name
    char name[MAX_DEVICE_NAME];

    // Transfer rate (bytes/sec)
    int transRate;
};

struct Event
{
    // The device involved
    char devName[MAX_DEVICE_NAME];

    // When the event started (usecs)
    int startTime;

    // The time taken for the data transfer (usecs)
    int transTime;

    // How long the event has run for (usecs)
    int activeTime;
};

struct Process
{
    // Process ID
    int id;

    // When the process started (usecs)
    int startTime;

    // The exit event time of the process (usecs)
    int exitTime;


    // Event variables
    //  The row where the process's events are
    int eventRow;
    //  The current event occurring (current event column)
    int eventCol;
    //  The time left for current event (usecs)
    int eventColTime;
    //  The number of events involved
    int eventTotal;


    // Special variables
    //  How the long the process has run for (usecs)
    int activeTime;
    //  Context switch time scheduled (usecs)
    int conSwTime;
    //  Context switch pending
    bool conSwPend;
    //  Quantum time planned (usecs)
    int quantTime;
};

struct ProcessQueue
{
    // The current number of processes in the queue
    int procCount;

    // The underlying array of processes that the queue uses
    struct Process elements[MAX_PROCESSES];
};

//  ----------------------------------------------------------------------
//  GLOBAL VARIABLES
//  ----------------------------------------------------------------------

// Optimal simulation values
int optimal_time_quantum = 0;
int total_process_completion_time = 0;

// A list of the devices defined
// - in the same order they are defined in the tracefile
struct Device deviceList[MAX_DEVICES];
// - sorted according to transfer rate
struct Device sortedDeviceList[MAX_DEVICES];

// Holds the events (columns) for the each process (rows)
struct Event events[MAX_PROCESSES][MAX_EVENTS_PER_PROCESS];

// A list of all tracefile processes
struct Process procList[MAX_PROCESSES];

// Record the amount of each tracefile component
int loadedDevs = 0;
int loadedProcs = 0;
int loadedEvs = 0;

// The current simulation's 'time' (usecs)
int simTime = 0;

// The number of time quantums tested
int testedTQs = 0;

// Queues for each process state
struct ProcessQueue new;
struct ProcessQueue ready;
struct ProcessQueue running;
struct ProcessQueue exitQ;
struct ProcessQueue blocked[MAX_DEVICES];

// Notification message variables
bool notifPending = false;
char notifTime[12];
char notifInfo[2000];
char notifQStat[500];

//  ----------------------------------------------------------------------
//  QUEUE FUNCTIONS
//  ----------------------------------------------------------------------

/*
 * Return the position of a process in a given queue using a given PID
 * Return -1 if not found
 */
int getProcPos(struct ProcessQueue pq, int pid)
{
    // For all processes in the queue
    for (int i = 0; i < pq.procCount; i++) {

        // If current ID is specified ID
        if (pq.elements[i].id == pid) {

            // Return position
            return i;

            // Stop searching
            break;
        }
    }

    // Return -1 if nothing found
    return -1;
}

/*
 * Remove the given process from the specified queue
 */
void removeFromQueue(struct ProcessQueue* pq, struct Process proc)
{
    // Find the position to remove from
    int posToRemove = getProcPos(*pq, proc.id);

    // Do not act on invalid values
    if (posToRemove == -1) {
        return;
    }

    // Decrease the process counter of the queue
    pq -> procCount--;

    // Shift all elements leftward to fill the gap
    for (int i = posToRemove; i < pq -> procCount; i++) {
        pq -> elements[i] = pq -> elements[i + 1];
    }
}

/*
 * Add the given process to the end of the specified queue
 */
void addToQueue(struct ProcessQueue* pq, struct Process proc)
{
    // Save process in current window position
    pq -> elements[pq -> procCount] = proc;

    // Increase process count of queue
    pq -> procCount++;
}

/*
 * Move a given process from the source queue to the destination queue
 */
void moveBetweenQueues(struct ProcessQueue* src, struct Process proc, struct ProcessQueue * dest)
{
    // Remove the process from the source queue
    removeFromQueue(src, proc);

    // Add the process to the destination queue
    addToQueue(dest, proc);
}

//  ----------------------------------------------------------------------
//  PARSING FUNCTIONS
//  ----------------------------------------------------------------------

/*
 * Print out all the data acquired from the trace-file
 */
void printTFInfo(void)
{
    // Print header
    printf("\n #### TRACEFILE PROCESSING #### \n\n");

    // Device Lists
    //  Normal list
    printf("\n Device List (Original Order)");
    printf("\n  (Dev X) Name    TransferRate (bytes/sec) \n");
    for (int i = 0; i < loadedDevs; i++) {
        struct Device dev = deviceList[i];
        printf("  (Dev %i) %-10s %-20i  \n", i, dev.name, dev.transRate);
    }
    printf("\n Sorted Device List");
    printf("\n  (Dev X) Name    TransferRate (bytes/sec) \n");
    for (int i = 0; i < loadedDevs; i++) {
        struct Device dev = sortedDeviceList[i];
        printf("  (Dev %i) %-10s %-20i  \n", i, dev.name, dev.transRate);
    }
    printf("\n");

    // Processes
    printf("  (Proc X) ");
    printf(" %-5s ", "acTi");
    printf(" %-5s ", "csP");
    printf(" %-5s ", "csT");
    printf(" %-5s ", "evC");
    printf(" %-5s ", "evCT");
    printf(" %-5s ", "evRo");
    printf(" %-5s ", "evTo");
    printf(" %-5s ", "exTi");
    printf(" %-5s ", "id");
    printf(" %-5s ", "quTi");
    printf(" %-5s ", "stTi");
    for (int i = 0; i < new.procCount; i++) {
        struct Process curProc = procList[i];
        printf("\n");
        printf("  (Proc %i) ", i);
        printf(" %-5i ", curProc.activeTime);
        printf(" %-5i ", curProc.conSwPend);
        printf(" %-5i ", curProc.conSwTime);
        printf(" %-5i ", curProc.eventCol);
        printf(" %-5i ", curProc.eventColTime);
        printf(" %-5i ", curProc.eventRow);
        printf(" %-5i ", curProc.eventTotal);
        printf(" %-5i ", curProc.exitTime);
        printf(" %-5i ", curProc.id);
        printf(" %-5i ", curProc.quantTime);
        printf(" %-5i ", curProc.startTime);

        // Events
        printf("\n");
        printf("  (Ev X) ");
        printf("   %-7s", "active");
        printf("     %-10s ", "device");
        printf("   %-7s", "start");
        printf("   %-7s ", "trans");
        for (int j = 0; j < curProc.eventTotal; j++) {
            struct Event curEv = events[i][j];
            printf("\n");
            printf("   (Ev %i) ", j);
            printf("   %-7i ", curEv.activeTime);
            printf("   %-10s ", curEv.devName);
            printf("   %-7i ", curEv.startTime);
            printf("   %-7i ", curEv.transTime);
        }

        // Space
        printf("\n");
    }
}

/*
 * Parse the inputted trace-file and save information for processing
 */
void parse_tracefile(char program[], char tracefile[])
{
    //  Open tracefile
    FILE* fp = fopen(tracefile, "r");

    // Check that file was opened without errors
    if (fp == NULL) {
        printf("%s: unable to open '%s'\n", program, tracefile);
        exit(EXIT_FAILURE);
    }

    // File reading temporary variables
    char line[BUFSIZ];
    int lc = 0;

    // Print header
    printf("\n #### TRACEFILE RAW #### \n\n");

    // Read lines from tracefile until we reach end-of-file
    while (fgets(line, sizeof line, fp) != NULL) {
        ++lc;

        // Skip comment lines
        if (line[0] == CHAR_COMMENT) {
            continue;
        } else {
            // Print lines other than comments
            printf("%s", line);
        }

        // Breaks each line into four words
        char word0[MAXWORD], word1[MAXWORD], word2[MAXWORD], word3[MAXWORD];
        int nwords = sscanf(line, "%s %s %s %s", word0, word1, word2, word3);

        // Ignore lines with no words
        if (nwords <= 0) {
            continue;
        }

        // Look for lines defining devices, processes and events
        if (nwords == 4 && strcmp(word0, "device") == 0) {
            // Store device definitions

            // Make Device
            struct Device dev;

            // Add name info
            strcpy(dev.name, word1);

            // Add transfer speed
            dev.transRate = atoi(word2);

            // Save in device lists
            deviceList[loadedDevs] = dev;
            sortedDeviceList[loadedDevs] = dev;

            // Increase device count
            loadedDevs++;

        } else if (nwords == 1 && strcmp(word0, "reboot") == 0) {
            // Device definitions have finished

            // Sort devices by priority using Bubble Sort
            for (int pass = 1; pass < loadedDevs; pass++) {
                for (int j = 0; j < loadedDevs - pass; j++) {
                    if (sortedDeviceList[j].transRate < sortedDeviceList[j + 1].transRate) {

                        // Save first element into temp
                        struct Device temp = sortedDeviceList[j];

                        // First element becomes second element
                        sortedDeviceList[j] = sortedDeviceList[j + 1];

                        // Second element becomes temp
                        sortedDeviceList[j + 1] = temp;
                    }
                }
            }

        } else if (nwords == 4 && strcmp(word0, "process") == 0) {
            // Store process definitions

            // Make Process
            struct Process proc;

            // Add PID
            proc.id = atoi(word1);

            // Add process start time
            proc.startTime = atoi(word2);

            // Increase process count
            loadedProcs++;

            // Add to Process List
            procList[loadedProcs - 1] = proc;

        } else if (nwords == 4 && strcmp(word0, "i/o") == 0) {
            // Store event definition

            // Make Event
            struct Event ev;

            // Add absolute start time to event (i.e. process start time + event start time)
            int relTime = atoi(word1);
            int curProcStart = procList[loadedProcs - 1].startTime;
            ev.startTime = curProcStart + relTime;

            // Save device name into event
            strcpy(ev.devName, word2);

            // Get data transferred (bytes)
            int data = atoi(word3);

            // Get speed of device (bytes/sec)
            int speed = 0;
            //  Iterate over all devices
            for (int i = 0; i < loadedDevs; i++) {

                // Get the device
                struct Device curDev = deviceList[i];

                // If its name matches this event's device name
                if (strcmp(curDev.name, ev.devName) == 0) {

                    // Save transfer speed
                    speed = curDev.transRate;

                    // Stop searching
                    break;
                }
            }

            // Get transfer time (in usecs)
            //  Divide data by speed and multiply by 10^6
            double time = 1000000.0 * data / speed;
            //  Get result as int
            int timeInt = (int) time;
            //  Get decimal value
            double timeDec = time - timeInt;
            //  If decimal value is not 0
            if (timeDec != 0.0) {
                // Increment time to the next integer
                time++;
            }

            // Save in event
            ev.transTime = time;

            // Save event in array for current process
            events[loadedProcs - 1][loadedEvs] = ev;

            // Increase event count for current process
            loadedEvs++;

        } else if (nwords == 2 && strcmp(word0, "exit") == 0) {

            // Save exit time in current process
            procList[loadedProcs - 1].exitTime = atoi(word1);

        } else if (nwords == 1 && strcmp(word0, "}") == 0) {
            // A process definition has finished

            // Save number of loaded events into process
            procList[loadedProcs - 1].eventTotal = loadedEvs;

            // Save event array row into process
            procList[loadedProcs - 1].eventRow = loadedProcs - 1;

            // Reset event count for next process
            loadedEvs = 0;

        } else {

            // Print error for other lines
            printf("%s: line %i of '%s' is unrecognized", program, lc, tracefile);
            exit(EXIT_FAILURE);
        }
    }

    // Close file
    fclose(fp);
}

//  ----------------------------------------------------------------------
//  NOTIFICATION FUNCTIONS
//  ----------------------------------------------------------------------

/*
 * Generates a string containing the given time with zeroes as filler
 */
void genTimeNotif(int time)
{
    // Get time as string (variable length)
    char varTime[8];
    sprintf(varTime, "%i", time);

    // Make holder for fixed length string
    char fixedTime[16];

    // Fill fixed string with zeroes
    for (int i = 0; i < 8; i++) {
        fixedTime[i] = '0';
    }

    // Add actual string to fixed string going right to left
    int varLen = strlen(varTime);
    int aIndex = 0;
    for (int i = (8 - varLen); i < 8; i++) {
        fixedTime[i] = varTime[aIndex];
        aIndex++;
    }

    // Terminate string
    fixedTime[8] = '\0';

    // Add to time notify
    strcat(notifTime, fixedTime);
}

/*
 * Generates a string representing a queue's state
 */
void genQStat(char start[], struct ProcessQueue pq)
{
    // Add start
    strcat(notifQStat, start);

    // For each process in the queue
    for (int i = 0; i < pq.procCount; i++) {

        // Add ID
        char curProc[10];
        sprintf(curProc, "%i", pq.elements[i].id);
        strcat(notifQStat, curProc);

        // Add separator
        if (pq.procCount != 0) {
            strcat(notifQStat, "|");
        }
    }
}

/*
 * Generate the queue state string for all queues
 */
void genAllQStat(void)
{
    // Add each queue status with a space
    genQStat("Ne:", new);
    strcat(notifQStat, " ");

    genQStat("Re:", ready);
    strcat(notifQStat, " ");

    genQStat("Ru:", running);
    strcat(notifQStat, " ");

    genQStat("Ex:", exitQ);
    strcat(notifQStat, " ");

    // Add blocked queue statuses
    strcat(notifQStat, "Bl:");
    for (int i = 0; i < loadedDevs; i++) {
        genQStat("", blocked[i]);
    }
}

/*
 * Generate "time quantum ignored" notifications
 */
void genTQIgNotif(void)
{
    // Make string
    char ignore[500];
    sprintf(ignore, "p%i.freshTQ (ign'd)", running.elements[0].id);

    // Add to notify
    strcat(notifInfo, ignore);

    // Activate
    notifPending = true;
}

/*
 * Notify the user about a process transition
 */
void genTransNotif(char src[], char dest[], char extra[], struct Process proc)
{
    // Makes string
    char trans[500];
    sprintf(trans, "p%i.%s->%s%s", proc.id, src, dest, extra);

    // Add to notify
    strcat(notifInfo, trans);
}

/*
 * Notify the user about rebooting with a new TQ
 */
void printReboot(int tq)
{
    // When time is 0
    if (simTime == 0) {

        // Generate message
        char reboot[50];
        sprintf(reboot, "reboot with TQ=%i,  ", tq);

        // Add message
        strcat(notifInfo, reboot);

        // Activate message
        notifPending = true;
    }
}

/*
 * Notify the user about simulation events
 */
void printNotif(int tq)
{
    // Print notification if its pending
    if (notifPending) {

        // Use normal otherwise
        genTimeNotif(simTime);

        // Generate queue status
        genAllQStat();

        // Print notification parts with padding
        printf("\n@%s   %-64s %-10s", notifTime, notifInfo, notifQStat);

        // Reset notification state
        notifPending = false;

        // Effectively clear notification strings
        notifTime[0] = '\0';
        notifInfo[0] = '\0';
        notifQStat[0] = '\0';
    }
}

//  ----------------------------------------------------------------------
//  HELPER FUNCTIONS
//  ----------------------------------------------------------------------

/*
 * Update the results for the optimum values
 */
void updateResults(int tq)
{
    // Current total process completion time (curTPCT)
    // Initialize to last simulation time
    int curTPCT = simTime;

    // Find the smallest starting time
    int minST = 0;
    // For all processes
    for (int i = 0; i < loadedProcs; i++) {

        // Retrieve process
        struct Process curP = procList[i];

        // Update minimum
        if (i == 0 || minST > curP.startTime) {
            minST = curP.startTime;
        }
    }

    // Subtract smallest starting time
    curTPCT -= minST;

    // Increase number of time quantums tested
    testedTQs++;

    // If first simulation
    if (testedTQs == 1) {

        // Save over optimal values without check
        total_process_completion_time = curTPCT;
        optimal_time_quantum = tq;

    } else {

        // If better values are found after the 1st simulation, 
        // overwrite the optimal values

        // If the current completion time is less than the optimal,
        // then make it the new optimal completion time
        if (curTPCT <= total_process_completion_time) {
            total_process_completion_time = curTPCT;

            // Update the time quantum  when curTPCT is updated or the same
            // If the current time quantum is greater than the optimal,
            // then make it the new optimal time quantum
            if (tq > optimal_time_quantum) {
                optimal_time_quantum = tq;
            }
        }
    }

    // Print final result of this simulation
    printf("\ntotal_process_completion_time %i %i\n\n", tq, curTPCT);
}

/*
 * Get the device's blocked queue index from its name
 * If nothing found, return -1
 */
int getBlockedQPos(char devName[])
{
    // Position of device's queue:
    int pos = -1;

    // For every device
    for (int i = 0; i < loadedDevs; i++) {

        // If names match
        if (strcmp(deviceList[i].name, devName) == 0) {

            // Update position
            pos = i;

            // Stop searching
            break;
        }
    }

    // Return result
    return pos;
}

/*
 * Reset/Initialize variables for next simulation
 */
void resetSim()
{
    // Reset simulation time to -1 so first increment produces time 0
    simTime = -1;

    // Reset queue process counts
    ready.procCount = 0;
    running.procCount = 0;
    exitQ.procCount = 0;

    // Reset all blocked queue process counts
    for (int i = 0; i < loadedDevs; i++) {
        blocked[i].procCount = 0;
    }

    // For all processes
    for (int i = 0; i < loadedProcs; i++) {

        // Add from original process list to New queue
        new.elements[i] = procList[i];

        // Increase New queue process counter
        new.procCount++;

        // Reset the run time of the process
        new.elements[i].activeTime = 0;

        // Reset context switching variables
        new.elements[i].conSwPend = false;
        new.elements[i].conSwTime = 0;

        // Reset events done by process
        new.elements[i].eventCol = 0;

        // Reset current event run time to -1 to prevent time 0 triggers
        new.elements[i].eventColTime = -1;

        // Reset quantum time left to -1 to prevent time 0 triggers
        new.elements[i].quantTime = -1;
    }
}


//  ----------------------------------------------------------------------
//  PROCESS STATE TRANSITION FUNCTIONS
//  ----------------------------------------------------------------------

/*
 * Handle New -> Ready transition
 */
void handleAdmit(void)
{
    // For all process positions in the New queue
    for (int pos = 0; pos < new.procCount; pos++) {

        // If start time equals current time
        if (new.elements[pos].startTime == simTime) {

            // Move process from New queue to Ready queue
            moveBetweenQueues(&new, new.elements[pos], &ready);

            // Notify user about Admit
            genTransNotif("NEW", "READY", ", ", ready.elements[ready.procCount - 1]);
            notifPending = true;

            // Schedule a Context Switch for added element in Ready queue
            ready.elements[ready.procCount - 1].conSwTime = TIME_CONTEXT_SWITCH;
            ready.elements[ready.procCount - 1].conSwPend = true;

            // Restart scanning for processes to Admit
            handleAdmit();
        }
    }
}

/*
 * Handle context switching variables
 */
void handleConSw(void)
{
    // Decrease context switch time if pending
    if (ready.elements[0].conSwPend) {
        ready.elements[0].conSwTime--;
    }
}

/*
 * Handle Ready -> Running transition
 */
void handleDispatch(int tq)
{
    // If something is in Ready and nothing is Running
    if (ready.procCount != 0 && running.procCount == 0) {

        // Execute scheduled context switches when:
        // - Context switch time has expired
        // - A context switch is pending
        if (ready.elements[0].conSwTime <= 0 && ready.elements[0].conSwPend) {

            // Move the process from Ready to Running
            moveBetweenQueues(&ready, ready.elements[0], &running);

            // Notify user about Dispatch
            genTransNotif("READY", "RUNNING", " (frTQ), ", running.elements[0]);
            notifPending = true;

            // Reset context switch variables
            running.elements[0].conSwTime = 0;
            running.elements[0].conSwPend = false;

            // Give Running process quantum time
            running.elements[0].quantTime = tq;
        }
    }
}

/*
 * Handle the running process
 */
void handleRunning(void)
{
    // If something is in the Running queue
    if (running.procCount != 0) {

        // Increase its active time
        running.elements[0].activeTime++;

        // Decrease its time quantum
        running.elements[0].quantTime--;
    }
}

/*
 * Handle Running -> Exit transition
 */
void handleRelease(void)
{
    // If something is in theRrunning queue
    if (running.procCount == 1) {

        // If a process has been running for a time equal to its exit time
        if (running.elements[0].activeTime == running.elements[0].exitTime) {

            // Move process from Running queue to Exit queue
            moveBetweenQueues(&running, running.elements[0], &exitQ);

            // Notify user about Release
            genTransNotif("RUNNING", "EXIT", "", running.elements[0]);
            notifPending = true;

            // If there's something in the Ready queue
            if (ready.procCount != 0) {

                // Schedule a Context Switch for first in Ready Queue
                ready.elements[0].conSwTime = TIME_CONTEXT_SWITCH;
                ready.elements[0].conSwPend = true;
            }
        }
    }
}

/*
 * Handle Running -> Ready transition
 */
void handleTimeout(int tq)
{
    // If something is running and a time quantum expires
    if (running.procCount == 1 && running.elements[0].quantTime == 0) {

        // If there is one existing process
        if (loadedProcs == 1) {

            // Re-give time quantum
            running.elements[0].quantTime = tq;

            // Print that time quantum was ignored
            genTQIgNotif();

        } else if (loadedProcs >= 2) {
            // Else if 2 or more processes exist

            // If something is in ready
            if (ready.procCount != 0) {

                // Move currently running process to ready queue
                moveBetweenQueues(&running, running.elements[0], &ready);

                // Notify about expiration
                char expire[200];
                sprintf(expire, "p%i.expire,  ", running.elements[0].id);
                strcat(notifInfo, expire);

                // Notify about Timeout
                genTransNotif("RUNNING", "READY", "", running.elements[0]);
                notifPending = true;

                // Schedule a Context Switch for first in Ready Queue
                ready.elements[0].conSwTime = TIME_CONTEXT_SWITCH;
                ready.elements[0].conSwPend = true;

            } else {
                // Re-give time quantum
                running.elements[0].quantTime = tq;

                // Print that time quantum was ignored
                genTQIgNotif();
            }
        }
    }
}

/*
 * Handle Running -> Blocked transition
 */
void handleEventWait(void)
{
    // If something is running and it hasn't finished its events
    bool notFinished = !(running.elements[0].eventTotal == running.elements[0].eventCol);
    if (running.procCount == 1 && notFinished) {

        // Get process's event row
        int evRow = running.elements[0].eventRow;

        // Get process current event column
        int evCol = running.elements[0].eventCol;

        // Get current event
        struct Event curEv = events[evRow][evCol];

        // Calculate relative starting time of event
        int relStart = curEv.startTime - running.elements[0].startTime;

        // Move process from Running to Blocked when process
        // has breached relative event starting time
        if (relStart <= running.elements[0].activeTime) {

            // Give the process IO time
            running.elements[0].eventColTime = TIME_ACQUIRE_BUS + curEv.transTime;

            // Find blocked queue index (of event's device)
            int bQPos = getBlockedQPos(curEv.devName);

            // Move from Running to Blocked
            moveBetweenQueues(&running, running.elements[0], &blocked[bQPos]);

            // Notify about Blocked transition
            char blockTran[100];
            sprintf(blockTran, "BLOCKED(%s)", curEv.devName);
            genTransNotif("RUNNING", blockTran, "", running.elements[0]);

            // Activate notification
            notifPending = true;

            // If a process is in ready
            if (ready.procCount != 0) {

                // Schedule a Context Switch for first in Ready Queue
                ready.elements[0].conSwTime = TIME_CONTEXT_SWITCH;
                ready.elements[0].conSwPend = true;
            }
        }
    }
}

/*
 * Handle Blocked -> Ready transition
 */
void handleEventOccurs(void)
{
    // For all blocked queues
    for (int q = 0; q < loadedDevs; q++) {

        // If it has something, and first blocked process IO time has expired
        if (blocked[q].procCount != 0 && blocked[q].elements[0].eventColTime == 0) {

            // Schedule context switch
            blocked[q].elements[0].conSwTime = TIME_CONTEXT_SWITCH;
            blocked[q].elements[0].conSwPend = true;

            // Move process to next event
            blocked[q].elements[0].eventCol++;

            // Move process from Blocked to Ready
            moveBetweenQueues(&blocked[q], blocked[q].elements[0], &ready);

            // Notification about Blocked transition
            char blockTran[100];
            int evRow = ready.elements[ready.procCount - 1].eventRow;
            int evCol = ready.elements[ready.procCount - 1].eventCol - 1;
            sprintf(blockTran, "BLOCKED(%s)", events[evRow][evCol].devName);
            genTransNotif(blockTran, "READY", "", ready.elements[ready.procCount - 1]);

            // Active notification
            notifPending = true;
        }
    }
}

/*
 * Handle blocked processes
 */
void handleBlocked(void)
{
    // For all devices (in order of transfer rate)
    for (int d = 0; d < loadedDevs; d++) {

        // Get device (from sorted device list)
        struct Device curDev = sortedDeviceList[d];

        // Get position of blocked queue of device
        int bQPos = getBlockedQPos(curDev.name);

        // Do not process invalid values
        if (bQPos == -1) {
            break;
        }

        // If it the queue is holding a process
        if (blocked[bQPos].procCount != 0) {

            // Decrease the IO time allocated for the first process
            blocked[bQPos].elements[0].eventColTime--;

            // Do not decrement further,
            // so only the first highest priority queue progresses
            break;
        }
    }
}

//  ----------------------------------------------------------------------
//  SIMULATE JOB MIX
//  ----------------------------------------------------------------------

/*
 * SIMULATE THE JOB-MIX FROM THE TRACEFILE FOR THE GIVEN TIME-QUANTUM
 */
void simulate_job_mix(int time_quantum)
{
    // Initialize / Reset the simulation variables
    resetSim();

    // Print initial header
    if (testedTQs == 0) {
        printTFInfo();
        printf("\n #### SIMULATIONS #### \n");
    }

    // Print simulation header
    printf("\nrunning simulate_job_mix( time_quantum = %i usecs )", time_quantum);

    // Keep processing
    // - while not all processes have finished yet
    // - while time has not reached limit
    while (exitQ.procCount != loadedProcs && simTime != 200000000) {

        // Progress time by 1 usec
        simTime++;

        // Print reboot message if needed
        printReboot(time_quantum);

        // Run process state handlers
        handleConSw();
        handleDispatch(time_quantum);
        handleAdmit();
        handleRelease();
        handleEventOccurs();
        handleEventWait();
        handleBlocked();
        handleTimeout(time_quantum);
        handleRunning();

        // Print notification
        printNotif(time_quantum);
    }

    // Update final results based on simulation done
    updateResults(time_quantum);
}

//  ----------------------------------------------------------------------
//  MAIN FUNCTION
//  ----------------------------------------------------------------------

/*
 * Notify user about argument format
 */
void usage(char program[])
{

    printf("Usage: %s tracefile TQ-first [TQ-final TQ-increment]\n", program);
    exit(EXIT_FAILURE);
}

/*
 * Entry point
 */
int main(int argcount, char *argvalue[])
{
    int TQ0 = 0, TQfinal = 0, TQinc = 0;

    //  CALLED WITH THE PROVIDED TRACEFILE (NAME) AND THREE TIME VALUES
    if (argcount == 5) {
        TQ0 = atoi(argvalue[2]);
        TQfinal = atoi(argvalue[3]);
        TQinc = atoi(argvalue[4]);

        if (TQ0 < 1 || TQfinal < TQ0 || TQinc < 1) {
            usage(argvalue[0]);
        }
    }//  CALLED WITH THE PROVIDED TRACEFILE (NAME) AND ONE TIME VALUE
    else if (argcount == 3) {
        TQ0 = atoi(argvalue[2]);
        if (TQ0 < 1) {
            usage(argvalue[0]);
        }
        TQfinal = TQ0;
        TQinc = 1;
    }//  CALLED INCORRECTLY, REPORT THE ERROR AND TERMINATE
    else {
        usage(argvalue[0]);
    }

    //  READ THE JOB-MIX FROM THE TRACEFILE, STORING INFORMATION IN DATA-STRUCTURES
    parse_tracefile(argvalue[0], argvalue[1]);

    //  SIMULATE THE JOB-MIX FROM THE TRACEFILE, VARYING THE TIME-QUANTUM EACH TIME.
    //  WE NEED TO FIND THE BEST (SHORTEST) TOTAL-PROCESS-COMPLETION-TIME
    //  ACROSS EACH OF THE TIME-QUANTA BEING CONSIDERED
    for (int time_quantum = TQ0; time_quantum <= TQfinal; time_quantum += TQinc) {
        simulate_job_mix(time_quantum);
    }

    //  PRINT THE PROGRAM'S RESULT
    printf("best %i %i\n", optimal_time_quantum, total_process_completion_time);

    exit(EXIT_SUCCESS);
}