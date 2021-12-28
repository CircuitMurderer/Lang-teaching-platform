const mockData = require('../mocks/index.js')

const request = options => {
  const { url, method } = options
  const defaultOption = {
    method: 'GET'
  }
  const finalOption = { ...defaultOption, ...options }
  return new Promise((resolve) => {
    wx.request({
      url: finalOption.url,
      method: finalOption.method,
      data: {
        'table': 'world',
        'utime': '20211226'
      },
      success: res => {
        resolve(JSON.parse(JSON.stringify(mockData[finalOption.newstype])))  
        //resolve(res.data)
      }
    })
  })
}

module.exports = request
