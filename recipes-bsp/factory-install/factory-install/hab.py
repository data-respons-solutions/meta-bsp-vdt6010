#!/usr/bin/env python3

import os, argparse

def prog_fuses():
    srk_key = [ 
        "0xd3845694",
        "0xdb234b98",
        "0xf3c22abd",
        "0x6b063ffa",
        "0x30a506ca",
        "0x9205c3af",
        "0x8a7ff149",
        "0x52cad15"
        ]

    n=0;
    for k in srk_key:
        with open(os.path.join(base, "HW_OCOTP_SRK"+str(n)), "w") as f:
            f.write(k+"\n")
            n = n+1
            
    n=0 
    for k in srk_key:
        with open(os.path.join(base, "HW_OCOTP_SRK"+str(n)), "r") as f:
            rb_key = f.read().rstrip("\n")
            if (rb_key != k):
                raise ValueError
            n = n+1
            
    

parser = argparse.ArgumentParser()
parser.add_argument("--commit", help="Engage HAB", action="store_true")
args = parser.parse_args()

base = "/sys/fsl_otp"
#base = "/home/hcl/fsl_otp"

try:   
    with open(os.path.join(base, "HW_OCOTP_LOCK"), "r") as f:
        fuseval = int(f.read().rstrip("\n"), base=16)
    if (not (fuseval & 0x00004000)):
        prog_fuses()
        with open(os.path.join(base, "HW_OCOTP_LOCK"), "w") as f:
            f.write("0x00040000\n")       
    else:
        print("SRK already fused")
        
    if (args.commit):   # Set the DIR_BT_DIS and SEC_CONFIG bits.
        with open(os.path.join(base, "HW_OCOTP_CFG5"), "w") as f:
            f.write("0xA\n")

except OSError as e:
    print("File op error", e)
    exit(1)
except ValueError as v:
    print("Fuse array mismatch", v)
    exit(1) 
exit(0)
