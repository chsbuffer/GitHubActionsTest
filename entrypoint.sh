#! /bin/bash
set -x
pwd
id

pacman --sync --refresh --sysupgrade --noconfirm sudo git

useradd --create-home build --groups wheel
chown --recursive build /github/workspace /github/home
# makepkg --syncdeps calls sudo 
echo "build ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

su --login build --command="\
 id ;\
 export MAKEFLAGS=-j$(nproc) &&\
 git clone --branch packages/mesa --depth 1 https://github.com/archlinux/svntogit-packages.git mesa &&\
 cd mesa/trunk &&\
 makepkg --syncdeps --noconfirm --skipinteg &&\
 cd .. ;\
"