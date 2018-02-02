# http package
后端服务分为两类：
1. view first: every Service handle a page's request
  除此之外，标准的blog tech会提供获取基本页面数据的json API
2. 模块服务的接口，每个接口都可能在不同的页面被使用，用于用户自定义扩展，设计自己定制化的博客
	自定义的用户需要申请开发者token，开发者token关联一个blog tech账号