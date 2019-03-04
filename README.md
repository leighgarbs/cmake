**[tools-cmake](https://github.com/leighgarbs/tools-cmake)** -
  Generalized cross-project CMake tools
==========================================================

For personal project use.  Defines a common build environment for other C++
projects.

## Features ##
* C++ code builds to C++11 standard
* MACOS, LINUX, WIN32 platform build-time constants
* Defines "Release" build type when no type is explicitly specified
* All compiler warnings enabled
* Enables CTest-based testing
  * Defines "tests" target which should have all tests as dependencies
  * Defines "add_test_executable" function for adding tests to the framework

## Style ##
* Emacs CMake major mode
