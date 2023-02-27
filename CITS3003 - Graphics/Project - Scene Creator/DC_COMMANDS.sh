

### COMMANDS FOR CITS3003 PROJECT


# Generate build files (only done once at the start)
# If build is broken:
# 1) Delete 'cmake-build' folder
# 2) Re-generate build files by running this again
### cmake -S . -B cmake-build


# Build project (do when .cpp file has been edited)
cmake --build cmake-build

# Run project
./start_scene


# Remove assimp log
rm assimp_log.txt


### NOTES

# For dependencies, run GEN SETUP from Linux Files!

# If you get this error:
# >>> CMake Error at lib/glew/CMakeLists.txt:94 (add_library):
# >>>   Cannot find source file:
# >>>     /home/david/GitHub_Repos/UWA-CompSci
# >>>     /CITS3003 - Graphics/Project - Scene Creator/lib/glew/src/glew.c
# It means that project template files are missing.

# Template is from:
# https://github.com/Matthew-Chidlow/CITS3003_Project_Templat

### END
