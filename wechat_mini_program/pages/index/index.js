const app = getApp()
const request = require('../../utils/request.js')
const shuffle = require('./utils/shuffle.js')

Page({
  data: {
    headerTitleName: [
      { name: '国内', nameID: '1', newsType: 'domestic' },
      { name: '国际', nameID: '2', newsType: 'world' },
      { name: '评论', nameID: '3', newsType: 'expert' },
      { name: '双语', nameID: '4', newsType: 'doublelang' },
      { name: '文化', nameID: '5', newsType: 'culture' },
      { name: '古诗文', nameID: '6', newsType: 'gushiwen' },
      { name: '精读', nameID: '7', newsType: 'intensive' },
    ],
    swiperIndex: '1/7',
    topPic: [],
    tapID: 1, // 判断是否选中
    contentNewsList: [],
    showCopyright: false,
    refreshing: false
  },

  onLoad: function() {
    this.renderPage('domestic', false, () => {
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
    let newsTitle = e.currentTarget.dataset.newstitle || ''
    app.globalData.nowTitle = newsTitle
    app.globalData.nowTime = e.currentTarget.dataset.newstime || ''
    app.globalData.nowText = e.currentTarget.dataset.newstext || ''
    wx.navigateTo({
      url: '../detail/detail?newsTitle=' + newsTitle
    })
  },

  handleSwiperChange: function(e) {
    this.setData({
      swiperIndex: `${e.detail.current + 1}/4`
    })
  },

  onPulldownrefresh_SV() {
    this.renderPage('culture', true, () => {
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
      request({ url: `https://www.heartravel.cn/index.php?table=${newsType}`, newstype: newsType })
        .then(res => {
          wx.hideLoading()
          this.setData({
            contentNewsList: res
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
      let contentNewsListTemp = shuffle(this.data.contentNewsList)
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
