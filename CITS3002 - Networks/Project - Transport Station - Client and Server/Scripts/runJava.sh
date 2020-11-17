
# Timetable location
ttFolder="time6"
#ttFolder="time36"

# Clear terminal
if [ "$1" == "-c" ] ; then
    clear && tput reset
fi

# Heading
echo ""
echo "############### Networks Project ###############"

# Simplified message
echo ""
echo "Getting ready..."

# Go into folder
cd parts

# Remake script for starting stations
# echo ""
#echo ">>> Running 'assignports.sh' : Remaking startstations.sh using adjacency..."
./assignportsJava.sh $ttFolder/adjacency startstations.sh

# Modify aforementioned script to work with JAR files
#echo ""
#echo ">>> Making startstationsFixed.sh"
prefix="../../../../../../../../.././usr/lib/jvm/oracle_jdk11/jdk-11.0.7/bin/java -jar "
awk -v prefix="$prefix" '{print prefix $0}' startstations.sh > startstationsFixed.sh

# Update executable
#echo ""
#echo ">>> Updating station executable to latest..."

# Get the new JAR from the project directory
cp ../../JavaProject/Dist/JavaProject.jar station.jar


# Start station servers
#echo ""
#echo ">>> Starting stations..."
./startstationsFixed.sh


# Remake query page
#echo ""
#echo ">>> Running 'makeform.sh' : Remaking querypage.html using startstation.sh..."
./makeform.sh startstations.sh querypage.html


# End msg
echo "Done!"
#echo ""
#echo "Processes running:"
#ps

# Handle HTML page
echo ""
echo ">>> Opening generated HTML page..."
# If opening switch on
if [ "$1" == "-o" ] ; then
    explorer.exe querypage.html
else
    echo "DISABLED (use '-o' switch to enable)"
fi

echo ""
echo ">>> Press enter when testing done..." 
read -rsn1

echo ""
echo ">>> Testing successfully completed!" 


# Shutdown servers
./killJava.sh


echo ""
echo "Processes running:"
ps
echo ""

