#! /bin/bash
set -x

pwd

pacman -Syu --noconfirm sudo git

useradd -m build -G wheel
chown -R build /github/workspace /github/home
echo "build ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

su --login build --command="\
 id &&\
 export MAKEFLAGS=-j$(nproc) &&\
 cd $PWD &&\
 git clone https://aur.archlinux.org/directx-headers.git --depth 1 &&\
 cd directx-headers &&\
 makepkg -i --syncdeps --noconfirm &&\
 cd .. &&\
 git clone https://aur.archlinux.org/mesa-d3d12.git --depth 1 &&\
 cd mesa-d3d12 &&\
 makepkg --syncdeps --noconfirm --skippgpcheck\
"