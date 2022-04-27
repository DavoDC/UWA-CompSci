
# Dependencies
# sudo apt install cmake libxmu-dev g++ libx11-dev libgl1-mesa-dev libglu1-mesa-dev xorg-dev

# Clean before (only if needed)
# cmake --build cmake-build --target clean


# If build is broken:
# 1) Delete 'cmake-build' folder
# 2) Re-generate build files


# Generate build files (only done once at the start)
# cmake -S . -B cmake-build


# Build project (do when .cpp file has been edited)
cmake --build cmake-build

# Run project
./start_scene

