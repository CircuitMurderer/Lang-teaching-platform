const app = getApp()
const appKey = 'fc35d7872c25744ab4669c7d9dbcf15e' // 用于访问新闻接口的appKey
const request = require('../../utils/request.js')
const extractArticleInfo = require('./utils/getArticleTime.js')
const shuffle = require('./utils/shuffle.js')

Page({
  data: {
    headerTitleName: [
      { name: '精读', nameID: '201701', newsType: 'top' },
      { name: '历史', nameID: '201702', newsType: 'junshi' },
      { name: '诗词', nameID: '201703', newsType: 'tiyu' },
      { name: '书法', nameID: '201704', newsType: 'keji' },
    ],
    swiperIndex: '1/4',
    topPic: [],
    tapID: 201701, // 判断是否选中
    contentNewsList: [],
    showCopyright: false,
    refreshing: false
  },

  onLoad: function() {
    this.renderPage('top', false, () => {
      this.setData({
        showCopyright: true
      })
    })
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
        title: '加载中'
      })
      request({ url: `https://v.juhe.cn/toutiao/index?type=${newsType}&key=${appKey}`, newstype: newsType })
        .then(res => {
          wx.hideLoading()
          let { articleList, topPic } = extractArticleInfo(res.result.data)
          this.setData({
            contentNewsList: articleList,
            topPic
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