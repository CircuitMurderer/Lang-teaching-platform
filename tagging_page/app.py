import json
import requests
import datetime
from flask import Flask, render_template, request

app = Flask(__name__)

today = datetime.date.today()
today = '20' + today.strftime('%y%m%d')


def get_data(which_table):
    tar_url = "https://www.heartravel.cn/index.php?table={}"
    res = requests.get(tar_url.format(which_table))

    return json.loads(res.text)


data = []
table = ['culture', 'domestic', 'world', 'gushiwen', 'expert', 'doublelang']
for i in table:
    news = get_data(i)
    for n in news[:5]:
        dic = n
        dic['text'] = dic['text'].split('\n')
        dic['type'] = i
        data.append(dic)


@app.route('/')
def index():
    return render_template('index.html', news=data)


@app.route('/save', methods=['POST'])
def save():
    title = request.form['main-title']
    time = request.form['main-time']
    text = request.form['main-text']

    post_data = {
        'query': "INSERT INTO intensive VALUES "
                 "('{}', '{}', '{}', '{}')".format(title, time, text, today),
        'title': title
    }

    res = requests.post('https://www.heartravel.cn/index.php', data=post_data)
    return res.text


@app.route('/tag/<which_news>')
def tag(which_news):
    return render_template('tag.html', news=data[int(which_news)])
