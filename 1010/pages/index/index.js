const app = getApp()

const request = require('../../utils/request.js')
const extractArticleInfo = require('./utils/getArticleTime.js')
const shuffle = require('./utils/shuffle.js')

Page({
  data: {
    headerTitleName: [
      { name: '精读', nameID: '201701', newsType: 'top' },
      { name: '国内', nameID: '201702', newsType: 'culture' },
      { name: '国际', nameID: '201703', newsType: 'world' },
      { name: '周边', nameID: '201704', newsType: 'culture' },
      { name: '文化', nameID: '201705', newsType: 'world' },
      { name: '历史', nameID: '201706', newsType: 'world' },
      { name: '诗词', nameID: '201707', newsType: 'world' }
    ],
    swiperIndex: '1/4',
    topPic: [],
    tapID: 201701, // 判断是否选中
    contentNewsList: [],
    showCopyright: false,
    refreshing: false,
    news: {}
  },

  onLoad: function() {
   this.renderPage('top', false, () => {
      this.setData({
        showCopyright: true
      })
    });
  },

  // headerBar 点击
  headerTitleClick: function(e) {
    this.setData({ tapID: e.target.dataset.id })
    this.renderPage(e.currentTarget.dataset.newstype, false)
  },

  //跳转到新闻详情页
  viewDetail: function(e) {
    let newsUrl = e.currentTarget.dataset.newsurl || ''
    let newsTitle = e.currentTarget.dataset.newstitle || ''
    let newsAuthor = e.currentTarget.dataset.newsauthor || ''
    wx.navigateTo({
      url: '../detail/detail?newsUrl=' + newsUrl
    })
  },

  handleSwiperChange: function(e) {
    this.setData({
      swiperIndex: `${e.detail.current + 1}/4`
    })
  },

  onPulldownrefresh_SV() {
    this.renderPage('top', true, () => {
      this.setData({
        refreshing: false
      })
    })
  },
  // isRefresh 是否为下拉刷新
  renderPage: function(newsType, isRefresh, calllBack) {
    if (!isRefresh) {
      wx.showLoading({
        title: '加载ing'
      })
      request({ url: 'http://www.heartravel.cn', newstype: newsType })
        .then(res => {
          wx.hideLoading()
          this.setData({
            contentNewsList: res.data,
          })
          if (calllBack) {
            calllBack()
          }
        })
        .catch(error => {
          wx.hideLoading()
        })
    } else {
      // 数组随机排序，模拟刷新
      let contentNewsListTemp = shuffle(JSON.parse(JSON.stringify(this.data.contentNewsList)))
      /* contentNewsListTemp.sort(() => {
        return Math.random() > 0.5 ? -1 : 1
      }) */
      setTimeout(() => {
        this.setData({
          contentNewsList: contentNewsListTemp
        })
        if (calllBack) {
          calllBack()
        }
      }, 2000)
    }
  }
})
