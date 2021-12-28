from flask import Flask, render_template
import requests
import json
import random
import datetime
today = datetime.date.today()
today = today.strftime('%y%m%d')

app = Flask(__name__)

data = []
table = ['culture','domestic','world','gushiwen','expert','doublelang']
for i in table:
    response = requests.get("https://www.heartravel.cn/index.php?table={}&utime=20{}".format(i, today))
    jsondata = response.text
    jsondata = json.loads(jsondata)
    x = random.randint(0,len(jsondata))
    data.append(jsondata[x])


@app.route('/')
def index():
    return render_template('index.html', movies=data)
