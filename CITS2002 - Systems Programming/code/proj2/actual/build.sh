# Compile testing program, run if successfully compiled
cc -o start/start start/start.c && ./start/start

# Go deeper
cd sifs-files

# Make library + Make testing program + Run if made successfully
echo "Making..."
make && echo "" && echo "Testing ..." && ./testing/runtest.sh
echo ""

# Clean out object files of library
echo "Cleaning..."
cd library
rm -f *.o
echo ""
