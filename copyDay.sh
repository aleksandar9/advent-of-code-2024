#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <XX>"
  exit 1
fi

NEW_DAY="day$1"
NEW_FILE="Day$1"
OLD_DAY="day00"
OLD_FILE="Day00"
SRC_DIR="src"

# Copy the directory
cp -r "$SRC_DIR/$OLD_DAY" "$SRC_DIR/$NEW_DAY"

# Rename files inside the new directory
for file in "$SRC_DIR/$NEW_DAY"/*; do
  new_file=$(echo "$file" | sed "s/$OLD_FILE/$NEW_FILE/")
  mv "$file" "$new_file"
done

# Update references inside the files
for file in "$SRC_DIR/$NEW_DAY"/*; do
  sed -i '' "s/$OLD_DAY/$NEW_DAY/g" "$file"
  sed -i '' "s/$OLD_FILE/$NEW_FILE/g" "$file"
done

echo "Package $OLD_DAY and files renamed to $NEW_DAY and $NEW_FILE successfully."
