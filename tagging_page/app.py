import datetime
import json
import random

import requests
from flask import Flask, render_template

today = datetime.date.today()
today = today.strftime('%y%m%d')

app = Flask(__name__)


def get_data(which_table, when_day):
    tar_url = "https://www.heartravel.cn/index.php?table={}&utime=20{}"
    res = requests.get(tar_url.format(which_table, when_day))

    return json.loads(res.text)


data = []
table = ['culture', 'domestic', 'world', 'gushiwen', 'expert', 'doublelang']
for i in table:
    news = get_data(i, today)
    while len(news) == 0:
        today = str(int(today) - 1)
        if today == '20211226':
            raise RuntimeError('No data in database.')
        news = get_data(i, today)

    # x = random.randint(0, len(news))
    print(len(news))
    dic = news[0]
    dic['text'] = dic['text'].split('\n')
    dic['max_len'] = max([len(t) for t in dic['text']])
    data.append(dic)


@app.route('/')
def index():
    return render_template('index.html', news=data)


@app.route('/tag/<which_news>')
def tag(which_news):
    return render_template('tag.html', news=data[int(which_news)])
