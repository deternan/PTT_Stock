# coding: utf-8

import os
import threading
import time

'''
Created on May 01, 2019 08:51 PM

@author: Phelps
'''

def workerthread(num):    
    os.system("python crawler.py -b Stock -i "+str(num)+" "+str(num)+ " &")
    print(num)
    return

threads = []
for x in range(4838, 5100):
    t = threading.Thread(target=workerthread, args=(x,))
    threads.append(t)
    t.start()
    'delay'
    time.sleep(5)

