# coding: utf-8

import os

'''
Created on April 23, 2018 00:54 AM

@author: Phelps
'''
for x in range(3000, 3002):
    os.system("python crawler.py -b Stock -i "+str(x)+" "+str(x)+ " &")
    #print("python crawler.py -b Stock -i ",x ,x)
    #print(x)