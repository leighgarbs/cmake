# This is meant to be included as part of the definition of a CMake project.  It
# is not intended to be used by itself.

# We want to explicitly define some kind of build type.  CMake defines the debug
# and release build types.  If neither is defined by the user then define
# release by default.  CMake will build with additional appropriate compiler
# flags depending on which build type is set; we leave the choice of most
# meaningful debug and release compiler flags to CMake and do not explicitly
# specify them here.  Specification of compiler flags that are explicitly
# specified in this file (not specified by CMake) happens just below.

if(NOT CMAKE_BUILD_TYPE)
  set(CMAKE_BUILD_TYPE "Release")
endif(NOT CMAKE_BUILD_TYPE)

# Flags used for all builds
set(${PROJECT_NAME}_FLAGS "-Wall")

# Flags used for all builds for specific platforms
set(${PROJECT_NAME}_LINUX_FLAGS   "-DLINUX")
set(${PROJECT_NAME}_WINDOWS_FLAGS "-DWINDOWS")
set(${PROJECT_NAME}_MACOS_FLAGS   "-DMACOS")

# Flags used for specific build types
set(${PROJECT_NAME}_DEBUG_FLAGS   "-DDEBUG")
set(${PROJECT_NAME}_RELEASE_FLAGS "-DRELEASE")

# Generate the LINUX variable
if(UNIX AND NOT APPLE)
  set(LINUX 1)
else(UNIX AND NOT APPLE)
  set(LINUX 0)
endif(UNIX AND NOT APPLE)

# Add the common flags and user-defined debug flags to the existing set of debug
# flags
string(CONCAT CMAKE_CXX_FLAGS_DEBUG
  "${CMAKE_CXX_FLAGS_DEBUG} "
  "${${PROJECT_NAME}_FLAGS} "
  "${${PROJECT_NAME}_DEBUG_FLAGS}")

# Add the common flags and user-defined release flags to the existing set of
# release flags
string(CONCAT CMAKE_CXX_FLAGS_RELEASE
  "${CMAKE_CXX_FLAGS_RELEASE} "
  "${${PROJECT_NAME}_FLAGS} "
  "${${PROJECT_NAME}_RELEASE_FLAGS}")

if(LINUX)

  # Add Linux-specific debug flags
  string(CONCAT CMAKE_CXX_FLAGS_DEBUG
    "${CMAKE_CXX_FLAGS_DEBUG} "
    "${${PROJECT_NAME}_LINUX_FLAGS}")

  # Add Linux-specific release flags
  string(CONCAT CMAKE_CXX_FLAGS_RELEASE
    "${CMAKE_CXX_FLAGS_RELEASE} "
    "${${PROJECT_NAME}_LINUX_FLAGS}")

elseif(WIN32)

  # Add Windows-specific debug flags
  string(CONCAT CMAKE_CXX_FLAGS_DEBUG
    "${CMAKE_CXX_FLAGS_DEBUG} "
    "${${PROJECT_NAME}_WINDOWS_FLAGS}")

  # Add Windows-specific release flags
  string(CONCAT CMAKE_CXX_FLAGS_RELEASE
    "${CMAKE_CXX_FLAGS_RELEASE} "
    "${${PROJECT_NAME}_WINDOWS_FLAGS}")

elseif(APPLE)

  # Add macOS-specific debug flags
  string(CONCAT CMAKE_CXX_FLAGS_DEBUG
    "${CMAKE_CXX_FLAGS_DEBUG} "
    "${${PROJECT_NAME}_MACOS_FLAGS}")

  # Add macOS-specific release flags
  string(CONCAT CMAKE_CXX_FLAGS_RELEASE
    "${CMAKE_CXX_FLAGS_RELEASE} "
    "${${PROJECT_NAME}_MACOS_FLAGS}")

endif(LINUX)
