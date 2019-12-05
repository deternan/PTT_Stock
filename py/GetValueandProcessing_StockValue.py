# coding: utf-8

'''
parsing stock data
created: December 05, 2019 10:00 AM
Last revision: December 05, 2019 10:40 AM
  
Author : Chao-Hsuan Ke
'''

import requests
import urllib, json

r = requests.get("https://www.twse.com.tw/exchangeReport/MI_INDEX?response=json&date=20191204&type=IND")

html_str = r.text

# response state
print(r.status_code)

# parsing
jsonStr = r.json()
#print(r.json())
#print(jsonStr['data3'])

data1Array = jsonStr['data1']

print(len(data1Array))

for item in data1Array:
    if('發行量加權股價指數' in item):
        value = item[1]
        print(item)
        print(value)

         


