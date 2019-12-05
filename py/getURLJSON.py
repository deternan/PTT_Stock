# coding: utf-8

import requests
import urllib, json

r = requests.get("https://www.twse.com.tw/exchangeReport/MI_INDEX?response=json&date=20191204")

html_str = r.text

# response state
print(r.status_code)

#print(type(r))
#print(type(html_str))

# json_url = urlopen(url)
# data = json.loads(json_url.read())
# print(data)


jsonStr = r.json()
#print(r.json())


