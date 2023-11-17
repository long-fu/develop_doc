#!/bin/bash
sudo apt-get --purge remove "*cuda*" "*cublas*" "*cufft*" "*cufile*" "*curand*" \
 "*cusolver*" "*cusparse*" "*gds-tools*" "*npp*" "*nvjpeg*" "nsight*" "*nvvm*"

sudo apt-get --purge remove "*nvidia*" "libxnvctrl*"

sudo apt-get autoremove