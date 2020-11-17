
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
#echo "Getting ready..."

# Go into folder
cd parts

# Remake script for starting stations
# echo ""
#echo ">>> Running 'assignports.sh' : Remaking startstations.sh using adjacency..."
./assignportsCPP.sh $ttFolder/adjacency startstations.sh

# Update executable
#echo ""
#echo ">>> Updating station executable to latest..."


# Get the CPP executable from the project directory 
cp ../../C++project/dist/debug/cygwin-windows/station.exe station


# Start station servers
#echo ""
#echo ">>> Starting stations..."
./startstations.sh

# Remake query page
#echo ""
#echo ">>> Running 'makeform.sh' : Remaking querypage.html using startstation.sh..."
./makeform.sh startstations.sh querypage.html


# End msg
##echo "Done!"
#echo ""
#echo "Processes running:"
#ps

# Handle HTML page
#echo ""
#echo ">>> Opening generated HTML page..."
# If opening switch on
if [ "$1" == "-o" ] ; then
    explorer.exe querypage.html
fi

echo ""
echo ">>> Press enter when testing done..." 
read -rsn1

echo ""
echo ">>> Testing successfully completed!" 


# Shutdown servers
pkill station

echo ""
echo "Processes running:"
ps
echo ""

