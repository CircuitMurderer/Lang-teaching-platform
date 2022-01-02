// pages/detail/detail.js
const app = getApp()

Page({
  /**
   * 页面的初始数据
   */
  data: {
    newsTitle: '',
    newsTime: '',
    newsText: [],
    contentTip: '由于后台接口原因，新闻具体内容无法编辑，只返回了一个新闻链接...'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let { newsTitle } = options
    let newsTime = app.globalData.nowTime
    let newsText = app.globalData.nowText.split("\n")

    this.setData({
      newsTitle: newsTitle,
      newsTime: newsTime,
      newsText: newsText
    })
  }
})
