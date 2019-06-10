#!/usr/bin/env python3

import sys, subprocess

from subprocess import CalledProcessError

def to_mb(val):
  return val/(1024*1024)

def from_mb(val):
  return val*1024*1024

def partition(drive, drive_size):
  pl = list()
  psize = (int(drive_size)-1)/len(part_names)
  offset = 1
  n=1
  try:
    subprocess.check_call(["parted", "-s", drive, "mklabel msdos" ])
    for p in part_names:
      print (str.format("Partition {0} ( {1} )  ext4, size {2}MB ", n, p, psize))
      argstr = str.format("mkpart primary ext4 {0} {1}", offset, offset+psize)
      subprocess.check_call(["parted", "-s", drive, argstr ], universal_newlines=True)
      part_dev = drive + str.format("p{0}", n)
      
      print (str.format("Format {0} ( {1} )  ext4, size {2}MB ", part_dev, p, psize))
      subprocess.check_call(["mkfs.ext4", part_dev, "-q", "-F", "-L", p])
      pl.append(part_dev)
      offset = offset + psize
      n = n+1
  except CalledProcessError as e:
    print (e)
  return pl
  

def set_led(led, value, trigger=''):
  led_path = "/sys/class/leds/" + led + "/brightness"
  try:
    f = open(led_path, 'w')
  except IOError as e:
    print ("LED", led, "=", value)
    return
    
  f.write(value)
  if (len(trigger) > 0):
    led_path = "/sys/class/leds/" + led + "/trigger"
    with open(led_path, 'w') as f:
      f.write(trigger)
      
def led_ok():
  set_led('status-red', '0' )
  set_led('status-green', '1')
  
def led_err():
  set_led('status-red', '1')
  set_led('status-green', '0')
  
def led_work():
  set_led('status-red', '0')
  set_led('status-green', '1', trigger='heartbeat')
  
""" Program starts here """

block_dev = "/dev/mmcblk0"
block_dev2 = "/dev/mmcblk0gp0"
part_names = ["factoryvpd", "uservpd"]  
vpd_is_parted = False

vpd_size = 128
  
led_work()

try:
  inf = subprocess.check_output(["lsblk", "-l", "-n", block_dev2], universal_newlines=True)
  l = str(inf).splitlines(keepends=False)
  for s in l:
    ss = s.split()
    if (ss[0] == "mmcblk0gp0"):
      disk_size = ss[3].rstrip("M");
      print ("Found drive", ss[0], "with size", disk_size, "MB")
    if (ss[0] == "mmcblk0gp0p2"):
      vpd_is_parted = True

  
except CalledProcessError as e:
  print ("No GP partition on emmc - create it")
  try:
    gparg = str.format("{0}", vpd_size*1024)
    inf = subprocess.check_output(["mmc", "gp", "create", "-y", gparg, "1", "1", "0", block_dev])
    print ("System must be power cycled to accept changes")
    subprocess.check_call(["poweroff", "-f"])
  except CalledProcessError as f:
    print ("Unable to create GP partition oe emmc - exit")
    exit (1)

try:
  if (not vpd_is_parted):
    print ("VPD drive is not partitioned, generate them")
    used_parts = partition(block_dev2, disk_size)
  else:
    print ("VPD drive is ready")
except CalledProcessError as e:
  print (e)
  exit (1)

exit(0)
