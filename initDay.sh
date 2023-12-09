#!/bin/bash

# Check if number of arguments is exactly 1
if [ $# -ne 1 ]; then
    echo "pass only day number as argument"
    exit 1
fi

# Load AOC_COOKIE
source .env

# Check if AOC_COOKIE is set
if [ -z "$AOC_COOKIE" ]; then
    echo "AOC_COOKIE is not set"
    exit 1
fi

# Define folder and filenames
day=$((10#$1)) # number of day without leading zeroes
prevDay=$((day - 1))
oldName=$(printf "%02d" "${prevDay}")
newName=$(printf "%02d" "${day}")
ktFolder="src/solutions23/"
inputFolder="src/solutions23/input"


# Make new file if it doesn't exist.
# param $1 - content
# param $2 - filename
function mkFile() {
    if [ -f "${2}" ]; then
      echo "File already exists: ${2}"
    else
      echo "${1}" > "${2}"
      git add "${2}"
      echo "Generated: ${2}"
    fi
}

# Copy kotlin file from previous day
# use mkFile with first argument the content of the old file and
# the second argument the new file name.
mkFile "$(cat "$ktFolder/Day$oldName.kt")" "$ktFolder/Day$newName.kt"

# Substitute occurences of oldName with newName, to update references to old input files
sed -i "s/$oldName/$newName/g" "$ktFolder/Day$newName.kt"

# Create new empty file for test input
mkFile "" "$inputFolder/Day${newName}_test.txt"


# Download puzzle input file
file="$inputFolder/Day$newName.txt"
url="https://adventofcode.com/2023/day/$day/input"
if [ -f "${file}" ]; then
  echo "File already exists: ${file}"
else
  if wget --header="Cookie: session=${AOC_COOKIE}" -O "${file}" "${url}"; then
    git add "${file}"
    echo "Downloaded: ${file}"
    exit 0
  else
    echo "Download failed"
    exit 1
  fi
fi

