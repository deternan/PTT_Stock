import requests
from io import StringIO
import pandas as pd
import numpy as np

datestr = '20190621'

r = requests.post('http://www.twse.com.tw/exchangeReport/BFT41U?response=csv&date=' + datestr + '&type=ALL')
#r = requests.post('http://www.twse.com.tw/exchangeReport/MI_INDEX?response=csv&date=' + datestr + '&type=ALL')

df = pd.read_csv(StringIO("\n".join([i.translate({ord(c): None for c in ' '}) 
                                     for i in r.text.split('\n') 
                                     if len(i.split('",')) == 17 and i[0] != '='])), header=0)
