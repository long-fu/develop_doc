

https://docs.nvidia.com/metropolis/deepstream/6.1/dev-guide/text/DS_Quickstart.html#remove-all-previous-deepstream-installations

6.1 这里的版本号可以更具deepstream的版本进行调整然后查看相应文档



sudo apt install \
libssl1.0.0 \
libgstreamer1.0-0 \
gstreamer1.0-tools \
gstreamer1.0-plugins-good \
gstreamer1.0-plugins-bad \
gstreamer1.0-plugins-ugly \
gstreamer1.0-libav \
libgstrtspserver-1.0-0 \
libjansson4 \
libyaml-cpp-dev \
gcc \
make \
git \
python3


wget https://developer.download.nvidia.com/compute/cuda/repos/ubuntu2004/x86_64/cuda-ubuntu2004.pin
sudo mv cuda-ubuntu2204.pin /etc/apt/preferences.d/cuda-repository-pin-60
wget https://developer.download.nvidia.com/compute/cuda/11.6.1/local_installers/cuda-repo-ubuntu2204-11-6-local_11.6.1-510.47.03-1_amd64.deb
sudo dpkg -i cuda-repo-ubuntu2004-11-6-local_11.6.1-510.47.03-1_amd64.deb
sudo apt-key add /var/cuda-repo-ubuntu2004-11-6-local/7fa2af80.pub
sudo apt-get update
sudo apt-get -y install cuda
    
    
    
sudo nano ~/.bashrc
    
#paste these 2 lines at the end of bashrc file
export LD_LIBRARY_PATH="/usr/local/cuda/lib64:/usr/local/cuda-11.6/extras/CUPTI/lib64:$LD_LIBRARY_PATH"
export PATH="/usr/local/cuda-11.6/bin:$PATH"
source ~/.bashrc

http://localhost:8888/lab?token=a4ec03b2a80e3bec288e8169e8c99356383eccbc03fb36f4

dWl1MmpwNzZ1NjBwN3I4M2w4ZXFvdmlmMWk6N2I0ODIxYWEtMjg2ZC00YjcyLTk2YmEtZDVkZmYxOTBmYjNh

Run the container
To run the container:

Allow external applications to connect to the host's X display:
xhost +
Run the docker container (use the desired container tag in the command line below):
If using docker (recommended):
docker run --gpus all -it --rm --net=host --privileged -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.1 nvcr.io/nvidia/deepstream:6.1.1-devel
If using nvidia-docker (deprecated) based on a version of docker prior to 19.03:

nvidia-docker run -it --rm -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.1  nvcr.io/nvidia/deepstream:6.1.1-devel
Note that the command mounts the host's X11 display in the guest filesystem to render output videos.

Additional Installations to use all DeepStreamSDK Features within the docker container.
With DS 6.1.1, DeepStream docker containers do not package libraries necessary for certain multimedia operations like audio data parsing, CPU decode, and CPU encode. This change could affect processing certain video streams/files like mp4 that include audio tracks.

Please run the below script inside the docker images to install additional packages that might be necessary to use all of the DeepStreamSDK features :

/opt/nvidia/deepstream/deepstream/user_additional_install.sh
