import requests
import json
import datetime
import random
today = datetime.date.today()
today = today.strftime('%y%m%d')
print(today)

data = []
table = ['culture','domestic','world','gushiwen','expert','doublelang']
for i in table:
    response = requests.get("https://www.heartravel.cn/index.php?table={}&utime=20{}".format(i, today))
    jsondata = response.text
    jsondata = json.loads(jsondata)
    x = random.randint(0,len(jsondata))
    data.append(jsondata[x])

# print(data)
for d in data:
    title = d['title']
    text = d['text']
    print(title)

