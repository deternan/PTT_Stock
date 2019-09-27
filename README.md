
## Functions and Tools
1) 擷取 PTT 文章 <br>
2) PTT 文章人工標記 GUI <br>
3) 相關工具 <br>
4) Data Source and DataSet <br>
5) Statistical <br>


### 擷取 PTT 文章 

> py/get_pttdata.py


### PTT 文章人工標記 GUI

<frame image> <br>

[https://github.com/deternan/PTT_Stock/tree/master/src/main/java/GUI](https://github.com/deternan/PTT_Stock/tree/master/src/main/java/GUI)


### 相關工具

擷取上市公司名稱 & ID

> src/main/java/ptt/get_TWSECompany_list.java

擷取上櫃公司名稱 & ID

> src/main/java/ptt/get_OTCcompany_list.java


### Data Source and DataSet
[本國上市證券國際證券辨識號碼一覽表](http://isin.twse.com.tw/isin/C_public.jsp?strMode=2) <br>

[本國上櫃證券國際證券辨識號碼一覽表](http://isin.twse.com.tw/isin/C_public.jsp?strMode=4) <br>

[2018/01/01 - 2019/06/30 每日收盤數值](https://github.com/deternan/PTT_Stock/tree/master/output/Values) <br> 


### Statistics


1) [發表過文章的作者 ID](https://github.com/deternan/PTT_Stock/blob/master/src/main/java/ptt/statistics/Statistical_AuthorsList.java) <br>
2) [文章被推文的數量](https://github.com/deternan/PTT_Stock/blob/master/src/main/java/ptt/statistics/Statistical_articlePush.java) <br>
3) [每個user發表後被推文的數量](https://github.com/deternan/PTT_Stock/blob/master/src/main/java/ptt/statistics/Statistical_NumofPushByAllAuthor.java) <br>
4) [每個user推文的數量](https://github.com/deternan/PTT_Stock/blob/master/src/main/java/ptt/statistics/Statistical_AuthorsPushedNumber.java) <br>

---
### Competition 
[第 11 屆 iT 邦幫忙鐵人賽](https://ithelp.ithome.com.tw/users/20119726/ironman/2138)

---

### Reference

PTT 網路版爬蟲
[ptt-web-crawler](https://github.com/jwlin/ptt-web-crawler#english_desc)

台股數值 爬蟲
https://github.com/Asoul/tsrtc <br>
https://github.com/Asoul/tsec <br>
https://medium.com/renee0918/python爬蟲-每日即時股價 <br>
https://medium.com/renee0918/python-爬取個股歷年股價資訊 <br>