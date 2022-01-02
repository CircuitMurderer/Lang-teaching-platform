const request = options => {
  const { url, method } = options
  const defaultOption = {
    method: 'GET'
  }
  const finalOption = { ...defaultOption, ...options }
  return new Promise((resolve, reject) => {
    wx.request({
      url: finalOption.url,
      method: finalOption.method,
      success: res => {
        resolve(res.data)
      },
      error: function(e) {
        reject({ reason: '网络错误' })
      }
    })
  })
}

module.exports = request
