# CMake 2.6 is the oldest version of CMake that still has documentation
# available online
cmake_minimum_required(VERSION 2.6)

# Get the name of the directory this file is in; this happens over the next two
# lines
get_filename_component(PROJECT_DIRECTORY ${CMAKE_CURRENT_LIST_DIR} DIRECTORY)
get_filename_component(PROJECT_DIRECTORY ${PROJECT_DIRECTORY} NAME)

# Name this project after the name of the directory containing it
project(${PROJECT_DIRECTORY})

# Add all subdirectories to this project as subprojects, except for the
# CMakeFiles directory we autogenerate
file(GLOB FILES RELATIVE ${CMAKE_CURRENT_LIST_DIR} *)
foreach(FILE ${FILES})
if (IS_DIRECTORY ${FILE} AND NOT ${FILE} STREQUAL CMakeFiles)
add_subdirectory(${FILE})
endif(IS_DIRECTORY ${FILE} AND NOT ${FILE} STREQUAL CMakeFiles)
endforeach()
