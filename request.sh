#!/usr/bin/env bash

set -e          # exit if any command fails
set -u          # prevent using an undefined variable
set -o pipefail # force pipelines to fail on the first non-zero status

PORT=8080

SKU=$1

curl "localhost:$PORT/$SKU"
