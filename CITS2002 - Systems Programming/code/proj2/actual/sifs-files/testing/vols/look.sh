# Go to volumes
cd sifs-files/testing/vols

# message
echo ""
echo "Looking at volumes..."

# Iterate over all volumes
# Octal dump each as integers
for f in ../vols/*.vol
do
    echo "Volume Name: $f"
    od -i $f
    echo ""
done


