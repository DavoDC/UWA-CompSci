
# Go to original volumes and exit if could not
cd testing/vols/original || (echo "Reset failed" && exit)

# Message
echo " Reset volume copies to original state"

# Iterate over all volumes
# Copy each up and rename
for f in *
do
    cp "$f" ../copies/${f}cp 
done


