
# Reset Volumes
./testing/vols/reset.sh

# Make volume tests
./testing/sifs_test mkvol 1 1024 5
./testing/sifs_test mkvol 2 4096 10

# Dirinfo tests
./testing/sifs_test dinfo A /
./testing/sifs_test dinfo A /dog/cat
./testing/sifs_test dinfo B sifs.h
./testing/sifs_test dinfo B /dog/cat
./testing/sifs_test dinfo C /
./testing/sifs_test dinfo C /sifs.h-copy
./testing/sifs_test dinfo C /dog/cat
./testing/sifs_test dinfo D /
./testing/sifs_test dinfo D ////
./testing/sifs_test dinfo D //subdir1/
./testing/sifs_test dinfo D ////subdir2/
./testing/sifs_test dinfo D /subdir1/dog
./testing/sifs_test dinfo D ////besttq-sample.c
./testing/sifs_test dinfo D /dog/cat

# File info tests
./testing/sifs_test finfo A Niswag.txt
./testing/sifs_test finfo B sifs.h
./testing/sifs_test finfo B sifs_mkvolume.c
./testing/sifs_test finfo B Niswag.txt
./testing/sifs_test finfo C sifs.h
./testing/sifs_test finfo C sifs.h-copy
./testing/sifs_test finfo C sifs_mkvolume.c
./testing/sifs_test finfo C Niswag.txt
./testing/sifs_test finfo D sifs.h
./testing/sifs_test finfo D sifs_mkvolume.c
./testing/sifs_test finfo D besttq-sample.c
./testing/sifs_test finfo D Niswag.txt
./testing/sifs_test finfo D subdir1
./testing/sifs_test finfo D subdir2
./testing/sifs_test finfo D /

# Make directory tests
./testing/sifs_test mkdir D fruits
./testing/sifs_test mkdir D veg
./testing/sifs_test mkdir D fruits/apple
./testing/sifs_test mkdir D fruits/apple/granny
./testing/sifs_test mkdir D fruits/apple/gala
./testing/sifs_test mkdir D fruits/pear
./testing/sifs_test mkdir E fruits/lemon
./testing/sifs_test mkdir E veg/carrot
./testing/sifs_test mkdir E veg/celery
./testing/sifs_test mkdir A happy
./testing/sifs_test mkdir A happy/cool
./testing/sifs_test mkdir A cool
./testing/sifs_test mkdir A cool/happy
./testing/sifs_test mkdir B sifs.h
./testing/sifs_test mkdir B /
./testing/sifs_test mkdir D dog
./testing/sifs_test mkdir D cat
./testing/sifs_test mkdir D cat/cat
./testing/sifs_test mkdir D frog
./testing/sifs_test mkdir D frog/dog
./testing/sifs_test mkdir D frog/cat
./testing/sifs_test mkdir D frog/dog/new
./testing/sifs_test mkdir D frog/dog/new/dog
./testing/sifs_test mkdir D frog/dog/new/cat
./testing/sifs_test mkdir D frog/dog/new/cat/frog
./testing/sifs_test mkdir D small.txt
./testing/sifs_test mkdir D small.txt/test
./testing/sifs_test mkdir A
./testing/sifs_test mkdir D Games
./testing/sifs_test mkdir D Games/Fortnite/
./testing/sifs_test mkdir D Games/Fortnite/Vbux
./testing/sifs_test mkdir D Games/Fortnite/Skins
./testing/sifs_test mkdir D Games/Fortnite/Guns
./testing/sifs_test mkdir D Games/FIFA20/
./testing/sifs_test mkdir D Games/Skyrim
./testing/sifs_test mkdir D subdir1/subdir2/
./testing/sifs_test mkdir D subdir1/subsubdir1/
./testing/sifs_test mkdir D subdir3
./testing/sifs_test mkdir D subdir3/subsubdir3/
./testing/sifs_test mkdir E c/cat/
./testing/sifs_test mkdir E b/bison/bumpy/dumpy/

# Write file tests
./testing/sifs_test wrfil D / zero.txt
./testing/sifs_test wrfil D / zeroCpy.txt
./testing/sifs_test wrfil D / small.txt
./testing/sifs_test wrfil D small.txt small.txt
./testing/sifs_test wrfil D / smallCpy.txt
./testing/sifs_test wrfil D cat small.txt
./testing/sifs_test wrfil D subdir1 med.txt
./testing/sifs_test wrfil D subdir1 med.txt
./testing/sifs_test wrfil D Games/Skyrim tomH.jpg
./testing/sifs_test wrfil D subdir1 medCpy.txt


# Read file tests
./testing/sifs_test refil D cat/cat/small.txt   

 Remove directory tests
./testing/sifs_test rmdir D subdir1
./testing/sifs_test rmdir D subdir2
./testing/sifs_test rmdir D sifs.h
./testing/sifs_test rmdir D cat/cat
./testing/sifs_test rmdir D frog/dog/new/cat

# Remove file tests
./testing/sifs_test rmfil D cat/cat/small.txt
./testing/sifs_test rmfil D besttq-sample.c
./testing/sifs_test rmfil D sifs.h

# Defrag test
./testing/sifs_test dfrag A
./testing/sifs_test dfrag B
./testing/sifs_test dfrag C
./testing/sifs_test dfrag D
./testing/sifs_test dfrag E
./testing/sifs_test dfrag F
./testing/sifs_test dfrag X








