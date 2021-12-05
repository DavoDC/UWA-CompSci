#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=Cygwin-Windows
CND_DLIB_EXT=dll
CND_CONF=Debug
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include NBmakefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/code/examPrac/examQs.o \
	${OBJECTDIR}/code/labs/lab1/bruteForceRot/bfRot.o \
	${OBJECTDIR}/code/labs/lab1/orig/rotate.o \
	${OBJECTDIR}/code/labs/lab1/q1-6/rot6.o \
	${OBJECTDIR}/code/labs/lab1/q7-8/rot8.o \
	${OBJECTDIR}/code/labs/lab1/q9/rot9.o \
	${OBJECTDIR}/code/labs/lab1/scanRot/scanRot.o \
	${OBJECTDIR}/code/labs/lab2/q1/q1.o \
	${OBJECTDIR}/code/labs/lab2/q2-3/q3.o \
	${OBJECTDIR}/code/labs/lab2/q4/q4.o \
	${OBJECTDIR}/code/labs/lab2/q5/q5.o \
	${OBJECTDIR}/code/labs/lab2/q6/q6.o \
	${OBJECTDIR}/code/labs/lab3/q1/q1.o \
	${OBJECTDIR}/code/labs/lab3/q2/q2.o \
	${OBJECTDIR}/code/labs/lab3/q3/q3.o \
	${OBJECTDIR}/code/labs/lab3/q4/q4.o \
	${OBJECTDIR}/code/labs/lab3/q5/complex/q5.o \
	${OBJECTDIR}/code/labs/lab3/q5/simp/q5.o \
	${OBJECTDIR}/code/labs/lab4/q1/q1.o \
	${OBJECTDIR}/code/labs/lab4/q2/q2.o \
	${OBJECTDIR}/code/labs/lab4/q4/q4.o \
	${OBJECTDIR}/code/labs/lab5/q1/q1.o \
	${OBJECTDIR}/code/labs/lab5/q2/src/pointers.o \
	${OBJECTDIR}/code/labs/lab5/q2/src/stringtest.o \
	${OBJECTDIR}/code/labs/lab5/q3/q3.o


# C Compiler Flags
CFLAGS=-std=c99 -Wall -Werror -pedantic

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/cits2002_-_systems_programming.exe

${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/cits2002_-_systems_programming.exe: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}
	${LINK.c} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/cits2002_-_systems_programming ${OBJECTFILES} ${LDLIBSOPTIONS}

${OBJECTDIR}/code/examPrac/examQs.o: code/examPrac/examQs.c
	${MKDIR} -p ${OBJECTDIR}/code/examPrac
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/examPrac/examQs.o code/examPrac/examQs.c

${OBJECTDIR}/code/labs/lab1/bruteForceRot/bfRot.o: code/labs/lab1/bruteForceRot/bfRot.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab1/bruteForceRot
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab1/bruteForceRot/bfRot.o code/labs/lab1/bruteForceRot/bfRot.c

${OBJECTDIR}/code/labs/lab1/orig/rotate.o: code/labs/lab1/orig/rotate.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab1/orig
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab1/orig/rotate.o code/labs/lab1/orig/rotate.c

${OBJECTDIR}/code/labs/lab1/q1-6/rot6.o: code/labs/lab1/q1-6/rot6.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab1/q1-6
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab1/q1-6/rot6.o code/labs/lab1/q1-6/rot6.c

${OBJECTDIR}/code/labs/lab1/q7-8/rot8.o: code/labs/lab1/q7-8/rot8.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab1/q7-8
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab1/q7-8/rot8.o code/labs/lab1/q7-8/rot8.c

${OBJECTDIR}/code/labs/lab1/q9/rot9.o: code/labs/lab1/q9/rot9.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab1/q9
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab1/q9/rot9.o code/labs/lab1/q9/rot9.c

${OBJECTDIR}/code/labs/lab1/scanRot/scanRot.o: code/labs/lab1/scanRot/scanRot.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab1/scanRot
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab1/scanRot/scanRot.o code/labs/lab1/scanRot/scanRot.c

${OBJECTDIR}/code/labs/lab2/q1/q1.o: code/labs/lab2/q1/q1.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab2/q1
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab2/q1/q1.o code/labs/lab2/q1/q1.c

${OBJECTDIR}/code/labs/lab2/q2-3/q3.o: code/labs/lab2/q2-3/q3.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab2/q2-3
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab2/q2-3/q3.o code/labs/lab2/q2-3/q3.c

${OBJECTDIR}/code/labs/lab2/q4/q4.o: code/labs/lab2/q4/q4.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab2/q4
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab2/q4/q4.o code/labs/lab2/q4/q4.c

${OBJECTDIR}/code/labs/lab2/q5/q5.o: code/labs/lab2/q5/q5.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab2/q5
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab2/q5/q5.o code/labs/lab2/q5/q5.c

${OBJECTDIR}/code/labs/lab2/q6/q6.o: code/labs/lab2/q6/q6.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab2/q6
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab2/q6/q6.o code/labs/lab2/q6/q6.c

${OBJECTDIR}/code/labs/lab3/q1/q1.o: code/labs/lab3/q1/q1.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab3/q1
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab3/q1/q1.o code/labs/lab3/q1/q1.c

${OBJECTDIR}/code/labs/lab3/q2/q2.o: code/labs/lab3/q2/q2.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab3/q2
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab3/q2/q2.o code/labs/lab3/q2/q2.c

${OBJECTDIR}/code/labs/lab3/q3/q3.o: code/labs/lab3/q3/q3.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab3/q3
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab3/q3/q3.o code/labs/lab3/q3/q3.c

${OBJECTDIR}/code/labs/lab3/q4/q4.o: code/labs/lab3/q4/q4.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab3/q4
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab3/q4/q4.o code/labs/lab3/q4/q4.c

${OBJECTDIR}/code/labs/lab3/q5/complex/q5.o: code/labs/lab3/q5/complex/q5.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab3/q5/complex
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab3/q5/complex/q5.o code/labs/lab3/q5/complex/q5.c

${OBJECTDIR}/code/labs/lab3/q5/simp/q5.o: code/labs/lab3/q5/simp/q5.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab3/q5/simp
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab3/q5/simp/q5.o code/labs/lab3/q5/simp/q5.c

${OBJECTDIR}/code/labs/lab4/q1/q1.o: code/labs/lab4/q1/q1.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab4/q1
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab4/q1/q1.o code/labs/lab4/q1/q1.c

${OBJECTDIR}/code/labs/lab4/q2/q2.o: code/labs/lab4/q2/q2.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab4/q2
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab4/q2/q2.o code/labs/lab4/q2/q2.c

${OBJECTDIR}/code/labs/lab4/q4/q4.o: code/labs/lab4/q4/q4.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab4/q4
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab4/q4/q4.o code/labs/lab4/q4/q4.c

${OBJECTDIR}/code/labs/lab5/q1/q1.o: code/labs/lab5/q1/q1.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab5/q1
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab5/q1/q1.o code/labs/lab5/q1/q1.c

${OBJECTDIR}/code/labs/lab5/q2/src/pointers.o: code/labs/lab5/q2/src/pointers.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab5/q2/src
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab5/q2/src/pointers.o code/labs/lab5/q2/src/pointers.c

${OBJECTDIR}/code/labs/lab5/q2/src/stringtest.o: code/labs/lab5/q2/src/stringtest.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab5/q2/src
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab5/q2/src/stringtest.o code/labs/lab5/q2/src/stringtest.c

${OBJECTDIR}/code/labs/lab5/q3/q3.o: code/labs/lab5/q3/q3.c
	${MKDIR} -p ${OBJECTDIR}/code/labs/lab5/q3
	${RM} "$@.d"
	$(COMPILE.c) -g -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/code/labs/lab5/q3/q3.o code/labs/lab5/q3/q3.c

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
