#!/bin/sh

# Always run from the client directory
cd "$(dirname "$0")" || exit 1

# Usage: ./run.sh [server-url]
mkdir -p out && javac -d out src/com/racer/game/*.java && java -cp out com.racer.game.GameFrame "$@"